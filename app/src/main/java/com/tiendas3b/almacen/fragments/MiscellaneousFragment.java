package com.tiendas3b.almacen.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.BarcodeActivity;
import com.tiendas3b.almacen.miscellaneous.StoreReceiptEpackageActivity;
import com.tiendas3b.almacen.shipment.vrp.TravelsActivity;
import com.tiendas3b.almacen.shipment.vrp.VrpActivity;

/**
 * Created by dfa on 19/07/2016.
 */
public class MiscellaneousFragment extends Fragment {

    private static final String URL_MAP = "http://201.161.106.125:8080/Logistica/MapaLogistica2.xhtml";
    private GlobalState mContext;

    public MiscellaneousFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (GlobalState) getActivity().getApplicationContext();
        getActivity().setTitle(R.string.menu_drw_miscellaneous);
        View view = inflater.inflate(R.layout.fragment_miscellaneous, container, false);
        init(view);
        return view;
    }

    private void init(final View view) {

        view.findViewById(R.id.btnOpt1).setOnClickListener(v -> startActivity(new Intent(mContext, BarcodeActivity.class)));

        view.findViewById(R.id.btnOpt2).setOnClickListener(v -> startActivity(new Intent(mContext, VrpActivity.class)));
//        view.findViewById(R.id.btnOpt2).setOnClickListener(v -> startActivity(new Intent(mContext, RoadmapActivity.class)));

        view.findViewById(R.id.btnMap).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_MAP));
            startActivity(intent);
        });

        view.findViewById(R.id.btnEpackage).setOnClickListener(v -> startActivity(new Intent(mContext, StoreReceiptEpackageActivity.class)));
        view.findViewById(R.id.btnTravels).setOnClickListener(v -> startActivity(new Intent(mContext, TravelsActivity.class)));

    }
}
