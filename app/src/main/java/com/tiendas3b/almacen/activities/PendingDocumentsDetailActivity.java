package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.PenDocDetailTableAdapter;
import com.tiendas3b.almacen.db.dao.PendingDocumentDetail;
import com.tiendas3b.almacen.dto.TravelDetailDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.NetworkUtil;
import com.tiendas3b.almacen.views.tables.PendingDocumentsDetailTableView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tiendas3b.almacen.activities.TravelDetailActivity.EXTRA_DATE;

public class PendingDocumentsDetailActivity extends AppCompatActivity /*implements DatePickerDialog.OnDateSetListener*/ {

    protected static final String STATE_DATE = "date";
    private static final String TAG = "PenDocDetailActivity";
    public static final String EXTRA_PENDING_DOCUMENT_ID = "EXTRA_PENDING_DOCUMENT_ID";
    private ProgressBar progressBar;
    protected Calendar calendar;
    protected GlobalState mContext;
    protected ViewSwitcher viewSwitcher;
//    protected IDatabaseManager databaseManager;
    protected List items;
    protected List items2;
    protected Date selectedDate;
//    protected MenuItem btnCalendar;
    private long pendingDocumentId;

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_calendar, menu);
//        btnCalendar = menu.findItem(R.id.action_calendar);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (id) {
//            case R.id.action_calendar:
//                showCalendar();
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    protected void hideProgress() {
//        if (btnCalendar != null) {
//            progressBar.setVisibility(View.INVISIBLE);
//            btnCalendar.setVisible(true);
//        }
//    }
//
//    private void showCalendar() {
//        DialogUtil.showCalendarTodayMax(this, calendar, this);
//    }
//
//
//    @Override
//    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        btnCalendar.setVisible(false);
//        progressBar.setVisibility(View.VISIBLE);
//        calendar.set(year, monthOfYear, dayOfMonth);
//        selectedDate = calendar.getTime();
//        super.onRetainCustomNonConfigurationInstance();
//        getDatabaseInfo();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_documents_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        if(savedInstanceState == null) {
//            calendar = Calendar.getInstance();
//            calendar.add(Calendar.DAY_OF_MONTH, -1);
//        } else {
//            calendar = (Calendar) savedInstanceState.getSerializable(STATE_DATE);
//        }
//        selectedDate = calendar.getTime();
        Intent i = getIntent();
        pendingDocumentId = i.getIntExtra(EXTRA_PENDING_DOCUMENT_ID, -1);
        selectedDate = (Date) i.getSerializableExtra(EXTRA_DATE);
        init();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(STATE_DATE, calendar);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

//    @Override
//    protected void onRestart() {
//        databaseManager = new DatabaseManager(this);
//        super.onRestart();
//    }
//
//    @Override
//    protected void onStop() {
//        if (databaseManager != null) databaseManager.closeDbConnections();
//        super.onStop();
//    }
//
//    @Override
//    protected void onResume() {
//        databaseManager = DatabaseManager.getInstance(this);
//        super.onResume();
//    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return new ArrayList<>(items);
    }

    protected void init() {
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        mContext = (GlobalState) getApplicationContext();
//        databaseManager = DatabaseManager.getInstance(mContext);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher);
        if (items == null) {
            getDatabaseInfo();
        }
    }

    private void getDatabaseInfo() {
        createItems();
        if (items == null || items.isEmpty()
                || items2 == null || items2.isEmpty()) {
            if (NetworkUtil.isConnected(mContext)) {
                download();
            }
        } else {
            setAdapter();
            viewSwitcher.setDisplayedChild(1);
//            hideProgress();
        }
    }

    protected void createItems() {
        setTitle(DateUtil.getDateStr(selectedDate));
        Object obj = getLastCustomNonConfigurationInstance();
        if (obj == null) {
//            List<Complaint> vArticles = db.listComplaint(DateUtil.getDateStr(selectedDate), mContext.getRegion());
//            items = createItems(vArticles);
            items = null;
        } else {
            Log.i(TAG, "ConfigurationInstance");
            items = (List<TravelDetailDTO>) obj;
        }
    }

    protected void showEmptyView() {
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.GONE);
    }

    protected <T> void fill(List<T> list) {
        if (list == null || list.isEmpty()) {
            viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
        } else {
            save(list);
        }
        viewSwitcher.setDisplayedChild(1);
    }

    protected <T> void fill2(List<T> list) {
        if (list == null || list.isEmpty()) {
            viewSwitcher.findViewById(R.id.list2).setVisibility(View.GONE);
        } else {
            save2(list);
        }
        viewSwitcher.setDisplayedChild(1);
    }

//    protected <T> void save(final List<T> list) {
//        new AsyncTask() {
//
//            @Override
//            protected Object doInBackground(Object[] objects) {
//                databaseManager.insertOrReplaceInTx(toArray(list));
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Object o) {
//                createItems();
//                setAdapter();
//            }
//        }.execute();
//    }

    protected void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getPendingDocsDetail(mContext.getRegion(), DateUtil.getDateStr(selectedDate)).enqueue(new Callback<List<PendingDocumentDetail>>() {
            @Override
            public void onResponse(Call<List<PendingDocumentDetail>> call, Response<List<PendingDocumentDetail>> response) {
                if (response.isSuccessful()) {
                    fill(response.body());
                } else {
                    showEmptyView();
                }
//                hideProgress();
            }

            @Override
            public void onFailure(Call<List<PendingDocumentDetail>> call, Throwable t) {
                showEmptyView();
//                hideProgress();
                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });

//        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getPendingDocsTransactions(mContext.getRegion(), DateUtil.getDateStr(selectedDate)).enqueue(new Callback<List<PendingDocumentDetail>>() {
            @Override
            public void onResponse(Call<List<PendingDocumentDetail>> call, Response<List<PendingDocumentDetail>> response) {
                if (response.isSuccessful()) {
                    fill2(response.body());
                } else {
//                    showEmptyView();
                }
//                hideProgress();
            }

            @Override
            public void onFailure(Call<List<PendingDocumentDetail>> call, Throwable t) {
                showEmptyView();
//                hideProgress();
                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });
    }

//    @Override
    protected <T> void save(List<T> list) {
        items = list;
        setAdapter();
    }

    protected <T> void save2(List<T> list) {
        items2 = list;
        setAdapter2();
    }

    @SuppressWarnings("unchecked")
    protected void setAdapter() {
        PendingDocumentsDetailTableView tableView = (PendingDocumentsDetailTableView) findViewById(R.id.list);
        tableView.setSaveEnabled(false);
        List<PendingDocumentDetail> newItems = new ArrayList<PendingDocumentDetail>(items);
        tableView.setDataAdapter(new PenDocDetailTableAdapter(getApplicationContext(), newItems, items));
//        tableView.addDataClickListener();
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.list).setVisibility(View.VISIBLE);
    }

    protected void setAdapter2() {
        PendingDocumentsDetailTableView tableView = (PendingDocumentsDetailTableView) findViewById(R.id.list2);
        tableView.setSaveEnabled(false);
        List<PendingDocumentDetail> newItems = new ArrayList<PendingDocumentDetail>(items2);//TODO checar si son items o items2 XD
        tableView.setDataAdapter(new PenDocDetailTableAdapter(getApplicationContext(), newItems, items2));
//        tableView.addDataClickListener();
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.list2).setVisibility(View.VISIBLE);
    }

}
