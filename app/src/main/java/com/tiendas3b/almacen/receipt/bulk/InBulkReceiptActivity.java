package com.tiendas3b.almacen.receipt.bulk;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.BuyDetailActivity;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
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
import com.tiendas3b.almacen.receipt.express.AlarmNotificationReceiver;
import com.tiendas3b.almacen.receipt.express.HeaderExpressDTO;
import com.tiendas3b.almacen.receipt.express2.ExpressReceiptActivity;
import com.tiendas3b.almacen.receipt.InsertReceiptExpressLog;
import com.tiendas3b.almacen.receipt.express2.tabs.ReceiptTimetableStatusActivity;
import com.tiendas3b.almacen.util.BluetoothUtil;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.FileUtil;
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

public class InBulkReceiptActivity extends AppCompatActivity {

    private static final String TAG = "PalletizedReceiptA";

    public static final String ODC = "odc";
    private static final boolean RECEIVED = false;

    //    private String mDate;
    private GlobalState mContext;
    private IDatabaseManager db;
    private int startGranel = 0;

    @BindView(R.id.lblProviderName)
    EditText lblProviderName;
//    @BindView(R.id.lblProviderId)
//    EditText lblProviderId;
    @BindView(R.id.lblOdc)
    EditText lblOdc;
    @BindView(R.id.lblScaffoldQuantity)
    EditText lblScaffoldQuantity;
    @BindView(R.id.lblArticlesQuantity)
    EditText lblArticlesQuantity;
//    @BindView(R.id.lblPlatform)
//    EditText lblPlatform;
    @BindView(R.id.rvExpressReceipt)
    RecyclerView recyclerView;
    @BindView(R.id.chrTime)
    Chronometer chrTime;
//    @BindView(R.id.txtBarcode)
//    EditText txtBarcode;
    private InBulkReceiptRvAdapter adapter;
    private long buyId;
    private int iclave;
    private String barcode;
    private int odc;
    //    private MenuItem btnSend;
    //    private String expiryDate;
    private Buy buy;
    private long regionId;
    private int scaffoldQuantity;
//    private boolean useCam;
    private ScanTimeDTO scanTimeDTO;
    private FhojaReciboEncTempDTO fhojaReciboEncTempDTO;
//    private CheckBox cbxCam;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);//TODO cambiar por solo accion send
//        MenuItem btnCam = menu.findItem(R.id.action_cam_scan);
//        cbxCam = (CheckBox) btnCam.getActionView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_send:
                if(startGranel == 1){
                    send();
                    return true;
                }else {
                    Toast.makeText(this, "Debes iniciar descarga", Toast.LENGTH_LONG).show();
                }
//            case R.id.action_info:
//                Snackbar.make(recyclerView, "Si la mercancía es incompleta se debe recibir manualmente", Snackbar.LENGTH_LONG).show();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void send() {
//        createExpressReceipt();
            receiptFinish(null);
    }

    private void receiptFinish(List<ExpressReceipt> expressReceipts) {
//        cancelAlarm();
//        saveFilePrint(expressReceipts);//if print
//        printTicket(expressReceipts);

//            chrTime.stop();
//            long now = new Date().getTime();
//            scanTimeDTO.setEndTimeMillis(now);
//            scanTimeDTO.setEndTime(DateUtil.getDateStr(now));
        Intent intent = new Intent(mContext, BuyDetailActivity.class);
        intent.putExtra(Constants.EXTRA_BUY, buyId)
                .putExtra(BuyDetailActivity.EXTRA_SCAN_TIME, scanTimeDTO);
        startActivity(intent);
        finish();
//            btnSend.setEnabled(true);
    }

//    @Override
//    public void onBackPressed() {
//        finish();
//    }

//    private void createExpressReceipt() {
//
//        List<ExpressArticle> list = adapter.getList();
//        for (ExpressArticle a : list) {
//            BuyDetail det = db.findBuyDetailByArticle(a.getIclave(), buyId);
//            ReceiptSheetDetailCapture capture = det.getReceiptSheetCapture();//on pause
//            VArticle article = det.getArticle();
//            int piecesByScaffold = db.getPiecesByUnit(Constants.UNITY_PLL, article.getIclave());
//            int piecesByBox = article.getPacking();
//            int count = a.getCount();
//            int totalPieces = count * piecesByBox;
//            if (capture == null) {
//                capture = new ReceiptSheetDetailCapture();
//                capture.setBuyDetailId(det.getId());
//                capture.setPaletizadoCount(totalPieces / piecesByScaffold);
//                capture.setReceivedAmount(totalPieces);
//                capture.setExpiryDate("");
//                capture.setLote("");
//                det.setReceiptSheetCaptureId(db.insert(capture));
//                db.update(det);
//            } else {
//                capture.setPaletizadoCount(totalPieces / piecesByScaffold);
//                capture.setReceivedAmount(totalPieces);
//                db.update(capture);
//            }
//        }
//    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_bulk);
        Toolbar toolbar = findViewById(R.id.toolbar);
        init();
        toolbar.setTitle("Detalle Proveedor " + buy.getProviderId());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        List<ExpressReceipt> expressReceipts = db.listExpressReceiptByFolio(odc, mContext.getRegion(), RECEIVED);
//        if (expressReceipts.size() > 0){
//            findViewById(R.id.progressBarReceiptGr).setVisibility(View.VISIBLE);
//            findViewById(R.id.btnStartGranel).setVisibility(Button.GONE);
//            startGranel = 1;
            downloadTimeInit();
//            downloadBuyDetails();
//        }

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
    protected void onPostResume() {
        super.onPostResume();
//        initChronometer();
    }

    @Override
    public void onBackPressed() {
        if (startGranel == 0) {
            Intent intent = new Intent(InBulkReceiptActivity.this, ReceiptTimetableStatusActivity.class).putExtra("EXTRA_ORDER_TYPE", 0);
            finish();
            startActivity(intent);
        } else {
            showWarningDialog();
        }
    }

    public void insertCancel(){
        //INSERTA LOG RECIBO EXPRESS
        InsertReceiptExpressLog insertReceiptExpressLog = new InsertReceiptExpressLog(db, mContext);
        insertReceiptExpressLog.insertRecExprLog(buy, Constants.IDLOG_CANCEL_REX, Constants.LOG_CANCEL_REX);
    }

    private void showWarningDialog() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getString(R.string.dialog_warning));
            alertDialogBuilder.setPositiveButton(getString(android.R.string.ok),
                    (dialog, which) -> {
                        insertCancel();
                        Intent intent = new Intent(InBulkReceiptActivity.this, ReceiptTimetableStatusActivity.class).putExtra("EXTRA_ORDER_TYPE", 0);
                        finish();
                        startActivity(intent);
                    }
            );
            alertDialogBuilder.setNegativeButton(android.R.string.cancel, null);

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
    }

    @OnClick(R.id.btnStartGranel)
    protected void startGranel() {
        initChronometer();
        startGranel = 1;
        findViewById(R.id.btnStartGranel).setVisibility(Button.GONE);
        findViewById(R.id.progressBarReceiptGr).setVisibility(View.VISIBLE);
        downloadBuyDetails();
        //INSERTA LOG RECIBO EXPRESS
        InsertReceiptExpressLog insertReceiptExpressLog = new InsertReceiptExpressLog(db, mContext);
        insertReceiptExpressLog.insertRecExprLog(buy, Constants.IDLOG_START_REX, Constants.LOG_START_REX);
    }

//    private void initChronometer() {
//        if (scanTimeDTO == null) {
//            chrTime.start();
//            scanTimeDTO = new ScanTimeDTO();
//            long now = new Date().getTime();
//            scanTimeDTO.setInitTimeMillis(now);
//            chrTime.setBase(SystemClock.elapsedRealtime());
//            scanTimeDTO.setInitTime(DateUtil.getTimeWithoutDateStr(now));
////            startAlarm(true, false);
//        }
//    }

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

                chrTime.setBase(SystemClock.elapsedRealtime() - now);
                scanTimeDTO.setInitTimeMillis(now);
                chrTime.start();
            }
//            scanTimeDTO.setInitTime(DateUtil.getTimeWithoutDateStr(now));
//            chrTime.start();
//            startAlarm(true, false);
        }
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
                                findViewById(R.id.progressBarReceiptGr).setVisibility(View.VISIBLE);
                                findViewById(R.id.btnStartGranel).setVisibility(Button.GONE);
                                startGranel = 1;
                                fhojaReciboEncTempDTO = r;
                                initChronometer();
                                downloadBuyDetails();
                            }
                        } else {
                            findViewById(R.id.btnStartGranel).setVisibility(View.VISIBLE);
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

    private void init() {
        mContext = (GlobalState) getApplicationContext();
        db = new DatabaseManager(mContext);
        regionId = mContext.getRegion();
        odc = getIntent().getIntExtra(ODC, -1);
        buy = db.findBuyByFolio(odc, regionId);
        buyId = buy.getId();

        setUpViews();
//        syncBuys();
//        downloadBuyDetails();
    }

    private void downloadBuyDetails() {
//        if (buy == null) {
//            Toast.makeText(mContext, "No existe ODC para día actual.", Toast.LENGTH_LONG).show();
//        } else {
        downloadDetails(odc, regionId, buy.getProviderId());
//        }
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
        if(list != null && !list.isEmpty()) {
            db.insertOrReplaceInTx(list);
        }

        updateList();
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
            if(createItems(list)) {
                downloadHeaderExpress();
            } else {
                finish();
            }
        }
    }

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
        if(details == null || details.isEmpty()){
            Toast.makeText(mContext, "No hay artículos.", Toast.LENGTH_LONG).show();
        } else {
            for (ExpressReceipt rex : details) {
                rex.setBuyDetailId(db.findBuyDetailByArticle(rex.getIclave(), buyId).getId());
                rex.setRegionId(regionId);
            }
            findViewById(R.id.progressBarReceiptGr).setVisibility(View.GONE);
            db.insertOrReplaceInTx(details);
            if (buy.getChecked() == Constants.NOT_ARRIVE || buy.getChecked() == Constants.NOT_ARRIVE_AUT ) {
                setInProcess();
            }
        }
//        updateList();
    }

    private void setUpViews() {
        ButterKnife.bind(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Provider provider = buy.getProvider();
        lblProviderName.setText(String.valueOf(provider.getId()) + " "+ provider.getName());
        lblOdc.setText(String.valueOf(buy.getFolio()));
//        lblProviderId.setText(String.valueOf(provider.getId()));
//        lblPlatform.setText(String.valueOf(buy.getPlatform()));

        adapter = new InBulkReceiptRvAdapter(mContext, new BaseRecyclerViewAdapter.OnViewHolderClick() {
            @Override
            public void onClick(View view, int position) {
//                Log.d(TAG, "" + adapter.getItem(position).getArticle().getIclave());
                startActivity(ExpressReceiptActivity.getIntent(mContext, odc, adapter.getItem(position).getIclave()));
            }
        });
        recyclerView.setAdapter(adapter);

//        txtBarcode.setVisibility(View.GONE);

    }

    private void updateList() {
        List<ExpressArticle> expressReceipts = db.listExpressArticles(mContext.getRegion(), odc);

//        List<VArticleInBulk> inBulkReceipts = new ArrayList<>();
//
//        for (ExpressArticle er : expressReceipts) {
//            VArticleInBulk ap = new VArticleInBulk();
//            ap.setArticle(er.getBuyDetail().getArticle());
////            ap.setTotal(er.getCantidad());
////            ap.setCount(0);
//            inBulkReceipts.add(ap);
//        }

        adapter.setListAndNotify(expressReceipts);
    }

    private void saveFilePrint(List<ExpressReceipt> expressReceipts) {

        HashMap<Integer, PalPutoTicket> sumHash = new HashMap<>();
        for (ExpressReceipt r : expressReceipts) {
            Integer key = r.getIclave();
            if (sumHash.containsKey(key)) {
                sumHash.get(key).setTotal(sumHash.get(key).getTotal() + 1);
            } else {
                sumHash.put(key, new PalPutoTicket(1, key, r.getBuyDetail().getArticle().getDescription()));
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
            PalPutoTicket o = sumHash.get(key);
            int total = o.getTotal();
            ticketDetails.append(total).append(" PLL ").append(o.getDesc()).append("\n");
            granTotal += total;
        }


        String ticketTimes = "Hr Inicio: " + DateUtil.getTimeStr(scanTimeDTO.getInitTimeMillis()) + " Hr Fin: " + DateUtil.getTimeNowStr();
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
    }

    @SuppressLint("UseSparseArrays")
    private void printTsc(List<ExpressReceipt> expressReceipts, String address) {

//        StringBuilder textAndPrint = new StringBuilder();
//        textAndPrint.append("Tiendas 3B\n").append("Almacén: ").append(db.getById(regionId, Region.class).getName())
//                .append("\nProveedor: ").append(lblProviderName.getText()).append("\n").append(DateUtil.getTodayStr()).append("\nODC: ").append(odc).append("\n\n");
        HashMap<Integer, PalPutoTicket> sumHash = new HashMap<>();
        Log.d(TAG, "printTsc address:" + address);
        for (ExpressReceipt r : expressReceipts) {
            Integer key = r.getIclave();
            if (sumHash.containsKey(key)) {
                sumHash.get(key).setTotal(sumHash.get(key).getTotal() + 1);
            } else {
                sumHash.put(key, new PalPutoTicket(1, key, r.getBuyDetail().getArticle().getDescription()));
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
            PalPutoTicket o = sumHash.get(key);
            int total = o.getTotal();
//            ticketDetails.append(total).append(" PLL ").append(o.getDesc()).append("\n");
            granTotal += total;
        }


        String ticketTimes = "Hr Inicio: " + DateUtil.getTimeStr(scanTimeDTO.getInitTimeMillis()) + " Hr Fin: " + DateUtil.getTimeNowStr();
//        ticketDetails.append(ticketTimes).append("\n");
        String ticketTotal = "Total PLL: " + granTotal;
//        ticketDetails.append(ticketTotal);
//
//        Log.i(TAG, "Ticket:" + ticketDetails.toString());
//
//        FileUtil.writeFile(ticketDetails.toString(), Environment.getExternalStorageDirectory().getAbsolutePath()
//                .concat(getString(R.string.root_folder).concat(getString(R.string.receipt_folder))), "ticket" + odc + ".txt");


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
//            int granTotal = 0;
            for (Integer key : sumHash.keySet()) {//hacer mas chido y con ganas
                PalPutoTicket o = sumHash.get(key);
                int total = o.getTotal();
                printer.text(y += 30, total + " PLL " + o.getDesc());
//                granTotal += total;
            }
            y += 60;
            printer.text(y += 30, ticketTimes);
            printer.text(y + 30, ticketTotal);

//        printer.text(y, "");
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

//    private void syncBuys() {
//        Log.i(TAG, "downloadUpdateTabs");
//        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
//        httpService.getBuys(mContext.getRegion(), mDate).enqueue(new Callback<List<Buy>>() {
//            @Override
//            public void onResponse(Call<List<Buy>> call, Response<List<Buy>> response) {
//                if (response.isSuccessful()) {
//                    processBuys(response.body());
//                } else {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Buy>> call, Throwable t) {
//                Log.e(TAG, mContext.getString(R.string.error_odc));
//                init();
//            }
//        });
//    }

//    private void processBuys(List<Buy> list) {
//        if (list != null) {
//            saveOrUpdate(list);
//        }
//    }

//    private void saveOrUpdate(List<Buy> list) {
//        db.insertOrUpdateBuys(list);
//    }

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
                } else {
                    b.setReceiptSheetCaptureId(db.insertOrUpdate(c));
                }
                items.add(b);
            }
        }

        db.insertOrReplaceInTx(items.toArray(new BuyDetail[items.size()]));
        return true;
    }

    private void setInProcess() {
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
        db.insertOrUpdate(receiptSheetCapture);
//        setItems();
    }

}
