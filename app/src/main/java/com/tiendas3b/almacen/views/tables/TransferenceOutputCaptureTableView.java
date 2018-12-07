package com.tiendas3b.almacen.views.tables;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.VArticle;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;

/**
 * Created by dfa on 11/04/2016.
 */
public class TransferenceOutputCaptureTableView extends TableView<VArticle> {

    private static final int TEXT_SIZE = 16;

    public TransferenceOutputCaptureTableView(Context context) {
        this(context, null);
    }

    public TransferenceOutputCaptureTableView(Context context, AttributeSet attributes) {
        this(context, attributes, 0);
    }

    public TransferenceOutputCaptureTableView(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);

        SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context,
                "Cve", "Desc."
//                , "Cajas #", "Costo $"
                , "Pzs", "Tipo", "Obs.");
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
//        setColumnWeight(3, 2);

//        setColumnComparator(0, DecreaseCaptureComparators.getIdComparator());
//        setColumnComparator(4, ReceiptSheetComparators.getDateTimeComparator());
//        setColumnComparator(17, ReceiptSheetComparators.getIdComparator());
    }

}