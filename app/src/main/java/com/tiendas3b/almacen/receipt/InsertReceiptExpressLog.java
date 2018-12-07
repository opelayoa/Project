package com.tiendas3b.almacen.receipt;

import android.util.Log;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Buy;
import com.tiendas3b.almacen.db.dao.ReceiptExpressLog;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.receipt.MmpReciboExpressLogDTO;
import com.tiendas3b.almacen.http.GeneralResponseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertReceiptExpressLog {

    IDatabaseManager db = null;
    GlobalState mContext = null;
    ReceiptExpressLog receiptExpressLog = null;

    public InsertReceiptExpressLog(IDatabaseManager db, GlobalState mContext){
        this.db = db;
        this.mContext = mContext;
    }

    public void insertRecExprLog( Buy provider, Integer idProceso, String descripcion){

        String myDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String myTime = new SimpleDateFormat("hh:mm:ss").format(new Date());
        String region = String.valueOf(provider.getRegionId());

        List<ReceiptExpressLog> findReceiptExpressLog = db.findReceiptExpressLog(region,provider.getProviderId(),provider.getFolio());

        if(findReceiptExpressLog.size() > 0) {
             receiptExpressLog = findReceiptExpressLog.get(0);
        }

        MmpReciboExpressLogDTO reciboExpressLogDTO = null;

        //SI LA LISTA ES VACIA AGREGA EL 1 COMO VALOR INICIAL EN IDORDEN, EN CASO CONTRARIO AUMENTA UN 1 AL ULTIMO VALOR IDORDEN
        if (receiptExpressLog == null){

            reciboExpressLogDTO = new MmpReciboExpressLogDTO(region, provider.getProviderId(), provider.getFolio(), myDate, myTime, 1, idProceso, descripcion);

            //Llena tabla ReceiptExpressLog
            receiptExpressLog = new ReceiptExpressLog(reciboExpressLogDTO.getIdAlmacen(), reciboExpressLogDTO.getIdProveedor(),reciboExpressLogDTO.getOdc(), reciboExpressLogDTO.getFecha(),
                    reciboExpressLogDTO.getHora(), reciboExpressLogDTO.getIdOrden(), reciboExpressLogDTO.getIdProceso(), reciboExpressLogDTO.getDescripcion(), null);
        }else{
            reciboExpressLogDTO = new MmpReciboExpressLogDTO(region, provider.getProviderId(), provider.getFolio(), myDate, myTime, receiptExpressLog.getIdOrden()+1, idProceso, descripcion);

            //Llena tabla ReceiptExpressLog
            receiptExpressLog = new ReceiptExpressLog(reciboExpressLogDTO.getIdAlmacen(), reciboExpressLogDTO.getIdProveedor(),reciboExpressLogDTO.getOdc(), reciboExpressLogDTO.getFecha(),
                    reciboExpressLogDTO.getHora(), reciboExpressLogDTO.getIdOrden(), reciboExpressLogDTO.getIdProceso(), reciboExpressLogDTO.getDescripcion(), null);
        }

        db.insert(receiptExpressLog);
        insertLog(reciboExpressLogDTO);
    }

    private void insertLog(MmpReciboExpressLogDTO mmpReciboExpressLogDTO){
        String TAG = "InsertReceiptExpressLog";
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        httpService.insertReciboExpressLog(mmpReciboExpressLogDTO).enqueue(new Callback<GeneralResponseDTO>() {
            @Override
            public void onResponse(Call<GeneralResponseDTO> call, Response<GeneralResponseDTO> response) {
                if (response.isSuccessful()) {
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseDTO> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });
    }

}
