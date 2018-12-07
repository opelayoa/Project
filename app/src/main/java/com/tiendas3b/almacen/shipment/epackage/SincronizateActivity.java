package com.tiendas3b.almacen.shipment.epackage;

import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.EPackage;
import com.tiendas3b.almacen.db.dao.EpackageDownload;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.epackage.EPackageLeaveDTO;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SincronizateActivity extends AppCompatActivity {
    protected static final String TAG = "SincronizateActivity";
    protected GlobalState mContext;
    protected IDatabaseManager db;
    protected int status;
    protected int targetStatus;
    Button btnSicronizar;
    TextView txtSincronizar;
    List<EpackageDownload> listEPackagesDownload;

    @BindView(R.id.switcher)
    ViewSwitcher viewSwitcher;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (GlobalState) getApplicationContext();
        db = new DatabaseManager(mContext);
        status = EpackageActivity.SHIPMENT_RECEIVED;
        targetStatus = EpackageActivity.SHIPMENT_TO_STORE;
        setContentView(R.layout.activity_epackage_sincronizate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        txtSincronizar = findViewById(R.id.txtSincronizar);

        btnSicronizar = (Button) findViewById(R.id.btnSincronizar);
        listEPackagesDownload = db.listEPackagesDownload(1);
        int totalPaquetes = 0;

        if(listEPackagesDownload != null){
            totalPaquetes = listEPackagesDownload.size();
        }

        if(totalPaquetes > 0) {
            if(totalPaquetes == 1) {
                txtSincronizar.setText("Falta " + totalPaquetes + " paquete por sincronizar");
            }else{
                txtSincronizar.setText("Faltan " + totalPaquetes + " paquetes por sincronizar");
            }
            btnSicronizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("boton ");
                    send();
                }
            });
        }else {
            sincronizateOk();
        }
    }

    private void send() {
        System.out.println("listEPackagesDownload: " + listEPackagesDownload.size());
        List<EPackageLeaveDTO> listDownloadPackage = new ArrayList<>();
        for(EpackageDownload beanEpackageDownload : listEPackagesDownload){
            EPackageLeaveDTO ePackageLeaveDTO = new EPackageLeaveDTO(true, beanEpackageDownload.getPackageId(), beanEpackageDownload.getCamClave(), beanEpackageDownload.getAlmacen(), beanEpackageDownload.getStatusLeave(), beanEpackageDownload.getTarjetStatus(), beanEpackageDownload.getFecha());
            listDownloadPackage.add(ePackageLeaveDTO);
        }

        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.actionEpackage_Sincronizate(listDownloadPackage).enqueue(new Callback<GeneralResponseDTO>() {
            @Override
            public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                if (response.isSuccessful()) {
                    GeneralResponseDTO r = response.body();

                    if (r == null) {
                        Log.e(TAG, "cómo así?!");
                    } else {
                        if (r.getCode() == 1L) {
                            processSuccess();
                        } else {
                            //if(btn != null) {
                            //    btn.showProgress(false);
                            //}
                            Snackbar.make(viewSwitcher, r.getDescription(), BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                    }

//                    if(r != null && r.getCode() == 1L){
//                        processSuccess(ePackage);
//                    } else {
//                        Snackbar.make(viewSwitcher, r.getDescription(), BaseTransientBottomBar.LENGTH_LONG).show();
//                        Log.e(TAG, "error1");
//                    }
                } else {
                    //if(btn != null) {
                    //  btn.showProgress(false);
                    // }
                    Snackbar.make(viewSwitcher, R.string.response_error, BaseTransientBottomBar.LENGTH_LONG).show();
                    Log.e(TAG, "error1");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
                //if(btn != null) {
                //    btn.showProgress(false);
                //}
                Snackbar.make(viewSwitcher, R.string.process_error, BaseTransientBottomBar.LENGTH_LONG).show();
                Log.e(TAG, "error2");
            }
        });
    }

    public void processSuccess(){
        System.out.println("listEPackagesDownload: " + listEPackagesDownload.size());
        for(EpackageDownload beanEpackageDownload : listEPackagesDownload){
            beanEpackageDownload.setActivo(0);
            db.update(beanEpackageDownload);
        }
        sincronizateOk();
    }

    public void sincronizateOk(){
        txtSincronizar.setText("No hay paquetes pendientes por sincronizar");
        btnSicronizar.setVisibility(View.GONE);
    }
}
