package com.tiendas3b.almacen.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dfa on 09/08/2016.
 */
public abstract class SaveMenuActivity extends AppCompatActivity{

    private Menu menu;
    MenuItem btnSave;
    @BindView(R.id.progress)
    ProgressBar progressBar;//debe existir en el layout
    protected GlobalState mContext;
    protected IDatabaseManager db;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_save_only, menu);
        btnSave = menu.findItem(R.id.action_capture_save);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_save:
                saveData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("ConstantConditions")
    protected void setup() {
        mContext = (GlobalState) getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DatabaseManager(this);
        ButterKnife.bind(this);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        init();
    }

    @Override
    protected void onRestart() {
        db = new DatabaseManager(mContext);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        if (db != null) db.closeDbConnections();
        super.onStop();
    }

    @Override
    protected void onResume() {
        db = DatabaseManager.getInstance(mContext);
        super.onResume();
    }

    protected void setResultOk() {
//        Intent data = new Intent();
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, null);
        } else {
            getParent().setResult(Activity.RESULT_OK, null);
        }
        finish();
    }

    protected void showExitWithoutSaveDialog(final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(getString(R.string.dialog_warning));
        alertDialogBuilder.setPositiveButton(getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SaveMenuActivity.super.onBackPressed();
                    }
                });

        alertDialogBuilder.setNegativeButton(android.R.string.cancel, null
        );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public Menu getMenu() {
        return menu;
    }

    protected abstract void saveData();
    protected abstract void init();
//    protected abstract boolean isValid();
//    protected abstract void showWarningDialog();
}
