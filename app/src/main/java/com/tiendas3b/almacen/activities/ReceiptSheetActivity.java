package com.tiendas3b.almacen.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.ReceiptSheetTableDataAdapter;
import com.tiendas3b.almacen.db.dao.ReceiptSheet;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.NetworkUtil;
import com.tiendas3b.almacen.views.GeneralSwipeRefreshLayout;
import com.tiendas3b.almacen.views.tables.ReceiptSheetTableView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptSheetActivity extends AppCompatActivity {

    private static final String TAG = ReceiptSheetActivity.class.getSimpleName();
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private ViewSwitcher viewSwitcher;
    private List<ReceiptSheet> items;
    private ReceiptSheetTableView tableView;
    private GeneralSwipeRefreshLayout swipeRefresh;
    private String mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (GlobalState) getApplicationContext();
        databaseManager = new DatabaseManager(mContext);
        setContentView(R.layout.activity_receipt_sheet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        init();
        super.onResume();
    }

    private void init() {
        Date date = new Date();
        setTitle(getTitle() + " " + new SimpleDateFormat(Constants.DATE_FORMAT_SHORT, Locale.getDefault()).format(date));
        mDate = new SimpleDateFormat(DateUtil.YYYY_MM_DD, Locale.getDefault()).format(date);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher);
        swipeRefresh = (GeneralSwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                download();
            }
        });

        if (items == null) {
            getDatabaseInfo();
            if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
                download();
            }
        }

    }

    private void getDatabaseInfo() {
        items = databaseManager.listReceiptSheets(mDate, mContext.getRegion());
        if (items.isEmpty()) {
            swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
                @Override
                public boolean canChildScrollUp() {
                    return false;
                }
            });
        } else {
            setAdapter();
            viewSwitcher.setDisplayedChild(1);
        }
    }

    private void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getReceiptSheets(mContext.getRegion(), mDate).enqueue(new Callback<List<ReceiptSheet>>() {
            @Override
            public void onResponse(Call<List<ReceiptSheet>> call, Response<List<ReceiptSheet>> response) {
                if (response.isSuccessful()) {
                    fill(response.body());
                } else {
                    showEmptyView();
                }
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<ReceiptSheet>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                showEmptyView();
                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });
    }

    private void showEmptyView() {
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.GONE);
    }

    private void fill(List<ReceiptSheet> list) {
        if (list == null) {
            showEmptyView();
        } else {
            save(list);
            setAdapter();
        }
        viewSwitcher.setDisplayedChild(1);
    }

    private void setAdapter() {
        tableView = (ReceiptSheetTableView) findViewById(R.id.list);
        TableDataAdapter<ReceiptSheet> adapter = tableView.getDataAdapter();
//        if(adapter == null) {
            adapter = new ReceiptSheetTableDataAdapter(this, items);
//        } else {
////            adapter = ((ReceiptSheetTableDataAdapter)adapter);
//            adapter.getData().clear();
//            adapter.addAll(items);
//            adapter.notifyDataSetChanged();
//        }
        tableView.setDataAdapter(adapter);
        tableView.addDataClickListener(new ClickListener());
        swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
            @Override
            public boolean canChildScrollUp() {
                return tableView.getChildAt(1).canScrollVertically(-1);
            }
        });
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.VISIBLE);
    }

    private void save(List<ReceiptSheet> list) {
        databaseManager.insertOrReplaceInTx(list.toArray(new ReceiptSheet[list.size()]));
        items = databaseManager.listReceiptSheets(mDate, mContext.getRegion());
    }

    private class ClickListener implements TableDataClickListener<ReceiptSheet> {

        @Override
        public void onDataClicked(final int rowIndex, final ReceiptSheet clickedData) {
//            final String carString = clickedData.getArriveTime() + " " + clickedData.getDeliveryTime();
//            Toast.makeText(ReceiptSheetActivity.this, carString, Toast.LENGTH_SHORT).show();

        }
    }
}
