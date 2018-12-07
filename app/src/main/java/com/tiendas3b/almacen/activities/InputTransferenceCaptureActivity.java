package com.tiendas3b.almacen.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.tiendas3b.almacen.adapters.InputTransferenceTableAdapter;
import com.tiendas3b.almacen.db.dao.InputTransference;
import com.tiendas3b.almacen.db.dao.InputTransferenceDetail;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.CaptureInputTransference;
import com.tiendas3b.almacen.dto.InputTransferenceDetailDTO;
import com.tiendas3b.almacen.dto.InputTransferenceVO;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.NetworkUtil;
import com.tiendas3b.almacen.util.PermissionUtil;
import com.tiendas3b.almacen.views.tables.InputTransferenceCaptureTableView;
import com.tiendas3b.almacen.views.tables.vo.InputTransferenceArticleVO;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dfa on 29/06/2016.
 */
public class InputTransferenceCaptureActivity extends AppCompatActivity {

    private static final String TAG = "InputTransferenceCA";
    private static final String ARG_INPUT_TRANS_ID = "inputTransferenceId";
    public static final String RESULT = "result";
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private ViewSwitcher viewSwitcher;
    private List<InputTransferenceArticleVO> items;
    //    private GeneralSwipeRefreshLayout swipeRefresh;
//    private SearchView searchView;
    //    private TransferenceOutputCaptureTableView tableView;
//    private TransferenceOutputTableAdapter adapter;
    private InputTransferenceCaptureTableView tableView;
    private InputTransferenceTableAdapter adapter;

    private long inputTransId;
    private boolean expand;
    private List<ObservationType> types;
    private List<ObservationType> obs;
    private List<ObservationType> obsLog;//obsevacion logistica
    private MenuItem btnSave;
    private ProgressBar progressBar;
    private InputTransference inputTransference;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_transference_input_capture, menu);

        btnSave = menu.findItem(R.id.action_capture_save);

//        final MenuItem item = menu.findItem(R.id.action_capture_search);
//        searchView = (SearchView) MenuItemCompat.getActionView(item);
////        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                if (items != null) {
//                    if (!expand && query.isEmpty()) {
//                        adapter = new DecreaseCaptureTableDataAdapter(mContext, new ArrayList<>(items), items, types, obs);
//                        tableView.setDataAdapter(adapter);
//                        expand = true;
//                    } else {
//                        if (query.length() > 2) {
//                            expand = false;
//                            final List<InputTransferenceArticleVO> filteredModelList = filter(query);
//                            adapter.setFilteredList(filteredModelList);
//                        }
//                    }
//                    tableView.scrollTo(0, 0);
//                }
//                return true;
//            }
//        });
//        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                // Do something when collapsed
//                expand = true;
//                return true;       // Return true to collapse action view
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
////                adapter.setLoaded();
//                return true;      // Return true to expand action view
//            }
//        });

        return true;
    }

//    private List<InputTransferenceArticleVO> filter(String query) {
//        query = query.toLowerCase();
//        final List<InputTransferenceArticleVO> filteredModelList = new ArrayList<>();
//        query = query.trim().toLowerCase();
//        for (InputTransferenceArticleVO model : items) {
//            final String textId = String.valueOf(model.getIclave());
//            final String textAndPrint = model.getDescription().toLowerCase();
//            if (textAndPrint.contains(query) || textId.contains(query)) {
//                filteredModelList.add(model);
//            }
//        }
//        return filteredModelList;
//    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//    }

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
            List<InputTransferenceArticleVO> toSend = new ArrayList<>();
            boolean valid = true;
            for (InputTransferenceArticleVO av : items) {
                int inv = av.getAmountInv() == null ? 0 : av.getAmountInv();
                int mer = av.getAmountMer() == null ? 0 : av.getAmountMer();
                int reg = av.getAmountReg() == null ? 0 : av.getAmountReg();
                if (inv + mer + reg == av.getAmountSmn()) {
                    toSend.add(av);
                } else{
                    valid = false;
                }
            }
            if(valid){
                send(toSend);
            } else {
                final Snackbar sb = Snackbar.make(tableView, R.string.transference_validate_error2, Snackbar.LENGTH_INDEFINITE);
                sb.setAction("Aceptar", v -> sb.dismiss()).show();
                btnSave.setVisible(true);
                progressBar.setVisibility(View.INVISIBLE);
            }
        } else {
            PermissionUtil.showDialog(this, PermissionUtil.REQUEST_WRITE_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void send(final List<InputTransferenceArticleVO> toSend) {
        EditText text = viewSwitcher.findViewById(R.id.txtCause);
        String cause = text.getText().toString();
        if (toSend.isEmpty() || cause.isEmpty()) {
            final Snackbar sb = Snackbar.make(viewSwitcher.findViewById(R.id.rlRoot), R.string.transference_validate_error, Snackbar.LENGTH_INDEFINITE);
            sb.setAction("Aceptar", v -> sb.dismiss()).show();
            btnSave.setVisible(true);
            progressBar.setVisibility(View.GONE);
        } else {
            final CaptureInputTransference captureInputTransference = new CaptureInputTransference();
            captureInputTransference.setToSend(convert(toSend));
            captureInputTransference.setCause(cause);
            InputTransferenceVO vo = new InputTransferenceVO();
            vo.setFolio(inputTransference.getFolio());
            vo.setRegionId(inputTransference.getRegionId());
            vo.setStoreId(inputTransference.getStoreId());
            vo.setDate(new SimpleDateFormat(DateUtil.YYYY_MM_DD, Locale.getDefault()).format(inputTransference.getDate()));
            captureInputTransference.setInputTransference(vo);
//            swipeRefresh.setRefreshing(true);
            Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
            httpService.insertInput(captureInputTransference).enqueue(new Callback<GeneralResponseDTO>() {
                @Override
                public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                    GeneralResponseDTO gr = response.body();
                    if (response.isSuccessful() && gr.getCode() > 0) {
                        deleteInputTransference();
                        startActivityForResult(new Intent(mContext, DecreaseReportActivity.class).putExtra("t", Constants.TE)
                                .putExtra("f", gr.getDescription()).putExtra("d", (Serializable) toSend), -1);
                        finish();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        btnSave.setVisible(true);
                        Log.e(TAG, "send error");
                    }
//                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    btnSave.setVisible(true);
//                    swipeRefresh.setRefreshing(false);
//                showEmptyView();
                    Log.e(TAG, "on failure!");
                }
            });
        }
    }

    private void deleteInputTransference() {
        databaseManager.deleteByInputTransferenceId(inputTransId);
        databaseManager.delete(InputTransference.class, inputTransference);
    }

    private List<InputTransferenceDetailDTO> convert(List<InputTransferenceArticleVO> list) {
        List<InputTransferenceDetailDTO> res = new ArrayList<>();
        for (InputTransferenceArticleVO a : list) {
            res.add(convert(a));
        }
        return res;
    }

    private InputTransferenceDetailDTO convert(InputTransferenceArticleVO a) {
        InputTransferenceDetailDTO articleDTO = new InputTransferenceDetailDTO();
        articleDTO.setIclave(Long.valueOf(a.getIclave()).intValue());
        articleDTO.setObs(a.getObs());
        articleDTO.setAmount(a.getAmount());
        articleDTO.setObsLog(a.getObsLog());
        articleDTO.setAmountInv(a.getAmountInv());
        articleDTO.setAmountMer(a.getAmountMer());
        articleDTO.setAmountReg(a.getAmountReg());
        articleDTO.setAmountSmn(a.getAmountSmn());
        articleDTO.setType(a.getType());
        articleDTO.setCost(a.getCost());
        return articleDTO;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (GlobalState) getApplicationContext();
        databaseManager = new DatabaseManager(mContext);
        setContentView(R.layout.activity_input_transference_capture);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inputTransId = getIntent().getLongExtra("t", -1);
        init();
    }

    private void init() {
        progressBar = findViewById(R.id.progress);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        viewSwitcher = findViewById(R.id.switcher);
        inputTransference = databaseManager.getById(inputTransId, InputTransference.class);
        setTitle((inputTransference.getStore() == null ? "N/A" : inputTransference.getStore().getName()) + getType());

        if (items == null) {
            getDatabaseInfo();
//            if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
//                downloadStatus();
//            }
        }
    }

    private void getDatabaseInfo() {
        createItems();
//        items = db.listViewArticlesTransference();
        if (items == null || items.isEmpty()) {
            if (NetworkUtil.isConnected(mContext)) {
                download();
            }
        } else {
            setAdapter();
            viewSwitcher.setDisplayedChild(1);
        }
    }

    private void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getInputTransferenceDetail(mContext.getRegion(), inputTransference.getStoreId(), inputTransference.getFolio(), new SimpleDateFormat(DateUtil.YYYY_MM_DD, Locale.getDefault()).format(inputTransference.getDate())).enqueue(new Callback<List<InputTransferenceDetail>>() {
            @Override
            public void onResponse(Call<List<InputTransferenceDetail>> call, Response<List<InputTransferenceDetail>> response) {
                if (response.isSuccessful()) {
                    fill(response.body());
                } else {
                    showEmptyView();
                }
//                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<InputTransferenceDetail>> call, Throwable t) {
//                swipeRefresh.setRefreshing(false);
                showEmptyView();
                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });
    }

    private void fill(List<InputTransferenceDetail> receiptSheets) {
        if (receiptSheets == null) {
            showEmptyView();
        } else {
            saveReceiptSheets(receiptSheets);
            createItems();
            setAdapter();
        }
        viewSwitcher.setDisplayedChild(1);
    }

    private void showEmptyView() {
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.GONE);
    }

    private void saveReceiptSheets(List<InputTransferenceDetail> list) {
        long itId = inputTransference.getId();
        for (InputTransferenceDetail itd : list) {
            itd.setInputTransferenceId(itId);
        }
        databaseManager.insertOrReplaceInTx(list.toArray(new InputTransferenceDetail[list.size()]));
    }

    @SuppressWarnings("unchecked")
    private void createItems() {
        Object obj = getLastCustomNonConfigurationInstance();
        if (obj == null) {
            long itId = inputTransference.getId();
            List<InputTransferenceDetail> vArticles = databaseManager.listInputTransferenceDetail(itId);
            if (vArticles != null && !vArticles.isEmpty()) {
                items = createItems(vArticles);
            }
        } else {
            items = (List<InputTransferenceArticleVO>) obj;
        }
    }

    private List<InputTransferenceArticleVO> createItems(@NonNull List<InputTransferenceDetail> vArticles) {
        List<InputTransferenceArticleVO> articlesVO = new ArrayList<>();
        for (InputTransferenceDetail a : vArticles) {
            InputTransferenceArticleVO aVO = new InputTransferenceArticleVO();
            Long iclave = a.getIclave();
            aVO.setIclave(iclave);
            VArticle art = databaseManager.findViewArticleByIclave(iclave);
            if(art == null) {
                Toast.makeText(mContext, "Sincroniza articulos", Toast.LENGTH_SHORT).show();
            } else {
                String desc = art.getDescription();
                desc = desc == null ? "No desc" : desc;
                aVO.setDescription(desc.concat(" / ").concat(art.getUnity()));
                aVO.setCost(art.getCost());
//            aVO.setAmountInv(art.getAmountInv());//cuando carga por eso deben ir vacíos
//            aVO.setAmountMer(art.getAmountMer());
//            aVO.setAmountReg(art.getAmountReg());
                aVO.setAmountSmn(a.getAmountSmn());
                articlesVO.add(aVO);
            }
        }
        return articlesVO;
    }

    private void setAdapter() {
        if(items == null){
            Toast.makeText(mContext, "Actualiza catálogos por favor", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            finish();
        } else {
            tableView = viewSwitcher.findViewById(R.id.list);
            types = databaseManager.listObservationType(4);
            obs = databaseManager.listObservationType(0);
            obsLog = databaseManager.listObservationType(12);//ver bot_relacion_razon_mov
            adapter = new InputTransferenceTableAdapter(this, new ArrayList<>(items), items, types, obs, obsLog);
//        swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
//            @Override
//            public boolean canChildScrollUp() {
//                return tableView.getChildAt(1).canScrollVertically(-1);
//            }
//        });
            tableView.setDataAdapter(adapter);
//        tableView.addDataClickListener(new ClickListener());
        }
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.VISIBLE);
    }

    public String getType() {
        switch (inputTransference.getType()){
            case 11:
                return "-RT";
            case 12:
                return "-RC";
            default:
                return "-PR";
        }
    }
}
