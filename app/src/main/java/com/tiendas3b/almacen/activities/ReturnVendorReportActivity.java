package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Region;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.ReturnVendorCaptureDTO;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.PdfUtil;
import com.tiendas3b.almacen.util.Preferences;
import com.tiendas3b.almacen.views.tables.DecreaseReportTableView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReturnVendorReportActivity extends AppCompatActivity {

    private static final String AUTHORITY="com.tiendas3b.almacen.provider";
    public static final String EXTRA_LIST = "LIST";
    public static final String EXTRA_DESC = "DESC";
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private List<ReturnVendorCaptureDTO> items;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (GlobalState) getApplicationContext();
        setContentView(R.layout.activity_return_vendor_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    @Override
    protected void onStop() {
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onStop();
    }

    private void init() {
        databaseManager = new DatabaseManager(mContext);
        String regionIdStr = new Preferences(this).getSharedStringSafe(Preferences.KEY_REGION, "-1");
        long regionId =  Long.parseLong(regionIdStr);
        Region region = (Region)databaseManager.getById(regionId, Region.class);
        Intent i = getIntent();
        items = (List<ReturnVendorCaptureDTO>) i.getSerializableExtra(EXTRA_LIST);
        String folio = i.getStringExtra(EXTRA_DESC);
        ((TextView) findViewById(R.id.lblRegion)).setText(region.getName());
        ((TextView) findViewById(R.id.lblAddress)).setText(region.getAddress());
        ((TextView) findViewById(R.id.txtFolio)).setText(String.format("ME: %s", folio));
        ((TextView) findViewById(R.id.txtAuthorized)).setText(mContext.getName());
        ((TextView) findViewById(R.id.txtDate)).setText(new SimpleDateFormat(DateUtil.DD_MM_YYYY_HH_MM_SS, Locale.getDefault()).format(new Date()));

//        setObservation();
        items.add(getTotal(items));
        DecreaseReportTableView list = (DecreaseReportTableView) findViewById(R.id.list);
//        list.setDataAdapter(new DecreaseReportTableDataAdapter(mContext, items));
    }

//    private void setObservation() {
//        for (ReturnVendorCaptureDTO a : items) {
//            a.setObservation((ObservationType) db.getById(a.getObs(), ObservationType.class));
//        }
//    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String path = PdfUtil.createPdf(findViewById(R.id.pdf), getString(R.string.renturn_vendor_folder));
                File file = new File(path);
                Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                intent.setDataAndType(FileProvider.getUriForFile(mContext, AUTHORITY, file), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
                finish();

//                File file = new File(path);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                startActivity(intent);
//                finish();
            }
        }, 50);
    }

    private ReturnVendorCaptureDTO getTotal(List<ReturnVendorCaptureDTO> articles) {
        ReturnVendorCaptureDTO article = new ReturnVendorCaptureDTO();
        int piecesSum = 0;
        float costSum = 0.0f;
        for (ReturnVendorCaptureDTO a : articles) {
            Integer pieces = a.getAmount();
            piecesSum += pieces;
//            costSum += (a.getCost() * pieces);
        }
        article.setAmount(piecesSum);
//        article.setCost(costSum);
        return article;
    }
}
