package com.tiendas3b.almacen.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.StoreRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.views.RecyclerViewFastScroller;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class OutputTransferenceActivity extends AppCompatActivity /*implements StoreFragment.OnListFragmentInteractionListener, TransferenceOutputFragment.OnFragmentInteractionListener*/ {

    private GlobalState mContext;
    private ViewSwitcher viewSwitcher;
    private RecyclerView recyclerView;
    private StoreRecyclerViewAdapter adapter;
    private List<Store> items;
    private IDatabaseManager databaseManager;
    private RecyclerViewFastScroller fastScroller;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transference);
        mContext = (GlobalState) getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        changeMainContent(new StoreFragment());
        init();
    }

    @Override
    protected void onRestart() {
        databaseManager = new DatabaseManager(this);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onStop();
    }

    @Override
    protected void onResume() {
        databaseManager = DatabaseManager.getInstance(this);
        super.onResume();
    }

    private void init() {
        viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher);
        databaseManager = new DatabaseManager(this);
        fastScroller = (RecyclerViewFastScroller) findViewById(R.id.fastscroller);
        if (items == null) {
            getDatabaseInfo();
//            if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
//                downloadStatus();
//            }
        }
    }

    private void getDatabaseInfo() {
        items = databaseManager.listStores(mContext.getRegion());
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
        adapter = new StoreRecyclerViewAdapter(mContext, items);
        initRecyclerView();
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) viewSwitcher.findViewById(R.id.list);
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
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        // Necessary to restore the BottomBar's state, otherwise we would
//        // lose the current tab on orientation change.
////        mBottomBar.onSaveInstanceState(outState);
//    }

//    private void changeMainContent(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
//    }

//    @Override
//    public void onListFragmentInteraction(Store store) {
//        TransferenceOutputFragment fragment = TransferenceOutputFragment.newInstance(store.getId());
//        changeMainContent(fragment);
//    }

//    @Override
//    public void onFragmentInteraction(VArticle articleTransference) {
//
//    }
}
