package com.tiendas3b.almacen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.tiendas3b.almacen.util.KeyboardUtil;

/**
 * Created by danflo on 27/03/2017 madafaka.
 */

public abstract class ScannerActivity extends AppCompatActivity {

    private static final String TAG = "ScannerActivity";
    private StringBuffer barcodeBuffer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null){
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
//            Log.d(TAG, "remove soft key");
//        }
//        Log.d(TAG, "HD:" + isHardwareKeyboardAvailable());
        barcodeBuffer = new StringBuffer();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyUp:" + keyCode);
        int unicode = event.getUnicodeChar();
        if(unicode != 0){
            Log.d(TAG, "unicode:" + unicode);
            if (keyCode != KeyEvent.KEYCODE_DPAD_DOWN && keyCode != KeyEvent.KEYCODE_ENTER) {
                String c = String.valueOf(Character.toChars(unicode)[0]);
                Log.d(TAG, "char:" + c);
                barcodeBuffer.append(c);
            }
            if(keyCode == KeyEvent.KEYCODE_ENTER){
                Log.d(TAG, "KEYCODE_ENTER");
//                clearTxt();
                processBarcode(barcodeBuffer.toString());
                clearTxt();
                barcodeBuffer = new StringBuffer("");
                KeyboardUtil.hide(this);
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    protected abstract void processBarcode(String barcode);

    protected abstract void clearTxt();
}
