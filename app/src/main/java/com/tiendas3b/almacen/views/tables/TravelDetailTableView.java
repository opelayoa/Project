package com.tiendas3b.almacen.views.tables;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.dto.TravelDetailDTO;
import com.tiendas3b.almacen.views.tables.comparators.TravelDetailComparators;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;

/**
 * Created by dfa on 11/04/2016.
 */
public class TravelDetailTableView extends SortableTableView<TravelDetailDTO> {

    public TravelDetailTableView(Context context) {
        this(context, null);
    }

    public TravelDetailTableView(Context context, AttributeSet attributes) {
        this(context, attributes, 0);
    }

    public TravelDetailTableView(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);

        CenterTableHeaderAdapter simpleTableHeaderAdapter = new CenterTableHeaderAdapter(context,
        "Des", "Tmas", "Destino", "Km inicial", "Km final", "Km Dif", "Hr Entrega", "Hr Salida", "Tpo Dif", "Caseta", "Diesel", "Multa", "Otro");
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
//        setColumnWeight(5, 2);
//        setColumnWeight(6, 2);

        setColumnComparator(0, TravelDetailComparators.getActDescComparator());
        setColumnComparator(1, TravelDetailComparators.getScaffoldComparator());
        setColumnComparator(2, TravelDetailComparators.getDestinationComparator());
        setColumnComparator(3, TravelDetailComparators.getInitialKmComparator());
        setColumnComparator(4, TravelDetailComparators.getFinalKmComparator());
        setColumnComparator(5, TravelDetailComparators.getDiffKmComparator());
        setColumnComparator(6, TravelDetailComparators.getInitialTimeComparator());
        setColumnComparator(7, TravelDetailComparators.getFinalTimeComparator());
        setColumnComparator(8, TravelDetailComparators.getDiffTimeComparator());
        setColumnComparator(9, TravelDetailComparators.getTollComparator());
        setColumnComparator(10, TravelDetailComparators.getDieselComparator());
        setColumnComparator(11, TravelDetailComparators.getTrafficTicketComparator());
        setColumnComparator(12, TravelDetailComparators.getOtherComparator());
    }
}