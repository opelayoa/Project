package com.tiendas3b.almacen.interactors;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.User;
import com.tiendas3b.almacen.finished.listeners.OnDownloadCatalogsFinishedListener;
import com.tiendas3b.almacen.finished.listeners.OnLoginFinishedListener;
import com.tiendas3b.almacen.http.ServiceGenerator;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.services.SyncCatalogsServices;
import com.tiendas3b.almacen.sync.SyncUtils;
import com.tiendas3b.almacen.util.Preferences;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;

import retrofit2.Call;

/**
 * Created by dfa on 17/02/2016.
 */
public class LoginInteractorImpl implements LoginInteractor {

    private static final String TAG = "LoginInteractorImpl";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private String url;

    @Override
    public void login(final String username, final String password, final OnLoginFinishedListener listener, String url) {
        this.url = url;

        if (mAuthTask != null) {
            return;
        }

        listener.resetErrors();
        boolean cancel = false;
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            listener.emptyPassword();
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            listener.onUsernameError();
            cancel = true;
        }

        if (!cancel) {
            listener.showProgress();
            mAuthTask = new UserLoginTask(username, password, listener);
            mAuthTask.execute(true);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Boolean, Void, Boolean> {

        private final String email;
        private final String password;
        private final OnLoginFinishedListener listener;
        private User user;
        private Boolean sync;
        private int connectionError;

        UserLoginTask(String email, String password, OnLoginFinishedListener listener) {
            this.email = email;
            this.password = password;
            this.listener = listener;
            connectionError = -1;
        }

//        @Override
//        protected void onPreExecute() {
////            listener.showProgress();
//        }

        @Override
        protected Boolean doInBackground(Boolean... params) {
            sync = params[0];
            boolean success = false;
            try {
                Tiendas3bClient loginService = ServiceGenerator.createService(Tiendas3bClient.class, email, password, url);
                Call<User> call = loginService.login();
                user = call.execute().body();
                success = user != null;
                if (success) {
                    user.setPassword(password);
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                connectionError = 1;
            } catch (ConnectException e) {
                e.printStackTrace();
                connectionError = 2;
            } catch (NoRouteToHostException e) {
                e.printStackTrace();
                connectionError = 3;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return success;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                if (sync) {
                    listener.onSuccess(user);
                } else {
                    listener.onSuccess2(user);
                }
            } else {
                if (sync) {
                    listener.onPasswordError(connectionError);
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            listener.hideProgress();
        }
    }

    @Override
    public void savePreferences(Context context, User user) {
        Preferences sp = new Preferences(context);
        sp.saveData(user);
    }

    @Override
    public void loginWithoutMd5(String login, String password, OnLoginFinishedListener listener, String url) {
        this.url = url;
        mAuthTask = new UserLoginTask(login, password, listener);
        mAuthTask.execute(false);
    }

    @Override
    public void downloadCatalogs(final GlobalState context, OnDownloadCatalogsFinishedListener listener) {
        SyncUtils.CreateSyncAccount(context);
        Intent msgIntent = new Intent(context, SyncCatalogsServices.class);
//        msgIntent.putExtra(SyncCatalogsServices.PARAM_IN_MSG, strInputMsg);
        context.startService(msgIntent);

////        new AsyncTask<Void, Void, Void>(){
////
////            @Override
////            protected Void doInBackground(Void... params) {
////                downloadStatusCat(mContext);
////                downloadUsersCat(mContext);
////                downloadCategoryCat(mContext);
////                downloadTypesCat(mContext);
////                downloadProjectStatusCat(mContext);
////                downloadSymptomCat(mContext);
////                downloadDiagnosticCat(mContext);
////                downloadTypeSymptomCat(mContext);
////                downloadSymptomDiagnosticCat(mContext);
////                downloadImpCauseCat(mContext);
////                downloadBranchTypeCat(mContext);
////                downloadBranchCat(mContext);
////                downloadStandardSolutionCat(mContext);
////                downloadPossibleOriginCat(mContext);
////                downloadProviderCat(mContext);
////                return null;
////            }
////        }.execute();
////TODO notificar... si quiero...
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 0;
    }
}
