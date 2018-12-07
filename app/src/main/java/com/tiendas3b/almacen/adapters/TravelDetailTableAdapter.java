package com.tiendas3b.almacen.adapters;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.tiendas3b.almacen.dto.TravelDetailDTO;

import java.util.List;

public class TravelDetailTableAdapter extends BaseTableAdapter<TravelDetailDTO> {

    public TravelDetailTableAdapter(Context context, List<TravelDetailDTO> data, List<TravelDetailDTO> allItems) {
        super(context, data, allItems);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        TravelDetailDTO item = getRowData(rowIndex);

        switch (columnIndex) {
            case 0:
                renderedView = renderString(item.getActDesc(), Gravity.START);
                break;
            case 1:
                renderedView = renderString(String.valueOf(item.getScaffoldDownloadNum()), Gravity.CENTER);
                break;
            case 2:
                renderedView = renderString(String.valueOf(item.getDestination()), Gravity.CENTER);
                break;
            case 3:
                renderedView = renderString(String.valueOf(item.getInitialKm()), Gravity.CENTER);
                break;
            case 4:
                renderedView = renderString(String.valueOf(item.getFinalKm()), Gravity.CENTER);
                break;
            case 5:
                renderedView = renderString(String.valueOf(item.getDiffKm()), Gravity.CENTER);
                break;
            case 6:
                renderedView = renderString(String.valueOf(item.getInitialTime()), Gravity.CENTER);
                break;
            case 7:
                renderedView = renderString(String.valueOf(item.getFinalTime()), Gravity.CENTER);
                break;
            case 8:
                renderedView = renderString(String.valueOf(item.getDiffTime()), Gravity.CENTER);
                break;
            case 9:
                renderedView = renderString(dfMoney.format(item.getToll()), Gravity.END);
                break;
            case 10:
                renderedView = renderString(dfMoney.format(item.getDiesel()), Gravity.END);
                break;
            case 11:
                renderedView = renderString(dfMoney.format(item.getTrafficTicket()), Gravity.END);
                break;
            case 12:
                renderedView = renderString(dfMoney.format(item.getOtherCost()), Gravity.END);
                break;
            default:
                break;
        }

        return renderedView;
    }
}
