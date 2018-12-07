package com.tiendas3b.almacen.shipment.epackage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ViewSwitcher;

import com.jetradar.desertplaceholder.DesertPlaceholder;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.ScannerActivity;
import com.tiendas3b.almacen.db.dao.EPackage;
import com.tiendas3b.almacen.db.dao.EpackageDownload;
import com.tiendas3b.almacen.db.dao.EpackageTruck;
import com.tiendas3b.almacen.db.dao.ProviderEpackage;
import com.tiendas3b.almacen.db.dao.Region;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.dao.TruckPackage;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.epackage.TruckPackageDTO;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.BluetoothUtil;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.NetworkUtil;
import com.tiendas3b.almacen.util.printer.TscSdk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import mbanje.kurt.fabbutton.FabButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class EpackageDownloadActivity extends ScannerActivity implements EpackageListener {
    protected static final String TAG = "EpackageDownlodActivity";
    public static final String EXTRA_PROVIDER_ID = "EXTRA_PROVIDER_ID";
    public static final String EXTRA_DATE = "EXTRA_DATE";

    public static final int RECEIPT_PENDING = 3;
    public static final int RECEIPT_SEMIRECEPIT = 20;
    public static final int RECEIPT_RECEIVED = 12;
    public static final int PICKING_CAGE_IN = 13;
    public static final int PICKING_CAGE_OUT = 14;
    public static final int AUDIT_STOWAGE = 15;
    public static final int SHIPMENT_RECEIVED = 4;
    public static final int SHIPMENT_TO_STORE = 5;
    //    public static final int SHIPMENT_TRUCK = 16;
    public static final int STORE_RECEIVED = 6;

    @BindView(R.id.switcher)
    ViewSwitcher viewSwitcher;
    @BindView(R.id.emptyView)
    DesertPlaceholder emptyView;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.txtBarcode)
    EditText txtBarcode;
//    @BindView(R.id.emptyMessage)
//    TextView lblEmptyMessage;

    protected List<EPackage> items;
    //protected List<EPackage> itemsTruckIn;
    protected GlobalState mContext;
    protected IDatabaseManager db;
    protected int status;
    protected int targetStatus;
    private EpackageRvAdapter adapter;
    private MenuItem btnSend;
    private ProviderEpackage provider;
    private String dateStr;
    protected Date selectedDate;
    private long nowFecha = 0;

    public static final String EXTRA_STORE = "EXTRA_STORE";
    private Store store;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        getMenuInflater().inflate(R.menu.menu_send, menu);
        btnSend = menu.findItem(R.id.action_send);
        if(items == null){
            btnSend.setVisible(false);
        }
//        List<EPackage> tmpBtn = db.listEPackagesDownload(status, store.getId());
//        if(tmpBtn == null){
//            btnSend.setVisible(false);
//        }
        return true;
    }

    //METODO PARA IMPRIMIR TICKET
    private void printTicket(List<EPackage> ePackage) {
        BluetoothUtil.startBluetooth();
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bondedDevices = btAdapter.getBondedDevices();
        for (BluetoothDevice btDevice : bondedDevices) {
            String name = btDevice.getName();
            Log.d(TAG, "for BT:" + name);
            System.out.println("*******TscSdk.NAME: "+ TscSdk.NAME);
            System.out.println("*******name: "+name);
            if (TscSdk.NAME.equals(name)) {
                printTruck(ePackage, btDevice.getAddress());
            }
        }
    }

    @SuppressLint("UseSparseArrays")
    private void printTruck(List<EPackage> ePackage, String address) {
        Log.d(TAG, "printTsc address:" + address);

//        StringBuilder ticketDetails = new StringBuilder();
        String tiendas3B = "           Tiendas 3B";
//        ticketDetails.append(tiendas3B).append("\n");|
        String almacen = "Almacen:      " + db.getById(mContext.getRegion(), Region.class).getName();
//        ticketDetails.append(almacen).append("\n");
        //String proveedor = "Proveedor:    " + provider.getName();
//        ticketDetails.append(proveedor).append("\n");
        String ticketDate = "Fecha Embarque: " + DateUtil.getTodayWithTimeStr();
//        ticketDetails.append(ticketDate).append("\n");
        String ticketStore = "Tienda: " + store.getName();

        TscSdk printer = new TscSdk();
        if (printer.openPort(address)) {
            printer.setup(70, (((ePackage.size() + ePackage.size()) + 4) * (ePackage.size()) + 75), 4, 4, 0, 0, 0);
            printer.cls();
            printer.text(40, tiendas3B);
            printer.text(70, almacen);
            //printer.text(100, proveedor);
            printer.text(100, ticketDate);
            printer.text(130, ticketStore);
            printer.text(160, "");
            int y = 190;
            printer.text(y, "");

            int granTotal = 0;

            for(EPackage beanEPackage : ePackage){
                TruckPackage truckPackage = db.findTruckPackageByIdPackage(beanEPackage.getId());
                ProviderEpackage provider = db.findProviderEpackageBy(beanEPackage.getProviderId());
                EpackageTruck truck = db.findEpackageTruckbyIdTruck(truckPackage.getTruckId());
                if(truck == null){
                    downloadEpackageTruckCat();
                    truck = db.findEpackageTruckbyIdTruck(truckPackage.getTruckId());
                }

                printer.text(y += 30, provider.getName() );
                printer.text(y += 30, beanEPackage.getBarcode() );
                printer.text(y += 30, beanEPackage.getDescription() );
                printer.text(y += 30, "Camion   " + truck.getDescription());
                printer.text(y += 30, "");
                granTotal++;
            }

            String ticketTotal = "Total de Paquetes: " + granTotal;

            y += 60;
            printer.text(y, ticketTotal);

            printer.print();
            printer.closePort();
        }

    }


    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epackage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    protected void processBarcode(String barcode) {
        Log.d(TAG, "processBarcode");
        Long idEpackage = (long)0;
        for(EPackage ePackage: items){
            if(ePackage.getBarcode().equals(barcode)){
                idEpackage = ePackage.getId();
            }
        }
        if(idEpackage != 0){
            EPackage epackage = db.findEpackageById(idEpackage);
            if(epackage == null){
                Snackbar.make(viewSwitcher, "No se encontró ese código de barras en el sistema", BaseTransientBottomBar.LENGTH_LONG).show();
            }else {
                onClick(epackage, null);
            }
        }else{
            Snackbar.make(viewSwitcher, "No se encontró ese código de barras en la lista", BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void clearTxt() {
        txtBarcode.setText("");
    }

    private void init() {
        mContext = (GlobalState) getApplicationContext();
        ButterKnife.bind(this);
        db = new DatabaseManager(mContext);
        adapter = new EpackageRvAdapter(this, this);
        recyclerView.setAdapter(adapter);
        emptyView.setOnButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSwitcher.setDisplayedChild(0);
                downloadStatus();
            }
        });
        if (items == null || items.isEmpty()) {
            downloadStatus();
        }else{
            processResponseOfLine(items);
        }
    }

    protected void download() {
//        db.truncate(EPackage.class);
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.getEpackages(mContext.getRegion()).enqueue(new Callback<List<EPackage>>() {
            @Override
            public void onResponse(Call<List<EPackage>> call, Response<List<EPackage>> response) {
                if (response.isSuccessful()) {
                    processResponse(response.body());
                } else {
                    showEmpty();
                }
                viewSwitcher.setDisplayedChild(1);
            }

            @Override
            public void onFailure(Call<List<EPackage>> call, Throwable t) {
                showEmpty(t.getLocalizedMessage());
                viewSwitcher.setDisplayedChild(1);
            }
        });
    }

    protected void downloadStatus() {
//        db.truncate(EPackage.class);
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        System.out.println("*********mContext.getRegion(): "+mContext.getRegion());
        httpService.getEpackages(mContext.getRegion(), status).enqueue(new Callback<List<EPackage>>() {
            @Override
            public void onResponse(Call<List<EPackage>> call, Response<List<EPackage>> response) {
                if (response.isSuccessful()) {
                    processResponse(response.body());
                } else {
                    showEmpty();
                }
                viewSwitcher.setDisplayedChild(1);
            }

            @Override
            public void onFailure(Call<List<EPackage>> call, Throwable t) {
                showEmpty(t.getLocalizedMessage());
                viewSwitcher.setDisplayedChild(1);
            }
        });
    }

    protected void showEmpty(String message) {
        emptyView.setMessage(message);
        showEmpty();
    }

    protected void showEmpty() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        //startActivity(new Intent(mContext, CageInActivity.class));
    }

    private void showData() {
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    protected void processResponseOfLine(List<EPackage> list) {
        if (list == null) {
            showEmpty();
        } else {
            loadItems();
            showData();
            viewSwitcher.setDisplayedChild(1);
        }
    }

    protected void processResponse(List<EPackage> list) {
        if (list == null) {
            showEmpty();
        } else {
            save(list);
            setItems();
            loadItems();
            if (items == null) {
                showEmpty();
            } else {
//                adapter = new EpackageRvAdapter(this, this);
//                adapter.setList(items);
//                recyclerView.setAdapter(adapter);
                showData();
                btnSend.setVisible(true);
            }
        }
    }

    private void save(List<EPackage> list) {
        db.insertOrReplaceInTx(list);
    }

    protected void action(final EPackage ePackage, int originStatus, int targetStatus, final FabButton btn) {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.actionEpackage(mContext.getRegion(), ePackage.getId(), originStatus, targetStatus).enqueue(new Callback<GeneralResponseDTO>() {
            @Override
            public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                if (response.isSuccessful()) {
                    GeneralResponseDTO r = response.body();
                    if (r == null) {
                        Log.e(TAG, "cómo así?!");
                        if(btn != null) {
                            btn.showProgress(false);
                        }
                    } else {
                        if (r.getCode() == 1L) {
                            processSuccess(ePackage);
                        } else {
                            if(btn != null) {
                                btn.showProgress(false);
                            }
                            Snackbar.make(viewSwitcher, r.getDescription(), BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                    }
                } else {
                    if(btn != null) {
                        btn.showProgress(false);
                    }
                    Snackbar.make(viewSwitcher, R.string.response_error, BaseTransientBottomBar.LENGTH_LONG).show();
                    Log.e(TAG, "error1");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
                if(btn != null) {
                    btn.showProgress(false);
                }
                Snackbar.make(viewSwitcher, R.string.process_error, BaseTransientBottomBar.LENGTH_LONG).show();
                Log.e(TAG, "error2");
            }
        });
    }

    protected void processSuccess(EPackage item) {
        Snackbar.make(viewSwitcher, R.string.process_success, BaseTransientBottomBar.LENGTH_LONG).show();

        TelephonyManager tm = (TelephonyManager) getSystemService(mContext.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String imei = tm.getDeviceId();

        EpackageTruck truckByEmei = db.TruckByEmei(mContext.getRegion(), imei, 1);

        if(truckByEmei != null) {
            nowFecha = new Date().getTime();
//        download();
            EpackageDownload epD = new EpackageDownload();
            epD.setActivo(1);
            epD.setAlmacen(item.getRegionId());
            epD.setCamClave(truckByEmei.getTruckId());
            epD.setPackageId(item.getId());
            epD.setStatusLeave(status);
            epD.setTarjetStatus(targetStatus);
            epD.setFecha(DateUtil.getDateStr(nowFecha) + " " + DateUtil.getTimeNowStr());
            epD.setId(item.getId());
            db.insertOrReplaceInTx(epD);

            item.setStatus(targetStatus);
            db.update(item);
            setItems();
//        if(items == null){
//            showEmpty();
//        }

            updateItems();
        }else{
            Snackbar.make(viewSwitcher, "El dispositivo movil no esta relacionado a un camion", BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(EPackage ePackage, FabButton btn) {
        if(!NetworkUtil.isConnected(mContext)) {
            processSuccess(ePackage);
        }else{
            action(ePackage, btn);
        }
    }

//    protected abstract void action(EPackage ePackage);
//    protected abstract void setItems();

    protected void action(EPackage ePackage, FabButton btn) {
        action(ePackage, status, targetStatus, btn);
    }

    protected void setItems() {
        items = db.listEPackages(status);
    }

    private void loadItems() {
        adapter.setList(items);
        adapter.notifyDataSetChanged();
//        if(items == null || items.isEmpty()){
//            showEmpty();
//        }
    }

    protected void updateItems() {
        if (items == null || items.isEmpty()) {
            showEmpty();
        } else {
            //adapter.animateTo(items);
            setItems();
            loadItems();
        }
    }


    protected void downloadPackageTruck() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<TruckPackageDTO>> call = httpService.getTruckPackage(mContext.getRegion());
        call.enqueue(new Callback<List<TruckPackageDTO>>() {
            @Override
            public void onResponse(Call<List<TruckPackageDTO>> call, Response<List<TruckPackageDTO>> response) {
                if (response.isSuccessful()) {
                    List<TruckPackageDTO> list = response.body();
                    if (list != null && !list.isEmpty()) {
                        db.truncate(TruckPackage.class);
                        for (TruckPackageDTO dto : list) {
                            TruckPackage tp = new TruckPackage();
                            //Truck pos = db.findPosition(dto.getAreaId(), dto.getRackId(), dto.getLevelId(), dto.getPositionId());
                            //if(pos != null) {
                                //System.out.println("****pos getAvailable: " + pos.getAvailable());
                            tp.setTruckId(dto.getCamClave());
                            tp.setPackageId(dto.getPackageId());
                            tp.setStatus(dto.getStatus());
                            db.truncate(TruckPackage.class);
                            db.insertOrReplaceInTx(tp);
                           // }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TruckPackageDTO>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
            }
        });
    }

    private void downloadEpackageTruckCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<EpackageTruck>> call = httpService.getEpackageTrucks();
        call.enqueue(new Callback<List<EpackageTruck>>() {
            @Override
            public void onResponse(Call<List<EpackageTruck>> call, Response<List<EpackageTruck>> response) {
                if (response.isSuccessful()) {
                    List<EpackageTruck> list = response.body();
                    db.truncate(EpackageTruck.class);
                    db.insertOrReplaceInTx(list.toArray(new EpackageTruck[list.size()]));
                }
            }

            @Override
            public void onFailure(Call<List<EpackageTruck>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
            }
        });
    }
}
