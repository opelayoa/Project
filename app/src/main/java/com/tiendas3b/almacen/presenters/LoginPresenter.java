package com.tiendas3b.almacen.presenters;

/**
 * Created by dfa on 17/02/2016.
 */
public interface LoginPresenter {

    void validateCredentials(String username, String password);

    void onDestroy();

    void validateCredentialsWithoutMd5();
}
