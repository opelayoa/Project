package com.tiendas3b.almacen.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.farbod.labelledspinner.LabelledSpinner;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.SpinnerAdapter;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.BuyDetail;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.DialogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReceipSheetDetailCaptureActivity extends AppCompatActivity {

    private EditText txtSampling;
    private EditText txtExpiration;
    private LabelledSpinner spnMeCause;
    private Date expirationDate;
    private IDatabaseManager databaseManager;
    private BuyDetail buyDetail;
    private EditText txtAceptedAmount;
    private List<ObservationType> obs;
    private EditText txtMeAmount;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receipt_sheet_capture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_capture_save:
//                saveData();
                Toast.makeText(this, "Guardado", Toast.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receip_sheet_detail_capture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseManager = new DatabaseManager(this);

        init();
    }

    @Override
    protected void onRestart() {
        databaseManager = new DatabaseManager(this);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onStop();
    }

    @Override
    protected void onResume() {
        databaseManager = DatabaseManager.getInstance(this);
        super.onResume();
    }

    private void init() {

        long buyDetailId = getIntent().getLongExtra(Constants.EXTRA_BUY_DETAIL_ID, -1L);
        buyDetail = databaseManager.getById(buyDetailId, BuyDetail.class);

        TextView lblBuyDetailName = (TextView) findViewById(R.id.lblBuyDetailName);
        TextView lblDate = (TextView) findViewById(R.id.lblDate);
        TextView lblProvider = (TextView) findViewById(R.id.lblProvider);
        final TextView lblFillRate = (TextView) findViewById(R.id.lblFillRate);
        txtAceptedAmount = (EditText) findViewById(R.id.txtAceptedAmount);
        txtMeAmount = (EditText) findViewById(R.id.txtMeAmount);
        spnMeCause = (LabelledSpinner) findViewById(R.id.spnMeCause);
        txtSampling = (EditText) findViewById(R.id.txtSampling);
        txtExpiration = (EditText) findViewById(R.id.txtExpiration);


        lblBuyDetailName.setText(buyDetail.getId() + "-" + buyDetail.getArticle().getDescription().concat(" ").concat(buyDetail.getArticle().getUnity()));
        Buy buy = buyDetail.getBuy();
        lblDate.setText(new SimpleDateFormat(DateUtil.YYYY_MM_DD, Locale.getDefault()).format(buy.getProgramedDate()));
        lblProvider.setText(buy.getProvider().getBusinessName());
        lblFillRate.setText("Fill Rate: " + fillRate() + "%");

        txtExpiration.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showCalendar();
                }
            }
        });

        txtSampling.setEnabled(isSampling(buyDetail.getArticle().getTypeId()));

        txtMeAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amountStr = s.toString();
                if (amountStr.isEmpty()) {
                    amountStr = "0";
                }
                spnMeCause.getSpinner().setEnabled(Integer.parseInt(amountStr) > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        obs = databaseManager.listObservationType(0);
        SpinnerAdapter adapterObs = new SpinnerAdapter(this, R.layout.spinner_item_table, obs);
        spnMeCause.setCustomAdapter(adapterObs);
        spnMeCause.getSpinner().setEnabled(false);
//        ArrayList<String> categories = createCategories();
////        categoryAdapter = new CategoryAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories);
//        spnMeCause.setCustomAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories));
//        spnMeCause.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
//            @Override
//            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
////                categoryId = id;
//            }
//
//            @Override
//            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
//
//            }
//        });

        txtAceptedAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lblFillRate.setText("Fill Rate: " + fillRate(s.toString()) + "%");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private boolean isSampling(Integer type) {
        //quemado porque tengo hueva de traer el catalogo
        // ya que estos comabios no tienen coherencia XD rio para no llorar :'(
        return type == 8;
    }

    private String fillRate(String aceptedAmount) {
        if (aceptedAmount.isEmpty()) {
            return "0";
        }

        return String.format(Locale.getDefault(), "%.2f", Integer.valueOf(aceptedAmount) * 100 / buyDetail.getArticleAmount().floatValue());
    }

    private String fillRate() {
        String aceptedAmount = txtAceptedAmount.getText().toString();
        return fillRate(aceptedAmount);
    }

    private ArrayList<String> createCategories() {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Aceptado");
        categories.add("No Aceptado");
        return categories;
    }

    private final Calendar c = Calendar.getInstance();

    private void showCalendar() {
        DialogUtil.showCalendar(ReceipSheetDetailCaptureActivity.this, c, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
//                          Date d = new Date();//TODO definir que hora
                expirationDate = c.getTime();
                txtExpiration.setText(DateUtil.getDateStr(expirationDate));
            }
        });
    }

//    private void showCalendar() {
//        final SimpleDateFormat dfThousnds = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());
//        final Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                c.set(year, monthOfYear, dayOfMonth);
////                Date d = new Date();//TODO definir que hora
//                expirationDate = c.getTime();
//                txtExpiration.setText(dfThousnds.format(expirationDate));
//            }
//        }, year, month, day);
////        dateDialog.getDatePicker().setMaxDate(new Date().getTime());
//        dateDialog.show();
//
//    }
}
