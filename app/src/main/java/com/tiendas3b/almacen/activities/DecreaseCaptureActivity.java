package com.tiendas3b.almacen.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.DecreaseCaptureTableDataAdapter;
import com.tiendas3b.almacen.db.dao.ArticleDecrease;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.ArticleCaptureDTO;
import com.tiendas3b.almacen.dto.CaptureDecrease;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DialogUtil;
import com.tiendas3b.almacen.util.NetworkUtil;
import com.tiendas3b.almacen.util.PermissionUtil;
import com.tiendas3b.almacen.views.tables.DecreaseCaptureTableView;
import com.tiendas3b.almacen.views.tables.vo.ArticleVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DecreaseCaptureActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = DecreaseCaptureActivity.class.getSimpleName();
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private ViewSwitcher viewSwitcher;
    private List<ArticleVO> items;
    private SearchView searchView;
    //    private boolean isExpanded;
    private DecreaseCaptureTableView tableView;
    private DecreaseCaptureTableDataAdapter adapter;
    private List<ObservationType> types;
    private List<ObservationType> obs;
    private List<ObservationType> obsLog;//obsevacion logistica
    private ProgressBar progressBar;
    private MenuItem btnSave;
    private boolean haveToReset;
//    private MenuItem sv;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (GlobalState) getApplicationContext();
        databaseManager = new DatabaseManager(mContext);
        setContentView(R.layout.activity_decrease_capture);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    protected void onRestart() {
        databaseManager = new DatabaseManager(this);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onStop();
    }

    @Override
    protected void onResume() {
        databaseManager = DatabaseManager.getInstance(this);
        super.onResume();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return new ArrayList<>(items);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_decrease_capture, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        btnSave = menu.findItem(R.id.action_decrease_capture_save);

        final MenuItem item = menu.findItem(R.id.action_decrease_capture_search);
//        sv = item;
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this
//                new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                Log.i(TAG, "onquerytextchange1:" + query +"|");
//                if (items != null) {
//                    if (haveToReset && query.isEmpty()) {
//                        adapter = new DecreaseCaptureTableDataAdapter(DecreaseCaptureActivity.this, new ArrayList<>(items), items, types, obs);
//                        tableView.setDataAdapter(adapter);
//                        tableView.scrollTo(0, 0);
//                        haveToReset = false;
//                    } else {
//                        if (query.length() > 2) {
//                            haveToReset = true;
//                            final List<ReceiptSheetDetPrintDTO> filteredModelList = filter(query);
//                            adapter.setFilteredList(filteredModelList);
//                            tableView.scrollTo(0, 0);
//                        }
//                    }
//                }
//                return true;
//            }
//        }
        );
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
//                isExpanded = true;
                return true;// Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
//                isExpanded = false;
                return true;// Return true to isExpanded action view
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    private List<ArticleVO> filter(String query) {
        query = query.toLowerCase();
        final List<ArticleVO> filteredModelList = new ArrayList<>();
        query = query.trim().toLowerCase();
        for (ArticleVO model : items) {
            final String textId = String.valueOf(model.getIclave());
            final String text = model.getDescription().toLowerCase();
            if (text.contains(query) || textId.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_decrease_capture_save:
                //TODO https://code.google.com/p/android/issues/detail?id=192357 - "Surface: getSlotFromBufferLocked: unknown buffer"
                saveData();
                return true;
            case R.id.action_decrease_scan:
                findByBarcode();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void findByBarcode() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.setPackage("com.google.zxing.client.android");
//                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");//SCAN_MODE
//                intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A,QR_CODE");
            startActivityForResult(intent, 0);
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.zxing.client.android")));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        databaseManager = DatabaseManager.getInstance(mContext);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                findByBarcodeResult(contents);
            } /*else if (resultCode == RESULT_CANCELED) {

            }*/
        }
    }

    private void findByBarcodeResult(String contents) {
        VArticle a = databaseManager.findViewArticleByBarcode(contents);
        if (a != null) {
            contents = String.valueOf(a.getIclave());
        }
//        if(!isExpanded){//hace que ejecute 2 veces el onQueryTextChange y por eso tronaba
//            searchView.setOnQueryTextListener(null);
//            sv.expandActionView();
//        }
//        searchView.setOnQueryTextListener(this);
        searchView.setQuery(contents, false);
    }

    private void saveData() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tableView.getWindowToken(), 0);
        if (NetworkUtil.isConnectedToWifi(this)) {
            if (PermissionUtil.permissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                btnSave.setVisible(false);
                progressBar.setVisibility(View.VISIBLE);
                List<ArticleVO> toSend = new ArrayList<>();
                for (ArticleVO av : items) {
                    Integer a = av.getAmount();
                    if (a != null && a != 0) {
                        toSend.add(av);
                    }
                }
                send(toSend);
            } else {
                PermissionUtil.showDialog(this, PermissionUtil.REQUEST_WRITE_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        } else {
            Snackbar.make(tableView, getString(R.string.no_wifi_connection), Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    private void send(final List<ArticleVO> toSend) {
        if (toSend.isEmpty()) {
            Snackbar.make(tableView, "No ha ingresado datos", Snackbar.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
            btnSave.setVisible(true);
        } else {
            Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
            final CaptureDecrease captureDecrease = new CaptureDecrease();
            captureDecrease.setToSend(convert(toSend));
            captureDecrease.setWarehouse(mContext.getRegion());
            httpService.insertDecrease(captureDecrease).enqueue(new Callback<GeneralResponseDTO>() {
                @Override
                public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                    GeneralResponseDTO gr = response.body();
                    if (response.isSuccessful() && gr.getCode() == 1L) {
                        progressBar.setVisibility(View.INVISIBLE);
                        btnSave.setVisible(true);

                        showFolio(gr.getDescription(), (Serializable) toSend);

//                        startActivityForResult(new Intent(mContext, DecreaseReportActivity.class).putExtra("t", Constants.ME)
//                                .putExtra("f", gr.getDescription()).putExtra("d", (Serializable) toSend), -1);
//                        finish();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        btnSave.setVisible(true);
                        String message = gr == null ? "Error de comunicación" : gr.getDescription();
                        Snackbar.make(tableView, message, Snackbar.LENGTH_INDEFINITE).show();
                        Log.e(TAG, "send error!");
                    }
                }

                @Override
                public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    btnSave.setVisible(true);
//                    if(t instanceof ConnectException){
//                        ((ConnectException) t).get
//                    }
                    Snackbar.make(tableView, t.getLocalizedMessage(), Snackbar.LENGTH_INDEFINITE).show();
                    Log.e(TAG, "on failure!");
                }
            });
        }
    }

    private void showFolio(final String folio, final Serializable data) {
        DialogUtil.showAlertDialog(this, "Folio ME: " + folio, "Ver PDF", "Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivityForResult(new Intent(mContext, DecreaseReportActivity.class).putExtra("t", Constants.ME)
                        .putExtra("f", folio).putExtra("d", data), -1);
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
    }

    private List<ArticleCaptureDTO> convert(List<ArticleVO> list) {
        List<ArticleCaptureDTO> res = new ArrayList<>();
        for (ArticleVO a : list) {
            res.add(convert(a));
        }
        return res;
    }

    private ArticleCaptureDTO convert(ArticleVO a) {
        ArticleCaptureDTO articleDTO = new ArticleCaptureDTO();
        articleDTO.setIclave(Long.valueOf(a.getIclave()).intValue());
        articleDTO.setObs(a.getObs());
        articleDTO.setObsLog(a.getObsLog());
        articleDTO.setAmount(a.getAmount());
        articleDTO.setType(a.getType());
        return articleDTO;
    }

//    private List<Long> getIds(List<VArticle> toSend) {
//        List<Long> ids = new ArrayList<>();
//        for (VArticle a : toSend) {
//            ids.add(a.getId());
//        }
//        return ids;
//    }

    private void init() {
        databaseManager = DatabaseManager.getInstance(mContext);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher);
        if (items == null) {
            getDatabaseInfo();
        }
    }

    private void getDatabaseInfo() {
        createItems();
        if (items == null || items.isEmpty()) {
            if (NetworkUtil.isConnected(mContext)) {
                download();
            }
        } else {
            setAdapter();
            viewSwitcher.setDisplayedChild(1);
        }
    }

    @SuppressWarnings("unchecked")
    private void createItems() {
        Object obj = getLastCustomNonConfigurationInstance();
        if (obj == null) {
            List<VArticle> vArticles = databaseManager.listViewArticlesDecrease();
            items = createItems(vArticles);
        } else {
            items = (List<ArticleVO>) obj;
        }
    }

    private List<ArticleVO> createItems(@NonNull List<VArticle> vArticles) {
        List<ArticleVO> articlesVO = new ArrayList<>();
        for (VArticle a : vArticles) {
            ArticleVO aVO = new ArticleVO();
            aVO.setIclave(a.getIclave());
            aVO.setDescription(a.getDescription().concat(" / ").concat(a.getUnity()));
            aVO.setCost(a.getCost());
            articlesVO.add(aVO);
        }
        return articlesVO;
    }

    private void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getViewArticlesDecrease(mContext.getRegion()).enqueue(new Callback<List<ArticleDecrease>>() {
            @Override
            public void onResponse(Call<List<ArticleDecrease>> call, Response<List<ArticleDecrease>> response) {
                if (response.isSuccessful()) {
                    fill(response.body());
                } else {
                    showEmptyView();
                }
            }

            @Override
            public void onFailure(Call<List<ArticleDecrease>> call, Throwable t) {
                showEmptyView();
                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });
    }

    private void showEmptyView() {
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.GONE);
    }

    private void fill(List<ArticleDecrease> list) {
        if (list == null) {
            showEmptyView();
        } else {
            save(list);
            createItems();
            setAdapter();
        }
        viewSwitcher.setDisplayedChild(1);
    }

    private void save(List<ArticleDecrease> list) {
        databaseManager.insertOrReplaceInTx(list.toArray(new ArticleDecrease[list.size()]));
    }

    private void setAdapter() {
        tableView = findViewById(R.id.list);
        types = databaseManager.listObservationType(4);
        obs = databaseManager.listObservationType(0);
        obsLog = databaseManager.listObservationType(6);
        if (types == null || obsLog == null || obs == null) {
            Toast.makeText(mContext, "Actualiza catálogos por favor", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(mContext, SettingsActivity.class);
            mContext.startActivity(intent);
            finish();
        } else {
            adapter = new DecreaseCaptureTableDataAdapter(this, new ArrayList<>(items), items, types, obs, obsLog);
            tableView.setDataAdapter(adapter);
//        tableView.addDataClickListener(new ClickListener());
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
            viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Log.i(TAG, "onquerytextchange1:" + query + "|");
        if (items != null) {
            if (haveToReset && query.isEmpty()) {
                Log.i(TAG, "haveToReset");
                adapter = new DecreaseCaptureTableDataAdapter(DecreaseCaptureActivity.this, new ArrayList<>(items), items, types, obs, obsLog);
                tableView.setDataAdapter(adapter);
                tableView.scrollTo(0, 0);
                haveToReset = false;
            } else {
                if (query.length() > 2) {
                    Log.i(TAG, "length() > 2");
                    haveToReset = true;
                    final List<ArticleVO> filteredModelList = filter(query);
                    adapter.setFilteredList(filteredModelList);
                    tableView.scrollTo(0, 0);
                }
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        showWarningDialog();
    }

    private void showWarningDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DecreaseCaptureActivity.this);
        alertDialogBuilder.setMessage(getString(R.string.dialog_warning));
        alertDialogBuilder.setPositiveButton(getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        DecreaseCaptureActivity.super.onBackPressed();
                    }
                });

        alertDialogBuilder.setNegativeButton(android.R.string.cancel, null
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//
//                    }
//                }
        );

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //    private class ClickListener implements TableDataClickListener<VArticle> {
//
//        @Override
//        public void onDataClicked(final int rowIndex, final VArticle clickedData) {
//            final String carString = clickedData.getIclave() + " " + clickedData.getDescription();
//            Toast.makeText(DecreaseCaptureActivity.this, carString, Toast.LENGTH_SHORT).show();
//        }
//    }

}
