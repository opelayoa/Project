package com.tiendas3b.almacen.receipt.express;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.RecyclerViewCalendarActivity;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.ConfigProvider;
import com.tiendas3b.almacen.db.dao.ExpressProvider;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.receipt.bulk.InBulkReceiptActivity;
import com.tiendas3b.almacen.receipt.express2.ExpressReceiptActivity;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProvidersActivity extends RecyclerViewCalendarActivity {

    private static final String TAG = "ProvidersActivity";
    private static final String MAL_VERSION = "MAL_VERSION";

    private BuyRecyclerViewAdapter adapter;
    private boolean malVersion;

    public static Intent getIntent(Context mContext, boolean malVersion) {
        return new Intent(mContext, ProvidersActivity.class).putExtra(MAL_VERSION, malVersion);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_providers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        malVersion = getIntent().getBooleanExtra(MAL_VERSION, true);

        initDate(savedInstanceState);
        adapter = new BuyRecyclerViewAdapter(this);
        adapter.setMalVersion(malVersion);
        init();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume i:" + items);
        super.onResume();
        adapter.setDb(databaseManager);
        if (items != null) {//TODO de mientras
            createItems();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setAdapter() {
        adapter.setDb(databaseManager);
        adapter.setList(items);
        adapter.notifyDataSetChanged();
//        IDatabaseManager db = new DatabaseManager(mContext);
        adapter.setClickListener((view, position) -> {
            if (databaseManager.isEmpty(VArticle.class)) {
                Toast.makeText(mContext, "Espera o sincroniza info", Toast.LENGTH_LONG).show();
            } else {
                Buy item = (Buy) items.get(position);
                ConfigProvider config = item.getProvider().getMmpConfig();

                if(malVersion){
                    ExpressProvider ep = databaseManager.findExpressProvider(item.getProviderId());

                    if(ep.getAutomatic()){
                        if("G".equals(ep.getDelivery())){
//                            startActivity(new Intent(mContext, InBulkReceiptActivity.class)
//                                    .putExtra(PalletizedReceiptActivity.ODC, item.getFolio()));
                            Toast.makeText(this, "En construcción", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(new Intent(mContext, PalletizedReceiptActivity.class)
                                    .putExtra(PalletizedReceiptActivity.ODC, item.getFolio()));
                        }
                    } else {
                        startActivity(new Intent(mContext, com.tiendas3b.almacen.receipt.express.ExpressReceiptActivity.class)
                                .putExtra(com.tiendas3b.almacen.receipt.express.ExpressReceiptActivity.ODC, item.getFolio()));
                    }
                } else {

                    switch (config.getP2()) {
                        case Constants.MMP_PAL:
                            startActivity(ExpressReceiptActivity.getIntent(mContext, item.getFolio()));
                            break;
                        case Constants.MMP_BULK:
                        case Constants.MMP_MIX:
                            startActivity(new Intent(mContext, InBulkReceiptActivity.class)
                                    .putExtra(PalletizedReceiptActivity.ODC, item.getFolio()));
//                        startActivity(ExpressReceiptActivity.getIntent(mContext, item.getFolio()));
                            break;
                        case Constants.MMP_NOT_IN:
                                //Toast.makeText(this, "No está en REX", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(mContext, InBulkReceiptActivity.class)
                                    .putExtra(PalletizedReceiptActivity.ODC, item.getFolio()));
                            break;
                        default:
                            Toast.makeText(this, "Favor de eportar.", Toast.LENGTH_LONG).show();
                            break;
                    }
                }

            }
        });
        showData();
        viewSwitcher.setDisplayedChild(1);
    }

    @Override
    protected void download() {
        setTitle(DateUtil.getDateStr(selectedDate));
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTimeGsonTime();
        if(malVersion){
            httpService.getBuysExpress(mContext.getRegion(), DateUtil.getDateStr(selectedDate)).enqueue(new Callback<List<Buy>>() {
                @Override
                public void onResponse(Call<List<Buy>> call, Response<List<Buy>> response) {
                    if (response.isSuccessful()) {
                        fill(response.body());
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<List<Buy>> call, Throwable t) {
                    Log.e(TAG, "onFailure");
//                init();
                    showError();
                }
            });
        } else {
            httpService.getBuysExpress2(mContext.getRegion(), DateUtil.getDateStr(selectedDate)).enqueue(new Callback<List<Buy>>() {
                @Override
                public void onResponse(Call<List<Buy>> call, Response<List<Buy>> response) {
                    if (response.isSuccessful()) {
                        fill(response.body());
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<List<Buy>> call, Throwable t) {
                    Log.e(TAG, "onFailure");
//                init();
                    showError();
                }
            });
        }
    }

//    private void downloadDetails() {
//
//    }

    @Override
    protected void createItems() {
        items = databaseManager.listBuysExpress(DateUtil.getDateStr(selectedDate), mContext.getRegion(), Constants.NOT_ARRIVE, Constants.IN_PROCESS);

        if(items != null) {
            Collections.sort(items, new Comparator<Buy>() {
                Date now = DateUtil.getTimeNow();

                @Override
                public int compare(Buy b1, Buy b2) {
                    int res = 0;
                    try {
                        Date t1 = DateUtil.getTime(b1.getTime());
                        Date t2 = DateUtil.getTime(b2.getTime());
//                    Log.e(TAG, "t1:" + t1 + " t2:" + t2 + " n:" + now);
                        if (t1.after(now) && t2.before(now)) {
                            res = -1;
                        } else if (t1.before(now) && t2.after(now)) {
                            res = 1;
                        } else {
                            res = normalOrder(t1, t2, b1, b2);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return res;
                }

                private int normalOrder(Date t1, Date t2, Buy b1, Buy b2) {
                    if (t1.after(t2)) {
                        return 1;
                    } else if (t1.before(t2)) {
                        return -1;
                    } else {
                        if (b1.getPlatform() < b2.getPlatform()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }
            });
        }

//        downloadDetails();

    }

}
