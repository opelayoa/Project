package com.tiendas3b.almacen.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.EditReceiptBoxesRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.BuyDetail;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.receipt.edit.ReceiptEditTimetableActivity;
import com.tiendas3b.almacen.util.Constants;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.tiendas3b.almacen.util.Constants.ARRIVE;

public class EditBoxesFragment extends Fragment {

    private static final String TAG = EditBoxesFragment.class.getSimpleName();
    private Menu menu;
    private Buy item;
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private ViewSwitcher viewSwitcher;
    private RecyclerView mRecyclerView;
    private List<BuyDetail> items;
    private OnFragmentInteractionListener mListener;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditReceiptBoxesRecyclerViewAdapter mAdapter;

    public EditBoxesFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save_only, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (item.getChecked() == ARRIVE) {
//            menu.clear(); TODO
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save:


                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (GlobalState) getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_edit_boxes_list, container, false);

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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        items = null;
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onDestroyView();
    }

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

    protected void init() {
        Intent intent = getActivity().getIntent();
        Long buyId = intent.getLongExtra(Constants.EXTRA_BUY, 0L);
        item = databaseManager.getById(buyId, Buy.class);

        if (item == null) {
            startActivity(new Intent(mContext, ReceiptEditTimetableActivity.class).putExtra(ReceiptEditTimetableActivity.EXTRA_ORDER_TYPE, 0));
            getActivity().finish();
            Toast.makeText(mContext, "Actualiza catálogos por favor", Toast.LENGTH_LONG).show();
        } else {
            setItems();
        }
    }

    private void setItems() {
        items = item.getBuyDetails();
        Collections.sort(items, new Comparator<BuyDetail>() {
            @Override
            public int compare(BuyDetail bd1, BuyDetail bd2) {
                return bd1.getArticle().getDescription().compareTo(bd2.getArticle().getDescription());
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new EditReceiptBoxesRecyclerViewAdapter(getActivity(), items, R.layout.row_edit_boxes, mListener);
        mRecyclerView = viewSwitcher.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void collapseAll(LinearLayout llCaptureSheetDetail) {
        collapseAll();
        llCaptureSheetDetail.setVisibility(View.VISIBLE);
    }

    private void collapseAll() {
        int count = mRecyclerView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mRecyclerView.getChildAt(i);
            child.findViewById(R.id.capture_sheet_detail).setVisibility(View.GONE);
        }
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(EditReceiptBoxesRecyclerViewAdapter.ViewHolder holder, int position);

        boolean onSaveDetail(EditReceiptBoxesRecyclerViewAdapter.ViewHolder viewHolder);

    }
}
