package com.tiendas3b.almacen.activities;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tiendas3b.almacen.R;

import butterknife.BindView;

@SuppressWarnings("ConstantConditions")
public abstract class RecyclerViewCalendarActivityPR extends BaseTableCalendarActivityPR {

    @BindView(R.id.list)
    protected RecyclerView recyclerView;

    @Override
    protected void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    protected void showData() {
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if (requestCode == getRequestCode()) {
//            if (resultCode == RESULT_OK) {
//                getDatabaseInfo();
//            } else if (resultCode == RESULT_CANCELED) {
//                // Handle cancel
//            }
//        }
//    }
//
//    public abstract int getRequestCode();
//    protected abstract void getDatabaseInfo();
//    protected abstract void setAdapter();
}
