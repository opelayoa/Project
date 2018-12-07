package com.tiendas3b.almacen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BuyRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.views.GeneralSwipeRefreshLayout;
import com.tiendas3b.almacen.views.RecyclerViewFastScroller;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuysFragment extends Fragment {

    private static final String ARG_DATE = "date";
    private static final String ARG_STATUS = "status";
    private static final String TAG = "BuysFragment";

    private OnFragmentInteractionListener mListener;
    private String mDate;
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private List<Buy> items;
    private ViewSwitcher viewSwitcher;
    private RecyclerView recyclerView;
    private BuyRecyclerViewAdapter adapter;
    //    private boolean canDownload;
    private GeneralSwipeRefreshLayout swipeRefresh;
    private RecyclerViewFastScroller fastScroller;
    private int status;


    public BuysFragment() {
    }

    public static Fragment newInstance(int status, String date) {
        BuysFragment fragment = new BuysFragment();
        Bundle args = new Bundle();
//        SimpleDateFormat df = new SimpleDateFormat(DateUtil.YYYY_MM_DD, Locale.getDefault());
        args.putInt(ARG_STATUS, status);
        args.putString(ARG_DATE, date);
//        args.putString(ARG_DATE, df.format(date));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDate = getArguments().getString(ARG_DATE, "");
            status = getArguments().getInt(ARG_STATUS, -1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (GlobalState) getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_buys_list, container, false);

        if (rootView instanceof ViewSwitcher) {
            viewSwitcher = ((ViewSwitcher) rootView);
            Animation slide_in_left = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            Animation slide_out_right = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right);
            viewSwitcher.setInAnimation(slide_in_left);
            viewSwitcher.setOutAnimation(slide_out_right);
        }

        databaseManager = new DatabaseManager(mContext);

        init(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDatabaseInfo();
    }

    @Override
    public void onDestroyView() {
        items = null;
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onDestroyView();
    }

    private void init(View rootView) {
        fastScroller = rootView.findViewById(R.id.fastscroller);
        swipeRefresh = rootView.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadUpdate();
            }
        });
//        if (items == null) {
//            getDatabaseInfo();
////            if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
////                downloadStatus();
////            }
//        }
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
//                Log.e(TAG, mContext.getString(R.string.error_odc));
//                swipeRefresh.setRefreshing(false);
//                viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//                viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
//                viewSwitcher.setDisplayedChild(1);
//            }
//        });
//    }

    private void getDatabaseInfo() {
//        items = Collections.emptyList();
        items = databaseManager.listBuys(mDate, mContext.getRegion(), status);
//        View emptyView = viewSwitcher.findViewById(R.id.emptyView);
        recyclerView = viewSwitcher.findViewById(R.id.list);
        swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
            @Override
            public boolean canChildScrollUp() {
//                    return false;
                View child = recyclerView.getChildAt(0);
                return child != null && child.canScrollVertically(-1);
            }
        });

        if (items.isEmpty()) {
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
            viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
        } else {
            setAdapter();
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
            viewSwitcher.findViewById(R.id.list).setVisibility(View.VISIBLE);
        }
        viewSwitcher.setDisplayedChild(1);


//        if (items.isEmpty()) {
////            recyclerView = (RecyclerView) viewSwitcher.findViewById(R.id.listAll);
////            recyclerView.setVisibility(View.GONE);
////            emptyView.setVisibility(View.VISIBLE);
//        } else {
//            downloadUpdate();
////            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
////            setAdapter();
//////            adapter = new BuyRecyclerViewAdapter(mContext, items, mListener, timetableReceipt);
//////            initRecyclerView();
//////            recyclerView.setAdapter(adapter);
////            recyclerView.setVisibility(View.VISIBLE);
////            viewSwitcher.setDisplayedChild(1);
//        }
////        }
    }

    private void downloadUpdate() {
        Log.d(TAG, "downloadUpdate");
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTimeGsonTime();
        httpService.getBuys(mContext.getRegion(), mDate).enqueue(new Callback<List<Buy>>() {
            @Override
            public void onResponse(Call<List<Buy>> call, Response<List<Buy>> response) {
                if (response.isSuccessful()) {
                    fillListUpdate(response.body());
                } else {
                    viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
                    viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
                }
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Buy>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_odc));
                swipeRefresh.setRefreshing(false);
                viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
                viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
                viewSwitcher.setDisplayedChild(1);
            }
        });
    }

    private void initRecyclerView() {
//        recyclerView = (RecyclerView) viewSwitcher.findViewById(R.id.list);
//        swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
//            @Override
//            public boolean canChildScrollUp() {
//                return /*recyclerView.getFirstVisiblePosition() > 0 ||*/ //con layout manager ya que depende si es linear o grid
//                        fastScroller.getScrollX() == 0 ||
//                        recyclerView.getChildAt(0) == null || recyclerView.getChildAt(0).canScrollVertically(-1);//   getTop() < 0;
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

//    private void fillList(List<Buy> list) {
//        if (list == null) {
//            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//            viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
//        } else {
////            items = ticketsList;
////            adapter = new BuyRecyclerViewAdapter(mContext, new ArrayList<>(items), mListener);
////            recyclerView.setAdapter(adapter);
//            save(list);
//            setAdapter();
//            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
//            viewSwitcher.findViewById(R.id.list).setVisibility(View.VISIBLE);
//        }
//        viewSwitcher.setDisplayedChild(1);
//    }

    private void fillListUpdate(List<Buy> list) {
        if (list == null) {
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
            viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
        } else {
//            items = ticketsList;
//            adapter = new BuyRecyclerViewAdapter(mContext, new ArrayList<>(items), mListener);
//            recyclerView.setAdapter(adapter);
            update(list);
            setAdapter();
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
            viewSwitcher.findViewById(R.id.list).setVisibility(View.VISIBLE);
        }
        viewSwitcher.setDisplayedChild(1);

    }

    private void update(List<Buy> list) {
        databaseManager.insertOrUpdateBuys(list);
        items = databaseManager.listBuys(mDate, mContext.getRegion(), status);
    }

    private void setAdapter() {
//        Calendar c = Calendar.getInstance();
//        c.setTime(DateUtil.getDate(mDate));
//        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK)-1;
//        timetableReceipt = db.listTimetable(dayOfWeek);
        adapter = new BuyRecyclerViewAdapter(mContext, items, mListener);
        initRecyclerView();
        recyclerView.setAdapter(adapter);
    }

//    @SuppressWarnings("unchecked")
//    public void save(final List<Buy> list) {
////        new AsyncTask<Void, Void, Void>() {
////            @Override
////            protected Void doInBackground(Void... params) {
//        databaseManager.insertOrReplaceInTx(list.toArray(new Buy[list.size()]));
//        items = databaseManager.listBuys(mDate, mContext.getRegion(), status);
////                return null;
////            }
////
////            @Override
////            protected void onPostExecute(Void aVoid) {
////        adapter = new BuyRecyclerViewAdapter(mContext, items, mListener, timetableReceipt);
////        initRecyclerView();
////        recyclerView.setAdapter(adapter);
////            }
////
////        }.execute();
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Buy buy);
    }
}
