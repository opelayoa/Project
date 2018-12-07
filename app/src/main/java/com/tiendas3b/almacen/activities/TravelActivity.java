package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.TravelTableAdapter;
import com.tiendas3b.almacen.dto.GeneralTravelDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.views.tables.TravelTableView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelActivity extends BaseTableCalendarActivity {

    private static final String TAG = TravelActivity.class.getName();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        btnCalendar = menu.findItem(R.id.action_calendar);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
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
        setTitle(DateUtil.getDateStr(selectedDate));
        Object obj = getLastCustomNonConfigurationInstance();
        if (obj == null) {
//            List<Complaint> vArticles = db.listComplaint(DateUtil.getDateStr(selectedDate), mContext.getRegion());
//            items = createItems(vArticles);
            items = null;
        } else {
            Log.i(TAG, "ConfigurationInstance");
            items = (List<GeneralTravelDTO>) obj;
        }
    }

    @Override
    protected void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        long r = mContext.getRegion();
        String d = DateUtil.getDateStr(selectedDate);
        Call<List<GeneralTravelDTO>> s = httpService.getTravel(r, d);
        s.enqueue(new Callback<List<GeneralTravelDTO>>() {
            @Override
            public void onResponse(Call<List<GeneralTravelDTO>> call, Response<List<GeneralTravelDTO>> response) {
                if (response.isSuccessful()) {
                    fill(response.body());
                } else {
                    showEmptyView();
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<List<GeneralTravelDTO>> call, Throwable t) {
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

    @Override
    @SuppressWarnings("unchecked")
    protected void setAdapter() {
        TravelTableView tableView = (TravelTableView) findViewById(R.id.list);
        tableView.setSaveEnabled(false);
        List<GeneralTravelDTO> newItems = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            //algun dia sera la unica opcion XD
            newItems = (List<GeneralTravelDTO>) items.stream().collect(Collectors.toList());
        } else {
            newItems = new ArrayList<GeneralTravelDTO>(items);
        }
        tableView.setDataAdapter(new TravelTableAdapter(getApplicationContext(), newItems, items));
        tableView.addDataClickListener(new TableDataClickListener<GeneralTravelDTO>() {
            @Override
            public void onDataClicked(int rowIndex, GeneralTravelDTO clickedData) {
                mContext.startActivity(new Intent(mContext, TravelDetailActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        .putExtra(TravelDetailActivity.EXTRA_TRUCK_ID, clickedData.getTruckId())
                        .putExtra(TravelDetailActivity.EXTRA_DATE, selectedDate)
                );
            }
        });
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.VISIBLE);
    }

}
