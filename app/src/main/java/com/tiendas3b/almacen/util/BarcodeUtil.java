package com.tiendas3b.almacen.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by danflo on 12/10/2017 madafaka.
 */

public class BarcodeUtil {

    public static final int REQUEST_CODE = 17;
    public static final String SCAN_RESULT = "SCAN_RESULT";

    public static void intentBarcodeReader(Activity context){
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.setPackage("com.google.zxing.client.android");
//                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");//SCAN_MODE
//                intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A,QR_CODE");
            context.startActivityForResult(intent, REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.zxing.client.android")));
        }
    }
}
