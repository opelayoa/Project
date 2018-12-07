package com.tiendas3b.almacen.views.tables;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.dto.GeneralTravelDTO;
import com.tiendas3b.almacen.views.tables.comparators.TravelComparators;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;

/**
 * Created by dfa on 11/04/2016.
 */
public class TravelTableView extends SortableTableView<GeneralTravelDTO> {

    public TravelTableView(Context context) {
        this(context, null);
    }

    public TravelTableView(Context context, AttributeSet attributes) {
        this(context, attributes, 0);
    }

    public TravelTableView(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);

        CenterTableHeaderAdapter simpleTableHeaderAdapter = new CenterTableHeaderAdapter(context,
        "Camion", "Est", "Tdas", "Vjes", "Tmas", "Dscgs", /*"Tmas Dscgs",*/ "Km Vje", "Km Total", "Tpo Vje", "Tpo Total", "Caseta", "Diesel", "Multa", "Otro");
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

//        setColumnWeight(0, 2);
//        setColumnWeight(1, 2);
//        setColumnWeight(2, 2);
//        setColumnWeight(3, 2);
//        setColumnWeight(4, 2);
//        setColumnWeight(5, 2);
//        setColumnWeight(6, 2);
//        setColumnWeight(7, 2);
//        setColumnWeight(8, 2);
//        setColumnWeight(9, 2);
//        setColumnWeight(10, 2);
//        setColumnWeight(11, 2);
//        setColumnWeight(12, 2);
//        setColumnWeight(13, 2);

        setColumnComparator(0, TravelComparators.getTruckComparator());
        setColumnComparator(1, TravelComparators.getStatusComparator());
        setColumnComparator(2, TravelComparators.getStoreComparator());
        setColumnComparator(3, TravelComparators.getTravelComparator());
        setColumnComparator(4, TravelComparators.getScaffoldComparator());
        setColumnComparator(5, TravelComparators.getDownloadComparator());
        setColumnComparator(6, TravelComparators.getTravelKmComparator());
        setColumnComparator(7, TravelComparators.getTotalKmComparator());
        setColumnComparator(8, TravelComparators.getTravelTimeComparator());
        setColumnComparator(9, TravelComparators.getTotalTimeComparator());
        setColumnComparator(10, TravelComparators.getTollComparator());
        setColumnComparator(11, TravelComparators.getDieselComparator());
        setColumnComparator(12, TravelComparators.getTrafficTicketComparator());
        setColumnComparator(13, TravelComparators.getOtherComparator());
    }
}