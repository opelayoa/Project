package com.tiendas3b.almacen.receipt.epackage;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
import com.tiendas3b.almacen.db.dao.ProviderEpackage;
import com.tiendas3b.almacen.db.dao.Region;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.receipt.express2.ExpressReceiptActivity;
import com.tiendas3b.almacen.util.BarcodeUtil;
import com.tiendas3b.almacen.util.BluetoothUtil;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.DialogUtil;
import com.tiendas3b.almacen.util.FileUtil;
import com.tiendas3b.almacen.util.printer.TscSdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import mbanje.kurt.fabbutton.FabButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EpackageActivity extends ScannerActivity implements EpackageListener {
    private static final String TAG = "EpackageActivity";
    public static final String EXTRA_PROVIDER_ID = "EXTRA_PROVIDER_ID";
    public static final String EXTRA_DATE = "EXTRA_DATE";

    //    @BindView(R.id.txtProvider)
//    EditText txtProvider;
//    @BindView(R.id.txtFolio)
//    EditText txtFolio;
//    @BindView(R.id.txt)
//    EditText ;
//    @BindView(R.id.txt)
//    EditText ;
//    @BindView(R.id.txt)
//    EditText ;
//    @BindView(R.id.txt)
//    EditText ;
    @BindView(R.id.switcher)
    ViewSwitcher viewSwitcher;
    @BindView(R.id.emptyView)
    DesertPlaceholder emptyView;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.txtBarcode)
    EditText txtBarcode;

    List<EPackage> items;
    private GlobalState mContext;
    private IDatabaseManager db;
    private EpackageRvAdapter adapter;
    private EpackageAutRvAdapter adapterAut;
    private ProviderEpackage provider;
    private String dateStr;
    private EPackage epackage;
    private MenuItem btnSend;
    private Call<List<EPackage>> call;
    private String tipoEpackage;
    private boolean useCam;
    List<EPackage> listaEpackage = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        tipoEpackage = String.valueOf(intent.getSerializableExtra("tipoEpackage"));

        getMenuInflater().inflate(R.menu.menu_send, menu);
        if(tipoEpackage.equals("Automatico")) {
            menu.findItem(R.id.action_cam_scan).setVisible(true);
        }
        btnSend = menu.findItem(R.id.action_send);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_send:
                validate();
                return true;
            case R.id.action_cam_scan:
                //useCam = !item.isChecked();
                useCam = false;
                item.setChecked(useCam);
                BarcodeUtil.intentBarcodeReader(com.tiendas3b.almacen.receipt.epackage.EpackageActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void validate() {
        Intent intent = getIntent();
        tipoEpackage = String.valueOf(intent.getSerializableExtra("tipoEpackage"));
        System.out.println("************tipoEpackage: "+tipoEpackage);
        if(!tipoEpackage.equals("Automatico")) {
            if (items != null && items.size() > 0) {
                DialogUtil.showAlertDialog(this, "Faltan " + items.size() + " por procesar. ¿continuar?", "Aceptar", "Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        send();
                    }
                }, null);
            } else {
                send();
            }
        }else{
            send();
        }
    }


    //Lo que regresa el scan con la camara
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        db = DatabaseManager.getInstance(this);
        if (requestCode == BarcodeUtil.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra(BarcodeUtil.SCAN_RESULT);
                processBarcode(contents);
            }
//            else if (resultCode == RESULT_CANCELED) {
//                txtBarcode.clearFocus();
//            }
        }
    }

    private void send() {
        Toast.makeText(mContext, "Imprimiendo ticket", Toast.LENGTH_SHORT).show();
        //mandar a imprimir
        items = db.listEPackages(provider.getId(), com.tiendas3b.almacen.picking.epackage.EpackageActivity.RECEIPT_RECEIVED, dateStr);
        printTicket(items);
        finish();
        startActivity(new Intent(mContext, EpackageProviderActivity.class));
    }

    //METODO PARA IMPRIMIR TICKET
    private void printTicket(List<EPackage> ePackage) {
        BluetoothUtil.startBluetooth();
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bondedDevices = btAdapter.getBondedDevices();
        for (BluetoothDevice btDevice : bondedDevices) {
            String name = btDevice.getName();
            Log.d(TAG, "for BT:" + name);
            System.out.println("*******TscSdk.NAME: "+TscSdk.NAME);
            System.out.println("*******name: "+name);
            if (TscSdk.NAME.equals(name)) {
                printTsc(ePackage, btDevice.getAddress());
                break;
            }
        }
    }

    @SuppressLint("UseSparseArrays")
    private void printTsc(List<EPackage> ePackage, String address) {
        HashMap<Integer, EpackageActivity.PalPutoTicket> sumHash = new HashMap<>();
        HashMap<Integer, String> llaves = new HashMap<>();
        long region = 0;
        Log.d(TAG, "printTsc address:" + address);
        Integer llave2 = 0;
        for (EPackage r : ePackage) {
            Integer llave = 0;
            int temp = 0;
            if(llaves.size() > 0){
                for(int i=0; i<llaves.size(); i++) {
                    if (llaves.get(i).equals(r.getDescription())) {
                        llave2 = i;
                        temp = 1;
                    }else{
                        temp = 2;
                    }
                }
                if (temp == 1) {
                    llave = llave2;
                }else if(temp == 2){
                    llave2 ++;
                    llave = llave2;
                }
            }

            region = r.getRegionId();
            if (sumHash.containsKey(llave)) {
                sumHash.get(llave).setTotal(sumHash.get(llave).getTotal() + 1);
            } else {
                sumHash.put(llave, new com.tiendas3b.almacen.receipt.epackage.EpackageActivity.PalPutoTicket(1, llave, r.getDescription()));
                llaves.put(llave, r.getDescription());
            }
        }

        StringBuilder ticketDetails = new StringBuilder();
        String tiendas3B = "           Tiendas 3B";
        ticketDetails.append(tiendas3B).append("\n");
        String almacen = "Almacen:      " + db.getById(region, Region.class).getName();
        ticketDetails.append(almacen).append("\n");
        String proveedor = "Proveedor:    " + provider.getName();
        ticketDetails.append(proveedor).append("\n");
//        String ticketOdc = "ODC:          " + odc;
//        ticketDetails.append(ticketOdc).append("\n");
        String ticketDate = "Fecha recibo: " + DateUtil.getTodayWithTimeStr();
        ticketDetails.append(ticketDate).append("\n");

        int granTotal = 0;
        for (Integer key : sumHash.keySet()) {
            com.tiendas3b.almacen.receipt.epackage.EpackageActivity.PalPutoTicket o = sumHash.get(key);
            int total = o.getTotal();
            ticketDetails.append(total).append(o.getDesc()).append("\n");
            granTotal += total;
        }

//        String ticketTimes = "Hr Inicio: " + DateUtil.getTimeStr(scanTimeDTO.getInitTimeMillis()) + " Hr Fin: " + DateUtil.getTimeNowStr();
//        ticketDetails.append(ticketTimes).append("\n");
        String ticketTotal = "Total de Paquetes: " + granTotal;
        ticketDetails.append(ticketTotal);

        TscSdk printer = new TscSdk();
        if (printer.openPort(address)) {
            printer.setup(70, (sumHash.size() * 3 + 75), 4, 4, 0, 0, 0);
            printer.cls();
            printer.text(40, tiendas3B);
            printer.text(70, almacen);
            printer.text(100, proveedor);
            printer.text(130, ticketDate);
            printer.text(160, "");
            int y = 190; //210
            printer.text(y, "");
            for (Integer key : sumHash.keySet()) {//hacer mas chido y con ganas
                com.tiendas3b.almacen.receipt.epackage.EpackageActivity.PalPutoTicket o = sumHash.get(key);
                int total = o.getTotal();
                printer.text(y += 30, total + " " + o.getDesc());
            }
            y += 60;
            //printer.text(y += 30, ticketTimes);
            printer.text(y + 30, ticketTotal);

            printer.print();
            printer.closePort();
        }

    }



    class PalPutoTicket {

        private int total;
        private int iclave;
        private String desc;

        public PalPutoTicket(int total, Integer key, String description) {
            this.total = total;
            iclave = key;
            desc = description;
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
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epackage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        init();
        toolbar.setTitle("Proveedor: " + provider.getId() + " - " + provider.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void processBarcode(String barcode) {
        Log.d(TAG, "processBarcode");
        EPackage epackage = db.findEpackageByDate(barcode, dateStr);
        if(epackage == null){
            Snackbar.make(viewSwitcher, "No se encontró ese código de barras", BaseTransientBottomBar.LENGTH_LONG).show();
        } else {
            if (!tipoEpackage.equals("Automatico")) {
                Log.d(TAG, "processBarcode: "+items.size());
                listaEpackage = items;
            }
            if (listaEpackage.size() > 0) {
                int ban = 0;
                for (EPackage epackageLista : listaEpackage) {
                    if (epackageLista.getBarcode().equals(epackage.getBarcode())) {
                        ban++;
                    }
                }
                if (ban > 0) {
                    if (!tipoEpackage.equals("Automatico")) {
                        receive(epackage, null);
                    }else{
                        Snackbar.make(viewSwitcher, "El código de barras ya se encuentra en la lista", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                } else {
                    if (!tipoEpackage.equals("Automatico")) {
                        Snackbar.make(viewSwitcher, "El código de barras no encuentra en la lista", BaseTransientBottomBar.LENGTH_LONG).show();
                    }else {
                        receive(epackage, null);
                    }
                    //listaEpackage.add(epackage);
                }
            } else {
                //jamas en la vida deberia entrar aqui
            }
        }
    }


//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK) {
//
//        }
//        return super.onKeyUp(keyCode, event);
//    }

    @Override
    protected void clearTxt() {
        txtBarcode.setText("");
    }

    private void init() {
        mContext = (GlobalState) getApplicationContext();
        ButterKnife.bind(this);
        db = new DatabaseManager(mContext);

        Intent intent = getIntent();
        tipoEpackage = String.valueOf(intent.getSerializableExtra("tipoEpackage"));
        System.out.println("************tipoEpackage: "+tipoEpackage);
        if(!tipoEpackage.equals("Automatico")) {
            adapter = new EpackageRvAdapter(this, this);
            recyclerView.setAdapter(adapter);
            provider = (ProviderEpackage) intent.getSerializableExtra(EXTRA_PROVIDER_ID);
            dateStr = intent.getStringExtra(EXTRA_DATE);
            if (provider == null) {
                Log.e(TAG, "Aqui no deberia de entrar nunca en la vida");
//            if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
//                downloadStatus();
//            }
            } else {
                setItems();
            }
        }else{
            dateStr = intent.getStringExtra(EXTRA_DATE);
            adapterAut = new EpackageAutRvAdapter(this, this);
            recyclerView.setAdapter(adapterAut);
            //validaciones de un proceso automatico
            epackage = (EPackage) intent.getSerializableExtra(EXTRA_PROVIDER_ID);
            provider = (ProviderEpackage) intent.getSerializableExtra("providerScan");
            System.out.println("************epackage: "+epackage);
            receive(epackage, null);
            //setItemsAut(epackage);
        }

        showData();
        viewSwitcher.setDisplayedChild(1);
    }

    private void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        long regionId = mContext.getRegion();
        if(call != null && call.isExecuted()){
            Log.w(TAG, "cancel");
            call.cancel();
            call = null;
        }
        call = httpService.getProvidersEpackage(regionId, dateStr);
        call.enqueue(new Callback<List<EPackage>>() {
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
                showEmpty();
                viewSwitcher.setDisplayedChild(1);
                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });

//        db.truncate(EPackage.class);
//        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
//        httpService.getEpackages(mContext.getRegion()).enqueue(new Callback<List<EPackage>>() {
//            @Override
//            public void onResponse(Call<List<EPackage>> call, Response<List<EPackage>> response) {
//                if (response.isSuccessful()) {
//                    processResponse(response.body());
//                } else {
//                    showEmpty();
//                }
//                viewSwitcher.setDisplayedChild(1);
//            }
//
//            @Override
//            public void onFailure(Call<List<EPackage>> call, Throwable t) {
//                showEmpty();
//                viewSwitcher.setDisplayedChild(1);
//                Log.e(TAG, "error update");
//            }
//        });
    }

    private void showEmpty() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showData() {
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void processResponse(List<EPackage> list) {
        if (list == null) {
            showEmpty();
        } else {
            save(list);
            setItems();
            showData();
        }
    }

    private void save(List<EPackage> list) {
        db.insertOrReplaceInTx(list);
    }

//    private void getDatabaseInfo() {
////        setItems();
//        if (items.isEmpty()) {
//        } else {
//            downloadUpdate();
//        }
//    }


    private void setItemsAut(EPackage epackage) {
        //listaEpackage.add(epackage);
        //items = listaEpackage;
        System.out.println("****epackage.getReceiptDate(): "+epackage.getReceiptDate());
        items = db.listEPackages(provider.getId(), com.tiendas3b.almacen.picking.epackage.EpackageActivity.RECEIPT_RECEIVED, epackage.getReceiptDate());
        listaEpackage = items;
        updateAutItems();
    }

    private void updateAutItems() {
        adapterAut.setList(items);
        adapterAut.notifyDataSetChanged();
    }

    private void setItems() {
        items = db.listEPackages(provider.getId(), com.tiendas3b.almacen.picking.epackage.EpackageActivity.RECEIPT_PENDING, dateStr);
        updateItems();
    }

//    private void setItemsProvider(ProviderEpackage provider) {
////        items = db.listEPackages(providerId, com.tiendas3b.almacen.picking.epackage.EpackageActivity.RECEIPT_PENDING);
//        items = provider.getPackages();
//        updateItems();
//    }

    private void updateItems() {
        adapter.setList(items);
        adapter.notifyDataSetChanged();
    }

    public void receive(final EPackage item, final FabButton btn) {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        final long id = item.getId();
        Intent intent = getIntent();
        tipoEpackage = String.valueOf(intent.getSerializableExtra("tipoEpackage"));
        System.out.println("************tipoEpackage: "+tipoEpackage);
        httpService.actionEpackage(mContext.getRegion(), id, com.tiendas3b.almacen.picking.epackage.EpackageActivity.RECEIPT_PENDING,
                com.tiendas3b.almacen.picking.epackage.EpackageActivity.RECEIPT_RECEIVED).enqueue(new Callback<GeneralResponseDTO>() {
            @Override
            public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                if (response.isSuccessful()) {
                    GeneralResponseDTO r = response.body();
                    if(r != null && r.getCode() == 1L){
                        processReceive(item);
                    }
                } else {
                    if (btn != null) {
                        btn.showProgress(false);
                    }
                    Snackbar.make(viewSwitcher,
                            R.string.response_error, BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
                if(btn != null) {
                    btn.showProgress(false);
                }
                Log.e(TAG, "error");
            }
        });
    }

    private void processReceive(EPackage item) {
        Snackbar.make(viewSwitcher, "Recibo exitoso", BaseTransientBottomBar.LENGTH_LONG).show();
        item.setStatus(com.tiendas3b.almacen.picking.epackage.EpackageActivity.RECEIPT_RECEIVED);
        db.update(item);
        Intent intent = getIntent();
        tipoEpackage = String.valueOf(intent.getSerializableExtra("tipoEpackage"));
        System.out.println("************tipoEpackage: "+tipoEpackage);
        if(!tipoEpackage.equals("Automatico")) {
            setItems();
        }else{
            setItemsAut(item);
        }
//        db.findEpackageBy(id);
//        downloadStatus();

    }

    @Override
    public void onClick(EPackage ePackage, FabButton btn) {
        receive(ePackage, btn);
    }
}
