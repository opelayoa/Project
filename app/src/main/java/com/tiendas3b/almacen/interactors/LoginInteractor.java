package com.tiendas3b.almacen.interactors;

import android.content.Context;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.User;
import com.tiendas3b.almacen.finished.listeners.OnDownloadCatalogsFinishedListener;
import com.tiendas3b.almacen.finished.listeners.OnLoginFinishedListener;

/**
 * Created by dfa on 17/02/2016.
 */
public interface LoginInteractor {

    void login(String username, String password, OnLoginFinishedListener listener, String url);

    void downloadCatalogs(GlobalState context, OnDownloadCatalogsFinishedListener listener);

    void savePreferences(Context mContext, User user);

    void loginWithoutMd5(String login, String password, OnLoginFinishedListener listener, String url);
}
