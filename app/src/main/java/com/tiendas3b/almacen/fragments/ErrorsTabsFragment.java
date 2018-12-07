package com.tiendas3b.almacen.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.ErrorsPagerAdapter;


public class ErrorsTabsFragment extends Fragment {

    public ErrorsTabsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buys_tabs, container, false);
        init(rootView);
        getActivity().setTitle(R.string.title_activity_errors);
        return rootView;
    }

    private void init(View rootView) {
        ErrorsPagerAdapter mSectionsPagerAdapter = new ErrorsPagerAdapter(getChildFragmentManager());
        ViewPager mViewPager = rootView.findViewById(R.id.pgr_tab);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        SmartTabLayout tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setViewPager(mViewPager);
        tabLayout.setVisibility(View.VISIBLE);
//        mViewPager.setCurrentItem(1);
    }
}
