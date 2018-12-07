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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.views.tables.vo.ArticleVO;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class DecreaseCaptureTableDataAdapter extends TableDataAdapter<ArticleVO> {

    private static final int TEXT_SIZE = 14;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    private final List<ArticleVO> allItems;
    private final List<ObservationType> types;
    private final List<ObservationType> obs;
    private final List<ObservationType> obsLog;
    private AsyncTask<Void, Void, Void> task;
    private final Context mContext;
    private List<ArticleVO> items;
    private final SpinnerAdapter adapterObs;
    private final SpinnerAdapter adapterObsLog;
    private final SpinnerAdapter adapterType;

    public DecreaseCaptureTableDataAdapter(Context context, List<ArticleVO> data, List<ArticleVO> allItems, List<ObservationType> types, List<ObservationType> obs, List<ObservationType> obsLog) {
        super(context, data);
        mContext = context;
        items = data;
        this.allItems = allItems;
        this.types = types;
        this.obs = obs;
        this.obsLog = obsLog;
        adapterType = new SpinnerAdapter(mContext, R.layout.spinner_item_table, types);
        adapterObs = new SpinnerAdapter(mContext, R.layout.spinner_item_table, obs);
        adapterObsLog = new SpinnerAdapter(mContext, R.layout.spinner_item_table, obsLog);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        ArticleVO item = getRowData(rowIndex);

        switch (columnIndex) {
            case 0:
                renderedView = renderString(String.valueOf(item.getIclave()), Gravity.CENTER);
                break;
            case 1:
                renderedView = renderString(item.getDescription(), Gravity.CENTER_VERTICAL);
                break;
            case 2:
                renderedView = renderEditText(item, parentView);
                break;
            case 3:
                renderedView = renderSpinnerType(item, parentView);
                break;
            case 4:
                renderedView = renderSpinner(item, parentView);
                break;
            case 5:
                renderedView = renderSpinnerObsLog(item, parentView);
                break;
            default:
                break;
        }


        return renderedView;
    }

    private View renderSpinnerObsLog(final ArticleVO item, ViewGroup parentView) {
        View view = getLayoutInflater().inflate(R.layout.table_cell_edit_spinner, parentView, false);
        Spinner spinner = view.findViewById(R.id.spn);
        spinner.setAdapter(adapterObsLog);
        Long obsLog = item.getObsLog();
        spinner.setSelection(obsLog == null ? 0 : getObsLogPosition(obsLog));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.setObsLog(adapterObsLog.getItemId(position));
                allItems.set(allItems.indexOf(item), item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private View renderSpinnerType(final ArticleVO item, ViewGroup parentView) {
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
        for (int i = 0; i < types.size(); i++) {
            if (type == types.get(i).getId()) {
                return i;
            }
        }
        return 0;
    }

    private View renderSpinner(final ArticleVO item, ViewGroup parentView) {
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

    private int getObsPosition(long type) {
        for (int i = 0; i < obs.size(); i++) {
            if (type == this.obs.get(i).getId()) {
                return i;
            }
        }
        return 0;
    }

    private int getObsLogPosition(long type) {
        for (int i = 0; i < obsLog.size(); i++) {
            if (type == this.obsLog.get(i).getId()) {
                return i;
            }
        }
        return 0;
    }

    private View renderEditText(final ArticleVO item, ViewGroup parentView) {
        EditText view = (EditText) getLayoutInflater().inflate(R.layout.table_cell_edit_text, parentView, false);
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String val = s.toString();
                if (val.isEmpty()) {
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
        return view;
    }

    private View renderString(String value, int gravity) {
        TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setMinLines(3);
        textView.setGravity(gravity);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    public synchronized void setFilteredList(List<ArticleVO> models) {
        if (task == null) {
            task = startTask(models);
        } else {
            task.cancel(true);
            task = startTask(models);
        }
    }


    private AsyncTask<Void, Void, Void> startTask(final List<ArticleVO> models) {
//        return new AsyncTask<Void, Void, Void>() {

//
//            @Override
//            protected Void doInBackground(Void... params) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
        notifyDataSetChanged();
        task = null;
//            }
//        }.execute();

        return null;
    }

    private void applyAndAnimateRemovals(List<ArticleVO> newModels) {
        int size = items.size() - 1;
        for (int i = size; i > -1; i--) {
            final ArticleVO model = items.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<ArticleVO> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final ArticleVO model = newModels.get(i);
            if (!items.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ArticleVO> newModels) {
        int size = newModels.size() - 1;
        for (int toPosition = size; toPosition > -1; toPosition--) {
            final ArticleVO model = newModels.get(toPosition);
            final int fromPosition = items.indexOf(model);
            if (fromPosition > -1 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public ArticleVO removeItem(int position) {
        final ArticleVO model = items.remove(position);
//        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, ArticleVO model) {
        items.add(position, model);
//        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ArticleVO model = items.remove(fromPosition);
        items.add(toPosition, model);
//        notifyItemMoved(fromPosition, toPosition);
    }
}
