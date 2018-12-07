package com.tiendas3b.almacen.picking.epackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.RecyclerViewCalendarActivity;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.util.DateUtil;

import java.io.Serializable;
import java.util.List;

public class EpackageStoresActivity extends RecyclerViewCalendarActivity {

    private static final String TAG = "EpackageStoresActivity";
    private StoresRecyclerViewAdapter adapter;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseManager = DatabaseManager.getInstance(EpackageStoresActivity.this);
        setContentView(R.layout.activity_epackage_provider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initDate(savedInstanceState);
        adapter = new StoresRecyclerViewAdapter(EpackageStoresActivity.this, databaseManager, DateUtil.getDateStr(selectedDate), new BaseRecyclerViewAdapter.OnViewHolderClick() {
            @Override
            public void onClick(View view, int position) {
                startActivity(new Intent(mContext, PickingEpackageActivity.class).putExtra(PickingEpackageActivity.EXTRA_STORE, (Serializable) items.get(position))
                        .putExtra(com.tiendas3b.almacen.receipt.epackage.EpackageActivity.EXTRA_DATE, DateUtil.getDateStr(selectedDate)));
            }
        });
        init();
        recyclerView.setAdapter(adapter);
//        initRecycler();
    }

//    protected void initRecycler() {
//    }

    @Override
    protected void createItems() {
        String dateStr = DateUtil.getDateStr(selectedDate);
        setTitle(dateStr);
//        adapter.setDb(databaseManager);
        adapter.setDate(dateStr);
//        Object obj = getLastCustomNonConfigurationInstance();
//        if (obj == null) {
//            items = null;
//        } else {
//            Log.i(TAG, "ConfigurationInstance");
//            items = (List<GeneralTravelDTO>) obj;
//        }
        items = databaseManager.listStoresToPick(mContext.getRegion(), DateUtil.getDayOfWeek(selectedDate));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setAdapter() {
        adapter.setList((List<Store>) items);
        adapter.notifyDataSetChanged();
        showData();
        viewSwitcher.setDisplayedChild(1);
    }

    @Override
    protected void download() {
        createItems();
        setAdapter();
        hideProgress();
//        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
//        long regionId = mContext.getRegion();
//        String dateStr = DateUtil.getDateStr(selectedDate);
//        Call<List<EPackage>> s = httpService.getProvidersEpackage(regionId, dateStr);
//        s.enqueue(new Callback<List<EPackage>>() {
//            @Override
//            public void onResponse(Call<List<EPackage>> call, Response<List<EPackage>> response) {
//                if (response.isSuccessful()) {
//                    fill(response.body());
//                } else {
//                    showEmptyView();
//                }
//                hideProgress();
//            }
//
//            @Override
//            public void onFailure(Call<List<EPackage>> call, Throwable t) {
//                showEmptyView();
//                hideProgress();
//                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
//            }
//        });
    }
}
