package com.tiendas3b.almacen.views.tables;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.ReceiptSheet;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;

/**
 * Created by dfa on 11/04/2016.
 */
public class ReceiptSheetTableView extends SortableTableView<ReceiptSheet> {

    private static final int TEXT_SIZE = 16;

    public ReceiptSheetTableView(Context context) {
        this(context, null);
    }

    public ReceiptSheetTableView(Context context, AttributeSet attributes) {
        this(context, attributes, 0);
    }

    public ReceiptSheetTableView(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);

        SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context,
                "Proveedor", "ODC", "Fact.", "EM", "Cita", "Llegada", "Entrega", "Salida", /*"Llegó",*/ /*"Recibidores",*/
                /*"Cita",*/ "Paletizado", "Mto fac", "Mto em", /*"RFC", "IVA", "IEPS",*/ "Puerta");
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
        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());

        setColumnWeight(0, 2);
        setColumnWeight(9, 2);
        setColumnWeight(10, 2);
//        setColumnWeight(2, 2);
//        setColumnWeight(12, 2);
//        setColumnWeight(13, 2);
//        setColumnWeight(14, 2);
//        setColumnWeight(15, 2);
//        setColumnWeight(16, 2);

//        setColumnComparator(0, ReceiptSheetComparators.getProviderComparator());
//        setColumnComparator(4, ReceiptSheetComparators.getDateTimeComparator());
//        setColumnComparator(17, ReceiptSheetComparators.getPlatfomrComparator());
    }

}