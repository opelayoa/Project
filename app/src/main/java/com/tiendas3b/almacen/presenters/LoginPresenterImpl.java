package com.tiendas3b.almacen.presenters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.User;
import com.tiendas3b.almacen.finished.listeners.OnDownloadCatalogsFinishedListener;
import com.tiendas3b.almacen.finished.listeners.OnLoginFinishedListener;
import com.tiendas3b.almacen.interactors.LoginInteractor;
import com.tiendas3b.almacen.interactors.LoginInteractorImpl;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.PermissionUtil;
import com.tiendas3b.almacen.util.Preferences;
import com.tiendas3b.almacen.views.LoginView;

import java.io.File;
import java.util.UUID;

/**
 * Created by dfa on 17/02/2016.
 */
public class LoginPresenterImpl implements LoginPresenter, OnLoginFinishedListener, OnDownloadCatalogsFinishedListener {

    private LoginView loginView;
    private LoginInteractor loginInteractor;
    private GlobalState mContext;

    public LoginPresenterImpl(LoginView loginView, GlobalState context) {
        this.loginView = loginView;
        this.mContext = context;
        this.loginInteractor = new LoginInteractorImpl();
    }

    @Override
    @SuppressLint("HardwareIds, MissingPermission")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void validateCredentials(String username, String password) {
//        Answers.getInstance().logCustom(new CustomEvent("LoginPresenterImpl").putCustomAttribute("validateCredentials", username +":"+password));
//        if (loginView != null) {
//            loginView.showProgress();
//        }
//        createFolder();
        if (PermissionUtil.hasPermissions((Activity) loginView, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE)) {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath().concat(mContext.getString(R.string.root_folder)));
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Preferences p = new Preferences(mContext);
            String url = p.getSharedStringSafe(Preferences.KEY_URL, Constants.URL_LOCAL);

            TelephonyManager mngr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = mngr.getDeviceId();
            long uniqueID = UUID.randomUUID().toString().hashCode();
            if (TextUtils.isEmpty(imei)) {
                p.setSharedStringSafe(Preferences.KEY_UID, uniqueID + "");
            } else {
                p.setSharedStringSafe(Preferences.KEY_UID, imei);
            }

            loginInteractor.login(username, password, this, url);
        } else {
            PermissionUtil.showDialog((Activity) loginView, PermissionUtil.REQUEST_WRITE_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE);
        }
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void validateCredentialsWithoutMd5() {
        Preferences p = new Preferences(mContext);
        String login = p.getSharedStringSafe(Preferences.KEY_LOGIN, "");
        String url = p.getSharedStringSafe(Preferences.KEY_URL, "");
        if (!"".equals(login) && !"".equals(url)) {
            String password = p.getSharedStringSafe(Preferences.KEY_PASS, "");
            loginInteractor.loginWithoutMd5(login, password, this, url);
        }
    }

    @Override
    public void onUsernameError() {
        if (loginView != null) {
            loginView.setUsernameError();
            hideProgress();
        }
    }

    @Override
    public void onPasswordError(int connectionError) {
        if (loginView != null) {
            loginView.setPasswordError(connectionError);
            hideProgress();
        }
    }

    @Override
    public void onError() {
        //TODO
    }

    @Override
    public void onDownloadCatalogs() {
        //TODO
    }

    @Override
    public void onSuccess(User user) {
        if (loginView != null) {
//            if (loginView.sameRegion(user.getRegion())) {
            setCrashlyticsInfo(user);
            loginView.navigateToHome();

            user.setRegion(mContext.getRegion());
            loginInteractor.savePreferences(mContext, user);
            loginInteractor.downloadCatalogs(mContext, this);
//            } else {
//                loginView.noQuierasChamaquear();
//                hideProgress();
//            }
        }
    }

    private void setCrashlyticsInfo(User user) {
        try {
            Crashlytics.setUserIdentifier("" + user.getSeq());
            Crashlytics.setUserEmail(user.getEmail());
            Crashlytics.setUserName(user.getLogin());
        } catch (Exception ignored){

        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createFolder() {
        if (PermissionUtil.permissionGranted((Activity) loginView, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath().concat(mContext.getString(R.string.root_folder)));
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } else {
            PermissionUtil.showDialog((Activity) loginView, PermissionUtil.REQUEST_WRITE_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void hideProgress() {
        loginView.hideProgress();
    }

    @Override
    public void showProgress() {
        loginView.showProgress();
    }

    @Override
    public void resetErrors() {
        loginView.resetErrors();
    }

    @Override
    public void onSuccess2(User user) {
        if (loginView != null) {
            setCrashlyticsInfo(user);
            loginView.navigateToHome();
        }
    }

    @Override
    public void emptyPassword() {
        loginView.setEmptyError();
    }

}
