package com.liveitandroid.liveit.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.database.DatabaseUpdatedStatusDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.presenter.XMLTVPresenter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AccountInfoActivity extends AppCompatActivity implements OnClickListener {
    int actionBarHeight;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
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
    private ProgressDialog progressDialog;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private TypedValue tv;
    @BindView(R.id.tv_active_conn)
    TextView tvActiveConn;
    @BindView(R.id.tv_created_at)
    TextView tvCreatedAt;
    @BindView(R.id.tv_expiry_date)
    TextView tvExpiryDate;
    @BindView(R.id.tv_is_trial)
    TextView tvIsTrial;
    @BindView(R.id.tv_max_connections)
    TextView tvMaxConnections;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    private String userName = "";
    private String userPassword = "";
    private XMLTVPresenter xmltvPresenter;

    class C15553 implements Runnable {
        C15553() {
        }

        public void run() {
            try {
                String dateValue = Calendar.getInstance().getTime().toString();
                String currentCurrentTime = Utils.getTime(AccountInfoActivity.this.context);
                String currentCurrentDate = Utils.getDate(dateValue);
                if (AccountInfoActivity.this.time != null) {
                    AccountInfoActivity.this.time.setText(currentCurrentTime);
                }
                if (AccountInfoActivity.this.date != null) {
                    AccountInfoActivity.this.date.setText(currentCurrentDate);
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
                    AccountInfoActivity.this.doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e2) {
                }
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        ButterKnife.bind(this);
        changeStatusBarColor();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getWindow().setFlags(1024, 1024);
        initialize();
        new Thread(new CountDownRunner()).start();
    }

    public void doWork() {
        runOnUiThread(new C15553());
    }

    private SessionManager mSessionManager;

    private void initialize() {
        this.context = this;
        mSessionManager = new SessionManager(this.context);
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        String username = this.loginPreferencesAfterLogin.getString("username", "");
        String expDate = this.loginPreferencesAfterLogin.getString(AppConst.LOGIN_PREF_EXP_DATE, "");
        String isTrial = this.loginPreferencesAfterLogin.getString(AppConst.LOGIN_PREF_IS_TRIAL, "");
        String activeCons = this.loginPreferencesAfterLogin.getString(AppConst.LOGIN_PREF_ACTIVE_CONS, "");
        String createdAt = this.loginPreferencesAfterLogin.getString(AppConst.LOGIN_PREF_CREATE_AT, "");
        String maxConnections = this.loginPreferencesAfterLogin.getString(AppConst.LOGIN_PREF_MAX_CONNECTIONS, "");
        if (this.tvUsername != null) {
            if (username.isEmpty()) {
                this.tvUsername.setText("--");
            } else {
                this.tvUsername.setText(username);
            }
        }
        if (this.tvStatus != null) {
            this.tvStatus.setText("Ativo");
        }

        if(mSessionManager.getSeries().equals("nao")){
            if (this.tvExpiryDate != null) {
                if (expDate.isEmpty()) {
                    this.tvExpiryDate.setText("Ilimitado");
                } else {
                    this.tvExpiryDate.setText(mSessionManager.getUserExpiryDate());
                }
            }
            if (this.tvIsTrial != null) {
                if (isTrial.isEmpty()) {
                    this.tvIsTrial.setText("--");
                } else if (isTrial.equals(AppConst.PASSWORD_UNSET)) {
                    this.tvIsTrial.setText("Não");
                } else if (isTrial.equals("1")) {
                    this.tvIsTrial.setText("Sim");
                }
            }
            if (this.tvCreatedAt != null) {
                if (createdAt.isEmpty()) {
                    this.tvCreatedAt.setText("--");
                } else {
                    this.tvCreatedAt.setText(new SimpleDateFormat("MMMM d, yyyy").format(new Date(((long) Integer.parseInt(createdAt)) * 1000)));
                }
            }
        }else{
            if (this.tvExpiryDate != null) {
                if (expDate.isEmpty()) {
                    this.tvExpiryDate.setText("Ilimitado");
                } else {
                    this.tvExpiryDate.setText(expDate);
                }
            }
            if (this.tvIsTrial != null) {
                this.tvIsTrial.setText(isTrial);
            }
            if (this.tvCreatedAt != null) {
                if (createdAt.isEmpty()) {
                    this.tvCreatedAt.setText("--");
                } else {
                    this.tvCreatedAt.setText(createdAt);
                }
            }
        }


        if (this.tvActiveConn != null) {
            if (activeCons.isEmpty()) {
                this.tvActiveConn.setText("--");
            } else {
                this.tvActiveConn.setText(activeCons);
            }
        }

        if (this.tvMaxConnections == null) {
            return;
        }
        if (maxConnections.isEmpty()) {
            this.tvMaxConnections.setText("--");
        } else {
            this.tvMaxConnections.setText(maxConnections);
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
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
    public void onResume() {
        super.onResume();
        getWindow().setFlags(1024, 1024);
        onFinish();
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

    public void onFinish() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }
}
