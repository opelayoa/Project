package com.tiendas3b.almacen.adapters;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.util.Constants;

import java.text.DecimalFormat;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class ReturnVendorReportTableDataAdapter extends TableDataAdapter<VArticle> {

    private static final int TEXT_SIZE = 14;
    private final Context mContext;
    private List<VArticle> items;

    public ReturnVendorReportTableDataAdapter(Context context, List<VArticle> data) {
        super(context, data);
        mContext = context;
        items = data;
    }


    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        VArticle item = getRowData(rowIndex);

        if(rowIndex < getCount() - 1) {
            switch (columnIndex) {
                case 0:
                    renderedView = renderString(String.valueOf(rowIndex + 1), Gravity.CENTER_HORIZONTAL);
                    break;
                case 1:
                    renderedView = renderString(String.valueOf(item.getIclave()), Gravity.CENTER_HORIZONTAL);
                    break;
                case 2:
                    renderedView = renderString(item.getDescription() + " / " + item.getUnity(), Gravity.START);
                    break;
                case 3:
//                    renderedView = renderString(String.valueOf(item.getAmount()), Gravity.CENTER_HORIZONTAL);
                    break;
                case 4:
                    renderedView = renderString(String.valueOf(item.getCost()), Gravity.CENTER_HORIZONTAL);
                    break;
                case 5:
//                    renderedView = renderString(item.getObservation().getDescription(), Gravity.CENTER_HORIZONTAL);//FIXME
                    break;
            }
        } else {
            switch (columnIndex) {
                case 2:
                    renderedView = renderString("Totales", Gravity.END);
                    break;
                case 3:
//                    renderedView = renderString(String.valueOf(item.getAmount()), Gravity.CENTER_HORIZONTAL);
                    break;
                case 4:
                    DecimalFormat df = new DecimalFormat(Constants.FORMAT_MONEY);
                    renderedView = renderString(df.format(item.getCost()), Gravity.CENTER_HORIZONTAL);
                    break;
            }
        }

        return renderedView;
    }


    private View renderString(String value, int gravity) {
        TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setGravity(gravity);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_MM, 3);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            textView.setTextColor(mContext.getResources().getColor(android.R.color.black, mContext.getTheme()));
        } else {
            textView.setTextColor(mContext.getResources().getColor(android.R.color.black));
        }
        return textView;
    }
}
