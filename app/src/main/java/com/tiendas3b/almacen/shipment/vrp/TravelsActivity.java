package com.tiendas3b.almacen.shipment.vrp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
//import com.tiendas3b.almacen.R2;
import com.tiendas3b.almacen.adapters.GenericSpinnerAdapter;
import com.tiendas3b.almacen.db.dao.Truck;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.shipment.vrp.dto.DesmadreDTO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravelsActivity extends AppCompatActivity {

    private static final String TAG = "TravelsActivity";
    @BindView(R.id.spnTrucks)
    Spinner spnTrucks;
    @BindView(R.id.list)
    RecyclerView recyclerView;

    private GlobalState mContext;
    private long selectedTruck;
    private IDatabaseManager db;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    private void init() {
//        date = DateUtil.getTodayStr();
        date = "2017-09-12";//borrar
        mContext = (GlobalState) getApplicationContext();
        db = new DatabaseManager(mContext);
        ButterKnife.bind(this);
        initSpinner();

    }

    private void initSpinner() {
        List<Truck> trucks = db.listActiveTrucks(mContext.getRegion());
        GenericSpinnerAdapter<Truck> truckAdapter = new GenericSpinnerAdapter<>(this, R.layout.spinner_item_table, trucks);
        spnTrucks.setAdapter(truckAdapter);
        spnTrucks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                selectedTruck = id;
                selectedTruck = 31;//borrar
                Log.d(TAG, "onItemSelected");
                download();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spnTrucks.setSelection(0);
    }

    @SuppressWarnings("unchecked")
    private void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTimeVpr();
        Log.d(TAG, "selectedTruck: " + selectedTruck);
//        httpService.getTravels(mContext.getRegion(), DateUtil.getTodayStr(), selectedTruck).enqueue(new Callback() {
        httpService.getTravels(1000L, date, selectedTruck).enqueue(new Callback() {
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
                Log.e(TAG, "err:" + t.getMessage());
            }
        });
    }

    private void process(List<DesmadreDTO> desmadres) {
        TravelsRvAdapter adapter = new TravelsRvAdapter(mContext, null);
        adapter.setList(desmadres);
        recyclerView.setAdapter(adapter);
    }

    public static List<DesmadreDTO> toList(String json) {
        if (null == json) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<DesmadreDTO>>() {
        }.getType());
    }

}
