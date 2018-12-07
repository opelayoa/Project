package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.satsuware.usefulviews.LabelledSpinner;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.adapters.GenericSpinnerAdapter;
import com.tiendas3b.almacen.adapters.RoadmapRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Roadmap;
import com.tiendas3b.almacen.db.dao.RoadmapDetail;
import com.tiendas3b.almacen.db.dao.Truck;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.dto.CaptureRoadmap;
import com.tiendas3b.almacen.dto.RoadmapDTO;
import com.tiendas3b.almacen.dto.RoadmapDetailDTO;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings({"ConstantConditions", "FieldCanBeLocal"})
public class RoadmapActivity extends RecyclerViewActivity {

    private static final String TAG = RoadmapActivity.class.getCanonicalName();
    public static final int REQUEST_CODE = 22;
    public static final String EXTRA_TRUCK_ID = "EXTRA_TRUCK_ID";
    public static final String EXTRA_LAST_TRAVEL = "EXTRA_LAST_TRAVEL";
    @BindView(R.id.spnTrucks)
    LabelledSpinner spnTrucks;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    MenuItem btnSave;
    private List<Roadmap> items;
    private long truckId;
    private String dateStr;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_roadmap, menu);
        btnSave = menu.findItem(R.id.action_send);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_new:
                if (items.isEmpty()) {
                    startActivity(0);
                } else {
                    Roadmap lastTravel = items.get(items.size() - 1);
                    startActivity(lastTravel.getTravel());
                }
                return true;
            case R.id.action_send:
                sendInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendInfo() {
        btnSave.setVisible(false);
        progressBar.setVisibility(View.VISIBLE);
        CaptureRoadmap captureRoadmap = createToSend();
        sendRequest(captureRoadmap);
    }

    private void sendRequest(CaptureRoadmap captureRoadmap) {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.insertRoadmap(captureRoadmap).enqueue(new Callback<GeneralResponseDTO>() {
            @Override
            public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                GeneralResponseDTO gr = response.body();
                if (response.isSuccessful() && gr != null && gr.getCode() == 1L) {
                    progressBar.setVisibility(View.INVISIBLE);
                    btnSave.setVisible(true);
                    updateSync();
//                    startActivityForResult(new Intent(mContext, DecreaseReportActivity.class).putExtra("t", Constants.DP)
//                            .putExtra("f", gr.getDescription()).putExtra("d", (Serializable) toSend), -1);
                    Toast.makeText(mContext, "Guardado exitoso", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    btnSave.setVisible(true);
                    Log.e(TAG, "send error gr!");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
//                    swipeRefresh.setRefreshing(false);
//                showEmptyView();
                    progressBar.setVisibility(View.INVISIBLE);
                    btnSave.setVisible(true);
                Log.e(TAG, "send error!");
            }
        });
    }

    private void updateSync() {
        db.updateRoadmaps();
    }

    private CaptureRoadmap createToSend() {
        List<RoadmapDTO> roadmapsDTO = new ArrayList<>();
        List<Roadmap> roadmaps = db.listRoadmapSync(false);
        for (Roadmap r : roadmaps) {
            RoadmapDTO dto = new RoadmapDTO();
            dto.setDate(r.getDate());
            dto.setDriverId(r.getDriverId());
            dto.setTravel(r.getTravel());
            dto.setTruckId(r.getTruckId());
            dto.setActivities(createActivities(r));
            roadmapsDTO.add(dto);
        }
        CaptureRoadmap cr = new CaptureRoadmap();
        cr.setRegionId(mContext.getRegion());
        cr.setToSend(roadmapsDTO);
        return cr;
    }

    private List<RoadmapDetailDTO> createActivities(Roadmap r) {
        List<RoadmapDetailDTO> roadmapsDTO = new ArrayList<>();
        List<RoadmapDetail> activities = r.getActivities();
        for (RoadmapDetail a : activities) {
            RoadmapDetailDTO dto = new RoadmapDetailDTO();
            dto.setDate(r.getDate());
            dto.setEndDate(a.getEndDate());
            dto.setActivityId(a.getActiviyId());
            dto.setOrigin((int) a.getOriginId());
            dto.setDestination((int) a.getDestinationId());
            dto.setInitialKm(a.getInitialKm());
            dto.setFinalKm(a.getFinalKm());
            dto.setInitialTime(a.getInitialTime());
            dto.setFinalTime(a.getFinalTime());
            dto.setScaffold(a.getScaffoldUpload());
            dto.setScaffoldDownload(a.getScaffoldDownload());
            dto.setToll(a.getCash());
            dto.setDiesel(a.getDiesel());
            dto.setTrafficTicket(a.getIave());
            dto.setOtherCost(a.getOtherCost());
            roadmapsDTO.add(dto);
        }
        return roadmapsDTO;
    }

    private void startActivity(int travel) {
        startActivityForResult(new Intent(this, TravelCaptureActivity.class).putExtra(EXTRA_LAST_TRAVEL, travel).putExtra(EXTRA_TRUCK_ID, truckId), REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadmap);
        mContext = (GlobalState) getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    public int getRequestCode() {
        return REQUEST_CODE;
    }

    @Override
    protected void init() {
        super.init();
        dateStr = DateUtil.getDateStr(new Date());
        spnTrucks.setVisibility(View.VISIBLE);
        List<Truck> trucks = db.listActiveTrucks(mContext.getRegion());
        GenericSpinnerAdapter<Truck> truckAdapter = new GenericSpinnerAdapter<>(this, R.layout.spinner_item_table, trucks);
        spnTrucks.setCustomAdapter(truckAdapter);
        spnTrucks.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                truckId = id;
                getDatabaseInfo();
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void getDatabaseInfo() {
        db = DatabaseManager.getInstance(this);
        items = db.listRoadmap(mContext.getRegion(), truckId);
        if (items.isEmpty()) {
//            downloadStatus();
            showEmptyView();
        } else {
            setAdapter();
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        viewSwitcher.setDisplayedChild(1);
    }

//    private void downloadStatus() {
//        Tiendas3bClient httpService = mContext.getHttpService();
//        httpService.getRoadmaps(mContext.getRegion(), dateStr, truckId).enqueue(new Callback<List<RoadmapVO>>() {
//            @Override
//            public void onResponse(Call<List<RoadmapVO>> call, Response<List<RoadmapVO>> response) {
//                if (response.isSuccessful()) {
//                    fill(response.body());
//                } else {
//                    showEmptyView();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<RoadmapVO>> call, Throwable t) {
//                showEmptyView();
//                Log.e(TAG, mContext.getString(R.string.error_odc));
//            }
//        });
//    }
//
//    private void fill(List<RoadmapVO> list) {
//        List<Roadmap> roadmaps = new ArrayList<>();
//        for (RoadmapVO rVO : list) {
//            roadmaps.add(createRoadmap(rVO));
//        }
//        db.insertOrReplaceInTx(roadmaps);
//
//        items = db.listRoadmap(mContext.getRegion(), truckId);
//        if (items.isEmpty()) {
//            showEmptyView();
//        } else {
//            setAdapter();
//            emptyView.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private Roadmap createRoadmap(RoadmapVO rVO) {
//        Roadmap r = new Roadmap();
//        r.setDate(rVO.getDate());
//        r.setSync(false);
//        r.setRegionId(mContext.getRegion());
////        r.setScaffolds(rVO.get);
////        r.setDriverId();
//        r.setTravel(rVO.getTravel());
//        r.setTruckId(rVO.getTruckId());
////        r.setActivities(createDetails(rVO.getActivities()));
//        return r;
//    }
//
//    private List<RoadmapDetail> createDetails(List<RoadmapDetailVO> activities) {
//        List<RoadmapDetail> details = new ArrayList<>();
//        for (RoadmapDetailVO dVO : activities) {
//            RoadmapDetail d = new RoadmapDetail();
//            d.setActiviyId(dVO.getActivityId());
//
//        }
//        return details;
//    }

    protected void setAdapter() {
        RoadmapRecyclerViewAdapter adapter = new RoadmapRecyclerViewAdapter(RoadmapActivity.this, new BaseRecyclerViewAdapter.OnViewHolderClick() {
            @Override
            public void onClick(View view, int position) {
                startActivity(new Intent(mContext, RoadmapDetailActivity.class).putExtra(RoadmapDetailActivity.EXTRA_ROADMAP_ID, items.get(position).getId()));
            }
        });
        adapter.setList(items);
        recyclerView.setAdapter(adapter);
    }
}
