package com.tiendas3b.almacen.fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.SectionsPagerAdapter;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.DialogUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuysTabsFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "BuysTabsFragment";
    private String mDate;
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private View rootView;

    protected static final String STATE_DATE = "date";
    protected Calendar calendar;
    protected Date selectedDate;
    protected MenuItem btnCalendar;
    private ProgressBar progressBar;
    private ViewPager mViewPager;
    private SmartTabLayout tabLayout;
    private boolean onResume;

    public BuysTabsFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        onResume = true;
    }

    @Override
    public void onPause() {
        onResume = false;
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        this.menu = menu;
        inflater.inflate(R.menu.menu_calendar, menu);
        btnCalendar = menu.findItem(R.id.action_calendar);
    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        if (item.getChecked() == ARRIVE) {
//            menu.clear();
//        }
////        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
////        toolbar.getMenu().clear();
//////                ((AppCompatActivity)getActivity()).getSupportActionBar().
//        super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_calendar:
                showCalendar();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void hideProgress() {
        if (btnCalendar != null) {
            progressBar.setVisibility(View.INVISIBLE);
            btnCalendar.setVisible(true);
        }
    }

    private void showCalendar() {
        DialogUtil.showCalendar(getActivity(), calendar, this);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        btnCalendar.setVisible(false);
        progressBar.setVisibility(View.VISIBLE);
        calendar.set(year, monthOfYear, dayOfMonth);
        selectedDate = calendar.getTime();
        mDate = DateUtil.getDateStr(selectedDate);
        getActivity().setTitle(getString(R.string.receipt_providers) + " " + mDate);
        getActivity().onRetainCustomNonConfigurationInstance();
        getDatabaseInfo();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_buys_tabs, container, false);
        mDate = DateUtil.getTodayStr();
        mContext = (GlobalState) getActivity().getApplicationContext();
        databaseManager = new DatabaseManager(mContext);

        if (savedInstanceState == null) {
            calendar = Calendar.getInstance();
//            calendar.add(Calendar.DAY_OF_MONTH, -1);
        } else {
            calendar = (Calendar) savedInstanceState.getSerializable(STATE_DATE);
        }
        selectedDate = calendar.getTime();

        getActivity().setTitle(getString(R.string.receipt_providers) + DateUtil.getDateStr(selectedDate.getTime(), DateUtil.YY_MM_DD));
//        getDatabaseInfo();
//        init();
        mViewPager = rootView.findViewById(R.id.pgr_tab);
        tabLayout = rootView.findViewById(R.id.tabs);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_DATE, calendar);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar = getActivity().findViewById(R.id.progress);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        getDatabaseInfo();
    }

    private void init() {
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), mDate);
        mViewPager.setAdapter(mSectionsPagerAdapter);//TODO checar sin internet/sin respuesta server
        mSectionsPagerAdapter.notifyDataSetChanged();

        tabLayout.setViewPager(mViewPager);
        tabLayout.setVisibility(View.VISIBLE);
//        progressBar = (ProgressBar) getActivity().findViewById(R.id.progress);
//        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
//        mViewPager.setCurrentItem(1);
    }

    private void getDatabaseInfo() {
//        items = Collections.emptyList();
        List<Buy> items = databaseManager.listBuys(mDate, mContext.getRegion());
        if (items.isEmpty()) {
            download();
        } else {
            downloadUpdate();
//            init();
        }
    }

    private void downloadUpdate() {
        Log.d(TAG, "downloadUpdateTabs");
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTimeGsonTime();
        httpService.getBuys(mContext.getRegion(), mDate).enqueue(new Callback<List<Buy>>() {
            @Override
            public void onResponse(Call<List<Buy>> call, Response<List<Buy>> response) {
                if (response.isSuccessful()) {
                    fillListUpdate(response.body());
                } else {
                }
                btnCalendar.setVisible(true);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Buy>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_odc));
                if(btnCalendar != null) {
                    btnCalendar.setVisible(true);
                    progressBar.setVisibility(View.GONE);
                    init();
                }
            }
        });
    }

    private void fillListUpdate(List<Buy> list) {
        if (list != null) {
            update(list);
        }
        if(onResume) {
            init();
        }
    }

    private void update(List<Buy> list) {
        databaseManager.insertOrUpdateBuys(list);
//        items = databaseManager.listBuys(mDate, mContext.getRegion(), status);
    }

    private void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getBuys(mContext.getRegion(), mDate).enqueue(new Callback<List<Buy>>() {
            @Override
            public void onResponse(Call<List<Buy>> call, Response<List<Buy>> response) {
                if (response.isSuccessful()) {
                    fillList(response.body());
                } else {
//                    viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//                    viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
                }
//                swipeRefresh.setRefreshing(false);
                btnCalendar.setVisible(true);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Buy>> call, Throwable t) {
                btnCalendar.setVisible(true);
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, mContext.getString(R.string.error_odc));
                init();
//                swipeRefresh.setRefreshing(false);
//                viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//                viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
//                viewSwitcher.setDisplayedChild(1);
            }
        });
    }

    private void fillList(List<Buy> list) {
        if (list != null) {
//            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//            viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
            save(list);
//            setAdapter();
//            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
//            viewSwitcher.findViewById(R.id.list).setVisibility(View.VISIBLE);
        }
        init();
//        viewSwitcher.setDisplayedChild(1);
    }

    public void save(final List<Buy> list) {
        databaseManager.insertOrReplaceInTx(list.toArray(new Buy[list.size()]));
//        items = databaseManager.listBuys(mDate, mContext.getRegion(), status);
    }
}
