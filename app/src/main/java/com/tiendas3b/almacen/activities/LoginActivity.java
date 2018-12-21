package com.tiendas3b.almacen.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.presenters.LoginPresenter;
import com.tiendas3b.almacen.presenters.LoginPresenterImpl;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.PermissionUtil;
import com.tiendas3b.almacen.util.Preferences;
import com.tiendas3b.almacen.views.LoginView;

import io.fabric.sdk.android.Fabric;

public class  LoginActivity extends AppCompatActivity implements LoginView {

    private AutoCompleteTextView txtUser;
    private EditText txtPassword;
    private Spinner spnRegion;
    private ProgressDialog progressDialog;
    private View loginForm;
    private LoginPresenter presenter;
    private String url;
    private boolean onload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics(), new Answers())
                .debuggable(true)
                .build();
        Fabric.with(fabric);*/
        setContentView(R.layout.activity_login);
        GlobalState mContext = (GlobalState) getApplicationContext();
        presenter = new LoginPresenterImpl(this, mContext);
        txtUser = findViewById(R.id.txt_user);

        onload = true;

        spnRegion = findViewById(R.id.spnRegion);
        spnRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (onload) {
                    onload = false;
                } else {
                    saveUrl(position);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        txtPassword = findViewById(R.id.password);
        txtPassword.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (/*id == R.id.login ||*/ id == EditorInfo.IME_ACTION_DONE) {
                saveUrl(spnRegion.getSelectedItemPosition());
                presenter.validateCredentials(txtUser.getText().toString(), txtPassword.getText().toString());
                return true;
            }
            return false;
        });

        Button btnSignin = findViewById(R.id.btn_sign_in);
        btnSignin.setOnClickListener(v -> {
//                Answers.getInstance().logCustom(new CustomEvent("LoginActivity").putCustomAttribute("btnSignin", "onClick"));
            saveUrl(spnRegion.getSelectedItemPosition());
            presenter.validateCredentials(txtUser.getText().toString(), txtPassword.getText().toString());
        });

        loginForm = findViewById(R.id.login_form);
        loginForm.setVisibility(View.GONE);
        final View imgLogo = findViewById(R.id.img_logo);
        Animation anmTranslate = AnimationUtils.loadAnimation(this, R.anim.translate_login);
        anmTranslate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgLogo.clearAnimation();
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imgLogo.getWidth(), imgLogo.getHeight());
                lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                imgLogo.setLayoutParams(lp);
                loginForm.setVisibility(View.VISIBLE);
                Animation animFade = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_login);
                animFade.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        txtUser.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(txtUser, InputMethodManager.SHOW_IMPLICIT);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                loginForm.startAnimation(animFade);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imgLogo.startAnimation(anmTranslate);


//        Preferences p = new Preferences(LoginActivity.this);
//        url = p.getSharedStringSafe(Preferences.KEY_URL, "");
//        if ("".equals(url)) {
//            saveUrl(0);
//        }
        presenter.validateCredentialsWithoutMd5();
        //-
        txtUser.setText("");
        txtPassword.setText("");
        //-


//        String truckId = p.getSharedStringSafe(Prefe  rences.KEY_TRUCK_ID, "0");
//        showRoadmapButton(Long.parseLong(truckId));

    }

    private void showRoadmapButton(long truckId) {
        if(truckId > 0){
            findViewById(R.id.btnTruck).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        FileUtil.writeFile("Login: start TrackingService");
//        Intent i = new Intent(this, TrackingService.class);
//        startService(i);
    }

    private void saveUrl(int position) {
        Preferences p = new Preferences(LoginActivity.this);
        url = getUrl(position + 1);
        String region = getRegion(position + 1);
//        Answers.getInstance().logCustom(new CustomEvent("LoginActivity").putCustomAttribute("saveUrl", "url:" + url));
        p.setSharedStringSafe(Preferences.KEY_REGION, region);
        p.setSharedStringSafe(Preferences.KEY_URL, url);
    }

    private String getRegion(int position) {
        switch (position) {
            case 1:
            case 1000:
                return Constants.REGION_1000;
            case 2:
            case 1001:
                return Constants.REGION_1001;
            case 3:
            case 1002:
                return Constants.REGION_1002;
            case 4:
            case 1003:
                return Constants.REGION_1003;
            case 5:
            case 1004:
                return Constants.REGION_1004;
            case 6:
            case 1005:
                return Constants.REGION_1005;
//            case 6:
//                return Constants.URL_R1000;
            default:
                return Constants.LOCAL;
        }
    }

    private String getUrl(int position) {
        switch (position) {
            case 1:
            case 1000:
                return Constants.URL_1000;
            case 2:
            case 1001:
                return Constants.URL_1001;
            case 3:
            case 1002:
                return Constants.URL_1002;
            case 4:
            case 1003:
                return Constants.URL_1003;
            case 5:
            case 1004:
                return Constants.URL_1004;
            case 6:
            case 1005:
                return Constants.URL_1005;
//            case 6:
//                return Constants.URL_R1000;
            default:
                return Constants.URL_LOCAL;
        }
    }

    @Override
    protected void onPause() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        showProgress(true);
    }

    @Override
    public void hideProgress() {
        showProgress(false);
    }

    @Override
    public void setUsernameError() {
        txtUser.setError(getString(R.string.error_invalid_user));
        txtUser.requestFocus();
    }

    @Override
    public void setPasswordError(int connectionError) {
        txtPassword.setError(getMessage(connectionError));
        txtPassword.requestFocus();
    }

    private String getMessage(int connectionError) {
        switch (connectionError){
            case 1://"SocketTimeoutException";//no server
            case 2://"ConnectException";//Failed to connect to ip //no server o wifi
            case 3://"NoRouteToHostException";//No route to host //wifi
                return connectionError + ": Error al conectarse a " + (url == null ? "NA" : url.substring(7, 20));
            default:
                return getString(R.string.error_invalid_password);
        }
    }

    @Override
    public void navigateToHome() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
//        hideProgress();
//        startActivity(new Intent(this, MainActivity.class));
//        finish();
//        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void resetErrors() {
        txtUser.setError(null);
        txtPassword.setError(null);
    }

    @Override
    public boolean sameRegion(Long region) {
        return url.equals(getUrl(region.intValue()));
    }

//    @Override
//    public void noQuierasChamaquear() {
//        txtPassword.setError("Usuario no corresponde a almacén");
//    }

    @Override
    public void setEmptyError() {
        txtPassword.setError(getString(R.string.error_empty_data));
        txtPassword.requestFocus();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        if (show) {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        ) {
                    presenter.validateCredentials(txtUser.getText().toString(), txtPassword.getText().toString());
                } else {

                }
            }
        }
    }

}

