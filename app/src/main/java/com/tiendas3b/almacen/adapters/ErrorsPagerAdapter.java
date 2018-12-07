package com.tiendas3b.almacen.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tiendas3b.almacen.fragments.GeneralInfoErrorsFragment;
import com.tiendas3b.almacen.fragments.MeErrorsFragment;

/**
 * Created by dfa on 04/04/2016.
 */
public class ErrorsPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_TABS = 2;
//    private final SimpleDateFormat df;
//    private ArrayList<Date> tittles;

    public ErrorsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return GeneralInfoErrorsFragment.newInstance();
            case 1:
                return MeErrorsFragment.newInstance();
        }
        return new Fragment();
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "General";
            case 1:
                return "M. E.";
            default:
                return String.valueOf(position);
        }

    }
}
