package com.tiendas3b.almacen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.ArticlesResearchActivity;
import com.tiendas3b.almacen.activities.ComplaintsActivity;
import com.tiendas3b.almacen.activities.OutputTransferenceActivity;
import com.tiendas3b.almacen.activities.PhysicalInventoryActivity;
import com.tiendas3b.almacen.audit.epackage.StowageActivity;

public class AuditFragment extends Fragment {
    public static final int REQUEST_CODE = 13;

    private GlobalState mContext;

    public AuditFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (GlobalState) getActivity().getApplicationContext();
        getActivity().setTitle(R.string.menu_drw_audit);
        View view = inflater.inflate(R.layout.fragment_audit, container, false);
        init(view);
        return view;
    }

    private void init(final View view) {

        view.findViewById(R.id.btnReceiptOpt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ArticlesResearchActivity.class));
            }
        });

        view.findViewById(R.id.btnReceiptOpt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ComplaintsActivity.class));
            }
        });

        view.findViewById(R.id.btnReceiptOpt5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PhysicalInventoryActivity.class));
            }
        });

        view.findViewById(R.id.btnReceiptOpt6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTicketDetailEdit = new Intent(getActivity(), OutputTransferenceActivity.class);
                getActivity().startActivityForResult(intentTicketDetailEdit, REQUEST_CODE);

            }
        });

        Button ib = (Button) view.findViewById(R.id.btnEpackage);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, StowageActivity.class));
            }
        });
//        ib.setOnClickListener(v -> {
//            startActivity(new Intent(mContext, StowageActivity.class));
//        });

    }
}
