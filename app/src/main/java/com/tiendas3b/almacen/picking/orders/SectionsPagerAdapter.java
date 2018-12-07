package com.tiendas3b.almacen.picking.orders;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danflo on 01/03/2017 madafaka.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private List<ArticleToPickingFragment> fragments = new ArrayList<>();

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public ArticleToPickingFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(ArticleToPickingFragment fragment) {
        fragments.add(fragment);
    }
}
