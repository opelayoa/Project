package com.tiendas3b.almacen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.tiendas3b.almacen.adapters.InputTransferenceRecyclerAdapter;
import com.tiendas3b.almacen.db.dao.InputTransference;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.views.GeneralSwipeRefreshLayout;
import com.tiendas3b.almacen.views.RecyclerViewFastScroller;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class InputTransferenceFragment extends Fragment {

    private static final String TAG = "InputTransfF";
    private static final String ARG_TYPE = "date";

    private short mType;
    private OnFragmentInteractionListener mListener;
    private GlobalState mContext;
    private ViewSwitcher viewSwitcher;
    private RecyclerView recyclerView;
    private InputTransferenceRecyclerAdapter adapter;
    private List<InputTransference> items;
    private boolean canDownload;
    private IDatabaseManager databaseManager;
    private GeneralSwipeRefreshLayout swipeRefresh;
    private RecyclerViewFastScroller fastScroller;

    public InputTransferenceFragment() {
    }

    public static Fragment newInstance(short type) {
        InputTransferenceFragment fragment = new InputTransferenceFragment();
        Bundle args = new Bundle();
        args.putShort(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getShort(ARG_TYPE, (short) -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (GlobalState) getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_input_transference, container, false);

        if (rootView instanceof ViewSwitcher) {
            viewSwitcher = ((ViewSwitcher) rootView);
            Animation slide_in_left = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            Animation slide_out_right = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right);
            viewSwitcher.setInAnimation(slide_in_left);
            viewSwitcher.setOutAnimation(slide_out_right);
        }

        databaseManager = new DatabaseManager(getActivity());

//        init();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        items = null;
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onDestroyView();
    }

    private void init() {
        fastScroller = viewSwitcher.findViewById(R.id.fastscroller);
        swipeRefresh = viewSwitcher.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(() -> download());
//        if (items == null) {
            getDatabaseInfo();
//            if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
//                downloadStatus();
//            }
//        }
    }

    private void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getInputTransference(mContext.getRegion(), mType).enqueue(new Callback<List<InputTransference>>() {
            @Override
            public void onResponse(Call<List<InputTransference>> call, Response<List<InputTransference>> response) {
                if (response.isSuccessful()) {
                    fillList(response.body());
                } else {
                    viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
                    viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
                }
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<InputTransference>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
                viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
//                Log.e(TAG, mContext.getString(R.string.error_odc));
            }
        });
    }

    private void getDatabaseInfo() {
        items = databaseManager.listInputTransference(mType, mContext.getRegion());
        if (items.isEmpty()) {
            swipeRefresh.setOnChildScrollUpListener(() -> false);
            showEmptyView();
        } else {
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
            setAdapter();
            recyclerView.setVisibility(View.VISIBLE);
        }
        viewSwitcher.setDisplayedChild(1);
    }

    private void showEmptyView() {
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
    }

    private void initRecyclerView() {
        recyclerView = viewSwitcher.findViewById(R.id.list);
        swipeRefresh.setOnChildScrollUpListener(() -> {
            return /*recyclerView.getFirstVisiblePosition() > 0 ||*/ //con layout manager ya que depende si es linear o grid
                    fastScroller.getScrollX() == 0 ||
                            recyclerView.getChildAt(0) == null || recyclerView.getChildAt(0).getTop() < 0;
        });

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

    private void fillList(List<InputTransference> list) {
        if (list == null) {
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
            viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
        } else {
            save(list);
            setAdapter();
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
            viewSwitcher.findViewById(R.id.list).setVisibility(View.VISIBLE);
        }
        viewSwitcher.setDisplayedChild(1);
    }

    private void setAdapter() {
        adapter = new InputTransferenceRecyclerAdapter(mContext, items, mListener);
        initRecyclerView();
        recyclerView.setAdapter(adapter);
    }

    @SuppressWarnings("unchecked")
    public void save(final List<InputTransference> list) {
        databaseManager.insertOrReplaceInTx(list.toArray(new InputTransference[list.size()]));
        items = databaseManager.listInputTransference(mType, mContext.getRegion());
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach");
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
        init();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(InputTransference transference);
    }
}
