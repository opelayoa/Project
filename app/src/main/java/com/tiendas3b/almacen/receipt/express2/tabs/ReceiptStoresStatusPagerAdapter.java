package com.tiendas3b.almacen.receipt.express2.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tiendas3b.almacen.util.Constants;

/**
 * Created by dfa on 04/04/2016.
 */
public class ReceiptStoresStatusPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_TABS = 5;
    private final int day;
    private final int type;

    public ReceiptStoresStatusPagerAdapter(FragmentManager fm, int type, int day) {
        super(fm);
        this.type = type;
        this.day = day;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return ReceiptStoresFragment.newInstance(position, type, day);
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case Constants.RECEIPT_PENDING:
                return "PEND ASN";
            case Constants.RECEIPT_PENDING_AUTOMATIC:
                return "PEND AUT";
            case Constants.RECEIPT_IN_PROCESS:
                return "PROC";
            case Constants.RECEIPT_TK_IMP:
                return "TK IMPR";
            case Constants.RECEIPT_HR_IMP:
                return "HR IMPR";
            default:
            return "X";
        }
    }

}