package com.tiendas3b.almacen.receipt.edit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tiendas3b.almacen.util.Constants;

/**
 * Created by dfa on 04/04/2016.
 */
public class ReceiptEditStatusPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_TABS = 5;
    private final int day;
    private final int type;

    public ReceiptEditStatusPagerAdapter(FragmentManager fm, int type, int day) {
        super(fm);
        this.type = type;
        this.day = day;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return ReceiptEditFragment.newInstance(position, type, day);
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case Constants.RECEIPT_PENDING:
                return "MUESTRA";
            case Constants.RECEIPT_PENDING_AUTOMATIC:
                return "MERMA";
            case Constants.RECEIPT_IN_PROCESS:
                return "FOLIO";
            case Constants.RECEIPT_TK_IMP:
                return "COMENTARIOS";
            case Constants.RECEIPT_HR_IMP:
                return "CANTIDADES";
            default:
            return "X";
        }
    }

}