package com.tiendas3b.almacen.adapters;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.tiendas3b.almacen.dto.GeneralTravelDTO;

import java.util.List;

public class TravelTableAdapter extends BaseTableAdapter<GeneralTravelDTO> {

    public TravelTableAdapter(Context context, List<GeneralTravelDTO> data, List<GeneralTravelDTO> allItems) {
        super(context, data, allItems);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        GeneralTravelDTO item = getRowData(rowIndex);

        try {
            switch (columnIndex) {
                case 0:
                    renderedView = renderString(item.getTruck(), Gravity.CENTER);
                    break;
                case 1:
                    renderedView = renderString(String.valueOf(item.getStatus()), Gravity.CENTER);
                    break;
                case 2:
                    renderedView = renderString(String.valueOf(item.getStoresNum()), Gravity.CENTER);
                    break;
                case 3:
                    renderedView = renderString(String.valueOf(item.getTravelsNum()), Gravity.CENTER);
                    break;
                case 4:
                    renderedView = renderString(String.valueOf(item.getScaffoldNum()), Gravity.CENTER);
                    break;
                case 5:
                    renderedView = renderString(String.valueOf(item.getDownloadsNum()), Gravity.CENTER);
                    break;
                case 6:
//                renderedView = renderString(String.valueOf(item.getScaffoldDownloadNum()), Gravity.CENTER);
//                break;
//            case 7:
                    renderedView = renderString(dfMoney.format(item.getTravelKm()), Gravity.CENTER);
                    break;
                case 7:
                    renderedView = renderString(dfMoney.format(item.getTotalKm()), Gravity.CENTER);
                    break;
                case 8:
                    renderedView = renderString(String.valueOf(item.getTravelTime()), Gravity.CENTER);
                    break;
                case 9:
                    renderedView = renderString(String.valueOf(item.getTotalTime()), Gravity.CENTER);
                    break;
                case 10:
                    renderedView = renderString(dfMoney.format(item.getToll()), Gravity.END);
                    break;
                case 11:
                    renderedView = renderString(dfMoney.format(item.getDiesel()), Gravity.END);
                    break;
                case 12:
                    renderedView = renderString(dfMoney.format(item.getTrafficTicket()), Gravity.END);
                    break;
                case 13:
                    renderedView = renderString(dfMoney.format(item.getOtherCost()), Gravity.END);
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
