package com.tiendas3b.almacen.receipt.bulk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.ExpressArticle;

/**
 * Created by dfa on 11/08/2016.
 */
public class InBulkReceiptRvAdapter extends BaseRecyclerViewAdapter<ExpressArticle> {

    private final Context ctx;


    public InBulkReceiptRvAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
        ctx = context;
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        @SuppressLint("RestrictedApi") ContextThemeWrapper ctx = new ContextThemeWrapper(context, R.style.AppTheme);
        View view = LayoutInflater.from(ctx).inflate(R.layout.row_in_bulk_receipt, viewGroup, false);

//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.row_epackage, viewGroup, false);
        return view;
    }

    @Override
    protected void bindView(ExpressArticle item, ViewHolder viewHolder) {
        if (item != null) {
            viewHolder.setItem(item);
//            EditText txtArticleId = (EditText) viewHolder.getView(R.id.txtArticleId);
            TextView lblArticleName = (TextView) viewHolder.getView(R.id.lblArticleName);
//            final EditText txtBoxesNum = (EditText) viewHolder.getView(R.id.txtBoxesNum);
//            final ImageButton btnOk = (ImageButton) viewHolder.getView(R.id.btnOk);

//            VArticle article = item.getArticle();
//            txtArticleId.setText(String.valueOf(item.getIclave()));
            lblArticleName.setText(item.getIclave() + " - " + item.getDescription() + " / " + item.getUnity());
//            int boxes = item.getCount();
//            if(boxes == 0) {
//                txtBoxesNum.setText("");
//                txtBoxesNum.setEnabled(true);
//                btnOk.setEnabled(true);
//            } else {
//                txtBoxesNum.setText(String.valueOf(boxes));
//                txtBoxesNum.setEnabled(false);
//                btnOk.setEnabled(false);
//            }
//
//            btnOk.setOnClickListener(v -> {
//                int boxesNum = Integer.parseInt(txtBoxesNum.getText().toString());
////                if(boxesNum == item.getTotal()) {
//                    item.setCount(boxesNum);
//                    txtBoxesNum.setEnabled(false);
//                    btnOk.setEnabled(false);
//                    Toast.makeText(ctx, "Guardado", Toast.LENGTH_LONG).show();
////                } else {
//////                    Toast.makeText(ctx, "Número de cajas diferente a ODC", Toast.LENGTH_LONG).show();
////                }
//            });
        }
    }
}
