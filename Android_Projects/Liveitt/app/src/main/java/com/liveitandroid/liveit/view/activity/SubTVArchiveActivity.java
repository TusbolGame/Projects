package com.liveitandroid.liveit.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.liveitandroid.liveit.view.interfaces.LiveStreamsEpgInterface;
import com.liveitandroid.liveit.view.interfaces.LoginInterface;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.callback.LiveStreamsEpgCallback;
import com.liveitandroid.liveit.model.callback.LoginCallback;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.pojo.XMLTVProgrammePojo;
import com.liveitandroid.liveit.presenter.LoginPresenter;
import com.liveitandroid.liveit.view.adapter.SubTVArchiveCategoriesAdapter;
import com.liveitandroid.liveit.view.interfaces.LiveStreamsEpgInterface;
import com.liveitandroid.liveit.view.interfaces.LoginInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SubTVArchiveActivity extends AppCompatActivity implements LoginInterface, LiveStreamsEpgInterface, OnNavigationItemSelectedListener, OnClickListener {
    int actionBarHeight;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    TextView clientNameTv;
    private Context context;
    LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesSharedPref;
    private LoginPresenter loginPresenter;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    @BindView(R.id.rl_tv_archive_title)
    RelativeLayout rlTvArchiveTitle;
    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TypedValue tv;
    @BindView(R.id.tv_egp_required)
    TextView tvEpgRequired;
    @BindView(R.id.tv_no_record_found)
    TextView tvNoRecordFound;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.time)
    TextView time;

    class C17501 implements Runnable {
        C17501() {
        }

        public void run() {
            try {
                String dateValue = Calendar.getInstance().getTime().toString();
                String currentCurrentTime = Utils.getTime(SubTVArchiveActivity.this.context);
                String currentCurrentDate = Utils.getDate(dateValue);
                if (SubTVArchiveActivity.this.time != null) {
                    SubTVArchiveActivity.this.time.setText(currentCurrentTime);
                }
                if (SubTVArchiveActivity.this.date != null) {
                    SubTVArchiveActivity.this.date.setText(currentCurrentDate);
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
                    SubTVArchiveActivity.this.doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e2) {
                }
            }
        }
    }

    public void doWork() {
        runOnUiThread(new C17501());
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_archive);
        ButterKnife.bind(this);
        changeStatusBarColor();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToggleIconPosition();
        initialize();
        new Thread(new CountDownRunner()).start();
    }


    private void initialize() {
        this.context = this;
        this.loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        String username = this.loginPreferencesSharedPref.getString("username", "");
        String password = this.loginPreferencesSharedPref.getString("password", "");
        String opened_stream_id = getIntent().getStringExtra("OPENED_STREAM_ID");
        String opened_num = getIntent().getStringExtra("OPENED_NUM");
        String opened_channel_id = getIntent().getStringExtra("OPENED_CHANNEL_ID");
        String opened_channel_name = getIntent().getStringExtra("OPENED_NAME");
        String opened_channel_icon = getIntent().getStringExtra("OPENED_STREAM_ICON");
        String opened_channel_duration = getIntent().getStringExtra("OPENED_ARCHIVE_DURATION");
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        if (opened_channel_id != null && !opened_channel_id.equals("")) {
            getEPG(opened_channel_id, opened_stream_id, opened_num, opened_channel_name, opened_channel_icon, opened_channel_duration);
        }
    }

    public void getEPG(String opened_channel_id, String opened_stream_id, String opened_num, String opened_channel_name, String opened_channel_icon, String opened_channel_duration) {
        if (this.liveStreamDBHandler != null) {
            ArrayList<XMLTVProgrammePojo> xmltvProgrammePojos = this.liveStreamDBHandler.getEPG(opened_channel_id);
            if (xmltvProgrammePojos != null) {
                int EpgSize = xmltvProgrammePojos.size();
                if (EpgSize != 0) {
                    String currentFormatDateAfter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(new Date());
                    List<String> datesTabs = new ArrayList();
                    int datesTabsIndex = 0;
                    int currentDateIndex = 0;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    for (int k = 0; k < EpgSize; k++) {
                        String currentFormatDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(Long.valueOf(Utils.epgTimeConverter(((XMLTVProgrammePojo) xmltvProgrammePojos.get(k)).getStart())));
                        if (Long.valueOf(getDateDiff(simpleDateFormat, currentFormatDate, currentFormatDateAfter)).longValue() >= 0 && !datesTabs.contains(currentFormatDate)) {
                            datesTabs.add(datesTabsIndex, currentFormatDate);
                            if (currentFormatDateAfter.equals(currentFormatDate)) {
                                currentDateIndex = datesTabsIndex;
                                break;
                            }
                            datesTabsIndex++;
                        }
                    }
                    ViewPager viewPager = this.viewpager;
                    viewPager.setAdapter(new SubTVArchiveCategoriesAdapter(datesTabs, xmltvProgrammePojos, opened_stream_id, opened_num, opened_channel_name, opened_channel_icon, opened_channel_id, opened_channel_duration, getSupportFragmentManager(), this));
                    this.slidingTabs.setupWithViewPager(this.viewpager);
                    this.viewpager.setCurrentItem(currentDateIndex);
                } else {
                    if (this.slidingTabs != null) {
                        this.slidingTabs.setVisibility(8);
                    }
                    if (this.rlTvArchiveTitle != null) {
                        this.rlTvArchiveTitle.setVisibility(0);
                    }
                    if (this.tvNoRecordFound != null) {
                        this.tvNoRecordFound.setVisibility(0);
                    }
                    if (this.tvEpgRequired != null) {
                        this.tvEpgRequired.setVisibility(0);
                    }
                }
                onFinish();
            }
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

    private void setToggleIconPosition() {
        this.tv = new TypedValue();
        if (getTheme().resolveAttribute(16843499, this.tv, true)) {
            this.actionBarHeight = TypedValue.complexToDimensionPixelSize(this.tv.data, getResources().getDisplayMetrics());
        }
        for (int i = 0; i < this.toolbar.getChildCount(); i++) {
            if (this.toolbar.getChildAt(i) instanceof ImageButton) {
                ((LayoutParams) this.toolbar.getChildAt(i).getLayoutParams()).gravity = 16;
            }
        }
    }

    public void onResume() {
        super.onResume();
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        if (this.loginPreferencesAfterLogin.getString("username", "").equals("") && this.loginPreferencesAfterLogin.getString("password", "").equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        //headerView();
    }

    public void liveStreamsEpg(LiveStreamsEpgCallback liveStreamsEpgCallback) {
    }

    public void atStart() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(0);
        }
    }

    public void onFinish() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
    }

    public void onFailed(String errorMessage) {
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.toolbar.inflateMenu(R.menu.menu_dashboard_logout);
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close_drawer:
                return;
            default:
                return;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout && this.context != null) {
            Utils.logoutUser(this.context);
        }
        return super.onOptionsItemSelected(item);
    }

    public void validateLogin(LoginCallback loginCallback, String validateLogin) {
        if (loginCallback != null && loginCallback.getUserLoginInfo().getAuth().intValue() == 1) {
            String userStatus = loginCallback.getUserLoginInfo().getStatus();
            if (!userStatus.equals("Active")) {
                Utils.showToast(this.context, AppConst.INVALID_STATUS + userStatus);
                if (this.context != null) {
                    Utils.logoutUser(this.context);
                }
            }
        }
    }

    public void stopLoader() {
    }
}