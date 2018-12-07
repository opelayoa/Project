package com.tiendas3b.almacen.views;

/**
 * Created by dfa on 17/02/2016.
 */
public interface LoginView {

    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError(int connectionError);

    void navigateToHome();

    void resetErrors();

    boolean sameRegion(Long region);

//    void noQuierasChamaquear();

    void setEmptyError();
}
