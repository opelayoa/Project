package com.tiendas3b.almacen.picking.orders.timetable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.tiendas3b.almacen.R;


public class TimetableTabsFragment extends Fragment {

    public static final String TYPE = "TYPE";
    public static final String DAY = "DAY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buys_tabs, container, false);//fragment_buys_tabs is generic
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        Bundle args = getArguments();
        StoresStatusPagerAdapter mSectionsPagerAdapter = new StoresStatusPagerAdapter(getChildFragmentManager(), args.getInt(TYPE), args.getInt(DAY));
        ViewPager mViewPager = rootView.findViewById(R.id.pgr_tab);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        SmartTabLayout tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setViewPager(mViewPager);
        tabLayout.setVisibility(View.VISIBLE);
//        mViewPager.setCurrentItem(1);
    }
}
