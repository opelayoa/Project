package com.tiendas3b.almacen.picking.epackage;

import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Area;
import com.tiendas3b.almacen.db.dao.EPackage;
import com.tiendas3b.almacen.db.dao.Position;
import com.tiendas3b.almacen.db.dao.PositionPackage;
import com.tiendas3b.almacen.db.dao.Rack;
import com.tiendas3b.almacen.db.dao.RackLevel;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;

import mbanje.kurt.fabbutton.FabButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickingEpackageActivity extends EpackageActivity {

    public static final String EXTRA_STORE = "EXTRA_STORE";
    private Store store;
    private EPackage epackageEncontrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        status = EpackageActivity.PICKING_CAGE_IN;
        targetStatus = EpackageActivity.PICKING_CAGE_OUT;
        store = (Store) getIntent().getSerializableExtra(EXTRA_STORE);
        super.onCreate(savedInstanceState);
        downloadPackagePosition();
    }

    @Override
    protected void action(final EPackage ePackage, final FabButton btn) {

        PositionPackage pp = db.getBy(ePackage.getId());
        Position position = pp.getPosition();

        RackLevel level = position.getLevel();
        Rack rack = level.getRack();
        Area area = rack.getArea();

        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.actionEpackageCageOut(mContext.getRegion(), ePackage.getId(), status, targetStatus,
                area.getAreaId(), rack.getRackId(), level.getLevelId(), position.getPositionId()).enqueue(new Callback<GeneralResponseDTO>() {
            @Override
            public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                if (response.isSuccessful()) {
                    GeneralResponseDTO r = response.body();
                    if(r != null && r.getCode() > 0L){
                        processSuccess(ePackage);
                    }
                } else {
                    btn.showProgress(false);
                    Snackbar.make(viewSwitcher, R.string.response_error, BaseTransientBottomBar.LENGTH_LONG).show();
                    Log.e(TAG, "error1");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
                btn.showProgress(false);
                Snackbar.make(viewSwitcher, R.string.process_error, BaseTransientBottomBar.LENGTH_LONG).show();
                Log.e(TAG, "error2");
            }
        });
    }

//    protected void processSuccess(EPackage ePackage) {
//        super.processSuccess(ePackage);
////        PositionPackage pp = new PositionPackage();
////        pp.setPackageId(ePackage.getId());
////        pp.setPositionId(positionIdSelected);
////        pp.setStatus(true);
////        db.insertOrReplaceInTx(pp);
//    }

    @Override
    protected void setItems() {
        items = db.listEPackages(status, store.getId());
//        updateItems();
    }

//    @Override
//    protected void downloadStatus(int status) {
//        downloadStatus(EpackageActivity.PICKING_CAGE_IN);
////        db.truncate(EPackage.class);
////        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
////        httpService.getEpackages(mContext.getRegion(), EpackageActivity.PICKING_CAGE_IN).enqueue(new Callback<List<EPackage>>() {
////            @Override
////            public void onResponse(Call<List<EPackage>> call, Response<List<EPackage>> response) {
////                if (response.isSuccessful()) {
////                    processResponse(response.body());
////                } else {
////                    showEmpty();
////                }
////                viewSwitcher.setDisplayedChild(1);
////            }
////
////            @Override
////            public void onFailure(Call<List<EPackage>> call, Throwable t) {
////                showEmpty(t.getLocalizedMessage());
////                viewSwitcher.setDisplayedChild(1);
////            }
////        });
//    }


    @Override
    protected void processBarcode(String barcode) {
        Log.d(TAG, "processBarcode");
        Log.d(TAG, "items: "+ items.size());
        if(items.size() > 0){
            for(EPackage epackage : items){
                if(epackage.getBarcode().equals(barcode)){
                    epackageEncontrado = epackage;
                }
            }
            if(epackageEncontrado != null){
                action(epackageEncontrado, null);
                epackageEncontrado = null;
                //setItems();
            }else{
                Snackbar.make(viewSwitcher, "No se encontró ese código de barras", BaseTransientBottomBar.LENGTH_LONG).show();
            }
        }
    }

}
