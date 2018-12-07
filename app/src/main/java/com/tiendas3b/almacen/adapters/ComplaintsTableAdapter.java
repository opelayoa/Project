package com.tiendas3b.almacen.adapters;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.tiendas3b.almacen.db.dao.Complaint;

import java.util.List;

public class ComplaintsTableAdapter extends BaseTableAdapter<Complaint> {

    public ComplaintsTableAdapter(Context context, List<Complaint> data, List<Complaint> allItems) {
        super(context, data, allItems);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        Complaint item = getRowData(rowIndex);

        switch (columnIndex) {
            case 0:
                renderedView = renderString(String.valueOf(item.getStoreId()), Gravity.END);
                break;
            case 1:
                renderedView = renderString(String.valueOf(item.getIclave()), Gravity.END);
                break;
            case 2:
                renderedView = renderString(dfThousnds.format(item.getQuantityOrdered()), Gravity.END);
                break;
            case 3:
                renderedView = renderString(dfThousnds.format(item.getQuantitySm()), Gravity.END);
                break;
            case 4:
                renderedView = renderString(dfThousnds.format(item.getQuantityAccepted()), Gravity.END);
                break;
            case 5:
                String obs = item.getObs();
                renderedView = renderString(obs == null ? "---" : obs.toUpperCase(), Gravity.START);
                break;
            case 6:
                renderedView = renderString(item.getAction(), Gravity.START);
                break;
            case 7:
                renderedView = renderString(item.getResponsibleArea(), Gravity.START);
                break;
            case 8:
                renderedView = renderString(item.getResponsibleSubarea(), Gravity.START);
                break;
            case 9:
                renderedView = renderString(item.getVarticle().getDescription(), Gravity.START);
                break;
            case 10:
                renderedView = renderString(dfMoney.format(item.getVarticle().getCost()), Gravity.END);
                break;
            case 11:
                renderedView = renderString(dfMoney.format(item.getVarticle().getSale()), Gravity.END);
                break;
            case 12:
                String storeObs = item.getStoreObs();
                renderedView = renderString(storeObs == null ? "---" : storeObs.toUpperCase(), Gravity.START);
                break;
            default:
                break;
        }

        return renderedView;
    }
}
