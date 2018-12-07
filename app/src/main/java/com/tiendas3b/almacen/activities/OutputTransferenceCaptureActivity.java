package com.tiendas3b.almacen.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.DecreaseCaptureTableDataAdapter;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.ArticleCaptureDTO;
import com.tiendas3b.almacen.dto.CaptureTransference;
import com.tiendas3b.almacen.fragments.TransferenceOutputFragment;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.PermissionUtil;
import com.tiendas3b.almacen.views.tables.DecreaseCaptureTableView;
import com.tiendas3b.almacen.views.tables.vo.ArticleVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dfa on 28/06/2016.
 */
public class OutputTransferenceCaptureActivity extends AppCompatActivity {

    private static final String TAG = TransferenceOutputFragment.class.getSimpleName();
    private static final String ARG_STORE = "storeId";
    public static final String RESULT = "result";
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private ViewSwitcher viewSwitcher;
    private List<ArticleVO> items;
    //    private GeneralSwipeRefreshLayout swipeRefresh;
    private SearchView searchView;
    //    private TransferenceOutputCaptureTableView tableView;
//    private TransferenceOutputTableAdapter adapter;
    private DecreaseCaptureTableView tableView;
    private DecreaseCaptureTableDataAdapter adapter;

    private long mStoreId;
    private boolean expand;
    private List<ObservationType> types;
    private List<ObservationType> obs;
    private List<ObservationType> obsLog;
    private MenuItem btnSave;
    private ProgressBar progressBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_transference_output_capture, menu);

        btnSave = menu.findItem(R.id.action_capture_save);

        final MenuItem item = menu.findItem(R.id.action_capture_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
//        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (items != null) {
                    if (!expand && query.isEmpty()) {
                        adapter = new DecreaseCaptureTableDataAdapter(mContext, new ArrayList<>(items), items, types, obs, obsLog);
                        tableView.setDataAdapter(adapter);
                        expand = true;
                    } else {
                        if (query.length() > 2) {
                            expand = false;
                            final List<ArticleVO> filteredModelList = filter(query);
                            adapter.setFilteredList(filteredModelList);
                        }
                    }
                    tableView.scrollTo(0, 0);
                }
                return true;
            }
        });
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when collapsed
                expand = true;
                return true;       // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
//                adapter.setLoaded();
                return true;      // Return true to expand action view
            }
        });

        return true;
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
    }

    private void send(final List<ArticleVO> toSend) {
        EditText text = (EditText) viewSwitcher.findViewById(R.id.txtCause);
        String cause = text.getText().toString();
        if (toSend.isEmpty() || cause.isEmpty()) {
            Snackbar.make(tableView, R.string.transference_validate_error, Snackbar.LENGTH_LONG).show();
        } else {
            Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
            final CaptureTransference captureTransference = new CaptureTransference();
            captureTransference.setToSend(convert(toSend));
            captureTransference.setWarehouseId(mContext.getRegion());
            captureTransference.setStoreId(mStoreId);
            captureTransference.setCause(cause);
//            swipeRefresh.setRefreshing(true);
            httpService.insertTransferenceOutput(captureTransference).enqueue(new Callback<GeneralResponseDTO>() {
                @Override
                public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                    GeneralResponseDTO gr = response.body();
                    if (response.isSuccessful() && gr.getCode() == 1L) {
//                        progressBar.setVisibility(View.INVISIBLE);
//                        btnSave.setVisible(true);
                        startActivityForResult(new Intent(mContext, DecreaseReportActivity.class).putExtra("t", Constants.TA)
                                .putExtra("f", gr.getDescription()).putExtra("d", (Serializable) toSend), -1);
                        finish();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        btnSave.setVisible(true);
//                    showEmptyView();
//                        Snackbar.make(tableView, gr.getDescription(), Snackbar.LENGTH_INDEFINITE).show();//TODO
                        Log.e(TAG, "send error!");
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
        articleDTO.setAmount(a.getAmount());
        articleDTO.setType(a.getType());
        return articleDTO;
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
        setContentView(R.layout.activity_output_transference_capture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mStoreId = ((Store) getIntent().getSerializableExtra("p")).getId();
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
        if (items.isEmpty()) {
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
        Object obj = getLastCustomNonConfigurationInstance();
        if (obj == null) {
            List<VArticle> vArticles = databaseManager.listViewArticlesTransference();
            if (vArticles != null && !vArticles.isEmpty()) {
                items = createItems(vArticles);
            }
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

    private void setAdapter() {
        tableView = (DecreaseCaptureTableView) viewSwitcher.findViewById(R.id.list);
        types = databaseManager.listObservationType(4);
        obs = databaseManager.listObservationType(3);
        obsLog = databaseManager.listActiveObservationType();
        adapter = new DecreaseCaptureTableDataAdapter(this, new ArrayList<>(items), items, types, obs,obsLog);
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


}
