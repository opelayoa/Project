package com.tiendas3b.almacen.adapters;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.tiendas3b.almacen.dto.GeneralErrorDTO;

import java.util.List;

public class GeneralErrorsTableAdapter extends BaseTableAdapter<GeneralErrorDTO> {

    public GeneralErrorsTableAdapter(Context context, List<GeneralErrorDTO> data, List<GeneralErrorDTO> allItems) {
        super(context, data, allItems);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        GeneralErrorDTO item = getRowData(rowIndex);

        switch (columnIndex) {
            case 0:
                renderedView = renderString(item.getDate(), Gravity.CENTER);
                break;
            case 1:
                renderedView = renderString(String.valueOf(item.getStoresPick()), Gravity.CENTER);
                break;
            case 2:
                renderedView = renderString(String.valueOf(item.getScaffoldPick()), Gravity.CENTER);
                break;
            case 3:
                renderedView = renderString(String.valueOf(item.getTeamMembers()), Gravity.CENTER);
                break;
            case 4:
                renderedView = renderString(String.valueOf(item.getErrorsPick()), Gravity.CENTER);
                break;
            default:
                break;
        }

        return renderedView;
    }
}
