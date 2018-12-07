package com.tiendas3b.almacen.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by danflo on 07/12/2016 madafaka.
 */
public class TextWatcherMoney implements TextWatcher {
    private final EditText txt;
    private String current;

    public TextWatcherMoney(EditText txt) {
        this.txt = txt;
        current = txt.getText().toString();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String text = s.toString();
        if(!text.equals(current)){
            txt.removeTextChangedListener(this);
            String cleanString = text.replaceAll("[$,.]", "");
            double parsed = Double.parseDouble(cleanString);
            String formatted = NumbersUtil.moneyFormatCleanString(parsed);
            current = formatted;
            txt.setText(formatted);
            txt.setSelection(formatted.length());
            txt.addTextChangedListener(this);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
