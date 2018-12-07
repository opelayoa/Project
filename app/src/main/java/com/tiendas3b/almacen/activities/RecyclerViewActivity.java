package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.EqualsBase;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("ConstantConditions")
public abstract class RecyclerViewActivity extends AppCompatActivity {

    @BindView(R.id.switcher)
    ViewSwitcher viewSwitcher;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    View emptyView;
    protected GlobalState mContext;
    protected List<? extends EqualsBase> items;
    protected IDatabaseManager db;

    @Override
    protected void onRestart() {
        db = new DatabaseManager(this);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        //no se todavia si poner los items = null...
        if (db != null) db.closeDbConnections();
        super.onStop();
    }

    @Override
    protected void onResume() {
        db = DatabaseManager.getInstance(this);
        super.onResume();
    }

    protected void init() {
        ButterKnife.bind(this);
        db = DatabaseManager.getInstance(this);
        if (items == null) {
            getDatabaseInfo();
        }
    }

    protected void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == getRequestCode()) {
            if (resultCode == RESULT_OK) {
                getDatabaseInfo();
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    public abstract int getRequestCode();
    protected abstract void getDatabaseInfo();
    protected abstract void setAdapter();
}
