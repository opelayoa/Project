package com.tiendas3b.almacen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.DecreaseCaptureActivity;
import com.tiendas3b.almacen.activities.InputTransferenceActivity;
import com.tiendas3b.almacen.activities.MainActivity;
import com.tiendas3b.almacen.activities.ODCsActivity;
import com.tiendas3b.almacen.activities.ReturnVendorActivity;
import com.tiendas3b.almacen.activities.ReturnVendorActivityBase;
import com.tiendas3b.almacen.activities.TimetableReceiptActivity;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.receipt.edit.ReceiptEditTimetableActivity;
import com.tiendas3b.almacen.receipt.epackage.EpackageProviderActivity;
import com.tiendas3b.almacen.receipt.express.ProvidersActivity;
import com.tiendas3b.almacen.receipt.express2.tabs.ReceiptTimetableStatusActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tiendas3b.almacen.R.id.btnReceiptOpt7;

public class ReceiptFragment extends Fragment {
    public static final int REQUEST_CODE = 14;
    public static final int RQ_RETURN_VENDOR = 15;

    private Unbinder unbinder;
    @BindView(R.id.btnExpressReceipt)
    Button btnExpressReceipt;
    @BindView(R.id.btnReceiptEpackage)
    Button btnReceiptEpackage;
    @BindView(R.id.btnReceiptEpackage2)
    Button btnReceiptEpackage2;

    private MainActivity mContext;

    public ReceiptFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (MainActivity) getActivity();
        getActivity().setTitle(R.string.menu_drw_receipt);
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);
        unbinder = ButterKnife.bind(this, view);
        init(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void init(View view) {
        view.findViewById(R.id.btnReceiptOpt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IDatabaseManager db = new DatabaseManager(mContext);
                if(db.isEmpty(VArticle.class)){
                    Toast.makeText(mContext, "Espera o sincroniza info", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(mContext, ODCsActivity.class));
                }

            }
        });
        view.findViewById(R.id.btnReceiptOpt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, TimetableReceiptActivity.class));
            }
        });
//        view.findViewById(R.id.btnReceiptOpt3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mContext, ReceiptSheetActivity.class));
//            }
//        });
        view.findViewById(R.id.btnReceiptOpt4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(mContext, DecreaseActivity.class));
                startActivity(new Intent(mContext, DecreaseCaptureActivity.class));
            }
        });
        view.findViewById(R.id.btnReceiptOpt5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, ReturnVendorActivity.class), RQ_RETURN_VENDOR);
            }
        });
        view.findViewById(R.id.btnReceiptOpt6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(mContext, ConstructionActivity.class));
                Intent intentTicketDetailEdit = new Intent(mContext , InputTransferenceActivity.class);
                getActivity().startActivityForResult(intentTicketDetailEdit, REQUEST_CODE);
            }
        });
        view.findViewById(btnReceiptOpt7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(mContext, ConstructionActivity.class));
                startActivityForResult(new Intent(mContext, ReturnVendorActivityBase.class), RQ_RETURN_VENDOR);
            }
        });

        btnExpressReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mContext.changeMainContent(new ExpressReceiptFragment());
                startActivity(ProvidersActivity.getIntent(mContext, true));
            }
        });

        btnReceiptEpackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ReceiptTimetableStatusActivity.class).putExtra(ReceiptTimetableStatusActivity.EXTRA_ORDER_TYPE, 0));
            }
        });

        //TODO RECIBO EXPRESS 2
        btnReceiptEpackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ReceiptTimetableStatusActivity.class).putExtra(ReceiptTimetableStatusActivity.EXTRA_ORDER_TYPE, 0));
            }
        });

        //TODO EPACKAGE
        btnReceiptEpackage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, EpackageProviderActivity.class));
            }
        });

        //TODO EDICIÓN RECIBO EXPRESS 2
//        btnEditReceipt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mContext, ReceiptEditTimetableActivity.class).putExtra(ReceiptEditTimetableActivity.EXTRA_ORDER_TYPE, 0));
//            }
//        });

    }


    //TODO EDICIÓN RECIBO EXPRESS 2
    @OnClick(R.id.btnEditReceipt)
    public void btnEdit(){
        startActivity(new Intent(mContext, ReceiptEditTimetableActivity.class).putExtra(ReceiptEditTimetableActivity.EXTRA_ORDER_TYPE, 0));
    }

}
