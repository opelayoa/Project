package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.dto.receipt.ReceiptFolioDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.receipt.edit.ReceiptEditTimetableActivity;
import com.tiendas3b.almacen.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditReceiptFolio extends AppCompatActivity {

    private static final String TAG = EditReceiptFolio.class.getName();
    private Buy item;
    private DatabaseManager db;
    private GlobalState mContext;
    Integer dpFolioEM = 0;
    private Tiendas3bClient httpService;

    @BindView(R.id.txtProvider)
    EditText txtProvider;
    @BindView(R.id.txtFolio)
    EditText txtFolio;
    @BindView(R.id.txtPlatform)
    EditText txtPlatform;
    @BindView(R.id.txtTime)
    EditText txtTime;
    @BindView(R.id.txtDeliveryTime)
    EditText txtDeliveryTime;
    @BindView(R.id.txtFolioEM)
    EditText txtFolioEM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_folio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ButterKnife.bind(this);
        init();
        toolbar.setTitle("FOLIO");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void init(){
        mContext = (GlobalState) getApplicationContext();
        httpService = ((GlobalState) getApplicationContext()).getHttpServiceWithAuthTime();
        db = new DatabaseManager(mContext);
        Intent intent = getIntent();
        Long buyId = intent.getLongExtra(Constants.EXTRA_BUY, 0L);
        item = db.getById(buyId, Buy.class);

        if (item != null) {

            getDpFolio(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    txtProvider.setText(item.getProvider().getName());
                    txtFolio.setText(String.valueOf(item.getFolio()));
                    txtPlatform.setText(item.getPlatform().toString());
                    txtTime.setText(item.getTime());
                    txtDeliveryTime.setText(item.getDeliveryTime());
                    txtFolioEM.setText(dpFolioEM.toString());
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });
        }
        else{
            Toast.makeText(mContext, "Fuera de Recibo Exprés", Toast.LENGTH_LONG).show();
            startActivity(new Intent(mContext, ReceiptEditTimetableActivity.class).putExtra(ReceiptEditTimetableActivity.EXTRA_ORDER_TYPE, 0));
            finish();
        }
    }

    private void getDpFolio(Callback callback){
        httpService.getDpFolio(item.getFolio()).enqueue(new Callback<ReceiptFolioDTO>() {
            @Override
            public void onResponse(Call<ReceiptFolioDTO> call, Response<ReceiptFolioDTO> response) {
                if (response.isSuccessful()) {
                        dpFolioEM = response.body().getFolioEM();
                }
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ReceiptFolioDTO> call, Throwable t) {
                Toast.makeText(mContext, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, ReceiptEditTimetableActivity.class).putExtra(ReceiptEditTimetableActivity.EXTRA_ORDER_TYPE, 0));
        finish();
    }

}
