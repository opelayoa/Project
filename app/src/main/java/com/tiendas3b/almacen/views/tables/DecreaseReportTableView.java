package com.tiendas3b.almacen.views.tables;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.views.tables.vo.ArticleVO;

import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;

/**
 * Created by dfa on 11/04/2016.
 */
public class DecreaseReportTableView extends TableView<ArticleVO> {

    private static final int TEXT_SIZE = 16;

    public DecreaseReportTableView(Context context) {
        this(context, null);
    }

    public DecreaseReportTableView(Context context, AttributeSet attributes) {
        this(context, attributes, 0);
    }

    public DecreaseReportTableView(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);

        CustomTableHeaderAdapter simpleTableHeaderAdapter = new CustomTableHeaderAdapter(context,
                "Cons.", "Clave", "Desc.", "Pzs"
//                , "Cajas #", "Costo $"
                , "Costo", "Obs.");
//        simpleTableHeaderAdapter.setTextSize(TEXT_SIZE);
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

        setColumnWeight(2, 4);
        setColumnWeight(5, 2);
//        setColumnWeight(4, 2);

//        setColumnComparator(0, DecreaseCaptureComparators.getIdComparator());
//        setColumnComparator(4, ReceiptSheetComparators.getDateTimeComparator());
//        setColumnComparator(17, ReceiptSheetComparators.getIdComparator());
    }

    public class CustomTableHeaderAdapter extends TableHeaderAdapter {

        private final String[] headers;
        private int textColor = 0x99000000;

        public CustomTableHeaderAdapter(final Context context, final String... headers) {
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
            textView.setPadding(10, 20, 10, 20);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize(TEXT_SIZE);
            textView.setTextColor(textColor);
            textView.setMaxLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setGravity(Gravity.CENTER);
            return textView;
        }

    }

}