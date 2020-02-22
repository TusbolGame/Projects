package com.liveitandroid.liveit.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.android.gms.cast.framework.CastContext;
import com.liveitandroid.liveit.view.interfaces.LoginInterface;
import com.liveitandroid.liveit.BuildConfig;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.WebServiceHandler.Globals;
import com.liveitandroid.liveit.WebServiceHandler.MainAsynListener;
import com.liveitandroid.liveit.WebServiceHandler.RavSharedPrefrences;
import com.liveitandroid.liveit.WebServiceHandler.Webservices;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.callback.LoginCallback;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.database.SeriesStreamsDatabaseHandler;
import com.liveitandroid.liveit.presenter.LoginPresenter;
import com.liveitandroid.liveit.view.interfaces.LoginInterface;
import com.liveitandroid.liveit.view.utility.UtilsMethods;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import java.util.Date;
import java.util.TimeZone;

import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.helper.Urls;
import com.liveitandroid.liveit.helper.XMLParser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class LoginActivity extends AppCompatActivity implements LoginInterface, MainAsynListener<String> {
    public static InputFilter EMOJI_FILTER = new C16351();
    String FirstMdkey;
    String SecondMdkey;
    @BindView(R.id.activity_login)
    LinearLayout activityLogin;
    @BindView(R.id.cb_remember_me)
    CheckBox cbRememberMe;
    private Context context;
    private Dialog dialog;
    @BindView(R.id.et_email)
    EditText emailIdET;
    private DatabaseHandler favDBHandler;
    Handler handler;
    String key;
    private LiveStreamDBHandler liveStreamDBHandler;
    @BindView(R.id.bt_submit)
    Button loginBT;
    private SharedPreferences loginPreferences;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesAfterLoginChannels;
    private SharedPreferences loginPreferencesAfterLoginEPG;
    private SharedPreferences loginPreferencesServerURl;
    private Editor loginPreferencesServerURlPut;
    private SharedPreferences loginPreferencesSharedPref_allowed_format;
    private SharedPreferences loginPreferencesSharedPref_epg_channel_update;
    private SharedPreferences loginPreferencesSharedPref_time_format;
    private Editor loginPrefsEditorBeforeLogin;
    private Editor loginPrefsEditorChannels;
    private Editor loginPrefsEditorEPG;
    private Editor loginPrefsEditor_epgchannelupdate;
    private Editor loginPrefsEditor_fomat;
    private Editor loginPrefsEditor_timefomat;
    private LoginPresenter loginPresenter;
    @BindView(R.id.tv_enter_credentials)
    TextView loginTV;
    private String loginWith;
    String model = Build.MODEL;
    private String password;
    @BindView(R.id.et_password)
    EditText passwordET;
    private ProgressDialog progressDialog;
    int random;
    String reqString;
    String salt;
    private Boolean saveLogin;
    private SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler;
    private String server_url;
    private String username;
    String version;
    @BindView(R.id.iv_logo)
    ImageView yourLogioTV;

    private Button networkButton;

    static class C16351 implements InputFilter {
        C16351() {
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {
                if (Character.getType(source.charAt(index)) == 19) {
                    return "";
                }
            }
            return null;
        }
    }

    class C16362 implements OnClickListener {
        C16362() {
        }

        @SuppressLint({"ApplySharedPref"})
        public void onClick(View view) {
            LoginActivity.this.getSharedPreferences(AppConst.ACCEPTCLICKED, 0).edit().putString(AppConst.ACCEPTCLICKED, "true").commit();
            LoginActivity.this.dialog.dismiss();
        }
    }

    private class OnFocusChangeAccountListener implements OnFocusChangeListener {
        private final View view;

        public OnFocusChangeAccountListener(View view) {
            this.view = view;
        }

        @SuppressLint({"ResourceType"})
        public void onFocusChange(View v, boolean hasFocus) {
            float to = 1.0f;
            if (hasFocus) {
                if (hasFocus) {
                    to = 1.15f;
                }
                try {
                    performScaleXAnimation(to);
                    performScaleYAnimation(to);
                    Log.e("id is", "" + this.view.getTag());
                    if (this.view.getTag().equals("1")) {
                        LoginActivity.this.emailIdET.setSelection(LoginActivity.this.emailIdET.length());
                    } else if (this.view.getTag().equals(AppConst.DB_EPG_ID)) {
                        LoginActivity.this.passwordET.setSelection(LoginActivity.this.passwordET.length());
                    } else if (!this.view.getTag().equals("3")) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (!hasFocus) {
                if (hasFocus) {
                    to = 1.09f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                performAlphaAnimation(hasFocus);
            }
        }

        private void performScaleXAnimation(float to) {
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(this.view, "scaleX", new float[]{to});
            scaleXAnimator.setDuration(150);
            scaleXAnimator.start();
        }

        private void performScaleYAnimation(float to) {
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(this.view, "scaleY", new float[]{to});
            scaleYAnimator.setDuration(150);
            scaleYAnimator.start();
        }

        private void performAlphaAnimation(boolean hasFocus) {
            if (hasFocus) {
                float toAlpha = hasFocus ? 0.6f : 0.5f;
                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this.view, "alpha", new float[]{toAlpha});
                alphaAnimator.setDuration(150);
                alphaAnimator.start();
            }
        }
    }

    public ImageView img;
    AlertDialog alert;
    AlertDialog alert2;
    LinearLayout iv_login_logo;
    ImageView iv_login_logo2;
    protected void onCreate(Bundle savedInstanceState) {
        Webservices.getWebservices = new Webservices(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_new);
        ButterKnife.bind(this);
        initialize();
        changeStatusBarColor();
        appVersionName();
        DEviceVersion();
        getDeviceName();
        GetRandomNumber();
        this.emailIdET.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.emailIdET));
        this.passwordET.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.passwordET));
        this.loginBT.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.loginBT));
        this.cbRememberMe.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.cbRememberMe));
        String value = getSharedPreferences(AppConst.ACCEPTCLICKED, 0).getString(AppConst.ACCEPTCLICKED, "");
        mostrav = (TextView) findViewById(R.id.tv_versao);
        mostravalid = (TextView) findViewById(R.id.tv_tipologia);
        //btn_website = (Button) findViewById(R.id.btn_lwebsite);
        iv_login_logo2 = (ImageView) findViewById(R.id.iv_logo);
        iv_login_logo = (LinearLayout) findViewById(R.id.activity_login);
        mSessionManager = new SessionManager(LoginActivity.this);
        UtilsMethods.Block_SpaceInEditText(this.passwordET);
        this.emailIdET.setFilters(new InputFilter[]{EMOJI_FILTER});

        loginBT.setVisibility(View.GONE);
        mostrav = (TextView) findViewById(R.id.tv_versao);
        mostravalid = (TextView) findViewById(R.id.tv_tipologia);

        networkButton = (Button)findViewById(R.id.network_button);
        networkButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, NetSpeedActivity.class);
                startActivity(intent);
            }
        });
    }

    public String getUserName() {
        if (this.context == null) {
            return this.username;
        }
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        return this.loginPreferencesAfterLogin.getString("username", "");
    }

    public String getUserPassword() {
        if (this.context == null) {
            return this.password;
        }
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        return this.loginPreferencesAfterLogin.getString("password", "");
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 19:
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    private void changeStatusBarColor() {
        Window window = getWindow();
        if (VERSION.SDK_INT >= 19) {
            window.clearFlags(67108864);
        }
        if (VERSION.SDK_INT >= 21) {
            window.addFlags(Integer.MIN_VALUE);
        }
        if (VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    private void initialize() {
        try {
            this.context = this;
            this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
            this.seriesStreamsDatabaseHandler = new SeriesStreamsDatabaseHandler(this.context);
            this.favDBHandler = new DatabaseHandler(this.context);
            if (this.context != null) {
                this.progressDialog = new ProgressDialog(this.context);
                this.progressDialog.setMessage(getResources().getString(R.string.please_wait));
                this.progressDialog.setCanceledOnTouchOutside(false);
                this.progressDialog.setCancelable(false);
                this.progressDialog.setProgressStyle(0);
            }
            this.emailIdET.requestFocus();
            this.username = this.emailIdET.getText().toString();
            this.password = this.passwordET.getText().toString();
            this.loginPresenter = new LoginPresenter(this, this.context);
            this.loginPreferences = getSharedPreferences(AppConst.SHARED_PREFERENCE, 0);
            this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            this.loginPreferencesServerURl = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_SERVER_URL, 0);
            this.loginPrefsEditorBeforeLogin = this.loginPreferences.edit();
            this.saveLogin = Boolean.valueOf(this.loginPreferences.getBoolean(AppConst.PREF_SAVE_LOGIN, false));

            new ValidateAPP().execute();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    void loginCheck() {
        this.loginWith = this.loginPreferences.getString(AppConst.PREF_LOGIN_WITH, "");
        if (!this.loginWith.equals(AppConst.LOGIN_WITH_DETAILS)) {
            return;
        }
        if (!this.saveLogin.booleanValue()) {
            this.emailIdET.setText(this.loginPreferences.getString("username", ""));
            this.passwordET.setText(this.loginPreferences.getString("password", ""));
            this.cbRememberMe.setChecked(false);
        } else if (this.loginPreferencesAfterLogin.getString("username", "").equals("") || this.loginPreferencesAfterLogin.getString("password", "").equals("")) {
            this.emailIdET.setText(this.loginPreferences.getString("username", ""));
            this.passwordET.setText(this.loginPreferences.getString("password", ""));
            this.cbRememberMe.setChecked(true);
        } else {
            this.emailIdET.setText(mSessionManager.getUserName());
            this.passwordET.setText(mSessionManager.getUserPASSWORD());

            if (!mSessionManager.getUserName().equals("") && !mSessionManager.getUserPASSWORD().equals("")) {
                loginBT.requestFocus();
            }
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void atStart() {
        if (this.progressDialog != null) {
            this.progressDialog.show();
        }
    }

    public void onFinish() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

    public void onFailed(String message) {
        if (this.context != null && !message.isEmpty()) {
            Utils.showToast(this.context, message);
        }
    }

    public void validateLogin(LoginCallback loginCallback, String validateLogin) {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
        if (loginCallback == null || loginCallback.getUserLoginInfo() == null) {
            onFailed(getResources().getString(R.string.invalid_server_response));
        } else if (loginCallback.getUserLoginInfo().getAuth().intValue() == 1) {
            String userStatus = loginCallback.getUserLoginInfo().getStatus();
            if (userStatus.equals("Active")) {
                String username = loginCallback.getUserLoginInfo().getUsername();
                String password = loginCallback.getUserLoginInfo().getPassword();
                String serverPort = loginCallback.getServerInfo().getPort();
                String serverUrl = loginCallback.getServerInfo().getUrl();
                String expDate = loginCallback.getUserLoginInfo().getExpDate();
                String isTrial = loginCallback.getUserLoginInfo().getIsTrial();
                String activeCons = loginCallback.getUserLoginInfo().getActiveCons();
                String createdAt = loginCallback.getUserLoginInfo().getCreatedAt();
                String maxConnections = loginCallback.getUserLoginInfo().getMaxConnections();
                List<String> allowedFormatList = loginCallback.getUserLoginInfo().getAllowedOutputFormats();
                if (allowedFormatList.size() != 0) {
                    String allowedFormat = (String) allowedFormatList.get(0);
                }
                Editor editor1 = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0).edit();
                editor1.putString("username", username);
                editor1.putString("password", password);
                editor1.putString(AppConst.LOGIN_PREF_SERVER_PORT, serverPort);
                editor1.putString(AppConst.LOGIN_PREF_SERVER_URL, serverUrl);
                editor1.putString(AppConst.LOGIN_PREF_EXP_DATE, datafim);
                editor1.putString(AppConst.LOGIN_PREF_IS_TRIAL, isTrial);
                editor1.putString(AppConst.LOGIN_PREF_ACTIVE_CONS, activeCons);
                editor1.putString(AppConst.LOGIN_PREF_CREATE_AT, createdAt);
                editor1.putString(AppConst.LOGIN_PREF_MAX_CONNECTIONS, maxConnections);
                editor1.putString(AppConst.LOGIN_PREF_SERVER_URL_MAG, this.dns);
                editor1.commit();
                this.loginPreferencesSharedPref_allowed_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_ALLOWED_FORMAT, 0);
                this.loginPreferencesSharedPref_time_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_TIME_FORMAT, 0);
                this.loginPreferencesSharedPref_epg_channel_update = this.context.getSharedPreferences(AppConst.LOGIN_PREF_EPG_CHANNEL_UPDATE, 0);
                this.loginPreferencesAfterLoginChannels = this.context.getSharedPreferences(AppConst.LOGIN_PREF_AUTOMATION_CHANNELS, 0);
                this.loginPreferencesAfterLoginEPG = this.context.getSharedPreferences(AppConst.LOGIN_PREF_AUTOMATION_EPG, 0);
                this.loginPrefsEditor_fomat = this.loginPreferencesSharedPref_allowed_format.edit();
                this.loginPrefsEditor_timefomat = this.loginPreferencesSharedPref_time_format.edit();
                this.loginPrefsEditor_epgchannelupdate = this.loginPreferencesSharedPref_epg_channel_update.edit();
                this.loginPrefsEditorChannels = this.loginPreferencesAfterLoginChannels.edit();
                this.loginPrefsEditorEPG = this.loginPreferencesAfterLoginEPG.edit();

                if (this.loginPreferencesAfterLoginChannels.getString(AppConst.LOGIN_PREF_AUTOMATION_CHANNELS, "").equals("")) {
                    this.loginPrefsEditorChannels.putString(AppConst.LOGIN_PREF_AUTOMATION_CHANNELS, "checked");
                    this.loginPrefsEditorChannels.apply();
                }
                if (this.loginPreferencesAfterLoginChannels.getString(AppConst.LOGIN_PREF_AUTOMATION_EPG, "").equals("")) {
                    this.loginPrefsEditorEPG.putString(AppConst.LOGIN_PREF_AUTOMATION_EPG, "checked");
                    this.loginPrefsEditorEPG.apply();
                }
                String allowedFormat1 = this.loginPreferencesSharedPref_allowed_format.getString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "");
                if (allowedFormat1 != null && allowedFormat1.equals("")) {
                    this.loginPrefsEditor_fomat.putString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "ts");
                    this.loginPrefsEditor_fomat.apply();
                }
                String timeFormat = this.loginPreferencesSharedPref_time_format.getString(AppConst.LOGIN_PREF_TIME_FORMAT, "");
                if (timeFormat != null && timeFormat.equals("")) {
                    this.loginPrefsEditor_timefomat.putString(AppConst.LOGIN_PREF_TIME_FORMAT, "HH:mm");
                    this.loginPrefsEditor_timefomat.apply();
                }
                String channelupdate = this.loginPreferencesSharedPref_epg_channel_update.getString(AppConst.LOGIN_PREF_EPG_CHANNEL_UPDATE, "");
                if (channelupdate != null && channelupdate.equals("")) {
                    this.loginPrefsEditor_epgchannelupdate.putString(AppConst.LOGIN_PREF_EPG_CHANNEL_UPDATE, "all");
                    this.loginPrefsEditor_epgchannelupdate.apply();
                }
                //Toast.makeText(this, getResources().getString(R.string.logged_in), 0).show();
                if (this.liveStreamDBHandler != null && this.liveStreamDBHandler.getMagportal(serverUrl) == 0) {
                    this.liveStreamDBHandler.deleteAndRecreateAllTables();
                    if (this.favDBHandler != null) {
                        this.favDBHandler.deleteAndRecreateAllTables();
                    }
                    if (this.seriesStreamsDatabaseHandler != null) {
                        this.seriesStreamsDatabaseHandler.deleteAndRecreateAllVSeriesTables();
                    }
                    this.liveStreamDBHandler.addMagPortal(serverUrl);
                }
                startActivity(new Intent(this, NewDashboardActivity.class));
                if (mSessionManager.getUserPASSWORD().equals(this.passwordET.getText()) && mSessionManager.getUserName().equals(this.emailIdET.getText()) && this.context != null && this.liveStreamDBHandler != null && this.liveStreamDBHandler.getAvailableChannelsCount() > 0) {
                    startActivity(new Intent(this, NewDashboardActivity.class));
                    finish();
                    return;
                } else if (this.context != null) {
                    startActivity(new Intent(this, ImportStreamsActivity.class));
                    finish();
                    return;
                } else {
                    return;
                }
            }
            Toast.makeText(this, getResources().getString(R.string.invalid_status) + userStatus, 0).show();
        } else if (validateLogin == AppConst.VALIDATE_LOGIN) {
            Toast.makeText(this, getResources().getString(R.string.invalid_details), 0).show();
        }
    }

    public void stopLoader() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
            Toast.makeText(this, "ERROR Code 2: " + getResources().getString(R.string.network_error), 0).show();
        }
    }

    @OnClick({R.id.bt_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                this.username = this.emailIdET.getText().toString().trim();
                this.password = this.passwordET.getText().toString().trim();
                new LoginTask().execute();
                return;
            default:
                return;
        }
    }

    public void onPostSuccess(String result, int flag, boolean isSucess) {
        if (isSucess && flag == 1) {
            try {
                Globals.jsonObj = new JSONObject(result);
                if (Globals.jsonObj.getString(NotificationCompat.CATEGORY_STATUS).equalsIgnoreCase("true")) {
                    String savedUsername = "";
                    String savedUrl = "";
                    String newUsername = "";
                    String newUrl = "";
                    if (this.loginPreferences != null) {
                        savedUsername = this.loginPreferences.getString("username", "");
                        savedUrl = this.loginPreferences.getString(AppConst.LOGIN_PREF_SERVER_URL_MAG, "");
                    }
                    if (!savedUrl.equals("")) {
                        newUsername = this.emailIdET.getText().toString().trim();
                        newUrl = Globals.jsonObj.optString("su");
                        if (!(savedUsername.equals(newUsername) && savedUrl.equals(newUrl))) {
                            if (this.liveStreamDBHandler != null) {
                                this.liveStreamDBHandler.deleteAndRecreateAllTables();
                            }
                            if (this.favDBHandler != null) {
                                this.favDBHandler.deleteAndRecreateAllTables();
                            }
                            if (this.seriesStreamsDatabaseHandler != null) {
                                this.seriesStreamsDatabaseHandler.deleteAndRecreateAllVSeriesTables();
                            }
                        }
                    }
                    RavSharedPrefrences.set_authurl(this, Globals.jsonObj.optString("su"));
                    this.SecondMdkey = md5(Globals.jsonObj.optString("su") + "*" + RavSharedPrefrences.get_salt(this) + "*" + Globals.RandomNumber);
                    if (Globals.jsonObj.getString("sc").equalsIgnoreCase(this.SecondMdkey)) {
                        this.loginPreferencesServerURlPut.putString(AppConst.LOGIN_PREF_SERVER_URL_MAG, RavSharedPrefrences.get_authurl(this));
                    }
                    this.loginPreferencesServerURlPut.commit();
                    this.loginPresenter.validateLogin(this.username, this.password);
                    return;
                }
                this.progressDialog.dismiss();
                Toast.makeText(this, "Could not connect to the Server !", 0).show();
            } catch (Exception e) {
            }
        }
    }

    public void fazagora()
    {
        try {
            this.loginPreferencesServerURlPut = this.loginPreferencesServerURl.edit();
            if (checkFields()) {
                atStart();
                if (this.cbRememberMe.isChecked()) {
                    this.loginPrefsEditorBeforeLogin.putBoolean(AppConst.PREF_SAVE_LOGIN, true);
                    this.loginPrefsEditorBeforeLogin.putString("username", this.username);
                    this.loginPrefsEditorBeforeLogin.putString("password", this.password);
                    this.loginPrefsEditorBeforeLogin.putString(AppConst.LOGIN_PREF_SERVER_URL_MAG, this.dns);
                    this.loginPrefsEditorBeforeLogin.putString("activationCode", "");
                    this.loginPrefsEditorBeforeLogin.putString(AppConst.PREF_LOGIN_WITH, AppConst.LOGIN_WITH_DETAILS);
                    this.loginPrefsEditorBeforeLogin.commit();

                }else{
                    this.loginPrefsEditorBeforeLogin.clear();
                    this.loginPrefsEditorBeforeLogin.putBoolean(AppConst.PREF_SAVE_LOGIN, false);
                    this.loginPrefsEditorBeforeLogin.putString(AppConst.PREF_LOGIN_WITH, AppConst.LOGIN_WITH_DETAILS);
                    this.loginPrefsEditorBeforeLogin.commit();
                }


                AppConst.BASE_URL = dns;

                this.loginPresenter.validateLogin(this.username, this.password);
                return;
            }
        } catch (Exception e) {

        }
    }


    public void fazagoraGravaLogin()
    {
        if (this.cbRememberMe.isChecked()) {
            this.loginPrefsEditorBeforeLogin.putBoolean(AppConst.PREF_SAVE_LOGIN, true);
            this.loginPrefsEditorBeforeLogin.putString("username", this.username);
            this.loginPrefsEditorBeforeLogin.putString("password", this.password);
            this.loginPrefsEditorBeforeLogin.putString(AppConst.LOGIN_PREF_SERVER_URL_MAG, this.server_url);
            this.loginPrefsEditorBeforeLogin.putString("activationCode", "");
            this.loginPrefsEditorBeforeLogin.putString(AppConst.PREF_LOGIN_WITH, AppConst.LOGIN_WITH_DETAILS);
            this.loginPrefsEditorBeforeLogin.commit();

            Editor editor1 = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0).edit();
            editor1.putString("username", username);
            editor1.putString("password", password);
            editor1.putString(AppConst.LOGIN_PREF_EXP_DATE, datafim);
            editor1.putString(AppConst.LOGIN_PREF_IS_TRIAL, tipo);
            editor1.putString(AppConst.LOGIN_PREF_ACTIVE_CONS, "0");
            editor1.putString(AppConst.LOGIN_PREF_CREATE_AT, datacriacao);
            editor1.putString(AppConst.LOGIN_PREF_MAX_CONNECTIONS, equipamentos);
            editor1.commit();

        }else{
            this.loginPrefsEditorBeforeLogin.clear();
            this.loginPrefsEditorBeforeLogin.putBoolean(AppConst.PREF_SAVE_LOGIN, false);
            this.loginPrefsEditorBeforeLogin.putString(AppConst.PREF_LOGIN_WITH, AppConst.LOGIN_WITH_DETAILS);
            this.loginPrefsEditorBeforeLogin.commit();
        }
        startActivity(new Intent(context, NewDashboardActivity.class));
        finish();
        return;

    }

    public void fazagora2()
    {
        startActivity(new Intent(this, NewDashboardActivity2.class));
        finish();
        return;
    }

    public void onPostError(int flag) {

    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int[] scrcoords = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = (event.getRawX() + ((float) w.getLeft())) - ((float) scrcoords[0]);
            float y = (event.getRawY() + ((float) w.getTop())) - ((float) scrcoords[1]);
            Log.d("Activity", "Touch event " + event.getRawX() + "," + event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() + "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + " coords " + scrcoords[0] + "," + scrcoords[1]);
            if (event.getAction() == 1 && (x < ((float) w.getLeft()) || x >= ((float) w.getRight()) || y < ((float) w.getTop()) || y > ((float) w.getBottom()))) {
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    public void appVersionName() {
        try {
            this.version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void DEviceVersion() {
        this.reqString = VERSION.RELEASE + " " + VERSION_CODES.class.getFields()[VERSION.SDK_INT].getName();
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                if (Character.isWhitespace(c)) {
                    capitalizeNext = true;
                }
                phrase.append(c);
            }
        }
        return phrase.toString();
    }

    public void GetRandomNumber() {
        this.random = new Random().nextInt(8378600) + 10000;
        Globals.RandomNumber = String.valueOf(this.random);
    }

    public static String md5(String s) {
        String MD5 = "MD5";
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(aMessageDigest & 255);
                while (h.length() < 2) {
                    h = AppConst.PASSWORD_UNSET + h;
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean checkFields() {
        if (this.emailIdET.getText().toString().trim().length() == 0) {
            this.emailIdET.setError(getResources().getString(R.string.enter_username_error));
            return false;
        } else if (this.passwordET.getText().toString().trim().length() != 0) {
            return true;
        } else {
            this.passwordET.setError(getResources().getString(R.string.enter_password_error));
            return false;
        }
    }

    //private Button btn_website;
    private SessionManager mSessionManager;
    private String VerAtual, VerServ;
    private String jsonStr, verapp , id_user, app_name, app_namee, erronovo, linkapp, melhoAPP, id, kodigrant, kodiid, kodisecret, website, emailadmin, filmesapp, kodiuser, kodisenha, client_secret, acess_token, kodiapi, tmdb_url, tmdb_api, series_cat, tmdb_img, kodisite, servidor, imageUrl, nome, estado, ordena, series, TipoUser, Pack, tipo, email, utilizador, senha, datacriacao, datafim, dns, epg_url, equipamentos, senhaadultos;
    private JSONObject jsonObject, jobj_userinfo, jobj_usererro, jobj_usertodo;
    private JSONArray jobj_usercanais, jobj_usergrupos;
    private TextView mostrav, mostravalid;

    /*===========Start==> Login AsyncTask============*/
    public class ValidateAPP extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                jobj_usertodo = null;
                PackageManager packageManager = getApplicationContext().getPackageManager();
                String mystring = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA));
                app_namee = mystring;
                app_name = mystring.replace(" ", "%20");
                app_name = app_name.replace(" ", "%20");
                app_name = app_name.replace(" ", "%20");
                jsonStr = Urls.urlValid + app_name + "&tipo_app=APK";
                XMLParser parser = new XMLParser();
                String xml = parser.getXmlFromUrl(jsonStr);
                jobj_usertodo = XML.toJSONObject(xml);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jobj_usertodo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                jobj_usertodo = null;
                jobj_usertodo = jsonObject.getJSONObject("tudo");
                erronovo = null;
                erronovo = jobj_usertodo.optString("erro");
                if (erronovo != "" && erronovo != null) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                    builder2.setTitle(app_namee);
                    builder2.setMessage(erronovo);
                    builder2.setPositiveButton("Fechar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    alert2 = builder2.create();
                    alert2.show();

                    mostrav.setText("Versão Instalada: " + BuildConfig.VERSION_NAME);
                    mostravalid.setText("Aplicação não Certificada. ");
                } else {
                    jobj_usergrupos = jobj_usertodo.getJSONArray("grupos");
                    jobj_userinfo = jobj_usertodo.getJSONObject("validacao");
                    nome = jobj_userinfo.optString("Nome");
                    acess_token = jobj_userinfo.optString("Acess_Token");
                    client_secret = jobj_userinfo.optString("Client_Secret_User");
                    if (jobj_userinfo.optString("logotipo").equals("false")) {
                        iv_login_logo2.setVisibility(View.INVISIBLE);
                    }
                    emailadmin = jobj_userinfo.optString("Email");
                    website = jobj_userinfo.optString("WebSite");
                    kodiuser = jobj_userinfo.optString("user_kodi");
                    filmesapp = jobj_userinfo.optString("filmes_app");
                    kodisenha = jobj_userinfo.optString("senha_kodi");
                    kodiapi = jobj_userinfo.optString("api_kodi");
                    kodisite = jobj_userinfo.optString("site_kodi");
                    kodigrant = jobj_userinfo.optString("grant_type");
                    kodiid = jobj_userinfo.optString("client_id");
                    kodisecret = jobj_userinfo.optString("client_secret");

                    /*if (website.equals("http://")) {
                        btn_website.setText("");
                        btn_website.setVisibility(View.INVISIBLE);
                    } else {
                        btn_website.setVisibility(View.VISIBLE);
                        btn_website.setText("Click aqui para ir até ao site.");
                        btn_website.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(website));
                                startActivity(i);
                            }
                        });
                    }*/
                    imageUrl = jobj_userinfo.optString("fundo");

                    if (!imageUrl.equals("false")) {
                        Picasso.with(getApplicationContext()).load(imageUrl).into(new Target() {
                            @Override
                            public void onPrepareLoad(Drawable arg0) {
                                iv_login_logo.setBackgroundResource(R.drawable.splash_new_bg);
                            }

                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                                iv_login_logo.setBackground(new BitmapDrawable(getApplicationContext().getResources(), bitmap));
                            }

                            @Override
                            public void onBitmapFailed(Drawable arg0) {
                                iv_login_logo.setBackgroundResource(R.drawable.splash_new_bg);
                            }
                        });
                    } else {
                        iv_login_logo.setBackgroundResource(R.drawable.splash_new_bg);
                    }
                    verapp = jobj_userinfo.optString("VerAPP");
                    linkapp = jobj_userinfo.optString("LinkAPP");

                    byte[] data = Base64.decode(jobj_userinfo.optString("Melhoramentos"), Base64.DEFAULT);
                    melhoAPP = new String(data, "UTF-8");

                    mSessionManager.setNameAPP(app_namee);
                    mSessionManager.setMelhoramentos(melhoAPP);

                    tmdb_url = jobj_userinfo.optString("tmdb_link");
                    tmdb_api = jobj_userinfo.optString("tmdb_api");
                    tmdb_img = jobj_userinfo.optString("tmdb_link_img");
                    series_cat = jobj_userinfo.optString("series_cat");

                    mSessionManager.setTMDBAPI(tmdb_api);
                    mSessionManager.setTMDBIMG(tmdb_img);
                    mSessionManager.setTMDBUrl(tmdb_url);
                    mSessionManager.setSeriesCat(series_cat);

                    mSessionManager.setUserFundo(imageUrl);
                    mSessionManager.setUserAcess_Token(acess_token);
                    mSessionManager.setClient_Secret(client_secret);
                    mostrav.setText("Versão Instalada: " + BuildConfig.VERSION_NAME);
                    mostravalid.setText("Aplicação " + app_namee + " Certificada.");

                    loginBT.setVisibility(View.VISIBLE);
                    loginCheck();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*===========Start==> Check online============*/
    private boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    /*===========Start==> Login AsyncTask============*/
    public class LoginTask extends AsyncTask<String, String, JSONObject> {

        private final static long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                jsonObject = null;
                String url2 = mSessionManager.getUserAcess_Token() + Urls.urlLogin1 + username + Urls.urlLogin2 + password + "&client_secret=" + mSessionManager.getClient_Secret();
                XMLParser parser = new XMLParser();
                String xml = parser.getXmlFromUrl(url2);
                jsonObject = XML.toJSONObject(xml);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                jobj_usertodo = null;
                jobj_usertodo = jsonObject.getJSONObject("tudo");
                erronovo = null;
                erronovo = jobj_usertodo.optString("erro");
                if (erronovo != "" && erronovo != null) {
                    Toast.makeText(LoginActivity.this, erronovo, Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                    jobj_usercanais = jobj_usertodo.getJSONArray("canais");
                    if (jobj_usercanais != null) {
                        if (jobj_usercanais.length() > 0) {
                            JSONArray jsonArray2 = new JSONArray();
                            for (int m = 0; m < jobj_usercanais.length(); m++) {
                                JSONObject tmpObj = jobj_usercanais.getJSONObject(m);
                                byte[] data = Base64.decode(tmpObj.optString("Nome"), Base64.DEFAULT);
                                String chName = new String(data, "UTF-8");

                                byte[] data2 = Base64.decode(tmpObj.optString("Imagem"), Base64.DEFAULT);
                                String chImage = new String(data2, "UTF-8");

                                JSONObject objqua = new JSONObject();
                                objqua.put("Nome", chName);
                                objqua.put("Imagem", chImage);
                                jsonArray2.put(objqua);
                            }
                            mSessionManager.setArray(jsonArray2, "Canais", LoginActivity.this);
                        }
                    }
                    jobj_userinfo = jobj_usertodo.getJSONObject("info");
                    id_user = jobj_userinfo.optString("ID");   // User Id
                    nome = jobj_userinfo.optString("Nome");   //

                    servidor = jobj_userinfo.optString("Servidor");   //
                    email = jobj_userinfo.optString("Email"); // Email
                    utilizador = jobj_userinfo.optString("Utilizador");   // User name
                    senha = jobj_userinfo.optString("Senha");     // Password
                    datafim = jobj_userinfo.optString("DataFim");     // Expiry Date
                    datacriacao = jobj_userinfo.optString("DataCriacao");

                    dns = jobj_userinfo.optString("DNS");     // DNS Url
                    epg_url = jobj_userinfo.optString("EPG");     // EPG Url
                    equipamentos = jobj_userinfo.optString("Equipamentos");   // Max Connections
                    senhaadultos = jobj_userinfo.optString("SenhaAdultos"); // Adult Password
                    estado = jobj_userinfo.optString("Estado");
                    tipo = jobj_userinfo.optString("Tipo");
                    Pack = jobj_userinfo.optString("Pack");
                    TipoUser = jobj_userinfo.optString("TipoUser");
                    series = jobj_userinfo.optString("series");
                    ordena = jobj_userinfo.optString("Ordena");

                    byte[] data = Base64.decode(jobj_userinfo.optString("Melhoramentos"), Base64.DEFAULT);
                    melhoAPP = new String(data, "UTF-8");

                    mSessionManager.setMelhoramentos(melhoAPP);

                    mSessionManager.setInicio("sim");
                    if (jobj_userinfo.getString("Estado").equals("Inactivo")) {
                        builder2 = new AlertDialog.Builder(context);
                        builder2.setTitle(app_namee);
                        builder2.setMessage("A sua conta está Expirada. Por favor faça a sua Renovação ou Reactivação para voltar a aceder.\n\n" +
                                " - Site para Renovação: " + acess_token);
                        builder2.setPositiveButton("Fechar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        alert2 = builder2.create();
                        alert2.show();
                    } else {
                        mSessionManager.setSeries(series);
                        mSessionManager.setOrdena(ordena);
                        mSessionManager.setDNSUrl(dns);
                        mSessionManager.setLogin(true);
                        mSessionManager.setUserID(id_user);
                        mSessionManager.setUserName(utilizador);      // User name
                        mSessionManager.setUserEmail(email);      // User name
                        mSessionManager.setUserPASSWORD(senha);
                        mSessionManager.setUserMaxConnections(equipamentos);    // Max Connections
                        mSessionManager.setUserNome(nome);
                        mSessionManager.setPackUser(Pack);
                        mSessionManager.setUserServidor(servidor);
                        mSessionManager.setUserExpiryDate(datafim);    // Expiry Date
                        mSessionManager.setUserStatus(estado);
                        mSessionManager.setUserIsTrial(tipo);
                        mSessionManager.setEmailAdmin(emailadmin);
                        mSessionManager.setkodiUser(kodiuser);
                        mSessionManager.setkodiSenha(kodisenha);
                        mSessionManager.setkodiApi(kodiapi);
                        mSessionManager.setkodiSite(kodisite);
                        mSessionManager.setkodiGrant(kodigrant);
                        mSessionManager.setkodiID(kodiid);
                        mSessionManager.setkodiSecret(kodisecret);
                        mSessionManager.setWebSite(website);
                        mSessionManager.setUserType(TipoUser);

                        if (mSessionManager.getUserAdultos().equals("")) {
                            mSessionManager.setUserAdultos(senhaadultos);
                        }

                        VerAtual = BuildConfig.VERSION_NAME;
                        VerServ = verapp;
                        if (!VerAtual.equals(VerServ)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Atualização "+app_namee);
                            builder.setMessage("Foi feita uma atualização importante na aplicação. Atualize agora mesmo.\n\n" +
                                    " - Versão Instalada: " + VerAtual + "\n" +
                                    " - Versão Atual: " + VerServ);
                            builder.setPositiveButton("Atualizar",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                LoginActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Urls.urlMarket + linkapp)));
                                            } catch (ActivityNotFoundException e) {
                                                LoginActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Urls.urlPlaystore + linkapp)));
                                            }
                                        }
                                    });
                            builder.setNegativeButton("Mais Tarde",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(!mSessionManager.getUserType().equals("Mag"))
                                            {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                builder.setTitle("Funcionalidade: "+app_namee);
                                                builder.setMessage("Pretende ver como segundo dispositivo???\n\n" +
                                                        " - Se optar por abrir normalmente abre como 1º dispositivo. E claro usar esta parte só para um equipamento.\n" +
                                                        " - Pode escolher esta opção sempre que iniciar.\n" +
                                                        " - Assim pode ver tv num dispositivo e ver filmes e o que deixar num segundo, terceiro dispositivo.\n" +
                                                        " - Exemplo: Pode estar a ver na ( Smart-TV ou tablet, ou telefone ou box como primeiro) e abrir num tablet, telefone como segundo e ver um filme ou ouvir uma rádio.");
                                                builder.setPositiveButton("Sim, Abrir como 2º Dispositivo.",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                mSessionManager.setUserType("Mag");
                                                                fazagora2();
                                                            }
                                                        });
                                                builder.setNegativeButton("Abrir normalmente.",
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if(mSessionManager.getSeries().equals("true") || mSessionManager.getSeries().equals("streaminy") || mSessionManager.getSeries().equals("nao")){
                                                                    fazagora();
                                                                }else{
                                                                    fazagoraGravaLogin();
                                                                }
                                                            }
                                                        });
                                                alert = builder.create();
                                                alert.show();
                                            }else
                                            {
                                                mSessionManager.setUserType("Mag");
                                                fazagora2();
                                            }
                                        }
                                    });
                            alert = builder.create();
                            alert.show();
                        } else {
                            if (jobj_userinfo.getString("Tipo").equals("Pagante")) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                long begin = dateFormat.parse(datafim).getTime();
                                long end = new Date().getTime();
                                long diff = (begin - end) / (MILLISECS_PER_DAY);
                                if (diff < 5 && diff > 0) {
                                    builder2 = new AlertDialog.Builder(context);
                                    builder2.setTitle(app_namee);
                                    builder2.setMessage("A sua conta expira daqui a " + diff + " dias. Faça já a sua Renovação.");
                                    builder2.setPositiveButton("Fechar",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if(!mSessionManager.getUserType().equals("Mag"))
                                                    {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                        builder.setTitle("Funcionalidade: "+app_namee);
                                                        builder.setMessage("Pretende ver como segundo dispositivo???\n\n" +
                                                                " - Se optar por abrir normalmente abre como 1º dispositivo. E claro usar esta parte só para um equipamento.\n" +
                                                                " - Pode escolher esta opção sempre que iniciar.\n" +
                                                                " - Assim pode ver tv num dispositivo e ver filmes e o que deixar num segundo, terceiro dispositivo.\n" +
                                                                " - Exemplo: Pode estar a ver na ( Smart-TV ou tablet, ou telefone ou box como primeiro) e abrir num tablet, telefone como segundo e ver um filme ou ouvir uma rádio.");
                                                        builder.setPositiveButton("Sim, Abrir como 2º Dispositivo.",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        mSessionManager.setUserType("Mag");
                                                                        fazagora2();
                                                                    }
                                                                });
                                                        builder.setNegativeButton("Abrir normalmente.",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        if(mSessionManager.getSeries().equals("true") || mSessionManager.getSeries().equals("streaminy") || mSessionManager.getSeries().equals("nao")){
                                                                            fazagora();
                                                                        }else{
                                                                            fazagoraGravaLogin();
                                                                        }
                                                                    }
                                                                });
                                                        alert = builder.create();
                                                        alert.show();
                                                    }else
                                                    {
                                                        mSessionManager.setUserType("Mag");
                                                        fazagora2();
                                                    }
                                                }
                                            });
                                    alert2 = builder2.create();
                                    alert2.show();
                                } else if (diff == 0) {
                                    builder2 = new AlertDialog.Builder(context);
                                    builder2.setTitle(app_namee);
                                    builder2.setMessage("A sua conta expira Hoje. Faça já a sua Renovação.");
                                    builder2.setPositiveButton("Fechar",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if(!mSessionManager.getUserType().equals("Mag"))
                                                    {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                        builder.setTitle("Funcionalidade: "+app_namee);
                                                        builder.setMessage("Pretende ver como segundo dispositivo???\n\n" +
                                                                " - Se optar por abrir normalmente abre como 1º dispositivo. E claro usar esta parte só para um equipamento.\n" +
                                                                " - Pode escolher esta opção sempre que iniciar.\n" +
                                                                " - Assim pode ver tv num dispositivo e ver filmes e o que deixar num segundo, terceiro dispositivo.\n" +
                                                                " - Exemplo: Pode estar a ver na ( Smart-TV ou tablet, ou telefone ou box como primeiro) e abrir num tablet, telefone como segundo e ver um filme ou ouvir uma rádio.");
                                                        builder.setPositiveButton("Sim, Abrir como 2º Dispositivo.",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        mSessionManager.setUserType("Mag");
                                                                        fazagora2();
                                                                    }
                                                                });
                                                        builder.setNegativeButton("Abrir normalmente.",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        if(mSessionManager.getSeries().equals("true") || mSessionManager.getSeries().equals("streaminy") || mSessionManager.getSeries().equals("nao")){
                                                                            fazagora();
                                                                        }else{
                                                                            fazagoraGravaLogin();
                                                                        }
                                                                    }
                                                                });
                                                        alert = builder.create();
                                                        alert.show();
                                                    }else
                                                    {
                                                        mSessionManager.setUserType("Mag");
                                                        fazagora2();
                                                    }
                                                }
                                            });
                                    alert2 = builder2.create();
                                    alert2.show();
                                }else {
                                    if(!mSessionManager.getUserType().equals("Mag"))
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setTitle("Funcionalidade: "+app_namee);
                                        builder.setMessage("Pretende ver como segundo dispositivo???\n\n" +
                                                " - Se optar por abrir normalmente abre como 1º dispositivo. E claro usar esta parte só para um equipamento.\n" +
                                                " - Pode escolher esta opção sempre que iniciar.\n" +
                                                " - Assim pode ver tv num dispositivo e ver filmes e o que deixar num segundo, terceiro dispositivo.\n" +
                                                " - Exemplo: Pode estar a ver na ( Smart-TV ou tablet, ou telefone ou box como primeiro) e abrir num tablet, telefone como segundo e ver um filme ou ouvir uma rádio.");
                                        builder.setPositiveButton("Sim, Abrir como 2º Dispositivo.",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mSessionManager.setUserType("Mag");
                                                        fazagora2();
                                                    }
                                                });
                                        builder.setNegativeButton("Abrir normalmente.",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if(mSessionManager.getSeries().equals("true") || mSessionManager.getSeries().equals("streaminy") || mSessionManager.getSeries().equals("nao")){
                                                            fazagora();
                                                        }else{
                                                            fazagoraGravaLogin();
                                                        }
                                                    }
                                                });
                                        alert = builder.create();
                                        alert.show();
                                    }else
                                    {
                                        mSessionManager.setUserType("Mag");
                                        fazagora2();
                                    }
                                }
                            } else if (jobj_userinfo.getString("Tipo").equals("Teste")) {
                                builder2 = new AlertDialog.Builder(context);
                                builder2.setTitle(app_namee);
                                builder2.setMessage("A conta é uma conta de Teste. Após o teste e gostar faça a sua Aquisição.");
                                builder2.setPositiveButton("Fechar",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(!mSessionManager.getUserType().equals("Mag"))
                                                {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                    builder.setTitle("Funcionalidade: "+app_namee);
                                                    builder.setMessage("Pretende ver como segundo dispositivo???\n\n" +
                                                            " - Se optar por abrir normalmente abre como 1º dispositivo. E claro usar esta parte só para um equipamento.\n" +
                                                            " - Pode escolher esta opção sempre que iniciar.\n" +
                                                            " - Assim pode ver tv num dispositivo e ver filmes e o que deixar num segundo, terceiro dispositivo.\n" +
                                                            " - Exemplo: Pode estar a ver na ( Smart-TV ou tablet, ou telefone ou box como primeiro) e abrir num tablet, telefone como segundo e ver um filme ou ouvir uma rádio.");
                                                    builder.setPositiveButton("Sim, Abrir como 2º Dispositivo.",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    mSessionManager.setUserType("Mag");
                                                                    fazagora2();
                                                                }
                                                            });
                                                    builder.setNegativeButton("Abrir normalmente.",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    if(mSessionManager.getSeries().equals("true") || mSessionManager.getSeries().equals("streaminy") || mSessionManager.getSeries().equals("nao")){
                                                                        fazagora();
                                                                    }else{
                                                                        fazagoraGravaLogin();
                                                                    }
                                                                }
                                                            });
                                                    alert = builder.create();
                                                    alert.show();
                                                }else
                                                {
                                                    mSessionManager.setUserType("Mag");
                                                    fazagora2();
                                                }
                                            }
                                        });
                                alert2 = builder2.create();
                                alert2.show();
                            } else {
                                if(!mSessionManager.getUserType().equals("Mag"))
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Funcionalidade: "+app_namee);
                                    builder.setMessage("Pretende ver como segundo dispositivo???\n\n" +
                                            " - Se optar por abrir normalmente abre como 1º dispositivo. E claro usar esta parte só para um equipamento.\n" +
                                            " - Pode escolher esta opção sempre que iniciar.\n" +
                                            " - Assim pode ver tv num dispositivo e ver filmes e o que deixar num segundo, terceiro dispositivo.\n" +
                                            " - Exemplo: Pode estar a ver na ( Smart-TV ou tablet, ou telefone ou box como primeiro) e abrir num tablet, telefone como segundo e ver um filme ou ouvir uma rádio.");
                                    builder.setPositiveButton("Sim, Abrir como 2º Dispositivo.",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mSessionManager.setUserType("Mag");
                                                    fazagora2();
                                                }
                                            });
                                    builder.setNegativeButton("Abrir normalmente.",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if(mSessionManager.getSeries().equals("true") || mSessionManager.getSeries().equals("streaminy") || mSessionManager.getSeries().equals("nao")){
                                                        fazagora();
                                                    }else{
                                                        fazagoraGravaLogin();
                                                    }
                                                }
                                            });
                                    alert = builder.create();
                                    alert.show();
                                }else
                                {
                                    mSessionManager.setUserType("Mag");
                                    fazagora2();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

