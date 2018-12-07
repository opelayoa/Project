package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.EditReceiptBoxesRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.fragments.EditBoxesFragment;
import com.tiendas3b.almacen.receipt.edit.ReceiptEditTimetableActivity;
import com.tiendas3b.almacen.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditReceiptBoxes extends AppCompatActivity implements EditBoxesFragment.OnFragmentInteractionListener{

    private final String TAG = getClass().getName();
    private DatabaseManager db;
    private GlobalState mContext;
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
        setContentView(R.layout.activity_edit_boxes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ButterKnife.bind(this);
        toolbar.setTitle("CANTIDADES");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    public void init(){
        mContext = (GlobalState) getApplicationContext();
        db = new DatabaseManager(mContext);
//        db.truncate(DecreaseMP.class);

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
    public void onFragmentInteraction(EditReceiptBoxesRecyclerViewAdapter.ViewHolder holder, int position) {
        EditBoxesFragment articleFrag = (EditBoxesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (articleFrag != null) {
            articleFrag.collapseAll(holder.llCaptureSheetDetail);
        }
    }

    @Override
    public boolean onSaveDetail(EditReceiptBoxesRecyclerViewAdapter.ViewHolder viewHolder) {
        return false;
    }
}
