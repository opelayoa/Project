package com.tiendas3b.almacen.picking.orders;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.tiendas3b.almacen.adapters.BaseTableAdapter;
import com.tiendas3b.almacen.db.dao.OrderDetailCapture;

import java.util.List;

public class OrderDetailTableAdapter extends BaseTableAdapter<OrderDetailCapture> {

    public OrderDetailTableAdapter(Context context, List<OrderDetailCapture> data, List<OrderDetailCapture> allItems) {
        super(context, data, allItems);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        OrderDetailCapture item = getRowData(rowIndex);

        switch (columnIndex) {
            case 0:
                renderedView = renderString(String.valueOf(item.getOrderCapture().getPaybill()), Gravity.CENTER);
                break;
            case 1:
                renderedView = renderString(String.valueOf(item.getIclave()), Gravity.CENTER);
                break;
//            case 2:
//                renderedView = renderString(item.getArticle().getCircuit(), Gravity.CENTER);
//                break;
            case 2:
                renderedView = renderString(item.getArticle().getDescription().concat("/").concat(item.getArticle().getUnity()), Gravity.START);
//                parentView.getLayoutParams().height = renderedView.getHeight();
                break;
//            case 4:
//                renderedView = renderString(String.valueOf(item.getPacking()), Gravity.CENTER);
//                break;
            case 3:
                renderedView = renderString(String.valueOf(item.getOrder()), Gravity.CENTER);
                break;
            case 4:
                renderedView = renderString(String.valueOf(item.getQuantity() / item.getPacking()), Gravity.CENTER);
                break;
            case 5:
                renderedView = renderString(item.getStatus(), Gravity.CENTER);
                break;
            default:
                break;
        }

        return renderedView;
    }
}
