package com.tiendas3b.almacen.views.tables;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetail;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;

/**
 * Created by dfa on 11/04/2016.
 */
public class ReceiptsheetDetailTableView extends TableView<ReceiptSheetDetail> {

    private static final int TEXT_SIZE = 16;

    public ReceiptsheetDetailTableView(Context context) {
        this(context, null);
    }

    public ReceiptsheetDetailTableView(Context context, AttributeSet attributes) {
        this(context, attributes, 0);
    }

    public ReceiptsheetDetailTableView(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);

//        CenterTableHeaderAdapter simpleTableHeaderAdapter = new CenterTableHeaderAdapter(context,
        SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context,
                "Clave", "Desc."
                , "Cad.", "ODC", "EM", "FR");
        simpleTableHeaderAdapter.setTextSize(TEXT_SIZE);
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
//        setColumnWeight(3, 2);
//        setColumnWeight(7, 2);

//        setColumnComparator(0, DecreaseCaptureComparators.getIdComparator());
//        setColumnComparator(4, ReceiptSheetComparators.getDateTimeComparator());
//        setColumnComparator(17, ReceiptSheetComparators.getIdComparator());
    }

//    public class CenterTableHeaderAdapter extends TableHeaderAdapter {
//
//        private final String[] headers;
//        private int textColor = 0x99000000;
//
//        public CenterTableHeaderAdapter(final Context context, final String... headers) {
//            super(context);
//            this.headers = headers;
//        }
//
//        public void setTextColor(final int textColor) {
//            this.textColor = textColor;
//        }
//
//        @Override
//        public View getHeaderView(final int columnIndex, final ViewGroup parentView) {
//            final TextView textView = new TextView(getContext());
//            if (columnIndex < headers.length) {
//                textView.setText(headers[columnIndex]);
//            }
//            textView.setPadding(10, 20, 10, 20);
//            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
//            textView.setTextSize(TEXT_SIZE);
//            textView.setTextColor(textColor);
//            textView.setMaxLines(2);
//            textView.setEllipsize(TextUtils.TruncateAt.END);
//            textView.setGravity(Gravity.CENTER);
//
//            switch( columnIndex ) {
//                case 5:
//                    ImageView imgView = new ImageView(getContext());
//                    imgView.setImageResource(R.drawable.ic_returnable_24dp);
//                    return imgView;
//                default: break;
//            }
//            return textView;
//        }
//
//    }

}