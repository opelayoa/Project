package com.tiendas3b.almacen.receipt.epackage;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.RecyclerViewCalendarActivity;
import com.tiendas3b.almacen.activities.RecyclerViewCalendarActivityPR;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.EPackage;
import com.tiendas3b.almacen.db.dao.ExpressReceipt;
import com.tiendas3b.almacen.db.dao.ProviderEpackage;
import com.tiendas3b.almacen.db.dao.ProviderEpackageDao;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.BarcodeUtil;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.DialogUtil;
import com.tiendas3b.almacen.util.KeyboardUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpackageProviderActivity extends RecyclerViewCalendarActivityPR  {

    private static final String TAG = "EpackProviderActivity";
    private ProviderRecyclerViewAdapter adapter;
    private boolean useCam;
    private long providerSelected;
    private IDatabaseManager db;
    //private ExpressReceipt expressDetailScan;
    private EPackage expressDetailScan;
    private ProviderEpackage providerScan;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epackage_provider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initDate(savedInstanceState);
        adapter = new ProviderRecyclerViewAdapter(EpackageProviderActivity.this, databaseManager, DateUtil.getDateStr(selectedDate), new BaseRecyclerViewAdapter.OnViewHolderClick() {
            @Override
            public void onClick(View view, int position) {
                providerSelected = position;
                //jamas entra aqui, a menos q se cambie de lugar la ejecucion del codigo de barras
                if(useCam){

                    System.out.println("****providerSelected: "+providerSelected);
                    System.out.println("****expressDetailScan: "+expressDetailScan);
                }else{
                startActivity(new Intent(mContext, EpackageActivity.class).putExtra(EpackageActivity.EXTRA_PROVIDER_ID, (Serializable) items.get(position))
                        .putExtra(EpackageActivity.EXTRA_DATE, DateUtil.getDateStr(selectedDate)));
                }
            }
        });
     //   downloadEPackageProviders();
        init();
        downloadEPackageProviders();
    //    createItems();
        recyclerView.setAdapter(adapter);
//        initRecycler();
    }


    //Lo que regresa el scan con la camara
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

    protected void processBarcode(String barcodeRead) {
        Log.i(TAG, "bc:" + barcodeRead);
//        if (barcodeRead.length() >= BARCODE_LENGTH) {

        String barcode = barcodeRead;//tal vez global

        expressDetailScan = db.findEpackageByDate(barcode, DateUtil.getDateStr(selectedDate));

        if (expressDetailScan == null) {
            Toast.makeText(mContext, "Código de barras no esperado", Toast.LENGTH_LONG).show();
        } else {
            providerScan = db.findProviderEpackageBy(expressDetailScan.getProviderId());
            //fillFields();
            //mandar actividad con el listado de escaneos realizados
            System.out.println("expressDetailScan.getProviderId(): "+expressDetailScan.getProviderId());
            System.out.println("DateUtil.getDateStr(selectedDate): " + DateUtil.getDateStr(selectedDate));
            providerSelected = expressDetailScan.getProviderId();
            System.out.println("****expressDetailScan: "+expressDetailScan);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(getString(R.string.dialog_epackageAutomatico));
            alertDialogBuilder.setPositiveButton(getString(android.R.string.ok),
//                  (dialog, which) -> finish());
                    (dialog, which) -> {
                        //insertCancel();
                        System.out.println("****expressDetailScan*****2: "+expressDetailScan);

                        Intent intent = new Intent(EpackageProviderActivity.this, EpackageActivity.class);
                        intent.putExtra("tipoEpackage", "Automatico");
                        intent.putExtra("providerScan", providerScan);
                        intent.putExtra(EpackageActivity.EXTRA_PROVIDER_ID, expressDetailScan)
                                .putExtra(EpackageActivity.EXTRA_DATE, DateUtil.getDateStr(selectedDate));
                        finish();
                        startActivity(intent);
                    }
            );
            alertDialogBuilder.setNegativeButton(android.R.string.cancel, null);

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

//        } else {
//            Toast.makeText(mContext, "Código de barras no válido", Toast.LENGTH_LONG).show();
//        }-
        KeyboardUtil.hide(this);
    }


    protected void showCalendar() {
        DialogUtil.showCalendar(EpackageProviderActivity.this, calendar, this);
    }

//    protected void initRecycler() {
//    }

    @Override
    protected void createItems() {
        String dateStr = DateUtil.getDateStr(selectedDate);
        setTitle(dateStr);
        adapter.setDb(databaseManager);
        adapter.setDate(dateStr);
        //System.out.println();
//        Object obj = getLastCustomNonConfigurationInstance();
//        if (obj == null) {
//            items = null;
//        } else {
//            Log.i(TAG, "ConfigurationInstance");
//            items = (List<GeneralTravelDTO>) obj;
//        }
        items = databaseManager.findProvidersBy(DateUtil.getDateStr(selectedDate), mContext.getRegion());
    }


    //Opciones existentes en el header
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //si esta seleccionada la camara
        if(id == R.id.action_cam_scan) {
            useCam = !item.isChecked();
            item.setChecked(useCam);
            BarcodeUtil.intentBarcodeReader(com.tiendas3b.almacen.receipt.epackage.EpackageProviderActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setAdapter() {
        adapter.setList((List<ProviderEpackage>) items);
        adapter.notifyDataSetChanged();
        showData();
    }

    @Override
    protected void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        long regionId = mContext.getRegion();
        String dateStr = DateUtil.getDateStr(selectedDate);
        //Call<List<ProviderEpackage>> p = httpService.getProvidersEpackageCat();

        Call<List<EPackage>> s = httpService.getProvidersEpackage(regionId, dateStr);
        //Call<List<EPackage>> s = httpService.getEpackages(regionId);
        s.enqueue(new Callback<List<EPackage>>() {
            @Override
            public void onResponse(Call<List<EPackage>> call, Response<List<EPackage>> response) {
                if (response.isSuccessful()) {
                    fill(response.body());
                    List<EPackage> list = response.body();
                    if (list != null && !list.isEmpty()) {
                        for (int i = 0; i < list.size(); i++) {
                            System.out.println("******** " + list.get(i).toString());
                        }

                        databaseManager.insertOrReplaceInTx(list.toArray(new EPackage[list.size()]));
                        //databaseManager.insertOrReplaceInTx(list.toArray(new ProviderEpackage[list.size()]));
                    }
                    //if(list != null && !list.isEmpty()) {
                    //     databaseManager.insertOrReplaceInTx(list.toArray(new EPackage[list.size()]));
                    //databaseManager.insertOrReplaceInTx(list.toArray(new ProviderEpackage[list.size()]));
                    //  }
                } else {
                    showEmptyView();

                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<List<EPackage>> call, Throwable t) {
                showEmptyView();
                hideProgress();
                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });
    }


    protected void downloadEPackageProviders() {
        //mContext = (GlobalState) getApplicationContext();
        List<ProviderEpackage> listaProviderEpackage = databaseManager.findProvidersBy(DateUtil.getDateStr(selectedDate), mContext.getRegion());
        fill(listaProviderEpackage);
    }

    /*
    protected void downloadEPackageProviders() {
            mContext = (GlobalState) getApplicationContext();
            Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
            Call<List<ProviderEpackage>> p = httpService.getProvidersEpackageCat();

            //Call<List<EPackage>> s = httpService.getEpackages(regionId);
            p.enqueue(new Callback<List<ProviderEpackage>>() {
                @Override
                public void onResponse(Call<List<ProviderEpackage>> call, Response<List<ProviderEpackage>> response) {
                    if (response.isSuccessful()) {
                        fill(response.body());
                        List<ProviderEpackage> list = response.body();


                        if(list != null && !list.isEmpty()) {
                            for(int i = 0; i < list.size(); i++){
                                System.out.println("******** " + list.get(i).toString());
                            }

                            databaseManager.insertOrReplaceInTx(list.toArray(new ProviderEpackage[list.size()]));
                            //databaseManager.insertOrReplaceInTx(list.toArray(new ProviderEpackage[list.size()]));
                        }
                        //if(list != null && !list.isEmpty()) {
                        //     databaseManager.insertOrReplaceInTx(list.toArray(new EPackage[list.size()]));
                        //databaseManager.insertOrReplaceInTx(list.toArray(new ProviderEpackage[list.size()]));
                        //  }
                    } else {
                        showEmptyView();

                    }
                    hideProgress();
                }

                @Override
                public void onFailure(Call<List<ProviderEpackage>> call, Throwable t) {
                    showEmptyView();
                    hideProgress();
                    Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
                }
            });
    }
    */
}
