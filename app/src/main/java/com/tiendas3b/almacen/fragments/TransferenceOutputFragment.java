package com.tiendas3b.almacen.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.TransferenceOutputTableAdapter;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.CaptureTransference;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.views.tables.TransferenceOutputCaptureTableView;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TransferenceOutputFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TransferenceOutputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransferenceOutputFragment extends Fragment {

    private static final String TAG = TransferenceOutputFragment.class.getSimpleName();
    private static final String ARG_STORE = "storeId";
    public static final String RESULT = "result";
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private ViewSwitcher viewSwitcher;
    private List<VArticle> items;
    private TransferenceOutputCaptureTableView tableView;
//    private GeneralSwipeRefreshLayout swipeRefresh;
    private SearchView searchView;
    private TransferenceOutputTableAdapter adapter;

    private OnFragmentInteractionListener mListener;
    private long mStoreId;
    private boolean expand;
    private List<ObservationType> types;
    private List<ObservationType> obs;

    public TransferenceOutputFragment() {
        // Required empty public constructor
    }

    public static TransferenceOutputFragment newInstance(long storeId) {
        TransferenceOutputFragment fragment = new TransferenceOutputFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_STORE, storeId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_transference_output_capture, menu);

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
                        adapter = new TransferenceOutputTableAdapter(getActivity(), new ArrayList<>(items), items, types, obs);
                        tableView.setDataAdapter(adapter);
                        expand = true;
                    } else {
                        if (query.length() > 2) {
                            expand = false;
                            final List<VArticle> filteredModelList = filter(query);
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

    }

    private List<VArticle> filter(String query) {
        query = query.toLowerCase();
        final List<VArticle> filteredModelList = new ArrayList<>();
        query = query.trim().toLowerCase();
        for (VArticle model : items) {
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
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tableView.getWindowToken(), 0);
        List<VArticle> toSend = new ArrayList<>();
        for (VArticle av : items) {
//            Integer a = av.getAmount();
//            if (a != null && a != 0) {
//                toSend.add(av);
//            }
        }
        send(toSend);
    }

    private void send(List<VArticle> toSend) {
        EditText text = (EditText) viewSwitcher.findViewById(R.id.txtCause);
        String cause = text.getText().toString();
        if (toSend.isEmpty() || cause.isEmpty()) {
            Snackbar.make(tableView, R.string.transference_validate_error, Snackbar.LENGTH_LONG).show();
        } else {
            Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
            final CaptureTransference captureTransference = new CaptureTransference();
//            captureTransference.setToSend(toSend);
            captureTransference.setWarehouseId(mContext.getRegion());
            captureTransference.setStoreId(mStoreId);
            captureTransference.setCause(cause);
//            swipeRefresh.setRefreshing(true);
            httpService.insertTransferenceOutput(captureTransference).enqueue(new Callback<GeneralResponseDTO>() {
                @Override
                public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                    GeneralResponseDTO gr = response.body();
                    if (response.isSuccessful() && gr.getCode() == 1L) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(RESULT, R.string.succes_output_transference);
                        getActivity().setResult(Activity.RESULT_OK, returnIntent);
                        getActivity().finish();
//                        mListener.success();//TODO
                    } else {
//                    showEmptyView();
//                        Snackbar.make(tableView, gr.getDescription(), Snackbar.LENGTH_INDEFINITE).show();//TODO
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStoreId = getArguments().getLong(ARG_STORE, -1L);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (GlobalState) getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_transference_output_capture, container, false);

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
//        setTitle(getTitle() + " " + new SimpleDateFormat(Constants.DATE_FORMAT_SHORT, Locale.getDefault()).format(date));
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
//            if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
//                downloadStatus();
//            }
        }

    }

    private void getDatabaseInfo() {
        items = databaseManager.listViewArticlesTransference();
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
        tableView = (TransferenceOutputCaptureTableView) viewSwitcher.findViewById(R.id.list);
        types = databaseManager.listObservationType(4);
        obs = databaseManager.listObservationType(3);
        adapter = new TransferenceOutputTableAdapter(getActivity(), new ArrayList<>(items), items, types, obs);
//        swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
//            @Override
//            public boolean canChildScrollUp() {
//                return tableView.getChildAt(1).canScrollVertically(-1);
//            }
//        });
        tableView.setDataAdapter(adapter);
        tableView.addDataClickListener(new ClickListener());
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.VISIBLE);
    }

    private class ClickListener implements TableDataClickListener<VArticle> {

        @Override
        public void onDataClicked(final int rowIndex, final VArticle clickedData) {
            final String carString = clickedData.getIclave() + " " + clickedData.getDescription();
            Toast.makeText(mContext, carString, Toast.LENGTH_SHORT).show();
        }
    }

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(VArticle VArticle);
    }
}
