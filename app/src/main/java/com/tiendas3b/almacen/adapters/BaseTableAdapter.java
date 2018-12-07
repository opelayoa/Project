package com.tiendas3b.almacen.adapters;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.tiendas3b.almacen.util.Constants;

import java.text.DecimalFormat;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

/**
 * Created by dfa on 11/04/2016.
 */
public abstract class BaseTableAdapter<T> extends TableDataAdapter<T> {

    private static final int TEXT_SIZE = 15;
    private final Context mContext;
//    private List<T> items;
//    private final List<T> allItems;
//    private AsyncTask<Void, Void, Void> task;
    protected final DecimalFormat dfThousnds;
    protected final DecimalFormat dfMoney;

    public BaseTableAdapter(Context context, List<T> data, List<T> allItems) {//todo remove allItems
        super(context, data);
        mContext = context;
//        items = data;
//        this.allItems = allItems;
        dfThousnds = new DecimalFormat(Constants.FORMAT_INT_THOUSANDS);
        dfMoney = new DecimalFormat(Constants.FORMAT_MONEY);
    }

    protected View renderString(String value, int gravity) {
        TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setGravity(gravity);
        textView.setPadding(5, 25, 5, 25);
        textView.setTextSize(TEXT_SIZE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            textView.setTextColor(getResources().getColor(android.R.color.black, mContext.getTheme()));
        } else {
            textView.setTextColor(getResources().getColor(android.R.color.black));
        }
        return textView;
    }

//    public synchronized void setFilteredList(List<ArticleResearch> models) {
//        if (task == null) {
//            task = startTask(models);
//        } else {
//            task.cancel(true);
//            task = startTask(models);
//        }
//    }


//    private AsyncTask<Void, Void, Void> startTask(final List<ArticleResearch> models) {
////        return new AsyncTask<Void, Void, Void>() {
//
////
////            @Override
////            protected Void doInBackground(Void... params) {
//                applyAndAnimateRemovals(models);
//                applyAndAnimateAdditions(models);
//                applyAndAnimateMovedItems(models);
////                return null;
////            }
////
////            @Override
////            protected void onPostExecute(Void aVoid) {
//                notifyDataSetChanged();
//                task = null;
////            }
////        }.execute();
//
//        return null;
//    }
//
//    private void applyAndAnimateRemovals(List<T> newModels) {
//        int size = items.size() - 1;
//        for (int i = size; i > -1; i--) {
//            final T model = items.get(i);
//            if (!newModels.contains(model)) {
//                removeItem(i);
//            }
//        }
//    }
//
//    private void applyAndAnimateAdditions(List<T> newModels) {
//        for (int i = 0, count = newModels.size(); i < count; i++) {
//            final T model = newModels.get(i);
//            if (!items.contains(model)) {
//                addItem(i, model);
//            }
//        }
//    }
//
//    private void applyAndAnimateMovedItems(List<T> newModels) {
//        int size = newModels.size() - 1;
//        for (int toPosition = size; toPosition > -1; toPosition--) {
//            final T model = newModels.get(toPosition);
//            final int fromPosition = items.indexOf(model);
//            if (fromPosition > -1 && fromPosition != toPosition) {
//                moveItem(fromPosition, toPosition);
//            }
//        }
//    }
//
//    public T removeItem(int position) {
//        final T model = items.remove(position);
////        notifyItemRemoved(position);
//        return model;
//    }
//
//    public void addItem(int position, T model) {
//        items.add(position, model);
////        notifyItemInserted(position);
//    }
//
//    public void moveItem(int fromPosition, int toPosition) {
//        final T model = items.remove(fromPosition);
//        items.add(toPosition, model);
////        notifyItemMoved(fromPosition, toPosition);
//    }

}
