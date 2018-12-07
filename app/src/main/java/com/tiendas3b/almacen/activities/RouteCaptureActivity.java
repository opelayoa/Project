package com.tiendas3b.almacen.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.satsuware.usefulviews.LabelledSpinner;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.AutoCompleteAdapter;
import com.tiendas3b.almacen.adapters.GenericSpinnerAdapter;
import com.tiendas3b.almacen.db.dao.Activity;
import com.tiendas3b.almacen.db.dao.Roadmap;
import com.tiendas3b.almacen.db.dao.RoadmapDetail;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.services.TrackingService;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.DialogUtil;
import com.tiendas3b.almacen.util.FileUtil;
import com.tiendas3b.almacen.util.KeyboardUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RouteCaptureActivity extends SaveMenuActivity {

    private static final String TAG = "RouteCaptureActivity";
    private static final int UPLOAD = 1;
    private static final int DOWNLOAD = 2;
    private static final int DIESEL = 3;
    private static final int RETURN = 4;
    private static final int ROUTE = 5;
    private static final int CHECK = 6;
    private static final int OTHERS = 7;
    private static final int KM_LIMIT = 100;
    private static final String RETURN_STR = "Regreso";
    @BindView(R.id.spnActivity)
    LabelledSpinner spnActivity;
    @BindView(R.id.spnScaffoldAmount)
    LabelledSpinner spnScaffoldAmount;
    @BindView(R.id.txtOrigin)
    AutoCompleteTextView txtOrigin;
    @BindView(R.id.txtDestination)
    AutoCompleteTextView txtDestination;
    @BindView(R.id.txtInitialKm)
    EditText txtInitialKm;
    @BindView(R.id.txtFinalKm)
    EditText txtFinalKm;
    @BindView(R.id.txtInitialTime)
    EditText txtInitialTime;
    @BindView(R.id.txtFinalTime)
    EditText txtFinalTime;
    @BindView(R.id.txtCash)
    EditText txtCash;
    @BindView(R.id.txtIave)
    EditText txtIave;
    @BindView(R.id.spnScaffoldDownloadedAmount)
    LabelledSpinner spnScaffoldDownloadedAmount;
    @BindView(R.id.txtDiesel)
    EditText txtDiesel;
    @BindView(R.id.txtStore)
    EditText txtOtherCost;
    @BindView(R.id.txtLabel)
    EditText txtLabel;

    private GlobalState mContext;
    private IDatabaseManager db;
    private long originId;
    private long destinationId;
    private String initialTime;
    private String finalTime;
    private long roadmapId;
    private String endDate;
    private Integer scaffoldUpload;
    private Integer scaffoldDownload;
    private int initialKm;
    private int finalKm;
    private Float toll;
    //    private Float iave;
    private Float diesel;
    private Float trafficTicket;
    private Float otherCost;
//    private String notes;
    private long activityId;
    private long regionId;
    private RoadmapDetail lastActivity;
    private int scaffoldRoadmap;
    private List<Store> stores;

    @Override
    protected void saveData() {
        if (isValid()) {
            save();
//            handleTrackingservice();//TODO activar
            setResultOk();
        }
    }

    private void save() {
        RoadmapDetail item = new RoadmapDetail();
        item.setScaffoldUpload(scaffoldUpload);
        item.setScaffoldDownload(scaffoldDownload);
        item.setInitialKm(initialKm);
        item.setFinalKm(finalKm);
        item.setInitialTime(initialTime);
        item.setFinalTime(finalTime);
        item.setCash(toll);
        item.setIave(trafficTicket);
        item.setDiesel(diesel);
        item.setTrafficTicket(trafficTicket);
        item.setOtherCost(otherCost);
//        item.setNotes(notes);
        item.setSync(false);
        item.setOriginId(originId);
        item.setDestinationId(destinationId);
        item.setActiviyId(activityId);
        item.setRoadmapId(roadmapId);
        item.setSync(false);
        item.setEndDate(endDate);
        db.insertOrReplaceInTx(item);
        if (scaffoldRoadmap > 0) {
            Roadmap roadmap = lastActivity.getRoadmap();
            roadmap.setScaffolds(scaffoldRoadmap);
            db.insertOrReplaceInTx(roadmap);
        }
    }

    private void handleTrackingservice() {
        if (activityId == Constants.UPLOAD_ACTIVITY_ID) {
            FileUtil.writeFile(TAG + ": start TrackingService");
            Intent i = new Intent(this, TrackingService.class);
            startService(i);
        } else if (activityId == Constants.RETURN_ACTIVITY_ID) {
            TrackingService.instance.stopSelf();
        }
    }

//    @Override
    protected boolean isValid() {
        activityId = ((Activity) spnActivity.getSpinner().getSelectedItem()).getId();
        scaffoldUpload = Integer.parseInt((String) spnScaffoldAmount.getSpinner().getSelectedItem());
        if (scaffoldUpload > 0) {
            scaffoldRoadmap = scaffoldUpload;
        }
        scaffoldDownload = Integer.parseInt((String) spnScaffoldDownloadedAmount.getSpinner().getSelectedItem());
        if (scaffoldDownload > 0) {
            int scaffolds = lastActivity.getRoadmap().getScaffolds();
            scaffoldRoadmap = scaffolds - scaffoldDownload;
            if (scaffoldRoadmap < 0) {
                Log.e(TAG, "segun yo nunca entra aqui XD");
                return false;
            }
        }

        String originStr = txtOrigin.getText().toString();
        if ("".equals(originStr)) {
            Toast.makeText(this, "Origen vacio", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "txtOrigin");
            return false;
        }
        Store origin = db.getById(Long.parseLong(originStr), Store.class);
        if (origin == null) {
            Toast.makeText(this, "Origen no válido", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "origin null");
            return false;
        }

        String destinationStr = txtDestination.getText().toString();
        if ("".equals(destinationStr)) {
            Toast.makeText(this, "Destino vacio", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "destination");
            return false;
        }
        Store destination = db.getById(Long.parseLong(destinationStr), Store.class);
//        dbStore.closeDbConnections();
        if (destination == null) {
            Toast.makeText(this, "Destino no válido", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "destination null");
            return false;
        }
        originId = origin.getId();
        destinationId = destination.getId();

        String initialKmStr = txtInitialKm.getText().toString();
        if ("".equals(initialKmStr)) {
            Toast.makeText(this, "Km inicail vacio", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "initialKmStr");
            return false;
        }
        initialKm = Integer.parseInt(initialKmStr);

        String finalKmStr = txtFinalKm.getText().toString();
        if ("".equals(finalKmStr)) {
            Toast.makeText(this, "Km final vacio", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "finalKmStr");
            return false;
        }
        finalKm = Integer.parseInt(finalKmStr);

        if (finalKm < initialKm) {
            Toast.makeText(this, "Km final menor a Km inicial", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "finalKm < initialKm");
            return false;
        }

        String initialTimeStr = txtInitialTime.getText().toString();
        if ("".equals(initialTimeStr)) {
            Toast.makeText(this, "Hr inicio vacio", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "initialTimeStr");
            return false;
        }
        initialTime = initialTimeStr;

        String finalTimeStr = txtFinalTime.getText().toString();
        if ("".equals(finalTimeStr)) {
            Toast.makeText(this, "Hr fin vacio", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "finalTimeStr");
            return false;
        }
        finalTime = finalTimeStr;

        if (initialTime.equals(finalTime)) {
            Toast.makeText(this, "Misma hora", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "same time");
            return false;
        }

//        try {
//            if (DateUtil.after(initialTime, finalTime)) {
//                Toast.makeText(this, "Hr fin mayor a Hr inicio", Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "initialDate.after(finalDate)");
//                return false;
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        if (activityId == Constants.DIESEL_ACTIVITY_ID) {
            if ("".equals(txtDiesel.getText().toString())) {
                Toast.makeText(this, "Diesel vacío", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (activityId == Constants.ROUTE_ACTIVITY_ID || activityId == Constants.RETURN_ACTIVITY_ID) {
            if (initialKm == finalKm) {
                Toast.makeText(this, "Mismo kilometraje", Toast.LENGTH_SHORT).show();
                return false;
            }
//            else if ((finalKm - initialKm) > KM_LIMIT){
//
//            }
        }

        String cashStr = txtCash.getText().toString();
        toll = Float.valueOf("".equals(cashStr) ? "0" : cashStr);
        String dieselStr = txtDiesel.getText().toString();
        diesel = Float.valueOf("".equals(dieselStr) ? "0" : dieselStr);
        String iaveStr = txtIave.getText().toString();
        trafficTicket = Float.valueOf("".equals(iaveStr) ? "0" : iaveStr);
        String otherCostStr = txtOtherCost.getText().toString();
        otherCost = Float.valueOf("".equals(otherCostStr) ? "0" : otherCostStr);

        endDate = DateUtil.getDateStr(new Date());
        Log.i(TAG, "valid!");
        return true;
    }

//    @Override
//    protected void showWarningDialog() {
//        DialogUtil.showAlertDialog(RouteCaptureActivity.this, R.string.route_warning_message, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                save();
//                setResultOk();
//            }
//        });
//    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_capture);
        mContext = (GlobalState) getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        View current = getCurrentFocus();
//        if (current != null) current.clearFocus();
//        txtOrigin.clearFocus();
//        spnActivity.requestFocus();
//        getWindow().getDecorView().clearFocus();
//    }

    @SuppressWarnings("unchecked")
    protected void init() {
        db = new DatabaseManager(this);
        regionId = mContext.getRegion();
        ButterKnife.bind(this);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        setLastActivity();

        List activities = getActivitiesAvailable();
        GenericSpinnerAdapter<Activity> activityAdapter = new GenericSpinnerAdapter(this, R.layout.spinner_item_table, activities);
        spnActivity.setCustomAdapter(activityAdapter);
        spnActivity.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {

            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                switch ((int) id) {
                    case UPLOAD:
                        showUpload();
                        break;
                    case DOWNLOAD:
                        showDownload();
                        break;
                    case DIESEL:
                        showDiesel();
                        break;
                    case RETURN:
                        showReturn();
                        break;
                    case ROUTE:
                        showRoute();
                        break;
                    case CHECK:
                        showCheck();
                        break;
                    case OTHERS:
                        showOthers();
                        break;
                }
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> sacaffoldSizeAdapter = ArrayAdapter.createFromResource(this, R.array.truck_scaffold_size_array, android.R.layout.simple_spinner_item);
        sacaffoldSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnScaffoldAmount.setCustomAdapter(sacaffoldSizeAdapter);

        ArrayAdapter<CharSequence> sacaffoldDownloadSizeAdapter = ArrayAdapter.createFromResource(this, R.array.truck_scaffold_download_size_array, android.R.layout.simple_spinner_item);
        sacaffoldDownloadSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnScaffoldDownloadedAmount.setCustomAdapter(sacaffoldDownloadSizeAdapter);

        stores = db.listStores(mContext.getRegion());
        AutoCompleteAdapter<Store> originAdapter = new AutoCompleteAdapter<>(this, R.layout.row_item_autocomplete, stores);
        txtOrigin.setAdapter(originAdapter);
        txtOrigin.setThreshold(1);

        AutoCompleteAdapter<Store> destinationAdapter = new AutoCompleteAdapter<>(this, R.layout.row_item_autocomplete, stores);
        txtDestination.setAdapter(destinationAdapter);
        txtDestination.setThreshold(1);

        txtInitialTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showClock();
                }
            }
        });
        txtInitialTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClock();
            }
        });

        txtFinalTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showClock2();
                }
            }
        });
        txtFinalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClock2();
            }
        });
    }

    private List<Activity> getActivitiesAvailable() {
        List<Activity> activities;
        switch ((int) lastActivity.getActiviyId()) {
            case UPLOAD:
                activities = db.listActivities((long) ROUTE);
                break;
            case DOWNLOAD:
                activities = db.listActivities((long) ROUTE, (long) RETURN);
                break;
            case DIESEL:
                activities = db.listActivities((long) ROUTE, (long) RETURN);
                break;
            case RETURN:
                activities = returnFilter();
                break;
            case ROUTE:
                activities = routeFilter();
                break;
            case CHECK:
                activities = db.listActivities((long) UPLOAD);
                break;
            case OTHERS:
                activities = db.listActivities();
                break;
            default:
                activities = db.listAll(Activity.class);
                break;
        }
        return activities;
    }

    private List<Activity> returnFilter() {
        List<Activity> activities;
        if ("".equals(lastActivity.getFinalTime())) {
            activities = db.listActivities((long) CHECK);
        } else {
            activities = db.listActivities((long) UPLOAD, (long) CHECK, (long) DIESEL);
        }
        return activities;
    }

    private List<Activity> routeFilter() {
        List<Activity> activities;
        Long id = lastActivity.getDestination().getId();
        if (id < 1000L) {
            activities = db.listActivities((long) DOWNLOAD/*, (long) OTHERS*/);
        } else /*if (id == 2002L)*/ {
            activities = db.listActivities((long) DIESEL);
        }
        return activities;
    }

    private void setLastActivity() {
        Intent i = getIntent();
        roadmapId = i.getLongExtra(RoadmapDetailActivity.EXTRA_ROADMAP_ID, -1L);
        long lastActivityId = i.getLongExtra(RoadmapDetailActivity.EXTRA_LAST_ACTIVITY_ID, -1L);
        if (lastActivityId == -1) {
            Store s = db.getById(regionId, Store.class);
            Activity a = db.getByDescription(RETURN_STR, Activity.class);
            lastActivity = new RoadmapDetail();
            lastActivity.setDestination(s);
            lastActivity.setDestinationId(regionId);
            lastActivity.setActiviyId(a.getId());
//            lastActivity.setFinalKm();
            lastActivity.setFinalTime("");
            //set final km from db
        } else {
            lastActivity = db.getById(lastActivityId, RoadmapDetail.class);
        }
    }

    private void showOthers() {
        spnScaffoldAmount.setVisibility(View.GONE);
        spnScaffoldDownloadedAmount.setVisibility(View.GONE);
        txtLabel.setVisibility(View.GONE);
        setInfo(false);
        setCosts(View.VISIBLE);
    }

    private void showCheck() {
        String diesel = getOriginTxt();
        txtDestination.setText(diesel);
        txtFinalKm.setText(getFinalKm());
        spnScaffoldAmount.setVisibility(View.GONE);
        spnScaffoldDownloadedAmount.setVisibility(View.GONE);
        txtLabel.setVisibility(View.GONE);
        setInfo(true);
        setCosts(View.GONE);
        txtOrigin.setEnabled(false);
        txtDestination.setEnabled(false);
        txtInitialKm.requestFocus();
        KeyboardUtil.show(this, txtInitialKm);
        txtInitialKm.setNextFocusForwardId(R.id.txtInitialTime);
        txtInitialKm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtFinalKm.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        txtInitialKm.setNextFocusForwardId(R.id.txtFinalKm);
//        txtFinalKm.setNextFocusForwardId(R.id.txtInitialTime);
        txtFinalKm.setEnabled(false);
        txtInitialTime.setNextFocusForwardId(R.id.txtFinalTime);
        txtFinalTime.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    private void showRoute() {
        spnScaffoldAmount.setVisibility(View.GONE);
        spnScaffoldDownloadedAmount.setVisibility(View.GONE);
        txtLabel.setVisibility(View.GONE);
        setInfo(false);
        setCosts(View.VISIBLE);
        txtDiesel.setVisibility(View.GONE);
        txtOrigin.setEnabled(false);
        txtDestination.requestFocus();
        txtDestination.setEnabled(true);
        KeyboardUtil.show(this, txtDestination);
        txtDestination.setNextFocusForwardId(R.id.txtFinalKm);
        txtFinalKm.setNextFocusForwardId(R.id.txtFinalTime);
        txtFinalTime.setNextFocusForwardId(R.id.txtCash);

        scaffoldRoadmap = lastActivity.getRoadmap().getScaffolds();
        AutoCompleteAdapter<Store> destinationAdapter;
        if (scaffoldRoadmap == 0) {
            List<Store> dieselStore = db.listDiesel(mContext.getRegion());
            destinationAdapter = new AutoCompleteAdapter<>(this, R.layout.row_item_autocomplete, dieselStore);
            txtDestination.setAdapter(destinationAdapter);
        }
        txtDestination.setThreshold(1);
        validateKm();
    }

    private void showReturn() {
        String regionName = getRegionTxt();
        txtDestination.setText(regionName);
        spnScaffoldAmount.setVisibility(View.GONE);
        spnScaffoldDownloadedAmount.setVisibility(View.GONE);
        txtLabel.setVisibility(View.GONE);
        setInfo(false);
        setCosts(View.VISIBLE);
        txtDiesel.setVisibility(View.GONE);
        txtOrigin.setEnabled(false);
        txtDestination.setEnabled(false);
        txtDestination.setSelection(txtDestination.length());
        KeyboardUtil.show(this, txtDestination);
        validateKm();
    }

    private void validateKm() {
        txtFinalKm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String kmStr = charSequence.toString();
                if (!kmStr.isEmpty() && txtInitialKm.getText().length() == charSequence.length()) {
                    int km = Integer.parseInt(kmStr);
                    if ((km - initialKm) > KM_LIMIT) {
                        DialogUtil.showAlertDialog(RouteCaptureActivity.this, R.string.route_warning_km);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void showDiesel() {
        String diesel = getOriginTxt();
        txtDestination.setText(diesel);
        txtFinalKm.setText(getFinalKm());
        spnScaffoldAmount.setVisibility(View.GONE);
        spnScaffoldDownloadedAmount.setVisibility(View.GONE);
        txtLabel.setVisibility(View.GONE);
        setInfo(false);
        setCosts(View.GONE);
        txtOrigin.setEnabled(false);
        txtDestination.setEnabled(false);
        txtFinalKm.setEnabled(false);
        txtDiesel.setVisibility(View.VISIBLE);
        txtFinalTime.requestFocus();
        txtDiesel.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    private void showDownload() {
        spnScaffoldAmount.setVisibility(View.GONE);
        txtDestination.setText(getOriginTxt());
        txtFinalKm.setText(getFinalKm());
        spnScaffoldDownloadedAmount.setVisibility(View.VISIBLE);
        txtLabel.setVisibility(View.VISIBLE);
        setInfo(false);
        setCosts(View.GONE);
        txtOrigin.setEnabled(false);
        txtDestination.setEnabled(false);
        txtFinalKm.setEnabled(false);
        txtFinalTime.requestFocus();
        txtFinalTime.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        KeyboardUtil.show(this, txtFinalTime);

        int scaffolds = lastActivity.getRoadmap().getScaffolds() + 1;
        List<String> items = new ArrayList<>();
        for (int i = 1; i < scaffolds; i++) {
            items.add(String.valueOf(i));
        }
        ArrayAdapter<String> acaffoldSizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        acaffoldSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnScaffoldDownloadedAmount.setCustomAdapter(acaffoldSizeAdapter);
    }

    private void showUpload() {
        spnScaffoldAmount.setVisibility(View.VISIBLE);
        String regionName = getRegionTxt();
        txtFinalKm.setText(getFinalKm());
        spnScaffoldDownloadedAmount.setVisibility(View.GONE);
        txtLabel.setVisibility(View.GONE);
        txtOrigin.setEnabled(false);
        txtDestination.setText(regionName);
        txtDestination.setEnabled(false);
        txtDestination.setSelection(txtDestination.getText().length());
        txtInitialKm.setEnabled(false);
        txtFinalKm.setText(txtInitialKm.getText());
        txtFinalKm.setEnabled(false);
        setInfo(false);
        setCosts(View.GONE);
        txtFinalTime.setImeOptions(EditorInfo.IME_ACTION_DONE);
        List<String> items = new ArrayList<>();
        for (int i = 1; i < Constants.SCAFFOLDS_IN_TRUCK; i++) {
            items.add(String.valueOf(i));
        }
        ArrayAdapter<String> acaffoldSizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        acaffoldSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnScaffoldAmount.setCustomAdapter(acaffoldSizeAdapter);
        spnScaffoldAmount.setSelection(items.size() - 1);
    }

    private String getFinalKm() {
        return String.valueOf(lastActivity.getFinalKm());
    }

    private void setCosts(int visibility) {
        txtCash.setVisibility(visibility);
        txtIave.setVisibility(visibility);
        txtDiesel.setVisibility(visibility);
        txtOtherCost.setVisibility(visibility);
    }

    private void setInfo(boolean enabled) {
        txtOrigin.setText(getOriginTxt());
        String finalKmTemp = getFinalKm();
        txtInitialKm.setText(finalKmTemp);
        txtInitialKm.setSelection(txtInitialKm.getText().length());
        txtInitialKm.setEnabled(enabled);
        txtFinalKm.setText(finalKmTemp);
        txtFinalKm.setSelection(txtFinalKm.getText().length());
        initialTime = lastActivity.getFinalTime();
        txtInitialTime.setText(initialTime);
        txtInitialTime.setEnabled(enabled);
        txtInitialTime.setKeyListener(null);
        txtFinalTime.setKeyListener(null);
    }

    private String getOriginTxt() {
        return String.valueOf(lastActivity.getDestination().getId());
    }

    private String getRegionTxt() {
        return String.valueOf(db.getById(regionId, Store.class).getId());
    }

    private final Calendar c = Calendar.getInstance();

    private void showClock() {
        KeyboardUtil.hide(this);
        DialogUtil.showClock(RouteCaptureActivity.this, c, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                initialTime = DateUtil.getTimeStr(hour, minute);
                txtInitialTime.setText(initialTime);
                txtFinalTime.requestFocus();
            }
        }, R.string.dialog_tittle_initial_time);
    }

    private void showClock2() {
        KeyboardUtil.hide(this);
        DialogUtil.showClock(RouteCaptureActivity.this, c, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                try {
                    finalTime = DateUtil.getTimeStr(hour, minute);
                    if (!DateUtil.after(initialTime, "22:00") && DateUtil.after(initialTime, finalTime)) {
                        Toast.makeText(mContext, "Hora inválida", Toast.LENGTH_SHORT).show();
                    } else {
                        txtFinalTime.setText(finalTime);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, R.string.dialog_tittle_final_time);
    }

    @Override
    public void onBackPressed() {
        showExitWithoutSaveDialog(RouteCaptureActivity.this);
    }
}
