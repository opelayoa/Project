package com.tiendas3b.almacen.receipt.edit;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.BuyDetail;
import com.tiendas3b.almacen.db.dao.ExpressArticle;
import com.tiendas3b.almacen.db.dao.ExpressReceipt;
import com.tiendas3b.almacen.db.dao.ReceiptSheetCapture;
import com.tiendas3b.almacen.db.dao.ReceiptSheetDetailCapture;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.BuyDetailAndCaptureDTO;
import com.tiendas3b.almacen.dto.BuyDetailDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.receipt.express.HeaderExpressDTO;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditReceipt {

    private static final String TAG = "EditReceipt";
//    private Buy buy;
    private IDatabaseManager db;
    private GlobalState mContext;
    private int odc;
    private long regionId, providerId, buyId;
//    private Boolean webServices = false;

    public EditReceipt(IDatabaseManager db, GlobalState mContext, int odc, long regionId, long providerId, long buyId){
        this.db = db;
        this.mContext = mContext;
        this.odc = odc;
        this.regionId = regionId;
        this.providerId = providerId;
        this.buyId = buyId;
    }

//    protected boolean downloadBuyDetails() {
//        downloadDetails(odc, regionId, providerId, new Callback(){
//
//            @Override
//            public void onResponse(Call call, Response response) {
//                //TODO AQUI DEBE REGRESAR
//                webServices = true;
//
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//
//            }
//        });
//        return webServices;
//    }

    protected void downloadDetails( Callback callback) {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.getBuyDetails(odc, String.valueOf(regionId), providerId).enqueue(new Callback<List<BuyDetailAndCaptureDTO>>() {
            @Override
            public void onResponse(Call<List<BuyDetailAndCaptureDTO>> call, Response<List<BuyDetailAndCaptureDTO>> response) {
                if (response.isSuccessful() && response.body() != null ) {

                    Boolean items = createItems(response.body());
                    if (items) {
                        downloadHeaderExpress(new Callback(){
                            @Override
                            public void onResponse(Call call, Response response) {
                                callback.onResponse(call, response);
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {

                            }
                        });
                    }else {
                        Toast.makeText(mContext, "Error descarga de detalles", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<BuyDetailAndCaptureDTO>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_odc));
                Toast.makeText(mContext, "Error descarga de detalles", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean createItems(List<BuyDetailAndCaptureDTO> list) {
        List<BuyDetail> items = new ArrayList<>();
//        captures = new ArrayList<>();
//        boolean complete = true;
        for (BuyDetailAndCaptureDTO a : list) {
            BuyDetailDTO d = a.getBuyDetail();
//            VArticle article = db.findViewArticleForPurchasing(d.getIclave(), d.getBarcode());
            VArticle article = db.findViewArticleByIclave(d.getIclave());
            if (article == null) {
                Toast.makeText(mContext, "No se encontró el artículo: " + d.getIclave(), Toast.LENGTH_LONG).show();
                return false;
            } else {
                BuyDetail b = new BuyDetail();
//            b.setArticle(article);
                b.setArticleId(article.getId());
                b.setArticleAmount(d.getArticleAmount());
                b.setBalance(d.getBalance());
                b.setBuyId(buyId);
//            b.setBuy(item);
                ReceiptSheetDetailCapture c = a.getCapture();
                if (c == null) {
//                complete = false;
                } else {
                    b.setReceiptSheetCaptureId(db.insertOrUpdate(c));
                }
                items.add(b);
            }
        }

        db.insertOrReplaceInTx(items.toArray(new BuyDetail[items.size()]));
        return true;
    }

    private void downloadHeaderExpress(Callback callback) {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.getHeaderExpress2(odc, regionId, providerId).enqueue(new Callback<HeaderExpressDTO>() {
            @Override
            public void onResponse(Call<HeaderExpressDTO> call, Response<HeaderExpressDTO> response) {
                if (response.isSuccessful()) {
//                    processHeader(response.body());
                    downloadDetails(response.body().getFolio(), new Callback(){

                                @Override
                                public void onResponse(Call call, Response response) {
                                    downloadArticles(new Callback(){

                                        @Override
                                        public void onResponse(Call call, Response response) {
                                            //TODO ULTIMA VALIDACION
                                            callback.onResponse(call, response);
                                        }

                                        @Override
                                        public void onFailure(Call call, Throwable t) {

                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {

                                }
                            }
                    );

                } else {
                }
            }

            @Override
            public void onFailure(Call<HeaderExpressDTO> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_odc));
                Toast.makeText(mContext, "getHeaderExpress", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processHeader(HeaderExpressDTO header) {
//        lblArticlesQuantity.setText(String.valueOf(header.getArticlesQuantity()));
//        scaffoldQuantity = header.getScaffoldQuantity();
//        lblScaffoldQuantity.setText(String.valueOf(scaffoldQuantity));

        ReceiptSheetCapture receiptSheetCapture = db.findReceiptSheetCaptureBy(buyId);
        if (receiptSheetCapture == null) {
            receiptSheetCapture = new ReceiptSheetCapture();
            receiptSheetCapture.setBuyId(buyId);
            receiptSheetCapture.setOdc(odc);
        }
//        receiptSheetCapture.setReceiptDate(DateUtil.getTodayStr());
//        receiptSheetCapture.setDateTime(buy.getTime());
//        receiptSheetCapture.setDeliveryTime(buy.getDeliveryTime());
//        receiptSheetCapture.setPlatform(buy.getPlatform());
        db.insertOrUpdate(receiptSheetCapture);

    }

    private void downloadDetails(int folio, Callback callback) {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.getExpressDetails2(regionId, folio).enqueue(new Callback<List<ExpressReceipt>>() {
            @Override
            public void onResponse(Call<List<ExpressReceipt>> call, Response<List<ExpressReceipt>> response) {
                if (response.isSuccessful()) {
                    processDetails(response.body());
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<ExpressReceipt>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_odc));
                Toast.makeText(mContext, "getExpressDetails", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processDetails(List<ExpressReceipt> details) {
        if(details == null || details.isEmpty()){
            Toast.makeText(mContext, "No hay artículos.", Toast.LENGTH_LONG).show();
        } else {
            for (ExpressReceipt rex : details) {
                //TODO SI NO HAY DETALLES DE PRODUCTOS DE PROVEEDOR,  LA APP SE DETIENE
                rex.setBuyDetailId(db.findBuyDetailByArticle(rex.getIclave(), buyId).getId());
                rex.setRegionId(regionId);
            }
//            findViewById(R.id.progressBarReceiptGr).setVisibility(View.GONE);
            db.insertOrReplaceInTx(details);
//            if (buy.getChecked() == Constants.NOT_ARRIVE || buy.getChecked() == Constants.NOT_ARRIVE_AUT ) {
//                setInProcess();
//            }
        }
    }

    private void downloadArticles(Callback callback) {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.getExpressArticles(regionId, odc).enqueue(new Callback<List<ExpressArticle>>() {
            @Override
            public void onResponse(Call<List<ExpressArticle>> call, Response<List<ExpressArticle>> response) {
                if (response.isSuccessful()) {

                    db.insertOrReplaceInTx(response.body());
                    callback.onResponse(call, response);
//                    updateList();
//                    processExpressArticles(response.body());
                } else {
                    Log.e(TAG, "getExpressArticles");
                }
            }

            @Override
            public void onFailure(Call<List<ExpressArticle>> call, Throwable t) {
                Log.e(TAG, "getExpressArticles");
            }
        });
    }

}
