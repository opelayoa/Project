package com.tiendas3b.almacen.adapters;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
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
import com.tiendas3b.almacen.views.tables.vo.InputTransferenceArticleVO;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class InputTransferenceTableAdapter extends TableDataAdapter<InputTransferenceArticleVO> {

    private static final int TEXT_SIZE = 16;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    private final List<InputTransferenceArticleVO> allItems;
//    private final String[] types;
//    private final String[] obs;
    private final List<ObservationType> types;
    private final List<ObservationType> obs;
    private final List<ObservationType> obsLog;
    private AsyncTask<Void, Void, Void> task;
    private final Context mContext;
    private final SpinnerAdapter adapterObs;
    private final SpinnerAdapter adapterObsLog;
    private final SpinnerAdapter adapterType;
//    private List<ArticleTransference> itemsToSend;
    private List<InputTransferenceArticleVO> items;

//    public List<ArticleTransference> getItemsToSend() {
//        return itemsToSend;
//    }

    public InputTransferenceTableAdapter(Context context, List<InputTransferenceArticleVO> data, List<InputTransferenceArticleVO> allItems, List<ObservationType> types, List<ObservationType> obs, List<ObservationType> obsLog) {
        super(context, data);
        mContext = context;
        items = data;
        this.allItems = allItems;
        this.types = types;
        this.obs = obs;
        this.obsLog = obsLog;
//        itemsToSend = new ArrayList<>();

//        types = new String[]{"20 ALM", "21 CC", "22 PRV", "23 TDA"};
        adapterType = new SpinnerAdapter(mContext, R.layout.spinner_item_table, types);
//        obs = new String[]{"01 Caducidad", "02 Rotura x Manipuleo", "03 Rotura x Robo", "04 Mala Calidad del Embalaje", "05 Mala Calidad del Producto"};//traer de ws
        adapterObs = new SpinnerAdapter(mContext, R.layout.spinner_item_table, obs);
        adapterObsLog = new SpinnerAdapter(mContext, R.layout.spinner_item_table, obsLog);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        InputTransferenceArticleVO item = getRowData(rowIndex);

        switch (columnIndex) {
            case 0:
                renderedView = renderString(item.getIclave()+"", Gravity.CENTER);
                break;
            case 1:
                renderedView = renderString(item.getDescription(), Gravity.CENTER_VERTICAL);
                break;
            case 2:
                item.setAmount(item.getAmountSmn());
                renderedView = renderString(item.getAmountSmn().toString(), Gravity.CENTER_VERTICAL | Gravity.END);
                break;
            case 3:
                return renderEditTextInv(item, parentView);
            case 4:
                return renderEditTextMer(item, parentView);
            case 5:
                return renderEditTextReg(item, parentView);
            case 6:
                renderedView = renderSpinerType(item, parentView);
                break;
            case 7:
                renderedView = renderSpiner(item, parentView);
                break;
            case 8:
                renderedView = renderSpinnerObsLog(item, parentView);
                break;
            default:
                break;
        }

        return renderedView;
    }

    private View renderSpinnerObsLog(final InputTransferenceArticleVO item, ViewGroup parentView) {
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

    private int getObsLogPosition(long type) {
        for (int i = 0; i < obsLog.size(); i++) {
            if (type == this.obsLog.get(i).getId()) {
                return i;
            }
        }
        return 0;
    }

    private TextInputEditText renderEditTextInv(final InputTransferenceArticleVO item, ViewGroup parentView) {
        TextInputEditText view = (TextInputEditText) getLayoutInflater().inflate(R.layout.table_cell_edit_text, parentView, false);
        Integer amount = item.getAmountInv();
        view.setText(amount == null ? null : amount.toString());
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String val = s.toString();
                if(val.isEmpty()) {
                    item.setAmountInv(null);
                } else {
                    int amount = Integer.valueOf(val);
                    item.setAmountInv(amount);

//                    item.setAmount(amount);//el total de piezas a repartir en TE, TA, ME sale en el reporte de TE
                }
                allItems.set(allItems.indexOf(item), item);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        view.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        return view;
    }

    private TextInputEditText renderEditTextReg(final InputTransferenceArticleVO item, ViewGroup parentView) {
        TextInputEditText view = (TextInputEditText) getLayoutInflater().inflate(R.layout.table_cell_edit_text, parentView, false);
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String val = s.toString();
                if(val.isEmpty()) {
                    item.setAmountReg(null);
                } else {
                    item.setAmountReg(Integer.valueOf(val));
                }
                allItems.set(allItems.indexOf(item), item);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Integer amount = item.getAmountReg();
        view.setText(amount == null ? "" : amount.toString());
        view.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        return view;
    }

    private TextInputEditText renderEditTextMer(final InputTransferenceArticleVO item, ViewGroup parentView) {
        TextInputEditText view = (TextInputEditText) getLayoutInflater().inflate(R.layout.table_cell_edit_text, parentView, false);
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String val = s.toString();
                if(val.isEmpty()) {
                    item.setAmountMer(null);
                } else {
                    item.setAmountMer(Integer.valueOf(val));
                }
                allItems.set(allItems.indexOf(item), item);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Integer amount = item.getAmountMer();
        view.setText(amount == null ? "" : amount.toString());
        view.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        return view;
    }

    private View renderSpinerType(final InputTransferenceArticleVO item, ViewGroup parentView) {
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
        spinner.setGravity(Gravity.CENTER);
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

    private View renderSpiner(final InputTransferenceArticleVO item, ViewGroup parentView) {
//        View view = getLayoutInflater().inflate(R.layout.table_cell_edit_spinner, parentView, false);
//        Spinner spinner = (Spinner) view.findViewById(R.id.spn);
        Spinner spinner = (Spinner) getLayoutInflater().inflate(R.layout.table_cell_edit_spinner, parentView, false);
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
//        spinner.setGravity(Gravity.CENTER);
        return spinner;
    }

    private int getObsPosition(long obs) {
        for (int i = 0; i < this.obs.size(); i++){
            if(obs == this.obs.get(i).getId()){
                return i;
            }
        }
        return 0;
    }


    private View renderEditText(final InputTransferenceArticleVO item, ViewGroup parentView) {
        EditText view = (EditText) getLayoutInflater().inflate(R.layout.table_cell_edit_text, parentView, false);
//        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if()
//            }
//        });
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.e("adapter", "onTextChanged");
                String val = s.toString();
                if(val.isEmpty()) {
//                    Log.e("adapter", "isEmpty");
                    item.setAmountSmn(null);
//                    itemsToSend.remove(item);
                } else {
//                    Log.e("adapter", "onTextChanged");
                    item.setAmountSmn(Integer.valueOf(val));
//                    if(!itemsToSend.contains(item))
//                        itemsToSend.add(new InputTransferenceArticleVO(item));
                }
                allItems.set(allItems.indexOf(item), item);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Integer amount = item.getAmountSmn();
        view.setText(amount == null ? "" : amount.toString());
        view.setInputType(EditorInfo.TYPE_CLASS_NUMBER);

        return view;
    }

    private View renderPrice(Float s) {
        String priceString = PRICE_FORMATTER.format(s == null ? .0f : s);

        TextView textView = new TextView(getContext());
        textView.setText(priceString);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);

//        if (car.getAmountFact() < 50000) {
//            textView.setTextColor(0xFF2E7D32);
//        } else if (car.getAmountFact() > 100000) {
//            textView.setTextColor(0xFFC62828);
//        }

        return textView;
    }

//    private View renderPower(ReceiptSheet car, ViewGroup parentView) {
//        View view = getLayoutInflater().inflate(R.layout.table_cell_edit_text, parentView, false);
////        TextView kwView = (TextView) view.findViewById(R.id.kw_view);
////        TextView psView = (TextView) view.findViewById(R.id.ps_view);
////
////        kwView.setText(car.getKw() + " kW");
////        psView.setText(car.getPs() + " PS");
//
//        return view;
//    }

//    private View renderCatName(ReceiptSheet car) {
//        return renderString(car.getFolio().toString());
//    }

//    private View renderProducerLogo(ReceiptSheet car, ViewGroup parentView) {
////        View view = getLayoutInflater().inflate(R.layout.table_cell_image, parentView, false);
////        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
////        imageView.setImageResource(car.getProducer().getLogo());
////        return view;
//        return new View(getContext());
//    }

    private View renderString(String value, int gravity) {
        TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setMinLines(3);
        textView.setGravity(gravity);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    public synchronized void setFilteredList(List<InputTransferenceArticleVO> models) {
        if (task == null) {
            task = startTask(models);
        } else {
            task.cancel(true);
            task = startTask(models);
        }
    }


    private AsyncTask<Void, Void, Void> startTask(final List<InputTransferenceArticleVO> models) {
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

    private void applyAndAnimateRemovals(final List<InputTransferenceArticleVO> newModels) {
//        Log.e("adapter", "applyAndAnimateRemovals");
        int size = items.size() - 1;
        for (int i = size; i > -1; i--) {
            final InputTransferenceArticleVO model = items.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(final List<InputTransferenceArticleVO> newModels) {
//        Log.e("adapter", "applyAndAnimateAdditions");
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final InputTransferenceArticleVO model = newModels.get(i);
            if (!items.contains(model)) {
//                addItem(i, model);
                items.add(i, model);
//        notifyItemInserted(i);
            }
        }
    }

    private void applyAndAnimateMovedItems(final List<InputTransferenceArticleVO> newModels) {
//        Log.e("adapter", "applyAndAnimateMovedItems");
        int size = newModels.size() - 1;
        for (int toPosition = size; toPosition > -1; toPosition--) {
            final InputTransferenceArticleVO model = newModels.get(toPosition);
            final int fromPosition = items.indexOf(model);
            if (fromPosition > -1 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public InputTransferenceArticleVO removeItem(int position) {
//        Log.e("adapter", "removeItem");
        final InputTransferenceArticleVO model = items.remove(position);
//        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, InputTransferenceArticleVO model) {
//        Log.e("adapter", "addItem");
        items.add(position, model);
//        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
//        Log.e("adapter", "moveItem");
        final InputTransferenceArticleVO model = items.remove(fromPosition);
        items.add(toPosition, model);
//        notifyItemMoved(fromPosition, toPosition);
    }

    public void reset(ArrayList<InputTransferenceArticleVO> models) {
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

//    private AsyncTask<Void, Void, Void> startReset(final List<InputTransferenceArticleVO> models) {
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
