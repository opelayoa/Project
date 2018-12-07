package com.tiendas3b.almacen.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.ArticlesResearchTableAdapter;
import com.tiendas3b.almacen.db.dao.ArticleResearch;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.views.tables.ArticlesResearchTableView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ConstantConditions")
public class ArticlesResearchActivity extends BaseTableCalendarActivity {

    private static final String TAG = ArticlesResearchActivity.class.getName();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        btnCalendar = menu.findItem(R.id.action_calendar);
        return true;
    }

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

//    private void hideProgress() {
//        if (btnCalendar != null) {
//            progressBar.setVisibility(View.INVISIBLE);
//            btnCalendar.setVisible(true);
//        }
//    }

//    private void showCalendar() {
//        DialogUtil.showCalendarTodayMax(ArticlesResearchActivity.this, c, this);
//    }

//    @Override
//    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        btnCalendar.setVisible(false);
//        progressBar.setVisibility(View.VISIBLE);
//        c.set(year, monthOfYear, dayOfMonth);
//        selectedDate = c.getTime();
//        super.onRetainCustomNonConfigurationInstance();
//        getDatabaseInfo();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_research);
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

    @SuppressWarnings("unchecked")
    protected void createItems() {
        setTitle(DateUtil.getDateStr(selectedDate));
        Object obj = getLastCustomNonConfigurationInstance();
        if (obj == null) {
            List<ArticleResearch> vArticles = databaseManager.listArticlesResearch(new SimpleDateFormat(DateUtil.YYYY_MM_DD, Locale.getDefault()).format(selectedDate), mContext.getRegion());
            items = createItems(vArticles);
//            items = vArticles;
        } else {
            items = (List<ArticleResearch>) obj;
        }
    }

    private List<ArticleResearch> createItems(@NonNull List<ArticleResearch> vArticles) {
//        List<ReceiptSheetDetPrintDTO> articlesVO = new ArrayList<>();
//        for (ArticleResearch a : vArticles) {
//            ReceiptSheetDetPrintDTO aVO = new ReceiptSheetDetPrintDTO();
//            aVO.setIclave(a.getIclave());
//            aVO.setDescription(a.getDescription().concat(" / ").concat(a.getUnity()));
//            aVO.setCost(a.getCost());
//            articlesVO.add(aVO);
//        }
//        return articlesVO;
        return vArticles;
    }

    protected void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getArticlesResearch(mContext.getRegion(), DateUtil.getDateStr(selectedDate)).enqueue(new Callback<List<ArticleResearch>>() {
            @Override
            public void onResponse(Call<List<ArticleResearch>> call, Response<List<ArticleResearch>> response) {
                if (response.isSuccessful()) {
                    fill(response.body());
                } else {
                    showEmptyView();
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<List<ArticleResearch>> call, Throwable t) {
                showEmptyView();
                hideProgress();
                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });
    }

//    private void showEmptyView() {
//        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.GONE);
//    }
//
//    private void fill(List<ArticleResearch> list) {
//        if (list == null) {
//            showEmptyView();
//        } else {
//            save(list);
//            createItems();
//            setAdapter();
//        }
//        viewSwitcher.setDisplayedChild(1);
//    }
//
//    private void save(List<ArticleResearch> list) {
//        db.insertOrReplaceInTx(list.toArray(new ArticleResearch[list.size()]));
//    }

    protected void setAdapter() {
        ArticlesResearchTableView tableView = (ArticlesResearchTableView) findViewById(R.id.list);
        List<ArticleResearch> newItems = new ArrayList<ArticleResearch>(items);
        ArticlesResearchTableAdapter adapter = new ArticlesResearchTableAdapter(getApplicationContext(), newItems, items);
        tableView.setDataAdapter(adapter);
//        tableView.addDataClickListener(new ClickListener());
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.VISIBLE);
    }
}
