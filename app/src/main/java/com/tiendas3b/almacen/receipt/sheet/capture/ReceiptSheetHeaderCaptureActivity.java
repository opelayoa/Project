package com.tiendas3b.almacen.receipt.sheet.capture;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.satsuware.usefulviews.LabelledSpinner;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.SaveMenuActivity;
import com.tiendas3b.almacen.adapters.GenericSpinnerAdapter;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.DateType;
import com.tiendas3b.almacen.db.dao.Level;
import com.tiendas3b.almacen.db.dao.ReceiptSheetCapture;
import com.tiendas3b.almacen.db.dao.Region;
import com.tiendas3b.almacen.dto.BuyDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.receipt.InsertReceiptExpressLog;
import com.tiendas3b.almacen.receipt.sheet.capture.dto.CaptureReceiptSheet;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.DialogUtil;
import com.tiendas3b.almacen.util.KeyboardUtil;
import com.tiendas3b.almacen.util.NumbersUtil;
import com.tiendas3b.almacen.util.OnFocusChangeListenerMoney;
import com.tiendas3b.almacen.util.TextWatcherMoney;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiptSheetHeaderCaptureActivity extends SaveMenuActivity {

    public static final String ODC = "ODC";
    private static final int MAX_STAFF_NUM = 5;
    private static final String TIME_ZERO = "00:00";
    private static final String TAG = "RSHCA";
    private static final int MIN_DELIVERY_TIME = 1;
    private static final int NOT_ARRIVE = 0;
    private static final int ARRIVE = 1;

    @BindView(R.id.lblProviderName)
    TextView lblProviderName;
    @BindView(R.id.lblOdc)
    TextView lblOdc;
    @BindView(R.id.swtArrive)
    Switch swtArrive;
    @BindView(R.id.spnLevel)
    LabelledSpinner spnLevel;
    @BindView(R.id.spnDate)
    LabelledSpinner spnDate;
    @BindView(R.id.spnStaffNum)
    LabelledSpinner spnStaffNum;
    @BindView(R.id.spnPlatform)
    LabelledSpinner spnPlatform;
    @BindView(R.id.txtDateHr)
    EditText txtDateHr;
    @BindView(R.id.txtArriveHr)
    EditText txtArriveHr;
    @BindView(R.id.txtDeliveryHr)
    EditText txtDeliveryHr;
    @BindView(R.id.txtDepartureHr)
    EditText txtDepartureHr;
    @BindView(R.id.txtDeliveryman)
    EditText txtDeliveryman;
    @BindView(R.id.txtReceiver)
    EditText txtReceiver;
    @BindView(R.id.txtFolio)
    EditText txtFolio;
    @BindView(R.id.txtSubtotal)
    EditText txtSubtotal;
    @BindView(R.id.txtTotal)
    EditText txtTotal;
    @BindView(R.id.txtIeps)
    EditText txtIeps;
    @BindView(R.id.txtIva)
    EditText txtIva;

    private Menu menu;
    private long levelId;
    private long dateTypeId;
    private int staffNum;
    private int platform;
    private Buy odc;
    private final Calendar c = Calendar.getInstance();
    //    private Date dateTime;
//    private Date arriveTime;
//    private Date deliveryTime;
//    private Date departureTime;
    private String dateTimeStr;
    private String arriveTimeStr;
    private String deliveryTimeStr;
    //    private String departureTimeStr;
    private long providerId;
    private long regionId;
    private int odcFolio;
    private int arrive;
    private GenericSpinnerAdapter levelAdapter;
    private GenericSpinnerAdapter dateAdapter;
    private Float total;
    private Float subtotal;
    private Float ieps;
    private Float iva;
    private String facturaRef;
    private String deliveryman;
    private String receiver;
    private Boolean focus;

    @Override
    protected void saveData() {
        if (isValid()) {
            save();
        }
    }

    private void save() {
        ReceiptSheetCapture r = db.findReceiptSheetCaptureBy(odc.getId());
        if (r == null) {
            r = new ReceiptSheetCapture();
        }
//        r.setRegionId(regionId);
//        r.setProviderId(providerId);
        r.setReceiptDate(DateUtil.getTodayStr());
        r.setOdc(odcFolio);
        r.setLevelId(levelId);
        r.setDateTime(dateTimeStr);
        r.setArriveTime(arriveTimeStr);
//        r.setDeliveryTime(deliveryTimeStr);
//        r.setDepartureTime(departureTimeStr);
        r.setArrive(arrive);
        r.setNumReceivers(staffNum);
        r.setDateTypeId(dateTypeId);
//        r.setPaletizado();//0-granel, 1-paletizado, 2 mixto
//        r.setAmountEm(subtotal);//
        r.setFacturaRef(facturaRef);//ventana
        r.setAmountFact(subtotal);//ventana
        r.setIeps(ieps);//ventana
        r.setIva(iva);//ventana
        r.setDeliveryman(deliveryman);
        r.setReceiver(receiver);
        r.setPlatform(platform);
        r.setDeliveryman(deliveryman);
        r.setReceiver(receiver);
        r.setBuyId(odc.getId());

        Buy buy = db.findBuyByFolio(odc.getFolio(),mContext.getRegion());
        r.setPaletizado(buy.getProvider().getMmpConfig().getP2());

        send(r);
    }

    private void send(ReceiptSheetCapture r) {
        CaptureReceiptSheet c = new CaptureReceiptSheet();
        c.setCapture(r);
        BuyDTO buyDTO = new BuyDTO();
        buyDTO.setId(odc.getId());
        buyDTO.setPlatform(odc.getPlatform());
        buyDTO.setTime(odc.getTime());
        buyDTO.setDeliveryTime(odc.getDeliveryTime());
        buyDTO.setChecked(odc.getChecked());
        buyDTO.setFolio(odc.getFolio());
        buyDTO.setForced(odc.getForced());
        buyDTO.setMov(odc.getMov());
        buyDTO.setProgramedDate(DateUtil.getDateStr(odc.getProgramedDate()));
        buyDTO.setProviderId(odc.getProviderId());
        buyDTO.setRegionId(odc.getRegionId());
        buyDTO.setTotal(odc.getTotal());
        c.setBuy(buyDTO);
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.insertReceiptSheet(c).enqueue(new Callback<ReceiptSheetCapture>() {
            @Override
            public void onResponse(Call<ReceiptSheetCapture> call, Response<ReceiptSheetCapture> response) {
                if (response.isSuccessful()) {
                    ReceiptSheetCapture res = response.body ();
                    if (res == null) {
                        Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
                    } else {

                        //INSERTA LOG RECIBO EXPRESS
                        InsertReceiptExpressLog insertReceiptExpressLog = new InsertReceiptExpressLog(db, mContext);
                        insertReceiptExpressLog.insertRecExprLog(odc, Constants.IDLOG_SAVE_REC_EXP, Constants.LOG_SAVE_REC_EXP);

                        db.insertOrUpdate(res);
                        Toast.makeText(mContext, "Guardado", Toast.LENGTH_LONG).show();
                        setResultOk();

                    }
                }
            }

            @Override
            public void onFailure(Call<ReceiptSheetCapture> call, Throwable t) {
                Toast.makeText(mContext, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//                btnInit.setEnabled(true);
                Log.e(TAG, "insertReceiptSheet");
            }
        });
    }

    //    @Override
    protected boolean isValid() {
        staffNum = (int) spnStaffNum.getSpinner().getSelectedItem();
        platform = (int) spnPlatform.getSpinner().getSelectedItem();
        levelId = spnLevel.getSpinner().getSelectedItemId();
        dateTypeId = spnDate.getSpinner().getSelectedItemId();
        providerId = odc.getProvider().getId();
        regionId = mContext.getRegion();
        odcFolio = odc.getFolio();
        arriveTimeStr = txtArriveHr.getText().toString();
        if ("".equals(arriveTimeStr) || !DateUtil.validateTime(arriveTimeStr)) {
            Toast.makeText(this, R.string.rsheet_arrive_time_error, Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean arriveB = swtArrive.isChecked();
        if (arriveB) {
            arrive = ARRIVE;

            if (staffNum < 1) {
                Toast.makeText(this, R.string.rsheet_staff_error, Toast.LENGTH_SHORT).show();
                return false;
            }

//        deliveryTimeStr = txtDeliveryHr.getText().toString();
//        if ("".equals(deliveryTimeStr) || !DateUtil.validateTime(deliveryTimeStr)) {
//            Toast.makeText(this, R.string.rsheet_delivery_time_error, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        departureTimeStr = txtDepartureHr.getText().toString();
//        if ("".equals(departureTimeStr) || !DateUtil.validateTime(departureTimeStr)) {
//            Toast.makeText(this, R.string.rsheet_departure_time_error, Toast.LENGTH_SHORT).show();
//            return false;
//        }

            deliveryman = txtDeliveryman.getText().toString().trim();
            if (TextUtils.isEmpty(deliveryman)) {
                Toast.makeText(this, R.string.rsheet_deliveryman_error, Toast.LENGTH_LONG).show();
                return false;
            }

            receiver = txtReceiver.getText().toString().trim();
            if (TextUtils.isEmpty(receiver)) {
                Toast.makeText(this, R.string.rsheet_receiver_error, Toast.LENGTH_LONG).show();
                return false;
            }

            facturaRef = txtFolio.getText().toString();
            if (TextUtils.isEmpty(facturaRef)) {
                Toast.makeText(this, R.string.rsheet_factura_ref_error, Toast.LENGTH_LONG).show();
                return false;
            }

            String totalStr = txtTotal.getText().toString().replaceAll("[$,]", "");
            if (TextUtils.isEmpty(totalStr)) {
                Toast.makeText(this, R.string.rsheet_total_error, Toast.LENGTH_LONG).show();
                return false;
            }
            total = Float.parseFloat(totalStr);
            if (total == 0) {
                Toast.makeText(this, R.string.rsheet_total_error, Toast.LENGTH_LONG).show();
            }

            String subtotalStr = txtSubtotal.getText().toString().replaceAll("[$,]", "");
            if (TextUtils.isEmpty(subtotalStr)) {
                Toast.makeText(this, R.string.rsheet_subtotal_error, Toast.LENGTH_LONG).show();
                return true;
            }
            subtotal = Float.parseFloat(subtotalStr);
            if (subtotal == 0) {
                Toast.makeText(this, R.string.rsheet_subtotal_error, Toast.LENGTH_LONG).show();
            }

            String iepsStr = txtIeps.getText().toString().replaceAll("[$,]", "");
            if (TextUtils.isEmpty(iepsStr)) {
                Toast.makeText(this, R.string.rsheet_ieps_error, Toast.LENGTH_LONG).show();
                return true;
            }
            ieps = Float.parseFloat(iepsStr);
            if(ieps > subtotal){
                Toast.makeText(this, R.string.rsheet_ieps_error, Toast.LENGTH_LONG).show();
                return true;
            }

            String ivaStr = txtIva.getText().toString().replaceAll("[$,]", "");
            if (TextUtils.isEmpty(ivaStr)) {
                Toast.makeText(this, R.string.rsheet_iva_error, Toast.LENGTH_LONG).show();
                return true;
            }
            iva = Float.parseFloat(ivaStr);
            if(iva > subtotal){
                Toast.makeText(this, R.string.rsheet_iva_error, Toast.LENGTH_LONG).show();
                return true;
            }

        } else {
            arrive = NOT_ARRIVE;
        }

        return true;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_sheet_header_detail);
        setup();

//        txtFolio.setFocusable(true);s

        txtDeliveryHr.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txtFolio.setFocusableInTouchMode(true);
                txtTotal.setFocusableInTouchMode(true);
                return false;
            }

        });


    }

    @Override
    protected void init() {
        Long buyId = getIntent().getLongExtra(ODC, 0L);
        odc = db.getById(buyId, Buy.class);
        downloadInfo();
        setLevelSpinner();
        setDateSpinner();
        setStaffSpinner();
        setPlatformSpinner();
        lblProviderName.setText(odc.getProvider().getName());
        lblOdc.setText("ODC: ".concat(String.valueOf(odc.getFolio())));

        setSwitchListener();
        setTextWatchers();

//        processBarcode();
    }

    private void downloadInfo() {

        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getReceiptSheetCapture(mContext.getRegion(), DateUtil.getTodayStr(), odc.getFolio(), odc.getProviderId(), odc.getId())
//        httpService.getReceiptSheetCapture(mContext.getRegion(), DateUtil.getDateStr(odc.getProgramedDate()), odc.getFolio(), odc.getProviderId(), odc.getId())
                .enqueue(new Callback<ReceiptSheetCapture>() {
                    @Override
                    public void onResponse(Call<ReceiptSheetCapture> call, Response<ReceiptSheetCapture> response) {
                        if (response.isSuccessful()) {
                            ReceiptSheetCapture r = response.body();
                            if (r != null) {
                                save(r);
                            }
                            fillFields();
                        } else {
//                    viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//                    viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ReceiptSheetCapture> call, Throwable t) {
                        Log.e(TAG, "Error getReceiptSheetCapture");
//                viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//                viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
//                viewSwitcher.setDisplayedChild(1);
                    }
                });


//        Intent msgIntent = new Intent(this, SyncViewArticlesService.class);
//        msgIntent.putExtra("messenger", new Messenger(new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//////                Bundle reply = msg.getData();
////                progressBar.setVisibility(View.INVISIBLE);
////                btnSync.setVisible(true);
////                lblLastUpgrade.setText(getLastUpdate());
////                // do whatever with the bundle here
//                processBarcode();
//            }
//        }));
//        btnSave.setVisible(false);
//        progressBar.setVisibility(View.VISIBLE);
//        startService(msgIntent);
//
//        ReceiptDownloads.startActionFoo(mContext, );

    }

    private void save(ReceiptSheetCapture receiptSheetCapture) {
        ReceiptSheetCapture r = db.findReceiptSheetCaptureBy(odc.getId());
        if (r != null) {
            receiptSheetCapture.setId(r.getId());
        }
        db.insertOrUpdate(receiptSheetCapture);
    }

    private void setTextWatchers() {
        txtTotal.addTextChangedListener(new TextWatcherMoney(txtTotal));
        txtSubtotal.addTextChangedListener(new TextWatcherMoney(txtSubtotal));
        txtIeps.addTextChangedListener(new TextWatcherMoney(txtIeps));
        txtIeps.setOnFocusChangeListener(new OnFocusChangeListenerMoney(txtIeps));
        txtIva.addTextChangedListener(new TextWatcherMoney(txtIva));
        txtIva.setOnFocusChangeListener(new OnFocusChangeListenerMoney(txtIva));
    }

    private void fillFields() {
        ReceiptSheetCapture rsCapture = db.findReceiptSheetCaptureBy(odc.getId());
        if (rsCapture == null) {
            dateTimeStr = odc.getTime();
            if (getString(R.string.no_timetable).equals(dateTimeStr)) {
                dateTimeStr = "00:00";
            }
            spnPlatform.getSpinner().setSelection(odc.getPlatform() - 1);
            deliveryTimeStr = DateUtil.getTimeNowStr();
        } else {
            deliveryTimeStr = rsCapture.getDeliveryTime();
            dateTimeStr = rsCapture.getDateTime();
            txtArriveHr.setText(rsCapture.getArriveTime());
            txtDeliveryHr.setText(deliveryTimeStr);
            txtDepartureHr.setText(rsCapture.getDepartureTime());
            swtArrive.setChecked(rsCapture.getArrive() == ARRIVE);
//            spnLevel.getSpinner().setSelection(levelAdapter.getPosition(rsCapture.getLevel()));
//            spnDate.getSpinner().setSelection(dateAdapter.getPosition(rsCapture.getDateType()));
            spnPlatform.getSpinner().setSelection(rsCapture.getPlatform() - 1);
            Integer staff = rsCapture.getNumReceivers();
            if(staff != null){
                spnStaffNum.getSpinner().setSelection(staff);
            }
            swtArrive.setEnabled(false);
            txtDeliveryman.setText(rsCapture.getDeliveryman());
            txtReceiver.setText(rsCapture.getReceiver());
            txtFolio.setText(rsCapture.getFacturaRef());
            txtTotal.setText(NumbersUtil.moneyFormat(rsCapture.getAmountEm()));
            txtSubtotal.setText(NumbersUtil.moneyFormat(rsCapture.getAmountFact()));
            txtIeps.setText(NumbersUtil.moneyFormat(rsCapture.getIeps()));
            txtIva.setText(NumbersUtil.moneyFormat(rsCapture.getIva()));
            if(odc.getChecked() == Constants.NOT_ARRIVE){ //VALIDAR AUTOMATICOS
                getMenu().clear();
            }
        }
        txtDateHr.setText(dateTimeStr);
        setTimeListeners();
//        setSwitchListener();
//        setTextWatchers();
    }

    private void setSwitchListener() {
        swtArrive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                setEnabled(isChecked);
            }
        });
    }

    private void setEnabled(boolean isChecked) {
        txtArriveHr.setEnabled(isChecked);
        txtDeliveryHr.setEnabled(isChecked);
        txtDepartureHr.setEnabled(isChecked);
        txtDeliveryman.setEnabled(isChecked);
        txtReceiver.setEnabled(isChecked);
        txtFolio.setEnabled(isChecked);
        txtTotal.setEnabled(isChecked);
        txtSubtotal.setEnabled(isChecked);
        txtIeps.setEnabled(isChecked);
        txtIva.setEnabled(isChecked);
        spnStaffNum.getSpinner().setEnabled(isChecked);
        if (!isChecked) {
            spnStaffNum.setSelection(0);
//            spnPlatform.setSelection(0);
            spnDate.setSelection(1);
            spnLevel.setSelection(2);
            txtArriveHr.setText(TIME_ZERO);
//            txtDeliveryHr.setText(TIME_ZERO);
//            txtDepartureHr.setText(TIME_ZERO);

        }
    }

    private void setPlatformSpinner() {
        int platformNum = db.getById(mContext.getRegion(), Region.class).getPlatformNum();
        Integer[] platforms = new Integer[platformNum];
        for (int i = 0; i < platformNum; ) {
            platforms[i++] = i;
        }
        ArrayAdapter<Integer> platformAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simple_spinner_item_t3b_black, platforms);
        platformAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPlatform.setCustomAdapter(platformAdapter);
//        spnPlatform.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
//            @Override
//            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
//                platform = position;
//            }
//
//            @Override
//            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
//
//            }
//        });
    }

    private void setStaffSpinner() {
        Integer[] staff = new Integer[MAX_STAFF_NUM];
        for (int i = 0; i < MAX_STAFF_NUM; ) {
            staff[i] = i++;
        }
        ArrayAdapter<Integer> platformAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simple_spinner_item_t3b_black, staff);
        platformAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStaffNum.setCustomAdapter(platformAdapter);
        spnStaffNum.setSelection(1);
//        spnStaffNum.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
//            @Override
//            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
//                staffNum = id;
//            }
//
//            @Override
//            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
//
//            }
//        });
    }

    private void setTimeListeners() {
        txtArriveHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.hasFocus()) {
                    showClock();
                }
//                else {
//                    arriveTimeStr = txtArriveHr.getText().toString();
//                }
            }
        });
        txtArriveHr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showClock();
                }
//                else {
//                    arriveTimeStr = txtArriveHr.getText().toString();
//                }
            }
        });

//        txtDeliveryHr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (view.hasFocus()) {
//                    showClock2();
//                } else {
//                    deliveryTimeStr = txtDeliveryHr.getText().toString();
//                }
//            }
//        });
//        txtDeliveryHr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    showClock2();
//                } else {
//                    deliveryTimeStr = txtDeliveryHr.getText().toString();
//                }
//            }
//        });
//
//        txtDepartureHr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (view.hasFocus()) {
//                    showClock3();
//                } else {
//                    departureTimeStr = txtDepartureHr.getText().toString();
//                }
//            }
//        });
//        txtDepartureHr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    showClock3();
//                } else {
//                    departureTimeStr = txtDepartureHr.getText().toString();
//                }
//            }
//        });

    }

    private void setDateSpinner() {
        List<DateType> dateTypes = db.listAll(DateType.class);
        dateAdapter = new GenericSpinnerAdapter<>(this, R.layout.spinner_item_table, dateTypes);
        spnDate.setCustomAdapter(dateAdapter);
        spnDate.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                dateTypeId = id;
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });
    }

    private void setLevelSpinner() {
        List<Level> levels = db.listAll(Level.class);
        levelAdapter = new GenericSpinnerAdapter<>(this, R.layout.spinner_item_table, levels);
        spnLevel.setCustomAdapter(levelAdapter);
        spnLevel.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                levelId = id;
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });
    }

    private void showClock() {
        KeyboardUtil.hide(this);
//        DialogUtil.showClock(ReceiptSheetHeaderCaptureActivity.this, c, new TimePickerDialog.OnTimeSetListener() {
        String[] now = deliveryTimeStr.split(":");
        DialogUtil.showRangeClock(ReceiptSheetHeaderCaptureActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                arriveTimeStr = DateUtil.getTimeStr(hour, minute);
                txtArriveHr.setText(arriveTimeStr);
//                txtDeliveryHr.requestFocus();
            }
        }, R.string.dialog_tittle_arrive_time, 0, 0, Integer.parseInt(now[0]), Integer.parseInt(now[1]));
    }

//    private void showClock2() {
//        if (TextUtils.isEmpty(arriveTimeStr) || !DateUtil.validateTime(arriveTimeStr)) {
//            Toast.makeText(mContext, R.string.rsheet_arrive_time_error, Toast.LENGTH_LONG).show();
//        } else {
//            String[] split = arriveTimeStr.split(":");
//            KeyboardUtil.hide(this);
//            int hour = parseInt(split[0]);
//            DialogUtil.showRangeClock(ReceiptSheetHeaderCaptureActivity.this, DateUtil.getAmOrPm(hour), new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
////                    try {
////                        deliveryTime = DateUtil.getTime(hour, minute);
//                    deliveryTimeStr = DateUtil.getTimeStr(hour, minute);
//                    txtDeliveryHr.setText(deliveryTimeStr);
//                    txtDepartureHr.requestFocus();
////                    } catch (ParseException e) {
////                        e.printStackTrace();
////                    }
//                }
//            }, R.string.dialog_tittle_delivery_time, hour, parseInt(split[1]));
//            KeyboardUtil.hide(this);
//        }
//    }
//
//    private void showClock3() {
//        if (TextUtils.isEmpty(deliveryTimeStr) || !DateUtil.validateTime(deliveryTimeStr)) {
//            Toast.makeText(mContext, R.string.rsheet_delivery_time_error, Toast.LENGTH_LONG).show();
//        } else {
//            String[] split = deliveryTimeStr.split(":");
//            KeyboardUtil.hide(this);
//            int hour = Integer.parseInt(split[0]);
//            DialogUtil.showRangeClock(ReceiptSheetHeaderCaptureActivity.this, DateUtil.getAmOrPm(hour), new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
////                    try {
////                        departureTime = DateUtil.getTime(hour, minute);
//                    departureTimeStr = DateUtil.getTimeStr(hour, minute);
//                    txtDepartureHr.setText(departureTimeStr);
////                    } catch (ParseException e) {
////                        e.printStackTrace();
////                    }
//                }
//            }, R.string.dialog_tittle_departure_time, hour, parseInt(split[1]) + MIN_DELIVERY_TIME);
//            KeyboardUtil.hide(this);
//        }
//    }


}
