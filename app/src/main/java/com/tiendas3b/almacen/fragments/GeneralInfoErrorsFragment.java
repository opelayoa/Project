package com.tiendas3b.almacen.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.GeneralErrorsTableAdapter;
import com.tiendas3b.almacen.dto.GeneralErrorDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.NetworkUtil;
import com.tiendas3b.almacen.views.tables.GeneralErrorsTableView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dfa on 22/07/2016.
 */
public class GeneralInfoErrorsFragment extends Fragment {

    private static final String TAG = "GnrlInfErrFragment";
    private GlobalState mContext;
    private ViewSwitcher viewSwitcher;
    //    private RecyclerView recyclerView;
//    private BuyRecyclerViewAdapter adapter;
    private List<GeneralErrorDTO> items;

    public static Fragment newInstance() {
        GeneralInfoErrorsFragment fragment = new GeneralInfoErrorsFragment();
//        Bundle args = new Bundle();
//        SimpleDateFormat df = new SimpleDateFormat(DateUtil.YYYY_MM_DD, Locale.getDefault());
//        args.putString(ARG_DATE, df.format(date));
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mDate = getArguments().getString(ARG_DATE, "");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (GlobalState) getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_general_errors, container, false);

        if (rootView instanceof ViewSwitcher) {
            viewSwitcher = ((ViewSwitcher) rootView);
            Animation slide_in_left = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            Animation slide_out_right = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right);
            viewSwitcher.setInAnimation(slide_in_left);
            viewSwitcher.setOutAnimation(slide_out_right);
        }

        init(rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        items = null;
//        if (db != null) db.closeDbConnections();
        super.onDestroyView();
    }

    private void init(View rootView) {
//        fastScroller = (RecyclerViewFastScroller) rootView.findViewById(R.id.fastscroller);
//        swipeRefresh = (GeneralSwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                downloadStatus();
//            }
//        });
//        if (items == null) {
//            getDatabaseInfo();
        if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
            download();
        }
//        }
    }

    private void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getGeneralErrors(mContext.getRegion()).enqueue(new Callback<List<GeneralErrorDTO>>() {
            @Override
            public void onResponse(Call<List<GeneralErrorDTO>> call, Response<List<GeneralErrorDTO>> response) {
                if (response.isSuccessful()) {
                    fillList(response.body());
                } else {
                    viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
                    viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
                }
//                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<GeneralErrorDTO>> call, Throwable t) {
//                swipeRefresh.setRefreshing(false);
                viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
                viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
                Log.e(TAG, mContext.getString(R.string.error_odc));
            }
        });
    }

    private void fillList(List<GeneralErrorDTO> list) {
        if (list == null) {
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
            viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
        } else {
//            items = ticketsList;
//            adapter = new BuyRecyclerViewAdapter(mContext, new ArrayList<>(items), mListener);
//            recyclerView.setAdapter(adapter);
//            save(list);
            items = list;
            setAdapter();
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
            viewSwitcher.findViewById(R.id.list).setVisibility(View.VISIBLE);
        }
        viewSwitcher.setDisplayedChild(1);
    }

    protected void setAdapter() {
        GeneralErrorsTableView tableView = (GeneralErrorsTableView) viewSwitcher.findViewById(R.id.list);
        tableView.setSaveEnabled(false);
        tableView.setDataAdapter(new GeneralErrorsTableAdapter(mContext, new ArrayList<>(items), items));
//        tableView.addDataClickListener(new ClickListener());
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.VISIBLE);
    }
}
