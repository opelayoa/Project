package com.tiendas3b.almacen.picking.orders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.ScannerActivity;
import com.tiendas3b.almacen.db.dao.OrderDetail;
import com.tiendas3b.almacen.db.dao.OrderDetailCapture;
import com.tiendas3b.almacen.db.dao.OrderPicking;
import com.tiendas3b.almacen.db.dao.OrderPickingCapture;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;

import net.gotev.speech.Speech;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickingCaptureActivity extends ScannerActivity implements ArticleToPickingFragment.OnFragmentInteractionListener {

    public static final String EXTRA_STORE_ID = "s";
    public static final String EXTRA_ORDER_TYPE = "EXTRA_ORDER_TYPE";
    private static final String TAG = "PickingCaptureActivity";
    private static final int REQUEST_CODE = 35;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    @BindView(R.id.vpgArticles)
    ViewPager mViewPager;
    @BindView(R.id.viewSwitcher)
    ViewSwitcher viewSwitcher;
    @BindView(R.id.lblFolios)
    TextView lblFolios;
    @BindView(R.id.txtBarcode)
    EditText txtBarcode;
    @BindView(R.id.chrTime)
    Chronometer chrTime;
    //    @BindView(R.id.btnInit)
//    Button btnInit;
//    @BindView(R.id.btnFinish)
//    Button btnFinish;
//    private Menu menu;
//    private MenuItem btnSave;
//    @BindView(R.id.progress)
//    ProgressBar progressBar;
    private GlobalState mContext;
    private long storeId;
    protected IDatabaseManager db;
    private List<OrderDetail> items;
    private List<OrderPickingCapture> orderPickingCaptureList;
    private PickingScanTimeDTO scanTimeDTO;
    private int orderTypeId;
    private boolean voiceActive;


//    private CameraSourcePreview mPreview;
//    private CameraSource mCameraSource;
//    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picking_capture);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        init();


//        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
//        mGraphicOverlay = (GraphicOverlay<BarcodeGraphic>) findViewById(R.id.graphicOverlay);
//        boolean autoFocus = true;
//        boolean useFlash = false;
//        createCameraSource(autoFocus, useFlash);
    }

    @Override
    protected void processBarcode(String barcode) {

        Log.d(TAG, "processBarcode");
        if(barcode != null) {
            mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem()).add(barcode);
        }
        requestFocusScan();
        txtBarcode.setText("");
    }

    @Override
    protected void clearTxt() {
//        txtBarcode.setText("");
    }

    @Override
    protected void onRestart() {
        Log.e(TAG, "onRestart");
        db = new DatabaseManager(this);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop");
        //no se todavia si poner los items = null...
        if (db != null) db.closeDbConnections();
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume");
        db = DatabaseManager.getInstance(this);
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initChronometer();
    }

    @Override
    protected void onDestroy() {
//        voiceShutdown();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (items == null) {
            PickingCaptureActivity.super.onBackPressed();
        } else {
            showWarningDialog();
        }
    }

    private void showWarningDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.dialog_warning));
        alertDialogBuilder.setPositiveButton(getString(android.R.string.ok),
                (dialog, which) -> PickingCaptureActivity.super.onBackPressed());
        alertDialogBuilder.setNegativeButton(android.R.string.cancel, null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void init() {
        ButterKnife.bind(this);
        mContext = (GlobalState) getApplicationContext();

//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
//        voiceActive = pref.getBoolean(Preferences.KEY_VOICE, false);
//        if (voiceActive) {
//            Speech.init(mContext, mContext.getPackageName());
////            Logger.setLogLevel(Logger.LogLevel.DEBUG);
//        }

//        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Intent intent = getIntent();
        storeId = intent.getLongExtra(EXTRA_STORE_ID, -1L);
        orderTypeId = intent.getIntExtra(EXTRA_ORDER_TYPE, -1);
        db = new DatabaseManager(this);
        Store store = db.getById(storeId, Store.class);
        if (store == null) {
            Toast.makeText(mContext, "Sincroniza información", Toast.LENGTH_LONG).show();
        } else {
            setTitle(store.getName());
            getOrders();
        }
    }

    private void getOrders() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        System.out.println(mContext.getRegion());
        httpService.getOrders(mContext.getRegion(), storeId, orderTypeId).enqueue(new Callback<List<OrderPicking>>() {
            @Override
            public void onResponse(Call<List<OrderPicking>> call, Response<List<OrderPicking>> response) {
                if (response.isSuccessful()) {
                    fillList(response.body());
                } else {
                }
//                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<OrderPicking>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_connection));
                chrTime.stop();
                lblFolios.setText(R.string.error_connection);
                viewSwitcher.setDisplayedChild(1);
//                Snackbar.make(viewSwitcher, "Error", Snackbar.LENGTH_INDEFINITE);
            }
        });
    }

    private void fillList(List<OrderPicking> list) {
        if (list == null) {
            lblFolios.setText("No hay folios");
            chrTime.setVisibility(View.GONE);
            chrTime.stop();
            viewSwitcher.setDisplayedChild(1);
        } else {
            backTask(list);
//            items = db.listOrderDetails(storeId);
//            setAdapter();
        }
    }

    private void backTask(final List<OrderPicking> list) {
        new SaveTask(list).execute();
    }

    @SuppressLint("StaticFieldLeak")
    class SaveTask extends AsyncTask<Void, Void, Void> {

        private final List<OrderPicking> list;

        SaveTask(List<OrderPicking> list) {
            this.list = list;
        }

        @Override
        protected Void doInBackground(Void[] params) {
            save(list);
            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            showPaybillDialog();
        }
    }

    private void showPaybillDialog() {
        final List<OrderPicking> orders = db.listOrders(storeId);
//        if(orders.size() == 1){
        CharSequence[] paybills = new CharSequence[orders.size()];
//            boolean[] selectedItemsArr = new boolean[orders.size()];
        int i = 0;
        final List<Integer> selectedItems = new ArrayList<>();
        for (OrderPicking o : orders) {
            int paybill = o.getPaybill();
            paybills[i] = String.valueOf(paybill).concat(getTypeAlias(o.getType()));
            selectedItems.add(i++);//si descomentas abajo quita el ++
//                selectedItemsArr[i++] = false;
        }


//            DialogUtil.showCheckBoxes(PickingCaptureActivity.this, "Folios a pickear", paybills, selectedItemsArr, (dialog, indexSelected, isChecked) -> {
//                if (isChecked) {
//                    selectedItems.add(indexSelected);
//                } else if (selectedItems.contains(indexSelected)) {
//                    selectedItems.remove(Integer.valueOf(indexSelected));
//                }
//            }, (dialog, which) -> {
        int size = selectedItems.size();
//                if (size > 0) {
        StringBuilder folios = new StringBuilder("Folios: ");
        int i1 = 0;
        Integer[] selectedPaybills = new Integer[size];
        for (Integer pos : selectedItems) {
            int p = orders.get(pos).getPaybill();
            folios.append(p).append(", ");
            selectedPaybills[i1++] = p;
        }
        lblFolios.setText(folios.toString());
        db.deleteOrderBy(selectedPaybills);
        items = db.listOrderDetails(selectedPaybills);
        setAdapter();
        viewSwitcher.setDisplayedChild(1);
////                initChronometer();
//                } else {
//                    PickingCaptureActivity.super.onBackPressed();
//                }
//            }, (dialog, which) -> PickingCaptureActivity.super.onBackPressed());
//        } else{
//            Toast.makeText(mContext, "Hay más de un pedido. Hacer manual", Toast.LENGTH_LONG).show();
//            finish();
//        }

    }

    private String getTypeAlias(int type) {
        String alias;
        if (type == 2) {
            alias = "F";
        } else {
            alias = db.findOrderTypeByType(mContext.getRegion(), type);
        }
        return alias;
    }

    private void initChronometer() {
        chrTime.start();
        scanTimeDTO = new PickingScanTimeDTO();
        long now = new Date().getTime();
        System.out.println("now: " + now);
        scanTimeDTO.setInitTimeMillis(now);
        scanTimeDTO.setInitTime(DateUtil.getDateStr(now) + " " + DateUtil.getTimeNowStr());
    }

    private void setAdapter() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        int size = items.size();
        Log.i(TAG, "size:" + size);
        int i = 0;
        for (OrderDetail d : items) {
//            mSectionsPagerAdapter.addFragment(ArticleToPickingFragment.newInstance(d, size, i++, idOrderCapture));
            mSectionsPagerAdapter.addFragment(ArticleToPickingFragment.newInstance(d.getId(), size, i++));
        }
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setEnabled(false);

        requestFocusScan();
    }

    private void save(@NonNull List<OrderPicking> list) {

        db.deleteOrderBy(storeId);//
        orderPickingCaptureList = new ArrayList<>();
        long regionId = mContext.getRegion();

//        if (list.size() > 1){
//            //
//        } else {
        for (OrderPicking o : list) {
            long id = db.insert(o);
            List<OrderDetail> details = o.getDetails();
            for (OrderDetail d : details) {
                int iclave = d.getIclave();
                Log.i(TAG, "iclave=" + iclave);
                long articleId = db.findViewArticleIdForPicking(iclave);
//                    if (articleId == -1) {
//                        Log.e(TAG, "articleId == -1 wtf!");
//                        List<VArticle> articles = db.findViewArticleByFatherIclave(iclave);
//                        for (VArticle a : articles) {
//                            OrderDetail child = new OrderDetail();
//                            child.setQuantity(d.getQuantity());
//                            child.setExistence(0);
//                            child.setArticleId(a.getId());
//                            child.setType(d.getType());
//                            child.setOrderId(id);
//                            child.setIclave((int) a.getIclave());
//                            child.setChild(true);
//                            child.setFatherIclave(iclave);
//                            Log.w(TAG, "child:"+ a.getIclave());
//                            db.insert(child);
//                        }
//                    } else {
//                Log.i(TAG, "exist=" + d.getExistence());
                d.setOrderId(id);
//                        d.setChild(false);
                d.setArticleId(articleId);
//                    try {
                db.insert(d);
//                    } catch (SQLiteConstraintException e){
//                        Log.w(TAG, "hijo:" + d.getIclave());
//                        e.printStackTrace();
//                    }
////                db.insertOrUpdate(d, OrderDetailDao.Properties.Iclave.eq(d.getIclave()), OrderDetailDao.Properties.OrderId.eq(id));
//                    }
            }

            OrderPickingCapture orderPickingCapture = new OrderPickingCapture();
            orderPickingCapture.setPaybill(o.getPaybill());
            orderPickingCapture.setStoreId(storeId);
            orderPickingCapture.setRegionId(regionId);
            orderPickingCaptureList.add(orderPickingCapture);
        }
        db.insertOrReplaceInTx(orderPickingCaptureList.toArray(new OrderPickingCapture[list.size()]));
//        db.insertOrUpdateWithDetails(list, OrderDetail.class);//algun dia
//        }
    }

    public void moveNextPage(OrderDetailCapture d, long orderId) {
        Log.e(TAG, "moveNextPage");
        OrderPicking o = db.getById(orderId, OrderPicking.class);
        OrderPickingCapture capture = db.findOrderCapture(o.getRegionId(), o.getStoreId(), o.getPaybill());
        d.setOrderCaptureId(capture.getId());
        try {
            db.insert(d);
        } catch (SQLiteConstraintException e) {
            Snackbar.make(mViewPager, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
        }
        int current = mViewPager.getCurrentItem();
        Log.e(TAG, "setCurrentItem:" + current);
        if (current == items.size() - 1) {
            showResume();
        } else {
            new Handler().post(() -> mViewPager.setCurrentItem(current + 1));
        }
    }

    @Override
    public void requestFocusScan() {
        txtBarcode.requestFocus();
    }

    @Override
    public boolean isVoiceActive() {
        return voiceActive;
    }

    @Override
    public List<OrderDetailCapture> findOrderDetailsByFatherIclave(int fatherIclave) {
        return db.findOrderDetailsByFatherIclave(mContext.getRegion(), storeId, fatherIclave);
    }

    @Override
    public void invalidate() {
        mViewPager.invalidate();
    }

    public void movePrevious(View view) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
    }

    private void showResume() {
//        voiceShutdown();
        chrTime.stop();
        long now = new Date().getTime();
        scanTimeDTO.setEndTimeMillis(now);
        scanTimeDTO.setEndTime(DateUtil.getDateStr(now) + " " + DateUtil.getTimeNowStr());
        startActivityForResult(new Intent(mContext, PickingResumeActivity.class).putExtra(PickingResumeActivity.EXTRA_STORE_ID, storeId)
                .putExtra(PickingResumeActivity.EXTRA_SCAN_TIME, scanTimeDTO), REQUEST_CODE);
    }

    private void voiceShutdown() {
        try {
            if (Speech.getInstance() != null) {
                Speech.getInstance().shutdown();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == 0) {
            finish();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {

        }
        return super.onKeyUp(keyCode, event);
    }
}
