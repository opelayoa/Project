package com.tiendas3b.almacen.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
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
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.InputTransferenceTableAdapter;
import com.tiendas3b.almacen.db.dao.InputTransference;
import com.tiendas3b.almacen.db.dao.InputTransferenceDetail;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.CaptureInputTransference;
import com.tiendas3b.almacen.dto.InputTransferenceDetailDTO;
import com.tiendas3b.almacen.dto.InputTransferenceVO;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.NetworkUtil;
import com.tiendas3b.almacen.views.tables.InputTransferenceCaptureTableView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputTransferenceCaptureFragment extends Fragment {

    private static final String TAG = InputTransferenceCaptureFragment.class.getSimpleName();
    private static final String ARG_INPUT_TRANS_ID = "inputTransferenceId";
    public static final String RESULT = "result";
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private ViewSwitcher viewSwitcher;
    private List<InputTransferenceDetail> items;
    private InputTransferenceCaptureTableView tableView;
    //    private GeneralSwipeRefreshLayout swipeRefresh;
    private SearchView searchView;
    private InputTransferenceTableAdapter adapter;

    //    private OnFragmentInteractionListener mListener;
    private long inputTransId;
    private boolean expand;
    private List<ObservationType> types;
    private List<ObservationType> obs;
    private InputTransference inputTransference;

    public InputTransferenceCaptureFragment() {
        // Required empty public constructor
    }

    public static InputTransferenceCaptureFragment newInstance(long inputTransferenceId) {
        InputTransferenceCaptureFragment fragment = new InputTransferenceCaptureFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_INPUT_TRANS_ID, inputTransferenceId);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Snackbar.make(getView(), "Snackbar", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_transference_input_capture, menu);

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
//
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
//
////    @Override
////    public void onPrepareOptionsMenu(Menu menu) {
////        super.onPrepareOptionsMenu(menu);
////    }

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

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void saveData() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tableView.getWindowToken(), 0);
        List<InputTransferenceDetailDTO> toSend = new ArrayList<>();
        boolean valid = true;
//        for (InputTransferenceDetail av : items) {
//            int inv = av.getAmountInv() == null ? 0 : av.getAmountInv();
//            int mer = av.getAmountMer() == null ? 0 : av.getAmountMer();
//            int reg = av.getAmountReg() == null ? 0 : av.getAmountReg();
//            if (inv + mer + reg == av.getAmountSmn()) {
//                InputTransferenceDetailDTO o = new InputTransferenceDetailDTO();
////                o.setId(av.getIclave());
////                o.setAmountInv(av.getAmountInv());
////                o.setAmountSmn(av.getAmountSmn());
////                o.setAmountMer(av.getAmountMer());
////                o.setAmountReg(av.getAmountReg());
////                o.setBarcode(av.getBarcode());
////                o.setDescription(av.getDescription());
////                o.setObs(av.getObs());
////                o.setPacking(av.getPacking());
////                o.setType(av.getType());
////                o.setUnity(av.getUnity());
//                o.setCost(av.getCost());
//                toSend.add(o);
//            } else{
//                valid = false;
//            }
//        }
        if(valid){
            send(toSend);
        } else {
//            ViewCompat.setTranslationZ(viewSwitcher.findViewById(R.id.scrollTable), 0.0f);
            final Snackbar sb = Snackbar.make(tableView, R.string.transference_validate_error, Snackbar.LENGTH_INDEFINITE);
            sb.setAction("Aceptar", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sb.dismiss();
                }
            }).show();
        }
    }

    private void send(List<InputTransferenceDetailDTO> toSend) {
        EditText text = viewSwitcher.findViewById(R.id.txtCause);
        String cause = text.getText().toString();
        if (toSend.isEmpty() || cause.isEmpty()) {
            final Snackbar sb = Snackbar.make(viewSwitcher.findViewById(R.id.rlRoot), R.string.transference_validate_error, Snackbar.LENGTH_INDEFINITE);
            sb.setAction("Aceptar", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sb.dismiss();
                }
            }).show();
        } else {
            final CaptureInputTransference captureInputTransference = new CaptureInputTransference();
            captureInputTransference.setToSend(toSend);
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
                        success();
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

    private void success() {
        databaseManager.delete(InputTransferenceDetail.class, items.toArray(new InputTransferenceDetail[items.size()]));
        databaseManager.delete(InputTransference.class, inputTransference);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(RESULT, R.string.succes_input_transference);
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            inputTransId = getArguments().getLong(ARG_INPUT_TRANS_ID, -1L);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (GlobalState) getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_transference_input_capture, container, false);

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
//        Date date = new Date();
//        swipeRefresh = (GeneralSwipeRefreshLayout) viewSwitcher.findViewById(R.id.swipeRefresh);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                downloadStatus();
//            }
//        });

        inputTransference = databaseManager.getById(inputTransId, InputTransference.class);
        getActivity().setTitle("TA de " + (inputTransference.getStore() == null ? "N/A" : inputTransference.getStore().getName()));

        if (items == null) {
            getDatabaseInfo();
            if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
                download();
            }
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
            setAdapter();
        }
        viewSwitcher.setDisplayedChild(1);
    }

    private void saveReceiptSheets(List<InputTransferenceDetail> list) {
        long itId = inputTransference.getId();
        for (InputTransferenceDetail itd : list) {
            itd.setInputTransferenceId(itId);
        }
        databaseManager.insertOrReplaceInTx(list.toArray(new InputTransferenceDetail[list.size()]));
        items = databaseManager.listInputTransferenceDetail(itId);
//        this.items = list;//TODO lo quite
    }

    private void showEmptyView() {
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.GONE);
    }

    private void getDatabaseInfo() {
        items = databaseManager.listInputTransferenceDetail(inputTransference.getId());
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

    private void setAdapter() {
        tableView = viewSwitcher.findViewById(R.id.list);
        types = databaseManager.listObservationType(4);
        obs = databaseManager.listObservationType(0);
//        adapter = new InputTransferenceTableAdapter(getActivity(), new ArrayList<>(items), items, types, obs);
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

//    private class ClickListener implements TableDataClickListener<InputTransferenceDetail> {
//
//        @Override
//        public void onDataClicked(final int rowIndex, final InputTransferenceDetail clickedData) {
////            final String carString = clickedData.getId() + " " + clickedData.getDescription();
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
//        void onFragmentInteraction(Lis);
//    }
}
