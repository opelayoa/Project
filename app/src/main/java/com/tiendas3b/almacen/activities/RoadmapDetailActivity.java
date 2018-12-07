package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.adapters.RoadmapDetailRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Roadmap;
import com.tiendas3b.almacen.db.dao.RoadmapDetail;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.services.TrackingService;

import java.util.List;

import butterknife.BindView;

@SuppressWarnings({"ConstantConditions", "FieldCanBeLocal"})
public class RoadmapDetailActivity extends RecyclerViewActivity {

    private static final String TAG = "RoadmapDetailActivity";
    private static final int REQUEST_CODE = 23;
    public static final String EXTRA_ROADMAP_ID = "EXTRA_ROADMAP_ID";
    public static final String EXTRA_LAST_ACTIVITY_ID = "EXTRA_LAST_ACTIVITY_ID";
    public static final long ACTIVITY_RETURN = 4L;
    private List<RoadmapDetail> items;
    private long roadmapId;
    @BindView(R.id.txtTruck)
    EditText txtTruck;
    @BindView(R.id.txtScaffold)
    EditText txtScaffold;
    @BindView(R.id.txtDriver)
    EditText txtDriver;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_roadmap_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new) {
            if (items.isEmpty()) {
                startActivity(-1L);
            } else {
                RoadmapDetail lastActivity = items.get(items.size() - 1);
                if (ACTIVITY_RETURN == lastActivity.getActiviyId()) {
                    Toast.makeText(this, "Crea un nuevo viaje", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(this, RoadmapActivity.class));
                    onBackPressed();
                } else {
                    startActivity(lastActivity.getId());
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startActivity(long activityId) {
        startActivityForResult(new Intent(this, RouteCaptureActivity.class).putExtra(EXTRA_ROADMAP_ID, roadmapId).putExtra(EXTRA_LAST_ACTIVITY_ID, activityId), REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadmap);
        mContext = (GlobalState) getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

        Log.i(TAG, "start services!!!!!!!!");
        Intent i = new Intent(this, TrackingService.class);
        startService(i);
    }

    @Override
    public int getRequestCode() {
        return REQUEST_CODE;
    }

    @Override
    protected void init() {
        super.init();
        Roadmap roadmap = db.getById(roadmapId, Roadmap.class);
        txtTruck.setText(roadmap.getTruck().getDescription());
        Integer scaffolds = roadmap.getScaffolds();
        txtScaffold.setText(String.valueOf(scaffolds == null ? 0 : scaffolds));
        txtDriver.setText(roadmap.getDriver().getDescription());
        txtTruck.setVisibility(View.VISIBLE);
        txtDriver.setVisibility(View.VISIBLE);
        txtScaffold.setVisibility(View.VISIBLE);
    }

    @Override
    protected void getDatabaseInfo() {
        roadmapId = getIntent().getLongExtra(EXTRA_ROADMAP_ID, -1L);
        db = DatabaseManager.getInstance(this);
        items = db.listRoadmapDetails(roadmapId);
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
////        Tiendas3bClient httpService = mContext.getHttpService();
////        httpService.getActivities(mContext.getRegion(), dateStr, truckId).enqueue(new Callback<List<Buy>>() {
////            @Override
////            public void onResponse(Call<List<Buy>> call, Response<List<Buy>> response) {
////                if (response.isSuccessful()) {
//////                    fillList(response.body());
////                } else {
////                    showEmptyView();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<List<Buy>> call, Throwable t) {
////                showEmptyView();
////                Log.e(TAG, mContext.getString(R.string.error_odc));
////            }
////        });
//    }

    protected void setAdapter() {
        RoadmapDetailRecyclerViewAdapter adapter = new RoadmapDetailRecyclerViewAdapter(RoadmapDetailActivity.this, new BaseRecyclerViewAdapter.OnViewHolderClick() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(mContext, String.valueOf(items.get(position).getDestination()), Toast.LENGTH_LONG).show();
            }
        });
        adapter.setList(items);
        recyclerView.setAdapter(adapter);
    }
}
