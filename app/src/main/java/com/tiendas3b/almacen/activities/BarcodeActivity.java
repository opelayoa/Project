package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.ScannerActivity;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.services.SyncViewArticlesService;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.Preferences;

import java.text.DecimalFormat;
import java.util.Locale;

public class BarcodeActivity extends ScannerActivity {

    private static final String TAG = "BarcodeActivity";
    private IDatabaseManager databaseManager;
    private EditText txtId;
    private EditText txtBarcode;
    private EditText txtDescription;
    private EditText txtUnity;
//    private EditText txtCost;
    private EditText txtSale;
    private TextView lblLastUpgrade;
    private ProgressBar progressBar;
    private MenuItem btnSync;
    private EditText txtUnit;
    private EditText txtCircuit;
//    private StringBuffer barcodeBuffer;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_prefernces, menu);
        btnSync = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            syncCatalogs();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void syncCatalogs() {
        Intent msgIntent = new Intent(this, SyncViewArticlesService.class);
        msgIntent.putExtra("messenger", new Messenger(new Handler() {
            @Override
            public void handleMessage(Message msg) {
//                Bundle reply = msg.getData();
                progressBar.setVisibility(View.INVISIBLE);
                btnSync.setVisible(true);
                lblLastUpgrade.setText(getLastUpdate());
            }
        }));
        btnSync.setVisible(false);
        progressBar.setVisibility(View.VISIBLE);
        startService(msgIntent);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_barcode);
//        mContext = (GlobalState) getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
//        //Make progress bar disappear
//        progressBar.setVisibility(View.INVISIBLE);

//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        if (imm != null){
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
//            Log.d(TAG, "remove soft key");
//        }
//
//            Log.d(TAG, "HD:" + isHardwareKeyboardAvailable());
//        barcodeBuffer = new StringBuffer();


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

    @SuppressWarnings("ConstantConditions")
    private void init() {
        Button btnScanner = (Button) findViewById(R.id.btnScanner);
        lblLastUpgrade = (TextView) findViewById(R.id.lblLastUpgrade);
        lblLastUpgrade.setText(getLastUpdate());
        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.setPackage("com.google.zxing.client.android");
//                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");//SCAN_MODE
//                intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A,QR_CODE");
                    startActivityForResult(intent, 0);
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.zxing.client.android")));
                }
            }
        });

        txtId = (EditText) findViewById(R.id.txtId);
        txtBarcode = (EditText) findViewById(R.id.txtBarcode);

//        final boolean[] last = {false};
//        txtBarcode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        Log.d(TAG, "beforeTextChanged");
//                last[0] = false;
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                if(last[0]) {
////                    processBarcode(s.toString());
////                }
//                last[0] = true;
//        Log.d(TAG, "afterTextChanged");
//            }
//        });

        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtUnity = (EditText) findViewById(R.id.txtUnity);
//        txtCost = (EditText) findViewById(R.id.txtCost);
        txtSale = (EditText) findViewById(R.id.txtSale);
        txtUnit = (EditText) findViewById(R.id.txtUnit);
        txtCircuit = (EditText) findViewById(R.id.txtCircuit);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d(TAG, "onKeyDown kc:" + keyCode);
////        if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
////            processBarcode(barcodeBuffer.toString());
////        }
//        return super.onKeyDown(keyCode, event);
//    }

//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
////            Log.d(TAG, "onKeyUp kc:" + keyCode);
//        int unicode = event.getUnicodeChar();
//        if(unicode != 0){
////            Log.d(TAG, "onKeyUp:" + c);
//            if (keyCode != KeyEvent.KEYCODE_DPAD_DOWN && keyCode != KeyEvent.KEYCODE_ENTER) {
//            String c = String.valueOf(Character.toChars(unicode)[0]);
//                barcodeBuffer.append(c);
//            }
//            if(keyCode == KeyEvent.KEYCODE_ENTER){
//                clearTxt();
//                processBarcode(barcodeBuffer.toString());
//            }
//        }
//        return super.onKeyUp(keyCode, event);
//    }

    protected void clearTxt() {
        txtBarcode.getText().clear();
//        txtBarcode.setText("");
//        txtId.setText("");
//        txtDescription.setText("");
//        txtUnity.setText("");
////        txtCost.setText("");
//        txtSale.setText("");
//        txtUnit.setText("");
//        txtCircuit.setText("");
    }

    private boolean isHardwareKeyboardAvailable() { return getResources().getConfiguration().keyboard != Configuration.KEYBOARD_NOKEYS; }

    private String getLastUpdate() {
        return getString(R.string.last_update, new Preferences(this).getSharedStringSafe(Preferences.KEY_LAST_UPDATE, "N/A"));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
//                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
//                Toast.makeText(this, contents + "-" + format, Toast.LENGTH_LONG).show();
                processBarcode(contents);
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    protected void processBarcode(String barcode) {
//        barcodeBuffer = new StringBuffer("");
        Log.d(TAG, "bc:" + barcode + " len:" + barcode.length());
        barcode = barcode.substring(0, barcode.length());
//        if(barcode.length() < 5){
//        Log.d(TAG, "< 5");
//            return;
//        }
        databaseManager = new DatabaseManager(this);
        VArticle vArticle = databaseManager.findViewArticleByBarcode(barcode);
        if(vArticle == null){
        Log.d(TAG, "vArticle null");
            Toast.makeText(this, barcode + " no se encuentra", Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "yep!");
            txtId.setText(String.format(Locale.getDefault(), Constants.FORMAT_INT, vArticle.getIclave()));
            txtBarcode.setText(vArticle.getBarcode());
            txtDescription.setText(vArticle.getDescription());
            txtUnity.setText(vArticle.getUnity());
            DecimalFormat df = new DecimalFormat(Constants.FORMAT_MONEY);
//            Float cost = vArticle.getCost();
//            txtCost.setText(cost == null ? "N/A" : df.format(cost));
            Float sale = vArticle.getSale();
            txtSale.setText(sale == null ? "N/A" : df.format(sale));
            txtUnit.setText(vArticle.getUnit().getDescription());
            String circuit = vArticle.getCircuit();
            txtCircuit.setText(circuit == null ? "N/A" : circuit);

//            txtBarcode.requestFocus();
        }
    }

}
