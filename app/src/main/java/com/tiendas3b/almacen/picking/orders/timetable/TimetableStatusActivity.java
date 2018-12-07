package com.tiendas3b.almacen.picking.orders.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.StorePickingStatus;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.picking.orders.PickingCaptureActivity;
import com.tiendas3b.almacen.picking.orders.PickingResumeActivity;
import com.tiendas3b.almacen.task.SaveOrReplaceTask;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimetableStatusActivity extends AppCompatActivity implements StoresFragment.OnFragmentInteractionListener {

    public static final String EXTRA_ORDER_TYPE = "EXTRA_ORDER_TYPE";
    private static final String TAG = "TimetableStatusActivity";
//    @BindView(R.id.spnDays)
//    Spinner spnDays;
//    @BindView(R.id.pb)
//    ProgressBar progressBar;
    private int selectedDay;
    private int orderTypeId;

    //    @BindView(R.id.switcher)
//    ViewSwitcher viewSwitcher;
//    @BindView(R.id.emptyView)
//    View emptyView;
    protected GlobalState mContext;
    protected IDatabaseManager db;
    protected BaseRecyclerViewAdapter.OnViewHolderClick listener;

//    private int total;
//    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_status);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        downloadStoreStatus();
    }

    private void init() {
//        ButterKnife.bind(this);

        mContext = (GlobalState) getApplicationContext();
        db = new DatabaseManager(mContext);
//        setListener();
        selectedDay = DateUtil.getDayOfWeek();
//        init();
        orderTypeId = getIntent().getIntExtra(EXTRA_ORDER_TYPE, -1);
        setTitle(getTittle());
//        setSpinnerAdapter();
    }

    private void downloadStoreStatus() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.getStorePickingStatus(mContext.getRegion(), orderTypeId).enqueue(new Callback<List<StorePickingStatus>>() {
            @Override
            public void onResponse(Call<List<StorePickingStatus>> call, Response<List<StorePickingStatus>> response) {
                List<StorePickingStatus> body = response.body();
                if (response.isSuccessful() && body != null) {
                    new SaveStatusTask(TimetableStatusActivity.this, body).execute();
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<StorePickingStatus>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_connection));
            }
        });
    }

    private static class SaveStatusTask extends SaveOrReplaceTask<StorePickingStatus>{

        private WeakReference<TimetableStatusActivity> activityReference;

        SaveStatusTask(TimetableStatusActivity activity, List<StorePickingStatus> list) {
            super(activity.getApplicationContext(), list);
            activityReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TimetableStatusActivity activity = activityReference.get();
            if (activity != null && !activity.isFinishing()) {
                FrameLayout fl = activity.findViewById(R.id.content);
                activity.setTabs();
            }
        }
    }

    private String getTittle() {
        switch (orderTypeId) {
            case 4:
                return "Circuito R";
            case 123:
                return "Circuito CF";
            default:
                return "Picking ";
        }
    }

    private void setTabs() {
        TimetableTabsFragment fragment = new TimetableTabsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TimetableTabsFragment.TYPE, orderTypeId);
        bundle.putInt(TimetableTabsFragment.DAY, selectedDay);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment).commit();
    }

    @Override
    protected void onRestart() {

        db = new DatabaseManager(this);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        if (db != null) db.closeDbConnections();
        super.onStop();
    }

    @Override
    protected void onResume() {
        db = DatabaseManager.getInstance(this);
        super.onResume();
    }
////////////////offline
//    private void downloadOrders() {
//        long regionId = mContext.getRegion();
//        List<Store> storesForToday = db.listStoresToPick(regionId, selectedDay);
//        total = storesForToday.size();
//        progressBar.setMax(total);
//        count = 0;
//        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
//        for (Store s : storesForToday) {
//            httpService.getOrders(regionId, s.getId(), orderTypeId).enqueue(new Callback<List<OrderPicking>>() {
//                @Override
//                public void onResponse(Call<List<OrderPicking>> call, Response<List<OrderPicking>> response) {
//                    List<OrderPicking> body = response.body();
//                    if (response.isSuccessful() && body != null) {
//                        backTask(body, s.getId());
//                    } else {
//                        count++;
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<List<OrderPicking>> call, Throwable t) {
//                    count++;
//                    Log.e(TAG, mContext.getString(R.string.error_connection));
//                }
//            });
//        }
//    }
//
//    private void backTask(final List<OrderPicking> list, Long storeId) {
//        new TimetableStatusActivity.SaveTask(list, storeId).execute();
//    }
//
//    @SuppressLint("StaticFieldLeak")
//    class SaveTask extends AsyncTask<Void, Void, Void> {
//
//        private final List<OrderPicking> list;
//        private final long storeId;
//
//        SaveTask(List<OrderPicking> list, long storeId) {
//            this.list = list;
//            this.storeId = storeId;
//        }
//
//        @Override
//        protected Void doInBackground(Void[] params) {
//            save(list, storeId);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void o) {
//            count++;
//            if(count == total){
//                setTabs();
//            }
//            progressBar.setProgress(count);
//
//        }
//    }
//
//    private void save(@NonNull List<OrderPicking> list, long storeId) {
//
////        db.deleteOrderBy(storeId);//
//        ArrayList<OrderPickingCapture> orderPickingCaptureList = new ArrayList<>();
//        long regionId = mContext.getRegion();
//        for (OrderPicking o : list) {
//            long id = db.insert(o);
//            List<OrderDetail> details = o.getDetails();
//            for (OrderDetail d : details) {
//                int iclave = d.getIclave();
////                Log.d(TAG, "iclave=" + iclave);
//                long articleId = db.findViewArticleIdForPicking(iclave);
//                d.setOrderId(id);
//                d.setArticleId(articleId);
//                db.insert(d);//or replace
//            }
//
//            OrderPickingCapture orderPickingCapture = new OrderPickingCapture();
//            orderPickingCapture.setPaybill(o.getPaybill());
//            orderPickingCapture.setStoreId(storeId);
//            orderPickingCapture.setRegionId(regionId);
//            orderPickingCaptureList.add(orderPickingCapture);
//        }
//        db.insertOrReplaceInTx(orderPickingCaptureList.toArray(new OrderPickingCapture[list.size()]));
//    }
//////////// offline

//    private void setSpinnerAdapter() {
//        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(mContext, R.array.days_of_week, R.layout.simple_spinner_item_t3b);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spnDays.setAdapter(spinnerAdapter);
//        spnDays.setSelection(selectedDay - 1);
//        spnDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedDay = position + 1;
//                changeDay();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }

//    private void changeDay() {
////        items = getStoresList();
////        if(items == null){
////
////        } else {
////            if(adapter == null){
////                try {
////                    Crashlytics.log("adapter == null mdf!");
////                    Crashlytics.logException(new Throwable());
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////            } else {
////                adapter.setList(items);
////                adapter.notifyDataSetChanged();
////            }
////        }
//    }

    @Override
    public IDatabaseManager getDb() {
        return db;
    }

    @Override
    public BaseRecyclerViewAdapter.OnViewHolderClick getAdapterListener(int status, StoreStatusRvAdapter adapter, int type) {
        switch (status){
            case Constants.PICKING_PENDING:
                return (view, position) -> startActivity(new Intent(mContext, PickingCaptureActivity.class)
                                .putExtra(PickingCaptureActivity.EXTRA_STORE_ID, adapter.getItem(position).getStore().getId())
                                .putExtra(PickingCaptureActivity.EXTRA_ORDER_TYPE, type)
                );
            case Constants.PICKING_COMPLETE:
                return (view, position) -> startActivity(new Intent(mContext, PickingResumeActivity.class)
                        .putExtra(PickingResumeActivity.EXTRA_STORE_ID, adapter.getItem(position).getStore().getId()));
//                        .putExtra(PickingResumeActivity.EXTRA_SCAN_TIME, scanTimeDTO), REQUEST_CODE)
            case Constants.PICKING_IN_PROCESS:
                default:
                break;
        }
        return null;
    }


}
