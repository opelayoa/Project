package com.tiendas3b.almacen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.PendingDocumentsActivity;

/**
 * Created by dfa on 19/07/2016.
 */
public class ManagementFragment extends Fragment {

    private GlobalState mContext;

    public ManagementFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (GlobalState) getActivity().getApplicationContext();
        getActivity().setTitle(R.string.menu_drw_management);
        View view = inflater.inflate(R.layout.fragment_management, container, false);
        init(view);
        return view;
    }

    private void init(final View view) {

        view.findViewById(R.id.btnOpt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PendingDocumentsActivity.class));
//                startActivity(new Intent(mContext, ConstructionActivity.class));
            }
        });

//        view.findViewById(R.id.btnOpt2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mContext, RoadmapActivity.class));
//            }
//        });

    }
}
