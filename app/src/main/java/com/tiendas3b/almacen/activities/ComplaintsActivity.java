package com.tiendas3b.almacen.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.ComplaintsTableAdapter;
import com.tiendas3b.almacen.db.dao.ArticleResearch;
import com.tiendas3b.almacen.db.dao.Complaint;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.views.tables.ComplaintsTableView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintsActivity extends BaseTableCalendarActivity {

    private static final String TAG = ComplaintsActivity.class.getName();



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        btnCalendar = menu.findItem(R.id.action_calendar);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);
        Toolbar toolbar = findViewById(R.id.toolbar);
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

    @SuppressWarnings("unchecked")
    protected void createItems() {
        setTitle(DateUtil.getDateStr(selectedDate));
        Object obj = getLastCustomNonConfigurationInstance();
//        Object obj = null;
        if (obj == null) {
            List<Complaint> vArticles = databaseManager.listComplaint(DateUtil.getDateStr(selectedDate), mContext.getRegion());
            items = createItems(vArticles);
//            items = vArticles;
        } else {
            Log.i(TAG, "ConfigurationInstance");
            items = (List<ArticleResearch>) obj;
        }
    }

    private List<Complaint> createItems(@NonNull List<Complaint> vArticles) {
        return vArticles;
    }

    protected void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getComplaints(mContext.getRegion(), DateUtil.getDateStr(selectedDate)).enqueue(new Callback<List<Complaint>>() {
            @Override
            public void onResponse(Call<List<Complaint>> call, Response<List<Complaint>> response) {
                if (response.isSuccessful()) {
                    fill(response.body());
                } else {
                    showEmptyView();
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<List<Complaint>> call, Throwable t) {
                showEmptyView();
                hideProgress();
                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });
    }

    @Override
    protected <T> void save(List<T> list) {
        setVarticle((List<Complaint>) list);
        super.save(list);
    }

    private void setVarticle(List<Complaint> list) {
        Log.i(TAG, "list: " + list.size());
        for (Complaint sc : list) {
            VArticle article = databaseManager.findViewArticleByIclave(sc.getIclave());
            if(article != null) {//TODO quitar if cuando en BOT se valide que las quejas no acepten multicodigos
                sc.setVarticleId(article.getId());
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void setAdapter() {
        ComplaintsTableView tableView = (ComplaintsTableView) findViewById(R.id.list);
        tableView.setSaveEnabled(false);
        List<Complaint> newItems = new ArrayList<Complaint>(items);
        tableView.setDataAdapter(new ComplaintsTableAdapter(getApplicationContext(), newItems, items));
//        tableView.addDataClickListener(new ClickListener());
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.VISIBLE);
    }

}
