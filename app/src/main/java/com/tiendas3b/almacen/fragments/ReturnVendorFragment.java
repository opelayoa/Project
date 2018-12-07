package com.tiendas3b.almacen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.VendorRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.views.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReturnVendorFragment extends Fragment {


    private GlobalState mContext;
    private ViewSwitcher viewSwitcher;
    private RecyclerView recyclerView;
    private VendorRecyclerViewAdapter adapter;
    private List<Provider> items;
    private IDatabaseManager databaseManager;
    private RecyclerViewFastScroller fastScroller;
    private OnListFragmentInteractionListener mListener;
    private SearchView searchView;

    public ReturnVendorFragment() {
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_vendors, menu);
//
//        final MenuItem item = menu.findItem(R.id.action_search);
//        searchView = (SearchView) MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                if(items != null){
//                    final List<Provider> filteredModelList = filter(query);
//                    adapter.animateTo(filteredModelList);
//                    recyclerView.scrollToPosition(0);
//                    Toast t = Toast.makeText(mContext, "Coincidencias: " + filteredModelList.size(), Toast.LENGTH_SHORT);
//                    t.setGravity(Gravity.TOP, 0, 0);
//                    t.show();
//                }
//                return true;
//            }
//        });
//        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                // Do something when collapsed
//                return true;       // Return true to collapse action view
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
////                adapter.setLoaded();
//                return true;      // Return true to expand action view
//            }
//        });
//    }
//
//    private List<Provider> filter(String query) {
//        query = query.toLowerCase();
//        final List<Provider> filteredModelList = new ArrayList<>();
//        query = query.trim().toLowerCase();
//        for (Provider model : items) {
//            final String textId = String.valueOf(model.getId());
//            final String text = model.getName().toLowerCase();
//            final String text2 = model.getBusinessName().toLowerCase();
//            if (text.contains(query) || textId.contains(query) || text2.contains(query)) {
//                filteredModelList.add(model);
//            }
//        }
//        return filteredModelList;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (id) {
//            case R.id.action_decrease_capture_save:
////                saveData();
//                return true;
//            case R.id.action_decrease_scan:
////                findByBarcode();
//                return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (GlobalState) getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_return_vendor_list, container, false);

        if (rootView instanceof ViewSwitcher) {
            viewSwitcher = ((ViewSwitcher) rootView);
            Animation slide_in_left = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            Animation slide_out_right = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right);
            viewSwitcher.setInAnimation(slide_in_left);
            viewSwitcher.setOutAnimation(slide_out_right);
        }

        databaseManager = new DatabaseManager(getActivity());

        init(rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        items = null;
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onDestroyView();
    }

    private void init(View rootView) {
        fastScroller = (RecyclerViewFastScroller) rootView.findViewById(R.id.fastscroller);
//        swipeRefresh = (GeneralSwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                downloadStatus();
//            }
//        });
        if (items == null) {
            getDatabaseInfo();
//            if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
//                downloadStatus();
//            }
        }
    }

//    private void downloadStatus() {
//        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
//        httpService.getBuys(mContext.getRegion(), mDate).enqueue(new Callback<List<Buy>>() {
//            @Override
//            public void onResponse(Call<List<Buy>> call, Response<List<Buy>> response) {
//                if (response.isSuccessful()) {
//                    fillList(response.body());
//                } else {
//                    viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//                    viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
//                }
//                swipeRefresh.setRefreshing(false);
//            }
//
//            @Override
//            public void onFailure(Call<List<Buy>> call, Throwable t) {
//                swipeRefresh.setRefreshing(false);
//                viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//                viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
//                Log.e(TAG, mContext.getString(R.string.error_odc));
//            }
//        });
//    }

    private void getDatabaseInfo() {
        items = databaseManager.listSupplyVendors();
        if (items.isEmpty()) {
//            swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
//                @Override
//                public boolean canChildScrollUp() {
//                    return false;
//                }
//            });
        } else {
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
            setAdapter();
            recyclerView.setVisibility(View.VISIBLE);
            viewSwitcher.setDisplayedChild(1);
        }
//        }
    }

    private void setAdapter() {
//        Calendar c = Calendar.getInstance();
//        c.setTime(DateUtil.getDate(mDate));
//        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;
//        timetableReceipt = db.listTimetable(dayOfWeek);
        adapter = new VendorRecyclerViewAdapter(mContext, new ArrayList<>(items)/*, mListener*/);
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

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(Provider store);

//        agregar los 3 botones de las tiendas
    }
}
