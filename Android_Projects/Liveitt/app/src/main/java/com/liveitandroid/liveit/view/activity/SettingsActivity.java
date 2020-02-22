package com.liveitandroid.liveit.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.database.DatabaseUpdatedStatusDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.database.PasswordDBModel;
import com.liveitandroid.liveit.presenter.XMLTVPresenter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class SettingsActivity extends AppCompatActivity implements OnClickListener {
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    int actionBarHeight;
    private PopupWindow changeSortPopUp;
    private TextView clientNameTv;
    Button closedBT;
    public Context context;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel();
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel();
    @BindView(R.id.date)
    TextView date;
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private Editor loginPrefsEditor;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.rl_automation)
    LinearLayout rlAutomation;
    @BindView(R.id.rl_general_settings_card)
    CardView rlGeneralSettingsCard;
    @BindView(R.id.rl_general_settings)
    LinearLayout rlGeneralSettings;
    @BindView(R.id.rl_automation_card)
    CardView rlAutomationCard;
    @BindView(R.id.rl_epg_shift)
    LinearLayout rlEPGShift;
    @BindView(R.id.rl_epg_shift_card)
    CardView rlEPGShiftCard;
    @BindView(R.id.rl_epg_channel_update_card)
    CardView rlLayoutEPGCard;
    @BindView(R.id.rl_parental)
    LinearLayout rlParental;
    @BindView(R.id.rl_parental_card)
    CardView rlParentalCard;
    @BindView(R.id.rl_stream_format_card)
    CardView rlStreamCard;
    @BindView(R.id.rl_player_card)
    CardView rlPlayerCard;
    @BindView(R.id.rl_player)
    LinearLayout rlPlayer;
    @BindView(R.id.rl_epg_channel_update)
    LinearLayout rlRlEpgChannelUpdate;
    @BindView(R.id.rl_stream_format)
    LinearLayout rlStreamFormat;
    @BindView(R.id.rl_time_format_card)
    CardView rlTimeCard;
    @BindView(R.id.rl_time_format)
    LinearLayout rlTimeFormat;
    Button savePasswordBT;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private TypedValue tv;
    private String userName = "";
    private String userPassword = "";
    private XMLTVPresenter xmltvPresenter;

    class C17091 implements Runnable {
        C17091() {
        }

        public void run() {
            try {
                String dateValue = Calendar.getInstance().getTime().toString();
                String currentCurrentTime = Utils.getTime(SettingsActivity.this.context);
                String currentCurrentDate = Utils.getDate(dateValue);
                if (SettingsActivity.this.time != null) {
                    SettingsActivity.this.time.setText(currentCurrentTime);
                }
                if (SettingsActivity.this.date != null) {
                    SettingsActivity.this.date.setText(currentCurrentDate);
                }
            } catch (Exception e) {
            }
        }
    }

    class C17102 implements DialogInterface.OnClickListener {
        C17102() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C17113 implements DialogInterface.OnClickListener {
        C17113() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.logoutUser(SettingsActivity.this.context);
        }
    }

    class C17124 implements DialogInterface.OnClickListener {
        C17124() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsAndVod(SettingsActivity.this.context);
        }
    }

    class C17135 implements DialogInterface.OnClickListener {
        C17135() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C17146 implements DialogInterface.OnClickListener {
        C17146() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadTvGuid(SettingsActivity.this.context);
        }
    }

    class C17157 implements DialogInterface.OnClickListener {
        C17157() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C17168 implements OnClickListener {
        C17168() {
        }

        public void onClick(View view) {
            SettingsActivity.this.changeSortPopUp.dismiss();
        }
    }

    class CountDownRunner implements Runnable {
        CountDownRunner() {
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    SettingsActivity.this.doWork();
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
                    to = 1.12f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                Log.e("id is", "" + this.view.getTag());
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals("1"))) {
                    SettingsActivity.this.rlPlayer.setBackgroundResource(R.drawable.shape_checkbox_focused);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals(AppConst.DB_EPG_ID))) {
                    SettingsActivity.this.rlParental.setBackgroundResource(R.drawable.shape_checkbox_focused);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals("3"))) {
                    SettingsActivity.this.rlEPGShift.setBackgroundResource(R.drawable.shape_checkbox_focused);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals("5"))) {
                    SettingsActivity.this.rlStreamFormat.setBackgroundResource(R.drawable.shape_checkbox_focused);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals("6"))) {
                    SettingsActivity.this.rlTimeFormat.setBackgroundResource(R.drawable.shape_checkbox_focused);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals(AppConst.DB_SERIES_STREAMS_CAT_ID))) {
                    SettingsActivity.this.rlRlEpgChannelUpdate.setBackgroundResource(R.drawable.shape_checkbox_focused);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals(AppConst.DB_SERIES_STREAMS_ID))) {
                    SettingsActivity.this.savePasswordBT.setBackgroundResource(R.drawable.back_btn_effect);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals("9"))) {
                    SettingsActivity.this.closedBT.setBackgroundResource(R.drawable.logout_btn_effect);
                }
                if (this.view != null && this.view.getTag() != null && this.view.getTag().equals("10")) {
                    SettingsActivity.this.rlAutomation.setBackgroundResource(R.drawable.shape_checkbox_focused);
                }
                if (this.view != null && this.view.getTag() != null && this.view.getTag().equals("11")) {
                    SettingsActivity.this.rlGeneralSettings.setBackgroundResource(R.drawable.shape_checkbox_focused);
                }
                if (this.view != null && this.view.getTag() != null && this.view.getTag().equals("12")) {
                    return;
                }
            } else if (!hasFocus) {
                if (hasFocus) {
                    to = 1.09f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                performAlphaAnimation(hasFocus);
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals("1"))) {
                    SettingsActivity.this.rlPlayer.setBackgroundResource(R.drawable.ripple_white);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals(AppConst.DB_EPG_ID))) {
                    SettingsActivity.this.rlParental.setBackgroundResource(R.drawable.ripple_white);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals("3"))) {
                    SettingsActivity.this.rlEPGShift.setBackgroundResource(R.drawable.ripple_white);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals("5"))) {
                    SettingsActivity.this.rlStreamFormat.setBackgroundResource(R.drawable.ripple_white);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals("6"))) {
                    SettingsActivity.this.rlTimeFormat.setBackgroundResource(R.drawable.ripple_white);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals(AppConst.DB_SERIES_STREAMS_CAT_ID))) {
                    SettingsActivity.this.rlRlEpgChannelUpdate.setBackgroundResource(R.drawable.ripple_white);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals(AppConst.DB_SERIES_STREAMS_ID))) {
                    SettingsActivity.this.savePasswordBT.setBackgroundResource(R.drawable.black_button_dark);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals("9"))) {
                    SettingsActivity.this.closedBT.setBackgroundResource(R.drawable.black_button_dark);
                }
                if (this.view != null && this.view.getTag() != null && this.view.getTag().equals("10")) {
                    SettingsActivity.this.rlAutomation.setBackgroundResource(R.drawable.ripple_white);
                }
                if (this.view != null && this.view.getTag() != null && this.view.getTag().equals("11")) {
                    SettingsActivity.this.rlGeneralSettings.setBackgroundResource(R.drawable.ripple_white);
                }
                if (this.view != null && this.view.getTag() != null && this.view.getTag().equals("12")) {
                    return;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        if (this.rlPlayerCard != null) {
            this.rlPlayerCard.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.rlPlayerCard));
        }
        this.rlPlayerCard.requestFocus();
        if (this.rlParentalCard != null) {
            this.rlParentalCard.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.rlParentalCard));
        }
        if (this.rlEPGShiftCard != null) {
            this.rlEPGShiftCard.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.rlEPGShiftCard));
        }
        if (this.rlStreamCard != null) {
            this.rlStreamCard.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.rlStreamCard));
        }
        if (this.rlTimeCard != null) {
            this.rlTimeCard.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.rlTimeCard));
        }
        if (this.rlLayoutEPGCard != null) {
            this.rlLayoutEPGCard.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.rlLayoutEPGCard));
        }
        if (this.rlAutomationCard != null) {
            this.rlAutomationCard.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.rlAutomationCard));
        }
        if (this.rlGeneralSettingsCard != null) {
            this.rlGeneralSettingsCard.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.rlGeneralSettingsCard));
            if ((getResources().getConfiguration().screenLayout & 15) == 3) {
                this.rlGeneralSettingsCard.requestFocus();
                this.rlGeneralSettingsCard.setFocusableInTouchMode(true);
            }
        }
        getWindow().setFlags(1024, 1024);
        changeStatusBarColor();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initialize();
        mSessionManager = new SessionManager(this.context);
        new Thread(new CountDownRunner()).start();
    }

    public void doWork() {
        runOnUiThread(new C17091());
    }

    private void initialize() {
        this.context = this;
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
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

    @SuppressLint({"InlinedApi"})
    private void changeStatusBarColor() {
        Window window = getWindow();
        window.clearFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        if (VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    public void startTvGuideActivity() {
        startActivity(new Intent(this, NewEPGActivity.class));
        finish();
    }

    public void startImportTvGuideActivity() {
        startActivity(new Intent(this, ImportEPGActivity.class));
        finish();
    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
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

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.toolbar.inflateMenu(R.menu.menu_text_icon);
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(16843499, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        for (int i = 0; i < this.toolbar.getChildCount(); i++) {
            if (this.toolbar.getChildAt(i) instanceof ActionMenuView) {
                ((LayoutParams) this.toolbar.getChildAt(i).getLayoutParams()).gravity = 16;
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if(mSessionManager.getFilmesAPP().equals("4") || mSessionManager.getFilmesAPP().equals(4)){
                startActivity(new Intent(this.context, NewDashboardActivity.class));
            }else{
                startActivity(new Intent(this.context, NewDashboardActivity2.class));
            }
            finish();
        }
        if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        }
        if (id == R.id.action_logout && this.context != null) {
            new Builder(this.context, R.style.AlertDialogCustom).setTitle(getResources().getString(R.string.logout_title)).setMessage(getResources().getString(R.string.logout_message)).setPositiveButton(17039379, new C17113()).setNegativeButton(17039369, new C17102()).show();
        }
        if (id == R.id.menu_load_channels_vod) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle("Confirmação para Atualização...");
            alertDialog.setMessage("Confirma o Procedimento?");
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("Sim", new C17124());
            alertDialog.setNegativeButton("Não", new C17135());
            alertDialog.show();
        }
        if (id == R.id.menu_load_tv_guide) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle("Confirmação para Atualização...");
            alertDialog.setMessage("Confirma o Procedimento?");
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("Sim", new C17146());
            alertDialog.setNegativeButton("Não", new C17157());
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean getChannelVODUpdateStatus() {
        if (this.liveStreamDBHandler == null || this.databaseUpdatedStatusDBModelLive == null) {
            return false;
        }
        this.databaseUpdatedStatusDBModelLive = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_CHANNELS, "1");
        if (this.databaseUpdatedStatusDBModelLive == null) {
            return false;
        }
        if (this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState() == null) {
            return true;
        }
        if (this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH) || this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FAILED)) {
            return true;
        }
        return false;
    }

    private boolean getEPGUpdateStatus() {
        if (this.liveStreamDBHandler == null || this.databaseUpdatedStatusDBModelEPG == null) {
            return false;
        }
        this.databaseUpdatedStatusDBModelEPG = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID);
        if (this.databaseUpdatedStatusDBModelEPG == null) {
            return false;
        }
        if (this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState() == null || this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH) || this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FAILED)) {
            return true;
        }
        if (this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState() == null || this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals("")) {
            return true;
        }
        return false;
    }

    public void onResume() {
        super.onResume();
        new Thread(new CountDownRunner()).start();
        getWindow().setFlags(1024, 1024);
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        if (this.loginPreferencesAfterLogin.getString("username", "").equals("") && this.loginPreferencesAfterLogin.getString("password", "").equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (this.context != null) {
        }
        loginPreferencesSharedPref_time_format = getSharedPreferences(AppConst.LOGIN_PREF_TIME_FORMAT, 0);
        String timeFormat = loginPreferencesSharedPref_time_format.getString(AppConst.LOGIN_PREF_TIME_FORMAT, "");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_header_title:
                startActivity(new Intent(this, NewDashboardActivity.class));
                return;
            default:
                return;
        }
    }

    private void logoutUser() {
        Toast.makeText(this, getResources().getString(R.string.logged_out), 0).show();
        Intent intentLogout = new Intent(this, LoginActivity.class);
        Editor loginPreferencesEditor = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0).edit();
        loginPreferencesEditor.clear();
        loginPreferencesEditor.commit();
        startActivity(intentLogout);
    }

    @OnClick({R.id.rl_general_settings, R.id.rl_general_settings_card, R.id.rl_player, R.id.rl_player_card, R.id.rl_parental, R.id.rl_parental_card, R.id.rl_epg_shift, R.id.rl_epg_shift_card, R.id.rl_stream_format, R.id.rl_stream_format_card, R.id.rl_time_format, R.id.rl_time_format_card, R.id.rl_epg_channel_update, R.id.rl_epg_channel_update_card, R.id.rl_automation, R.id.rl_automation_card})
    public void onViewClicked(View view) {
        String app_name = getBaseContext().getApplicationInfo().loadLabel(getBaseContext().getPackageManager()).toString();
        switch (view.getId()) {
            case R.id.rl_automation:
            case R.id.rl_automation_card:
                startActivity(new Intent(this, AutomationActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.rl_epg_channel_update:
            case R.id.rl_epg_channel_update_card:
                startActivity(new Intent(this, EPGChannelUpdateActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.rl_epg_shift:
            case R.id.rl_epg_shift_card:
                startActivity(new Intent(this, EPGTimeShiftActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.rl_parental:
            case R.id.rl_parental_card:
                String username = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0).getString("username", "");
                this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
                ArrayList<PasswordDBModel> list = this.liveStreamDBHandler.getAllPassword();
                String usernameDB = "";
                String userPasswordDB = "";
                if (list != null) {
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        PasswordDBModel listItem = (PasswordDBModel) it.next();
                        if (listItem.getUserDetail().equals(username) && !listItem.getUserPassword().isEmpty()) {
                            usernameDB = listItem.getUserDetail();
                            userPasswordDB = listItem.getUserPassword();
                        }
                    }
                }
                if (usernameDB != null && !usernameDB.equals("") && !usernameDB.isEmpty()) {
                    passwordConfirmationPopUp(this, 100, username, usernameDB, userPasswordDB);
                    return;
                } else if (username != null && !username.isEmpty() && !username.equals("")) {
                    showSortPopup(this, 100, username);
                    return;
                } else {
                    return;
                }
            case R.id.rl_player:
            case R.id.rl_player_card:
                if(mSessionManager.getUserType().equals("Mag")){
                    startActivity(new Intent(this, PlayerSelectionActivity2.class));
                }else{
                    startActivity(new Intent(this, PlayerSelectionActivity.class));
                }
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.rl_stream_format:
            case R.id.rl_stream_format_card:
                startActivity(new Intent(this, StreamFormatActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.rl_time_format:
            case R.id.rl_time_format_card:
                startActivity(new Intent(this, TimeFormatActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.rl_general_settings:
            case R.id.rl_general_settings_card:
                startActivity(new Intent(this, GeneralSettingsActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            default:
                return;
        }
    }

    private void passwordConfirmationPopUp(SettingsActivity context, int i, String username, String usernameDB, String passwordDB) {
        View layout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_password_verification, (RelativeLayout) context.findViewById(R.id.rl_password_verification));
        this.changeSortPopUp = new PopupWindow(context);
        this.changeSortPopUp.setContentView(layout);
        this.changeSortPopUp.setWidth(-1);
        this.changeSortPopUp.setHeight(-1);
        this.changeSortPopUp.setFocusable(true);
        this.changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());
        this.changeSortPopUp.showAtLocation(layout, 17, 0, 0);
        this.savePasswordBT = (Button) layout.findViewById(R.id.bt_save_password);
        this.closedBT = (Button) layout.findViewById(R.id.bt_close);
        if (this.savePasswordBT != null) {
            this.savePasswordBT.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.savePasswordBT));
        }
        if (this.closedBT != null) {
            this.closedBT.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.closedBT));
        }
        final EditText passwordET = (EditText) layout.findViewById(R.id.et_password);
        final String[] passowrd = new String[1];
        passwordET.requestFocus();
        this.closedBT.setOnClickListener(new C17168());
        final String str = passwordDB;
        final SettingsActivity settingsActivity = context;
        this.savePasswordBT.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (fieldsCheck()) {
                    passwordValidation(comparePassword(str));
                }
            }

            private void passwordValidation(boolean comPassword) {
                if (comPassword) {
                    SettingsActivity.this.changeSortPopUp.dismiss();
                    startActity();
                    return;
                }
                if (settingsActivity != null) {
                    Toast.makeText(settingsActivity, SettingsActivity.this.getResources().getString(R.string.parental_invalid_password), 1).show();
                }
                passwordET.getText().clear();
            }

            private boolean fieldsCheck() {
                passowrd[0] = String.valueOf(passwordET.getText());
                if (passowrd != null && passowrd[0].equals("")) {
                    Toast.makeText(settingsActivity, SettingsActivity.this.getResources().getString(R.string.enter_password_error), 1).show();
                    return false;
                } else if (passowrd == null || passowrd[0].equals("")) {
                    return false;
                } else {
                    return true;
                }
            }

            private boolean comparePassword(String passwordDB) {
                passowrd[0] = String.valueOf(passwordET.getText());
                if (passowrd[0] == null || passowrd[0].equals("") || passowrd[0].isEmpty() || passwordDB == null || passwordDB.isEmpty() || passwordDB.equals("") || !passowrd[0].equals(passwordDB)) {
                    return false;
                }
                return true;
            }

            private void startActity() {
                SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, ParentalControlActivitity.class));
            }
        });
    }

    private void showSortPopup(Activity context, int p, String username) {
        View layout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_password_prompt, (RelativeLayout) context.findViewById(R.id.rl_password_prompt));
        this.changeSortPopUp = new PopupWindow(context);
        this.changeSortPopUp.setContentView(layout);
        this.changeSortPopUp.setWidth(-1);
        this.changeSortPopUp.setHeight(-1);
        this.changeSortPopUp.setFocusable(true);
        this.changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());
        this.changeSortPopUp.showAtLocation(layout, 17, 0, 0);
        this.savePasswordBT = (Button) layout.findViewById(R.id.bt_save_password);
        this.closedBT = (Button) layout.findViewById(R.id.bt_close);
        if (this.savePasswordBT != null) {
            this.savePasswordBT.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.savePasswordBT));
        }
        if (this.closedBT != null) {
            this.closedBT.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.closedBT));
        }
        final EditText passwordET = (EditText) layout.findViewById(R.id.tv_password);
        final EditText confirmPasswordET = (EditText) layout.findViewById(R.id.tv_confirm_password);
        final String[] passowrd = new String[1];
        final String[] confirmPassword = new String[1];
        this.closedBT.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SettingsActivity.this.changeSortPopUp.dismiss();
            }
        });
        final Activity activity = context;
        final String str = username;
        this.savePasswordBT.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (fieldsCheck()) {
                    setPassword(comparePassword());
                }
            }

            private boolean fieldsCheck() {
                passowrd[0] = String.valueOf(passwordET.getText());
                confirmPassword[0] = String.valueOf(confirmPasswordET.getText());
                if (passowrd != null && passowrd[0].equals("")) {
                    Toast.makeText(activity, SettingsActivity.this.getResources().getString(R.string.enter_password_error), 1).show();
                    return false;
                } else if (passowrd != null && !passowrd[0].equals("") && confirmPassword != null && confirmPassword[0].equals("")) {
                    Toast.makeText(activity, SettingsActivity.this.getResources().getString(R.string.parental_confirm_password), 1).show();
                    return false;
                } else if (passowrd == null || confirmPassword == null || passowrd[0].equals("") || confirmPassword[0].equals("")) {
                    return false;
                } else {
                    return true;
                }
            }

            private void setPassword(boolean comPassword) {
                if (comPassword) {
                    PasswordDBModel passwordDBModel = new PasswordDBModel();
                    passwordDBModel.setUserPassword(String.valueOf(passowrd[0]));
                    passwordDBModel.setUserDetail(str);
                    if (SettingsActivity.this.liveStreamDBHandler != null) {
                        SettingsActivity.this.liveStreamDBHandler.addPassword(passwordDBModel);
                        SettingsActivity.this.changeSortPopUp.dismiss();
                        startActity();
                        return;
                    }
                    return;
                }
                if (activity != null) {
                    Toast.makeText(activity, SettingsActivity.this.getResources().getString(R.string.parental_password_confirm_password_match_error), 1).show();
                }
                passwordET.getText().clear();
                confirmPasswordET.getText().clear();
            }

            private boolean comparePassword() {
                passowrd[0] = String.valueOf(passwordET.getText());
                confirmPassword[0] = String.valueOf(confirmPasswordET.getText());
                if (passowrd == null || passowrd[0].equals("") || confirmPassword == null || confirmPassword[0].equals("") || !passowrd[0].equals(confirmPassword[0])) {
                    return false;
                }
                return true;
            }

            private void startActity() {
                SettingsActivity.this.startActivity(new Intent(activity, ParentalControlActivitity.class));
            }
        });
    }
}
