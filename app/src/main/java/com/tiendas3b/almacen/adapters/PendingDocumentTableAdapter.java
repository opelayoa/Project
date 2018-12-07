package com.tiendas3b.almacen.adapters;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.tiendas3b.almacen.db.dao.PendingDocument;

import java.util.List;

public class PendingDocumentTableAdapter extends BaseTableAdapter<PendingDocument> {

    public PendingDocumentTableAdapter(Context context, List<PendingDocument> data, List<PendingDocument> allItems) {
        super(context, data, allItems);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        PendingDocument item = getRowData(rowIndex);

        try {
            switch (columnIndex) {
                case 0:
                    renderedView = renderString(item.getDate(), Gravity.CENTER);
                    break;
                case 1:
                    renderedView = renderString(String.valueOf(item.getTa()), Gravity.CENTER);
                    break;
                case 2:
                    renderedView = renderString(String.valueOf(item.getTaPieces()), Gravity.CENTER);
                    break;
                case 3:
                    renderedView = renderString(String.valueOf(item.getRt()), Gravity.CENTER);
                    break;
                case 4:
                    renderedView = renderString(String.valueOf(item.getRtPieces()), Gravity.CENTER);
                    break;
                case 5:
                    renderedView = renderString(String.valueOf(item.getTaStore()), Gravity.CENTER);
                    break;
                case 6:
                    renderedView = renderString(String.valueOf(item.getRtStore()), Gravity.CENTER);
                    break;
                default:
                    break;
            }
        } catch (Exception e){
            renderedView = renderString("null", Gravity.CENTER);
            e.printStackTrace();
        }

        return renderedView;
    }
}
