package com.tiendas3b.almacen.receipt.edit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.task.SaveOrReplaceTask;
import com.tiendas3b.almacen.util.DateUtil;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptEditTimetableActivity extends AppCompatActivity implements ReceiptEditFragment.OnFragmentInteractionListener {

    public static final String EXTRA_ORDER_TYPE = "EXTRA_ORDER_TYPE";
    private static final String TAG = "ReceiptTimetableStatus";
    private int selectedDay;
    private int orderTypeId;
    protected GlobalState mContext;
    protected IDatabaseManager db;
    protected BaseRecyclerViewAdapter.OnViewHolderClick listener;


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
        // Handle presses on the action bar items -
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
        download();
    }

    private void showInfo(){
        String des = "";
        des += "PAL: PALETIZADO" + "\n";
        des += "AGR: A GRANEL";
        Toast.makeText(this, des, Toast.LENGTH_LONG).show();
    }

    private void init() {
        mContext = (GlobalState) getApplicationContext();
        db = new DatabaseManager(mContext);
        selectedDay = DateUtil.getDayOfWeek();
        orderTypeId = getIntent().getIntExtra(EXTRA_ORDER_TYPE, -1);
        setTitle(getTittle());
    }

    protected void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTimeGsonTime();
        httpService.getBuysExpress2(mContext.getRegion(), DateUtil.getTodayStr()).enqueue(new Callback<List<Buy>>() {
            @Override
            public void onResponse(Call<List<Buy>> call, Response<List<Buy>> response) {
                if (response.isSuccessful()) {
                    fill(response.body());
                    List<Buy> body = response.body();
                    if (response.isSuccessful() && body != null) {
                        ProgressBar progressBar = findViewById(R.id.progressBar3);
                        progressBar.setVisibility(View.INVISIBLE);
                        new SaveStatusTask(ReceiptEditTimetableActivity.this, body).execute();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Buy>> call, Throwable t) {
                Log.e(TAG, "onFailure");
            }
        });
    }

    protected <T> void fill(List<Buy> list) {
        if (list == null || list.isEmpty()) {
            Toast.makeText(mContext, "No Existen Datos", Toast.LENGTH_LONG).show();
        } else {
            save(list);
        }
    }

    private void save(List<Buy> list) {
        db.insertOrReplaceInTx(list);
    }

    private static class SaveStatusTask extends SaveOrReplaceTask<Buy>{

        private WeakReference<ReceiptEditTimetableActivity> activityReference;

        SaveStatusTask(ReceiptEditTimetableActivity activity, List<Buy> list) {
            super(activity.getApplicationContext(), list);
            activityReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ReceiptEditTimetableActivity activity = activityReference.get();
            if (activity != null && !activity.isFinishing()) {
                FrameLayout fl = activity.findViewById(R.id.content);
                activity.setTabs();
            }
        }
    }

    private String getTittle() {
        switch (orderTypeId) {
            case 0:
                return "EDICIÓN RECIBO EXPRÉS";
            default:
                return "EDICIÓN RECIBO EXPRÉS";
        }
    }

    private void setTabs() {
        ReceiptEditTimetableTabsFragment fragment = new ReceiptEditTimetableTabsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ReceiptEditTimetableTabsFragment.TYPE, orderTypeId);
        bundle.putInt(ReceiptEditTimetableTabsFragment.DAY, selectedDay);
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
    public BaseRecyclerViewAdapter.OnViewHolderClick getAdapterListener(int status, ReceiptEditStatusRvAdapter adapter, int type) {
//        switch (status){
//            case Constants.PICKING_PENDING:
//
//                ConfigProvider config = adapter.getItem(status).getProvider().getMmpConfig();
//                switch (config.getP2()) {
//                    case Constants.MMP_PAL:
//                        return (view, position) -> startActivity(new Intent(mContext, ExpressReceiptActivity.class)
//                                .putExtra(PalletizedReceiptActivity.ODC, adapter.getItem(position).getFolio()));
////                            startActivity(ExpressReceiptActivity.getIntent(mContext, item.getFolio()));
////                            break;
//                    case Constants.MMP_BULK:
//                    case Constants.MMP_MIX:
//                        return (view, position) -> startActivity(new Intent(mContext, InBulkReceiptActivity.class)
//                                .putExtra(PalletizedReceiptActivity.ODC, adapter.getItem(position).getFolio()));
////                        startActivity(ExpressReceiptActivity.getIntent(mContext, item.getFolio()));
////                            break;
//                    case Constants.MMP_NOT_IN:
//                        Toast.makeText(this, "No está en REX", Toast.LENGTH_LONG).show();
//                        break;
//                    default:
//                        Toast.makeText(this, "Favor de eportar.", Toast.LENGTH_LONG).show();
//                        break;
//                }
//
////                return (view, position) -> startActivity(new Intent(mContext, PickingCaptureActivity.class)
////                        .putExtra(PickingCaptureActivity.EXTRA_STORE_ID, adapter.getItem(position).getStore().getId())
////                        .putExtra(PickingCaptureActivity.EXTRA_ORDER_TYPE, type)
////                );
////                return (view, position) -> startActivity(new Intent(mContext, PickingResumeActivity.class)
////                        .putExtra(PickingResumeActivity.EXTRA_STORE_ID, adapter.getItem(position).getStore().getId()));
////                        .putExtra(PickingResumeActivity.EXTRA_SCAN_TIME, scanTimeDTO), REQUEST_CODE)
//            case Constants.PICKING_IN_PROCESS:
////                Toast.makeText(mContext)
//            default:
//                break;
//        }
        return null;
    }


}
