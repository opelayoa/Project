package com.tiendas3b.almacen.adapters;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.views.tables.vo.ReturnVendorArticleVO;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class ReturnVendorTableAdapter extends TableDataAdapter<ReturnVendorArticleVO> {

    private static final int TEXT_SIZE = 14;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    private final List<ReturnVendorArticleVO> allItems;
//    private final String[] types;
//    private final String[] obs;
    private final List<ObservationType> types;
    private final List<ObservationType> obs;
    private AsyncTask<Void, Void, Void> task;
    private final Context mContext;
    private final SpinnerAdapter adapterObs;
    private final SpinnerAdapter adapterType;
//    private List<ArticleTransference> itemsToSend;
    private List<ReturnVendorArticleVO> items;

//    public List<ArticleTransference> getItemsToSend() {
//        return itemsToSend;
//    }

    public ReturnVendorTableAdapter(Context context, List<ReturnVendorArticleVO> data, List<ReturnVendorArticleVO> allItems, List<ObservationType> types, List<ObservationType> obs) {
        super(context, data);
        mContext = context;
        items = data;
        this.allItems = allItems;
        this.types = types;
        this.obs = obs;
//        itemsToSend = new ArrayList<>();

//        types = new String[]{"20 ALM", "21 CC", "22 PRV", "23 TDA"};
        adapterType = new SpinnerAdapter(mContext, R.layout.spinner_item_table, types);
//        obs = new String[]{"01 Caducidad", "02 Rotura x Manipuleo", "03 Rotura x Robo", "04 Mala Calidad del Embalaje", "05 Mala Calidad del Producto"};//traer de ws
        adapterObs = new SpinnerAdapter(mContext, R.layout.spinner_item_table, obs);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        ReturnVendorArticleVO item = getRowData(rowIndex);

        switch (columnIndex) {
            case 0:
                renderedView = renderString(String.valueOf(item.getIclave()), Gravity.START);
                break;
            case 1:
                renderedView = renderString(item.getDescription(), Gravity.START);
                break;
            case 2:
                renderedView = renderEditText(item, parentView);
                break;
            case 3:
                renderedView = renderSpinerType(item, parentView);
                break;
            case 4:
                renderedView = renderSpiner(item, parentView);
                break;
        }

        return renderedView;
    }

    private View renderSpinerType(final ReturnVendorArticleVO item, ViewGroup parentView) {
        View view = getLayoutInflater().inflate(R.layout.table_cell_edit_spinner, parentView, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.spn);
        spinner.setAdapter(adapterType);
        Long type = item.getType();
        spinner.setSelection(type == null ? 0 : getTypePosition(type));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.setType(adapterType.getItemId(position));
                allItems.set(allItems.indexOf(item), item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private int getTypePosition(long type) {
        for (int i = 0; i < types.size(); i++){
            if(type == types.get(i).getId()){
                return i;
            }
        }
        return 0;
    }

    private View renderSpiner(final ReturnVendorArticleVO item, ViewGroup parentView) {
        View view = getLayoutInflater().inflate(R.layout.table_cell_edit_spinner, parentView, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.spn);
        spinner.setAdapter(adapterObs);
        Long obs = item.getObs();
        spinner.setSelection(obs == null ? 0 : getObsPosition(obs));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.setObs(adapterObs.getItemId(position));
                allItems.set(allItems.indexOf(item), item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private int getObsPosition(long obs) {
        for (int i = 0; i < this.obs.size(); i++){
            if(obs == this.obs.get(i).getId()){
                return i;
            }
        }
        return 0;
    }

    private View renderEditText(final ReturnVendorArticleVO item, ViewGroup parentView) {
        EditText view = (EditText) getLayoutInflater().inflate(R.layout.table_cell_edit_text, parentView, false);
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String val = s.toString();
                if(val.isEmpty()) {
                    item.setAmount(null);
                } else {
                    item.setAmount(Integer.valueOf(val));
                }
                allItems.set(allItems.indexOf(item), item);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Integer amount = item.getAmount();
        view.setText(amount == null ? "" : amount.toString());
        view.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        return view;
    }

    private View renderString(String value, int gravity) {
        TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setGravity(gravity);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    public synchronized void setFilteredList(List<ReturnVendorArticleVO> models) {
        if (task == null) {
            task = startTask(models);
        } else {
            task.cancel(true);
            task = startTask(models);
        }
    }


    private AsyncTask<Void, Void, Void> startTask(final List<ReturnVendorArticleVO> models) {
        return new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                applyAndAnimateRemovals(models);
                applyAndAnimateAdditions(models);
                applyAndAnimateMovedItems(models);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                notifyDataSetChanged();
                task = null;
            }
        }.execute();
    }

    private void applyAndAnimateRemovals(final List<ReturnVendorArticleVO> newModels) {
//        Log.e("adapter", "applyAndAnimateRemovals");
        int size = items.size() - 1;
        for (int i = size; i > -1; i--) {
            final ReturnVendorArticleVO model = items.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(final List<ReturnVendorArticleVO> newModels) {
//        Log.e("adapter", "applyAndAnimateAdditions");
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final ReturnVendorArticleVO model = newModels.get(i);
            if (!items.contains(model)) {
//                addItem(i, model);
                items.add(i, model);
//        notifyItemInserted(i);
            }
        }
    }

    private void applyAndAnimateMovedItems(final List<ReturnVendorArticleVO> newModels) {
//        Log.e("adapter", "applyAndAnimateMovedItems");
        int size = newModels.size() - 1;
        for (int toPosition = size; toPosition > -1; toPosition--) {
            final ReturnVendorArticleVO model = newModels.get(toPosition);
            final int fromPosition = items.indexOf(model);
            if (fromPosition > -1 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public ReturnVendorArticleVO removeItem(int position) {
//        Log.e("adapter", "removeItem");
        final ReturnVendorArticleVO model = items.remove(position);
//        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, ReturnVendorArticleVO model) {
//        Log.e("adapter", "addItem");
        items.add(position, model);
//        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
//        Log.e("adapter", "moveItem");
        final ReturnVendorArticleVO model = items.remove(fromPosition);
        items.add(toPosition, model);
//        notifyItemMoved(fromPosition, toPosition);
    }

    public void reset(ArrayList<ReturnVendorArticleVO> models) {
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
        notifyDataSetChanged();
//        if (task == null) {
//            Log.e("adapter", "reset");
//            task = startReset(models);
//        } else {
//            task.cancel(true);
//            task = startReset(models);
//        }
    }

//    private AsyncTask<Void, Void, Void> startReset(final List<ArticleTransference> models) {
//        Log.e("adapter", "startReset");
//        return new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                Log.e("startReset", "doInBackground");
//                applyAndAnimateAdditions(models);
//                Log.e("startReset", "doInBackground2");
//                applyAndAnimateMovedItems(models);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                Log.e("startReset", "onPostExecute");
//                notifyDataSetChanged();
//                task = null;
//            }
//        }.execute();
//    }
}
