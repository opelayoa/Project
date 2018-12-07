package com.tiendas3b.almacen.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.ReceiptComments;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.dto.receipt.MmpReciboExpressAsnComentariosDTO;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.receipt.InsertReceiptExpressLog;
import com.tiendas3b.almacen.receipt.edit.ReceiptEditTimetableActivity;
import com.tiendas3b.almacen.util.Constants;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditReceiptComments extends AppCompatActivity {

    private Buy item;
    private DatabaseManager db;
    private GlobalState mContext;
    private Tiendas3bClient httpService;
    private static final String TAG = EditReceiptComments.class.getName();

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
    @BindView(R.id.txtComments)
    EditText txtComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comentarios);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ButterKnife.bind(this);
        init();
        toolbar.setTitle("COMENTARIOS");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.btnSaveComments)
    public void saveComments() {
        //TODO INSERTA EN TABLA LOCAL
        Date date = new Date();
        String comments = txtComments.getText().toString();
        ReceiptComments receiptComments = new ReceiptComments(mContext.getRegion(), date, item.getProviderId(), item.getFolio(), 20, comments);
        db.insert(receiptComments);

        MmpReciboExpressAsnComentariosDTO comentariosDTO = new MmpReciboExpressAsnComentariosDTO(String.valueOf(mContext.getRegion()),item.getProviderId(),item.getFolio(),
                                                                                                0,0,txtComments.getText().toString());

        insertLog(comentariosDTO, new Callback(){
            @Override
            public void onResponse(Call call, Response response) {

                GeneralResponseDTO responseDTO = (GeneralResponseDTO) response.body();

                String mensaje;

                if(responseDTO.getCode() == 1){
                    mensaje = "Guardado";
                }else{
                    mensaje = "Ya existe un comentario \npara este proveedor";
                }

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditReceiptComments.this);
                alertDialogBuilder.setMessage(mensaje);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton(getString(android.R.string.ok),
                        (dialog, which) -> {
                            startActivity(new Intent(EditReceiptComments.this, ReceiptEditTimetableActivity.class).putExtra(ReceiptEditTimetableActivity.EXTRA_ORDER_TYPE, 0));
                            finish();
                        }
                );
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    }

    protected void init() {
        mContext = (GlobalState) getApplicationContext();
        httpService = ((GlobalState) getApplicationContext()).getHttpServiceWithAuthTime();
        db = new DatabaseManager(mContext);
        Intent intent = getIntent();
        Long buyId = intent.getLongExtra(Constants.EXTRA_BUY, 0L);
        item = db.getById(buyId, Buy.class);

        txtProvider.setText(item.getProvider().getName());
        txtFolio.setText(String.valueOf(item.getFolio()));
        txtPlatform.setText(item.getPlatform().toString());
        txtTime.setText(item.getTime());
        txtDeliveryTime.setText(item.getDeliveryTime());
        txtComments.setText("");
    }



    private void insertLog(MmpReciboExpressAsnComentariosDTO comentariosDTO, Callback callback) {
        httpService.insertComment(comentariosDTO).enqueue(new Callback<GeneralResponseDTO>() {
            @Override
            public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, ReceiptEditTimetableActivity.class).putExtra(ReceiptEditTimetableActivity.EXTRA_ORDER_TYPE, 0));
        finish();
    }

}
