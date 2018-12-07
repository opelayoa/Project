package com.tiendas3b.almacen.adapters;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.tiendas3b.almacen.dto.SmDetailDTO;

import java.util.List;

public class SmSupplyTableAdapter extends BaseTableAdapter<SmDetailDTO> {

    public SmSupplyTableAdapter(Context context, List<SmDetailDTO> data, List<SmDetailDTO> allItems) {
        super(context, data, allItems);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        SmDetailDTO item = getRowData(rowIndex);

        switch (columnIndex) {
            case 0:
                renderedView = renderString(item.getStoreName(), Gravity.CENTER);
                break;
            case 1:
                renderedView = renderString(String.valueOf(item.getIclave()), Gravity.CENTER);
                break;
            case 2:
                renderedView = renderString(dfThousnds.format(item.getAmount()), Gravity.END);
                break;
            case 3:
                renderedView = renderString(dfThousnds.format(item.getCost()), Gravity.END);
                break;
            case 4:
                renderedView = renderString(dfThousnds.format(item.getSale()), Gravity.END);
                break;
            case 5:
                renderedView = renderString(item.getBarcode(), Gravity.CENTER);
                break;
            case 6:
                String obs = item.getDescription();
                renderedView = renderString(obs == null ? "---" : obs.concat(" " + item.getUnity()), Gravity.START);
                break;
            case 7:
                renderedView = renderString(item.getVendorName(), Gravity.START);
                break;
            case 8:
                renderedView = renderString(item.getGroup(), Gravity.START);
                break;
            case 9:
                renderedView = renderString(item.getLine(), Gravity.START);
                break;
            case 10:
                renderedView = renderString(item.getType(), Gravity.START);
                break;
            case 11:
                renderedView = renderString(item.getStatus(), Gravity.START);
                break;
            default:
                break;
        }

        return renderedView;
    }
}
