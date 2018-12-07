package com.tiendas3b.almacen.receipt.report;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.db.dao.Region;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.picking.orders.ScanTimeDTO;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.NumbersUtil;
import com.tiendas3b.almacen.util.PdfUtil;
import com.tiendas3b.almacen.util.Preferences;
import com.tiendas3b.almacen.views.tables.ReceiptReportTableView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("unchecked")
public class ReceiptReportActivity extends AppCompatActivity {

    private static final String AUTHORITY = "com.tiendas3b.almacen.provider";
    private static final String TAG = "ReceiptReportActivity";
    public static final String EXTRA_SCAN_TIME = "EXTRA_SCAN_TIME";
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private List<ReceiptSheetDetPrintDTO> items;
    private ReceiptSheetPrintDTO item;

    @BindView(R.id.txtDate)
    EditText txtDate;
    @BindView(R.id.txtFolio)
    EditText txtFolio;
    @BindView(R.id.txtBill)
    EditText txtBill;
    @BindView(R.id.txtNames)
    EditText txtNames;
    @BindView(R.id.txtSubtotal)
    EditText txtSubtotal;
    @BindView(R.id.txtIeps)
    EditText txtIeps;
    @BindView(R.id.txtIva)
    EditText txtIva;
    @BindView(R.id.txtTotal)
    EditText txtTotal;
//    @BindView(R.id.lblTittle)
//    TextView lblTittle;
    @BindView(R.id.lblRegion)
    TextView lblRegion;
    @BindView(R.id.lblAddress)
    TextView lblAddress;
    @BindView(R.id.lblTels)
    TextView lblTels;
    @BindView(R.id.lblCaption)
    TextView lblCaption;
    @BindView(R.id.lblProviderName)
    TextView lblProviderName;
    @BindView(R.id.lblProviderAddress)
    TextView lblProviderAddress;
    @BindView(R.id.lblProviderAttention)
    TextView lblProviderAttention;
    @BindView(R.id.lblProviderTels)
    TextView lblProviderTels;
//    @BindView(R.id.lblTerm)
//    TextView lblTerm;
    @BindView(R.id.list)
    ReceiptReportTableView list;
    private ReceiptReportTableDataAdapter adapter;
    private ScanTimeDTO scanTimeDTO;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (GlobalState) getApplicationContext();
        setContentView(R.layout.activity_pdf_receipt);
        init();
    }

    @Override
    protected void onRestart() {
        databaseManager = new DatabaseManager(this);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onStop();
    }

    @Override
    protected void onResume() {
        databaseManager = DatabaseManager.getInstance(this);
        super.onResume();
    }

    private void init() {
        ButterKnife.bind(this);
        databaseManager = new DatabaseManager(mContext);
        String regionIdStr = new Preferences(this).getSharedStringSafe(Preferences.KEY_REGION, "-1");
        long regionId = Long.parseLong(regionIdStr);
        Intent i = getIntent();
        int folio = i.getIntExtra("f", -1);
        int type = i.getIntExtra("t", -1);
        long providerId = i.getLongExtra("p", -1L);
        String billRef = i.getStringExtra("b");
        String names = i.getStringExtra("n");
        scanTimeDTO = i.getParcelableExtra(EXTRA_SCAN_TIME);

        txtFolio.setText(String.format(getFolio(type) + ": %s", folio));
        download(folio, regionId, providerId, billRef);

        Region region = databaseManager.getById(regionId, Region.class);
//        lblTittle.setText(getTitle(type));
        lblRegion.setText(region.getName());
        lblAddress.setText(region.getAddress());
        lblTels.setText("Tels:".concat(region.getTels()));
        txtBill.setText(billRef);
        txtNames.setText(names);
        txtDate.setText(new SimpleDateFormat(DateUtil.DD_MM_YYYY_HH_MM_SS, Locale.getDefault()).format(new Date())
                + " - " + DateUtil.getTimeWithoutDateStr(scanTimeDTO.getEndTimeMillis() - scanTimeDTO.getInitTimeMillis()));
    }

    private void download(int folio, long regionId, long providerId, String billRef) {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.receiptSheetForPrint(folio, regionId, providerId, billRef).enqueue(new Callback<ReceiptSheetPrintDTO>() {
            @Override
            public void onResponse(Call<ReceiptSheetPrintDTO> call, Response<ReceiptSheetPrintDTO> response) {
                if (response.isSuccessful()) {
                    item = response.body();
                    process();
                } else {
                }
            }

            @Override
            public void onFailure(Call<ReceiptSheetPrintDTO> call, Throwable t) {
            }
        });
    }

    private void process() {

        txtFolio.setText(txtFolio.getText() + "  ODC:" + item.getOdc());
        txtSubtotal.setText(NumbersUtil.moneyFormat(item.getSubtotal()));
        txtIeps.setText(NumbersUtil.moneyFormat(item.getIeps()));
        txtIva.setText(NumbersUtil.moneyFormat(item.getIva()));
        txtTotal.setText(NumbersUtil.moneyFormat(item.getTotal()));
        lblCaption.setText(getString(R.string.pdf_caption, item.getTotal()));

        Provider provider = databaseManager.getById(item.getProviderId(), Provider.class);
        lblProviderName.setText(provider.getBusinessName().concat(" ").concat(String.valueOf(provider.getId())));
        lblProviderAddress.setText(provider.getAddress());
        lblProviderAttention.setText("Atención:" + provider.getAtention() + "   Plazo:" + provider.getTerm() + " días");
//        lblTerm.setText("Plazo:" + provider.getTerm() + " días");
        String tel2 = provider.getPhone2();
        lblProviderTels.setText("Tels:" + provider.getPhone1() + ", " + (tel2 == null ? "" : tel2));

        items = item.getDetails();
        Log.i(TAG, "items size:" + items.size());
//        ReceiptReportTableView list = (ReceiptReportTableView) findViewById(R.id.list);
        adapter = new ReceiptReportTableDataAdapter(mContext, items);
        list.setDataAdapter(adapter);
        adapter.notifyDataSetChanged();
//            list.invalidate();

        Log.i(TAG, "finish list");
    }

    private String getFolio(int type) {
        switch (type) {
            case Constants.ME:
                return getString(R.string.pdf_me_folio);
            case Constants.DP:
                return getString(R.string.pdf_dp_folio);
            case Constants.TA:
                return getString(R.string.pdf_ta_folio);
            case Constants.TE:
                return getString(R.string.pdf_te_folio);
            case Constants.EM:
                return getString(R.string.pdf_em_folio);
        }

        return "FOL";
    }

    private int getTitle(int type) {
        switch (type) {
            case Constants.ME:
                return R.string.pdf_me;
            case Constants.DP:
                return R.string.pdf_dp;
            case Constants.TA:
                return R.string.pdf_ta;
            case Constants.TE:
                return R.string.pdf_te;
            case Constants.EM:
                return R.string.pdf_em;
        }

        return R.string.pdf_default_report_name;
    }

//    private void setObservation() {
//        for (ReceiptSheetDetPrintDTO a : items) {
//            a.setObsStr(((ObservationType) databaseManager.getById(a.getObs(), ObservationType.class)).getDescription());
//        }
//    }

    @Override
    protected void onPostResume() {
        Log.i(TAG, "onPostResume");
        super.onPostResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "post i");
                String path = PdfUtil.createPdf(findViewById(R.id.pdf), getString(R.string.receipt_folder));
                File file = new File(path);
                Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                intent.setDataAndType(FileProvider.getUriForFile(mContext, AUTHORITY, file), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
                Log.i(TAG, "post f");
                finish();
            }
        }, 1000);
    }

//    private ReceiptSheetDetPrintDTO getTotal(List<ReceiptSheetDetPrintDTO> articles) {
//        ReceiptSheetDetPrintDTO article = new ReceiptSheetDetPrintDTO();
//        int piecesSum = 0;
//        float costSum = 0.0f;
//        for (ReceiptSheetDetPrintDTO a : articles) {
//            Integer pieces = a.getPieces();
//            if(pieces != null){
//                piecesSum += pieces;
//                costSum += (a.getUnitCost() * pieces);
//            }
//        }
//        article.setPieces(piecesSum);
//        article.setCost(costSum);
//        return article;
//    }

}
