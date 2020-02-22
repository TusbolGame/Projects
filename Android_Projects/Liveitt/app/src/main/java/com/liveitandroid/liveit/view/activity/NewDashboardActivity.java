package com.liveitandroid.liveit.view.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.liveitandroid.liveit.CustomDialog;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.helper.Urls;
import com.liveitandroid.liveit.helper.XMLParser;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.DatabaseUpdatedStatusDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.view.nstplayer.NSTPlayerSkyActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;
import org.json.XML;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NewDashboardActivity extends AppCompatActivity implements OnClickListener {
    private static final int TIME_INTERVAL = 2000;
    @BindView(R.id.conta_linear)
    LinearLayout conta_linear;
    @BindView(R.id.settings_linear)
    LinearLayout settings_linear;
    @BindView(R.id.updates_linear)
    LinearLayout updates_linear;
    @BindView(R.id.sair_linear)
    LinearLayout sair_linear;
    @BindView(R.id.gravacoes)
    LinearLayout gravacoes;
    @BindView(R.id.menu_extras)
    LinearLayout menu_extras;
    private Context context = this;
    String currentDate = "";
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel();
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.epg)
    LinearLayout epg;
    private LiveStreamDBHandler liveStreamDBHandler;
    @BindView(R.id.live_tv)
    LinearLayout live_tv;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesAfterLoginChannels;
    private SharedPreferences loginPreferencesAfterLoginEPG;
    private SharedPreferences loginPreferencesSharedPref;
    private Editor loginPrefsEditorChannels;
    private Editor loginPrefsEditorEPG;
    private long mBackPressed;
    @BindView(R.id.main_layout)
    LinearLayout main_layout;
    @BindView(R.id.menu_moviesvod)
    LinearLayout menu_moviesvod;
    @BindView(R.id.menu_seriesvod)
    LinearLayout menu_seriesvod;
    @BindView(R.id.settings)
    LinearLayout settings;
    @BindView(R.id.time)
    TextView time;
    private String userName = "";
    private String userPassword = "";

    class C15575 implements DialogInterface.OnClickListener {
        C15575() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.logoutUser(context);
        }
    }

    class C15564 implements DialogInterface.OnClickListener {
        C15564() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C16471 implements Runnable {
        C16471() {
        }

        public void run() {
            try {
                String dateValue = Calendar.getInstance().getTime().toString();
                String currentCurrentTime = Utils.getTime(NewDashboardActivity.this.context);
                String currentCurrentDate = Utils.getDate(dateValue);
                if (NewDashboardActivity.this.time != null) {
                    NewDashboardActivity.this.time.setText(currentCurrentTime);
                }
                if (NewDashboardActivity.this.date != null) {
                    NewDashboardActivity.this.date.setText(currentCurrentDate);
                }
            } catch (Exception e) {
            }
        }
    }

    class CountDownRunner implements Runnable {
        CountDownRunner() {
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    NewDashboardActivity.this.doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e2) {
                }
            }
        }
    }

    public void doWork() {
        runOnUiThread(new C16471());
    }

    private class OnFocusChangeAccountListener implements OnFocusChangeListener {
        private final View view;

        public OnFocusChangeAccountListener(View view) {
            this.view = view;
        }

        @SuppressLint({"ResourceType"})
        public void onFocusChange(View v, boolean hasFocus) {
            float to = 2.0f;
            if (hasFocus) {
                if (hasFocus) {
                    to = 1.09f;
                } else {
                    to = 1.0f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                if (this.view.getTag().equals("1")) {
                    this.view.setBackgroundResource(R.drawable.livetv_focused);
                } else if (this.view.getTag().equals("2")) {
                    this.view.setBackgroundResource(R.drawable.ondemand_focused);
                } else if (this.view.getTag().equals("3")) {
                    this.view.setBackgroundResource(R.drawable.catch_up_focused);
                } else if (this.view.getTag().equals("4")) {
                    this.view.setBackgroundResource(R.drawable.livetv_focused);
                } else if (this.view.getTag().equals("5")) {
                    this.view.setBackgroundResource(R.drawable.green_focused);
                } else if (this.view.getTag().equals("6")) {
                    this.view.setBackgroundResource(R.drawable.green_focused);
                } else if (this.view.getTag().equals("7")) {
                    this.view.setBackgroundResource(R.drawable.green_focused);
                } else if (this.view.getTag().equals("8")) {
                    this.view.setBackgroundResource(R.drawable.green_focused);
                } else if (this.view.getTag().equals("9")) {
                    this.view.setBackgroundResource(R.drawable.green_focused);
                } else if (this.view.getTag().equals("10")) {
                    this.view.setBackgroundResource(R.drawable.green_focused);
                } else if (this.view.getTag().equals("11")) {
                    this.view.setBackgroundResource(R.drawable.green_focused);
                }
            } else if (!hasFocus) {
                if (hasFocus) {
                    to = 1.09f;
                } else {
                    to = 1.0f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                performAlphaAnimation(hasFocus);
                if (this.view.getTag().equals("1")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_01);
                } else if (this.view.getTag().equals("2")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_02);
                } else if (this.view.getTag().equals("3")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_03);
                } else if (this.view.getTag().equals("4")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_01);
                } else if (this.view.getTag().equals("5")) {
                    this.view.setBackgroundResource(R.drawable.riple_green);
                } else if (this.view.getTag().equals("6")) {
                    this.view.setBackgroundResource(R.drawable.riple_green);
                } else if (this.view.getTag().equals("7")) {
                    this.view.setBackgroundResource(R.drawable.riple_green);
                } else if (this.view.getTag().equals("8")) {
                    this.view.setBackgroundResource(R.drawable.riple_green);
                } else if (this.view.getTag().equals("9")) {
                    this.view.setBackgroundResource(R.drawable.riple_green);
                } else if (this.view.getTag().equals("10")) {
                    this.view.setBackgroundResource(R.drawable.riple_green);
                } else if (this.view.getTag().equals("11")) {
                    this.view.setBackgroundResource(R.drawable.riple_green);
                }
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
    public LinearLayout fragment_fil_p2;
    private SessionManager mSessionManager;
    @SuppressLint({"SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_dashboard_layout);
        ButterKnife.bind(this);
        changeStatusBarColor();
        hideSystemUi();
        new Thread(new CountDownRunner()).start();
        makeButtonsClickable();
        this.live_tv.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.live_tv));
        this.live_tv.requestFocus();
        this.epg.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.epg));
        this.menu_moviesvod.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_moviesvod));
        this.menu_seriesvod.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_seriesvod));
        this.settings.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.settings));
        this.gravacoes.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.gravacoes));
        this.menu_extras.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_extras));

        this.conta_linear.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.conta_linear));
        this.settings_linear.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.settings_linear));
        this.updates_linear.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.updates_linear));
        this.sair_linear.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.sair_linear));

        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        String expDate = this.loginPreferencesAfterLogin.getString(AppConst.LOGIN_PREF_EXP_DATE, "");

        //this.tvExpiryDate.setText("Data-Final : " + new SimpleDateFormat("MMMM d, yyyy").format(new Date(((long) Integer.parseInt(expDate)) * 1000)));
        mSessionManager = new SessionManager(this.context);

        TextView tv_expiry_date = (TextView) findViewById(R.id.expiration_date);
        TextView tv_nome_suse = (TextView) findViewById(R.id.nome_user);
        TextView tv_email_suse = (TextView) findViewById(R.id.email_user);

        tv_expiry_date.setText("Fim: "+mSessionManager.getUserExpiryDate());
        tv_nome_suse.setText(""+mSessionManager.getUserNome());
        tv_email_suse.setText(""+mSessionManager.getUserEmail());

        fragment_fil_p2 = (LinearLayout) findViewById(R.id.main_layout);
        if (!mSessionManager.getUserFundo().equals("")) {
            Picasso.with(getApplicationContext()).load(mSessionManager.getUserFundo()).into(new Target() {
                @Override
                public void onPrepareLoad(Drawable arg0) {
                    fragment_fil_p2.setBackgroundResource(R.drawable.splash_new_bg);
                }

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                    fragment_fil_p2.setBackground(new BitmapDrawable(getApplicationContext().getResources(), bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable arg0) {
                    fragment_fil_p2.setBackgroundResource(R.drawable.splash_new_bg);
                }
            });
        }
    }

    private void GetCurrentDateTime() {
        String dateValue = Calendar.getInstance().getTime().toString();
        String currentCurrentTime = Utils.getTime(this.context);
        String currentCurrentDate = Utils.getDate(dateValue);
        if (this.time != null) {
            this.time.setText(currentCurrentTime);
        }
        if (this.date != null) {
            this.date.setText(currentCurrentDate);
        }
    }

    protected void onResume() {
        hideSystemUi();
        new Thread(new CountDownRunner()).start();
        super.onResume();
    }

    private void makeButtonsClickable() {
        this.live_tv.setOnClickListener(this);
        this.menu_moviesvod.setOnClickListener(this);
        this.menu_seriesvod.setOnClickListener(this);
        this.epg.setOnClickListener(this);
        this.settings.setOnClickListener(this);
        this.gravacoes.setOnClickListener(this);
        this.menu_extras.setOnClickListener(this);
        this.conta_linear.setOnClickListener(this);
        this.settings_linear.setOnClickListener(this);
        this.updates_linear.setOnClickListener(this);
        this.sair_linear.setOnClickListener(this);
    }

    public void onClick(View view) {
        String app_name = getBaseContext().getApplicationInfo().loadLabel(getBaseContext().getPackageManager()).toString();
        switch (view.getId()) {
            case R.id.conta_linear:
                startActivity(new Intent(this, AccountInfoActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.updates_linear:
                startActivity(new Intent(this, AtualizaocesActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.menu_extras:
                startActivity(new Intent(this, ExtrasMenuActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.sair_linear:
                new AlertDialog.Builder(this.context, R.style.AlertDialogCustom).setTitle(getResources().getString(R.string.logout_title)).setMessage(getResources().getString(R.string.logout_message)).setPositiveButton(17039379, new C15575()).setNegativeButton(17039369, new C15564()).show();
                return;
            case R.id.settings_linear:
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.epg:
                if (checkEPGAutomation()) {
                    launchTvGuide();
                } else {
                    startActivity(new Intent(this, NewEPGCategoriesActivity.class));
                }
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.live_tv:
                if(mSessionManager.getSeries().equals("true") || mSessionManager.getSeries().equals("nao") || mSessionManager.getSeries().equals("streaminy")) {
                    if (checkChannelsAutomation()) {
                        launchImportChannels("live");
                    } else {
                        this.currentDate = currentDateValue();
                        startXMLTV2(getUserName(), getUserPassword(), this.currentDate);
                    }
                }else{
                    AbrirIPTVCORE("exceptional");
                }
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.menu_moviesvod:
                startActivity(new Intent(this, VodMenuEscolhaFilmes.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.menu_seriesvod:
                startActivity(new Intent(this, VodMenuEscolhaSeries.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.settings:
                if (checkChannelsAutomation()) {
                    launchImportChannels("catchup");
                } else {
                    startActivity(new Intent(this, TVArchiveActivityNewFlow.class));
                }
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.gravacoes:
                Dexter.withActivity(this)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                                startActivity(new Intent(NewDashboardActivity.this, RecordingsListActivity.class));
                            }
                            @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                                Toast.makeText(NewDashboardActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                            }
                            @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                Toast.makeText(NewDashboardActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                            }
                        }).check();
                return;
            default:
                return;
        }
    }

    private static final String _IPTV_CORE_CLASS_NAME = "ru.iptvremote.android.iptv.core.ChannelsActivity";
    private static final String _IPTV_CORE_PACKAGE_NAME = "ru.iptvremote.android.iptv.core";
    public void AbrirIPTVCORE(String reideus)
    {
        try {
            Intent intent = new Intent();
            try {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=ru.iptvremote.android.iptv.core"));
            } catch (ActivityNotFoundException e) {
                intent = new Intent(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=ru.iptvremote.android.iptv.core")));
            }
            intent.setClassName(_IPTV_CORE_PACKAGE_NAME, _IPTV_CORE_CLASS_NAME);
            String username = mSessionManager.getUserName();
            String password = mSessionManager.getUserPASSWORD();
            String dns = mSessionManager.getDNSUrl();
            String tvqrqu_url2 = dns+"get.php?username=" + username + "&password=" + password + "&type=m3u_plus&output=mpegts&epg_shift=0";
            intent.setData(Uri.parse(tvqrqu_url2));
            String tvqrqu_url = dns+"xmltv.php?username=" + username + "&password=" + password + "&epg_shift=0";
            intent.putExtra("url-tvg", tvqrqu_url);

            intent.putExtra("preferred_player_package", mSessionManager.getPackageSelectedTVNovo());
            intent.putExtra("package", getPackageName());
            intent.putExtra("http_connect_timeout", 60000);
            intent.putExtra("http_read_timeout", 60000);
            startActivity(intent);
        }catch (ActivityNotFoundException e) {
            showIptvCoreNotFoundDialog();
        }
    }

    public void showIptvCoreNotFoundDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(mSessionManager.getNameAPP()+"-Erro");
        builder.setMessage("Instalar por favor o IPTV-Core na playstore. E volte de novo aqui.");
        builder.setPositiveButton("Ok, Instalar Agora.",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=ru.iptvremote.android.iptv.core")));
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=ru.iptvremote.android.iptv.core")));
                        }
                    }
                });
        alert = builder.create();
        alert.show();
    }

    AlertDialog alert;
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

    public void hideSystemUi() {
        this.main_layout.setSystemUiVisibility(4871);
    }

    public void onBackPressed() {
        if (this.mBackPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            return;
        }
        Toast.makeText(getBaseContext(), "Prima Novamente Voltar para Terminar o processo da aplicação.", 0).show();
        this.mBackPressed = System.currentTimeMillis();
    }

    public static String parseDateToddMMyyyy(String time) {
        String updatedDate = "";
        try {
            return new SimpleDateFormat(" MMMM dd,yyyy").format(new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US).parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return updatedDate;
        }
    }

    private String currentDateValue() {
        return Utils.parseDateToddMMyyyy(Calendar.getInstance().getTime().toString());
    }

    private void launchImportChannels(String type) {
        this.currentDate = currentDateValue();
        startImportChannels(getUserName(), getUserPassword(), this.currentDate, type);
    }

    private void launchTvGuide() {
        this.currentDate = currentDateValue();
        startXMLTV(getUserName(), getUserPassword(), this.currentDate);
    }

    public String getUserName() {
        if (this.context == null) {
            return this.userName;
        }
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        return this.loginPreferencesAfterLogin.getString("username", "");
    }

    public String getUserPassword() {
        if (this.context == null) {
            return this.userPassword;
        }
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        return this.loginPreferencesAfterLogin.getString("password", "");
    }

    private void startImportChannels(String userName, String userPassword, String currentDate, String type) {
        String status = "";
        String lastUpdatedStatusdate = "";
        int channelsCount = this.liveStreamDBHandler.getAvailableChannelsCount();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.databaseUpdatedStatusDBModelEPG = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_CHANNELS, "1");
        if (this.databaseUpdatedStatusDBModelEPG != null) {
            status = this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState();
            lastUpdatedStatusdate = this.databaseUpdatedStatusDBModelEPG.getDbLastUpdatedDate();
        }
        long dateDifference = getDateDiff(simpleDateFormat, lastUpdatedStatusdate, currentDate);
        if (channelsCount == 0 || dateDifference >= 2) {
            startImportLiveActivity(type);
        }else if (type.equals("live")) {
            if (checkEPGAutomation()) {
                this.currentDate = currentDateValue();
                startXMLTV2(getUserName(), getUserPassword(), this.currentDate);
            } else {
                startActivity(new Intent(this, LiveActivityNewFlow.class));
            }
        } else if (type.equals(AppConst.VOD)) {
            startActivity(new Intent(this, VodActivityNewFlow.class));
        } else if (type.equals("series")) {
            startActivity(new Intent(this, SeriesActivtyNewFlow.class));
        } else if (type.equals("catchup")) {
            startActivity(new Intent(this, TVArchiveActivityNewFlow.class));
        }
    }


    public void startXMLTV2(String userName, String userPassword, String currentDate) {
        String status = "";
        String lastUpdatedStatusdate = "";
        int epgCount = this.liveStreamDBHandler.getEPGCount();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.databaseUpdatedStatusDBModelEPG = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID);
        if (this.databaseUpdatedStatusDBModelEPG != null) {
            status = this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState();
            lastUpdatedStatusdate = this.databaseUpdatedStatusDBModelEPG.getDbLastUpdatedDate();
        }
        long dateDifference = getDateDiff(simpleDateFormat, lastUpdatedStatusdate, currentDate);
        if (dateDifference >= 2 || epgCount == 0) {
            startImportTvGuideActivity2();
        } else{
            startActivity(new Intent(this, LiveActivityNewFlow.class));
        }
    }


    public void startImportTvGuideActivity2() {

        if (this.liveStreamDBHandler != null) {
            AppConst appConst = new AppConst();
            this.liveStreamDBHandler.updateDBStatusAndDate(appConst.DB_EPG, appConst.DB_EPG_ID, appConst.DB_UPDATED_STATUS_PROCESSING, this.currentDate);
            Intent epgIntent = new Intent(this, ImportEPGActivity.class);
            epgIntent.setAction("redirect_ATUALIZA");
            startActivity(epgIntent);
            overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        }
    }

    public void startXMLTV(String userName, String userPassword, String currentDate) {
        String status = "";
        String lastUpdatedStatusdate = "";
        int epgCount = this.liveStreamDBHandler.getEPGCount();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.databaseUpdatedStatusDBModelEPG = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID);
        if (this.databaseUpdatedStatusDBModelEPG != null) {
            status = this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState();
            lastUpdatedStatusdate = this.databaseUpdatedStatusDBModelEPG.getDbLastUpdatedDate();
        }
        long dateDifference = getDateDiff(simpleDateFormat, lastUpdatedStatusdate, currentDate);
        if (dateDifference >= 2 || epgCount == 0) {
            startImportTvGuideActivity();
        } else{
            startTvGuideActivity();
        }
    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void startImportTvGuideActivity() {
        if (this.liveStreamDBHandler != null) {
            AppConst appConst = new AppConst();
            this.liveStreamDBHandler.updateDBStatusAndDate(appConst.DB_EPG, appConst.DB_EPG_ID, appConst.DB_UPDATED_STATUS_PROCESSING, this.currentDate);
            Intent epgIntent = new Intent(this, ImportEPGActivity.class);
            epgIntent.setAction("redirect_epg_category");
            startActivity(epgIntent);
            overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        }
    }

    public void startImportLiveActivity(String type) {
        if (this.liveStreamDBHandler != null) {
            this.liveStreamDBHandler.updateDBStatusAndDate(AppConst.DB_CHANNELS, "1", AppConst.DB_UPDATED_STATUS_PROCESSING, this.currentDate);
            Intent channelsIntent = new Intent(this, ImportStreamsActivity.class);
            if (type.equals("live")) {
                if (checkEPGAutomation()) {
                    this.currentDate = currentDateValue();
                    startXMLTV2(getUserName(), getUserPassword(), this.currentDate);
                } else {
                    channelsIntent.setAction(ImportStreamsActivity.REDIRECT_LIVE_TV);
                    startActivity(channelsIntent);
                }
            } else if (type.equals(AppConst.VOD)) {
                channelsIntent.setAction(ImportStreamsActivity.REDIRECT_VOD);
                startActivity(channelsIntent);
            } else if (type.equals("catchup")) {
                channelsIntent.setAction(ImportStreamsActivity.REDIRECT_CATCHUP);
                startActivity(channelsIntent);
            } else {
                channelsIntent.setAction(ImportStreamsActivity.REDIRECT_SERIES);
                startActivity(channelsIntent);
            }
            overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        }
    }

    public boolean checkChannelsAutomation() {
        this.loginPreferencesAfterLoginChannels = getSharedPreferences(AppConst.LOGIN_PREF_AUTOMATION_CHANNELS, 0);
        return this.loginPreferencesAfterLoginChannels.getString(AppConst.LOGIN_PREF_AUTOMATION_CHANNELS, "").equals("checked");
    }

    public boolean checkEPGAutomation() {
        this.loginPreferencesAfterLoginEPG = getSharedPreferences(AppConst.LOGIN_PREF_AUTOMATION_EPG, 0);
        return this.loginPreferencesAfterLoginEPG.getString(AppConst.LOGIN_PREF_AUTOMATION_EPG, "").equals("checked");
    }

    public void startTvGuideActivity() {
        startActivity(new Intent(this, NewEPGCategoriesActivity.class));
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }
}
