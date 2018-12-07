package com.tiendas3b.almacen.views.tables;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.PendingDocumentDetail;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;

/**
 * Created by dfa on 11/04/2016.
 */
public class PendingDocumentsDetailTableView extends SortableTableView<PendingDocumentDetail> {

    public PendingDocumentsDetailTableView(Context context) {
        this(context, null);
    }

    public PendingDocumentsDetailTableView(Context context, AttributeSet attributes) {
        this(context, attributes, 0);
    }

    public PendingDocumentsDetailTableView(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);

        CenterTableHeaderAdapter simpleTableHeaderAdapter = new CenterTableHeaderAdapter(context,
        "Tipo", "Evt", "Tds", "Pzs");
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
//        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());

//        setColumnWeight(0, 2);

//        setColumnComparator(0, TravelComparators.getTruckComparator() );
    }
}