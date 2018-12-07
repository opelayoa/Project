package com.tiendas3b.almacen.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.ScannerCountTableAdapter;
import com.tiendas3b.almacen.db.dao.ScannerCount;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.views.tables.ScannerCountTableView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ConstantConditions")
public class PhysicalInventoryActivity extends BaseTableCalendarActivity {

    private static final String TAG = ArticlesResearchActivity.class.getName();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        btnCalendar = menu.findItem(R.id.action_calendar);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical_inventory);
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
//        Object obj = null;
        if (obj == null) {
            List<ScannerCount> vArticles = databaseManager.listScannerCount(DateUtil.getDateStr(selectedDate), mContext.getRegion());
            items = createItems(vArticles);
//            items = vArticles;
        } else {
            Log.i(TAG, "ConfigurationInstance");
            items = (List<ScannerCount>) obj;
        }
    }

    private List<ScannerCount> createItems(@NonNull List<ScannerCount> vArticles) {
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
        httpService.getPhysicalInventory(mContext.getRegion(), DateUtil.getDateStr(selectedDate)).enqueue(new Callback<List<ScannerCount>>() {
            @Override
            public void onResponse(Call<List<ScannerCount>> call, Response<List<ScannerCount>> response) {
                if (response.isSuccessful()) {
                    fill(response.body());
                } else {
                    showEmptyView();
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<List<ScannerCount>> call, Throwable t) {
                showEmptyView();
                hideProgress();
                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });
    }

    @Override
    protected <T> void save(List<T> list) {
        setVarticle((List<ScannerCount>) list);
        super.save(list);
    }

    private void setVarticle(List<ScannerCount> list) {
        for (ScannerCount sc : list) {
            VArticle varticle = databaseManager.findViewArticleByIclave(sc.getIclave());
            if(varticle == null){
                //sync
            } else {
                sc.setVarticleId(varticle.getId());
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void setAdapter() {
        ScannerCountTableView tableView = (ScannerCountTableView) findViewById(R.id.list);
        tableView.setSaveEnabled(false);
        List<ScannerCount> newItems = new ArrayList(items);
        tableView.setDataAdapter(new ScannerCountTableAdapter(getApplicationContext(), newItems, items));
//        tableView.addDataClickListener(new ClickListener());
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.VISIBLE);
    }

}
