package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.TravelDetailTableAdapter;
import com.tiendas3b.almacen.dto.TravelDetailDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.views.tables.TravelDetailTableView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelDetailActivity extends BaseTableCalendarActivity {

    private static final String TAG = TravelDetailActivity.class.getName();
    public static final String EXTRA_TRUCK_ID = "EXTRA_TRUCK_ID";
    public static final String EXTRA_DATE = "EXTRA_DATE";
    private long truckId;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        btnCalendar = menu.findItem(R.id.action_calendar);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_detail);
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
        Intent i = getIntent();
        truckId = i.getIntExtra(EXTRA_TRUCK_ID, -1);
        selectedDate = (Date) i.getSerializableExtra(EXTRA_DATE);
        init();
    }
    
    //    @SuppressWarnings("unchecked")
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

//    private List<Complaint> createItems(@NonNull List<Complaint> vArticles) {
////        List<ReceiptSheetDetPrintDTO> articlesVO = new ArrayList<>();
////        for (ArticleResearch a : vArticles) {
////            ReceiptSheetDetPrintDTO aVO = new ReceiptSheetDetPrintDTO();
////            aVO.setIclave(a.getIclave());
////            aVO.setDescription(a.getDescription().concat(" / ").concat(a.getUnity()));
////            aVO.setCost(a.getCost());
////            articlesVO.add(aVO);
////        }
////        return articlesVO;
//        return vArticles;
//    }

    protected void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getTravelDetail(mContext.getRegion(), DateUtil.getDateStr(selectedDate), truckId).enqueue(new Callback<List<TravelDetailDTO>>() {
            @Override
            public void onResponse(Call<List<TravelDetailDTO>> call, Response<List<TravelDetailDTO>> response) {
                if (response.isSuccessful()) {
                    fill(response.body());
                } else {
                    showEmptyView();
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<List<TravelDetailDTO>> call, Throwable t) {
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
        TravelDetailTableView tableView = (TravelDetailTableView) findViewById(R.id.list);
        tableView.setSaveEnabled(false);
        List newItems = new ArrayList<>(items);
        tableView.setDataAdapter(new TravelDetailTableAdapter(getApplicationContext(), newItems, items));
//        tableView.addDataClickListener();
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.VISIBLE);
    }

}
