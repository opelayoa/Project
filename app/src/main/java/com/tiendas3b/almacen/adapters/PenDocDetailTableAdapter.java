package com.tiendas3b.almacen.adapters;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.tiendas3b.almacen.db.dao.PendingDocumentDetail;

import java.util.List;

public class PenDocDetailTableAdapter extends BaseTableAdapter<PendingDocumentDetail> {

    public PenDocDetailTableAdapter(Context context, List<PendingDocumentDetail> data, List<PendingDocumentDetail> allItems) {
        super(context, data, allItems);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        PendingDocumentDetail item = getRowData(rowIndex);

        switch (columnIndex) {
            case 0:
                renderedView = renderString(item.getType(), Gravity.START);
                break;
            case 1:
                renderedView = renderString(String.valueOf(item.getEvents()), Gravity.CENTER);
                break;
            case 2:
                renderedView = renderString(String.valueOf(item.getStores()), Gravity.CENTER);
                break;
            case 3:
                renderedView = renderString(String.valueOf(item.getAmount()), Gravity.CENTER);
                break;
            default:
                break;
        }

        return renderedView;
    }
}
