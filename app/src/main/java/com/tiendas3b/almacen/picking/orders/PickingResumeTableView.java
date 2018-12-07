package com.tiendas3b.almacen.picking.orders;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.OrderDetailCapture;
import com.tiendas3b.almacen.views.tables.CenterTableHeaderAdapter;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.TableDataRowColorizers;

/**
 * Created by dfa on 11/04/2016.
 */
public class PickingResumeTableView extends SortableTableView<OrderDetailCapture> {

    private static final String TAG = "PickingResumeTV";

    public PickingResumeTableView(Context context) {
        this(context, null);
    }

    public PickingResumeTableView(Context context, AttributeSet attributes) {
        this(context, attributes, 0);
    }

    public PickingResumeTableView(Context context, AttributeSet attributes, int styleAttributes) {
        super(context, attributes, styleAttributes);

        CenterTableHeaderAdapter simpleTableHeaderAdapter = new CenterTableHeaderAdapter(context,
                "Folio"
                , "Clave"
//                , "Loc."
                , "Descripción"
//                , "Emp."
                , "Pedido"
                , "Entrega"
                , "Estatus");
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


        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        int widthPx = dm.widthPixels;
        Log.e(TAG, "widthPx" + widthPx);

// will either be DENSITY_LOW, DENSITY_MEDIUM or DENSITY_HIGH
        int dpiClassification = dm.densityDpi;
        Log.e(TAG, "dpiClassification" + dpiClassification);

// these will return the actual dpi horizontally and vertically
        float xDpi = dm.xdpi;
        Log.e(TAG, "xDpi" + xDpi);
        float yDpi = dm.ydpi;
        Log.e(TAG, "yDpi" + yDpi);

        float widthDp = widthPx / (xDpi / 160);
        Log.e(TAG, "widthDp" + widthDp);
        if (widthDp > 535) {
//            TableColumnDpWidthModel columnModel = new TableColumnDpWidthModel(context, 6, 75);
//            columnModel.setColumnWidth(2, 150);
//            columnModel.setColumnWidth(4, 80);
//            columnModel.setColumnWidth(5, 80);
//            setColumnModel(columnModel);
            final TableColumnWeightModel columnModel = (TableColumnWeightModel) getColumnModel();
            columnModel.setColumnWeight(0, 1);
            columnModel.setColumnWeight(1, 1);
            columnModel.setColumnWeight(2, 3);
            columnModel.setColumnWeight(3, 1);
            columnModel.setColumnWeight(4, 1);
            columnModel.setColumnWeight(5, 1);
            setColumnModel(columnModel);
        } else {
            TableColumnDpWidthModel columnModel = new TableColumnDpWidthModel(context, 6, 75);
            columnModel.setColumnWidth(2, 150);
            columnModel.setColumnWidth(4, 80);
            columnModel.setColumnWidth(5, 80);
            setColumnModel(columnModel);
        }


//        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());
//        setColumnComparator(0, TravelDetailComparators.getActDescComparator());
    }

    float mLastMotionY;
    float mLastMotionX;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mLastMotionX != 0 && mLastMotionY != 0 && Math.abs(mLastMotionX - ev.getRawX()) > Math.abs(mLastMotionY - ev.getRawY())) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                mLastMotionX = ev.getRawX();
                mLastMotionY = ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

}