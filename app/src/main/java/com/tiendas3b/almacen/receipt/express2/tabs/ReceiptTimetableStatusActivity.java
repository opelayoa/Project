package com.tiendas3b.almacen.receipt.express2.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.ConfigProvider;
import com.tiendas3b.almacen.db.dao.StorePickingStatus;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.picking.orders.PickingCaptureActivity;
import com.tiendas3b.almacen.picking.orders.PickingResumeActivity;
import com.tiendas3b.almacen.receipt.bulk.InBulkReceiptActivity;
import com.tiendas3b.almacen.receipt.express.ExpressReceiptActivity;
import com.tiendas3b.almacen.receipt.express.PalletizedReceiptActivity;
import com.tiendas3b.almacen.task.SaveOrReplaceTask;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptTimetableStatusActivity extends AppCompatActivity implements ReceiptStoresFragment.OnFragmentInteractionListener {

    public static final String EXTRA_ORDER_TYPE = "EXTRA_ORDER_TYPE";
    private static final String TAG = "ReceiptTimetableStatus";
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_info:
                showInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        downloadStoreStatus();
        download();
    }

    private void showInfo(){
        String des = "";
        des += "PAL: PALETIZADO" + "\n";
        des += "AGR: A GRANEL";
        Toast.makeText(this, des, Toast.LENGTH_LONG).show();

//        Snackbar.make(clMain, "TEST", Snackbar.LENGTH_LONG).show();
//        final Snackbar sb = Snackbar.make(fragment, des, Snackbar.LENGTH_INDEFINITE);
//        sb.setAction("Aceptar", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sb.dismiss();
//            }
//        });
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

    protected void download() {
//        setTitle(DateUtil.getDateStr(selectedDate));
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTimeGsonTime();

        System.out.println(mContext.getRegion());
        httpService.getBuysExpress2(mContext.getRegion(), DateUtil.getTodayStr()).enqueue(new Callback<List<Buy>>() {
            @Override
            public void onResponse(Call<List<Buy>> call, Response<List<Buy>> response) {
                if (response.isSuccessful()) {
                    System.out.println("");
                    fill(response.body());

                    List<Buy> body = response.body();
                    if (response.isSuccessful() && body != null) {
                        ProgressBar progressBar = findViewById(R.id.progressBar3);
                        progressBar.setVisibility(View.INVISIBLE);
                        new SaveStatusTask(ReceiptTimetableStatusActivity.this, body).execute();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Buy>> call, Throwable t) {
                Log.e(TAG, "onFailure");
//                init();
//                    showError();
            }
        });
    }

    protected <T> void fill(List<Buy> list) {
        if (list == null || list.isEmpty()) {
//            showEmptyView();
//            hideProgress();
        } else {
            save(list);
        }
//        viewSwitcher.setDisplayedChild(1);
    }

    private void save(List<Buy> list) {
//        db.insertOrReplaceInTx(list.toArray(new Buy[list.size()]));
        db.insertOrReplaceInTx(list);
//        items = list;//db.listBuysExpress(DateUtil.getTodayStr(), mContext.getRegion());
    }

//    private void downloadStoreStatus() {
//        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
//        httpService.getStorePickingStatus(mContext.getRegion(), orderTypeId).enqueue(new Callback<List<StorePickingStatus>>() {
//            @Override
//            public void onResponse(Call<List<StorePickingStatus>> call, Response<List<StorePickingStatus>> response) {
//                List<StorePickingStatus> body = response.body();
//                if (response.isSuccessful() && body != null) {
//                    new SaveStatusTask(ReceiptTimetableStatusActivity.this, body).execute();
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<StorePickingStatus>> call, Throwable t) {
//                Log.e(TAG, mContext.getString(R.string.error_connection));
//            }
//        });
//    }

    private static class SaveStatusTask extends SaveOrReplaceTask<Buy>{

        private WeakReference<ReceiptTimetableStatusActivity> activityReference;

        SaveStatusTask(ReceiptTimetableStatusActivity activity, List<Buy> list) {
            super(activity.getApplicationContext(), list);
            activityReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ReceiptTimetableStatusActivity activity = activityReference.get();
            if (activity != null && !activity.isFinishing()) {
                FrameLayout fl = activity.findViewById(R.id.content);
                activity.setTabs();
            }
        }
    }

    private String getTittle() {
        switch (orderTypeId) {
            case 0:
                return "PROVEEDORES DEL DÍA";
            default:
                return "PROVEEDORES DEL DÍA";
        }
    }

    private void setTabs() {
        ReceiptTimetableTabsFragment fragment = new ReceiptTimetableTabsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ReceiptTimetableTabsFragment.TYPE, orderTypeId);
        bundle.putInt(ReceiptTimetableTabsFragment.DAY, selectedDay);
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

    @Override
    public IDatabaseManager getDb() {
        return db;
    }

    @Override
    public BaseRecyclerViewAdapter.OnViewHolderClick getAdapterListener(int status, ReceiptStoreStatusRvAdapter adapter, int type) {
        switch (status){
            case Constants.PICKING_PENDING:

                ConfigProvider config = adapter.getItem(status).getProvider().getMmpConfig();
                switch (config.getP2()) {
                    case Constants.MMP_PAL:
                        return (view, position) -> startActivity(new Intent(mContext, ExpressReceiptActivity.class)
                                .putExtra(PalletizedReceiptActivity.ODC, adapter.getItem(position).getFolio()));
//                            startActivity(ExpressReceiptActivity.getIntent(mContext, item.getFolio()));
//                            break;
                    case Constants.MMP_BULK:
                    case Constants.MMP_MIX:
                        return (view, position) -> startActivity(new Intent(mContext, InBulkReceiptActivity.class)
                                .putExtra(PalletizedReceiptActivity.ODC, adapter.getItem(position).getFolio()));
//                        startActivity(ExpressReceiptActivity.getIntent(mContext, item.getFolio()));
//                            break;
                    case Constants.MMP_NOT_IN:
                        Toast.makeText(this, "No está en REX", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(this, "Favor de eportar.", Toast.LENGTH_LONG).show();
                        break;
                }

//                return (view, position) -> startActivity(new Intent(mContext, PickingCaptureActivity.class)
//                        .putExtra(PickingCaptureActivity.EXTRA_STORE_ID, adapter.getItem(position).getStore().getId())
//                        .putExtra(PickingCaptureActivity.EXTRA_ORDER_TYPE, type)
//                );
//                return (view, position) -> startActivity(new Intent(mContext, PickingResumeActivity.class)
//                        .putExtra(PickingResumeActivity.EXTRA_STORE_ID, adapter.getItem(position).getStore().getId()));
//                        .putExtra(PickingResumeActivity.EXTRA_SCAN_TIME, scanTimeDTO), REQUEST_CODE)
            case Constants.PICKING_IN_PROCESS:
//                Toast.makeText(mContext)
            default:
                break;
        }
        return null;
    }


}
