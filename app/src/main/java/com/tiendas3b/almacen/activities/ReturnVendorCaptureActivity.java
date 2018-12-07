package com.tiendas3b.almacen.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.ReturnVendorTableAdapter;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.CaptureReturnVendor;
import com.tiendas3b.almacen.dto.ReturnVendorCaptureDTO;
import com.tiendas3b.almacen.fragments.ReturnVendorCaptureFragment;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.PermissionUtil;
import com.tiendas3b.almacen.views.tables.ReturnVendorCaptureTableView;
import com.tiendas3b.almacen.views.tables.vo.ReturnVendorArticleVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReturnVendorCaptureActivity extends AppCompatActivity {

    private static final String TAG = ReturnVendorCaptureFragment.class.getSimpleName();
    private static final String ARG_VENDOR = "vendorId";
    public static final String RESULT = "result";
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private ViewSwitcher viewSwitcher;
    private List<ReturnVendorArticleVO> items;
    private ReturnVendorCaptureTableView tableView;
    //    private GeneralSwipeRefreshLayout swipeRefresh;
//    private SearchView searchView;
    private ReturnVendorTableAdapter adapter;

    //    private OnFragmentInteractionListener mListener;
    private long mVendorId;
    //    private boolean expand;
    private List<ObservationType> types;
    private List<ObservationType> obs;
    private MenuItem btnSave;
    private ProgressBar progressBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_return_vendor_capture, menu);
        btnSave = menu.findItem(R.id.action_capture_save);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_capture_save:
                saveData();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveData() {
        if (PermissionUtil.permissionGranted(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            btnSave.setVisible(false);
            progressBar.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(tableView.getWindowToken(), 0);
            List<ReturnVendorArticleVO> toSend = new ArrayList<>();
            for (ReturnVendorArticleVO av : items) {
                Integer a = av.getAmount();
                if (a != null && a != 0) {
                    toSend.add(av);
                }
            }
            send(toSend);
        } else {
            PermissionUtil.showDialog(this, PermissionUtil.REQUEST_WRITE_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

//        List<ReturnVendorCaptureDTO> toSend = new ArrayList<>();
//        for (VArticle av : items) {
////            Integer a = av.getAmount();
////            if (a != null && a != 0) {
////                ReturnVendorCaptureDTO o = new ReturnVendorCaptureDTO();
////                o.setIclave(Long.valueOf(av.getIclave()).intValue());
////                o.setBarcode(av.getBarcode());
////                o.setDescription(av.getDescription());
////                o.setObs(av.getObs());
////                o.setPacking(av.getPacking());
////                o.setType(av.getType());
////                o.setUnity(av.getUnity());
////                o.setProviderId(av.getProviderId());
////                o.setIvaId(av.getIvaId());
////                o.setAmount(a);
////                toSend.add(o);
////            }
//        }
//        send(toSend);
    }

    private void send(final List<ReturnVendorArticleVO> toSend) {
        EditText text = (EditText) viewSwitcher.findViewById(R.id.txtCause);
        String cause = text.getText().toString();
        EditText txtFolio = (EditText) viewSwitcher.findViewById(R.id.txtFolio);
        String folio = txtFolio.getText().toString();
        if (toSend.isEmpty() || cause.isEmpty() || folio.isEmpty()) {
            Snackbar.make(tableView, "No ha ingresado datos", Snackbar.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
            btnSave.setVisible(true);
//            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.transference_validate_error, Snackbar.LENGTH_LONG).show();
        } else {
            final CaptureReturnVendor captureTransference = new CaptureReturnVendor();
            captureTransference.setToSend(convert(toSend));
            captureTransference.setWarehouse(mContext.getRegion());
            captureTransference.setCause(cause);
            captureTransference.setFolio(Integer.valueOf(folio));
//            swipeRefresh.setRefreshing(true);
            Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
            httpService.insertReturnVendor(captureTransference).enqueue(new Callback<GeneralResponseDTO>() {
                @Override
                public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                    GeneralResponseDTO gr = response.body();
                    if (response.isSuccessful() && gr != null && gr.getCode() == 1L) {
                        progressBar.setVisibility(View.INVISIBLE);
                        btnSave.setVisible(true);
                        startActivityForResult(new Intent(mContext, DecreaseReportActivity.class).putExtra("t", Constants.DP)
                                .putExtra("f", gr.getDescription()).putExtra("d", (Serializable) toSend), -1);
                        finish();
                    } else {
//                    showEmptyView();
//                        Snackbar.make(tableView, gr.getDescription(), Snackbar.LENGTH_INDEFINITE).show();
                        Log.e(TAG, "send error!");
                    }
//                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
//                    swipeRefresh.setRefreshing(false);
//                showEmptyView();
                    Log.e(TAG, "send error!");
                }
            });
        }
    }

    private List<ReturnVendorCaptureDTO> convert(List<ReturnVendorArticleVO> list) {
        List<ReturnVendorCaptureDTO> res = new ArrayList<>();
        for (ReturnVendorArticleVO a : list) {
            res.add(convert(a));
        }
        return res;
    }

    private ReturnVendorCaptureDTO convert(ReturnVendorArticleVO a) {
        ReturnVendorCaptureDTO articleDTO = new ReturnVendorCaptureDTO();
        articleDTO.setIclave(Long.valueOf(a.getIclave()).intValue());
        articleDTO.setObs(a.getObs());
        articleDTO.setAmount(a.getAmount());
        articleDTO.setType(a.getType());
        articleDTO.setProviderId(a.getProviderId());
        articleDTO.setIvaId(a.getIvaId());
        return articleDTO;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (GlobalState) getApplicationContext();
        databaseManager = new DatabaseManager(mContext);
        setContentView(R.layout.activity_return_vendor_capture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mVendorId = ((Provider) getIntent().getSerializableExtra("p")).getId();
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

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher);
//            Animation slide_in_left = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
//            Animation slide_out_right = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right);
//            viewSwitcher.setInAnimation(slide_in_left);
//            viewSwitcher.setOutAnimation(slide_out_right);

        databaseManager = new DatabaseManager(mContext);

        if (items == null) {
            getDatabaseInfo();
//            if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
//                downloadStatus();
//            }
        }
    }

    private void getDatabaseInfo() {
        createItems();
//        items = db.listViewArticles(mVendorId);
        if (items == null || items.isEmpty()) {
//            swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
//                @Override
//                public boolean canChildScrollUp() {
//                    return false;
//                }
//            });
        } else {
            setAdapter();
            viewSwitcher.setDisplayedChild(1);
        }
    }

    @SuppressWarnings("unchecked")
    private void createItems() {
//        Object obj = getLastCustomNonConfigurationInstance();
//        if (obj == null) {
        List<VArticle> vArticles = databaseManager.listViewArticles(mVendorId);
        if (vArticles != null && !vArticles.isEmpty()) {
            setTitle(vArticles.get(0).getProvider().getName());
            items = createItems(vArticles);
        } else {
            Toast.makeText(mContext, R.string.refresh_catalogs, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            finish();
        }
//        } else {
//            items = (List<ReceiptSheetDetPrintDTO>) obj;
//        }
    }

    private List<ReturnVendorArticleVO> createItems(@NonNull List<VArticle> vArticles) {
        List<ReturnVendorArticleVO> articlesVO = new ArrayList<>();
        for (VArticle a : vArticles) {
            ReturnVendorArticleVO aVO = new ReturnVendorArticleVO();
            aVO.setIclave(a.getIclave());
            aVO.setDescription(a.getDescription().concat(" / ").concat(a.getUnity()));
            aVO.setCost(a.getCost());
            aVO.setIvaId(a.getIvaId());
            aVO.setProviderId(a.getProviderId());
            articlesVO.add(aVO);
        }
        return articlesVO;
    }

//    private void downloadStatus() {
//        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
//        httpService.getReturnVendorDetail(mVendorId).enqueue(new Callback<List<VArticle>>() {//TODO bajar vfarticles por providerId
//            @Override
//            public void onResponse(Call<List<VArticle>> call, Response<List<VArticle>> response) {
//                if (response.isSuccessful()) {
//                    fill(response.body());
//                } else {
////                    showEmptyView();
//                }
////                swipeRefresh.setRefreshing(false);
//            }
//
//            @Override
//            public void onFailure(Call<List<VArticle>> call, Throwable t) {
////                swipeRefresh.setRefreshing(false);
////                showEmptyView();
//                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
//            }
//        });
//    }
//
//    private void fill(List<VArticle> list) {
//        if (list == null) {
//            showEmptyView();
//        } else {
//            save(list);
//            createItems();
//            setAdapter();
//        }
//        viewSwitcher.setDisplayedChild(1);
//    }

    private void showEmptyView() {
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.GONE);
    }
//
//    private void save(List<VArticle> list) {
////        for (VArticle a : list) {
////            a.setProviderId(mVendorId);
////        }
//        db.insertOrReplaceInTx(list.toArray(new VArticle[list.size()]));
////        items = db.listViewArticles(mVendorId);
//    }

    private void setAdapter() {
        tableView = (ReturnVendorCaptureTableView) viewSwitcher.findViewById(R.id.list);
        types = databaseManager.listObservationType(4);
        obs = databaseManager.listObservationType(3);
        adapter = new ReturnVendorTableAdapter(this, new ArrayList<>(items), items, types, obs);
//        swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
//            @Override
//            public boolean canChildScrollUp() {
//                return tableView.getChildAt(1).canScrollVertically(-1);
//            }
//        });
        tableView.setDataAdapter(adapter);
//        tableView.addDataClickListener(new ClickListener());
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.VISIBLE);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return new ArrayList<>(items);
    }


    @Override
    public void onBackPressed() {
        showWarningDialog();
    }

    private void showWarningDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReturnVendorCaptureActivity.this);
        alertDialogBuilder.setMessage(getString(R.string.dialog_warning));
        alertDialogBuilder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                ReturnVendorCaptureActivity.super.onBackPressed();
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
