package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.adapters.ReturnVendorRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.views.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@SuppressWarnings("ConstantConditions")
public class ReturnVendorActivityBase extends RecyclerViewActivity {

    private ReturnVendorRecyclerViewAdapter adapter;
    private List<Provider> items;
    @BindView(R.id.fastscroller)
    RecyclerViewFastScroller fastScroller;
    private SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vendors, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(items != null){
                    final List<Provider> filteredModelList = filter(query);
                    adapter.animateTo(filteredModelList);
                    recyclerView.scrollToPosition(0);
                    Toast t = Toast.makeText(mContext, "Coincidencias: " + filteredModelList.size(), Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.TOP, 0, 0);
                    t.show();
                }
                return true;
            }
        });
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when collapsed
                return true;       // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
//                adapter.setLoaded();
                return true;      // Return true to expand action view
            }
        });
        return true;
    }

    private List<Provider> filter(String query) {
        query = query.toLowerCase();
        final List<Provider> filteredModelList = new ArrayList<>();
        query = query.trim().toLowerCase();
        for (Provider model : items) {
            final String textId = String.valueOf(model.getId());
            final String text = model.getName().toLowerCase();
            final String text2 = model.getBusinessName().toLowerCase();
            if (text.contains(query) || textId.contains(query) || text2.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_vendor);
        mContext = (GlobalState) getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

//    protected void init() {
//        super.init();
//    }

    @Override
    public int getRequestCode() {
        return 0;
    }

    protected void getDatabaseInfo() {
        items = db.listSupplyActiveVendors();//TODO hacer impl
        if (items.isEmpty()) {
            showEmptyView();
        } else {
            setAdapter();
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        viewSwitcher.setDisplayedChild(1);
    }

    protected void setAdapter() {
        adapter = new ReturnVendorRecyclerViewAdapter(mContext, new BaseRecyclerViewAdapter.OnViewHolderClick() {
            @Override
            public void onClick(View view, int position) {
                Intent intentTicketDetailEdit = new Intent(mContext , PhysicalChangeCaptureActivity.class).putExtra("p", items.get(position));
                startActivity(intentTicketDetailEdit);
//                mContext.startActivity(new Intent(mContext, ReturnVendorCaptureActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("p", items.get(position)));
            }
        });
        adapter.setList(new ArrayList<>(items));
        initRecyclerView();
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) viewSwitcher.findViewById(R.id.list);
//        swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
//            @Override
//            public boolean canChildScrollUp() {
//                return /*recyclerView.getFirstVisiblePosition() > 0 ||*/ //con layout manager ya que depende si es linear o grid
//                        fastScroller.getScrollX() == 0 ||
//                                recyclerView.getChildAt(0) == null || recyclerView.getChildAt(0).getTop() < 0;
//            }
//        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                //TODO if the items are filtered, considered hiding the fast scroller here
                final int firstVisibleItemPosition = findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != 0) {
                    // this avoids trying to handle un-needed calls
                    if (firstVisibleItemPosition == -1)
                        //not initialized, or no items shown, so hide fast-scroller
                        fastScroller.setVisibility(View.GONE);
                    return;
                }
                final int lastVisibleItemPosition = findLastVisibleItemPosition();
                int itemsShown = lastVisibleItemPosition - firstVisibleItemPosition + 1;
                //if all items are shown, hide the fast-scroller
                fastScroller.setVisibility(adapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
            }
        });
        fastScroller.removeAllViews();
        fastScroller.setRecyclerView(recyclerView);
        fastScroller.setViewsToUse(R.layout.recycler_view_fast_scroller__fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);
    }

//    @Override
//    public void onListFragmentInteraction(Provider provider) {
//        startActivity(new Intent(mContext, ReturnVendorCaptureActivity.class).putExtra("p", (Serializable) provider));
//    }

//    public interface OnListFragmentInteractionListener {
//
//        void onListFragmentInteraction(Provider store);
//
//    }

//    private void replaceFragment (Fragment fragment){
//        String backStateName =  fragment.getClass().getName();
//        String fragmentTag = backStateName;
//
//        FragmentManager manager = getSupportFragmentManager();
//        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);
//
//        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(R.id.content, fragment, fragmentTag);
////            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            ft.addToBackStack(backStateName);
//            ft.commit();
//        }
//    }

//    private void changeMainContent(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
//    }

//    @Override
//    public void onListFragmentInteraction(Provider vendor) {
//        ReturnVendorCaptureFragment fragment = ReturnVendorCaptureFragment.newInstance(vendor.getId());
//        replaceFragment(fragment);
//    }
//
//    @Override
//    public void onBackPressed() {
//        back();
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                back();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private void back() {
//        FragmentManager fm = getSupportFragmentManager();
//        if (fm.getBackStackEntryCount() > 1) {
//            fm.popBackStack();
//        } else {
//            fm.popBackStack();
//            super.onBackPressed();
//        }
//    }

}
