package com.liveitandroid.liveit.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.database.DatabaseUpdatedStatusDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.presenter.XMLTVPresenter;
import java.util.Calendar;

public class AutomationActivity extends AppCompatActivity implements OnClickListener {
    private int actionBarHeight;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    @BindView(R.id.bt_save_changes)
    Button btSaveChanges;
    @BindView(R.id.btn_back_playerselection)
    Button btnBackPlayerselection;
    @BindView(R.id.cb_automation_epg)
    CheckBox cbAutomationEPG;
    @BindView(R.id.cb_automation_live_vod)
    CheckBox cbAutomationLiveVod;
    private TextView clientNameTv;
    @BindView(R.id.content_drawer)
    RelativeLayout contentDrawer;
    private Context context;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel();
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel();
    @BindView(R.id.date)
    TextView date;
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLoginChannels;
    private SharedPreferences loginPreferencesAfterLoginEPG;
    private Editor loginPrefsEditorChannels;
    private Editor loginPrefsEditorEPG;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private TypedValue tv;
    private String userName = "";
    private String userPassword = "";
    private XMLTVPresenter xmltvPresenter;

    class C15621 implements Runnable {
        C15621() {
        }

        public void run() {
            try {
                String dateValue = Calendar.getInstance().getTime().toString();
                String currentCurrentTime = Utils.getTime(AutomationActivity.this.context);
                String currentCurrentDate = Utils.getDate(dateValue);
                if (AutomationActivity.this.time != null) {
                    AutomationActivity.this.time.setText(currentCurrentTime);
                }
                if (AutomationActivity.this.date != null) {
                    AutomationActivity.this.date.setText(currentCurrentDate);
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
                    AutomationActivity.this.doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e2) {
                }
            }
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
                    to = 1.05f;
                }
                Log.e("id is", "" + this.view.getTag());
                if (this.view.getTag().equals("1")) {
                    performScaleXAnimation(to);
                    performScaleYAnimation(to);
                    this.view.setBackgroundResource(R.drawable.back_btn_effect);
                } else if (this.view.getTag().equals(AppConst.DB_EPG_ID)) {
                    performScaleXAnimation(to);
                    performScaleYAnimation(to);
                    this.view.setBackgroundResource(R.drawable.logout_btn_effect);
                } else {
                    performScaleXAnimation(1.12f);
                    performScaleYAnimation(1.12f);
                }
            } else if (!hasFocus) {
                if (hasFocus) {
                    to = 1.09f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                if (this.view.getTag().equals("1")) {
                    this.view.setBackgroundResource(R.drawable.black_button_dark);
                } else if (this.view.getTag().equals(AppConst.DB_EPG_ID)) {
                    this.view.setBackgroundResource(R.drawable.black_button_dark);
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automation);
        ButterKnife.bind(this);
        focusInitialize();
        changeStatusBarColor();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getWindow().setFlags(1024, 1024);
        initialize();
        new Thread(new CountDownRunner()).start();
    }

    public void doWork() {
        runOnUiThread(new C15621());
    }

    private void focusInitialize() {
        if (this.btSaveChanges != null) {
            this.btSaveChanges.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.btSaveChanges));
        }
        if (this.btnBackPlayerselection != null) {
            this.btnBackPlayerselection.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.btnBackPlayerselection));
        }
        if (this.cbAutomationLiveVod != null) {
            this.cbAutomationLiveVod.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.cbAutomationLiveVod));
        }
        if (this.cbAutomationEPG != null) {
            this.cbAutomationEPG.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.cbAutomationEPG));
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

    private void initialize() {
        this.context = this;
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        this.loginPreferencesAfterLoginChannels = getSharedPreferences(AppConst.LOGIN_PREF_AUTOMATION_CHANNELS, 0);
        this.loginPreferencesAfterLoginEPG = getSharedPreferences(AppConst.LOGIN_PREF_AUTOMATION_EPG, 0);
        String automationChannels = this.loginPreferencesAfterLoginChannels.getString(AppConst.LOGIN_PREF_AUTOMATION_CHANNELS, "");
        String automationEPG = this.loginPreferencesAfterLoginEPG.getString(AppConst.LOGIN_PREF_AUTOMATION_EPG, "");
        if (automationChannels.equals("checked")) {
            this.cbAutomationLiveVod.setChecked(true);
        }
        if (automationEPG.equals("checked")) {
            this.cbAutomationEPG.setChecked(true);
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
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    public void onResume() {
        super.onResume();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_header_title:
                startActivity(new Intent(this, NewDashboardActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            default:
                return;
        }
    }

    @OnClick({R.id.bt_save_changes, R.id.btn_back_playerselection})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_save_changes:
                this.loginPreferencesAfterLoginChannels = getSharedPreferences(AppConst.LOGIN_PREF_AUTOMATION_CHANNELS, 0);
                this.loginPreferencesAfterLoginEPG = getSharedPreferences(AppConst.LOGIN_PREF_AUTOMATION_EPG, 0);
                this.loginPrefsEditorChannels = this.loginPreferencesAfterLoginChannels.edit();
                this.loginPrefsEditorEPG = this.loginPreferencesAfterLoginEPG.edit();
                if (this.cbAutomationLiveVod.isChecked()) {
                    if (this.loginPrefsEditorChannels != null) {
                        this.loginPrefsEditorChannels.putString(AppConst.LOGIN_PREF_AUTOMATION_CHANNELS, "checked");
                    }
                } else if (this.loginPrefsEditorChannels != null) {
                    this.loginPrefsEditorChannels.putString(AppConst.LOGIN_PREF_AUTOMATION_CHANNELS, "");
                }
                if (this.cbAutomationEPG.isChecked()) {
                    if (this.loginPrefsEditorEPG != null) {
                        this.loginPrefsEditorEPG.putString(AppConst.LOGIN_PREF_AUTOMATION_EPG, "checked");
                    }
                } else if (this.loginPrefsEditorEPG != null) {
                    this.loginPrefsEditorEPG.putString(AppConst.LOGIN_PREF_AUTOMATION_EPG, "");
                }
                this.loginPrefsEditorChannels.apply();
                this.loginPrefsEditorEPG.apply();
                Toast.makeText(this, getResources().getString(R.string.player_setting_save), 0).show();
                return;
            case R.id.btn_back_playerselection:
                finish();
                return;
            default:
                return;
        }
    }
}