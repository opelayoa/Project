package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.EditReceiptDecreaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.DecreaseMP;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.fragments.EditDecreaseFragment;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.receipt.edit.ReceiptEditTimetableActivity;
import com.tiendas3b.almacen.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditReceiptDecrease extends AppCompatActivity implements EditDecreaseFragment.OnFragmentInteractionListener{

    private final String TAG = getClass().getName();
    private DatabaseManager db;
    private GlobalState mContext;
    private int sample;
    Boolean aBoolean = false;
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
    private Buy item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_receipt_decrease);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ButterKnife.bind(this);
        toolbar.setTitle("MERMA");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    public void init(){
        mContext = (GlobalState) getApplicationContext();
        db = new DatabaseManager(mContext);
        db.truncate(DecreaseMP.class);

        Intent intent = getIntent();
        Long buyId = intent.getLongExtra(Constants.EXTRA_BUY, 0L);
        item = db.getById(buyId, Buy.class);
        txtProvider.setText(item.getProvider().getName());
        txtFolio.setText(String.valueOf(item.getFolio()));
        txtPlatform.setText(item.getPlatform().toString());
        txtTime.setText(item.getTime());
        txtDeliveryTime.setText(item.getDeliveryTime());

    }

        @Override
        public void onBackPressed() {
            startActivity(new Intent(mContext, ReceiptEditTimetableActivity.class).putExtra(ReceiptEditTimetableActivity.EXTRA_ORDER_TYPE, 0));
            finish();
        }

        @Override
        public void onFragmentInteraction(EditReceiptDecreaseRecyclerViewAdapter.ViewHolder holder, int position) {
            EditDecreaseFragment articleFrag = (EditDecreaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
            if (articleFrag != null) {
                articleFrag.collapseAll(holder.llCaptureSheetDetail);
            }
        }

    @Override
    public boolean onSaveDetail(EditReceiptDecreaseRecyclerViewAdapter.ViewHolder viewHolder) {
        if (viewHolder.btnSaveDetail.getText().equals("Guardar")) {
            if (isValid(viewHolder)) {



                getArticle(viewHolder.barcode, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        VArticle vArticle = (VArticle) response.body();
                        DecreaseMP decreaseMP = new DecreaseMP(vArticle.getIclave(), vArticle.getDescription().concat(" / ").concat(vArticle.getUnity()), sample, viewHolder.spnTypeId,
                                viewHolder.spnObsId, viewHolder.spnObsLogId, vArticle.getCost(), "", viewHolder.folioOdc);
////                        decreaseMP.setType(20L);//TIPO CADUCIDAD
////                        decreaseMP.setObs(01L);//FIJO, ALM (MEMO)
////                        decreaseMP.setObsLog(30L);//OBS LOG MUESTRA MP
                        db.insertOrReplaceInTx(new DecreaseMP[]{decreaseMP});
                        Toast.makeText(mContext, "Guardado", Toast.LENGTH_LONG).show();
                        viewHolder.open = false;
                        viewHolder.llCaptureSheetDetail.setVisibility(View.GONE);
                        viewHolder.btnSaveDetail.setText("Cerrar");
                        aBoolean = true;
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        }else{
            viewHolder.llCaptureSheetDetail.setVisibility(View.GONE);
        }
        return aBoolean;
    }

    private boolean isValid(EditReceiptDecreaseRecyclerViewAdapter.ViewHolder viewHolder){
        Editable sampleText = viewHolder.txtSample.getText();
        if (TextUtils.isEmpty(sampleText)) {
            Toast.makeText(mContext, "Captura Merma", Toast.LENGTH_LONG).show();
            return false;
        }
        sample = Integer.parseInt(sampleText.toString().replace(",", ""));
        if (sample  < 1 ){
            Toast.makeText(mContext, "Merma En Cero", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //TODO OBTIENE INFORMACION PARA LLENAR TABLAS DE MERMA
    private void getArticle(String barcode, Callback callback){

        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.getArticle(barcode).enqueue(new Callback<VArticle>() {
            @Override
            public void onResponse(Call<VArticle> call, Response<VArticle> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<VArticle> call, Throwable t) {
                Toast.makeText(mContext, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

}
