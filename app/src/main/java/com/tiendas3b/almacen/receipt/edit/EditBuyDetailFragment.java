package com.tiendas3b.almacen.receipt.edit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.BuyDetailActivity;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.BuyDetail;
import com.tiendas3b.almacen.db.dao.Check;
import com.tiendas3b.almacen.db.dao.ComplaintCat;
import com.tiendas3b.almacen.db.dao.DecreaseMP;
import com.tiendas3b.almacen.db.dao.FMuestras;
import com.tiendas3b.almacen.db.dao.ReceiptSheetCapture;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetailCapture;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.ArticleCaptureDTO;
import com.tiendas3b.almacen.dto.BuyDTO;
import com.tiendas3b.almacen.dto.BuyDetailAndCaptureDTO;
import com.tiendas3b.almacen.dto.BuyDetailDTO;
import com.tiendas3b.almacen.dto.CaptureDecrease;
import com.tiendas3b.almacen.dto.CaptureDocCompras;
import com.tiendas3b.almacen.dto.ReceiptSheetCaptureDTO;
import com.tiendas3b.almacen.dto.receipt.FMuestrasDTO;
import com.tiendas3b.almacen.dto.receipt.FhojaReciboEncTempDTO;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.picking.orders.ScanTimeDTO;
import com.tiendas3b.almacen.receipt.report.ReceiptReportActivity;
import com.tiendas3b.almacen.receipt.sheet.capture.ReceiptSheetHeaderCaptureActivity;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.DialogUtil;
import com.tiendas3b.almacen.util.NetworkUtil;
import com.tiendas3b.almacen.views.tables.vo.ArticleVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tiendas3b.almacen.R.id.txtBarcode;
import static com.tiendas3b.almacen.R.id.txtDeliveryTime;
import static com.tiendas3b.almacen.R.id.txtFolio;
import static com.tiendas3b.almacen.R.id.txtPlatform;
import static com.tiendas3b.almacen.R.id.txtTime;
import static com.tiendas3b.almacen.util.Constants.ARRIVE;

public class EditBuyDetailFragment extends Fragment {

    private static final String TAG = EditBuyDetailFragment.class.getSimpleName();
    private static final int RECEIPT_SHEET_TYPE = 14;
    private Buy item;
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private ViewSwitcher viewSwitcher;
    private RecyclerView recyclerView;
    private List<BuyDetail> items;
    private List<ReceiptSheetDetailCapture> captures;
    private EditBuyDetailRecyclerViewAdapter adapter;
    private OnFragmentInteractionListener mListener;
    //    private ReceiptSheetCapture receiptSheetCapture;
    private LinearLayout llBuy;
    private TextView txtReceived;
    private View btnInit;
    private View btnFinish;
    private Menu menu;
    private MenuItem btnSave;
    private ProgressBar progressBar;
    private FhojaReciboEncTempDTO fhojaReciboEncTempDTO;
    List<DecreaseMP> decreaseMPList = new ArrayList<>();
    List<FMuestras> fMuestrasList = new ArrayList<>();
    private List<ArticleVO> articleVOList = new ArrayList<>();
    private List<FMuestrasDTO> fMuestrasDTOList = new ArrayList<>();

    public EditBuyDetailFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_save_only, menu);
//        btnSave = menu.findItem(R.id.action_send);
//        btnSave.setVisible(false);
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
                fMuestrasList = databaseManager.findFMuestras(this.item.getFolio());


                        for (int i = 0; i < decreaseMPList.size(); i++){
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

                        insertDecrease(new Callback(){

                            @Override
                            public void onResponse(Call call, Response response) {

                                GeneralResponseDTO gr = (GeneralResponseDTO) response.body();
                                for (int i = 0; i < fMuestrasList.size(); i++){
                                    FMuestrasDTO fMuestrasDTO = new FMuestrasDTO(
                                            fMuestrasList.get(i).getTclave(), fMuestrasList.get(i).getIclave(), fMuestrasList.get(i).getPclave(), fMuestrasList.get(i).getFecha(),
                                            fMuestrasList.get(i).getOdc(), "", fMuestrasList.get(i).getLote(), fMuestrasList.get(i).getFechaCaducidad(),
                                            fMuestrasList.get(i).getCantidad(), Integer.parseInt(gr.getDescription()), fMuestrasList.get(i).getUserId()
                                    );
                                    fMuestrasDTOList.add(fMuestrasDTO);
                                }
                                insertFMuestas(fMuestrasDTOList);
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {

                            }
                        });


//                send();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertDecrease(Callback callback){
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        final CaptureDecrease captureDecrease = new CaptureDecrease();

        captureDecrease.setToSend(convert(articleVOList));
        captureDecrease.setWarehouse(mContext.getRegion());
        httpService.insertDecrease(captureDecrease).enqueue(new Callback<GeneralResponseDTO>() {
            @Override
            public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                GeneralResponseDTO gr = response.body();
                if (response.isSuccessful() && gr.getCode() == 1L) {

                    callback.onResponse(call, response);

                } else {
                    String message = gr == null ? "Error de comunicación" : gr.getDescription();
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "send error!");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
                Toast.makeText(mContext,  t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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

    private void insertFMuestas(List<FMuestrasDTO> fMuestrasDTO){
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.insertFMuestras(fMuestrasDTO).enqueue(new Callback<GeneralResponseDTO>() {
            @Override
            public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                GeneralResponseDTO gr = response.body();
                if (response.isSuccessful() && gr.getCode() == 1L) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setMessage(getString(R.string.decrease_folio) + ": " + fMuestrasDTO.get(0).getDpfolio());
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
                Toast.makeText(mContext,  t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "on failure!");
            }
        });

    }

    private void send() {
        btnSave.setVisible(false);
        progressBar.setVisibility(View.VISIBLE);
        if (valid()) {

        } else {
            Snackbar.make(recyclerView, "No se pueden procesar artículos en cero, favor de hacerlo en la PC.", Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    private boolean valid() {
        for (BuyDetail b : items) {
            if (b.getReceiptSheetCapture().getReceivedAmount() > 0) {
                return true;
            }
        }
        return false;
    }

    private void process(String folio, String billRef, String names) {
        Toast.makeText(mContext, "Procesdado", Toast.LENGTH_LONG).show();
//        BuyDTO buy = captureDocCompras.getBuy();
//        item = databaseManager.getById(buy.getId(), Buy.class);
        item = databaseManager.getById(item.getId(), Buy.class);
        item.setChecked(Constants.ARRIVE);
        databaseManager.update(item);
        setStatus();
        setItems();
        showFolio(folio, billRef, names);
    }

    private void showFolio(final String folio, final String billRef, final String names) {
        DialogUtil.showAlertDialog(getActivity(), "Folio EM: " + folio, "Ver PDF", "Cerrar", (dialogInterface, i) -> {
            startActivityForResult(new Intent(mContext, ReceiptReportActivity.class).putExtra("t", Constants.EM)
                    .putExtra("f", Integer.parseInt(folio)).putExtra("p", item.getProviderId()).putExtra("b", billRef)
                    .putExtra(ReceiptReportActivity.EXTRA_SCAN_TIME, mListener.getTimer())
                    .putExtra("n", names), -1);
//            Intent intent = new Intent(getActivity(), ReceiptTimetableStatusActivity.class).putExtra("EXTRA_ORDER_TYPE", 0);
            getActivity().finish();
//            startActivity(intent);
        }, (dialogInterface, i) ->{
//                Intent intent = new Intent(getActivity(), ReceiptTimetableStatusActivity.class).putExtra("EXTRA_ORDER_TYPE", 0);
            getActivity().finish();
//            startActivity(intent);
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            inputTransId = getArguments().getLong(ARG_INPUT_TRANS_ID, -1L);
//        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (GlobalState) getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_edit_buy_detail_list, container, false);

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
        progressBar = getActivity().findViewById(R.id.progress);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onDestroyView() {
        items = null;
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onDestroyView();
    }

    private void init(View rootView) {
        if (items == null) {
            Intent intent = getActivity().getIntent();
            Long buyId = intent.getLongExtra(Constants.EXTRA_BUY, 0L);
            item = databaseManager.getById(buyId, Buy.class);
            Activity activity = getActivity();
            activity.setTitle(getString(R.string.buy_folio) + ": " + item.getFolio()
                    /* + " " + new SimpleDateFormat(DateUtil.DD_MM, Locale.getDefault()).format(item.getProgramedDate())*/);

            ((EditText) rootView.findViewById(R.id.txtProvider)).setText(item.getProvider().getName());
            ((EditText) rootView.findViewById(txtFolio)).setText(String.format(Locale.getDefault(), "%d", item.getFolio()));
//            if (item.getDay() == -1) {
//                ((EditText) rootView.findViewById(txtPlatform)).setText(R.string.no_timetable);
//                ((EditText) rootView.findViewById(txtTime)).setText(R.string.no_timetable);
//                ((EditText) rootView.findViewById(txtDeliveryTime)).setText(R.string.no_timetable);
//            } else {
            ((EditText) rootView.findViewById(txtPlatform)).setText(String.format(Locale.getDefault(), "%d", item.getPlatform()));
            ((EditText) rootView.findViewById(txtTime)).setText(item.getTime());
            ((EditText) rootView.findViewById(txtDeliveryTime)).setText(item.getDeliveryTime());
//            }
            txtReceived = rootView.findViewById(R.id.txtReceived);
            btnInit = (rootView.findViewById(R.id.btnInit));
            btnFinish = (rootView.findViewById(R.id.btnFinish));
            llBuy = rootView.findViewById(R.id.llbuy);
            setStatus();

//            setReceiptSheet(rootView);

            getDatabaseInfo();
            if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
                downloadBuys();
            }
        }
    }

    private void setStatus() {
        if (item.getChecked() == ARRIVE) {
            btnInit.setVisibility(View.GONE);
            btnFinish.setVisibility(View.GONE);
            txtReceived.setText(R.string.received);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                txtReceived.setTextColor(mContext.getResources().getColor(R.color.received, mContext.getTheme()));
            } else {
                txtReceived.setTextColor(mContext.getResources().getColor(R.color.received));
            }
//        } else if (item.getChecked() == Constants.IN_PROCESS || item.getChecked() == Constants.NOT_ARRIVE_AUT_TEMP) {
//            btnInit.setEnabled(false);
////            btnFinish.setEnabled(true);
//            txtReceived.setText(R.string.in_process);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                txtReceived.setTextColor(mContext.getResources().getColor(R.color.in_process, mContext.getTheme()));
//            } else {
//                txtReceived.setTextColor(mContext.getResources().getColor(R.color.in_process));
//            }
//            btnFinish.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setFinish();
//                }
//            });
////            initTimer();
//            downloadTimeInit();
        }
        else {
            btnInit.setVisibility(View.GONE);
            btnFinish.setVisibility(View.GONE);
            txtReceived.setText(R.string.no_received);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                txtReceived.setTextColor(mContext.getResources().getColor(R.color.no_received, mContext.getTheme()));
            } else {
                txtReceived.setTextColor(mContext.getResources().getColor(R.color.no_received));
            }
            btnInit.setOnClickListener(v -> setInProcess());
        }
        txtReceived.setVisibility(View.VISIBLE);
    }

    private void initTimer() {
        ReceiptSheetCapture receiptSheetCapture = databaseManager.findReceiptSheetCaptureBy(item.getId());
        if(receiptSheetCapture != null) {
            mListener.setBaseTime(receiptSheetCapture.getReceiptDate(), fhojaReciboEncTempDTO.getHoraEntrega());
        }
    }

    private void downloadTimeInit() {

        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getHojaReciboEncTemp(item.getFolio(), String.valueOf(mContext.getRegion()), item.getProviderId())
//        httpService.getReceiptSheetCapture(mContext.getRegion(), DateUtil.getDateStr(odc.getProgramedDate()), odc.getFolio(), odc.getProviderId(), odc.getId())
                .enqueue(new Callback<FhojaReciboEncTempDTO>() {
                    @Override
                    public void onResponse(Call<FhojaReciboEncTempDTO> call, Response<FhojaReciboEncTempDTO> response) {
                        if (response.isSuccessful()) {
                            FhojaReciboEncTempDTO r = response.body();
                            if (r != null) {
                                fhojaReciboEncTempDTO = r;
//                                initChronometer();
                                initTimer();
//                                save(r);
                            }
//                            fillFields();
                        } else {
//                    viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//                    viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<FhojaReciboEncTempDTO> call, Throwable t) {
                        Log.e(TAG, "Error getReceiptSheetCapture");
//                viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//                viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
//                viewSwitcher.setDisplayedChild(1);
                    }
                });
    }

    private void setFinish() {
        final ReceiptSheetCapture receiptSheetCapture = databaseManager.findReceiptSheetCaptureBy(item.getId());
        if (receiptSheetCapture == null) {
            Toast.makeText(mContext, "Falta Encabezado", Toast.LENGTH_LONG).show();
        } else {
            Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
            httpService.finishReceiptSheet(item.getFolio(), item.getRegionId(), item.getProviderId(), receiptSheetCapture.getReceiptDate()).enqueue(new Callback<GeneralResponseDTO>() {
                @Override
                public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                    if (response.isSuccessful()) {
                        GeneralResponseDTO res = response.body();
                        if (res.getCode() == Constants.WS_SUCCESS) {
                            receiptSheetCapture.setDepartureTime(res.getDescription());
//                        receiptSheetCapture.setDepartureTime(DateUtil.getTimeNowStr());
                            databaseManager.update(receiptSheetCapture);
                            btnFinish.setEnabled(false);
                            Float a = receiptSheetCapture.getAmountFact();
                            if (a != null && a > 0.0F) {
                                showSend();
                            }
                        }
                    } else {
                    }
                    Log.i(TAG, "finishReceiptSheet success");
                }

                @Override
                public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
//                Toast.makeText(mContext, "No procesado", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "finishReceiptSheet error");
                }
            });
        }
    }

    private void setInProcess() {
        mListener.startTime();
        btnInit.setEnabled(false);
        CaptureDocCompras captureDocCompras = new CaptureDocCompras();
        BuyDTO buyDTO = new BuyDTO();
        buyDTO.setId(item.getId());
        buyDTO.setPlatform(item.getPlatform());
        buyDTO.setTime(item.getTime());
        buyDTO.setDeliveryTime(item.getDeliveryTime());
        buyDTO.setChecked(Constants.NOT_ARRIVE_TEMP);
        buyDTO.setFolio(item.getFolio());
        buyDTO.setForced(item.getForced());
        buyDTO.setMov(item.getMov());
        buyDTO.setProgramedDate(DateUtil.getDateStr(item.getProgramedDate()));
        buyDTO.setProviderId(item.getProviderId());
        buyDTO.setRegionId(item.getRegionId());
        buyDTO.setTotal(item.getTotal());
        captureDocCompras.setBuy(buyDTO);

        String dateTimeStr = item.getTime();
        if (getString(R.string.no_timetable).equals(dateTimeStr)) {
            dateTimeStr = "00:00";
        }
        ReceiptSheetCaptureDTO receiptSheetCaptureDTO = new ReceiptSheetCaptureDTO();
        ReceiptSheetCapture r = databaseManager.findReceiptSheetCaptureBy(item.getId());
        if (r == null) {
            receiptSheetCaptureDTO.setDateTime(dateTimeStr);
            receiptSheetCaptureDTO.setBuyId(item.getId());
            receiptSheetCaptureDTO.setOdc(item.getFolio());
            receiptSheetCaptureDTO.setReceiptDate(DateUtil.getTodayStr());
            receiptSheetCaptureDTO.setPlatform(item.getPlatform());
//            receiptSheetCaptureDTO.setNumReceivers(0);
//            receiptSheetCaptureDTO.setDateTime(DateUtil.getDateStr(item.getProgramedDate()));
        } else {
            receiptSheetCaptureDTO.setDateTime(r.getDateTime());
            receiptSheetCaptureDTO.setBuyId(r.getBuyId());
            receiptSheetCaptureDTO.setOdc(r.getOdc());
            receiptSheetCaptureDTO.setReceiptDate(r.getReceiptDate());
            receiptSheetCaptureDTO.setPlatform(r.getPlatform());
            receiptSheetCaptureDTO.setNumReceivers(r.getNumReceivers());
            receiptSheetCaptureDTO.setDeliveryman(r.getDeliveryman());
            receiptSheetCaptureDTO.setReceiver(r.getReceiver());
            receiptSheetCaptureDTO.setFacturaRef(r.getFacturaRef());
            receiptSheetCaptureDTO.setLevelId(r.getLevelId());
            receiptSheetCaptureDTO.setIva(r.getIva());
            receiptSheetCaptureDTO.setIeps(r.getIeps());
            receiptSheetCaptureDTO.setDateTypeId(r.getDateTypeId());
            receiptSheetCaptureDTO.setArriveTime(r.getArriveTime());
//            receiptSheetCaptureDTO.setPaletizado(r.getPaletizado());
//            receiptSheetCaptureDTO.setAmountEm(r.getAmountEm());
            receiptSheetCaptureDTO.setAmountFact(r.getAmountFact());
        }
        receiptSheetCaptureDTO.setArrive(Constants.ARRIVE);
        receiptSheetCaptureDTO.setDeliveryTime(DateUtil.getTimeNowStr());


        captureDocCompras.setReceiptSheet(receiptSheetCaptureDTO);
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.insertReceiptSheet(captureDocCompras).enqueue(new Callback<CaptureDocCompras>() {
            @Override
            public void onResponse(Call<CaptureDocCompras> call, Response<CaptureDocCompras> response) {
                if (response.isSuccessful()) {
                    startDelivery(response.body());
                }
            }

            @Override
            public void onFailure(Call<CaptureDocCompras> call, Throwable t) {
                Toast.makeText(mContext, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                btnInit.setEnabled(true);
                Log.e(TAG, "sdfgb");
            }
        });
    }

    private void startDelivery(CaptureDocCompras captureDocCompras) {
        BuyDTO buy = captureDocCompras.getBuy();
        item = databaseManager.getById(buy.getId(), Buy.class);
        item.setChecked(buy.getChecked());
        databaseManager.update(item);
        setStatus();
//        btnInit.setEnabled(false);
//        btnFinish.setEnabled(true);

        ReceiptSheetCaptureDTO r = captureDocCompras.getReceiptSheet();
        ReceiptSheetCapture receiptSheetCapture = databaseManager.findReceiptSheetCaptureBy(item.getId());
        if (receiptSheetCapture == null) {
            receiptSheetCapture = new ReceiptSheetCapture();
            receiptSheetCapture.setBuyId(item.getId());
            receiptSheetCapture.setOdc(item.getFolio());
        }
        receiptSheetCapture.setArrive(r.getArrive());
        receiptSheetCapture.setReceiptDate(r.getReceiptDate());
        receiptSheetCapture.setDateTime(r.getDateTime());
        receiptSheetCapture.setDeliveryTime(r.getDeliveryTime());
        receiptSheetCapture.setPlatform(r.getPlatform());
        receiptSheetCapture.setNumReceivers(r.getNumReceivers());
        databaseManager.insertOrUpdate(receiptSheetCapture);

        setItems();
//        adapter = new EditBuyDetailRecyclerViewAdapter(getActivity(), items, mListener, complaints, checks);
//        recyclerView.setAdapter(adapter);
//        adapter.enabledClickListener();
//        adapter.notifyDataSetChanged();
    }

    private void getDatabaseInfo() {
        setItems();
//        View emptyView = viewSwitcher.findViewById(R.id.emptyView);
        if (items.isEmpty()) {
//            recyclerView = (RecyclerView) viewSwitcher.findViewById(R.id.listAll);
//            swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
//                @Override
//                public boolean canChildScrollUp() {
//                    return false;
//                }
//            });
//            recyclerView.setVisibility(View.GONE);
//            emptyView.setVisibility(View.VISIBLE);
        } else {

            downloadUpdate();

//            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
//            List<ComplaintCat> complaints = databaseManager.listComplaintBy(RECEIPT_SHEET_TYPE);
//            List<Check> checks = databaseManager.listAll(Check.class);
//            adapter = new EditBuyDetailRecyclerViewAdapter(getActivity(), items, mListener, complaints, checks);
//            initRecyclerView();
//            recyclerView.setVisibility(View.VISIBLE);
//            recyclerView.setAdapter(adapter);
//            viewSwitcher.setDisplayedChild(1);
        }
    }

    private void downloadUpdate() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.getBuyDetails(item.getFolio(), String.valueOf(item.getRegionId()), item.getProviderId()).enqueue(new Callback<List<BuyDetailAndCaptureDTO>>() {
            @Override
            public void onResponse(Call<List<BuyDetailAndCaptureDTO>> call, Response<List<BuyDetailAndCaptureDTO>> response) {
                if (response.isSuccessful()) {
                    fillListUpdate(response.body());
                } else {
//                    viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//                    viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
                }
//                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<BuyDetailAndCaptureDTO>> call, Throwable t) {
//                swipeRefresh.setRefreshing(false);
//                viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
//                viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
                viewSwitcher.setDisplayedChild(1);
                Log.e(TAG, "error update");
            }
        });

    }

    private void fillListUpdate(List<BuyDetailAndCaptureDTO> list) {
        if (list == null) {
//            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//            viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
        } else {
//            initRecyclerView();
//            items = list;
            createItems(list);
            update();
//            save();
//            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
//            viewSwitcher.findViewById(R.id.list).setVisibility(View.VISIBLE);
        }
        viewSwitcher.setDisplayedChild(1);
    }

    private void setItems() {
        item = databaseManager.getById(item.getId(), Buy.class);
        items = item.getBuyDetails();
        Collections.sort(items, new Comparator<BuyDetail>() {
            @Override
            public int compare(BuyDetail bd1, BuyDetail bd2) {
                return bd1.getArticle().getDescription().compareTo(bd2.getArticle().getDescription());
            }
        });

        List<ComplaintCat> complaints = databaseManager.listComplaintBy(RECEIPT_SHEET_TYPE);
        List<Check> checks = databaseManager.listAll(Check.class);
        adapter = new EditBuyDetailRecyclerViewAdapter(getActivity(), items, mListener, complaints, checks);
        initRecyclerView();
        recyclerView.setAdapter(adapter);
    }

    @SuppressWarnings("unchecked")
    private void downloadBuys() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.getBuyDetails(item.getFolio(), String.valueOf(item.getRegionId()), item.getProviderId()).enqueue(new Callback<List<BuyDetailAndCaptureDTO>>() {
            @Override
            public void onResponse(Call<List<BuyDetailAndCaptureDTO>> call, Response<List<BuyDetailAndCaptureDTO>> response) {
                if (response.isSuccessful()) {
                    fillList(response.body());
                } else {
//                    viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//                    viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
                }
//                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<BuyDetailAndCaptureDTO>> call, Throwable t) {
//                swipeRefresh.setRefreshing(false);
                TextView txt = viewSwitcher.findViewById(R.id.txtEmpty);
                txt.setText(t.getLocalizedMessage());
//                viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//                viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
                viewSwitcher.setDisplayedChild(1);
                Log.e(TAG, mContext.getString(R.string.error_odc));
            }
        });
    }

    private void fillList(List<BuyDetailAndCaptureDTO> list) {
        if (list != null) {
//            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
//            viewSwitcher.findViewById(R.id.list).setVisibility(View.GONE);
        } else {
//            initRecyclerView();
//            items = list;
            createItems(list);
//            update();
            save();
            viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
            viewSwitcher.findViewById(R.id.list).setVisibility(View.VISIBLE);
        }
        viewSwitcher.setDisplayedChild(1);
    }

    private void update() {
        databaseManager.insertOrUpdateBuyDetails(items);
//        items = databaseManager.listBuys(mDate, mContext.getRegion());
        setItems();
//        List<ComplaintCat> complaints = databaseManager.listComplaintBy(RECEIPT_SHEET_TYPE);
//        List<Check> checks = databaseManager.listAll(Check.class);
//        adapter = new EditBuyDetailRecyclerViewAdapter(getActivity(), items, mListener, complaints, checks);
//        initRecyclerView();
//        recyclerView.setAdapter(adapter);
    }

    private void createItems(List<BuyDetailAndCaptureDTO> list) {
        items = new ArrayList<>();
        captures = new ArrayList<>();
        boolean complete = true;
        for (BuyDetailAndCaptureDTO a : list) {
            BuyDetailDTO d = a.getBuyDetail();
            VArticle article = databaseManager.findViewArticleByIclave(d.getIclave());
//            VArticle article = databaseManager.findViewArticleForPurchasing(d.getIclave(), d.getBarcode());//para obtener icompras=1
//            long articleId = databaseManager.findViewArticleIdForPurchasing(d.getIclave(), d.getBarcode());
            BuyDetail b = new BuyDetail();
//            b.setArticle(article);
            b.setArticleId(article.getId());
            b.setArticleAmount(d.getArticleAmount());
            b.setBalance(d.getBalance());
            b.setBuyId(item.getId());
//            b.setBuy(item);
            ReceiptSheetDetailCapture c = a.getCapture();
            if (c == null) {
                complete = false;
            } else {
                b.setReceiptSheetCaptureId(databaseManager.insertOrUpdate(c));
            }
            items.add(b);
//            captures.add(c);
        }

        if (complete) {
            btnFinish.setEnabled(true);
        }
    }

    private void save() {
//        setId(items, item.getId());
//        databaseManager.insertOrReplaceInTx(captures.toArray(new ReceiptSheetDetailCapture[captures.size()]));
        databaseManager.insertOrReplaceInTx(items.toArray(new BuyDetail[items.size()]));
        setItems();
//        List<ComplaintCat> complaints = databaseManager.listComplaintBy(RECEIPT_SHEET_TYPE);
//        List<Check> checks = databaseManager.listAll(Check.class);
//        adapter = new EditBuyDetailRecyclerViewAdapter(getActivity(), items, mListener, complaints, checks);
//        initRecyclerView();
//        recyclerView.setAdapter(adapter);
    }

//    private void setId(List<BuyDetail> list, Long id) {
//        for (BuyDetail buyDetail : list){
//            buyDetail.setBuyId(id);
//        }
//    }

    private void initRecyclerView() {
        recyclerView = viewSwitcher.findViewById(R.id.list);
//        swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
//            @Override
//            public boolean canChildScrollUp() {
//                return /*recyclerView.getFirstVisiblePosition() > 0 ||*/ //con layout manager ya que depende si es liner o grid
//                        recyclerView.getChildAt(0) == null || recyclerView.getChildAt(0).getTop() < 0;
//            }
//        });
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

    public void collapseAll(LinearLayout llCaptureSheetDetail) {
        collapseAll();
        llCaptureSheetDetail.setVisibility(View.VISIBLE);
    }

    private void collapseAll() {
        int count = recyclerView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = recyclerView.getChildAt(i);
            child.findViewById(R.id.capture_sheet_detail).setVisibility(View.GONE);
        }
    }

    public void save(ReceiptSheetDetailCapture c) {
//        ReceiptSheetCapture receiptSheetCapture = databaseManager.findReceiptSheetCaptureBy(item.getId());
//        c.setReceiptSheetId(receiptSheetCapture.getId());
        databaseManager.insertOrReplaceInTx(c);
        BuyDetail buyDet = databaseManager.getById(c.getBuyDetailId(), BuyDetail.class);
        buyDet.setReceiptSheetCaptureId(c.getId());
        databaseManager.update(buyDet);
        Toast.makeText(mContext, "Guardado", Toast.LENGTH_LONG).show();
        setItems();
        if (isComplete()) {
//            showSend();
            btnFinish.setEnabled(true);
        }
    }

    private void showSend() {
        menu.findItem(R.id.action_send).setVisible(true);
    }

    private boolean isComplete() {
        for (BuyDetail d : items) {
            Long rscId = d.getReceiptSheetCaptureId();
            if (rscId == null || rscId < 1L) {
                return false;
            }
        }
        return true;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(BuyDetail buy);

        void onFragmentInteraction(EditBuyDetailRecyclerViewAdapter.BuyDetailHolder holder, int position);

        void showCalendar(EditText txtExpiryDate, int extraDays);

        boolean onSaveDetail(EditBuyDetailRecyclerViewAdapter.BuyDetailHolder holder);

        void startTime();

        void stopTime();

        ScanTimeDTO getTimer();

        void setBaseTime(String date, String deliveryTime);
    }
}
