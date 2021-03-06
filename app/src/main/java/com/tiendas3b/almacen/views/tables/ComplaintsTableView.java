package com.tiendas3b.almacen.views.tables;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Complaint;
import com.tiendas3b.almacen.views.tables.comparators.ComplaintsComparators;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;

/**
 * Created by dfa on 11/04/2016.
 */
public class ComplaintsTableView extends SortableTableView<Complaint> {

    public ComplaintsTableView(Context context) {
        this(context, null);
    }

    public ComplaintsTableView(Context context, AttributeSet attributes) {
        this(context, attributes, 0);
    }

    public ComplaintsTableView(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);

        CenterTableHeaderAdapter simpleTableHeaderAdapter = new CenterTableHeaderAdapter(context,
                "Tda", "Cve", "Ped", "SM", "Ace", /*"Con",*/"Obs", "Acc", "AR", "SR", "Des", "Cos", "Ven", "ObsTda");
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

        setColumnWeight(5, 2);
        setColumnWeight(9, 3);
        setColumnWeight(12, 3);

        setColumnComparator(0, ComplaintsComparators.getStoreComparator());
        setColumnComparator(1, ComplaintsComparators.getIclaveComparator());
        setColumnComparator(7, ComplaintsComparators.getRAComparator());
        setColumnComparator(8, ComplaintsComparators.getRSComparator());
        setColumnComparator(9, ComplaintsComparators.getDesComparator());
        setColumnComparator(10, ComplaintsComparators.getCosComparator());
        setColumnComparator(11, ComplaintsComparators.getVenComparator());
    }
}