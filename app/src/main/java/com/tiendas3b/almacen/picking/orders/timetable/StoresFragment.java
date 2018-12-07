package com.tiendas3b.almacen.picking.orders.timetable;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.StorePickingStatus;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.views.RecyclerViewFastScroller;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by danflo on 08/05/2018 madafaka.
 */
public class StoresFragment extends Fragment {

    private static final String ARG_DAY = "ARG_DAY";
    private static final String ARG_TYPE = "ARG_TYPE";
    private static final String ARG_POSITION = "ARG_POSITION";
    private GlobalState mContext;
    private ViewSwitcher viewSwitcher;
    private OnFragmentInteractionListener mListener;
    private IDatabaseManager db;
    private List<StorePickingStatus> items;
    private StoreStatusRvAdapter adapter;

    private Unbinder unbinder;
    @BindView(R.id.fastscroller)
    RecyclerViewFastScroller fastScroller;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    private int mDay;
    private int mStatus;
    private int mType;

    public static Fragment newInstance(int position, int type, int day) {
        StoresFragment fragment = new StoresFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putInt(ARG_TYPE, type);
        args.putInt(ARG_DAY, day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_store_list, container, false);
        if (rootView instanceof ViewSwitcher) {
            viewSwitcher = ((ViewSwitcher) rootView);
//            Animation slide_in_left = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
//            Animation slide_out_right = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right);
//            viewSwitcher.setInAnimation(slide_in_left);
//            viewSwitcher.setOutAnimation(slide_out_right);
        }
        unbinder = ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    @SuppressWarnings("ConstantConditions")
    private void init() {
        Bundle args = getArguments();
        mDay = args.getInt(ARG_DAY);
        mStatus = args.getInt(ARG_POSITION);
        mType = args.getInt(ARG_TYPE);
        mContext = (GlobalState) Objects.requireNonNull(getActivity()).getApplicationContext();
        db = mListener.getDb();
        if (items == null) {
            getDatabaseInfo();
        }
    }

    private void getDatabaseInfo() {
        items = db.listStoresStatusPick(mContext.getRegion(), DateUtil.getTodayStr(), mType, mStatus);
        if (items.isEmpty()) {
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
            setAdapter();
            recyclerView.setVisibility(View.VISIBLE);
        }
            viewSwitcher.setDisplayedChild(1);
//        }
    }

    private void setAdapter() {
        adapter = new StoreStatusRvAdapter(mContext);
        adapter.setList(items);
        adapter.setClickListener(mListener.getAdapterListener(mStatus, adapter, mType));
        initRecyclerView();
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerView() {
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
        if (context instanceof StoresFragment.OnFragmentInteractionListener) {
            mListener = (StoresFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface OnFragmentInteractionListener {

        IDatabaseManager getDb();

        BaseRecyclerViewAdapter.OnViewHolderClick getAdapterListener(int mStatus, StoreStatusRvAdapter adapter, int type);
    }
}
