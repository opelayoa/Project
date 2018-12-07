package com.tiendas3b.almacen.shipment.vrp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.TrackingActivity;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.shipment.vrp.activities.ReviewFragment;
import com.tiendas3b.almacen.shipment.vrp.dto.DesmadreDTO;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.Preferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tiendas3b.almacen.shipment.vrp.TravelsActivity.toList;


public class VrpActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 12;
    private static final int STATUS_REVIEW = 1;
    private static final String TAG = "VrpActivity";
    private GlobalState mContext;
    private long truckId;
    private Preferences preferences;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrp);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init() {
        mContext = (GlobalState) getApplicationContext();
        preferences = new Preferences(mContext);
        truckId = preferences.getSharedLongSafe(Preferences.KEY_TRUCK_ID, 0L);
        if(truckId == 0L){
            Toast.makeText(mContext, "Asignar camión por favor.", Toast.LENGTH_LONG).show();
            startActivityForResult(TrackingActivity.getStartIntent(mContext), REQUEST_CODE);
        } else {

        }
        showFragment();

    }

    @SuppressWarnings("unchecked")
    private void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTimeVpr();
        Log.d(TAG, "selectedTruck: " + truckId);
        httpService.getTravels(mContext.getRegion(), DateUtil.getTodayStr(), truckId).enqueue(new Callback() {
//        httpService.getTravels(1000L, date, selectedTruck).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    JsonElement json = new Gson().toJsonTree(response.body());
                    JsonObject root = json.getAsJsonObject();
                    if (root.get("res").getAsInt() == 1) {
                        JsonArray rutas = root.getAsJsonArray("rutas");
                        List<DesmadreDTO> desmadres = toList(rutas.toString());
                        process(desmadres);
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void process(List<DesmadreDTO> desmadres) {

    }

    private void showFragment() {
        int currentStatus = preferences.getInt(Preferences.KEY_TRAVEL_STATUS, STATUS_REVIEW);
        switch (currentStatus) {
            case STATUS_REVIEW:
                changeMainContent(new ReviewFragment());
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void changeMainContent(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment)
//                .addToBackStack(null)
                .commit();
    }


}
