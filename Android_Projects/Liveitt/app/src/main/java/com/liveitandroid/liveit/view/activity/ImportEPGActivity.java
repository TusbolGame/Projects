package com.liveitandroid.liveit.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.liveitandroid.liveit.view.interfaces.XMLTVInterface;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.callback.XMLTVCallback;
import com.liveitandroid.liveit.model.database.DatabaseUpdatedStatusDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.presenter.XMLTVPresenter;
import com.liveitandroid.liveit.view.interfaces.XMLTVInterface;
import com.liveitandroid.liveit.view.utility.LoadingGearSpinner;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Calendar;

public class ImportEPGActivity extends AppCompatActivity implements XMLTVInterface {
    public static final String REDIRECT_EPG_CATEGORY = "redirect_epg_category";
    Context context;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel();
    @BindView(R.id.iv_gear_loader)
    LoadingGearSpinner ivGearLoader;
    LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private Editor loginPrefsEditor;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rl_skip)
    RelativeLayout rlSkip;
    @BindView(R.id.tv_countings)
    TextView tvCountings;
    @BindView(R.id.tv_importing_epg)
    TextView tvImportingEpg;
    @BindView(R.id.tv_percentage)
    TextView tvPercentage;
    @BindView(R.id.tv_setting_streams)
    TextView tvSettingStreams;
    private XMLTVPresenter xmlTvPresenter;

    class AnonymousClass1NewAsyncTask extends AsyncTask<String, Integer, Boolean> {
        //final int ITERATIONS = this.val$xmltvCallback.programmePojos.size();
        Context mcontext = null;
        private volatile boolean running = true;
        final /* synthetic */ XMLTVCallback val$xmltvCallback;

        AnonymousClass1NewAsyncTask(Context context, XMLTVCallback xMLTVCallback) {
            this.val$xmltvCallback = xMLTVCallback;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            if (ImportEPGActivity.this.liveStreamDBHandler != null) {
                ImportEPGActivity.this.liveStreamDBHandler.addEPG(this.val$xmltvCallback.programmePojos);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            int totalEPGFound = this.val$xmltvCallback.programmePojos.size();
            ImportEPGActivity.this.loginPreferencesAfterLogin = ImportEPGActivity.this.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            String skipButton = ImportEPGActivity.this.loginPreferencesAfterLogin.getString(AppConst.SKIP_BUTTON_PREF, "");
            Utils.showToast(ImportEPGActivity.this.context, ImportEPGActivity.this.getResources().getString(R.string.epg_imported) + " (" + totalEPGFound + ")");
            if (ImportEPGActivity.this.liveStreamDBHandler != null) {
                ImportEPGActivity.this.liveStreamDBHandler.updateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID, AppConst.DB_UPDATED_STATUS_FINISH);
            }
            if (ImportEPGActivity.this.context != null) {
                if (ImportEPGActivity.REDIRECT_EPG_CATEGORY.equals(ImportEPGActivity.this.getIntent().getAction())) {
                    ImportEPGActivity.this.startActivity(new Intent(ImportEPGActivity.this.context, NewEPGCategoriesActivity.class));
                    ImportEPGActivity.this.finish();
                    return;
                }
                if(mSessionManager.getUserType().equals("Mag")){
                    ImportEPGActivity.this.startActivity(new Intent(ImportEPGActivity.this.context, NewDashboardActivity2.class));
                }else{
                    ImportEPGActivity.this.startActivity(new Intent(ImportEPGActivity.this.context, NewDashboardActivity.class));
                }
                ImportEPGActivity.this.finish();
            }
        }

        protected void onCancelled() {
            this.running = false;
        }
    }
    private SessionManager mSessionManager;
    public RelativeLayout fragment_fil_p2;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_import_epg_new);
        ButterKnife.bind(this);
        changeStatusBarColor();
        this.context = this;
        mSessionManager = new SessionManager(this.context);
        fragment_fil_p2 = (RelativeLayout) findViewById(R.id.rl_import_process);
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

        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        initialize();
        getWindow().setFlags(1024, 1024);
    }

    private void initialize() {
        if (this.context != null) {
            this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            String username = this.loginPreferencesAfterLogin.getString("username", "");
            String password = this.loginPreferencesAfterLogin.getString("password", "");
            int epgCounts = this.liveStreamDBHandler.getEPGCount();
            this.databaseUpdatedStatusDBModelEPG = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID);
            String status = "";
            if (this.databaseUpdatedStatusDBModelEPG != null) {
                addDatabaseStatusOnSetup(this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState());
            }
            this.xmlTvPresenter = new XMLTVPresenter(this, this.context);
            this.liveStreamDBHandler.updateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID, AppConst.DB_UPDATED_STATUS_PROCESSING);
            this.xmlTvPresenter.epgXMLTV(username, password);
        }
    }

    private void addDatabaseStatusOnSetup(String status) {
        String str = "";
        str = currentDateValue();
        if (status != null && !status.equals("")) {
            return;
        }
        if (str != null) {
            addDBStatus(this.liveStreamDBHandler, str);
        } else {
            Utils.showToast(this.context, "Invalid current date");
        }
    }

    private String currentDateValue() {
        return Utils.parseDateToddMMyyyy(Calendar.getInstance().getTime().toString());
    }

    private void addDBStatus(LiveStreamDBHandler liveStreamDBHandler, String currentDate) {
        DatabaseUpdatedStatusDBModel updatedStatusDBModel = new DatabaseUpdatedStatusDBModel();
        updatedStatusDBModel.setDbUpadatedStatusState("");
        updatedStatusDBModel.setDbLastUpdatedDate(currentDate);
        updatedStatusDBModel.setDbCategory(AppConst.DB_EPG);
        updatedStatusDBModel.setDbCategoryID(AppConst.DB_EPG_ID);
        liveStreamDBHandler.addDBUpdatedStatus(updatedStatusDBModel);
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

    public void epgXMLTV(XMLTVCallback xmltvCallback) {
        if (xmltvCallback != null && this.context != null && xmltvCallback.programmePojos != null) {
            if (this.liveStreamDBHandler != null) {
                this.liveStreamDBHandler.makeEmptyEPG();
            }
            if (this.tvImportingEpg != null) {
                this.tvImportingEpg.setText(getResources().getString(R.string.importing_epg));
            }
            if (VERSION.SDK_INT >= 11) {
                new AnonymousClass1NewAsyncTask(this.context, xmltvCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
            } else {
                new AnonymousClass1NewAsyncTask(this.context, xmltvCallback).execute(new String[0]);
            }
        } else if (this.context != null) {
            if (this.liveStreamDBHandler != null) {
                this.liveStreamDBHandler.updateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID, AppConst.DB_UPDATED_STATUS_FINISH);
            }
            if (REDIRECT_EPG_CATEGORY.equals(getIntent().getAction())) {
                startActivity(new Intent(this.context, NewEPGCategoriesActivity.class));
                finish();
                return;
            }

            if (REDIRECT_EPG_CATEGORY.equals("redirect_ATUALIZA")) {
                startActivity(new Intent(this, LiveActivityNewFlow.class));
                finish();
                return;
            }

            if(mSessionManager.getUserType().equals("Mag")){
                startActivity(new Intent(this.context, NewDashboardActivity2.class));
            }else{
                startActivity(new Intent(this.context, NewDashboardActivity.class));
            }
            finish();
        }
    }

    public void onResume() {
        super.onResume();
        getWindow().setFlags(1024, 1024);
    }

    public void atStart() {
    }

    public void onFinish() {
    }

    public void onFailed(String errorMessage) {
        Utils.showToast(this.context, getResources().getString(R.string.network_error));
    }

    @OnClick({R.id.bt_skip})
    public void onViewClicked() {
        if (this.context != null) {
            this.loginPrefsEditor = this.loginPreferencesAfterLogin.edit();
            if (this.loginPrefsEditor != null) {
                this.loginPrefsEditor.putString(AppConst.SKIP_BUTTON_PREF, "pressed");
                this.loginPrefsEditor.commit();
            }

            if(mSessionManager.getUserType().equals("Mag")){
                startActivity(new Intent(this.context, NewDashboardActivity2.class));
            }else{
                startActivity(new Intent(this.context, NewDashboardActivity.class));
            }
            finish();
        }
    }

    public void epgXMLTVUpdateFailed(String failedUpdate) {
        if (failedUpdate.equals(AppConst.DB_UPDATED_STATUS_FAILED) && this.liveStreamDBHandler != null) {
            this.liveStreamDBHandler.updateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID, AppConst.DB_UPDATED_STATUS_FAILED);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        if(mSessionManager.getUserType().equals("Mag")){
            startActivity(new Intent(this, NewDashboardActivity2.class));
        }else{
            startActivity(new Intent(this, NewDashboardActivity.class));
        }
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        finish();
    }
}
