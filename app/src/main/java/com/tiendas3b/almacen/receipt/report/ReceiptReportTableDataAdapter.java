package com.tiendas3b.almacen.receipt.report;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiendas3b.almacen.util.NumbersUtil;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class ReceiptReportTableDataAdapter extends TableDataAdapter<ReceiptSheetDetPrintDTO> {

    private static final int TEXT_SIZE = 14;
    private final Context mContext;
//    private List<ReceiptSheetDetPrintDTO> items;

    public ReceiptReportTableDataAdapter(Context context, List<ReceiptSheetDetPrintDTO> data) {
        super(context, data);
        mContext = context;
//        items = data;
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        ReceiptSheetDetPrintDTO item = getRowData(rowIndex);
//        if(rowIndex < getCount() - 1) {
            switch (columnIndex) {
                case 0:
                    renderedView = renderString(String.valueOf(item.getIclave()), Gravity.CENTER_HORIZONTAL);
                    break;
                case 1:
                    renderedView = renderString(item.getBarcode(), Gravity.CENTER_HORIZONTAL);
                    break;
                case 2:
                    renderedView = renderString(item.getDescription(), Gravity.START);
                    break;
                case 3:
                    renderedView = renderString(String.valueOf(item.getPacking()), Gravity.CENTER_HORIZONTAL);
                    break;
                case 4:
                    renderedView = renderString(NumbersUtil.thousandsSeparator(item.getPieces()), Gravity.CENTER_HORIZONTAL);
                    break;
                case 5:
                    renderedView = renderString(NumbersUtil.thousandsSeparator(item.getBoxes()), Gravity.CENTER_HORIZONTAL);
                    break;
                case 6:
                    renderedView = renderString(NumbersUtil.moneyFormat(item.getUnitCost()), Gravity.END);
                    break;
                case 7:
                    renderedView = renderString(NumbersUtil.moneyFormat(item.getTotalCost()), Gravity.END);
                    break;
            }
//        }
//        else {
//            switch (columnIndex) {
//                case 2:
//                    renderedView = renderString("Totales", Gravity.END);
//                    break;
//                case 3:
//                    renderedView = renderString(String.valueOf(item.getAmount()), Gravity.CENTER_HORIZONTAL);
//                    break;
//                case 4:
//                    DecimalFormat df = new DecimalFormat(Constants.FORMAT_MONEY);
//                    renderedView = renderString(df.format(item.getCost()), Gravity.CENTER_HORIZONTAL);
//                    break;
//            }
//        }

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
