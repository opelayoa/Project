package com.tiendas3b.almacen.adapters;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.tiendas3b.almacen.db.dao.ScannerCount;

import java.util.List;

public class ScannerCountTableAdapter extends BaseTableAdapter<ScannerCount> {

    public ScannerCountTableAdapter(Context context, List<ScannerCount> data, List<ScannerCount> allItems) {
        super(context, data, allItems);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        ScannerCount item = getRowData(rowIndex);

        switch (columnIndex) {
            case 0:
                renderedView = renderString(String.valueOf(item.getIclave()), Gravity.END);
                break;
            case 1:
                renderedView = renderString(dfThousnds.format(item.getFolio()), Gravity.END);
                break;
            case 2:
                renderedView = renderString(dfThousnds.format(item.getAmount()), Gravity.END);
                break;
            case 3:
                renderedView = renderString(item.getVarticle().getDescription(), Gravity.START);
                break;
            case 4:
                renderedView = renderString(dfMoney.format(item.getVarticle().getCost()), Gravity.END);
                break;
            case 5:
                renderedView = renderString(dfMoney.format(item.getVarticle().getSale()), Gravity.END);
                break;
            default:
                break;
        }

        return renderedView;
    }
}
