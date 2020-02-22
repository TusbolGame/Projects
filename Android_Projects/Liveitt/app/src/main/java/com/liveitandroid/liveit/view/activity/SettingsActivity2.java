package com.liveitandroid.liveit.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity2 extends AppCompatActivity implements OnClickListener {
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    int actionBarHeight;
    Button closedBT;
    public Context context;
    @BindView(R.id.date)
    TextView date;
    private SharedPreferences loginPreferencesAfterLogin;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.rl_general_settings_card)
    CardView rlGeneralSettingsCard;
    @BindView(R.id.rl_general_settings)
    LinearLayout rlGeneralSettings;
    @BindView(R.id.rl_player_card)
    CardView rlPlayerCard;
    @BindView(R.id.rl_player)
    LinearLayout rlPlayer;
    @BindView(R.id.rl_time_format_card)
    CardView rlTimeCard;
    @BindView(R.id.rl_time_format)
    LinearLayout rlTimeFormat;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String userName = "";
    private String userPassword = "";

    class C17091 implements Runnable {
        C17091() {
        }

        public void run() {
            try {
                String dateValue = Calendar.getInstance().getTime().toString();
                String currentCurrentTime = Utils.getTime(SettingsActivity2.this.context);
                String currentCurrentDate = Utils.getDate(dateValue);
                if (SettingsActivity2.this.time != null) {
                    SettingsActivity2.this.time.setText(currentCurrentTime);
                }
                if (SettingsActivity2.this.date != null) {
                    SettingsActivity2.this.date.setText(currentCurrentDate);
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
            Utils.logoutUser(SettingsActivity2.this.context);
        }
    }

    class C17124 implements DialogInterface.OnClickListener {
        C17124() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsAndVod(SettingsActivity2.this.context);
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
            Utils.loadTvGuid(SettingsActivity2.this.context);
        }
    }

    class C17157 implements DialogInterface.OnClickListener {
        C17157() {
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
                    SettingsActivity2.this.doWork();
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
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals("1"))) {
                    SettingsActivity2.this.rlPlayer.setBackgroundResource(R.drawable.shape_checkbox_focused);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals("2"))) {
                    SettingsActivity2.this.rlTimeFormat.setBackgroundResource(R.drawable.shape_checkbox_focused);
                }
                if (this.view != null && this.view.getTag() != null && this.view.getTag().equals("3")) {
                    SettingsActivity2.this.rlGeneralSettings.setBackgroundResource(R.drawable.shape_checkbox_focused);
                }
                if (this.view != null && this.view.getTag() != null && this.view.getTag().equals("4")) {
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
                    SettingsActivity2.this.rlPlayer.setBackgroundResource(R.drawable.ripple_white);
                }
                if (!(this.view == null || this.view.getTag() == null || !this.view.getTag().equals("2"))) {
                    SettingsActivity2.this.rlTimeFormat.setBackgroundResource(R.drawable.ripple_white);
                }
                if (this.view != null && this.view.getTag() != null && this.view.getTag().equals("3")) {
                    SettingsActivity2.this.rlGeneralSettings.setBackgroundResource(R.drawable.ripple_white);
                }
                if (this.view != null && this.view.getTag() != null && this.view.getTag().equals("4")) {
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
        setContentView(R.layout.activity_settings2);
        ButterKnife.bind(this);
        if (this.rlPlayerCard != null) {
            this.rlPlayerCard.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.rlPlayerCard));
        }
        this.rlPlayerCard.requestFocus();
        if (this.rlTimeCard != null) {
            this.rlTimeCard.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.rlTimeCard));
        }
        if (this.rlGeneralSettingsCard != null) {
            this.rlGeneralSettingsCard.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.rlGeneralSettingsCard));
        }
        mSessionManager = new SessionManager(this);
        /*getWindow().setFlags(1024, 1024);*/
        changeStatusBarColor();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initialize();

        new Thread(new CountDownRunner()).start();
    }

    public void doWork() {
        runOnUiThread(new C17091());
    }

    private void initialize() {
        this.context = this;
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
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

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
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

    @OnClick({R.id.rl_general_settings, R.id.rl_general_settings_card, R.id.rl_player, R.id.rl_player_card, R.id.rl_time_format, R.id.rl_time_format_card})
    public void onViewClicked(View view) {
        String app_name = getBaseContext().getApplicationInfo().loadLabel(getBaseContext().getPackageManager()).toString();
        switch (view.getId()) {
            case R.id.rl_player:
            case R.id.rl_player_card:
                if(mSessionManager.getUserType().equals("Mag")){
                    startActivity(new Intent(this, PlayerSelectionActivity2.class));
                }else{
                    startActivity(new Intent(this, PlayerSelectionActivity.class));
                }
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
}
