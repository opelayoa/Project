package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.fragments.BuysFragment;
import com.tiendas3b.almacen.util.Constants;

public class ODCsActivity extends AppCompatActivity implements BuysFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odcs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onFragmentInteraction(Buy buy) {
        //TODO aqui se debe hacer la verificacion de maestro detalle (por si quieren tablets)
        Intent intent = new Intent(ODCsActivity.this, BuyDetailActivity.class);
        intent.putExtra(Constants.EXTRA_BUY, buy.getId());
        startActivity(intent);
    }
}
