package com.tiendas3b.almacen.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.SmSupplyTableAdapter;
import com.tiendas3b.almacen.dto.SmDetailDTO;
import com.tiendas3b.almacen.dto.SmSummaryDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.views.tables.SmSupplyTableView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmSupplyActivity extends BaseTableCalendarActivity {

    private static final String TAG = SmSupplyActivity.class.getName();
    private Tiendas3bClient httpService;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        btnCalendar = menu.findItem(R.id.action_calendar);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sm_supply);
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
        httpService = ((GlobalState) getApplicationContext()).getHttpServiceWithAuthTime();
        init();
        downloadSummary();
    }

    private void downloadSummary() {
        httpService.getSmSummary(mContext.getRegion(), DateUtil.getDateStr(selectedDate)).enqueue(new Callback<SmSummaryDTO>() {
            @Override
            public void onResponse(Call<SmSummaryDTO> call, Response<SmSummaryDTO> response) {
                Log.i(TAG, "getSmSummary");
                if (response.isSuccessful()) {
                    fillSummary(response.body());
                } else {
//                    showEmptyView();
                }
//                hideProgress();
            }

            @Override
            public void onFailure(Call<SmSummaryDTO> call, Throwable t) {
//                showEmptyView();
//                hideProgress();
                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });
    }

    private void fillSummary(SmSummaryDTO summaryDTO) {
        DecimalFormat dfThousnds = new DecimalFormat(Constants.FORMAT_MONEY);
        ((EditText)findViewById(R.id.txtAtmRefSum)).setText(String.valueOf(summaryDTO.getAtmrefCount()));
        ((EditText)findViewById(R.id.txtIclaveSum)).setText(String.valueOf(summaryDTO.getIcalveCount()));
        ((EditText)findViewById(R.id.txtAmountSum)).setText(String.valueOf(summaryDTO.getAtmcantCount()));
        ((EditText)findViewById(R.id.txtCostSum)).setText(dfThousnds.format(summaryDTO.getAtmcostCount()));
        ((EditText)findViewById(R.id.txtSaleSum)).setText(dfThousnds.format(summaryDTO.getAtmsaleCount()));

    }

    //    @SuppressWarnings("unchecked")
    protected void createItems() {
        setTitle(DateUtil.getDateStr(selectedDate));
        Object obj = getLastCustomNonConfigurationInstance();
        if (obj == null) {
//            List<Complaint> vArticles = db.listComplaint(DateUtil.getDateStr(selectedDate), mContext.getRegion());
//            items = createItems(vArticles);
        } else {
            Log.i(TAG, "ConfigurationInstance");
            items = (List<SmDetailDTO>) obj;
        }
    }

    protected void download() {
        httpService.getSmDetail(mContext.getRegion(), DateUtil.getDateStr(selectedDate)).enqueue(new Callback<List<SmDetailDTO>>() {
            @Override
            public void onResponse(Call<List<SmDetailDTO>> call, Response<List<SmDetailDTO>> response) {
                Log.i(TAG, "getSmDetail");
                if (response.isSuccessful()) {
                    fill(response.body());
                } else {
                    showEmptyView();
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<List<SmDetailDTO>> call, Throwable t) {
                showEmptyView();
                hideProgress();
                Log.e(TAG, mContext.getString(R.string.error_empty_data));
            }
        });
    }

    @Override
    protected <T> void save(List<T> list) {
//        setVarticle((List<Complaint>) list);
//        super.save(list);
        items = list;
        setAdapter();
    }

    @SuppressWarnings("unchecked")
    protected void setAdapter() {
        SmSupplyTableView tableView = (SmSupplyTableView) findViewById(R.id.list);
        tableView.setSaveEnabled(false);
        List<SmDetailDTO> newItems = new ArrayList<SmDetailDTO>(items);
        tableView.setDataAdapter(new SmSupplyTableAdapter(getApplicationContext(), newItems, items));
//        tableView.addDataClickListener(new ClickListener());
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.VISIBLE);
    }

}
