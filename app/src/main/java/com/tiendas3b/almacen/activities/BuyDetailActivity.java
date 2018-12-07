package com.tiendas3b.almacen.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BuyDetailRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.BuyDetail;
import com.tiendas3b.almacen.db.dao.ComplaintCat;
import com.tiendas3b.almacen.db.dao.DecreaseMP;
import com.tiendas3b.almacen.db.dao.FMuestras;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetailCapture;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.ArticleCaptureDTO;
import com.tiendas3b.almacen.dto.CaptureDecrease;
import com.tiendas3b.almacen.dto.ReceiptSheetDetailCaptureDTO;
import com.tiendas3b.almacen.dto.receipt.FMuestrasDTO;
import com.tiendas3b.almacen.fragments.BuyDetailFragment;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.picking.orders.ScanTimeDTO;
import com.tiendas3b.almacen.receipt.InsertReceiptExpressLog;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.DialogUtil;
import com.tiendas3b.almacen.util.KeyboardUtil;
import com.tiendas3b.almacen.util.Preferences;
import com.tiendas3b.almacen.views.tables.vo.ArticleVO;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyDetailActivity extends AppCompatActivity implements BuyDetailFragment.OnFragmentInteractionListener {

    public static final int REQUEST_CODE = 56;
    private static final String TAG = "BuyDetailActivity";
    public static final String EXTRA_SCAN_TIME = "EXTRA_SCAN_TIME";
    private final Calendar c = Calendar.getInstance();
    private int billAmount;
    private int receivedAmount;
    private int sample;
    private String expiryDate;
    private String lote;
    private String comments;
    private long check;
    private String rejectionReason;
    private long rejectionReasonId;
    private int paletizado;
    private int packing;
    private int paletizadoCount;
    private GlobalState mContext;
    private VArticle vArticle;
    private IDatabaseManager db;
    private List<ArticleVO> articleVO = new ArrayList<>();

    private Chronometer chrTime;
    private ScanTimeDTO scanTime;
    private boolean isExpress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = (GlobalState) getApplicationContext();
        db = new DatabaseManager(mContext);

//        init();
    }

    private void init() {
        chrTime = findViewById(R.id.chrTime);

        if (scanTime != null) {
            chrTime.setBase(SystemClock.elapsedRealtime() - (new Date().getTime() - scanTime.getInitTimeMillis()));
            chrTime.start();
        }
    }

    @Override
    public void startTime() {
        chrTime.setBase(SystemClock.elapsedRealtime());
        chrTime.start();
        scanTime = new ScanTimeDTO();
        long now = new Date().getTime();
        scanTime.setInitTimeMillis(now);
        scanTime.setInitTime(DateUtil.getTimeWithoutDateStr(now));
    }

    @Override
    public void stopTime() {
        chrTime.stop();
        long now = new Date().getTime();
        scanTime.setEndTimeMillis(now);
        scanTime.setEndTime(DateUtil.getDateStr(now));
    }

    @Override
    public ScanTimeDTO getTimer() {
        return scanTime;
    }

    @Override
    public void setBaseTime(String date, String deliveryTime) {
        Intent intent = getIntent();
        scanTime = intent.getParcelableExtra(BuyDetailActivity.EXTRA_SCAN_TIME);
        chrTime = findViewById(R.id.chrTime);
        isExpress = scanTime != null;
        if(isExpress){
            scanTime = new ScanTimeDTO();
//            String[] split = deliveryTime.split(":");
//            scanTime.setInitTimeMillis(DateUtil.getTimeMillis(date, split[0], split[1]));
//            scanTime.setInitTime(deliveryTime);
//
//                chrTime.setBase(SystemClock.elapsedRealtime() - (new Date().getTime() - scanTime.getInitTimeMillis()));
//                chrTime.start();


            Date myTime = null, dateSys = null;
            SimpleDateFormat dateformate = new SimpleDateFormat("HH:mm:ss");
            String dateSystem = new SimpleDateFormat("HH:mm:ss").format(new Date());
            try {
                myTime = dateformate.parse(deliveryTime);
                dateSys = dateformate.parse(dateSystem);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Long now = dateSys.getTime() - myTime.getTime();

            System.out.println(now/3600000);
            chrTime.setBase(SystemClock.elapsedRealtime() - now);
            scanTime.setInitTimeMillis(now);
            chrTime.start();


        }
    }

    @Override
    public void onFragmentInteraction(BuyDetail buy) {
// =(
    }

    @Override
    public void onFragmentInteraction(BuyDetailRecyclerViewAdapter.BuyDetailHolder holder, int position) {
        BuyDetailFragment articleFrag = (BuyDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (articleFrag != null) {
            articleFrag.collapseAll(holder.llCaptureSheetDetail);
        }
    }

    @Override
    public void showCalendar(final EditText txtExpiryDate, int extraDays) {
        DialogUtil.showCalendarTodayPlusDaysMim(BuyDetailActivity.this, c, extraDays, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            txtExpiryDate.setText(DateUtil.getDateStr(year, month + 1, dayOfMonth));
            }
        });
    }

    @Override
    public boolean onSaveDetail(BuyDetailRecyclerViewAdapter.BuyDetailHolder holder) {




        if (isValid(holder)) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage( "Cajas faltantes: " + holder.txtMissingAmount.getText() + "\n" +
                    "Cajas recibidas: " + holder.txtReceivedAmount.getText() + "\n" +
                    getString(R.string.save_warning) );
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(getString(android.R.string.ok),
//                  (dialog, which) -> finish());
                    (dialog, which) -> {




            //TODO PROCESO DE MERMA, MUESTRA M.P.
            if (sample > 0){
                getArticle(holder.mItem.getArticle().getBarcode(), new Callback(){

                    @Override
                    public void onResponse(Call call, Response response) {


                        vArticle = (VArticle) response.body();
                        DecreaseMP decreaseMP = new DecreaseMP(vArticle.getIclave(), vArticle.getDescription().concat(" / ").concat(vArticle.getUnity()), sample, 20L,
                                                              01L, 30L, vArticle.getCost(),"", holder.mItem.getBuy().getFolio());
//                        decreaseMP.setIclave(vArticle.getIclave());
////                        decreaseMP.setDescription(vArticle.getDescription().concat(" / ").concat(vArticle.getUnity()));
////                        decreaseMP.setCost(vArticle.getCost());
////                        decreaseMP.setAmount(sample);
////                        decreaseMP.setType(20L);//TIPO CADUCIDAD
////                        decreaseMP.setObs(01L);//FIJO, ALM (MEMO)
////                        decreaseMP.setObsLog(30L);//OBS LOG MUESTRA MP
////                        decreaseMP.setOdc();
                        db.insertOrReplaceInTx(decreaseMP);

                        FMuestras fMuestras = new FMuestras(String.valueOf(mContext.getRegion()), holder.mItem.getArticle().getIclave(), holder.mItem.getBuy().getProviderId(), "",
                                holder.mItem.getBuy().getFolio(), lote, expiryDate, sample, 0);
                        db.insertOrReplaceInTx(fMuestras);
//                        FMuestrasDTO fMuestrasDTO = new FMuestrasDTO(String.valueOf(mContext.getRegion()), holder.mItem.getArticle().getIclave(), holder.mItem.getBuy().getProviderId(), "",
//                                holder.mItem.getBuy().getFolio(), "", lote, expiryDate, sample, 0);
//                        todo insertFMuestas(fMuestrasDTO);
                        save(holder);

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }else{
                save(holder);
            }

                    }
            );
            alertDialogBuilder.setNegativeButton(android.R.string.cancel, null);

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            holder.open = false;
            holder.llCaptureSheetDetail.setVisibility(View.GONE);
            KeyboardUtil.hide(this);
            return true;
        }
        return false;
    }

    //TODO OBTIENE INFORMACION PARA LLENAR TABLAS DE MERMA
    private void getArticle(String barcode, Callback callback){

        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.getArticle(barcode).enqueue(new Callback<VArticle>() {
            @Override
            public void onResponse(Call<VArticle> call, Response<VArticle> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                }

            }

            @Override
            public void onFailure(Call<VArticle> call, Throwable t) {
                Toast.makeText(mContext, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//                btnInit.setEnabled(true);
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    private void save(BuyDetailRecyclerViewAdapter.BuyDetailHolder holder) {
        BuyDetailFragment articleFrag = (BuyDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (articleFrag != null) {
            BuyDetail d = holder.mItem;
            Buy b = holder.mItem.getBuy();
            int estatus =  holder.mItem.getBuy().getChecked();
            ReceiptSheetDetailCaptureDTO c = new ReceiptSheetDetailCaptureDTO();
            c.setBuyDetailId(d.getId());
//            c.setBuyDetail(d);
            c.setBillAmount(billAmount);
            c.setOdcAmount(d.getArticleAmount());
            c.setReceivedAmount(receivedAmount);
            c.setSample(sample);
            c.setExpiryDate(expiryDate);
            c.setPaletizadoCount(paletizadoCount);
            c.setLote(lote);
            c.setComments(comments);
            c.setCheckId(check);
            c.setRejectionReasonStr(rejectionReason);
            c.setRejectionReasonId(rejectionReasonId);
            c.setPacking(packing);
            c.setPaletizado(paletizado);

            c.setRegionId(b.getRegionId());
            c.setProviderId(b.getProviderId());
//            c.setReceiptDate(DateUtil.getDateStr(b.getProgramedDate()));
            c.setReceiptDate(DateUtil.getTodayStr());
            c.setOdc(b.getFolio());
            c.setIclave(d.getArticle().getIclave());
            c.setDeviceId(Long.parseLong(new Preferences(mContext).getSharedStringSafe(Preferences.KEY_UID, "-1L")));
            send(c, articleFrag, estatus);

            //INSERTA LOG RECIBO EXPRESS
            InsertReceiptExpressLog insertReceiptExpressLog = new InsertReceiptExpressLog(db, mContext);
            insertReceiptExpressLog.insertRecExprLog(holder.mItem.getBuy(), Constants.IDLOG_CONFIRM_PROD, Constants.LOG_CONFIRM_PROD + "_"+ holder.mItem.getArticle().getIclave() );

        }

    }

    private void send(final ReceiptSheetDetailCaptureDTO rcdc, final BuyDetailFragment articleFrag, int estatus) {

        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.insertReceiptSheetDetail(rcdc).enqueue(new Callback<ReceiptSheetDetailCapture>() {
            @Override
            public void onResponse(Call<ReceiptSheetDetailCapture> call, Response<ReceiptSheetDetailCapture> response) {
                if (response.isSuccessful()) {
//                    saveDetail(response.body());
                    articleFrag.save(response.body());
                }
            }

            @Override
            public void onFailure(Call<ReceiptSheetDetailCapture> call, Throwable t) {
                Toast.makeText(mContext, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//                btnInit.setEnabled(true);
                Log.e(TAG, t.getLocalizedMessage());
            }
        });

        //on succes
//            selectedPosition = -1;//adapter
    }

//    private void saveDetail(ReceiptSheetDetailCapture c) {
//        IDatabaseManager databaseManager = new DatabaseManager(mContext);
//        databaseManager.insertOrReplaceInTx(c);
//        BuyDetail buyDet = databaseManager.getById(c.getBuyDetailId(), BuyDetail.class);
//        buyDet.setReceiptSheetCaptureId(c.getId());
//        databaseManager.update(buyDet);
//    }

    private boolean isValid(BuyDetailRecyclerViewAdapter.BuyDetailHolder h) {

        Editable billText = h.txtBillAmount.getText();
        if (TextUtils.isEmpty(billText)) {
            Toast.makeText(mContext, R.string.buy_bill_amount, Toast.LENGTH_LONG).show();
            return false;
        }

        int packingSize = h.mItem.getArticle().getPacking();
        billAmount = Integer.parseInt(billText.toString().replace(",", "")) * packingSize;//para seguir enviando piezas

        Editable receivedText = h.txtReceivedAmount.getText();
        if (TextUtils.isEmpty(receivedText)) {
            Toast.makeText(mContext, R.string.buy_received_amount, Toast.LENGTH_LONG).show();
            return false;
        }
        receivedAmount = Integer.parseInt(receivedText.toString().replace(",", "")) * packingSize;//para seguir enviando piezas


        Editable paletizadoText = h.txtPaletizadoCount.getText();
        if (TextUtils.isEmpty(paletizadoText)) {
            Toast.makeText(mContext, R.string.buy_paletizado_count, Toast.LENGTH_LONG).show();
            return false;
        }

        Editable sampleText = h.txtSample.getText();
        if (TextUtils.isEmpty(sampleText)) {
            Toast.makeText(mContext, R.string.buy_sample, Toast.LENGTH_LONG).show();
            return false;
        }

        Editable expiryDateText = h.txtExpiryDate.getText();
        if (TextUtils.isEmpty(expiryDateText)) {
            Toast.makeText(mContext, R.string.buy_expiry_date, Toast.LENGTH_LONG).show();
            return false;
        }

        sample = Integer.parseInt(sampleText.toString().replace(",", ""));

        lote = h.txtLote.getText().toString();
        if (TextUtils.isEmpty(lote)) {
            Toast.makeText(mContext, R.string.buy_lote, Toast.LENGTH_LONG).show();
            return false;
        }


        paletizadoCount = Integer.parseInt(paletizadoText.toString().replace(",", ""));

        //TODO validar
        expiryDate = h.txtExpiryDate.getText().toString();
        comments = h.txtComments.getText().toString();

        paletizado = h.spnPaletizado.getSpinner().getSelectedItemPosition();
        packing = h.spnPacking.getSpinner().getSelectedItemPosition();
        check = h.spnCheck.getSpinner().getSelectedItemId();
        ComplaintCat rr = (ComplaintCat) h.spnRejectionReason.getSpinner().getSelectedItem();
        rejectionReason = rr.getDescription();
        rejectionReasonId = rr.getId();

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //TODO cambiar menu si ya se capturaron detalles
            }
        }
    }
}
