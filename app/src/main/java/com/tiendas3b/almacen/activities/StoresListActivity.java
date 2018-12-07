package com.tiendas3b.almacen.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.adapters.StoreRvAdapter;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.views.RecyclerViewFastScroller;

import java.util.List;

import butterknife.BindView;

@SuppressWarnings("ConstantConditions")
public class StoresListActivity extends RecyclerViewActivity {

    @BindView(R.id.fastscroller)
    RecyclerViewFastScroller fastScroller;
    protected List<Store> items;
    protected StoreRvAdapter adapter;
    protected BaseRecyclerViewAdapter.OnViewHolderClick listener;
//    private SearchView searchView;

    protected void init() {
        super.init();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public int getRequestCode() {
        return 0;//agregar a constantes para tener un orden blah blah
    }

    protected void getDatabaseInfo() {
        items = getStoresList();
        if (items.isEmpty()) {
            showEmptyView();
        } else {
            setAdapter();
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        viewSwitcher.setDisplayedChild(1);
    }

    protected List<Store> getStoresList() {
        return db.listStores(mContext.getRegion());
    }

    protected void setAdapter() {
        adapter = new StoreRvAdapter(mContext, listener);
        initRecyclerView();
        adapter.setList(items);
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerView() {
        recyclerView = viewSwitcher.findViewById(R.id.list);
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

}
