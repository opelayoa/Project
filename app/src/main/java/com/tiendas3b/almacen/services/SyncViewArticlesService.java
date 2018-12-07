package com.tiendas3b.almacen.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.Preferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncViewArticlesService extends IntentService {

    private static final String TAG = "SyncViewArticlesService";
    private Tiendas3bClient httpService;
    private IDatabaseManager databaseManager;
    private CountDownLatch doneSignal;
    private static final int TOTAL = 1;
    private GlobalState mContext;

    public SyncViewArticlesService() {
        super("SyncViewArticlesService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            mContext = (GlobalState) getApplicationContext();
            databaseManager = new DatabaseManager(mContext);
            httpService = mContext.getHttpServiceWithAuthTime();

            doneSignal = new CountDownLatch(TOTAL);//num catalogs

            downloadViewArticleCat(intent);

            try {
                doneSignal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        databaseManager.closeDbConnections();
        super.onDestroy();
    }

    private void downloadViewArticleCat(final Intent intent) {
//        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<VArticle>> call = httpService.getViewArticles();
        call.enqueue(new Callback<List<VArticle>>() {
            @Override
            public void onResponse(Call<List<VArticle>> call, Response<List<VArticle>> response) {
                if (response.isSuccessful()) {
                    List<VArticle> list = response.body();
                    databaseManager.insertOrReplaceInTx(list.toArray(new VArticle[list.size()]));
                    Preferences p = new Preferences(mContext);
                    p.setSharedStringSafe(Preferences.KEY_LAST_UPDATE, new SimpleDateFormat(DateUtil.DD_MM_YYYY_HH_MM, Locale.getDefault()).format(new Date()));
                    sendResponseToActivity(intent);
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<VArticle>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                sendResponseToActivity(intent);
                doneSignal.countDown();
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void sendResponseToActivity(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Messenger messenger = (Messenger) bundle.get("messenger");
            Message msg = Message.obtain();
//            msg.setData(bundle); //put the data here
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                Log.i("error", "error");
            }
        }
    }

}
