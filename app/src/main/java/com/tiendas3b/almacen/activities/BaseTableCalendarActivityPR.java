package com.tiendas3b.almacen.activities;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.DialogUtil;
import com.tiendas3b.almacen.util.NetworkUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dfa on 05/07/2016.
 */
@SuppressWarnings("unchecked")
public abstract class BaseTableCalendarActivityPR extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    protected static final String STATE_DATE = "date";
    private static final String TAG = "BaseTableCalAct";
    protected Calendar calendar;
    protected GlobalState mContext;
    protected IDatabaseManager databaseManager;
    protected List items;
    protected Date selectedDate;
    protected MenuItem btnCalendar;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.switcher)
    protected ViewSwitcher viewSwitcher;
    @BindView(R.id.emptyView)
    View emptyView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        menu.findItem(R.id.action_cam_scan).setVisible(true);
        btnCalendar = menu.findItem(R.id.action_calendar);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_calendar:
                showCalendar();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void hideProgress() {
        if (btnCalendar != null) {
            progressBar.setVisibility(View.INVISIBLE);
            btnCalendar.setVisible(true);
        }
    }

    protected void showCalendar() {
        DialogUtil.showCalendar(BaseTableCalendarActivityPR.this, calendar, this);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        btnCalendar.setVisible(false);
        progressBar.setVisibility(View.VISIBLE);
        calendar.set(year, monthOfYear, dayOfMonth);
        selectedDate = calendar.getTime();
        super.onRetainCustomNonConfigurationInstance();
        download();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(STATE_DATE, calendar);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        Log.e(TAG, "onRestart");
        databaseManager = new DatabaseManager(this);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop");
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume");
        databaseManager = DatabaseManager.getInstance(this);
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        Log.e(TAG, "onPostResume");
        super.onPostResume();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return items == null ? null : new ArrayList<>(items);
    }

    @SuppressWarnings("ConstantConditions")
    protected void initDate(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            calendar = Calendar.getInstance();
        } else {
            calendar = (Calendar) savedInstanceState.getSerializable(STATE_DATE);
        }
        selectedDate = calendar.getTime();
        setTitle(DateUtil.getDateStr(selectedDate));
    }

    protected void init() {
        ButterKnife.bind(this);
        mContext = (GlobalState) getApplicationContext();
        databaseManager = DatabaseManager.getInstance(mContext);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        if (items == null) {
            getDatabaseInfo();
        }
    }

    private void getDatabaseInfo() {
//        createItems();
        if (items == null || items.isEmpty()) {
            if (NetworkUtil.isConnected(mContext)) {
                download();
            }
        } else {
            setAdapter();
            viewSwitcher.setDisplayedChild(1);
            hideProgress();
        }
    }

    protected void showEmptyView() {
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.GONE);
    }

    protected <T> void fill(List<T> list) {
        if (list == null || list.isEmpty()) {
            showEmptyView();
            hideProgress();
        } else {
            save(list);
        }
        viewSwitcher.setDisplayedChild(1);
    }

    protected void showError() {
        showEmptyView();
        hideProgress();
    }

    protected <T> void save(final List<T> list) {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                databaseManager.insertOrReplaceInTx(toArray(list));
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                createItems();
                setAdapter();
                hideProgress();
            }
        }.execute();
    }

    public static <T> T[] toArray(Collection<T> c, T[] a) {
        return c.size() > a.length ? c.toArray((T[]) Array.newInstance(a.getClass().getComponentType(), c.size())) : c.toArray(a);
    }

    /**
     * The collection CAN be empty
     */
    public static <T> T[] toArray(Collection<T> c, Class klass) {
        return toArray(c, (T[]) Array.newInstance(klass, c.size()));
    }

    /**
     * The collection CANNOT be empty!
     */
    public static <T> T[] toArray(@NonNull Collection<T> c) {
        return toArray(c, c.iterator().next().getClass());
    }

    protected abstract void setAdapter();

    protected abstract void download();

    protected abstract void createItems();


}
