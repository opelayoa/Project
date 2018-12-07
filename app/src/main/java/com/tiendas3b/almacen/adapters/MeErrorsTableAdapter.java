package com.tiendas3b.almacen.adapters;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.tiendas3b.almacen.dto.MeErrorDTO;

import java.util.List;

public class MeErrorsTableAdapter extends BaseTableAdapter<MeErrorDTO> {

    public MeErrorsTableAdapter(Context context, List<MeErrorDTO> data, List<MeErrorDTO> allItems) {
        super(context, data, allItems);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        MeErrorDTO item = getRowData(rowIndex);

        switch (columnIndex) {
            case 0:
                renderedView = renderString(item.getMemberId(), Gravity.CENTER);
                break;
            case 1:
                renderedView = renderString(String.valueOf(item.getNumErrors()), Gravity.CENTER);
                break;
            default:
                break;
        }

        return renderedView;
    }
}
