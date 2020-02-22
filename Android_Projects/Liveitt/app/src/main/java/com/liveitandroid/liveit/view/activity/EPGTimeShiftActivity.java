package com.liveitandroid.liveit.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.liveitandroid.liveit.presenter.XMLTVPresenter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class EPGTimeShiftActivity extends AppCompatActivity implements OnClickListener {
    private int actionBarHeight;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    @BindView(R.id.bt_save_changes)
    Button btSaveChanges;
    @BindView(R.id.btn_back_playerselection)
    Button btnBackPlayerselection;
    private TextView clientNameTv;
    @BindView(R.id.content_drawer)
    RelativeLayout contentDrawer;
    private Context context;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel();
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel();
    @BindView(R.id.date)
    TextView date;
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private Editor loginPrefsEditor;
    @BindView(R.id.spinner_epg)
    Spinner spinnerEPG;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private TypedValue tv;
    private String userName = "";
    private String userPassword = "";
    private XMLTVPresenter xmltvPresenter;

    class C15861 implements Runnable {
        C15861() {
        }

        public void run() {
            try {
                String dateValue = Calendar.getInstance().getTime().toString();
                String currentCurrentTime = Utils.getTime(EPGTimeShiftActivity.this.context);
                String currentCurrentDate = Utils.getDate(dateValue);
                if (EPGTimeShiftActivity.this.time != null) {
                    EPGTimeShiftActivity.this.time.setText(currentCurrentTime);
                }
                if (EPGTimeShiftActivity.this.date != null) {
                    EPGTimeShiftActivity.this.date.setText(currentCurrentDate);
                }
            } catch (Exception e) {
            }
        }
    }

    class C15872 implements DialogInterface.OnClickListener {
        C15872() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C15883 implements DialogInterface.OnClickListener {
        C15883() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.logoutUser(EPGTimeShiftActivity.this.context);
        }
    }

    class C15894 implements DialogInterface.OnClickListener {
        C15894() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsAndVod(EPGTimeShiftActivity.this.context);
        }
    }

    class C15905 implements DialogInterface.OnClickListener {
        C15905() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C15916 implements DialogInterface.OnClickListener {
        C15916() {
        }

        public void onClick(DialogInterface dialog, int which) {
            EPGTimeShiftActivity.this.loadTvGuid();
        }
    }

    class C15927 implements DialogInterface.OnClickListener {
        C15927() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class CountDownRunner implements Runnable {
        CountDownRunner() {
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    EPGTimeShiftActivity.this.doWork();
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
                    performScaleXAnimation(1.05f);
                    performScaleYAnimation(1.05f);
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
        setContentView(R.layout.activity_epgtime_shift);
        ButterKnife.bind(this);
        focusInitialize();
        changeStatusBarColor();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getWindow().setFlags(1024, 1024);
        initialize();
        mSessionManager = new SessionManager(this.context);
        new Thread(new CountDownRunner()).start();
    }

    public void doWork() {
        runOnUiThread(new C15861());
    }

    private void focusInitialize() {
        if (this.btSaveChanges != null) {
            this.btSaveChanges.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.btSaveChanges));
        }
        if (this.btnBackPlayerselection != null) {
            this.btnBackPlayerselection.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.btnBackPlayerselection));
        }
        if (this.spinnerEPG != null) {
            this.spinnerEPG.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.spinnerEPG));
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
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.spinnerEPG.setSelection(Utils.getPositionOfEPG(this.loginPreferencesAfterLogin.getString(AppConst.LOGIN_PREF_SELECTED_EPG_SHIFT, "")));
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

    public void startTvGuideActivity() {
        startActivity(new Intent(this, NewEPGActivity.class));
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        finish();
    }

    public void startImportTvGuideActivity() {
        startActivity(new Intent(this, ImportEPGActivity.class));
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
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
    private SessionManager mSessionManager;
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if(mSessionManager.getFilmesAPP().equals("4") || mSessionManager.getFilmesAPP().equals(4)){
                startActivity(new Intent(this, NewDashboardActivity.class));
            }else{
                startActivity(new Intent(this, NewDashboardActivity2.class));
            }
            finish();
        }
        if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        }
        if (id == R.id.action_logout && this.context != null) {
            new Builder(this.context, R.style.AlertDialogCustom).setTitle(getResources().getString(R.string.logout_title)).setMessage(getResources().getString(R.string.logout_message)).setPositiveButton(17039379, new C15883()).setNegativeButton(17039369, new C15872()).show();
        }
        if (id == R.id.menu_load_channels_vod) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle("Confirmação para Atualização...");
            alertDialog.setMessage("Confirma o Procedimento?");
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("Sim", new C15894());
            alertDialog.setNegativeButton("Não", new C15905());
            alertDialog.show();
        }
        if (id == R.id.menu_load_tv_guide) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle("Confirmação para Atualização...");
            alertDialog.setMessage("Confirma o Procedimento?");
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("Sim", new C15916());
            alertDialog.setNegativeButton("Não", new C15927());
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

    private void loadTvGuid() {
        if (this.context == null) {
            return;
        }
        if (getEPGUpdateStatus()) {
            SharedPreferences loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            Editor loginPrefsEditor = loginPreferencesAfterLogin.edit();
            if (loginPrefsEditor != null) {
                loginPrefsEditor.putString(AppConst.SKIP_BUTTON_PREF, "autoLoad");
                loginPrefsEditor.commit();
                String skipButton = loginPreferencesAfterLogin.getString(AppConst.SKIP_BUTTON_PREF, "");
                new LiveStreamDBHandler(this.context).makeEmptyEPG();
                startActivity(new Intent(this.context, ImportEPGActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            }
        } else if (this.context != null) {
            Utils.showToast(this.context, getResources().getString(R.string.upadating_tv_guide));
        }
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
        getWindow().setFlags(1024, 1024);
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        if (this.loginPreferencesAfterLogin.getString("username", "").equals("") && this.loginPreferencesAfterLogin.getString("password", "").equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        } else if (this.context == null) {
        }
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
                this.loginPrefsEditor = this.loginPreferencesAfterLogin.edit();
                if (this.loginPrefsEditor != null) {
                    this.spinnerEPG.getSelectedItemPosition();
                    this.loginPrefsEditor.putString(AppConst.LOGIN_PREF_SELECTED_EPG_SHIFT, String.valueOf(this.spinnerEPG.getSelectedItem()));
                    this.loginPrefsEditor.commit();

                    mSessionManager.setcheckedEPG(Integer.parseInt(String.valueOf(this.spinnerEPG.getSelectedItem())));
                    Toast.makeText(this, getResources().getString(R.string.player_setting_save), 0).show();
                    return;
                }
                Toast.makeText(this, getResources().getString(R.string.player_setting_error), 0).show();
                return;
            case R.id.btn_back_playerselection:
                finish();
                return;
            default:
                return;
        }
    }
}