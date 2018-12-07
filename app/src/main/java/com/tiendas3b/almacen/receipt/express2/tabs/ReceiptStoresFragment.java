package com.tiendas3b.almacen.receipt.express2.tabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.ConfigProvider;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.receipt.bulk.InBulkReceiptActivity;
import com.tiendas3b.almacen.receipt.express2.ExpressReceiptActivity;
import com.tiendas3b.almacen.receipt.express.PalletizedReceiptActivity;
import com.tiendas3b.almacen.receipt.InsertReceiptExpressLog;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.views.RecyclerViewFastScroller;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by danflo on 04/07/2018 dzr.
 */
public class ReceiptStoresFragment extends Fragment {

    private static final String ARG_DAY = "ARG_DAY";
    private static final String ARG_TYPE = "ARG_TYPE";
    private static final String ARG_POSITION = "ARG_POSITION";
    private GlobalState mContext;
    private ViewSwitcher viewSwitcher;
    private OnFragmentInteractionListener mListener;
    private IDatabaseManager db;
    private List<Buy> items;
    private ReceiptStoreStatusRvAdapter adapter;

    private Unbinder unbinder;
    @BindView(R.id.fastscroller)
    RecyclerViewFastScroller fastScroller;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    private int mDay;
    private int mStatus;
    private int mType;

    public static Fragment newInstance(int position, int type, int day) {
        ReceiptStoresFragment fragment = new ReceiptStoresFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putInt(ARG_TYPE, type);
        args.putInt(ARG_DAY, day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_store_list, container, false);
        View rootView = inflater.inflate(R.layout.fragment_buys_list, container, false);

        //adapter = new ReceiptStoreStatusRvAdapter(mContext);
        if (rootView instanceof ViewSwitcher) {
            viewSwitcher = ((ViewSwitcher) rootView);
//            Animation slide_in_left = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
//            Animation slide_out_right = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right);
//            viewSwitcher.setInAnimation(slide_in_left);
//            viewSwitcher.setOutAnimation(slide_out_right);
        }
        unbinder = ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    @SuppressWarnings("ConstantConditions")
    private void init() {
        Bundle args = getArguments();
        mDay = args.getInt(ARG_DAY);
        mStatus = args.getInt(ARG_POSITION);
        mType = args.getInt(ARG_TYPE);
        mContext = (GlobalState) Objects.requireNonNull(getActivity()).getApplicationContext();
        db = mListener.getDb();
        if (items == null) {
            getDatabaseInfo();
        }
    }

    private void getDatabaseInfo() {
//        items = db.listStoresStatusPick(mContext.getRegion(), DateUtil.getTodayStr(), mType, mStatus);

        items = null;
        switch (mStatus){
            case Constants.RECEIPT_PENDING:
                items = db.listBuysExpress(DateUtil.getTodayStr(), mContext.getRegion(), Constants.NOT_ARRIVE);
                break;

            case Constants.RECEIPT_PENDING_AUTOMATIC:
                items = db.listBuysExpress(DateUtil.getTodayStr(), mContext.getRegion(), Constants.NOT_ARRIVE_AUT);
                break;
            case Constants.RECEIPT_IN_PROCESS:
                items = db.listBuysExpress(DateUtil.getTodayStr(), mContext.getRegion(), Constants.NOT_ARRIVE_TEMP, Constants.NOT_ARRIVE_AUT_TEMP);
                break;
            case Constants.RECEIPT_TK_IMP:
                items = db.listBuysExpress(DateUtil.getTodayStr(), mContext.getRegion(), Constants.ARRIVE);
                break;
            case Constants.RECEIPT_HR_IMP:
                items = db.listBuysExpress(DateUtil.getTodayStr(), mContext.getRegion(), Constants.ARRIVE_HR_IMPR);
                break;
            default:
                items = null;
                break;

        }

        createItems();

        if (items.isEmpty()) {
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
            setAdapter();
            recyclerView.setVisibility(View.VISIBLE);
        }
        viewSwitcher.setDisplayedChild(0);
//        }
    }

    protected void createItems() {
        if (items != null) {
            Collections.sort(items, new Comparator<Buy>() {
                Date now = DateUtil.getTimeNow();

                @Override
                public int compare(Buy b1, Buy b2) {
                    int res = 0;
                    try {
                        Date t1 = DateUtil.getTime(b1.getTime());
                        Date t2 = DateUtil.getTime(b2.getTime());
//                    Log.e(TAG, "t1:" + t1 + " t2:" + t2 + " n:" + now);
                        if (t1.after(now) && t2.before(now)) {
                            res = -1;
                        } else if (t1.before(now) && t2.after(now)) {
                            res = 1;
                        } else {
                            res = normalOrder(t1, t2, b1, b2);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return res;
                }

                private int normalOrder(Date t1, Date t2, Buy b1, Buy b2) {
                    if (t1.after(t2)) {
                        return 1;
                    } else if (t1.before(t2)) {
                        return -1;
                    } else {
                        if (b1.getPlatform() < b2.getPlatform()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }
            });
        }
    }

    private void setAdapter() {
        adapter = new ReceiptStoreStatusRvAdapter(mContext, new BaseRecyclerViewAdapter.OnViewHolderClick() {
            @Override
            public void onClick(View v, int position) {
                if (db.isEmpty(VArticle.class)) {
                    Toast.makeText(mContext, "Espera o sincroniza info", Toast.LENGTH_LONG).show();
                } else {
//                String provider = items.get(items.indexOf(v)).getProvider().getName();//
//                final Buy provider = items.get(recyclerView.getChildAdapterPosition(v));
                    final Buy provider = items.get(position);

                    int value = provider.getFolio();
                    ConfigProvider config = provider.getProvider().getMmpConfig();

                    //INSERTA LOG RECIBO EXPRESS
                    InsertReceiptExpressLog insertReceiptExpressLog = new InsertReceiptExpressLog(db, mContext);
                    insertReceiptExpressLog.insertRecExprLog(provider, Constants.IDLOG_SELECT_PROV, Constants.LOG_SELECT_PROV);


                    switch (mStatus) {
                        case Constants.RECEIPT_PENDING:
                            switch (config.getP2()) {
                                case Constants.MMP_PAL:
                                    startActivity(ExpressReceiptActivity.getIntent(mContext, provider.getFolio()));
                                    getActivity().finish();
                                    break;
                                case Constants.MMP_BULK:
                                case Constants.MMP_MIX:
                                    startActivity(new Intent(mContext, InBulkReceiptActivity.class)
                                            .putExtra(PalletizedReceiptActivity.ODC, provider.getFolio()));
                                    getActivity().finish();
                                    break;
                                case Constants.MMP_NOT_IN:
                                    startActivity(ExpressReceiptActivity.getIntent(mContext, provider.getFolio()));
                                    getActivity().finish();
                                    break;
                                default:
                                    Toast.makeText(mContext, "Favor de eportar.", Toast.LENGTH_LONG).show();
                                    break;
                            }
                            break;
                        case Constants.RECEIPT_PENDING_AUTOMATIC:
                            switch (config.getP2()) {
                                case Constants.MMP_PAL:
                                    startActivity(ExpressReceiptActivity.getIntent(mContext, provider.getFolio()));
                                    getActivity().finish();
                                    break;
                                case Constants.MMP_BULK:
                                case Constants.MMP_MIX:
                                    startActivity(new Intent(mContext, InBulkReceiptActivity.class)
                                            .putExtra(PalletizedReceiptActivity.ODC, provider.getFolio()));
                                    getActivity().finish();
                                    break;
                                case Constants.MMP_NOT_IN:
                                    startActivity(ExpressReceiptActivity.getIntent(mContext, provider.getFolio()));
                                    getActivity().finish();
                                    break;
                                default:
                                    Toast.makeText(mContext, "Favor de eportar.", Toast.LENGTH_LONG).show();
                                    break;
                            }
                            break;
                        case Constants.RECEIPT_IN_PROCESS:
                            switch (config.getP2()) {
                                case Constants.MMP_PAL:
                                    startActivity(ExpressReceiptActivity.getIntent(mContext, provider.getFolio()));
                                    getActivity().finish();
                                    break;
                                case Constants.MMP_BULK:
                                case Constants.MMP_MIX:
                                    startActivity(new Intent(mContext, InBulkReceiptActivity.class)
                                            .putExtra(PalletizedReceiptActivity.ODC, provider.getFolio()));
                                    getActivity().finish();
                                    break;
                                case Constants.MMP_NOT_IN:
                                    startActivity(ExpressReceiptActivity.getIntent(mContext, provider.getFolio()));
                                    getActivity().finish();
                                    break;
                                default:
                                    Toast.makeText(mContext, "Favor de eportar.", Toast.LENGTH_LONG).show();
                                    break;
                            }
                            break;
                        case Constants.RECEIPT_TK_IMP:
                            break;
                        case Constants.RECEIPT_HR_IMP:
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        adapter.setList(items);
        initRecyclerView();
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerView() {
//        swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
//            @Override
//            public boolean canChildScrollUp() {
//                return /*recyclerView.getFirstVisiblePosition() > 0 ||*/ //con layout manager ya que depende si es linear o grid
//                        fastScroller.getScrollX() == 0 ||
//                                recyclerView.getChildAt(0) == null || recyclerView.getChildAt(0).getTop() < 0;
//            }
//        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                //TODO if the items are filtered, considered hiding the fast scroller here
                final int firstVisibleItemPosition = findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != 0) {
                    // this avoids trying to handle un-needed calls
                    if (firstVisibleItemPosition == -1)
                        //not initialized, or no items shown, so hide fast-scroller
                        fastScroller.setVisibility(View.GONE);
                    return;
                }
                final int lastVisibleItemPosition = findLastVisibleItemPosition();
                int itemsShown = lastVisibleItemPosition - firstVisibleItemPosition + 1;
                //if all items are shown, hide the fast-scroller
                fastScroller.setVisibility(adapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
            }
        });
        fastScroller.removeAllViews();
        fastScroller.setRecyclerView(recyclerView);
        fastScroller.setViewsToUse(R.layout.recycler_view_fast_scroller__fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ReceiptStoresFragment.OnFragmentInteractionListener) {

            mListener = (ReceiptStoresFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface OnFragmentInteractionListener {

        IDatabaseManager getDb();

        BaseRecyclerViewAdapter.OnViewHolderClick getAdapterListener(int mStatus, ReceiptStoreStatusRvAdapter adapter, int type);

    }
}
