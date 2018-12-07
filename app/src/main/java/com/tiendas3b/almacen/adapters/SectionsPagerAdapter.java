package com.tiendas3b.almacen.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tiendas3b.almacen.fragments.BuysFragment;
import com.tiendas3b.almacen.util.Constants;

import java.util.ArrayList;

/**
 * Created by dfa on 04/04/2016.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_TABS = 3;
//    private final SimpleDateFormat df;
//    private ArrayList<Date> tittles;
    private ArrayList<Integer> tittles;
    private String date;

    public SectionsPagerAdapter(FragmentManager fm, String date) {
        super(fm);
        createTittles();
//        df = new SimpleDateFormat(DateUtil.DD_MM, Locale.getDefault());
        this.date = date;
    }

    private void createTittles() {
        tittles = new ArrayList<>();
//        Calendar cal = Calendar.getInstance();
//        Date today = new Date();
//        cal.setTime(today);
//        cal.add(Calendar.DATE, 1);
//        Date tomorrow = cal.getTime();
//        cal.add(Calendar.DATE, -2);
//        Date yesterday = cal.getTime();
//        tittles.add(yesterday);
//        tittles.add(today);
//        tittles.add(tomorrow);
        tittles.add(Constants.NOT_ARRIVE);
        tittles.add(Constants.IN_PROCESS);
        tittles.add(Constants.ARRIVE);
    }

//    @Override
//    public int getItemPosition(Object object) {
////        if (object instanceof BuysFragment) {
////            return POSITION_UNCHANGED; // don't force a reload
////        } else {
////            // POSITION_NONE means something like: this fragment is no longer valid
////            // triggering the ViewPager to re-build the instance of this fragment.
//            return POSITION_NONE;
////        }
//    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return BuysFragment.newInstance(tittles.get(position), date);
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getTittle(tittles.get(position));
    }

    private CharSequence getTittle(Integer status) {
        switch (status){
            case Constants.ARRIVE:
                return "Procesado";
            case Constants.IN_PROCESS:
                return "En proceso";
            case Constants.NOT_ARRIVE:
                return "Pendiente";
            default:
                return "X";
        }
    }













}
