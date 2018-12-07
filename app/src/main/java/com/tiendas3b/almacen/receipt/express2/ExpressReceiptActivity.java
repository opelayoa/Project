package com.tiendas3b.almacen.receipt.express2;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.ScannerActivity;
import com.tiendas3b.almacen.activities.BuyDetailActivity;
import com.tiendas3b.almacen.db.dao.Barcode;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.BuyDetail;
import com.tiendas3b.almacen.db.dao.ExpressArticle;
import com.tiendas3b.almacen.db.dao.ExpressReceipt;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.db.dao.ReceiptSheetCapture;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetailCapture;
import com.tiendas3b.almacen.db.dao.Region;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.BuyDTO;
import com.tiendas3b.almacen.dto.BuyDetailAndCaptureDTO;
import com.tiendas3b.almacen.dto.BuyDetailDTO;
import com.tiendas3b.almacen.dto.CaptureDocCompras;
import com.tiendas3b.almacen.dto.ReceiptSheetCaptureDTO;
import com.tiendas3b.almacen.dto.receipt.FhojaReciboEncTempDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.picking.orders.ScanTimeDTO;
import com.tiendas3b.almacen.receipt.InsertReceiptExpressLog;
import com.tiendas3b.almacen.receipt.express.AlarmNotificationReceiver;
import com.tiendas3b.almacen.receipt.express.HeaderExpressDTO;
import com.tiendas3b.almacen.receipt.express2.tabs.ReceiptTimetableStatusActivity;
import com.tiendas3b.almacen.util.BarcodeUtil;
import com.tiendas3b.almacen.util.BluetoothUtil;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.FileUtil;
import com.tiendas3b.almacen.util.KeyboardUtil;
import com.tiendas3b.almacen.util.printer.TscSdk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpressReceiptActivity extends ScannerActivity {

    private static final String ODC = "ODC";
    private static final String ICLAVE = "ICLAVE";
    private static final String TAG = "ExpressReceiptActivity2";
    //    private static final int BARCODE_LENGTH = 17;
    private static final boolean RECEIVED = true;
    private GlobalState mContext;
    private IDatabaseManager db;
    private long regionId;
    private int odc;
    private Buy buy;
    private long buyId;
    private boolean useCam;
    private ScanTimeDTO scanTimeDTO;
    private int scaffoldQuantity;
    private int initrex = 0; //bandera de iniciar recibo express


    @BindView(R.id.txtBarcode)
    EditText txtBarcode;
    @BindView(R.id.lblProviderName)
    EditText lblProviderName;
    //    @BindView(R.id.lblProviderId)
//    EditText lblProviderId;
    @BindView(R.id.clMain)
    ConstraintLayout clMain;
    @BindView(R.id.lblOdc)
    EditText lblOdc;
    @BindView(R.id.lblPalletTotal)
    EditText lblPalletTotal;
    //    @BindView(R.id.lblPalletTotalOdc)
//    EditText lblPalletTotalOdc;
    @BindView(R.id.lblPalletId)
    EditText lblPalletId;
    @BindView(R.id.lblArticleName)
    EditText lblArticleName;
    @BindView(R.id.lblBox4Pallet1)
    EditText lblBox4Pallet1;
    @BindView(R.id.lblBox4Pallet2)
    EditText lblBox4Pallet2;
    @BindView(R.id.lblScaffoldQuantity)
    EditText lblScaffoldQuantity;
    @BindView(R.id.lblArticlesQuantity)
    EditText lblArticlesQuantity;
    //    @BindView(R.id.lblPlatform)
//    EditText lblPlatform;
    @BindView(R.id.chrTime)
    Chronometer chrTime;
    private ExpressReceipt expressDetailScan;
    private boolean isInBulk;
    private long iclave;
    private FhojaReciboEncTempDTO fhojaReciboEncTempDTO = null;

//    @BindView(R.id.rvExpressReceipt)
//    RecyclerView recyclerView;

    public static Intent getIntent(Context mContext, int folio) {
        return new Intent(mContext, ExpressReceiptActivity.class).putExtra(ODC, folio);
    }

    public static Intent getIntent(Context mContext, int odc, long iclave) {
        return new Intent(mContext, ExpressReceiptActivity.class)
                .putExtra(ICLAVE, iclave)
                .putExtra(ODC, odc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e(TAG, "onCreateOptionsMenu:" + isInBulk);
        if (isInBulk) {
            getMenuInflater().inflate(R.menu.menu_express_receipt_without_send, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_express_receipt2, menu);
        }
//        MenuItem btnCam = menu.findItem(R.id.action_cam_scan);
//        cbxCam = (CheckBox) btnCam.getActionView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_cam_scan:
                if(initrex == 1 || isInBulk){//Bandera inicia rex2
                    useCam = !item.isChecked();
                    item.setChecked(useCam);
                    if (useCam) {
                        txtBarcode.setHint("Click aquí para escanear");
                    } else {
                        txtBarcode.setHint("Usar escáner físico");//(R.string.hint_scan_barcode);
                    }
                    return true;
                }else{
                    Toast.makeText(this, "Debes iniciar descarga",Toast.LENGTH_LONG).show();
                }
            case R.id.action_send:
                if(initrex == 1){//Bandera inicia rex2
                    List<ExpressReceipt> expressReceipts = db.listExpressReceiptByFolio(odc, mContext.getRegion(), RECEIVED);
                    receiptFinish(expressReceipts);
////                Buy buy = db.findBuyByFolio(odc, mContext.getRegion());
////                buyId = buy.getId();
//                Intent intent = new Intent(mContext, BuyDetailActivity.class);
//                intent.putExtra(Constants.EXTRA_BUY, buyId);
////                intent.putExtra(Constants.EXTRA_EXPRESS_RECEIPT, true);
//                startActivity(intent);
                    return true;
                }else{
                    Toast.makeText(this, "Debes iniciar descarga",Toast.LENGTH_LONG).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        Log.e(TAG, "onCreateOptionsMenu:" + isInBulk);
        db = new DatabaseManager(this);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onCreateOptionsMenu:" + isInBulk);
        if (db != null) db.closeDbConnections();
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onCreateOptionsMenu:" + isInBulk);
        db = DatabaseManager.getInstance(this);
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        Log.e(TAG, "onCreateOptionsMenu:" + isInBulk);
        super.onPostResume();
        if(isInBulk){
            initChronometer();
        }

    }

    private void initChronometer() {
        if (scanTimeDTO == null) {
            scanTimeDTO = new ScanTimeDTO();
            long now;
            if(fhojaReciboEncTempDTO == null) {
                now = new Date().getTime();
                scanTimeDTO.setInitTimeMillis(now);
                chrTime.setBase(SystemClock.elapsedRealtime());
                scanTimeDTO.setInitTime(DateUtil.getTimeWithoutDateStr(now));
                chrTime.start();
            }else{
                Date myTime = null, dateSys = null;
                SimpleDateFormat dateformate = new SimpleDateFormat("HH:mm:ss");
                String dateSystem = new SimpleDateFormat("HH:mm:ss").format(new Date());
                try {
                    myTime = dateformate.parse(fhojaReciboEncTempDTO.getHoraEntrega());
                    dateSys = dateformate.parse(dateSystem);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                now = dateSys.getTime() - myTime.getTime();

                System.out.println(now/3600000);
                chrTime.setBase(SystemClock.elapsedRealtime() - now);
                scanTimeDTO.setInitTimeMillis(now);
                chrTime.start();
            }
//            scanTimeDTO.setInitTime(DateUtil.getTimeWithoutDateStr(now));
//            chrTime.start();
//            startAlarm(true, false);
        }
    }

    private void startAlarm(boolean isNotification, boolean isRepeat) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

//        if (isNotification) {
        myIntent = new Intent(this, AlarmNotificationReceiver.class);
        myIntent.putExtra(AlarmNotificationReceiver.EXTRA_ODC, odc);
        pendingIntent = PendingIntent.getBroadcast(this, odc, myIntent, 0);
//        } else {
//            myIntent = new Intent(this, AlarmToastReceiver.class);
//            pendingIntent = PendingIntent.getBroadcast(this, odc, myIntent, 0);
//        }
//        if (isRepeat) {
//            manager.setRepeating(AlarmManager.RTC_WAKEUP, new Date().getTime() + 300000, 60 * 1000, pendingIntent);
//        }else {
        manager.set(AlarmManager.RTC_WAKEUP, new Date().getTime() + 3 * 60 * 1000, pendingIntent);
//        }
    }

    private void cancelAlarm() {
        Intent myIntent = new Intent(this, AlarmNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, odc, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreateOptionsMenu:" + isInBulk);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_receipt2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        init();
        toolbar.setTitle("Detalle Prov " + buy.getProviderId());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button btnStartRex2 =  findViewById(R.id.btnStartRex2);
        if(isInBulk){
            btnStartRex2.setVisibility(Button.GONE);
        }else{
            findViewById(R.id.btnBulk).setVisibility(Button.GONE);
            downloadTimeInit();

//            if(fhojaReciboEncTempDTO == null){
//                btnStartRex2.setVisibility(Button.VISIBLE);
//            }
//            if (fhojaReciboEncTempDTO != null){
////                List<ExpressReceipt> expressReceipts = db.listExpressReceiptByFolio(odc, mContext.getRegion(), RECEIVED);
//                findViewById(R.id.progressBarReceipt).setVisibility(View.VISIBLE);
//                findViewById(R.id.btnStartRex2).setVisibility(Button.GONE);
//                initrex = 1;
////                initChronometer();
//                downloadBuyDetails();
//                useCam = true;
////                ReceiptSheetCapture receiptSheetCapture = db.findReceiptSheetCaptureBy(buy.getId());
////                chrTime.start();
////                scanTimeDTO = new ScanTimeDTO();
////                long now = Long.parseLong(receiptSheetCapture.getDeliveryTime());
////                scanTimeDTO.setInitTimeMillis(now);
////                chrTime.setBase(SystemClock.elapsedRealtime());
////                scanTimeDTO.setInitTime(DateUtil.getTimeWithoutDateStr(now));
//            }else{
//            }
//            findViewById(R.id.btnBulk).setVisibility(Button.GONE);

        }

        txtBarcode.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if(initrex == 1){
                            // Perform action on key press
                            return true;
                        }else{
                            txtBarcode.setText("");
                            Toast.makeText(ExpressReceiptActivity.this, "Debes iniciar descarga",Toast.LENGTH_SHORT).show();
                        }
                    }

                return false;
            }
        });

    }

    private void init() {
        mContext = (GlobalState) getApplicationContext();
//        ButterKnife.bind(this);
//        setTitle();
//        setTitle();
        db = new DatabaseManager(mContext);
        regionId = mContext.getRegion();
        Intent intent = getIntent();
        odc = intent.getIntExtra(ODC, -1);
        buy = db.findBuyByFolio(odc, regionId);
        buyId = buy.getId();

        setUpViews();
////        syncBuys();
        iclave = intent.getLongExtra(ICLAVE, 0L);
        isInBulk = iclave != 0L;
        Log.d(TAG, "isInBulk:" + isInBulk);
        if (isInBulk) {
            downloadHeaderExpress();
            cleanFields();
            ExpressArticle prod = db.findExpressArticle(regionId, odc, iclave);
            if (prod != null) {
                fillFields(prod);
            }
//            downloadExpressArticles();//quitar de aquí y descargarlos en la de agranel
            VArticle article = db.findViewArticleByIclave(iclave);
            lblArticleName.setText(article.getDescription() + "/" + article.getUnity());
        } else {
            //downloadBuyDetails();
        }
    }

    private void setUpViews() {
        ButterKnife.bind(this);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Provider provider = buy.getProvider();
        lblProviderName.setText(String.valueOf(provider.getId()) + " " + provider.getName());
//        lblProviderId.setText(String.valueOf(provider.getId()));
        lblOdc.setText(String.valueOf(buy.getFolio()));
//        lblPlatform.setText(String.valueOf(buy.getPlatform()));

//        adapter = new ExpressReceiptRvAdapter(mContext, null);
//        recyclerView.setAdapter(adapter);

        txtBarcode.setOnClickListener(view -> {
//                final int action = event.getAction();
//                if (action == MotionEvent.ACTION_DOWN) {
            if (useCam) {
                BarcodeUtil.intentBarcodeReader(com.tiendas3b.almacen.receipt.express2.ExpressReceiptActivity.this);
            }
//                }
        });
    }

    private void downloadTimeInit() {

        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getHojaReciboEncTemp(buy.getFolio(), String.valueOf(mContext.getRegion()), buy.getProviderId())
//        httpService.getReceiptSheetCapture(mContext.getRegion(), DateUtil.getDateStr(odc.getProgramedDate()), odc.getFolio(), odc.getProviderId(), odc.getId())
                .enqueue(new Callback<FhojaReciboEncTempDTO>() {
                    @Override
                    public void onResponse(Call<FhojaReciboEncTempDTO> call, Response<FhojaReciboEncTempDTO> response) {
                        if (response.isSuccessful()) {
                            FhojaReciboEncTempDTO r = response.body();
                            if (r != null) {
                                fhojaReciboEncTempDTO = r;
                                    findViewById(R.id.progressBarReceipt).setVisibility(View.VISIBLE);
                                    findViewById(R.id.btnStartRex2).setVisibility(Button.GONE);
                                    initrex = 1;
                                    initChronometer();
                                    downloadBuyDetails();
                                    useCam = true;
                                }
                        } else {
                            findViewById(R.id.btnStartRex2).setVisibility(Button.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<FhojaReciboEncTempDTO> call, Throwable t) {
                        Log.e(TAG, "Error getReceiptSheetCapture");
//                viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//                viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
//                viewSwitcher.setDisplayedChild(1);
                    }
                });
    }

    private void downloadBuyDetails() {
//        if (buy == null) {
//            Toast.makeText(mContext, "No existe ODC para día actual.", Toast.LENGTH_LONG).show();
//        } else {
        downloadDetails(odc, regionId, buy.getProviderId());
//        }
    }

    private void downloadDetails(int folio, long regionId, long providerId) {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.getBuyDetails(folio, String.valueOf(regionId), providerId).enqueue(new Callback<List<BuyDetailAndCaptureDTO>>() {
            @Override
            public void onResponse(Call<List<BuyDetailAndCaptureDTO>> call, Response<List<BuyDetailAndCaptureDTO>> response) {
                if (response.isSuccessful()) {
                    processBuyDetails(response.body());
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<BuyDetailAndCaptureDTO>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_odc));
                Toast.makeText(mContext, "Error descarga de detalles", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void processBuyDetails(List<BuyDetailAndCaptureDTO> list) {
        if (list == null) {
        } else {
            if (createItems(list)) {
                downloadHeaderExpress();
            } else {
                finish();
                Log.i(TAG, "else");
            }
        }
    }

    private boolean createItems(List<BuyDetailAndCaptureDTO> list) {
        List<BuyDetail> items = new ArrayList<>();
//        captures = new ArrayList<>();
//        boolean complete = true;
        for (BuyDetailAndCaptureDTO a : list) {
            BuyDetailDTO d = a.getBuyDetail();
//            VArticle article = db.findViewArticleForPurchasing(d.getIclave(), d.getBarcode());
            VArticle article = db.findViewArticleByIclave(d.getIclave());
            if (article == null) {
                Toast.makeText(mContext, "No se encontró el artículo: " + d.getIclave(), Toast.LENGTH_LONG).show();
                return false;
            } else {
                BuyDetail b = new BuyDetail();
//            b.setArticle(article);
                b.setArticleId(article.getId());
                b.setArticleAmount(d.getArticleAmount());
                b.setBalance(d.getBalance());
                b.setBuyId(buyId);
//            b.setBuy(item);
                ReceiptSheetDetailCapture c = a.getCapture();
                if (c == null) {
//                complete = false;
                    Log.e(TAG, "ReceiptSheetDetailCapture == null");
                } else {
                    b.setReceiptSheetCaptureId(db.insertOrUpdate(c));
                }
                items.add(b);
            }
        }

        db.insertOrReplaceInTx(items.toArray(new BuyDetail[items.size()]));
//        BuyDetail det = db.findBuyDetailByArticle(iclave, buyId);
//        createExpressReceipt(det);
//        updateList();
        return true;
    }

//    private void downloadHeaderExpressPtm() {
//        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
//        httpService.getHeaderExpress(odc, regionId, buy.getProviderId()).enqueue(new Callback<HeaderExpressDTO>() {
//            @Override
//            public void onResponse(Call<HeaderExpressDTO> call, Response<HeaderExpressDTO> response) {
//                if (response.isSuccessful()) {
//                    processHeaderPtm(response.body());
//                } else {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<HeaderExpressDTO> call, Throwable t) {
//                Log.e(TAG, mContext.getString(R.string.error_odc));
//                Toast.makeText(mContext, "getHeaderExpress", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void processHeaderPtm(HeaderExpressDTO header) {
//        lblArticlesQuantity.setText(String.valueOf(header.getArticlesQuantity()));
//        scaffoldQuantity = header.getScaffoldQuantity();
//        lblScaffoldQuantity.setText(String.valueOf(scaffoldQuantity));
//    }

    private void downloadHeaderExpress() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.getHeaderExpress2(odc, regionId, buy.getProviderId()).enqueue(new Callback<HeaderExpressDTO>() {
            @Override
            public void onResponse(Call<HeaderExpressDTO> call, Response<HeaderExpressDTO> response) {
                if (response.isSuccessful()) {
                    processHeader(response.body());
                } else {
                }
            }

            @Override
            public void onFailure(Call<HeaderExpressDTO> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_odc));
                Toast.makeText(mContext, "getHeaderExpress", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processHeader(HeaderExpressDTO header) {
        lblArticlesQuantity.setText(String.valueOf(header.getArticlesQuantity()));
        scaffoldQuantity = header.getScaffoldQuantity();
        lblScaffoldQuantity.setText(String.valueOf(scaffoldQuantity));

        if (!isInBulk) {
            ReceiptSheetCapture receiptSheetCapture = db.findReceiptSheetCaptureBy(buy.getId());
            if (receiptSheetCapture == null) {
                receiptSheetCapture = new ReceiptSheetCapture();
                receiptSheetCapture.setBuyId(buy.getId());
                receiptSheetCapture.setOdc(buy.getFolio());
            }
            receiptSheetCapture.setReceiptDate(DateUtil.getTodayStr());
            receiptSheetCapture.setDateTime(buy.getTime());
            receiptSheetCapture.setDeliveryTime(buy.getDeliveryTime());
            receiptSheetCapture.setPlatform(buy.getPlatform());
//        receiptSheetCapture.setFacturaRef(header.getFacturaRef());
//        receiptSheetCapture.setIva(header.getIva());
//        receiptSheetCapture.setIeps(header.getIeps());
//        receiptSheetCapture.setAmountFact(Float.valueOf(header.getFacturaSubtotal()));
//        receiptSheetCapture.setArrive(r.getArrive());
//        receiptSheetCapture.setNumReceivers(r.getNumReceivers());
//        receiptSheetCapture.setSerie(header.getSerie());
            db.insertOrUpdate(receiptSheetCapture);
            downloadDetails(header.getFolio());
            downloadArticles();
        }
    }

    private void downloadArticles() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.getExpressArticles(regionId, odc).enqueue(new Callback<List<ExpressArticle>>() {
            @Override
            public void onResponse(Call<List<ExpressArticle>> call, Response<List<ExpressArticle>> response) {
                if (response.isSuccessful()) {
                    processExpressArticles(response.body());
                } else {
                    Log.e(TAG, "getExpressArticles");
                }
            }

            @Override
            public void onFailure(Call<List<ExpressArticle>> call, Throwable t) {
                Log.e(TAG, "getExpressArticles");
            }
        });
    }

    private void processExpressArticles(List<ExpressArticle> list) {
        if (list != null && !list.isEmpty()) {
            db.insertOrReplaceInTx(list);
        }
    }

    private void downloadDetails(int folio) {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.getExpressDetails2(regionId, folio).enqueue(new Callback<List<ExpressReceipt>>() {
            @Override
            public void onResponse(Call<List<ExpressReceipt>> call, Response<List<ExpressReceipt>> response) {
                if (response.isSuccessful()) {
                    processDetails(response.body());
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<ExpressReceipt>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_odc));
                Toast.makeText(mContext, "getExpressDetails", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processDetails(List<ExpressReceipt> details) {
        Log.i(TAG, "processDetails");

//QUITAR ELEMENTOS DE LA LISTA QUE YA SE ESCANEARON

        List<ExpressReceipt> expressReceiptList = new ArrayList<>();
        for (int i=0; i < details.size(); i++){

            expressDetailScan = db.findExpressReceipt(details.get(i).getBarcode());
            if (expressDetailScan != null && expressDetailScan.getReceived()){
//                details.remove(i);
//                i--;
                expressReceiptList.add(expressDetailScan);
            }else{
                expressReceiptList.add(details.get(i));
            }
        }

            for (ExpressReceipt rex : expressReceiptList) {
                    rex.setBuyDetailId(db.findBuyDetailByArticle(rex.getIclave(), buyId).getId());
                    rex.setRegionId(regionId);
            }
            findViewById(R.id.progressBarReceipt).setVisibility(View.GONE);
        db.insertOrReplaceInTx(expressReceiptList.toArray(new ExpressReceipt[expressReceiptList.size()]));

        if (buy.getChecked() == Constants.NOT_ARRIVE || buy.getChecked() == Constants.NOT_ARRIVE_AUT ) {
            setInProcess();
        }
        updateList();
    }

    private void setInProcess() {
        Log.i(TAG, "setInProcess");
        CaptureDocCompras captureDocCompras = new CaptureDocCompras();
        BuyDTO buyDTO = new BuyDTO();
        buyDTO.setId(buy.getId());
        buyDTO.setPlatform(buy.getPlatform());
        buyDTO.setTime(buy.getTime());
        buyDTO.setDeliveryTime(buy.getDeliveryTime());
//        if(buy.getChecked() == 4){
//            buyDTO.setChecked(Constants.NOT_ARRIVE_AUT_TEMP);
//        }else{
//            buyDTO.setChecked(Constants.NOT_ARRIVE_TEMP);
//        }
        buyDTO.setChecked(Constants.NOT_ARRIVE_TEMP);
        buyDTO.setFolio(buy.getFolio());
        buyDTO.setForced(buy.getForced());
        buyDTO.setMov(buy.getMov());
        buyDTO.setProgramedDate(DateUtil.getDateStr(buy.getProgramedDate()));
        buyDTO.setProviderId(buy.getProviderId());
        buyDTO.setRegionId(buy.getRegionId());
        buyDTO.setTotal(buy.getTotal());
        captureDocCompras.setBuy(buyDTO);

        String dateTimeStr = buy.getTime();
        if (getString(R.string.no_timetable).equals(dateTimeStr)) {
            dateTimeStr = "00:00";
        }
        ReceiptSheetCaptureDTO receiptSheetCaptureDTO = new ReceiptSheetCaptureDTO();
        ReceiptSheetCapture r = db.findReceiptSheetCaptureBy(buy.getId());
        if (r == null) {
            receiptSheetCaptureDTO.setDateTime(dateTimeStr);
            receiptSheetCaptureDTO.setBuyId(buy.getId());
            receiptSheetCaptureDTO.setOdc(buy.getFolio());
            receiptSheetCaptureDTO.setReceiptDate(DateUtil.getTodayStr());
            receiptSheetCaptureDTO.setPlatform(buy.getPlatform());
//            receiptSheetCaptureDTO.setNumReceivers(0);
//            receiptSheetCaptureDTO.setDateTime(DateUtil.getDateStr(buy.getProgramedDate()));
        } else {
            Log.i(TAG, "r != null");
            receiptSheetCaptureDTO.setDateTime(r.getDateTime());
            receiptSheetCaptureDTO.setBuyId(r.getBuyId());
            receiptSheetCaptureDTO.setOdc(r.getOdc());
            receiptSheetCaptureDTO.setReceiptDate(r.getReceiptDate());
            receiptSheetCaptureDTO.setPlatform(r.getPlatform());
            receiptSheetCaptureDTO.setNumReceivers(r.getNumReceivers());
            receiptSheetCaptureDTO.setDeliveryman(r.getDeliveryman());
            receiptSheetCaptureDTO.setReceiver(r.getReceiver());
            receiptSheetCaptureDTO.setFacturaRef(r.getFacturaRef());
            receiptSheetCaptureDTO.setLevelId(r.getLevelId());
            receiptSheetCaptureDTO.setIva(r.getIva());
            receiptSheetCaptureDTO.setIeps(r.getIeps());
            receiptSheetCaptureDTO.setDateTypeId(r.getDateTypeId());
            receiptSheetCaptureDTO.setArriveTime(r.getArriveTime());
//            receiptSheetCaptureDTO.setPaletizado(r.getPaletizado());
//            receiptSheetCaptureDTO.setAmountEm(r.getAmountEm());
            receiptSheetCaptureDTO.setAmountFact(r.getAmountFact());
        }
        receiptSheetCaptureDTO.setArrive(Constants.ARRIVE);
        receiptSheetCaptureDTO.setDeliveryTime(DateUtil.getTimeNowStr());
        captureDocCompras.setReceiptSheet(receiptSheetCaptureDTO);

        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.insertReceiptSheet(captureDocCompras).enqueue(new Callback<CaptureDocCompras>() {
            @Override
            public void onResponse(Call<CaptureDocCompras> call, Response<CaptureDocCompras> response) {
                if (response.isSuccessful()) {
                    startDelivery(response.body());
                }
            }

            @Override
            public void onFailure(Call<CaptureDocCompras> call, Throwable t) {
                Toast.makeText(mContext, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "sdfgb");
            }
        });
    }

    private void startDelivery(CaptureDocCompras captureDocCompras) {
        Log.i(TAG, "startDelivery");
        BuyDTO buy = captureDocCompras.getBuy();
//        this.buy = db.getById(buy.getId(), Buy.class);
        this.buy.setChecked(buy.getChecked());
        db.update(this.buy);
//        setStatus();

        ReceiptSheetCaptureDTO r = captureDocCompras.getReceiptSheet();
        ReceiptSheetCapture receiptSheetCapture = db.findReceiptSheetCaptureBy(this.buy.getId());
        if (receiptSheetCapture == null) {
            receiptSheetCapture = new ReceiptSheetCapture();
            receiptSheetCapture.setBuyId(this.buy.getId());
            receiptSheetCapture.setOdc(this.buy.getFolio());
        }
        receiptSheetCapture.setArrive(r.getArrive());
        receiptSheetCapture.setReceiptDate(r.getReceiptDate());
        receiptSheetCapture.setDateTime(r.getDateTime());
        receiptSheetCapture.setDeliveryTime(r.getDeliveryTime());
        receiptSheetCapture.setPlatform(r.getPlatform());
        receiptSheetCapture.setNumReceivers(r.getNumReceivers());
        receiptSheetCapture.setAmountEm(r.getAmountEm());
        db.insertOrUpdate(receiptSheetCapture);
//        downloadTimeInit();
//        initChronometer();
//        setItems();
    }

    private void updateList() {
        List<ExpressReceipt> expressReceipts = db.listExpressReceiptByFolio(odc, mContext.getRegion(), RECEIVED);
//        adapter.setListAndNotify(expressReceipts);


        if (expressReceipts.size() == scaffoldQuantity) {
            receiptFinish(expressReceipts);
        } else {
            cleanFields();
        }
    }

    private void updateListInBulk() {
        List<ExpressReceipt> expressReceipts = db.listExpressReceiptByFolio(odc, mContext.getRegion(), RECEIVED);
//        adapter.setListAndNotify(expressReceipts);
        if (expressReceipts.size() == scaffoldQuantity) {
            Toast.makeText(mContext, "Todas las tarimas del articulo han sido capturadas.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            lblPalletId.setText("");
        }
    }

    private void cleanFields() {
        lblBox4Pallet1.setText("");
        lblBox4Pallet2.setText("");
        lblArticleName.setText("");
        lblPalletId.setText("");
        lblPalletTotal.setText("");
//        lblPalletTotalOdc.setText("");
    }

    private void receiptFinish(List<ExpressReceipt> expressReceipts) {
//        cancelAlarm();
        saveFilePrint(expressReceipts);
        printTicket(expressReceipts);

        Intent intent = new Intent(mContext, BuyDetailActivity.class);
        intent.putExtra(Constants.EXTRA_BUY, buyId)
                .putExtra(BuyDetailActivity.EXTRA_SCAN_TIME, scanTimeDTO);
        startActivity(intent);
//        finish();
//            btnSend.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (initrex == 0) {
            Intent intent = new Intent(ExpressReceiptActivity.this, ReceiptTimetableStatusActivity.class).putExtra("EXTRA_ORDER_TYPE", 0);
            finish();
            startActivity(intent);
        } else {
        showWarningDialog();
//        finish();
        }
    }

    public void insertCancel(){
        //INSERTA LOG RECIBO EXPRESS
        InsertReceiptExpressLog insertReceiptExpressLog = new InsertReceiptExpressLog(db, mContext);
        insertReceiptExpressLog.insertRecExprLog(buy, Constants.IDLOG_CANCEL_REX, Constants.LOG_CANCEL_REX);
    }

    private void showWarningDialog() {
        if (isInBulk) {
            super.onBackPressed();
        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getString(R.string.dialog_warning));
            alertDialogBuilder.setPositiveButton(getString(android.R.string.ok),
//                  (dialog, which) -> finish());
                    (dialog, which) -> {
                        insertCancel();

                        Intent intent = new Intent(ExpressReceiptActivity.this, ReceiptTimetableStatusActivity.class).putExtra("EXTRA_ORDER_TYPE", 0);
                        finish();
                        startActivity(intent);
                    }
            );
            alertDialogBuilder.setNegativeButton(android.R.string.cancel, null);

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @OnClick(R.id.btnStartRex2)
    protected void startRex2() {
        if (isInBulk) {
        }else{
            initChronometer();
            findViewById(R.id.progressBarReceipt).setVisibility(View.VISIBLE);
            findViewById(R.id.btnStartRex2).setVisibility(Button.GONE);
            initrex = 1;

            //INSERTA LOG RECIBO EXPRESS
            InsertReceiptExpressLog insertReceiptExpressLog = new InsertReceiptExpressLog(db, mContext);
            insertReceiptExpressLog.insertRecExprLog(buy, Constants.IDLOG_START_REX, Constants.LOG_START_REX);

            System.out.println("Iniciar Scann, Scann, Scann");
            downloadBuyDetails();
        }
    }

    @OnClick(R.id.btnProviders)
    protected void providers() {
        onBackPressed();
    }

    @OnClick(R.id.btnBulk)
    protected void bulk() {
        if (isInBulk) {
            onBackPressed();
        }
    }

    @OnClick(R.id.btnOk)
    protected void ok() {
//                BuyDetail det = db.findBuyDetailByArticle(detailExpress.getIclave(), buyId);
//                createExpressReceipt(det, detailExpress);

        if (isInBulk) {
            short palletId = 0;
            try {
                palletId = Short.parseShort(lblPalletId.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (palletId == 0) {
                Toast.makeText(this, "Ingresar ID de tarima por favor.", Toast.LENGTH_LONG).show();
            } else {
                expressDetailScan = db.findExpressReceipt(regionId, odc, iclave, palletId);
                if (expressDetailScan == null) {
                    Toast.makeText(mContext, "No se encontró ese id de tarima", Toast.LENGTH_LONG).show();
                } else {
                    createExpressReceipt();
                    updateListInBulk();
                }
            }
        } else {
            if(initrex == 1){//Bandera inicia rex2
                if (expressDetailScan == null) {
                    Toast.makeText(this, "Escanear por favor.", Toast.LENGTH_SHORT).show();
                } else {
                    createExpressReceipt();
                    updateList();
                }
            }else{
                Toast.makeText(this, "Debes iniciar descarga",Toast.LENGTH_LONG).show();
            }

        }
    }

    @OnClick(R.id.btnCancel)
    protected void cancel() {
//        showWarningDialog();
        onBackPressed();
    }

    private void createExpressReceipt(/*BuyDetail det, ExpressReceipt detailExpress*/) {
//        ExpressReceipt er = new ExpressReceipt();
////        er.setRegionId(mContext.getRegion());
//        er.setBarcode(barcode);
//        er.setBuyDetailId(det.getId());
//        db.insert(er);

        if (expressDetailScan.getReceived()) {
            Snackbar.make(clMain, "Tarima previamente escaneada", Snackbar.LENGTH_LONG).show();
        } else {
            int iclaveL = expressDetailScan.getIclave();
            BuyDetail det = db.findBuyDetailByArticle(iclaveL, buyId);
            ReceiptSheetDetailCapture capture = det.getReceiptSheetCapture();//on pause
//        int piecesForScaffold = db.getPiecesByUnit(Constants.UNITY_PLL, det.getArticle().getIclave());
            ExpressArticle prod = db.findExpressArticle(regionId, odc, iclaveL);
            int palletsAsn = prod.getPalletsAsn();
            int piecesForScaffold;
            if (palletsAsn == 0) {
                Log.e(TAG, "palletsAsn == 0");
                piecesForScaffold = expressDetailScan.getBoxesPallet() * prod.getPiecesByBox();
            } else {
                Log.i(TAG, "PiecesAsn:" + prod.getPiecesAsn() + " palletsAsn:" + palletsAsn);
                piecesForScaffold = prod.getPiecesAsn() / palletsAsn;
            }
            if (capture == null) {
                Log.i(TAG, "First piecesForScaffold:" + piecesForScaffold);
                capture = new ReceiptSheetDetailCapture();
                capture.setBuyDetailId(det.getId());
                capture.setReceivedAmount(piecesForScaffold);
                capture.setPaletizadoCount(1);
                capture.setExpiryDate(expressDetailScan.getExpirationDate());
                capture.setLote(expressDetailScan.getLote());
                det.setReceiptSheetCaptureId(db.insert(capture));
                db.update(det);
            } else {
                Log.i(TAG, "piecesForScaffold:" + piecesForScaffold);
                capture.setPaletizadoCount(capture.getPaletizadoCount() + 1);
                int quantity = capture.getReceivedAmount();
                int newTotal = quantity + piecesForScaffold;
                capture.setReceivedAmount(newTotal);
                db.update(capture);
            }

            expressDetailScan.setReceived(true);
            db.update(expressDetailScan);
            Snackbar.make(clMain, "Guardado", Snackbar.LENGTH_SHORT).show();

            //INSERTA LOG RECIBO EXPRESS
            InsertReceiptExpressLog insertReceiptExpressLog = new InsertReceiptExpressLog(db, mContext);
            insertReceiptExpressLog.insertRecExprLog(buy, Constants.IDLOG_SCANN_PROD, Constants.LOG_SCANN_PROD + "_" +expressDetailScan.getIclave());

        }
    }


    private void saveFilePrint(List<ExpressReceipt> expressReceipts) {

        HashMap<Integer, com.tiendas3b.almacen.receipt.express2.ExpressReceiptActivity.PalPutoTicket> sumHash = new HashMap<>();
        for (ExpressReceipt r : expressReceipts) {
            Integer key = r.getIclave();
            if (sumHash.containsKey(key)) {
                sumHash.get(key).setTotal(sumHash.get(key).getTotal() + 1);
            } else {
                sumHash.put(key, new com.tiendas3b.almacen.receipt.express2.ExpressReceiptActivity.PalPutoTicket(1, key, r.getBuyDetail().getArticle().getDescription()));
            }
        }

        StringBuilder ticketDetails = new StringBuilder();
        String tiendas3B = "           Tiendas 3B";
        ticketDetails.append(tiendas3B).append("\n");
        String almacen = "Almacen:      " + db.getById(regionId, Region.class).getName();
        ticketDetails.append(almacen).append("\n");
        String proveedor = "Proveedor:    " + lblProviderName.getText();
        ticketDetails.append(proveedor).append("\n");
        String ticketOdc = "ODC:          " + odc;
        ticketDetails.append(ticketOdc).append("\n");
        String ticketDate = "Fecha recibo: " + DateUtil.getTodayWithTimeStr();
        ticketDetails.append(ticketDate).append("\n");

        int granTotal = 0;
        for (Integer key : sumHash.keySet()) {
            com.tiendas3b.almacen.receipt.express2.ExpressReceiptActivity.PalPutoTicket o = sumHash.get(key);
            int total = o.getTotal();
            ticketDetails.append(total).append(" PLL ").append(o.getDesc()).append("\n");
            granTotal += total;
        }

        String ticketTimes;
        if(fhojaReciboEncTempDTO != null) {
            ticketTimes = "Hr Inicio: " + fhojaReciboEncTempDTO.getHoraEntrega().substring(0,5) + " Hr Fin: " + DateUtil.getTimeNowStr();
        }else{
            ticketTimes = "Hr Inicio: " + DateUtil.getTimeStr(scanTimeDTO.getInitTimeMillis()) + " Hr Fin: " + DateUtil.getTimeNowStr();
        }
        ticketDetails.append(ticketTimes).append("\n");
        String ticketTotal = "Total PLL: " + granTotal;
        ticketDetails.append(ticketTotal);

        Log.i(TAG, "Ticket:" + ticketDetails.toString());

        FileUtil.writeFile(ticketDetails.toString(), Environment.getExternalStorageDirectory().getAbsolutePath()
                .concat(getString(R.string.root_folder).concat(getString(R.string.receipt_folder))), "ticket" + odc + ".txt");
    }

    private void printTicket(List<ExpressReceipt> expressReceipts) {
        Log.d(TAG, "printTicket");
        BluetoothUtil.startBluetooth();
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bondedDevices = btAdapter.getBondedDevices();
        for (BluetoothDevice btDevice : bondedDevices) {
            String name = btDevice.getName();
            Log.d(TAG, "for BT:" + name);
            if (TscSdk.NAME.equals(name)) {
                printTsc(expressReceipts, btDevice.getAddress());
                break;
            }
        }

        //INSERTA LOG RECIBO EXPRESS
        InsertReceiptExpressLog insertReceiptExpressLog = new InsertReceiptExpressLog(db, mContext);
        insertReceiptExpressLog.insertRecExprLog(buy, Constants.IDLOG_PRINT_TICKET, Constants.LOG_PRINT_TICKET);

    }

    @SuppressLint("UseSparseArrays")
    private void printTsc(List<ExpressReceipt> expressReceipts, String address) {

//        StringBuilder textAndPrint = new StringBuilder();
//        textAndPrint.append("Tiendas 3B\n").append("Almacén: ").append(db.getById(regionId, Region.class).getName())
//                .append("\nProveedor: ").append(lblProviderName.getText()).append("\n").append(DateUtil.getTodayStr()).append("\nODC: ").append(odc).append("\n\n");
        HashMap<Integer, com.tiendas3b.almacen.receipt.express2.ExpressReceiptActivity.PalPutoTicket> sumHash = new HashMap<>();
        Log.d(TAG, "printTsc address:" + address);
        for (ExpressReceipt r : expressReceipts) {
            Integer key = r.getIclave();
            if (sumHash.containsKey(key)) {
                sumHash.get(key).setTotal(sumHash.get(key).getTotal() + 1);
            } else {
                sumHash.put(key, new com.tiendas3b.almacen.receipt.express2.ExpressReceiptActivity.PalPutoTicket(1, key, r.getBuyDetail().getArticle().getDescription()));
            }
        }


//        StringBuilder ticketDetails = new StringBuilder();
        String tiendas3B = "           Tiendas 3B";
//        ticketDetails.append(tiendas3B).append("\n");
        String almacen = "Almacen:      " + db.getById(regionId, Region.class).getName();
//        ticketDetails.append(almacen).append("\n");
        String proveedor = "Proveedor:    " + lblProviderName.getText();
//        ticketDetails.append(proveedor).append("\n");
        String ticketOdc = "ODC:          " + odc;
//        ticketDetails.append(ticketOdc).append("\n");
        String ticketDate = "Fecha recibo: " + DateUtil.getTodayWithTimeStr();
//        ticketDetails.append(ticketDate).append("\n");
//
        int granTotal = 0;
        for (Integer key : sumHash.keySet()) {
            com.tiendas3b.almacen.receipt.express2.ExpressReceiptActivity.PalPutoTicket o = sumHash.get(key);
            int total = o.getTotal();
            granTotal += total;
        }

        String ticketTimes;
        if(fhojaReciboEncTempDTO != null) {
            ticketTimes = "Hr Inicio: " + fhojaReciboEncTempDTO.getHoraEntrega().substring(0,5) + " Hr Fin: " + DateUtil.getTimeNowStr();
        }else{
            ticketTimes = "Hr Inicio: " + DateUtil.getTimeStr(scanTimeDTO.getInitTimeMillis()) + " Hr Fin: " + DateUtil.getTimeNowStr();
        }
        String ticketTotal = "Total PLL: " + granTotal;


        TscSdk printer = new TscSdk();
        if (printer.openPort(address)) {
            printer.setup(70, (sumHash.size() * 3 + 75), 4, 4, 0, 0, 0);
            printer.cls();
            printer.text(40, tiendas3B);
            printer.text(70, almacen);
            printer.text(100, proveedor);
            printer.text(130, ticketOdc);
            printer.text(160, ticketDate);
            printer.text(190, "");
            int y = 210;
            printer.text(y, "");
            for (Integer key : sumHash.keySet()) {//hacer mas chido y con ganas
                com.tiendas3b.almacen.receipt.express2.ExpressReceiptActivity.PalPutoTicket o = sumHash.get(key);
                int total = o.getTotal();
                printer.text(y += 30, total + " PLL " + o.getDesc());
            }
            y += 60;
            printer.text(y += 30, ticketTimes);
            printer.text(y + 30, ticketTotal);

            printer.print();
            printer.closePort();
        }

    }


    class PalPutoTicket {

        private int total;
        private int iclave;
        private String desc;

        public PalPutoTicket(int total, Integer key, String description) {
            this.total = total;
            iclave = key;
            desc = description;
        }

        public int getIclave() {
            return iclave;
        }

        public void setIclave(int iclave) {
            this.iclave = iclave;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        db = DatabaseManager.getInstance(this);
        if (requestCode == BarcodeUtil.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra(BarcodeUtil.SCAN_RESULT);
                processBarcode(contents);
            }
//            else if (resultCode == RESULT_CANCELED) {
//                txtBarcode.clearFocus();
//            }
        }
    }

    @Override
    protected void processBarcode(String barcodeRead) {
        Log.i(TAG, "bc:" + barcodeRead);
//        if (barcodeRead.length() >= BARCODE_LENGTH) {
        String barcode = barcodeRead;//tal ]vez global

        expressDetailScan = db.findExpressReceipt(barcode);
        if (expressDetailScan == null) {
            //TODO BUSCAR TARIMA POR ICLAVE, ESCANEANDO CAJA...
            Barcode barcodedb = db.findIclaveBarcode(barcode);
//            Toast.makeText(mContext, barcodedb.getIclave() + barcodedb.getIdesc(),Toast.LENGTH_LONG).show();

            //TODO VALIDA QUE EL CODIGO DE BARRAS ESTE ACTIVO, SI BARCODEDB ES NULO ESTA INACTIVO
            if(barcodedb != null){
                List<ExpressReceipt> expressReceiptList = db.findExpressReceiptList(odc, Long.valueOf(barcodedb.getIclave()));

                if (expressReceiptList.size() > 0) {

                    //TODO Asigna tarima a caja
                    for (int i = 0; i < expressReceiptList.size(); i++) {
                        if (!expressReceiptList.get(i).getReceived()) {
                            expressDetailScan = expressReceiptList.get(i);
                            break;
                        }
                    }

                    //TODO Si ya se escanearon todas las tarimas, asignar primer tarima encontrada
                    if (expressDetailScan == null) {
                        expressDetailScan = expressReceiptList.get(0);
                    }
                    fillFields();


                } else {
                    Toast.makeText(mContext, "Código de barras no esperado", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(mContext, "Código de barras no esperado", Toast.LENGTH_LONG).show();
            }

        } else {
            fillFields();
        }

//        } else {
//            Toast.makeText(mContext, "Código de barras no válido", Toast.LENGTH_LONG).show();
//        }
        KeyboardUtil.hide(this);
    }

    @SuppressLint("SetTextI18n")
    private void fillFields() {
        BuyDetail buyDetail = expressDetailScan.getBuyDetail();
        VArticle article = buyDetail.getArticle();
        if (article == null) {
            Toast.makeText(mContext, "article null", Toast.LENGTH_SHORT).show();
        } else {
            lblArticleName.setText(article.getDescription() + "/" + article.getUnity());
            Integer boxesPallet = expressDetailScan.getBoxesPallet();


            ExpressArticle prod = db.findExpressArticle(regionId, odc, article.getIclave());
            if (prod != null) {
                lblBox4Pallet2.setText(prod.getBoxesByBed().toString());
            }


//        if(boxesPallet == null){
//            lblBox4Pallet1.setText("N/A");
//        } else {
            lblBox4Pallet1.setText(boxesPallet.toString());
//        }
//        lblBox4Pallet2.setText(expressDetail.get);
            lblPalletId.setText(expressDetailScan.getScaffoldNum().toString());
            lblPalletTotal.setText(String.valueOf(expressDetailScan.getCantidad()));
        }
    }

    private void fillFields(ExpressArticle prod) {
//        if(boxesPallet == null){
//            lblBox4Pallet1.setText("N/A");
//        } else {
        lblBox4Pallet1.setText(prod.getBoxesByPallet().toString());
        lblBox4Pallet2.setText(prod.getBoxesByBed().toString());
        lblPalletTotal.setText(prod.getPalletsAsn().toString());
//        lblPalletTotalOdc.setText(prod.getBoxesByPallet());
//        }
//        lblBox4Pallet2.setText(expressDetail.get);
        if (isInBulk) {
//            lblPalletId.setFocusable(true);
//            lblPalletId.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
            txtBarcode.setNextFocusDownId(View.NO_ID);
            txtBarcode.setNextFocusLeftId(View.NO_ID);
            txtBarcode.setNextFocusRightId(View.NO_ID);
            txtBarcode.setNextFocusUpId(View.NO_ID);
            txtBarcode.setNextFocusForwardId(View.NO_ID);
//            lblPalletId.requestFocus();
        } else {
            lblPalletId.setText(expressDetailScan.getScaffoldNum() + "/" + expressDetailScan.getCantidad());
        }
    }

    @Override
    protected void clearTxt() {
        txtBarcode.setText("");
    }
}
