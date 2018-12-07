package com.tiendas3b.almacen.shipment.epackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.RecyclerViewCalendarActivity;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.shipment.epackage.ShipmentEpackageActivity;
import com.tiendas3b.almacen.shipment.epackage.StoresRecyclerViewAdapter;
import com.tiendas3b.almacen.util.DateUtil;

import java.io.Serializable;
import java.util.List;

public class EpackageStoresActivity extends RecyclerViewCalendarActivity {

    private static final String TAG = "ShipmentEpackageActivity";
    private StoresRecyclerViewAdapter adapter;

    @Override
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
                startActivity(new Intent(mContext, ShipmentEpackageActivity.class).putExtra(ShipmentEpackageActivity.EXTRA_STORE, (Serializable) items.get(position))
                        .putExtra(com.tiendas3b.almacen.shipment.epackage.EpackageActivity.EXTRA_DATE, DateUtil.getDateStr(selectedDate)));
            }
        });
        init();
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void createItems() {
        String dateStr = DateUtil.getDateStr(selectedDate);
        setTitle(dateStr);
        adapter.setDate(dateStr);
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
    }

}
