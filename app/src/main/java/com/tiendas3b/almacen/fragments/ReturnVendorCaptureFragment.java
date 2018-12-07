package com.tiendas3b.almacen.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.ReturnVendorReportActivity;
import com.tiendas3b.almacen.adapters.ReturnVendorTableAdapter;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.CaptureReturnVendor;
import com.tiendas3b.almacen.dto.ReturnVendorCaptureDTO;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.NetworkUtil;
import com.tiendas3b.almacen.util.PermissionUtil;
import com.tiendas3b.almacen.views.tables.ReturnVendorCaptureTableView;
import com.tiendas3b.almacen.views.tables.vo.ReturnVendorArticleVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReturnVendorCaptureFragment extends Fragment {

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

    public ReturnVendorCaptureFragment() {
        // Required empty public constructor
    }

    public static ReturnVendorCaptureFragment newInstance(long vendorId) {
        ReturnVendorCaptureFragment fragment = new ReturnVendorCaptureFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_VENDOR, vendorId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_return_vendor_capture, menu);

        btnSave = menu.findItem(R.id.action_decrease_capture_save);

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
//                        adapter = new TransferenceOutputTableAdapter(getActivity(), new ArrayList<>(items), items, types, obs);
//                        tableView.setDataAdapter(adapter);
//                        expand = true;
//                    } else {
//                        if (query.length() > 2) {
//                            expand = false;
//                            final List<ArticleTransference> filteredModelList = filter(query);
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

    }

//    private List<ArticleTransference> filter(String query) {
//        query = query.toLowerCase();
//        final List<ArticleTransference> filteredModelList = new ArrayList<>();
//        query = query.trim().toLowerCase();
//        for (ArticleTransference model : items) {
//            final String textId = String.valueOf(model.getId());
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
//        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getActivity().findViewById(android.R.id.content).getWindowToken(), 0);
            List<ReturnVendorArticleVO> toSend = new ArrayList<>();
            for (ReturnVendorArticleVO av : items) {
                Integer a = av.getAmount();
                if (a != null && a != 0) {
                    toSend.add(av);
                }
            }
            send(toSend);
        } else {
            PermissionUtil.showDialog(getActivity(), PermissionUtil.REQUEST_WRITE_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
            Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
            final CaptureReturnVendor captureTransference = new CaptureReturnVendor();
            captureTransference.setToSend(convert(toSend));
            captureTransference.setWarehouse(mContext.getRegion());
            captureTransference.setCause(cause);
            captureTransference.setFolio(Integer.valueOf(folio));
//            swipeRefresh.setRefreshing(true);
            httpService.insertReturnVendor(captureTransference).enqueue(new Callback<GeneralResponseDTO>() {
                @Override
                public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                    GeneralResponseDTO gr = response.body();
                    if (response.isSuccessful() && gr != null && gr.getCode() == 1L) {
                        startActivity(new Intent(mContext, ReturnVendorReportActivity.class).putExtra(ReturnVendorReportActivity.EXTRA_DESC, gr.getDescription()).putExtra(ReturnVendorReportActivity.EXTRA_LIST, (Serializable) /*getIds(*/toSend/*)*/));
//                        Intent returnIntent = new Intent();
//                        returnIntent.putExtra(RESULT, R.string.succes_return_vendor);
//                        getActivity().setResult(Activity.RESULT_OK, returnIntent);
//                        getActivity().finish();
//                        mListener.success();//TODO
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

//    @Override
//    public Object onRetainCustomNonConfigurationInstance() {
//        return new ArrayList<>(items);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVendorId = getArguments().getLong(ARG_VENDOR, -1L);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (GlobalState) getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_return_vendor_capture, container, false);

        if (rootView instanceof ViewSwitcher) {
            viewSwitcher = ((ViewSwitcher) rootView);
            Animation slide_in_left = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            Animation slide_out_right = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right);
            viewSwitcher.setInAnimation(slide_in_left);
            viewSwitcher.setOutAnimation(slide_out_right);
        }

        databaseManager = new DatabaseManager(mContext);

        init();
        return rootView;
    }

    private void init() {

//        viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher);
//        swipeRefresh = (GeneralSwipeRefreshLayout) viewSwitcher.findViewById(R.id.swipeRefresh);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                downloadStatus();
//            }
//        });

        if (items == null) {
            getDatabaseInfo();
            if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
                download();
            }
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
        if(vArticles != null && !vArticles.isEmpty()) {
            getActivity().setTitle(vArticles.get(0).getProvider().getName());
            items = createItems(vArticles);
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

    private void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getReturnVendorDetail(mVendorId).enqueue(new Callback<List<VArticle>>() {//TODO bajar vfarticles por providerId
            @Override
            public void onResponse(Call<List<VArticle>> call, Response<List<VArticle>> response) {
                if (response.isSuccessful()) {
                    fill(response.body());
                } else {
//                    showEmptyView();
                }
//                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<VArticle>> call, Throwable t) {
//                swipeRefresh.setRefreshing(false);
//                showEmptyView();
                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });
    }

    private void fill(List<VArticle> list) {
        if (list == null) {
            showEmptyView();
        } else {
            save(list);
            createItems();
            setAdapter();
        }
        viewSwitcher.setDisplayedChild(1);
    }

    private void showEmptyView() {
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.GONE);
    }

    private void save(List<VArticle> list) {
//        for (VArticle a : list) {
//            a.setProviderId(mVendorId);
//        }
        databaseManager.insertOrReplaceInTx(list.toArray(new VArticle[list.size()]));
//        items = db.listViewArticles(mVendorId);
    }

    private void setAdapter() {
        tableView = (ReturnVendorCaptureTableView) viewSwitcher.findViewById(R.id.list);
        types = databaseManager.listObservationType(4);
        obs = databaseManager.listObservationType(3);
        adapter = new ReturnVendorTableAdapter(getActivity(), new ArrayList<>(items), items, types, obs);
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

//    private class ClickListener implements TableDataClickListener<ArticleTransference> {
//
//        @Override
//        public void onDataClicked(final int rowIndex, final ArticleTransference clickedData) {
//            final String carString = clickedData.getId() + " " + clickedData.getDescription();
//            Toast.makeText(mContext, carString, Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onDestroyView() {
        items = null;
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onDestroyView();
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
////            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(ArticleTransference articleTransference);
//    }
}
