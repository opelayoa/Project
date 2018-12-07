package com.tiendas3b.almacen.picking.epackage;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
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
import com.tiendas3b.almacen.db.dao.Position;
import com.tiendas3b.almacen.db.dao.PositionPackage;
import com.tiendas3b.almacen.db.dao.Rack;
import com.tiendas3b.almacen.db.dao.RackLevel;
import com.tiendas3b.almacen.db.dao.Region;
import com.tiendas3b.almacen.dto.epackage.PositionPackageDTO;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.picking.orders.SectionsPagerAdapter;
import com.tiendas3b.almacen.util.BluetoothUtil;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.printer.TscSdk;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import mbanje.kurt.fabbutton.FabButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CageInActivity extends EpackageActivity {

    private long positionIdSelected;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    private String packageBarCode = null;
    private String packageBarCodeUb = null;
    List<EPackage> listaEpackage = new ArrayList<>();
    EPackage ePackageUpdt = new EPackage();

    private PositionPackageDTO positionPackageDTO = null;
    private long idEpackage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        status = EpackageActivity.RECEIPT_RECEIVED;
        targetStatus = EpackageActivity.PICKING_CAGE_IN;
        super.onCreate(savedInstanceState);
        downloadPackagePosition();
        //downloadStatusCageIn();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void action(final EPackage ePackage, final FabButton btn) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CageInActivity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(R.layout.dialog_location)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> send(ePackage, btn))
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                    if(btn != null) {
                        btn.showProgress(false);
                    }
                    positionIdSelected = -1L;
//                        onBackPressed();
                });
        builder.setTitle(R.string.location);
        final AlertDialog dialog = builder.create();
        dialog.show();

        LabelledSpinner spnArea = dialog.findViewById(R.id.spnArea);
        final LabelledSpinner spnRack = dialog.findViewById(R.id.spnRack);
        final LabelledSpinner spnLevel = dialog.findViewById(R.id.spnLevel);
        final LabelledSpinner spnPosition = dialog.findViewById(R.id.spnPosition);
        final List<Area> areas = db.listAreas(mContext.getRegion());
        GenericSpinnerAdapter<Area> areasAdapter = new GenericSpinnerAdapter<>(mContext, R.layout.spinner_item_table, areas);
        spnArea.setCustomAdapter(areasAdapter);
        spnArea.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {

                final List<Rack> racks = areas.get(position).getRacks();
                GenericSpinnerAdapter<Rack> racksAdapter = new GenericSpinnerAdapter<>(mContext, R.layout.spinner_item_table, racks);
                spnRack.setCustomAdapter(racksAdapter);
                spnRack.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
                    @Override
                    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                        final List<RackLevel> levels = racks.get(position).getLevels();
                        GenericSpinnerAdapter<RackLevel> levelsAdapter = new GenericSpinnerAdapter<>(mContext, R.layout.spinner_item_table, levels);
                        spnLevel.setCustomAdapter(levelsAdapter);
                        spnLevel.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
                            @Override
                            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {

                                List<Position> positions = levels.get(position).getPositions();
                                GenericSpinnerAdapter<Position> levelsAdapter = new GenericSpinnerAdapter<>(mContext, R.layout.spinner_item_table, positions);
                                spnPosition.setCustomAdapter(levelsAdapter);
                                spnPosition.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
                                    @Override
                                    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                                        positionIdSelected = id;
                                    }

                                    @Override
                                    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

                                    }
                                });
                            }

                            @Override
                            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

                            }
                        });
                    }

                    @Override
                    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });

    }

    private void send(final EPackage ePackage, final FabButton btn) {
        Position position = db.getById(positionIdSelected, Position.class);
        RackLevel level = position.getLevel();
        Rack rack = level.getRack();
        Area area = rack.getArea();

        Log.i(TAG, "a:" + area.getAreaId() + " r: " + rack.getRackId() + " l:" + level.getLevelId() + " p: " + position.getPositionId());

        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.actionEpackageCageIn(mContext.getRegion(), ePackage.getId(), status, targetStatus,
                area.getAreaId(), rack.getRackId(), level.getLevelId(), position.getPositionId()).enqueue(new Callback<GeneralResponseDTO>() {
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
            }
        });
    }

    protected void processSuccess(EPackage ePackage) {
        super.processSuccess(ePackage);
        PositionPackage pp = new PositionPackage();
        pp.setPackageId(ePackage.getId());
        pp.setPositionId(positionIdSelected);
        pp.setStatus(true);
        db.insertOrReplaceInTx(pp);
        processCageIn();
    }

    private void processCageIn() {
        //Snackbar.make(viewSwitcher, "Resguardo exitoso", BaseTransientBottomBar.LENGTH_LONG).show();
        //EPackage ePackageUpdate = new EPackage();
        //ePackageUpdate.setId(item.getId());
        //item.setStatus(com.tiendas3b.almacen.picking.epackage.EpackageActivity.PICKING_CAGE_IN);
        //db.update(item);
        itemsCageIn = db.listEPackages(EpackageActivity.PICKING_CAGE_IN);
    }

    @Override
    protected void setItems() {
        items = db.listEPackages(EpackageActivity.RECEIPT_RECEIVED);
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
                //Ya se escaneo un paquete y debe ejecutar el proceso de validar el escaneo de ubicacion y asignar el paquete a la ubicacion
                packageBarCodeUb = "UB" + barcode;
                findBarCodeUb(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        positionPackageDTO = (PositionPackageDTO) response.body();

                        if(positionPackageDTO == null){
                            Snackbar.make(viewSwitcher, "El código de barras de la Ubicación no se encuentra en la lista", BaseTransientBottomBar.LENGTH_LONG).show();
                        }else{
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CageInActivity.this);
                            alertDialogBuilder.setMessage("Desea relacionar el Paquete " + packageBarCode + " con la Ubicación "+packageBarCodeUb);
                            alertDialogBuilder.setPositiveButton(getString(android.R.string.ok),
//                  (dialog, which) -> finish());
                                    (dialog, which) -> {
                                        //insertCancel();
                                        System.out.println("****acepta la relacion**********" + ePackageUpdt);
                                        //EPackage ePackage = new EPackage();
                                        //ePackage.setId(idEpackage);
                                        send(positionPackageDTO, ePackageUpdt, null);
//                                        packageBarCodeUb = null;
//                                        packageBarCode = null;
//                                        startActivity(new Intent(mContext, CageInActivity.class));
                                    }
                            );
                            alertDialogBuilder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                        //insertCancel();
                                        System.out.println("****cancela y limpia campos**********");
                                        packageBarCodeUb = null;
                                        packageBarCode = null;
                                        //finish();
                                    }
                            );

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
                //si el objeto PositionPackageDTO trae datos, setearle el idEpackage y crear el servicio send similar al q ya existe, si el objeto no trae nada, es por q no existe el codigo de barras


            }
        }else{}
        requestFocusScan();
        txtBarcode.setText("");
    }

    public void requestFocusScan() {
        txtBarcode.requestFocus();
    }


    //Busca codigo de barras de ubicacion
    private void findBarCodeUb(Callback callback) {
        Log.d(TAG, "findBarCodeUb - packageBarCodeUb: "+packageBarCodeUb);
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<PositionPackageDTO> call = httpService.findBarCodeUb(packageBarCodeUb);
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
    }


    private void send(PositionPackageDTO positionPackageDTO, EPackage ePackage, final FabButton btn) {
        positionIdSelected = positionPackageDTO.getPositionId();
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
                            packageBarCodeUb = null;
                            packageBarCode = null;
                            startActivity(new Intent(mContext, CageInActivity.class));
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


//    @Override
//    protected void downloadStatus(int status) {
//        downloadStatus(EpackageActivity.RECEIPT_RECEIVED);
//    }

}
