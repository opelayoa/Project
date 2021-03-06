package com.tiendas3b.almacen.receipt.edit;

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
import com.tiendas3b.almacen.activities.BuyDetailActivity;
import com.tiendas3b.almacen.activities.EditReceiptBoxes;
import com.tiendas3b.almacen.activities.EditReceiptComments;
import com.tiendas3b.almacen.activities.EditReceiptDecrease;
import com.tiendas3b.almacen.activities.EditReceiptFolio;
import com.tiendas3b.almacen.adapters.BaseRecyclerViewAdapter;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.ConfigProvider;
import com.tiendas3b.almacen.db.dao.ExpressReceipt;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.BuyDetailAndCaptureDTO;
import com.tiendas3b.almacen.receipt.bulk.InBulkReceiptActivity;
import com.tiendas3b.almacen.receipt.express.HeaderExpressDTO;
import com.tiendas3b.almacen.receipt.express.PalletizedReceiptActivity;
import com.tiendas3b.almacen.receipt.express2.ExpressReceiptActivity;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.views.RecyclerViewFastScroller;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by danflo on 04/07/2018 dzr.
 */
public class ReceiptEditFragment extends Fragment {

    private static final String ARG_DAY = "ARG_DAY";
    private static final String ARG_TYPE = "ARG_TYPE";
    private static final String ARG_POSITION = "ARG_POSITION";
    private GlobalState mContext;
    private ViewSwitcher viewSwitcher;
    private OnFragmentInteractionListener mListener;
    private IDatabaseManager db;
    private List<Buy> items;
    private ReceiptEditStatusRvAdapter adapter;

    private Unbinder unbinder;
    @BindView(R.id.fastscroller)
    RecyclerViewFastScroller fastScroller;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    private int mDay;
    private int mStatus;
    private int mType;

    public static Fragment newInstance(int position, int type, int day) {
        ReceiptEditFragment fragment = new ReceiptEditFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putInt(ARG_TYPE, type);
        args.putInt(ARG_DAY, day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buys_list, container, false);

        if (rootView instanceof ViewSwitcher) {
            viewSwitcher = ((ViewSwitcher) rootView);
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
        items = null;
        switch (mStatus) {
            case Constants.RECEIPT_PENDING:
                items = db.listBuysExpress(DateUtil.getTodayStr(), mContext.getRegion(), Constants.ARRIVE);
                break;
            case Constants.RECEIPT_PENDING_AUTOMATIC:
                items = db.listBuysExpress(DateUtil.getTodayStr(), mContext.getRegion(), Constants.ARRIVE);
                break;
            case Constants.RECEIPT_IN_PROCESS:
                items = db.listBuysExpress(DateUtil.getTodayStr(), mContext.getRegion(), Constants.ARRIVE);
                break;
            case Constants.RECEIPT_TK_IMP:
                items = db.listBuysExpress(DateUtil.getTodayStr(), mContext.getRegion(), Constants.ARRIVE);
                break;
            case Constants.RECEIPT_HR_IMP:
                items = db.listBuysExpress(DateUtil.getTodayStr(), mContext.getRegion(), Constants.ARRIVE);
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
        adapter = new ReceiptEditStatusRvAdapter(mContext, new BaseRecyclerViewAdapter.OnViewHolderClick() {
            @Override
            public void onClick(View v, int position) {
                if (db.isEmpty(VArticle.class)) {
                    Toast.makeText(mContext, "Espera o sincroniza info", Toast.LENGTH_LONG).show();
                } else {
                    final Buy provider = items.get(position);

                    ConfigProvider config = provider.getProvider().getMmpConfig();

//                    switch (mStatus) {
//                        case Constants.RECEIPT_PENDING:
//                            startActivity(new Intent(mContext, BuyDetailActivity.class).putExtra(Constants.EXTRA_BUY, provider.getId()));
//                            getActivity().finish();
//                            break;
//                        case Constants.RECEIPT_PENDING_AUTOMATIC:
//                            startActivity(new Intent(mContext, EditBuyDetailActivity.class).putExtra(Constants.EXTRA_BUY, provider.getId()));
//                            getActivity().finish();
//                            break;
//                        case Constants.RECEIPT_IN_PROCESS:
//                            startActivity(new Intent(mContext, EditReceiptFolio.class).putExtra(Constants.EXTRA_BUY, provider.getId()));
//                            getActivity().finish();
//                            break;
//                        case Constants.RECEIPT_TK_IMP:
//                            startActivity(new Intent(mContext, EditReceiptComments.class).putExtra(Constants.EXTRA_BUY, provider.getId()));
//                            getActivity().finish();
//                            break;
//                        case Constants.RECEIPT_HR_IMP:
//                            break;
//                        default:
//                            break;
//                    }


                    EditReceipt editReceipt1 = new EditReceipt(db, mContext, provider.getFolio(), provider.getRegionId(), provider.getProviderId(), provider.getId());
                    editReceipt1.downloadDetails(new Callback(){

                        @Override
                        public void onResponse(Call call, Response response) {

                            switch (mStatus) {
                                case Constants.RECEIPT_PENDING:
                                    startActivity(new Intent(mContext, EditBuyDetailActivity.class).putExtra(Constants.EXTRA_BUY, provider.getId()));
                                    getActivity().finish();
                                    break;
                                case Constants.RECEIPT_PENDING_AUTOMATIC:
                                    startActivity(new Intent(mContext, EditReceiptDecrease.class).putExtra(Constants.EXTRA_BUY, provider.getId()));
                                    getActivity().finish();
                                    break;
                                case Constants.RECEIPT_IN_PROCESS:
                                    startActivity(new Intent(mContext, EditReceiptFolio.class).putExtra(Constants.EXTRA_BUY, provider.getId()));
                                    getActivity().finish();
                                    break;
                                case Constants.RECEIPT_TK_IMP:
                                    startActivity(new Intent(mContext, EditReceiptComments.class).putExtra(Constants.EXTRA_BUY, provider.getId()));
                                    getActivity().finish();
                                    break;
                                case Constants.RECEIPT_HR_IMP:
                                    startActivity(new Intent(mContext, EditReceiptBoxes.class).putExtra(Constants.EXTRA_BUY, provider.getId()));
                                    getActivity().finish();
                                    break;
                                default:
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {

                        }
                    });






//                    editReceipt1.downloadDetails(new  Callback(){
//                        @Override
//                        public void onResponse(Call call, Response response) {
//                            List<BuyDetailAndCaptureDTO> list = (List<BuyDetailAndCaptureDTO>) response.body();
//                            editReceipt1.processBuyDetails(list);
//
//                            editReceipt1.downloadHeaderExpress(new Callback(){
//
//                                @Override
//                                public void onResponse(Call call, Response response) {
//                                    HeaderExpressDTO header = (HeaderExpressDTO) response.body();
//                                    editReceipt1.downloadDetails(new Callback(){
//                                                      @Override
//                                                      public void onResponse(Call call, Response response) {
//
//                                                          List<ExpressReceipt> details = (List<ExpressReceipt>) response.body();
//                                                          editReceipt1.processDetails(details);
//
//                                                          switch (mStatus) {
//                                                              case Constants.RECEIPT_PENDING:
//                                                                  startActivity(new Intent(mContext, EditBuyDetailActivity.class).putExtra(Constants.EXTRA_BUY, provider.getId()));
//                                                                  getActivity().finish();
//                                                                  break;
//                                                              case Constants.RECEIPT_PENDING_AUTOMATIC:
//                                                                  startActivity(new Intent(mContext, EditBuyDetailActivity.class).putExtra(Constants.EXTRA_BUY, provider.getId()));
//                                                                  getActivity().finish();
//                                                                  break;
//                                                              case Constants.RECEIPT_IN_PROCESS:
//                                                                  startActivity(new Intent(mContext, EditReceiptFolio.class).putExtra(Constants.EXTRA_BUY, provider.getId()));
//                                                                  getActivity().finish();
//                                                                  break;
//                                                              case Constants.RECEIPT_TK_IMP:
//                                                                  startActivity(new Intent(mContext, EditReceiptComments.class).putExtra(Constants.EXTRA_BUY, provider.getId()));
//                                                                  getActivity().finish();
//                                                                  break;
//                                                              case Constants.RECEIPT_HR_IMP:
//                                                                  break;
//                                                              default:
//                                                                  break;
//                                                          }
//                                                      }
//
//                                                      @Override
//                                                      public void onFailure(Call call, Throwable t) {
//
//                                                      }
//                                                  },
//                                            header.getFolio());
//                                }
//
//                                @Override
//                                public void onFailure(Call call, Throwable t) {
//
//                                }
//                            });
//
//                            Toast.makeText(mContext, "Termino WS", Toast.LENGTH_LONG).show();
//                            //INSERTA LOG RECIBO EXPRESS
////                            InsertReceiptExpressLog insertReceiptExpressLog = new InsertReceiptExpressLog(db, mContext);
////                            insertReceiptExpressLog.insertRecExprLog(provider, Constants.IDLOG_SELECT_PROV, Constants.LOG_SELECT_PROV);
//
//
//                        }
//
//                        @Override
//                        public void onFailure(Call call, Throwable t) {
//
//                        }
//                    });


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
        if (context instanceof ReceiptEditFragment.OnFragmentInteractionListener) {

            mListener = (ReceiptEditFragment.OnFragmentInteractionListener) context;
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

        BaseRecyclerViewAdapter.OnViewHolderClick getAdapterListener(int mStatus, ReceiptEditStatusRvAdapter adapter, int type);

    }
}
