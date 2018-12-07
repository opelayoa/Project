package com.tiendas3b.almacen.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tiendas3b.almacen.db.dao.SpinnerBase;

import java.util.List;

/**
 * Created by dfa on 03/05/2016.
 */
public class GenericSpinnerAdapter<T extends SpinnerBase> extends ArrayAdapter<T> {

    protected final List<T> items;

    public GenericSpinnerAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
//        if(objects == null || objects.isEmpty()){
//            add(SpinnerBase.addEmpty());
//        }
        this.items = objects;
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(items.get(position).getDescription());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(items.get(position).getDescription());
        return view;
    }

//    @Override
//    public int getPosition(T item) {
//        return super.getPosition(item);
//    }
}
