package com.tiendas3b.almacen.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.ReceiptSheetDetailsTableDataAdapter;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetail;
import com.tiendas3b.almacen.db.dao.TimetableReceipt;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.NetworkUtil;
import com.tiendas3b.almacen.views.tables.ReceiptsheetDetailTableView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptSheetDetailActivity extends AppCompatActivity {

    private static final String TAG = DecreaseCaptureActivity.class.getSimpleName();
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private ViewSwitcher viewSwitcher;
    private List<ReceiptSheetDetail> items;
    private TimetableReceipt item;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (GlobalState) getApplicationContext();
        databaseManager = new DatabaseManager(mContext);
        setContentView(R.layout.activity_receipt_sheet_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    @Override
    protected void onRestart() {
        databaseManager = new DatabaseManager(this);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onStop();
    }

    @Override
    protected void onResume() {
        databaseManager = DatabaseManager.getInstance(this);
        super.onResume();
    }


    @SuppressWarnings("unchecked")
    private void init() {
        viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher);
        Long buyId = getIntent().getLongExtra(Constants.EXTRA_RECEIP_SHEET, 0L);
        String provider = getIntent().getStringExtra(Constants.EXTRA_PROVIDER_NAME);
        setTitle(provider);
        item = databaseManager.getById(buyId, TimetableReceipt.class);

        if (items == null) {
            getDatabaseInfo();
            if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
                download();
            }
        }

    }

    private void getDatabaseInfo() {
        items = databaseManager.listReceiptSheetDetails(item.getId());
        if (items.isEmpty()) {
//            swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
//                @Override
//                public boolean canChildScrollUp() {
//                    return false;
//                }
//            });
        } else {
            setAdapter();
            viewSwitcher.setDisplayedChild(1);
        }
    }

    private void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getReceiptSheetDetails(mContext.getRegion(), item.getDateTime(), item.getFolio().toString()).enqueue(new Callback<List<ReceiptSheetDetail>>() {
            @Override
            public void onResponse(Call<List<ReceiptSheetDetail>> call, Response<List<ReceiptSheetDetail>> response) {
                if (response.isSuccessful()) {
                    fill(response.body());
                } else {
                    showEmptyView();
                }
//                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<ReceiptSheetDetail>> call, Throwable t) {
//                swipeRefresh.setRefreshing(false);
                showEmptyView();
                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });
    }

    private void showEmptyView() {
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.GONE);
    }

    private void fill(List<ReceiptSheetDetail> list) {
        if (list == null) {
            showEmptyView();
        } else {
            save(list);
            setAdapter();
        }
        viewSwitcher.setDisplayedChild(1);
    }

    @SuppressWarnings("ConstantConditions")
    private void setAdapter() {
        ReceiptsheetDetailTableView tableView = (ReceiptsheetDetailTableView) findViewById(R.id.list);
//        types = db.listObservationType(4);
//        obs = db.listObservationType(0);
        ReceiptSheetDetailsTableDataAdapter adapter = new ReceiptSheetDetailsTableDataAdapter(this, new ArrayList<>(items), items);
//        swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
//            @Override
//            public boolean canChildScrollUp() {
//                return tableView.getChildAt(1).canScrollVertically(-1);
//            }
//        });
        tableView.setDataAdapter(adapter);
//        tableView.addDataClickListener(new ClickListener());
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.VISIBLE);
    }

    private void save(List<ReceiptSheetDetail> list) {
        Long id = item.getId();
        for (ReceiptSheetDetail rsd : list) {
            rsd.setTimetableReceiptId(id);
        }
        databaseManager.insertOrReplaceInTx(list.toArray(new ReceiptSheetDetail[list.size()]));
        items = databaseManager.listReceiptSheetDetails(id);
    }

}
