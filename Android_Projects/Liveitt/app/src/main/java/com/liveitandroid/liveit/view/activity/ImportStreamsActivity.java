package com.liveitandroid.liveit.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.model.callback.LiveStreamCategoriesCallback;
import com.liveitandroid.liveit.model.callback.LiveStreamsCallback;
import com.liveitandroid.liveit.model.callback.VodCategoriesCallback;
import com.liveitandroid.liveit.model.callback.VodStreamsCallback;
import com.liveitandroid.liveit.presenter.XtreamPanelAPIPresenter;
import com.liveitandroid.liveit.view.interfaces.PlayerApiInterface;
import com.liveitandroid.liveit.view.interfaces.XtreamPanelAPIInterface;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.callback.GetSeriesStreamCallback;
import com.liveitandroid.liveit.model.callback.GetSeriesStreamCategoriesCallback;
import com.liveitandroid.liveit.model.callback.XtreamPanelAPICallback;
import com.liveitandroid.liveit.model.database.DatabaseUpdatedStatusDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.database.SeriesStreamsDatabaseHandler;
import com.liveitandroid.liveit.model.pojo.PanelAvailableChannelsPojo;
import com.liveitandroid.liveit.model.pojo.PanelLivePojo;
import com.liveitandroid.liveit.model.pojo.PanelMoviePojo;
import com.liveitandroid.liveit.presenter.PlayerApiPresenter;
import com.liveitandroid.liveit.presenter.XMLTVPresenter;
import com.liveitandroid.liveit.view.utility.LoadingGearSpinner;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ImportStreamsActivity extends AppCompatActivity implements PlayerApiInterface {
    public static final String REDIRECT_CATCHUP = "redirect_catchup";
    public static final String REDIRECT_LIVE_TV = "redirect_live_tv";
    public static final String REDIRECT_SERIES = "redirect_series";
    public static final String REDIRECT_VOD = "redirect_vod";
    Context context;
    @BindView(R.id.iv_gear_loader)
    LoadingGearSpinner ivGearLoader;
    LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private PlayerApiPresenter playerApiPresenter;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rl_import_layout)
    RelativeLayout rlImportLayout;
    @BindView(R.id.rl_import_process)
    LinearLayout rlImportProcess;
    private SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler;
    @BindView(R.id.tv_countings)
    TextView tvCountings;
    @BindView(R.id.tv_importing_streams)
    TextView tvImportingStreams;
    @BindView(R.id.tv_percentage)
    TextView tvPercentage;
    @BindView(R.id.tv_setting_streams)
    TextView tvSettingStreams;
    private XMLTVPresenter xmlTvPresenter;

    private SessionManager mSessionManager;
    public LinearLayout fragment_fil_p2;
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_import_streams_new);
        ButterKnife.bind(this);
        changeStatusBarColor();
        getWindow().setFlags(1024, 1024);
        this.context = this;
        mSessionManager = new SessionManager(this.context);
        fragment_fil_p2 = (LinearLayout) findViewById(R.id.rl_import_process);
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
        }else{
            fragment_fil_p2.setBackgroundResource(R.drawable.splash_new_bg);
        }

        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        this.seriesStreamsDatabaseHandler = new SeriesStreamsDatabaseHandler(this.context);
        this.playerApiPresenter = new PlayerApiPresenter(this.context, this);
        initialize();
    }

    public void onResume() {
        super.onResume();
        getWindow().setFlags(1024, 1024);
    }

    String username = "";
    String password = "";

    private void initialize() {
        if (this.context != null) {
            if (this.tvImportingStreams != null) {
                this.tvImportingStreams.setText(getResources().getString(R.string.importign_all_channels));
            }
            this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            username = this.loginPreferencesAfterLogin.getString("username", "");
            password = this.loginPreferencesAfterLogin.getString("password", "");
            addDatabaseStatusOnSetup();
            getChannelsCategories(this.context, this.liveStreamDBHandler, username, password);
        }
    }


    private void getChannelsCategories(Context context, LiveStreamDBHandler liveStreamDBHandler, String username, String password) {
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty() && !username.equals("") && !password.equals("")) {
            liveStreamDBHandler.updateDBStatus(AppConst.DB_CHANNELS, "1", AppConst.DB_UPDATED_STATUS_PROCESSING);
            if (this.liveStreamDBHandler != null) {
                this.liveStreamDBHandler.makeEmptyAllChannelsVODTablesRecords();
            }
            this.playerApiPresenter.getStreamCat(username, password);
        }
    }

    private void getVodCategories(Context context, String username, String password) {
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty() && !username.equals("") && !password.equals("")) {
            this.playerApiPresenter.getStreamVodCat(username, password);
        }
    }

    private void getSerCategories(Context context, LiveStreamDBHandler liveStreamDBHandler, String username, String password) {
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty() && !username.equals("") && !password.equals("")) {
            if (this.seriesStreamsDatabaseHandler != null) {
                this.seriesStreamsDatabaseHandler.deleteAndRecreateAllVSeriesTables();
            }
            this.playerApiPresenter.getSeriesStreamCat(username, password);
        }
    }

    private void addDatabaseStatusOnSetup() {
        String currentDate = "";
        currentDate = currentDateValue();
        if (this.liveStreamDBHandler != null) {
            int count = this.liveStreamDBHandler.getDBStatusCount();
            if (count != -1 && count == 0) {
                if (currentDate != null) {
                    addDBStatus(this.liveStreamDBHandler, currentDate);
                } else {
                    Utils.showToast(this.context, "Invalid current date");
                }
            }
        }
        addSeriesStreamCatStatus(currentDate);
        addSeriesStreamStatus(currentDate);
    }

    private String currentDateValue() {
        return Utils.parseDateToddMMyyyy(Calendar.getInstance().getTime().toString());
    }

    private void addDBStatus(LiveStreamDBHandler liveStreamDBHandler, String currentDate) {
        DatabaseUpdatedStatusDBModel updatedStatusDBModel = new DatabaseUpdatedStatusDBModel();
        updatedStatusDBModel.setDbUpadatedStatusState("");
        updatedStatusDBModel.setDbLastUpdatedDate(currentDate);
        updatedStatusDBModel.setDbCategory(AppConst.DB_CHANNELS);
        updatedStatusDBModel.setDbCategoryID("1");
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

    public void atStart() {
    }

    public void onFinish() {
    }

    public void onFailed(String errorMessage) {
        //Utils.showToast(this.context, getResources().getString(R.string.network_error));
    }

    public void atProcess() {
    }

    public void getCategories(List<LiveStreamCategoriesCallback> getCategoriesCallback) {
        if (getCategoriesCallback != null) {
            if (liveStreamDBHandler != null) {
                liveStreamDBHandler.addLiveCategoriesPlayer(getCategoriesCallback);
            }
        }
        this.playerApiPresenter.getStream(username, password);
    }

    public void getStreamCatFailed(String message) {
        this.playerApiPresenter.getStream(username, password);
    }


    public void getStreams(List<LiveStreamsCallback> getStreamsCallback) {
        if (getStreamsCallback != null) {
            if (liveStreamDBHandler != null) {
                liveStreamDBHandler.addAllAvailableChannelPlayer(getStreamsCallback);
            }
        }
        getVodCategories(this.context, username, password);
    }

    public void getStreamsFailed(String message) {
        getVodCategories(this.context, username, password);
    }


    public void getCategoriesVod(List<VodCategoriesCallback> getVodCatCallback) {
        if (getVodCatCallback != null) {
            if (liveStreamDBHandler != null) {
                liveStreamDBHandler.addMovieCategoriesPlayer(getVodCatCallback);
            }
        }
        this.playerApiPresenter.getStreamVod(username, password);
    }

    public void getStreamCatVodFailed(String message) {
        this.playerApiPresenter.getStreamVod(username, password);
    }


    public void getStreamsVod(List<VodStreamsCallback> getVodStreamCallback) {
        if (getVodStreamCallback != null) {
            if (liveStreamDBHandler != null) {
                liveStreamDBHandler.addAllAvailableVodPlayer(getVodStreamCallback);
            }
        }
        getSerCategories(this.context, this.liveStreamDBHandler, username, password);
    }

    public void getStreamsVodFailed(String message) {
        getSerCategories(this.context, this.liveStreamDBHandler, username, password);
    }

    public void getSeriesCategories(List<GetSeriesStreamCategoriesCallback> getSeriesStreamCallback) {
        if (this.seriesStreamsDatabaseHandler != null) {
            this.seriesStreamsDatabaseHandler.deleteAndRecreateAllVSeriesTables();
        }
        if (getSeriesStreamCallback != null) {
            if (seriesStreamsDatabaseHandler != null) {
                seriesStreamsDatabaseHandler.addSeriesCategoriesPlayer(getSeriesStreamCallback);
            }
        }
        this.playerApiPresenter.getSeriesStream(username, password);
    }

    public void getSeriesStreamCatFailed(String message) {
        this.playerApiPresenter.getSeriesStream(username, password);
    }


    public void getSeriesStreams(List<GetSeriesStreamCallback> getSeriesStreamCallback) {
        if (getSeriesStreamCallback != null) {
            if (seriesStreamsDatabaseHandler != null) {
                seriesStreamsDatabaseHandler.addAllSeriesStreamsPlayer(getSeriesStreamCallback);
            }
        }

        String action = getIntent().getAction();
        if (REDIRECT_LIVE_TV.equals(action)) {
            startActivity(new Intent(this.context, LiveActivityNewFlow.class));
            finish();
        } else if (REDIRECT_VOD.equals(action)) {
            startActivity(new Intent(this.context, VodActivityNewFlow.class));
            finish();
        } else if (REDIRECT_SERIES.equals(action)) {
            startActivity(new Intent(this.context, SeriesActivtyNewFlow.class));
            finish();
        } else if (REDIRECT_CATCHUP.equals(action)) {
            startActivity(new Intent(this.context, TVArchiveActivityNewFlow.class));
            finish();
        } else {
            if(mSessionManager.getUserType().equals("Mag")){
                startActivity(new Intent(this.context, NewDashboardActivity2.class));
            }else{
                startActivity(new Intent(this.context, NewDashboardActivity.class));
            }
            finish();
        }
    }

    public void getSeriesStreamsFailed(String message) {
        String action = getIntent().getAction();
        if (REDIRECT_LIVE_TV.equals(action)) {
            startActivity(new Intent(this.context, LiveActivityNewFlow.class));
            finish();
        } else if (REDIRECT_VOD.equals(action)) {
            startActivity(new Intent(this.context, VodActivityNewFlow.class));
            finish();
        } else if (REDIRECT_SERIES.equals(action)) {
            startActivity(new Intent(this.context, SeriesActivtyNewFlow.class));
            finish();
        } else if (REDIRECT_CATCHUP.equals(action)) {
            startActivity(new Intent(this.context, TVArchiveActivityNewFlow.class));
            finish();
        } else {
            if(mSessionManager.getUserType().equals("Mag")){
                startActivity(new Intent(this.context, NewDashboardActivity2.class));
            }else{
                startActivity(new Intent(this.context, NewDashboardActivity.class));
            }
            finish();
        }
    }

    private void addSeriesStreamCatStatus(String currentDate) {
        if (this.seriesStreamsDatabaseHandler != null) {
            int count = this.seriesStreamsDatabaseHandler.getSeriesStreamsCatDBStatusCount();
            if (count != -1 && count == 0) {
                if (currentDate != null) {
                    addSeriesStreamCatDBStatus(this.seriesStreamsDatabaseHandler, currentDate);
                } else {
                    Utils.showToast(this.context, "Invalid current date");
                }
            }
        }
    }

    private void addSeriesStreamStatus(String currentDate) {
        if (this.seriesStreamsDatabaseHandler != null) {
            int count = this.seriesStreamsDatabaseHandler.getSeriesStreamsDBStatusCount();
            if (count != -1 && count == 0) {
                if (currentDate != null) {
                    addSeriesStreamDBStatus(this.seriesStreamsDatabaseHandler, currentDate);
                } else {
                    Utils.showToast(this.context, "Invalid current date");
                }
            }
        }
    }

    private void addSeriesStreamCatDBStatus(SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler, String currentDate) {
        DatabaseUpdatedStatusDBModel updatedStatusDBModel = new DatabaseUpdatedStatusDBModel();
        updatedStatusDBModel.setDbUpadatedStatusState("");
        updatedStatusDBModel.setDbLastUpdatedDate(currentDate);
        updatedStatusDBModel.setDbCategory(AppConst.DB_SERIES_STREAMS_CAT);
        updatedStatusDBModel.setDbCategoryID(AppConst.DB_SERIES_STREAMS_CAT_ID);
        seriesStreamsDatabaseHandler.addSeriesStreamsCatStatus(updatedStatusDBModel);
    }

    private void addSeriesStreamDBStatus(SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler, String currentDate) {
        DatabaseUpdatedStatusDBModel updatedStatusDBModel = new DatabaseUpdatedStatusDBModel();
        updatedStatusDBModel.setDbUpadatedStatusState("");
        updatedStatusDBModel.setDbLastUpdatedDate(currentDate);
        updatedStatusDBModel.setDbCategory(AppConst.DB_SERIES_STREAMS);
        updatedStatusDBModel.setDbCategoryID(AppConst.DB_SERIES_STREAMS_ID);
        seriesStreamsDatabaseHandler.addSeriesStreamsCatStatus(updatedStatusDBModel);
    }
}
