package com.tiendas3b.almacen.views.tables;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.ArticleResearch;
import com.tiendas3b.almacen.views.tables.comparators.ArticleResearchComparators;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

/**
 * Created by dfa on 11/04/2016.
 */
public class ArticlesResearchTableView extends SortableTableView<ArticleResearch> {

    public ArticlesResearchTableView(Context context) {
        this(context, null);
    }

    public ArticlesResearchTableView(Context context, AttributeSet attributes) {
        this(context, attributes, 0);
    }

    public ArticlesResearchTableView(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);

        CenterTableHeaderAdapter simpleTableHeaderAdapter = new CenterTableHeaderAdapter(context,
                "Ped", "For", "Sur", "SO", "Inv", "Can");
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
        setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd));
        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());

//        setColumnWeight(1, 2);

        setColumnComparator(0, ArticleResearchComparators.getAvgPedComparator());
        setColumnComparator(1, ArticleResearchComparators.getAvgFzdComparator());
        setColumnComparator(2, ArticleResearchComparators.getAvgSurtComparator());
        setColumnComparator(3, ArticleResearchComparators.getAvgSoComparator());
        setColumnComparator(4, ArticleResearchComparators.getAvgInvestComparator());
        setColumnComparator(5, ArticleResearchComparators.getAvgCancelComparator());
    }

}