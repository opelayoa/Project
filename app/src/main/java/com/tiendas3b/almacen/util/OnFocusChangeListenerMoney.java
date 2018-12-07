package com.tiendas3b.almacen.util;

import android.view.View;
import android.widget.EditText;

/**
 * Created by danflo on 27/12/2016 madafaka.
 */
public class OnFocusChangeListenerMoney implements View.OnFocusChangeListener {

    private final EditText txt;

    public OnFocusChangeListenerMoney(EditText txt) {
        this.txt = txt;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) {
            txt.setSelection(txt.getText().length());
        }
    }
}
