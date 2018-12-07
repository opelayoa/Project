package com.tiendas3b.almacen.shipment.epackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.satsuware.usefulviews.LabelledSpinner;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.GenericSpinnerAdapter;
import com.tiendas3b.almacen.db.dao.Area;
import com.tiendas3b.almacen.db.dao.EPackage;
import com.tiendas3b.almacen.db.dao.EpackageTruck;
import com.tiendas3b.almacen.db.dao.Position;
import com.tiendas3b.almacen.db.dao.PositionPackage;
import com.tiendas3b.almacen.db.dao.Rack;
import com.tiendas3b.almacen.db.dao.RackLevel;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.dao.Truck;
import com.tiendas3b.almacen.db.dao.TruckPackage;
import com.tiendas3b.almacen.dto.epackage.PositionPackageDTO;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.shipment.epackage.EpackageActivity;

import java.util.ArrayList;
import java.util.List;

import mbanje.kurt.fabbutton.FabButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipmentEpackageActivity extends EpackageActivity {

    private long truckIdSelected;
    public static final String EXTRA_STORE = "EXTRA_STORE";
    private Store store;
    private EPackage epackageEncontrado;

    private String packageBarCode = null;
    private String packageBarCodeCam = null;
    List<EPackage> listaEpackage = new ArrayList<>();
    EPackage ePackageUpdt = new EPackage();
    private EpackageTruck epackageTruck = null;
    private long idEpackage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        status = EpackageActivity.PICKING_CAGE_OUT;
        targetStatus = EpackageActivity.AUDIT_STOWAGE;
        store = (Store) getIntent().getSerializableExtra(EXTRA_STORE);
        super.onCreate(savedInstanceState);
        downloadPackageTruck();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void action(final EPackage ePackage, final FabButton btn) {
        EpackageTruck truck = db.getById(truckIdSelected, EpackageTruck.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(ShipmentEpackageActivity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(R.layout.dialog_select_shipment)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> send(ePackage, truck, btn))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                    if(btn != null) {
                        btn.showProgress(false);
                    }
                    truckIdSelected = -1L;
//                        onBackPressed();
                });
        builder.setTitle(R.string.truck);
        final AlertDialog dialog = builder.create();
        dialog.show();

        LabelledSpinner spnCamion = dialog.findViewById(R.id.spnCamion);
        final List<EpackageTruck> trucks = db.listActiveEpackageTruck(mContext.getRegion());
        GenericSpinnerAdapter<EpackageTruck> trucksAdapter = new GenericSpinnerAdapter<>(mContext, R.layout.spinner_item_table, trucks);
        spnCamion.setCustomAdapter(trucksAdapter);
        spnCamion.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long Id) {
                EpackageTruck truck = db.findEpackageTruckbyId(Id);
                truckIdSelected = truck.getTruckId();
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });
    }

    private void send(final EPackage ePackage, EpackageTruck truck, final FabButton btn) {

        if(truckIdSelected == 0){
            truckIdSelected = truck.getTruckId();
        }

        Log.i(TAG,  " truckIdSelected: " + truckIdSelected);
        //Log.i(TAG,  " truck.getTruckId: " + truck.getTruckId());

        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.actionEpackageTruckIn(mContext.getRegion(), ePackage.getId(), status, targetStatus, truckIdSelected).enqueue(new Callback<GeneralResponseDTO>() {
            @Override
            public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                if (response.isSuccessful()) {
                    GeneralResponseDTO r = response.body();

                    if (r == null) {
                        Log.e(TAG, "cómo así?!");
                    } else {
                        if (r.getCode() == 1L) {
                            processSuccess(ePackage);
                        } else {
                            if(btn != null) {
                                btn.showProgress(false);
                            }
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
                    if(btn != null) {
                        btn.showProgress(false);
                    }
                    Snackbar.make(viewSwitcher, R.string.response_error, BaseTransientBottomBar.LENGTH_LONG).show();
                    Log.e(TAG, "error1");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
                if(btn != null) {
                    btn.showProgress(false);
                }
                Snackbar.make(viewSwitcher, R.string.process_error, BaseTransientBottomBar.LENGTH_LONG).show();
                Log.e(TAG, "error2");
                truckIdSelected = 0;
                packageBarCode = null;
            }
        });
    }

    protected void processSuccess(EPackage ePackage) {
        super.processSuccess(ePackage);
        TruckPackage tp = new TruckPackage();
        tp.setPackageId(ePackage.getId());
        /*9
        if(truckIdSelected == 0){
            tp.setTruckId(epackageTruck.getTruckId());
        }else{
            tp.setTruckId(truckIdSelected);
        }
        */
        tp.setTruckId(truckIdSelected);
        tp.setStatus(1);
        db.insertOrReplaceInTx(tp);
       // processTruckIn();
        truckIdSelected = 0;
        packageBarCode = null;
    }

    //private void processTruckIn() {
     //   itemsTruckIn = db.listEPackages(com.tiendas3b.almacen.picking.epackage.EpackageActivity.AUDIT_STOWAGE);
    //}

    @Override
    protected void setItems() {
        items = db.listEPackages(status, store.getId());
        //updateItems();
    }

    @Override
    protected void processBarcode(String barcode) {

        Log.d(TAG, "processBarcode");
        Log.d(TAG, "items: " + items);
        listaEpackage = items;
        if(barcode != null) {
            //mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem()).add(barcode);
            if (packageBarCode == null) {
                //Proceso para asignar el codigo de barras leido a la variable de paquete
                int ban = 0;
                long tmpIdEpackage = 0;
                for (EPackage epackageLista : listaEpackage) {
                    if (epackageLista.getBarcode().equals(barcode)) {
                        ePackageUpdt = epackageLista;
                        tmpIdEpackage = epackageLista.getId();
                        ban++;
                        break;
                    }
                }
                if (ban > 0) {
                    Log.d(TAG, "validar ");
                    packageBarCode = barcode;
                    idEpackage = tmpIdEpackage;
                } else {
                    Snackbar.make(viewSwitcher, "El código de barras no encuentra en la lista", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }else{
                //Ya se escaneo un paquete y debe ejecutar el proceso de validar el escaneo de camion y asignar el paquete al camion
                packageBarCodeCam = "UB" + barcode;
                epackageTruck = db.findEpackageTruckbyBarcode(packageBarCodeCam);

                        if(epackageTruck == null){
                            Snackbar.make(viewSwitcher, "El código de barras del Camion no se encuentra en la lista", BaseTransientBottomBar.LENGTH_LONG).show();
                        }else{
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShipmentEpackageActivity.this);
                            alertDialogBuilder.setMessage("Desea enviar el Paquete " + packageBarCode + " en el Camion "+epackageTruck.getDescription());
                            alertDialogBuilder.setPositiveButton(getString(android.R.string.ok),
//                  (dialog, which) -> finish());
                                    (dialog, which) -> {
                                        //insertCancel();
                                        System.out.println("****acepta la relacion**********" + ePackageUpdt);
                                        //EPackage ePackage = new EPackage();
                                        //ePackage.setId(idEpackage);
                                        send(ePackageUpdt, epackageTruck, null);
//                                        packageBarCodeUb = null;
//                                        packageBarCode = null;
//                                        startActivity(new Intent(mContext, CageInActivity.class));
                                    }
                            );
                            alertDialogBuilder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                        //insertCancel();
                                        System.out.println("****cancela y limpia campos**********");
                                        packageBarCodeCam = null;
                                        packageBarCode = null;
                                        //finish();
                                    }
                            );

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }
                //si el objeto PositionPackageDTO trae datos, setearle el idEpackage y crear el servicio send similar al q ya existe, si el objeto no trae nada, es por q no existe el codigo de barras


            }
        }

    public void requestFocusScan() {
        txtBarcode.requestFocus();
    }

/*
    //Busca codigo de barras de Camion
    private void findBarCodeCam(Callback callback) {
        Log.d(TAG, "findBarCodeUb - packageBarCodeUb: "+packageBarCodeCam);
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<PositionPackageDTO> call = httpService.findBarCodeUb(packageBarCodeCam);
        call.enqueue(new Callback<PositionPackageDTO>() {
            @Override
            public void onResponse(Call<PositionPackageDTO> call, Response<PositionPackageDTO> response) {
                if (response.isSuccessful()) {
                    PositionPackageDTO positionPackageDTO = response.body();
//                    if(positionPackageDTO != null) {
//                        db.insertOrReplaceInTx(positionPackageDTO);
//                    }
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<PositionPackageDTO> call, Throwable t) {

            }
        });
    }*/

/*
    private void send(PositionPackageDTO positionPackageDTO, EPackage ePackage, final FabButton btn) {
        truckIdSelected = positionPackageDTO.getPositionId();
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.actionEpackageCageIn(mContext.getRegion(), ePackage.getId(), status, targetStatus,
                positionPackageDTO.getAreaId(), positionPackageDTO.getRackId(), positionPackageDTO.getLevelId(), positionPackageDTO.getPositionId()).enqueue(new Callback<GeneralResponseDTO>() {
            @Override

            public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                if (response.isSuccessful()) {
                    GeneralResponseDTO r = response.body();

                    if (r == null) {
                        Log.e(TAG, "cómo así?!");
                    } else {
                        if (r.getCode() == 1L) {
                            processSuccess(ePackage);
                            packageBarCodeCam = null;
                            packageBarCode = null;
                            startActivity(new Intent(mContext, ShipmentEpackageActivity.class));
                        } else {
                            if(btn != null) {
                                btn.showProgress(false);
                            }
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
                    if(btn != null) {
                        btn.showProgress(false);
                    }
                    Snackbar.make(viewSwitcher, R.string.response_error, BaseTransientBottomBar.LENGTH_LONG).show();
                    Log.e(TAG, "error1");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
                if(btn != null) {
                    btn.showProgress(false);
                }
                Snackbar.make(viewSwitcher, R.string.process_error, BaseTransientBottomBar.LENGTH_LONG).show();
                Log.e(TAG, "error2");
            }
        });
    }
*/

//    @Override
//    protected void downloadStatus(int status) {
//        downloadStatus(EpackageActivity.RECEIPT_RECEIVED);
//    }
}
