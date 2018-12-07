package com.tiendas3b.almacen.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.BuyDetail;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.fragments.EditBoxesFragment;

import java.util.List;
import java.util.Locale;

public class EditReceiptBoxesRecyclerViewAdapter extends RecyclerView.Adapter<EditReceiptBoxesRecyclerViewAdapter.ViewHolder> {

    private List<BuyDetail> buyList;
    private int layout;
    private final Context mContext;
    private final EditBoxesFragment.OnFragmentInteractionListener mListener;
    private final Locale locale = Locale.getDefault();
    private static final String MILES = "%,d";

    public EditReceiptBoxesRecyclerViewAdapter(Context context, List<BuyDetail> buyList, int layout, EditBoxesFragment.OnFragmentInteractionListener mListener) {
        this.mContext = context;
        this.buyList = buyList;
        this.layout = layout;
        this.mListener = mListener;

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
        public final TextView txtBarcode;
        public final TextView txtOdcAmount;
        public final TextView txtOdcBuyUnit;
        public final TextView txtPzsXcaja;
        public final TextView txtBillAmount;
        public final TextView txtReceivedAmount;
        public final TextView txtMissingAmount;
        public final Button btnSaveDetail;
        public boolean open = false;

        public ViewHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.llBuyDetail);
            llCaptureSheetDetail = view.findViewById(R.id.capture_sheet_detail);
            vOwnBrand = view.findViewById(R.id.vOwnBrand);
            lblDescription = view.findViewById(R.id.lblDescription);
            txtBarcode = view.findViewById(R.id.txtBarcode);
            txtOdcAmount = view.findViewById(R.id.txtOdcAmount);
            txtOdcBuyUnit = view.findViewById(R.id.txtOdcBuyUnit);
            txtPzsXcaja = view.findViewById(R.id.txtPzsXcaja);
            txtBillAmount = view.findViewById(R.id.txtBillAmount);
            txtReceivedAmount = view.findViewById(R.id.txtReceivedAmount);
            txtMissingAmount = view.findViewById(R.id.txtMissingAmount);
            btnSaveDetail = view.findViewById(R.id.btnSaveDetail);
        }

        public void bind(BuyDetail buyDetail) {
            VArticle article = buyDetail.getArticle();
            int pieces = buyDetail.getArticleAmount();
            float packing = article.getPacking().floatValue();
            int amountUnit = (int) (pieces / packing);
            this.vOwnBrand.setText(String.valueOf(getAdapterPosition() + 1));
            GradientDrawable bg = (GradientDrawable) this.vOwnBrand.getBackground();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                bg.setColor(mContext.getResources().getColor(getColor(article.getTypeId()), mContext.getTheme()));
            } else {
                bg.setColor(mContext.getResources().getColor(getColor(article.getTypeId())));
            }
            this.lblDescription.setText(article.getIclave() + "-" + article.getDescription().concat(" ").concat(article.getUnity()));

            txtBarcode.setText(article.getBarcode());
            txtOdcAmount.setText(String.format(locale, MILES, pieces));
            txtOdcBuyUnit.setText(String.format(locale, MILES, amountUnit));
            txtPzsXcaja.setText(String.format(locale, MILES, (int) packing));


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