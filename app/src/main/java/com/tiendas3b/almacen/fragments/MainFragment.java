package com.tiendas3b.almacen.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.MainActivity;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.Preferences;

public class MainFragment extends Fragment {

    private MainActivity mContext;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = (MainActivity) getActivity();
//        FragmentMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
//        View view = binding.getRoot();
//
//        User user = new User();
//        Preferences p = new Preferences(mContext);
//        user.setGroupId(p.getSharedStringSafe(Preferences.KEY_GROUP, null));
//        Log.e("user!", "group:" + user.getGroupId());
//        user.setId(1L);
//        binding.setUser(user);

        getActivity().setTitle(R.string.areas);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        Preferences p = new Preferences(mContext);

//        String group = p.getSharedStringSafe(Preferences.KEY_GROUP, null);
        String group = "FINA";

        if (Constants.GROUP_REC.equals(group) || Constants.GROUP_GA.equals(group) || Constants.GROUP_FINA.equals(group)) {
            Button btnReceipt = view.findViewById(R.id.btnOpt1);
            btnReceipt.setVisibility(View.VISIBLE);
            btnReceipt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.changeMainContent(new ReceiptFragment());
                }
            });
        }
        if (Constants.GROUP_PCK.equals(group) || Constants.GROUP_GA.equals(group) || Constants.GROUP_FINA.equals(group)) {
            Button btnAudit = view.findViewById(R.id.btnOpt2);
            btnAudit.setVisibility(View.VISIBLE);
            btnAudit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.changeMainContent(new PickingFragment());
                }
            });
        }
        if (Constants.GROUP_AUDI.equals(group) || Constants.GROUP_GA.equals(group) || Constants.GROUP_FINA.equals(group)) {
            Button btn = view.findViewById(R.id.btnOpt3);
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.changeMainContent(new AuditFragment());
                }
            });
        }
        if (Constants.GROUP_EMB.equals(group) || Constants.GROUP_GA.equals(group) || Constants.GROUP_FINA.equals(group)) {
            Button btn = view.findViewById(R.id.btnOpt4);
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.changeMainContent(new ShipmentFragment());
                }
            });
        }
        if (Constants.GROUP_FINA.equals(group) || Constants.GROUP_GA.equals(group)) {
            Button btn = view.findViewById(R.id.btnOpt6);
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.changeMainContent(new ManagementFragment());
                }
            });
        }

//        if (Constants.GROUP_GA.equals(group) || Constants.GROUP_FINA.equals(group)) {
            Button btn = (Button) view.findViewById(R.id.btnOpt5);
//            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.changeMainContent(new MiscellaneousFragment());
                }
            });
//        }
    }
}
