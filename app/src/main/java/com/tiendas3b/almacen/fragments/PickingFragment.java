package com.tiendas3b.almacen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.ErrorsActivity;
import com.tiendas3b.almacen.activities.SmSupplyActivity;
import com.tiendas3b.almacen.db.dao.OrderType;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.picking.epackage.CageInActivity;
import com.tiendas3b.almacen.picking.epackage.EpackageStoresActivity;
import com.tiendas3b.almacen.picking.orders.timetable.TimetableStatusActivity;

import java.util.List;

/**
 * Created by dfa on 19/07/2016.
 */
public class PickingFragment extends Fragment {

    private GlobalState mContext;
    private IDatabaseManager db;
    private View rootView;

    public PickingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (GlobalState) getActivity().getApplicationContext();
        getActivity().setTitle(R.string.menu_drw_picking);
        rootView = inflater.inflate(R.layout.fragment_picking, container, false);
        init(rootView);
        return rootView;
    }

    private void init(final View view) {
        db = new DatabaseManager(mContext);

        final View btnOrders = view.findViewById(R.id.btnReceiptOpt1);
        btnOrders.setOnClickListener(v -> showPopupMenuOrder(btnOrders));

        view.findViewById(R.id.btnReceiptOpt2).setOnClickListener(v -> startActivity(new Intent(mContext, SmSupplyActivity.class)));

        view.findViewById(R.id.btnReceiptOpt3).setOnClickListener(v -> startActivity(new Intent(mContext, ErrorsActivity.class)));

        final Button ib = view.findViewById(R.id.btnEpackage);
        ib.setOnClickListener(v -> showPopupMenu(ib));
    }

    private void showPopupMenuOrder(View btn) {
        db = DatabaseManager.getInstance(mContext);
        final List<OrderType> orderTypes = db.findOrdersAvailable(mContext.getRegion());
        if(orderTypes == null || orderTypes.isEmpty()){
            Snackbar.make(rootView, "No hay circuitos disponibles. Sincroniza catálogos.", Snackbar.LENGTH_LONG).show();
        } else {
            PopupMenu popup = new PopupMenu(mContext, btn);
            Menu menu = popup.getMenu();
            int i = 0;
            for (OrderType t : orderTypes) {
                menu.add(0, i++, 0, "Circuito " + t.getDescription());
            }
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.popup_options, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                int pos = item.getItemId();
                int id = orderTypes.get(pos).getTypeId();
                startActivity(new Intent(mContext, TimetableStatusActivity.class).putExtra(TimetableStatusActivity.EXTRA_ORDER_TYPE, id));
//                startActivity(new Intent(mContext, TimetablePickingActivity.class).putExtra(TimetablePickingActivity.EXTRA_ORDER_TYPE, id));
                return true;
            });
            popup.show();
        }
    }

    public void showPopupMenu(Button btn) {
        PopupMenu popup = new PopupMenu(mContext, btn);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_options, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.action1:
                    startActivity(new Intent(mContext, CageInActivity.class));
                    break;
                case R.id.action2:
                    startActivity(new Intent(mContext, EpackageStoresActivity.class));
                    break;
            }
            return true;
        });
        popup.show();
    }
}
