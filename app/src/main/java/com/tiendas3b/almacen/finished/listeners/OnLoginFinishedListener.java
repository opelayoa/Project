package com.tiendas3b.almacen.finished.listeners;

import com.tiendas3b.almacen.db.dao.User;

/**
 * Created by dfa on 17/02/2016.
 */
public interface OnLoginFinishedListener {

    void onUsernameError();

    void onPasswordError(int connectionError);

    void onSuccess(User user);

    void hideProgress();

    void showProgress();

    void resetErrors();

    void onSuccess2(User user);

    void emptyPassword();
}
