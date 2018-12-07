package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.PendingDocumentTableAdapter;
import com.tiendas3b.almacen.db.dao.PendingDocument;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.views.tables.PendingDocumentsTableView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingDocumentsActivity extends BaseTableCalendarActivity {

    private static final String TAG = PendingDocumentsActivity.class.getName();

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_articles_research, menu);
//        btnCalendar = menu.findItem(R.id.action_calendar);
//        return true;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_documents);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        } else {
            calendar = (Calendar) savedInstanceState.getSerializable(STATE_DATE);
        }
        selectedDate = calendar.getTime();
        init();
    }

    protected void createItems() {
//        setTitle(DateUtil.getDateStr(selectedDate));
        Object obj = getLastCustomNonConfigurationInstance();
        if (obj == null) {
//            List<Complaint> vArticles = db.listComplaint(DateUtil.getDateStr(selectedDate), mContext.getRegion());
//            items = createItems(vArticles);
            items = null;
        } else {
            Log.i(TAG, "ConfigurationInstance");
            items = (List<PendingDocument>) obj;
        }
    }

    protected void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        long r = mContext.getRegion();
        Call<List<PendingDocument>> s = httpService.getPendingDocuments(r);
        s.enqueue(new Callback<List<PendingDocument>>() {
            @Override
            public void onResponse(Call<List<PendingDocument>> call, Response<List<PendingDocument>> response) {
                if (response.isSuccessful()) {
                    fill(response.body());
                } else {
                    showEmptyView();
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<List<PendingDocument>> call, Throwable t) {
                showEmptyView();
                hideProgress();
                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });
    }

    @Override
    protected <T> void save(List<T> list) {
        items = list;
        setAdapter();
    }

    @SuppressWarnings("unchecked")
    protected void setAdapter() {
        PendingDocumentsTableView tableView = (PendingDocumentsTableView) findViewById(R.id.list);
        tableView.setSaveEnabled(false);
        List<PendingDocument> newItems = new ArrayList<PendingDocument>(items);
        tableView.setDataAdapter(new PendingDocumentTableAdapter(getApplicationContext(), newItems, items));
        tableView.addDataClickListener(new TableDataClickListener<PendingDocument>() {
            @Override
            public void onDataClicked(int rowIndex, PendingDocument clickedData) {
                try {
                    mContext.startActivity(new Intent(mContext, PendingDocumentsDetailActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .putExtra(PendingDocumentsDetailActivity.EXTRA_PENDING_DOCUMENT_ID, clickedData.getId())
                            .putExtra(TravelDetailActivity.EXTRA_DATE, DateUtil.getDate(clickedData.getDate()))
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.VISIBLE);
    }

}
