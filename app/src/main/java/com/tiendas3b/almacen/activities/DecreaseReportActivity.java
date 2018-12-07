package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.adapters.DecreaseReportTableDataAdapter;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.dao.Region;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.PdfUtil;
import com.tiendas3b.almacen.util.Preferences;
import com.tiendas3b.almacen.views.tables.DecreaseReportTableView;
import com.tiendas3b.almacen.views.tables.vo.ArticleVO;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unchecked")
public class DecreaseReportActivity extends AppCompatActivity {

    private static final String AUTHORITY = "com.tiendas3b.almacen.provider";
    private GlobalState mContext;
    private IDatabaseManager databaseManager;
    private List<ArticleVO> items;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (GlobalState) getApplicationContext();
        setContentView(R.layout.activity_pdf_decrease);
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
        databaseManager = new DatabaseManager(mContext);
        String regionIdStr = new Preferences(this).getSharedStringSafe(Preferences.KEY_REGION, "-1");
        long regionId =  Long.parseLong(regionIdStr);
        Region region = databaseManager.getById(regionId, Region.class);
        Intent i = getIntent();
        items = (List<ArticleVO>) i.getSerializableExtra("d");
        String folio = i.getStringExtra("f");
        int type = i.getIntExtra("t", -1);
        ((TextView) findViewById(R.id.lblTittle)).setText(getTitle(type));
        ((TextView) findViewById(R.id.lblRegion)).setText(region.getName());
        ((TextView) findViewById(R.id.lblAddress)).setText(region.getAddress());
        ((TextView) findViewById(R.id.txtFolio)).setText(String.format(getFolio(type) + ": %s", folio));
        ((TextView) findViewById(R.id.txtAuthorized)).setText(mContext.getName());
        ((TextView) findViewById(R.id.txtDate)).setText(new SimpleDateFormat(DateUtil.DD_MM_YYYY_HH_MM_SS, Locale.getDefault()).format(new Date()));

        setObservation();
        items.add(getTotal(items));
        DecreaseReportTableView list = findViewById(R.id.list);
        list.setDataAdapter(new DecreaseReportTableDataAdapter(mContext, items));
    }

    private String getFolio(int type) {
        switch (type){
            case Constants.ME:
                return getString(R.string.pdf_me_folio);
            case Constants.DP:
                return getString(R.string.pdf_dp_folio);
            case Constants.TA:
                return getString(R.string.pdf_ta_folio);
            case Constants.TE:
                return getString(R.string.pdf_te_folio);
        }

        return "FOL";
    }

    private int getTitle(int type) {
        switch (type){
            case Constants.ME:
                return R.string.pdf_me;
            case Constants.DP:
                return R.string.pdf_dp;
            case Constants.TA:
                return R.string.pdf_ta;
            case Constants.TE:
                return R.string.pdf_te;
        }

        return R.string.pdf_default_report_name;
    }

    private void setObservation() {
        for (ArticleVO a : items) {
            a.setObsStr(( databaseManager.getById(a.getObs(), ObservationType.class)).getDescription());
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String path = PdfUtil.createPdf(findViewById(R.id.pdf), getString(R.string.decrease_folder));
                if(path == null){
                    Log.e(GlobalState.TAG, "No mames! no debería de fallar aquí nunca!");
                } else {
                    File file = new File(path);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                    intent.setDataAndType(FileProvider.getUriForFile(mContext, AUTHORITY, file), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                    finish();
                }
            }
        }, 100);
    }

    private ArticleVO getTotal(List<ArticleVO> articles) {
        ArticleVO article = new ArticleVO();
        int piecesSum = 0;
        float costSum = 0.0f;
        for (ArticleVO a : articles) {
            Integer pieces = a.getAmount();
            if(pieces != null){
                piecesSum += pieces;
                costSum += (a.getCost() * pieces);
            }
        }
        article.setAmount(piecesSum);
        article.setCost(costSum);
        return article;
    }

}
