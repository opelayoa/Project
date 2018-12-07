package com.tiendas3b.almacen.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.satsuware.usefulviews.LabelledSpinner;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.BuyDetail;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.fragments.EditDecreaseFragment;

import java.util.List;

public class EditReceiptDecreaseRecyclerViewAdapter extends RecyclerView.Adapter<EditReceiptDecreaseRecyclerViewAdapter.ViewHolder> {

    private List<BuyDetail> buyList;
    private int layout;
    private final Context mContext;
    private final EditDecreaseFragment.OnFragmentInteractionListener mListener;
    private final SpinnerAdapter adapterObs;
    private final SpinnerAdapter adapterObsLog;
    private final SpinnerAdapter adapterType;

    public EditReceiptDecreaseRecyclerViewAdapter(Context context, List<BuyDetail> buyList, int layout, EditDecreaseFragment.OnFragmentInteractionListener mListener,
                                                  List<ObservationType> types, List<ObservationType> obs, List<ObservationType> obsLog) {
        this.mContext = context;
        this.buyList = buyList;
        this.layout = layout;
        this.mListener = mListener;
        adapterType = new SpinnerAdapter(mContext, R.layout.spinner_item_table, types);
        adapterObs = new SpinnerAdapter(mContext, R.layout.spinner_item_table, obs);
        adapterObsLog = new SpinnerAdapter(mContext, R.layout.spinner_item_table, obsLog);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(buyList.get(position));
        holder.btnSaveDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSaveDetail(holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return buyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final LinearLayout llCaptureSheetDetail;
        public final TextView vOwnBrand;
        public final TextView lblDescription;
        public final EditText txtSample;
        public final LabelledSpinner spnObs;
        public final LabelledSpinner spnObsLog;
        public final LabelledSpinner spnType;
        public final Button btnSaveDetail;
        public boolean open = false;
        public String barcode;
        public Integer folioOdc;
        public Long spnObsId, spnObsLogId, spnTypeId;

        public ViewHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.llBuyDetail);
            llCaptureSheetDetail = view.findViewById(R.id.capture_sheet_detail);
            vOwnBrand = view.findViewById(R.id.vOwnBrand);
            lblDescription = view.findViewById(R.id.lblDescription);
            txtSample = view.findViewById(R.id.txtSample);
            spnType = view.findViewById(R.id.spnType);
            spnObs = view.findViewById(R.id.spnObs);
            spnObsLog = view.findViewById(R.id.spnObsLog);
            btnSaveDetail = view.findViewById(R.id.btnSaveDetail);

        }

        public void bind(BuyDetail buyDetail) {
            VArticle article = buyDetail.getArticle();

            this.vOwnBrand.setText(String.valueOf(getAdapterPosition() + 1));
            GradientDrawable bg = (GradientDrawable) this.vOwnBrand.getBackground();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                bg.setColor(mContext.getResources().getColor(getColor(article.getTypeId()), mContext.getTheme()));
            } else {
                bg.setColor(mContext.getResources().getColor(getColor(article.getTypeId())));
            }
            this.barcode = buyDetail.getArticle().getBarcode();
            this.folioOdc = buyDetail.getBuy().getFolio();
            this.lblDescription.setText(article.getIclave() + "-" + article.getDescription().concat(" ").concat(article.getUnity()));
            this.spnObs.setCustomAdapter(adapterObs);
            this.spnObsLog.setCustomAdapter(adapterObsLog);
            this.spnType.setCustomAdapter(adapterType);

            this.spnObs.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
                @Override
                public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                    spnObsId = id;
                }

                @Override
                public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
                }
            });
            this.spnObsLog.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
                @Override
                public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                    spnObsLogId = id;
                }

                @Override
                public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
                }
            });
            this.spnType.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
                @Override
                public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                    spnTypeId = id;
                }
                @Override
                public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
                }
            });


            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open = true;
                    llCaptureSheetDetail.setVisibility(View.VISIBLE);
                }
            };

            this.mView.setOnClickListener(clickListener);
            this.lblDescription.setOnClickListener(clickListener);

            if (this.open) {
                this.llCaptureSheetDetail.setVisibility(View.VISIBLE);
            } else {
                this.llCaptureSheetDetail.setVisibility(View.GONE);
            }
        }
    }

    private int getColor(Integer type) {
        if (type == 8) {
            return R.color.own_brand;
        }
        return R.color.comercial_brand;
    }

}