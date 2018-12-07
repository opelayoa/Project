package com.tiendas3b.almacen.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.tiendas3b.almacen.adapters.EditReceiptDecreaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.BuyDetail;
import com.tiendas3b.almacen.db.dao.DecreaseMP;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.ArticleCaptureDTO;
import com.tiendas3b.almacen.dto.CaptureDecrease;
import com.tiendas3b.almacen.dto.receipt.FMuestrasDTO;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.receipt.edit.ReceiptEditTimetableActivity;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.views.tables.vo.ArticleVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tiendas3b.almacen.util.Constants.ARRIVE;

public class EditDecreaseFragment extends Fragment {

    private static final String TAG = EditDecreaseFragment.class.getSimpleName();
    private Menu menu;
    private Buy item;
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private ViewSwitcher viewSwitcher;
    private RecyclerView mRecyclerView;
    private List<BuyDetail> items;
    private OnFragmentInteractionListener mListener;
    List<DecreaseMP> decreaseMPList = new ArrayList<>();
    private List<ArticleVO> articleVOList = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private EditReceiptDecreaseRecyclerViewAdapter mAdapter;
    private List<ObservationType> types;
    private List<ObservationType> obs;
    private List<ObservationType> obsLog;

    public EditDecreaseFragment() {
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

                decreaseMPList = databaseManager.findDecreaseMP(this.item.getFolio());

                if (decreaseMPList.size() > 0) {

                    for (int i = 0; i < decreaseMPList.size(); i++) {
                        //hacer pruebas con constructor
                        ArticleVO articleVO = new ArticleVO();
                        articleVO.setIclave(decreaseMPList.get(i).getIclave());
                        articleVO.setDescription(decreaseMPList.get(i).getDescription());
                        articleVO.setAmount(decreaseMPList.get(i).getAmount());
                        articleVO.setType(decreaseMPList.get(i).getType());
                        articleVO.setObs(decreaseMPList.get(i).getObs());
                        articleVO.setObsLog(decreaseMPList.get(i).getObsLog());
                        articleVO.setCost(decreaseMPList.get(i).getCost());
                        articleVO.setObsStr(decreaseMPList.get(i).getObsStr());
                        articleVOList.add(articleVO);
                    }
                    insertDecrease();

                } else {
                    Toast.makeText(getActivity(), "Lista De Productos Vacía", Toast.LENGTH_LONG).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertDecrease() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        final CaptureDecrease captureDecrease = new CaptureDecrease();

        captureDecrease.setToSend(convert(articleVOList));
        captureDecrease.setWarehouse(mContext.getRegion());
        httpService.insertDecrease(captureDecrease).enqueue(new Callback<GeneralResponseDTO>() {
            @Override
            public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                GeneralResponseDTO gr = response.body();
                if (response.isSuccessful() && gr.getCode() == 1L) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setMessage(getString(R.string.decrease_folio) + ": " + gr.getDescription());
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton(getString(android.R.string.ok),
                            (dialog, which) -> {
                                startActivity(new Intent(mContext, ReceiptEditTimetableActivity.class).putExtra(ReceiptEditTimetableActivity.EXTRA_ORDER_TYPE, 0));
                                getActivity().finish();

                            }
                    );

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {
                    String message = gr == null ? "Error de comunicación" : gr.getDescription();
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "send error!");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
                Toast.makeText(mContext, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "on failure!");
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (GlobalState) getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_edit_decrease_list, container, false);

        if (rootView instanceof ViewSwitcher) {
            viewSwitcher = ((ViewSwitcher) rootView);
            Animation slide_in_left = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            Animation slide_out_right = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right);
            viewSwitcher.setInAnimation(slide_in_left);
            viewSwitcher.setOutAnimation(slide_out_right);
        }

        databaseManager = new DatabaseManager(mContext);


        init(rootView);
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

    protected void init(View rootView) {
        Intent intent = getActivity().getIntent();
        Long buyId = intent.getLongExtra(Constants.EXTRA_BUY, 0L);
        item = databaseManager.getById(buyId, Buy.class);

        types = databaseManager.listObservationType(4);
        obs = databaseManager.listObservationType(0);
        obsLog = databaseManager.listObservationType(6);
        if (types == null || obsLog == null || obs == null) {
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
        mAdapter = new EditReceiptDecreaseRecyclerViewAdapter(getActivity(), items, R.layout.row_edit_decrease, mListener, types, obs, obsLog);
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

        void onFragmentInteraction(EditReceiptDecreaseRecyclerViewAdapter.ViewHolder holder, int position);

        boolean onSaveDetail(EditReceiptDecreaseRecyclerViewAdapter.ViewHolder viewHolder);

    }
}
