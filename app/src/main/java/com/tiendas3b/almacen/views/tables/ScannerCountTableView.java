package com.tiendas3b.almacen.views.tables;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.ScannerCount;
import com.tiendas3b.almacen.views.tables.comparators.ScannerCountComparators;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;

/**
 * Created by dfa on 11/04/2016.
 */
public class ScannerCountTableView extends SortableTableView<ScannerCount> {

    public ScannerCountTableView(Context context) {
        this(context, null);
    }

    public ScannerCountTableView(Context context, AttributeSet attributes) {
        this(context, attributes, 0);
    }

    public ScannerCountTableView(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);

        CenterTableHeaderAdapter simpleTableHeaderAdapter = new CenterTableHeaderAdapter(context,
                "Cve", "Fol", "Can", "Des", "Cos", "Ven");
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
//        setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd));
        setDataRowColorizer(TableDataRowColorizers.alternatingRows(rowColorEven, rowColorOdd));
        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());

        setColumnWeight(3, 2);

        setColumnComparator(0, ScannerCountComparators.getIclaveComparator());
        setColumnComparator(1, ScannerCountComparators.getFolComparator());
        setColumnComparator(2, ScannerCountComparators.getCanComparator());
        setColumnComparator(3, ScannerCountComparators.getDesComparator());
        setColumnComparator(4, ScannerCountComparators.getCosComparator());
        setColumnComparator(5, ScannerCountComparators.getVenComparator());
    }
}