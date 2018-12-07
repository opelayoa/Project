package com.tiendas3b.almacen.views.tables;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.codecrafters.tableview.TableHeaderAdapter;

/**
 * Created by dfa on 04/07/2016.
 */
public class CenterTableHeaderAdapter extends TableHeaderAdapter {

    private static final int TEXT_SIZE = 16;
    private final String[] headers;
    private int textColor = 0x99000000;

    public CenterTableHeaderAdapter(final Context context, final String... headers) {
        super(context);
        this.headers = headers;
    }

    public void setTextColor(final int textColor) {
        this.textColor = textColor;
    }

    @Override
    public View getHeaderView(final int columnIndex, final ViewGroup parentView) {
        final TextView textView = new TextView(getContext());
        if (columnIndex < headers.length) {
//            try {
//                textView.setText(new String(headers[columnIndex].getBytes(), "UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
                textView.setText(headers[columnIndex]);
//            }
        }
        textView.setPadding(8, 20, 8, 20);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(textColor);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

}