package com.tiendas3b.almacen.adapters;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tiendas3b.almacen.db.dao.ReceiptSheetDetail;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class ReceiptSheetDetailsTableDataAdapter extends TableDataAdapter<ReceiptSheetDetail> {

    private static final int TEXT_SIZE = 14;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    private final List<ReceiptSheetDetail> allItems;
    ////    private final String[] types;
////    private final String[] obs;
//private final List<ObservationType> types;
//    private final List<ObservationType> obs;
//    private AsyncTask<Void, Void, Void> task;
    private final Context mContext;
    //    private final ArrayAdapter<String> adapterObs;
//    private final ArrayAdapter<String> adapterType;
//    private List<ArticleDecrease> itemsToSend;
    private List<ReceiptSheetDetail> items;
//    private final SpinnerAdapter adapterObs;
//    private final SpinnerAdapter adapterType;

//    public List<ArticleDecrease> getItemsToSend() {
//        return itemsToSend;
//    }

    public ReceiptSheetDetailsTableDataAdapter(Context context, List<ReceiptSheetDetail> data, List<ReceiptSheetDetail> allItems) {
        super(context, data);
        mContext = context;
        items = data;
        this.allItems = allItems;
//        this.types = types;
//        this.obs = obs;
//        itemsToSend = new ArrayList<>();

////        types = new String[]{"20 ALM", "21 CC", "22 PRV", "23 TDA"};
//        adapterType = new SpinnerAdapter(mContext, R.layout.spinner_item_table, types);
////        obs = new String[]{"01 Caducidad", "02 Rotura x Manipuleo", "03 Rotura x Robo", "04 Mala Calidad del Embalaje", "05 Mala Calidad del Producto"};//traer de ws
//        adapterObs = new SpinnerAdapter(mContext, R.layout.spinner_item_table, obs);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        ReceiptSheetDetail item = getRowData(rowIndex);

        switch (columnIndex) {
            case 0:
                renderedView = renderString(item.getIclave().toString(), Gravity.START);
                break;
            case 1:
                renderedView = renderString(item.getDescription(), Gravity.START);
                break;
            case 2:
                String expiration = item.getExpiration();
                if ("N/A".equals(expiration)) {
                    renderedView = renderString(expiration, Gravity.START);
                } else {
                    renderedView = renderString(expiration.substring(5), Gravity.START);
                }
                break;
            case 3:
                renderedView = renderString(String.valueOf(item.getNumBoxesOdc()), Gravity.START);
                break;
            case 4:
                renderedView = renderString(String.valueOf(item.getNumBoxesEm()), Gravity.START);
                break;
            case 5:
                renderedView = renderString(item.getFillRate(), Gravity.START);
                break;
        }

        return renderedView;
    }

//    private View renderSpinerType(final ArticleDecrease item, ViewGroup parentView) {
//        View view = getLayoutInflater().inflate(R.layout.table_cell_edit_spinner, parentView, false);
//        Spinner spinner = (Spinner) view.findViewById(R.id.spn);
//        spinner.setAdapter(adapterType);
//        Long type = item.getType();
//        spinner.setSelection(type == null ? 0 : getTypePosition(type));
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                item.setType(adapterType.getItemId(position));
//                allItems.set(allItems.indexOf(item), item);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        return view;
//    }
//
//    private int getTypePosition(long type) {
//        for (int i = 0; i < types.size(); i++){
//            if(type == types.get(i).getId()){
//                return i;
//            }
//        }
//        return 0;
//    }
//
//    private View renderSpiner(final ArticleDecrease item, ViewGroup parentView) {
//        View view = getLayoutInflater().inflate(R.layout.table_cell_edit_spinner, parentView, false);
//        Spinner spinner = (Spinner) view.findViewById(R.id.spn);
//        spinner.setAdapter(adapterObs);
//        Long obs = item.getObs();
//        spinner.setSelection(obs == null ? 0 : getObsPosition(obs));
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                item.setObs(adapterObs.getItemId(position));
//                allItems.set(allItems.indexOf(item), item);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        return view;
//    }
//
//    private int getObsPosition(long type) {
//        for (int i = 0; i < obs.size(); i++){
//            if(type == this.obs.get(i).getId()){
//                return i;
//            }
//        }
//        return 0;
//    }
//
//    private View renderEditText(final ArticleDecrease item, ViewGroup parentView) {
//        EditText view = (EditText) getLayoutInflater().inflate(R.layout.table_cell_edit_text, parentView, false);
////        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
////            @Override
////            public void onFocusChange(View v, boolean hasFocus) {
////                if()
////            }
////        });
//        view.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
////                Log.e("adapter", "onTextChanged");
//                String val = s.toString();
//                if(val.isEmpty()) {
////                    Log.e("adapter", "isEmpty");
//                    item.setAmount(null);
////                    itemsToSend.remove(item);
//                } else {
////                    Log.e("adapter", "onTextChanged");
//                    item.setAmount(Integer.valueOf(val));
////                    if(!itemsToSend.contains(item))
////                        itemsToSend.add(new ArticleDecrease(item));
//                }
//                allItems.set(allItems.indexOf(item), item);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//        Integer amount = item.getAmount();
//        view.setText(amount == null ? "" : amount.toString());
//        return view;
//    }

//    private View renderPrice(Float s) {
//        String priceString = PRICE_FORMATTER.format(s == null ? .0f : s);
//
//        TextView textView = new TextView(getContext());
//        textView.setText(priceString);
//        textView.setPadding(20, 10, 20, 10);
//        textView.setTextSize(TEXT_SIZE);
//
////        if (car.getAmountFact() < 50000) {
////            textView.setTextColor(0xFF2E7D32);
////        } else if (car.getAmountFact() > 100000) {
////            textView.setTextColor(0xFFC62828);
////        }
//
//        return textView;
//    }

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
        textView.setGravity(gravity);
        textView.setPadding(20, 20, 20, 20);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

//    public synchronized void setFilteredList(List<ArticleDecrease> models) {
//        if (task == null) {
//            task = startTask(models);
//        } else {
//            task.cancel(true);
//            task = startTask(models);
//        }
//    }
//
//
//    private AsyncTask<Void, Void, Void> startTask(final List<ArticleDecrease> models) {
//        return new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                applyAndAnimateRemovals(models);
//                applyAndAnimateAdditions(models);
//                applyAndAnimateMovedItems(models);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                notifyDataSetChanged();
//                task = null;
//            }
//        }.execute();
//    }
//
//    private void applyAndAnimateRemovals(List<ArticleDecrease> newModels) {
////        Log.e("adapter", "applyAndAnimateRemovals");
//        int size = items.size() - 1;
//        for (int i = size; i > -1; i--) {
//            final ArticleDecrease model = items.get(i);
//            if (!newModels.contains(model)) {
//                removeItem(i);
//            }
//        }
//    }
//
//    private void applyAndAnimateAdditions(List<ArticleDecrease> newModels) {
////        Log.e("adapter", "applyAndAnimateAdditions");
//        for (int i = 0, count = newModels.size(); i < count; i++) {
//            final ArticleDecrease model = newModels.get(i);
//            if (!items.contains(model)) {
//                addItem(i, model);
//            }
//        }
//    }
//
//    private void applyAndAnimateMovedItems(List<ArticleDecrease> newModels) {
////        Log.e("adapter", "applyAndAnimateMovedItems");
//        int size = newModels.size() - 1;
//        for (int toPosition = size; toPosition > -1; toPosition--) {
//            final ArticleDecrease model = newModels.get(toPosition);
//            final int fromPosition = items.indexOf(model);
//            if (fromPosition > -1 && fromPosition != toPosition) {
//                moveItem(fromPosition, toPosition);
//            }
//        }
//    }
//
//    public ArticleDecrease removeItem(int position) {
////        Log.e("adapter", "removeItem");
//        final ArticleDecrease model = items.remove(position);
////        notifyItemRemoved(position);
//        return model;
//    }
//
//    public void addItem(int position, ArticleDecrease model) {
////        Log.e("adapter", "addItem");
//        items.add(position, model);
////        notifyItemInserted(position);
//    }
//
//    public void moveItem(int fromPosition, int toPosition) {
////        Log.e("adapter", "moveItem");
//        final ArticleDecrease model = items.remove(fromPosition);
//        items.add(toPosition, model);
////        notifyItemMoved(fromPosition, toPosition);
//    }
//
//    public void reset(ArrayList<ArticleDecrease> models) {
//        applyAndAnimateAdditions(models);
//        applyAndAnimateMovedItems(models);
//        notifyDataSetChanged();
////        if (task == null) {
////            Log.e("adapter", "reset");
////            task = startReset(models);
////        } else {
////            task.cancel(true);
////            task = startReset(models);
////        }
//    }

//    private AsyncTask<Void, Void, Void> startReset(final List<ArticleDecrease> models) {
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
