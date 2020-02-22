package com.liveitandroid.liveit.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.database.DatabaseUpdatedStatusDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VodMenuFilmesListaActivity extends AppCompatActivity implements OnClickListener {
    private static final int TIME_INTERVAL = 2000;
    @BindView(R.id.menu_filmes)
    LinearLayout menu_filmes;
    @BindView(R.id.menu_series)
    LinearLayout menu_series;
    private Context context = this;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesAfterLoginChannels;
    private SharedPreferences loginPreferencesAfterLoginEPG;
    private SharedPreferences loginPreferencesSharedPref;
    private SharedPreferences.Editor loginPrefsEditorChannels;
    private SharedPreferences.Editor loginPrefsEditorEPG;
    private LiveStreamDBHandler liveStreamDBHandler;
    String currentDate = "";
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel();
    private long mBackPressed;
    @BindView(R.id.main_layout)
    LinearLayout main_layout;
    private String userName = "";
    private String userPassword = "";

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
                    this.view.setBackgroundResource(R.drawable.live_tv_background);
                } else if (this.view.getTag().equals("2")) {
                    this.view.setBackgroundResource(R.drawable.on_demand_background);
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

    private SessionManager mSessionManager;
    @SuppressLint({"SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vod_menu_filmeslista_layout);
        ButterKnife.bind(this);
        changeStatusBarColor();
        hideSystemUi();
        makeButtonsClickable();
        this.menu_filmes.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_filmes));
        this.menu_series.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_series));
        this.menu_filmes.requestFocus();

        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);

        mSessionManager = new SessionManager(this.context);
    }

    protected void onResume() {
        hideSystemUi();
        super.onResume();
    }

    private void makeButtonsClickable() {
        this.menu_filmes.setOnClickListener(this);
        this.menu_series.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_filmes:
                if (checkChannelsAutomation()) {
                    launchImportChannels(AppConst.VOD);
                } else {
                    startActivity(new Intent(this, VodActivityNewFlow.class));
                }
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.menu_series:

                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            default:
                return;
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

    public void hideSystemUi() {
        this.main_layout.setSystemUiVisibility(4871);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        return;
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
        if (channelsCount == 0 || dateDifference >= 1) {
            startImportLiveActivity(type);
        } else if (type.equals("live")) {
            startActivity(new Intent(this, LiveActivityNewFlow.class));
        } else if (type.equals(AppConst.VOD)) {
            startActivity(new Intent(this, VodActivityNewFlow.class));
        } else if (type.equals("series")) {
            startActivity(new Intent(this, SeriesActivtyNewFlow.class));
        } else if (type.equals("catchup")) {
            startActivity(new Intent(this, TVArchiveActivityNewFlow.class));
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
        if (epgCount == 0) {
            startImportTvGuideActivity();
        } else if (dateDifference >= 0 && dateDifference < 2) {
            startTvGuideActivity();
        } else if (dateDifference >= 2) {
            startImportTvGuideActivity();
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
            epgIntent.setAction("redirect_epg_category"); //ImportEPGActivity.REDIRECT_EPG_CATEGORY
            startActivity(epgIntent);
            overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        }
    }

    public void startImportLiveActivity(String type) {
        if (this.liveStreamDBHandler != null) {
            this.liveStreamDBHandler.updateDBStatusAndDate(AppConst.DB_CHANNELS, "1", AppConst.DB_UPDATED_STATUS_PROCESSING, this.currentDate);
            Intent channelsIntent = new Intent(this, ImportStreamsActivity.class);
            if (type.equals("live")) {
                channelsIntent.setAction(ImportStreamsActivity.REDIRECT_LIVE_TV);
            } else if (type.equals(AppConst.VOD)) {
                channelsIntent.setAction(ImportStreamsActivity.REDIRECT_VOD);
            } else if (type.equals("catchup")) {
                channelsIntent.setAction(ImportStreamsActivity.REDIRECT_CATCHUP);
            } else {
                channelsIntent.setAction(ImportStreamsActivity.REDIRECT_SERIES);
            }
            startActivity(channelsIntent);
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