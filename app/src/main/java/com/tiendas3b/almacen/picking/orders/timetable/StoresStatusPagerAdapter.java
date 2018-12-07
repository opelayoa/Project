package com.tiendas3b.almacen.picking.orders.timetable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tiendas3b.almacen.util.Constants;

/**
 * Created by dfa on 04/04/2016.
 */
public class StoresStatusPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_TABS = 3;
    private final int day;
    private final int type;

    public StoresStatusPagerAdapter(FragmentManager fm, int type, int day) {
        super(fm);
        this.type = type;
        this.day = day;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return StoresFragment.newInstance(position, type, day);
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case Constants.PICKING_PENDING:
                return "Pendiente";
            case Constants.PICKING_COMPLETE:
                return "Procesado";
            case Constants.PICKING_IN_PROCESS:
                return "En proceso";
            default:
                return "X";
        }
    }

}