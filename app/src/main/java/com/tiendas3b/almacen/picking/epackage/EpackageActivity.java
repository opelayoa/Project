package com.tiendas3b.almacen.picking.epackage;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.jetradar.desertplaceholder.DesertPlaceholder;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.ScannerActivity;
import com.tiendas3b.almacen.db.dao.EPackage;
import com.tiendas3b.almacen.db.dao.Position;
import com.tiendas3b.almacen.db.dao.PositionPackage;
import com.tiendas3b.almacen.db.dao.ProviderEpackage;
import com.tiendas3b.almacen.db.dao.Region;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.epackage.PositionPackageDTO;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.picking.epackage.EpackageListener;
import com.tiendas3b.almacen.picking.epackage.EpackageRvAdapter;
import com.tiendas3b.almacen.receipt.epackage.EpackageProviderActivity;
import com.tiendas3b.almacen.util.BluetoothUtil;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.DialogUtil;
import com.tiendas3b.almacen.util.NetworkUtil;
import com.tiendas3b.almacen.util.printer.TscSdk;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import mbanje.kurt.fabbutton.FabButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class EpackageActivity extends ScannerActivity implements EpackageListener {
    protected static final String TAG = "EpackageActivity";
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
    protected List<EPackage> itemsCageIn;
    protected GlobalState mContext;
    protected IDatabaseManager db;
    protected int status;
    protected int targetStatus;
    private EpackageRvAdapter adapter;
    private MenuItem btnSend;
    private ProviderEpackage provider;
    private String dateStr;

    public static final String EXTRA_STORE = "EXTRA_STORE";
    private Store store;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        getMenuInflater().inflate(R.menu.menu_send, menu);
        btnSend = menu.findItem(R.id.action_send);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_send:
                //downloadStatusCageIn();
                validate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void validate() {
        Intent intent = getIntent();
        send();
    }

    private void send() {
        //mandar a imprimir
//        dateStr = DateUtil.getDateStr(new Date());
//        items = db.listEPackages(com.tiendas3b.almacen.picking.epackage.EpackageActivity.PICKING_CAGE_IN, dateStr);
        if(status == 12) {
            itemsCageIn = db.listEPackages(EpackageActivity.PICKING_CAGE_IN);
            if (itemsCageIn != null) {
                //downloadPackagePosition();
                Toast.makeText(mContext, "Imprimiendo ticket", Toast.LENGTH_SHORT).show();
                printTicket(itemsCageIn);
                //finish();
                //startActivity(new Intent(mContext, CageInActivity.class));
            } else {
                Snackbar.make(viewSwitcher, "No se ha resguardado ningun paquete el día de hoy", BaseTransientBottomBar.LENGTH_LONG).show();
            }
        }else{
            store = (Store) getIntent().getSerializableExtra(EXTRA_STORE);
            items = db.listEPackages(targetStatus, store.getId());
            //ProviderEpackage provider = db.findProviderEpackageBy(items);
            if(items != null){
                printTicket(items);
            }else {
                Snackbar.make(viewSwitcher, "No se ha retirado ningun paquete el día de hoy", BaseTransientBottomBar.LENGTH_LONG).show();
            }
        }

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
                if(status == 12) {
                    printTsc(ePackage, btDevice.getAddress());
                }else{
                    printTscPicking(ePackage, btDevice.getAddress());
                }
                break;
            }
        }
    }

    @SuppressLint("UseSparseArrays")
    private void printTscPicking(List<EPackage> ePackage, String address) {
        HashMap<Integer, CageIn> sumHash = new HashMap<>();
        Log.d(TAG, "printTsc address:" + address);

//        StringBuilder ticketDetails = new StringBuilder();
        String tiendas3B = "           Tiendas 3B";
//        ticketDetails.append(tiendas3B).append("\n");
        String almacen = "Almacen:      " + db.getById(mContext.getRegion(), Region.class).getName();
//        ticketDetails.append(almacen).append("\n");
        //String proveedor = "Proveedor:    " + provider.getName();
//        ticketDetails.append(proveedor).append("\n");
        String ticketDate = "Fecha Resguardo: " + DateUtil.getTodayWithTimeStr();
//        ticketDetails.append(ticketDate).append("\n");


        TscSdk printer = new TscSdk();
        if (printer.openPort(address)) {
            printer.setup(70, (((ePackage.size() + ePackage.size()) + 4) * (ePackage.size()) + 75), 4, 4, 0, 0, 0);
            printer.cls();
            printer.text(40, tiendas3B);
            printer.text(70, almacen);
            //printer.text(100, proveedor);
            printer.text(100, ticketDate);
            printer.text(130, "");
            int y = 160;
            printer.text(y, "");

            int granTotal = 0;

            for(EPackage beanEPackage : ePackage){
                ProviderEpackage provider = db.findProviderEpackageBy(beanEPackage.getProviderId());

                printer.text(y += 30, provider.getName() );
                printer.text(y += 30, beanEPackage.getBarcode() );
                printer.text(y += 30, beanEPackage.getDescription() );
                printer.text(y += 30, store.getName());
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

    @SuppressLint("UseSparseArrays")
    private void printTsc(List<EPackage> ePackage, String address) {
        HashMap<Integer, CageIn> sumHash = new HashMap<>();
        Log.d(TAG, "printTsc address:" + address);

//        StringBuilder ticketDetails = new StringBuilder();
        String tiendas3B = "           Tiendas 3B";
//        ticketDetails.append(tiendas3B).append("\n");
        String almacen = "Almacen:      " + db.getById(mContext.getRegion(), Region.class).getName();
//        ticketDetails.append(almacen).append("\n");
        //String proveedor = "Proveedor:    " + provider.getName();
//        ticketDetails.append(proveedor).append("\n");
        String ticketDate = "Fecha Resguardo: " + DateUtil.getTodayWithTimeStr();
//        ticketDetails.append(ticketDate).append("\n");


        TscSdk printer = new TscSdk();
        if (printer.openPort(address)) {
            printer.setup(70, (((ePackage.size() + ePackage.size()) + 4) * (ePackage.size()) + 75), 4, 4, 0, 0, 0);
            printer.cls();
            printer.text(40, tiendas3B);
            printer.text(70, almacen);
            //printer.text(100, proveedor);
            printer.text(100, ticketDate);
            printer.text(130, "");
            int y = 160;
            printer.text(y, "");

            int granTotal = 0;

            for(EPackage beanEPackage : ePackage){
                PositionPackage positionPackage = db.getBy(beanEPackage.getId());
                //PositionPackage positionPackage2 = db.getByPosition(Long.valueOf(1));
                //System.out.println("positionPackage getPosition: " + positionPackage.getPosition());
                //System.out.println("positionPackage: getPositionId" + positionPackage.getPositionId());

                String cadena = positionPackage.getPosition() + "";
                String cadenaTemp1 = "";
                String cadenaTemp2 = "";

                if(cadena.length() > 27) {
                    cadenaTemp1 = cadena.substring(0, 27);
                    cadenaTemp2 = cadena.substring(28, cadena.length());
                }

                printer.text(y += 30, beanEPackage.getProvider().getName() );
                printer.text(y += 30, beanEPackage.getBarcode() );
                printer.text(y += 30, beanEPackage.getDescription() );
                if(cadena.length() > 27) {
                    printer.text(y += 30, cadenaTemp1);
                    printer.text(y += 30, cadenaTemp2);
                }else {
                    printer.text(y += 30, cadena);
                }
                printer.text(y += 30, "");
                granTotal++;
            }

            String ticketTotal = "Total de Paquetes: " + granTotal;

            //printer.text(y, "");
            y += 60;
            printer.text(y, ticketTotal);

            printer.print();
            printer.closePort();
        }

    }

    class CageIn {

        private int total;
        private int iclave;
        private String desc;
        private String proveedor;
        private String ubicacion;

        public CageIn(int total, Integer key, String description, String proveedor, String ubicacion) {
            this.total = total;
            iclave = key;
            desc = description;
            proveedor = proveedor;
            ubicacion = ubicacion;
        }

        public int getIclave() {
            return iclave;
        }

        public void setIclave(int iclave) {
            this.iclave = iclave;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getProveedor() {
            return proveedor;
        }

        public void setProveedor(String proveedor) {
            this.proveedor = proveedor;
        }

        public String getUbicacion() {
            return ubicacion;
        }

        public void setUbicacion(String ubicacion) {
            this.ubicacion = ubicacion;
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
        EPackage epackage = db.findEpackageBy(barcode);
        if (epackage == null) {
            Snackbar.make(viewSwitcher, "No se encontró ese código de barras", BaseTransientBottomBar.LENGTH_LONG).show();
        } else {
            onClick(epackage, null);
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
        if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
            downloadStatus();
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

    protected void downloadStatusCageIn() {
//        db.truncate(EPackage.class);
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        System.out.println("*********mContext.getRegion(): "+mContext.getRegion());
        httpService.getEpackages(mContext.getRegion(), targetStatus).enqueue(new Callback<List<EPackage>>() {
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
//        download();
        item.setStatus(targetStatus);
        db.update(item);
        setItems();
//        if(items == null){
//            showEmpty();
//        }
        updateItems();
    }

    @Override
    public void onClick(EPackage ePackage, FabButton btn) {
        action(ePackage, btn);
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


    protected void downloadPackagePosition() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<PositionPackageDTO>> call = httpService.getPositionPackage(mContext.getRegion());
        call.enqueue(new Callback<List<PositionPackageDTO>>() {
            @Override
            public void onResponse(Call<List<PositionPackageDTO>> call, Response<List<PositionPackageDTO>> response) {
                if (response.isSuccessful()) {
                    List<PositionPackageDTO> list = response.body();
                    if (list != null && !list.isEmpty()) {
                        db.truncate(PositionPackage.class);
                        for (PositionPackageDTO dto : list) {
                            PositionPackage pp = new PositionPackage();
                            Position pos = db.findPosition(dto.getAreaId(), dto.getRackId(), dto.getLevelId(), dto.getPositionId());
                            if(pos != null) {
                                System.out.println("pos: " + pos.getAvailable());
                                pp.setPositionId(pos.getId());
                                pp.setPackageId(dto.getPackageId());
                                pp.setStatus(dto.isStatus());
                                db.insertOrReplaceInTx(pp);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PositionPackageDTO>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
            }
        });
    }
}
