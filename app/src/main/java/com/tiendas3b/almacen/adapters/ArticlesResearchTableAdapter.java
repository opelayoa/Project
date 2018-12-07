package com.tiendas3b.almacen.adapters;

/**
 * Created by dfa on 11/04/2016.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.tiendas3b.almacen.db.dao.ArticleResearch;

import java.util.List;

public class ArticlesResearchTableAdapter extends BaseTableAdapter<ArticleResearch> {

//    private static final int TEXT_SIZE = 14;
//    private final List<ArticleResearch> allItems;
////    private AsyncTask<Void, Void, Void> task;
//    private final Context mContext;
//    private final DecimalFormat dfThousnds;
//    private List<ArticleResearch> items;

    public ArticlesResearchTableAdapter(Context context, List<ArticleResearch> data, List<ArticleResearch> allItems) {
        super(context, data, allItems);
//        mContext = context;
//        items = data;
//        this.allItems = allItems;
//        dfThousnds = new DecimalFormat(Constants.FORMAT_INT_THOUSANDS);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        View renderedView = null;
        ArticleResearch item = getRowData(rowIndex);

        switch (columnIndex) {
            case 0:
                renderedView = renderString(dfThousnds.format(item.getAvgPed()), Gravity.END);
                break;
            case 1:
                renderedView = renderString(dfThousnds.format(item.getAvgFzd()), Gravity.END);
                break;
            case 2:
                renderedView = renderString(dfThousnds.format(item.getAvgSurt()), Gravity.END);
                break;
            case 3:
                renderedView = renderString(dfThousnds.format(item.getAvgSo()), Gravity.END);
                break;
            case 4:
                renderedView = renderString(dfThousnds.format(item.getAvgInvest()), Gravity.END);
                break;
            case 5:
                renderedView = renderString(dfThousnds.format(item.getAvgCancel()), Gravity.END);
                break;
            default:
                break;
        }

        return renderedView;
    }

//    private View renderString(String value, int gravity) {
//        TextView textView = new TextView(getContext());
//        textView.setText(value);
//        textView.setGravity(gravity);
//        textView.setPadding(20, 10, 20, 10);
//        textView.setTextSize(TEXT_SIZE);
//        return textView;
//    }

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
//    private void applyAndAnimateRemovals(List<ArticleResearch> newModels) {
//        int size = items.size() - 1;
//        for (int i = size; i > -1; i--) {
//            final ArticleResearch model = items.get(i);
//            if (!newModels.contains(model)) {
//                removeItem(i);
//            }
//        }
//    }
//
//    private void applyAndAnimateAdditions(List<ArticleResearch> newModels) {
//        for (int i = 0, count = newModels.size(); i < count; i++) {
//            final ArticleResearch model = newModels.get(i);
//            if (!items.contains(model)) {
//                addItem(i, model);
//            }
//        }
//    }
//
//    private void applyAndAnimateMovedItems(List<ArticleResearch> newModels) {
//        int size = newModels.size() - 1;
//        for (int toPosition = size; toPosition > -1; toPosition--) {
//            final ArticleResearch model = newModels.get(toPosition);
//            final int fromPosition = items.indexOf(model);
//            if (fromPosition > -1 && fromPosition != toPosition) {
//                moveItem(fromPosition, toPosition);
//            }
//        }
//    }
//
//    public ArticleResearch removeItem(int position) {
//        final ArticleResearch model = items.remove(position);
////        notifyItemRemoved(position);
//        return model;
//    }
//
//    public void addItem(int position, ArticleResearch model) {
//        items.add(position, model);
////        notifyItemInserted(position);
//    }
//
//    public void moveItem(int fromPosition, int toPosition) {
//        final ArticleResearch model = items.remove(fromPosition);
//        items.add(toPosition, model);
////        notifyItemMoved(fromPosition, toPosition);
//    }
}
