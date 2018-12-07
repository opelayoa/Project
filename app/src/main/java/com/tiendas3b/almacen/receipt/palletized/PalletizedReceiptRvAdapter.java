package com.tiendas3b.almacen.receipt.palletized;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.VArticle;

/**
 * Created by dfa on 11/08/2016.
 */
public class PalletizedReceiptRvAdapter extends BaseRecyclerViewAdapter<VArticlePalletized> {
    private final Context ctx;

//    private EpackageActivity activity;

    public PalletizedReceiptRvAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
//        activity = context;
        ctx = context;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        @SuppressLint("RestrictedApi") ContextThemeWrapper ctx = new ContextThemeWrapper(context, R.style.AppTheme);
        View view = LayoutInflater.from(ctx).inflate(R.layout.row_palletized_receipt, viewGroup, false);

//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.row_epackage, viewGroup, false);
        return view;
    }

    @Override
    protected void bindView(VArticlePalletized item, ViewHolder viewHolder) {
        if (item != null) {
            viewHolder.setItem(item);
            EditText txtArticleId = (EditText) viewHolder.getView(R.id.txtArticleId);
            TextView lblArticleName = (TextView) viewHolder.getView(R.id.lblArticleName);
            EditText txtScaffoldNum = (EditText) viewHolder.getView(R.id.txtScaffoldNum);
            ImageButton btnAdd = (ImageButton) viewHolder.getView(R.id.btnAdd);
            ImageButton btnRemove = (ImageButton) viewHolder.getView(R.id.btnRemove);

            VArticle article = item.getArticle();
            txtArticleId.setText(String.valueOf(article.getIclave()));
            lblArticleName.setText(article.getDescription() + " / " + article.getUnity());
            txtScaffoldNum.setText(item.getCount() + " / " + item.getTotal());

            btnAdd.setOnClickListener(v -> {
                int newValue = item.getCount() + 1;
                if(newValue <= item.getTotal()) {
                    item.setCount(newValue);
                    txtScaffoldNum.setText(item.getCount() + " / " + item.getTotal());
                } else {
                    Toast.makeText(ctx, "No puedes agragar más tarimas", Toast.LENGTH_LONG).show();
                }
            });
            btnRemove.setOnClickListener(v -> {
                int newValue = item.getCount() - 1;
                if(newValue < 0) {
                    Toast.makeText(ctx, "No puedes restar más tarimas", Toast.LENGTH_LONG).show();
                } else {
                    item.setCount(newValue);
                    txtScaffoldNum.setText(item.getCount() + " / " + item.getTotal());
                }
            });
        }
    }
}
