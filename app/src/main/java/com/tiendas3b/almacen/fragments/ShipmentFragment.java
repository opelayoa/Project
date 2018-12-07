package com.tiendas3b.almacen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.ConstructionActivity;
import com.tiendas3b.almacen.activities.ShipmentMapActivity;
import com.tiendas3b.almacen.shipment.activities.ListTripActivity;
import com.tiendas3b.almacen.shipment.activities.ShipmentConfigActivity;
import com.tiendas3b.almacen.shipment.day.sheet.DaySheetActivity;
import com.tiendas3b.almacen.shipment.epackage.EpackageDownloadStoresActivity;
import com.tiendas3b.almacen.shipment.epackage.LeaveActivity;
import com.tiendas3b.almacen.shipment.epackage.EpackageStoresActivity;
import com.tiendas3b.almacen.shipment.epackage.SincronizateActivity;
import com.tiendas3b.almacen.shipment.presenters.ShipmentPresenter;
import com.tiendas3b.almacen.shipment.presenters.ShipmentPresenterImpl;
import com.tiendas3b.almacen.shipment.transference.InputTransferenceActivity;
import com.tiendas3b.almacen.shipment.util.DialogFactory;
import com.tiendas3b.almacen.shipment.views.ShipmentView;

/**
 * Created by dfa on 19/07/2016.
 */
public class ShipmentFragment extends Fragment implements ShipmentView {
//    public static final int REQUEST_CODE = 13;

    private GlobalState mContext;

    private AlertDialog progressDialog;

    private AlertDialog successDialog;
    private AlertDialog errorDialog;

    private ShipmentPresenter shipmentPresenter;

    public ShipmentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = (GlobalState) getActivity().getApplicationContext();
        getActivity().setTitle(R.string.menu_drw_shipment);
        View view = inflater.inflate(R.layout.fragment_shipment, container, false);

        shipmentPresenter = new ShipmentPresenterImpl(mContext, this);
        init(view);
        return view;
    }

    private void init(final View view) {


        view.findViewById(R.id.btnOpt1).setOnClickListener(v -> startActivity(new Intent(mContext, ListTripActivity.class)));
        view.findViewById(R.id.btnOpt2).setOnClickListener(v -> {
            Intent intentTicketDetailEdit = new Intent(mContext, InputTransferenceActivity.class);
            getActivity().startActivity(intentTicketDetailEdit);
        });
//        view.findViewById(R.id.btnOpt2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mContext, ConstructionActivity.class));
//            }
//        });
        view.findViewById(R.id.btnOpt3).setOnClickListener(v -> startActivity(new Intent(mContext, ShipmentMapActivity.class)));
        view.findViewById(R.id.btnOpt4).setOnClickListener(v -> startActivity(new Intent(mContext, ConstructionActivity.class)));
        view.findViewById(R.id.btnOpt5).setOnClickListener(v -> startActivity(new Intent(mContext, ConstructionActivity.class)));
        view.findViewById(R.id.btnOpt6).setOnClickListener(v -> startActivity(new Intent(mContext, DaySheetActivity.class)));
        view.findViewById(R.id.btnShipmentConfig).setOnClickListener(v -> startActivity(new Intent(mContext, ShipmentConfigActivity.class)));
        view.findViewById(R.id.btnEpackage).setOnClickListener(v -> showPopupMenu(v));
        view.findViewById(R.id.cargaInfo).setOnClickListener(v -> {
            this.shipmentPresenter.uploadInfoTrips();
        });
    }


    public void showPopupMenu(View btn) {
        PopupMenu popup = new PopupMenu(mContext, btn);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_epackage_shipment, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.action1:
                    startActivity(new Intent(mContext, EpackageStoresActivity.class));
                    break;
                case R.id.action2:
                    startActivity(new Intent(mContext, LeaveActivity.class));
                    break;
                case R.id.action3:
                    startActivity(new Intent(mContext, EpackageDownloadStoresActivity.class));
                    break;
                case R.id.action4:
                    startActivity(new Intent(mContext, SincronizateActivity.class));
                    break;
            }
            return true;
        });
        popup.show();
    }

    @Override
    public void initProgressDialog(String title) {
        progressDialog = DialogFactory.getProgressDialog(getActivity(), title);
        progressDialog.show();
    }

    @Override
    public void stopProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showError(String error) {
        errorDialog = DialogFactory.getErrorDialog(getActivity(), error);
        errorDialog.show();
    }

    @Override
    public void showInfo(String info) {
        successDialog = DialogFactory.getSuccessDialog(getActivity(), info);
        successDialog.show();
    }
}
