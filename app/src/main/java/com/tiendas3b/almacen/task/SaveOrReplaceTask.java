package com.tiendas3b.almacen.task;

import android.content.Context;
import android.os.AsyncTask;

import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;

import java.util.List;

/**
 * Created by danflo on 10/05/2018 madafaka.
 */
public class SaveOrReplaceTask<T> extends AsyncTask<Void, Void, Void> {

    private final IDatabaseManager db;
    private final List<T> list;

    public SaveOrReplaceTask(Context context, List<T> list) {
        this.list = list;
        db = DatabaseManager.getInstance(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        db.insertOrReplaceInTx(list);
        return null;
    }

}
