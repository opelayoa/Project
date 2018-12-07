package com.tiendas3b.almacen.receipt.express;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ExpressReceiptFragment extends Fragment {

    private Unbinder unbinder;
    @BindView(R.id.btnExpressReceipt)
    Button btnExpressReceipt;

    private MainActivity mContext;

    public ExpressReceiptFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (MainActivity) getActivity();
        mContext.setTitle("Recibo Exprés");
        View view = inflater.inflate(R.layout.fragment_receipt_express, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void init() {
        btnExpressReceipt.setOnClickListener(v -> startActivity(new Intent(mContext, ProvidersActivity.class)));
    }

}
