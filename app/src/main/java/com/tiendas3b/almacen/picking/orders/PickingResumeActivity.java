package com.tiendas3b.almacen.picking.orders;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.crashlytics.android.Crashlytics;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.OrderDetailCapture;
import com.tiendas3b.almacen.db.dao.OrderPickingCapture;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.picking.MvlTiempoPickingDTO;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.DialogUtil;
import com.tiendas3b.almacen.util.NetworkUtil;
import com.tiendas3b.almacen.util.Preferences;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PickingResumeActivity extends AppCompatActivity {

    private static final String TAG = "PickingResumeActivity";
    public static final String EXTRA_STORE_ID = "EXTRA_STORE_ID";
    public static final String EXTRA_SCAN_TIME = "EXTRA_SCAN_TIME";
    private GlobalState mContext;
    private IDatabaseManager db;
    private long storeId;
    private List<OrderDetailCapture> items;

    private String tiempoPickeo = "";
    private String tiempoConfirma = "";
    private String fecha;
    private long nowInitConf = 0;
    private long nowFinConf = 0;
    private MvlTiempoPickingDTO mvlTiempoPickingDTO;

    @BindView(R.id.switcher)
    ViewSwitcher viewSwitcher;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    //    @BindView(R.id.list)
    PickingResumeTableView tableView;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.lblScanTime)
    TextView lblScanTime;

    private Menu menu;
    private MenuItem btnSave;
    private PickingScanTimeDTO scanTime;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_picking_capture, menu);
        btnSave = menu.findItem(R.id.action_send);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send) {
            send();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void send() {



        if(NetworkUtil.isConnectedToWifi(this)) {
            btnSave.setVisible(false);
            progress.setVisibility(View.VISIBLE);
            OrderPickingCaptureDTO captureDTO = getCaptures();
            //inserta tiempo de pickeo
            insertaTiempoPickeo(captureDTO);

            Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
            httpService.insertPickingConfirmation(captureDTO).enqueue(new Callback<GeneralResponseDTO>() {
                @Override
                public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                    GeneralResponseDTO gr = response.body();
                    if (response.isSuccessful() && gr != null && gr.getCode() > 0L) {
                        downloadFile(gr.getDescription());
                        progress.setVisibility(View.INVISIBLE);

                        //Calcula el tiempo de confirmacion
                        nowFinConf = new Date().getTime();
                        tiempoConfirma = DateUtil.getTimeWithoutDateStr(nowFinConf - nowInitConf);
                        mvlTiempoPickingDTO.setTiempofinalconf(DateUtil.getDateStr(nowFinConf) + " " + DateUtil.getTimeNowStr());
                        mvlTiempoPickingDTO.setTiempconfirma(tiempoConfirma);
                        actualizaTimePicking();

                        DialogUtil.showAlertDialog(PickingResumeActivity.this, "Folio SM: " + gr.getCode(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(0);
                                PickingResumeActivity.this.finish();
                            }
                        });
                    } else {
                        progress.setVisibility(View.INVISIBLE);
                        btnSave.setVisible(true);

                        //Calcula el tiempo de confirmacion
                        nowFinConf = new Date().getTime();
                        tiempoConfirma = DateUtil.getTimeWithoutDateStr(nowFinConf - nowInitConf);
                        mvlTiempoPickingDTO.setTiempofinalconf(DateUtil.getDateStr(nowFinConf) + " " + DateUtil.getTimeNowStr());
                        mvlTiempoPickingDTO.setTiempconfirma(tiempoConfirma);
                        actualizaTimePicking();

                        String message = gr == null ? response.code() + "-Null response" : gr.getDescription();
                        Snackbar.make(viewSwitcher, message, Snackbar.LENGTH_INDEFINITE).show();
                    }
                }

                @Override
                public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
                    progress.setVisibility(View.INVISIBLE);
                    btnSave.setVisible(true);
                    String msg = t.getMessage();
                    Snackbar.make(viewSwitcher, msg, Snackbar.LENGTH_INDEFINITE).show();
                    Log.e(TAG, mContext.getString(R.string.error_connection));
                    try {
                        Crashlytics.log(msg);
                        Crashlytics.logException(new Throwable());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            String msg = "No WiFi";
            Snackbar.make(viewSwitcher, msg, Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    //inserta tiempo de pickeo
    public void insertaTiempoPickeo(OrderPickingCaptureDTO captureDTO){
        //22913
        int prodPed = 0;
        int prodPick = 0;
        int prodFalta = 0;
        int prodIncom = 0;
        String circuito = "";
        nowInitConf = new Date().getTime();
        fecha = (DateUtil.getDateStr(nowInitConf) + " " + DateUtil.getTimeNowStr());

        prodPed = items.size();
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).getStatus().equals("Correcto")){
                prodPick = prodPick + 1;
            }else{
                if(items.get(i).getQuantity() == 0){
                    prodFalta = prodFalta + 1;
                }else{
                    prodIncom = prodIncom + 1;
                }
            }
            circuito = items.get(i).getType();
        }

        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        mvlTiempoPickingDTO =
                new MvlTiempoPickingDTO((int) captureDTO.getRegionId(), fecha, String.valueOf(captureDTO.getStoreId()), scanTime.initTime, scanTime.endTime, tiempoPickeo, String.valueOf(prodPed), String.valueOf(prodPick), String.valueOf(prodFalta), String.valueOf(prodIncom), circuito, (DateUtil.getDateStr(nowInitConf) + " " + DateUtil.getTimeNowStr()), null, null);

        httpService.insertTimePicking(mvlTiempoPickingDTO).enqueue(new Callback<GeneralResponseDTO>() {
            @Override
            public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                GeneralResponseDTO gr = response.body();
                if (response.isSuccessful() && gr.getCode() == 1L) {
                    System.out.println("todo bien");
                } else {
                    System.out.println("MAL");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                btnSave.setVisible(true);
                String msg = t.getMessage();
                Snackbar.make(viewSwitcher, msg, Snackbar.LENGTH_INDEFINITE).show();
                Log.e(TAG, mContext.getString(R.string.error_connection));
                try {
                    Crashlytics.log(msg);
                    Crashlytics.logException(new Throwable());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //actualiza tiempo poickeo
    public void actualizaTimePicking(){
        //22913
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.updateTimePicking(mvlTiempoPickingDTO).enqueue(new Callback<GeneralResponseDTO>() {
            @Override
            public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                GeneralResponseDTO gr = response.body();
                if (response.isSuccessful() && gr.getCode() == 1L) {
                    System.out.println("todo bien");
                } else {
                    System.out.println("MAL");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                btnSave.setVisible(true);
                String msg = t.getMessage();
                Snackbar.make(viewSwitcher, msg, Snackbar.LENGTH_INDEFINITE).show();
                Log.e(TAG, mContext.getString(R.string.error_connection));
                try {
                    Crashlytics.log(msg);
                    Crashlytics.logException(new Throwable());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (items == null) {
            PickingResumeActivity.super.onBackPressed();
        } else {
            showWarningDialog();
        }
    }

    private void showWarningDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.dialog_warning));
        alertDialogBuilder.setPositiveButton(getString(android.R.string.ok),
                (dialog, which) -> PickingResumeActivity.super.onBackPressed());
        alertDialogBuilder.setNegativeButton(android.R.string.cancel, null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void downloadFile(final String fileName) {
        String url = new Preferences(this).getSharedStringSafe(Preferences.KEY_URL, Constants.URL_LOCAL);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url.split("a")[0] + "reports/sm/" + fileName));//cambiar ese pinche split XD
        request.setTitle(fileName);
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(mContext, getString(R.string.root_folder).concat(getString(R.string.picking_folder)), fileName);
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);
    }

    private OrderPickingCaptureDTO getCaptures() {
        long regionId = mContext.getRegion();
        List<OrderPickingCapture> capture = db.findOrderCapture(regionId, storeId);
        OrderPickingCaptureDTO r = new OrderPickingCaptureDTO();
        r.setRegionId(regionId);
        r.setStoreId(storeId);
        r.setScanTimeDTO(scanTime);
        ArrayList<OrderCaptureDTO> orders = new ArrayList<>();
        for (OrderPickingCapture o : capture) {
            OrderCaptureDTO dto = new OrderCaptureDTO();
            dto.setPaybill(o.getPaybill());
            List<OrderDetailCaptureDTO> details = new ArrayList<>();
            for (OrderDetailCapture d : o.getDetails()) {
                OrderDetailCaptureDTO detailDTO = new OrderDetailCaptureDTO();
                detailDTO.setIclave(d.getIclave());
                detailDTO.setQuantity(d.getQuantity());
                detailDTO.setType(d.getType());
                detailDTO.setChild(d.getChild());
                detailDTO.setFatherIclave(d.getFatherIclave());
                detailDTO.setPacking(d.getPacking());
                details.add(detailDTO);
            }
            dto.setDetails(details);
            orders.add(dto);
        }
        r.setOrders(orders);
        return r;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picking_resume);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle("prueba");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init() {
        mContext = (GlobalState) getApplicationContext();
        insertTable();
        ButterKnife.bind(this);
        db = new DatabaseManager(mContext);
        Intent intent = getIntent();
        storeId = intent.getLongExtra(EXTRA_STORE_ID, -1L);
        scanTime = intent.getParcelableExtra(EXTRA_SCAN_TIME);
        if(scanTime != null) {
            lblScanTime.setText("Tiempo total:".concat(DateUtil.getTimeWithoutDateStr(scanTime.getEndTimeMillis() - scanTime.getInitTimeMillis())));
            tiempoPickeo = String.valueOf(DateUtil.getTimeWithoutDateStr(scanTime.getEndTimeMillis() - scanTime.getInitTimeMillis()));
        }
        findItems();
        setAdapter();
    }

    private void insertTable() {
        ViewStub stub = findViewById(R.id.layout_stub);
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        int widthPx = dm.widthPixels;
//        int dpiClassification = dm.densityDpi;
        float xDpi = dm.xdpi;
//        float yDpi = dm.ydpi;
        float widthDp = widthPx / (xDpi / 160);
        if (widthDp > 535) {
            stub.setLayoutResource(R.layout.content_picking_resume_normal);
        } else {
            stub.setLayoutResource(R.layout.content_picking_resume_scroll_h);
        }
        View inflated = stub.inflate();

        tableView = inflated.findViewById(R.id.list);
    }

    private void setAdapter() {
        tableView.setSaveEnabled(false);
        List newItems = new ArrayList<>(items);
        tableView.setDataAdapter(new OrderDetailTableAdapter(mContext, newItems, items));
//        tableView.addDataClickListener();
        viewSwitcher.setDisplayedChild(1);
    }

    private void findItems() {
        items = db.findOrderDetails(mContext.getRegion(), storeId);
    }


}
