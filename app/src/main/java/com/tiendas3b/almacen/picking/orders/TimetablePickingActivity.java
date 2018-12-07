package com.tiendas3b.almacen.picking.orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.StoresListActivity;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.util.DateUtil;

import java.util.List;

import butterknife.BindView;

public class TimetablePickingActivity extends StoresListActivity {

    public static final String EXTRA_ORDER_TYPE = "EXTRA_ORDER_TYPE";
    private static final String TAG = "TimetablePickingAct";
    @BindView(R.id.spnDays)
    Spinner spnDays;
    private int selectedDay;
    private int orderTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_picking);
        mContext = (GlobalState) getApplicationContext();
        setListener();
        selectedDay = DateUtil.getDayOfWeek();
        init();
        orderTypeId = getIntent().getIntExtra(EXTRA_ORDER_TYPE, -1);
        setSpinnerAdapter();
//        downloadOrders();
    }

    private void setSpinnerAdapter() {
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(mContext, R.array.days_of_week, R.layout.simple_spinner_item_t3b);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDays.setAdapter(spinnerAdapter);
        spnDays.setSelection(selectedDay - 1);
        spnDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDay = position + 1;
                changeDay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void changeDay() {
        items = getStoresList();
        if(items == null){

        } else {
            if(adapter == null){
                try {
                    Crashlytics.log("adapter == null mdf!");
                    Crashlytics.logException(new Throwable());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                adapter.setList(items);
                adapter.notifyDataSetChanged();
            }
        }
    }

//    private void downloadOrders() {
//        StringBuffer stores = new StringBuffer();
//        for(Store s : items){
//            stores.append(s.getId()).append(",");
//        }
//        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
//        httpService.getOnlyOrders(mContext.getRegion(), stores.toString()).enqueue(new Callback<List<OrderPicking>>() {
//            @Override
//            public void onResponse(Call<List<OrderPicking>> call, Response<List<OrderPicking>> response) {
//                if (response.isSuccessful()) {
//                    fillList(response.body());
//                } else {
//                }
////                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFailure(Call<List<OrderPicking>> call, Throwable t) {
////                Log.e(TAG, mContext.getString(R.string.error_connection));
////                Snackbar.make(viewSwitcher, "Error", Snackbar.LENGTH_INDEFINITE);
//            }
//        });
//    }

//    private void fillList(List<OrderPicking> list) {
//        if (list == null) {
////            lblFolios.setText("No hay folios");
//        } else {
//            save(list);
////            items = db.listOrderDetails(storeId);
//        }
//    }
//
//    private void save(@NonNull List<OrderPicking> list) {
//        db.deleteAllOrders();
//        List<OrderPickingCapture> orderPickingCaptureList = new ArrayList<>();
//        long regionId = mContext.getRegion();
//        for (OrderPicking o : list) {
//            db.insert(o);
//            OrderPickingCapture orderPickingCapture = new OrderPickingCapture();
//            orderPickingCapture.setPaybill(o.getPaybill());
//            orderPickingCapture.setStoreId(o.getStoreId());
//            orderPickingCapture.setRegionId(regionId);
//            orderPickingCaptureList.add(orderPickingCapture);
//        }
//        db.insertOrReplaceInTx(orderPickingCaptureList.toArray(new OrderPickingCapture[list.size()]));
//    }

    private void setListener() {
        listener = (view, position) -> mContext.startActivity(new Intent(mContext, PickingCaptureActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(PickingCaptureActivity.EXTRA_STORE_ID, adapter.getItem(position).getId())
                .putExtra(PickingCaptureActivity.EXTRA_ORDER_TYPE, orderTypeId)
        );
    }

    protected List<Store> getStoresList() {
        return db.listStoresToPick(mContext.getRegion(), selectedDay);
    }
}
