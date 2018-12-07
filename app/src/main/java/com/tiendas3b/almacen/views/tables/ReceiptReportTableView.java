package com.tiendas3b.almacen.views.tables;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.receipt.report.ReceiptSheetDetPrintDTO;

import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;

/**
 * Created by dfa on 11/04/2016.
 */
public class ReceiptReportTableView extends TableView<ReceiptSheetDetPrintDTO> {

    private static final int TEXT_SIZE = 4;

    public ReceiptReportTableView(Context context) {
        this(context, null);
    }

    public ReceiptReportTableView(Context context, AttributeSet attributes) {
        this(context, attributes, 0);
    }

    public ReceiptReportTableView(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);

        CustomTableHeaderAdapter simpleTableHeaderAdapter = new CustomTableHeaderAdapter(context,
                "Clave", "Código barras", "Descripción", "Empaque", "Piezas"
                , "Cajas", "Costo Uni", "Costo total");
        setHeaderAdapter(simpleTableHeaderAdapter);

        int rowColorEven;
        int rowColorOdd;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            simpleTableHeaderAdapter.setTextColor(context.getResources().getColor(android.R.color.white, context.getTheme()));
            rowColorEven = context.getResources().getColor(android.R.color.white, context.getTheme());
            rowColorOdd = context.getResources().getColor(R.color.table_data_row_odd, context.getTheme());
        } else {
            simpleTableHeaderAdapter.setTextColor(context.getResources().getColor(android.R.color.white));
            rowColorEven = context.getResources().getColor(android.R.color.white);
            rowColorOdd = context.getResources().getColor(R.color.table_data_row_odd);
        }
        setDataRowColorizer(TableDataRowColorizers.alternatingRows(rowColorEven, rowColorOdd));
//        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());
        setColumnWeight(1, 2);
        setColumnWeight(2, 3);
        setColumnWeight(7, 2);
    }

    private class CustomTableHeaderAdapter extends TableHeaderAdapter {

        private final String[] headers;
        private int textColor = 0x99000000;

        CustomTableHeaderAdapter(final Context context, final String... headers) {
            super(context);
            this.headers = headers;
        }

        public void setTextColor(final int textColor) {
            this.textColor = textColor;
        }

        @Override
        public View getHeaderView(final int columnIndex, final ViewGroup parentView) {
            final TextView textView = new TextView(getContext());
            if (columnIndex < headers.length) {
                textView.setText(headers[columnIndex]);
            }
            textView.setPadding(0, 20, 0, 20);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_MM, TEXT_SIZE);
            textView.setTextColor(textColor);
            textView.setMaxLines(1);
//            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setGravity(Gravity.CENTER);
            return textView;
        }

    }

}