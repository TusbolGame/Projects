package com.liveitandroid.liveit.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.view.interfaces.SeriesInterface;
import com.google.gson.JsonElement;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.model.callback.GetEpisdoeDetailsCallback;
import com.liveitandroid.liveit.model.callback.SeasonsDetailCallback;
import com.liveitandroid.liveit.model.database.DatabaseUpdatedStatusDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.database.PasswordStatusDBModel;
import com.liveitandroid.liveit.model.database.SeriesStreamsDatabaseHandler;
import com.liveitandroid.liveit.presenter.SeriesPresenter;
import com.liveitandroid.liveit.presenter.XMLTVPresenter;
import com.liveitandroid.liveit.view.adapter.EpisodeDetailAdapter;

import com.liveitandroid.liveit.view.adapter.SeasonsAdapter;
import com.liveitandroid.liveit.view.interfaces.SeriesInterface;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SeasonsActivitiy extends AppCompatActivity implements SeriesInterface, OnClickListener {
    static ProgressBar pbPagingLoader1;
    int actionBarHeight;
    @BindView(R.id.main_layout)
    LinearLayout activityLogin;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    ArrayList<LiveStreamCategoryIdDBModel> categoriesList;
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    TextView clientNameTv;
    @BindView(R.id.content_drawer)
    RelativeLayout contentDrawer;
    private Context context;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel();
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel();

    private ArrayList<GetEpisdoeDetailsCallback> episdoeDetailsCallbacksList = new ArrayList();
    private EpisodeDetailAdapter episodeDetailAdapter;
    @BindView(R.id.fl_frame)
    FrameLayout frameLayout;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetail;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlckedDetail;
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private SeasonsAdapter mAdapter;
    private LayoutManager mLayoutManager;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    @BindView(R.id.pb_paging_loader)
    ProgressBar pbPagingLoader;
    @BindView(R.id.rl_vod_layout)
    RelativeLayout rl_vod_layout;
    SearchView searchView;
    private ArrayList<SeasonsDetailCallback> seasonsDetailCallbacks = new ArrayList();
    @BindView(R.id.tv_settings)
    TextView seasonsName;
    private String seriesCover = "";
    private String seriesId = "";
    private String seriesName = "";
    private SeriesPresenter seriesPresenter;
    private SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler;
    ArrayList<LiveStreamCategoryIdDBModel> subCategoryList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TypedValue tv;
    @BindView(R.id.empty_view)
    TextView tvNoRecordFound;
    private String userName = "";
    private String userPassword = "";
    private XMLTVPresenter xmltvPresenter;

    class C16711 implements DialogInterface.OnClickListener {
        C16711() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C16722 implements DialogInterface.OnClickListener {
        C16722() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.logoutUser(SeasonsActivitiy.this.context);
        }
    }

    class C16733 implements DialogInterface.OnClickListener {
        C16733() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsAndVod(SeasonsActivitiy.this.context);
        }
    }

    class C16744 implements DialogInterface.OnClickListener {
        C16744() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C16755 implements DialogInterface.OnClickListener {
        C16755() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadTvGuid(SeasonsActivitiy.this.context);
        }
    }

    class C16766 implements DialogInterface.OnClickListener {
        C16766() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C16777 implements OnQueryTextListener {
        C16777() {
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            SeasonsActivitiy.this.tvNoRecordFound.setVisibility(8);
            if (!(SeasonsActivitiy.this.mAdapter == null || SeasonsActivitiy.this.tvNoRecordFound == null || SeasonsActivitiy.this.tvNoRecordFound.getVisibility() == 0)) {
                SeasonsActivitiy.this.mAdapter.filter(newText, SeasonsActivitiy.this.tvNoRecordFound);
            }
            return false;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasons_activitiy);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        if (this.appbarToolbar != null) {
            this.appbarToolbar.setBackground(getResources().getDrawable(R.drawable.vod_backgound));
        }
        changeStatusBarColor();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getWindow().setFlags(1024, 1024);
        initializeV();
        mSessionManager = new SessionManager(this.context);
        this.frameLayout.setVisibility(View.GONE);
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

    private void initializeV() {
        this.context = this;
        this.seriesPresenter = new SeriesPresenter(this.context, this);
        Intent intent = getIntent();
        if (intent != null) {
            this.seriesId = intent.getStringExtra(AppConst.SERIES_SERIES_ID);
            this.seriesCover = intent.getStringExtra(AppConst.SERIES_COVER);
            this.seriesName = intent.getStringExtra(AppConst.SERIES_NAME);
            if (!(this.seriesName == null || this.seriesName.isEmpty())) {
                this.seasonsName.setText(this.seriesName);
                this.seasonsName.setSelected(true);
            }
        }
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        String username = this.loginPreferencesAfterLogin.getString("username", "");
        String password = this.loginPreferencesAfterLogin.getString("password", "");
        if (this.seriesId != null && !this.seriesId.isEmpty() && this.seriesPresenter != null && username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            this.seriesPresenter.getSeriesEpisode(username, password, this.seriesId);
        }
    }

//    public void getSeriesEpisodeInfo(JsonElement jsonElement) {
//        if (jsonElement != null) {
//            JSONObject jsonObject;
//            JSONObject jsonArrayEpisodes;
//            Iterator<String> iterator;
//            String key;
//            JSONArray jsonArrayEpisode;
//            int i;
//            JSONObject jsonObjectEpisodeDetails;
//            GetEpisdoeDetailsCallback getEpisdoeDetailsCallback;
//            SeasonsDetailCallback seasonsDetailCallback;
//            JSONArray jsonArraySeasons;
//            try {
//                jsonObject = new JSONObject(jsonElement.toString());
//                jsonArrayEpisodes = null;
//                String seasons = jsonObject.getString("seasons");
//                boolean seanValue;
//                if (seasons.equals("null")) {
//                    if (jsonObject.getJSONObject("episodes") != null) {
//                        jsonArrayEpisodes = jsonObject.getJSONObject("episodes");
//                    }
//                    this.episdoeDetailsCallbacksList.clear();
//                    if (jsonArrayEpisodes != null) {
//                        iterator = jsonArrayEpisodes.keys();
//                        while (iterator.hasNext()) {
//                            key = Integer.valueOf((String) iterator.next());
//                            seanValue = true;
//                            if (jsonArrayEpisodes.get(String.valueOf(key)) instanceof JSONArray) {
//                                jsonArrayEpisode = new JSONArray(jsonArrayEpisodes.get(String.valueOf(key)).toString());
//                                for (i = 0; i < jsonArrayEpisode.length(); i++) {
//                                    jsonObjectEpisodeDetails = jsonArrayEpisode.getJSONObject(i);
//                                    getEpisdoeDetailsCallback = new GetEpisdoeDetailsCallback();
//                                    seasonsDetailCallback = new SeasonsDetailCallback();
//                                    if (jsonObjectEpisodeDetails.getString("id") == null || jsonObjectEpisodeDetails.getString("id").isEmpty()) {
//                                        getEpisdoeDetailsCallback.setId("");
//                                    } else {
//                                        getEpisdoeDetailsCallback.setId(jsonObjectEpisodeDetails.getString("id"));
//                                    }
//                                    if (jsonObjectEpisodeDetails.getInt("season") == -1 || jsonObjectEpisodeDetails.getInt("season") == 0) {
//                                        getEpisdoeDetailsCallback.setId("");
//                                    } else {
//                                        getEpisdoeDetailsCallback.setSeasonNumber(Integer.valueOf(jsonObjectEpisodeDetails.getInt("season")));
//                                    }
//                                    if (seanValue) {
//                                        seanValue = false;
//                                        if (jsonObjectEpisodeDetails.getInt("season") == -1 || jsonObjectEpisodeDetails.getInt("season") == 0) {
//                                            seasonsDetailCallback.setSeasonNumber(Integer.valueOf(-1));
//                                        } else {
//                                            seasonsDetailCallback.setSeasonNumber(Integer.valueOf(jsonObjectEpisodeDetails.getInt("season")));
//                                        }
//                                        seasonsDetailCallback.setAirDate("");
//                                        seasonsDetailCallback.setEpisodeCount(Integer.valueOf(-1));
//                                        seasonsDetailCallback.setId(Integer.valueOf(-1));
//                                        seasonsDetailCallback.setName("");
//                                        seasonsDetailCallback.setOverview("");
//                                        seasonsDetailCallback.setCoverBig("");
//                                        seasonsDetailCallback.setCoverBig("");
//                                        this.seasonsDetailCallbacks.add(seasonsDetailCallback);
//                                    }
//                                    if (jsonObjectEpisodeDetails.getString("title") == null || jsonObjectEpisodeDetails.getString("title").isEmpty()) {
//                                        getEpisdoeDetailsCallback.setTitle("");
//                                    } else {
//                                        getEpisdoeDetailsCallback.setTitle(jsonObjectEpisodeDetails.getString("title"));
//                                    }
//                                    if (jsonObjectEpisodeDetails.getString("direct_source") == null || jsonObjectEpisodeDetails.getString("direct_source").isEmpty()) {
//                                        getEpisdoeDetailsCallback.setDirectSource("");
//                                    } else {
//                                        getEpisdoeDetailsCallback.setDirectSource(jsonObjectEpisodeDetails.getString("direct_source"));
//                                    }
//                                    if (jsonObjectEpisodeDetails.getString("added") == null || jsonObjectEpisodeDetails.getString("added").isEmpty()) {
//                                        getEpisdoeDetailsCallback.setAdded("");
//                                    } else {
//                                        getEpisdoeDetailsCallback.setAdded(jsonObjectEpisodeDetails.getString("added"));
//                                    }
//                                    if (jsonObjectEpisodeDetails.getString("custom_sid") == null || jsonObjectEpisodeDetails.getString("custom_sid").isEmpty()) {
//                                        getEpisdoeDetailsCallback.setCustomSid("");
//                                    } else {
//                                        getEpisdoeDetailsCallback.setCustomSid(jsonObjectEpisodeDetails.getString("custom_sid"));
//                                    }
//                                    if (jsonObjectEpisodeDetails.getString("container_extension") == null || jsonObjectEpisodeDetails.getString("container_extension").isEmpty()) {
//                                        getEpisdoeDetailsCallback.setContainerExtension("");
//                                    } else {
//                                        getEpisdoeDetailsCallback.setContainerExtension(jsonObjectEpisodeDetails.getString("container_extension"));
//                                    }
//                                    this.episdoeDetailsCallbacksList.add(getEpisdoeDetailsCallback);
//                                }
//                                continue;
//                            }
//                        }
//                    }
//                } else if (seasons.equals("[]")) {
//                    if (jsonObject.getJSONObject("episodes") != null) {
//                        jsonArrayEpisodes = jsonObject.getJSONObject("episodes");
//                    }
//                    this.episdoeDetailsCallbacksList.clear();
//                    if (jsonArrayEpisodes != null) {
//                        iterator = jsonArrayEpisodes.keys();
//                        while (iterator.hasNext()) {
//                            key = (String) iterator.next();
//                            seanValue = true;
//                            if (jsonArrayEpisodes.get(key) instanceof JSONArray) {
//                                jsonArrayEpisode = new JSONArray(jsonArrayEpisodes.get(key).toString());
//                                for (i = 0; i < jsonArrayEpisode.length(); i++) {
//                                    jsonObjectEpisodeDetails = jsonArrayEpisode.getJSONObject(i);
//                                    getEpisdoeDetailsCallback = new GetEpisdoeDetailsCallback();
//                                    seasonsDetailCallback = new SeasonsDetailCallback();
//                                    if (jsonObjectEpisodeDetails.getString("id") == null || jsonObjectEpisodeDetails.getString("id").isEmpty()) {
//                                        getEpisdoeDetailsCallback.setId("");
//                                    } else {
//                                        getEpisdoeDetailsCallback.setId(jsonObjectEpisodeDetails.getString("id"));
//                                    }
//                                    if (jsonObjectEpisodeDetails.getInt("season") == -1 || jsonObjectEpisodeDetails.getInt("season") == 0) {
//                                        getEpisdoeDetailsCallback.setId("");
//                                    } else {
//                                        getEpisdoeDetailsCallback.setSeasonNumber(Integer.valueOf(jsonObjectEpisodeDetails.getInt("season")));
//                                    }
//                                    if (seanValue) {
//                                        seanValue = false;
//                                        if (jsonObjectEpisodeDetails.getInt("season") == -1 || jsonObjectEpisodeDetails.getInt("season") == 0) {
//                                            seasonsDetailCallback.setSeasonNumber(Integer.valueOf(-1));
//                                        } else {
//                                            seasonsDetailCallback.setSeasonNumber(Integer.valueOf(jsonObjectEpisodeDetails.getInt("season")));
//                                        }
//                                        seasonsDetailCallback.setAirDate("");
//                                        seasonsDetailCallback.setEpisodeCount(Integer.valueOf(-1));
//                                        seasonsDetailCallback.setId(Integer.valueOf(-1));
//                                        seasonsDetailCallback.setName("");
//                                        seasonsDetailCallback.setOverview("");
//                                        seasonsDetailCallback.setCoverBig("");
//                                        seasonsDetailCallback.setCoverBig("");
//                                        this.seasonsDetailCallbacks.add(seasonsDetailCallback);
//                                    }
//                                    if (jsonObjectEpisodeDetails.getString("title") == null || jsonObjectEpisodeDetails.getString("title").isEmpty()) {
//                                        getEpisdoeDetailsCallback.setTitle("");
//                                    } else {
//                                        getEpisdoeDetailsCallback.setTitle(jsonObjectEpisodeDetails.getString("title"));
//                                    }
//                                    if (jsonObjectEpisodeDetails.getString("direct_source") == null || jsonObjectEpisodeDetails.getString("direct_source").isEmpty()) {
//                                        getEpisdoeDetailsCallback.setDirectSource("");
//                                    } else {
//                                        getEpisdoeDetailsCallback.setDirectSource(jsonObjectEpisodeDetails.getString("direct_source"));
//                                    }
//                                    if (jsonObjectEpisodeDetails.getString("added") == null || jsonObjectEpisodeDetails.getString("added").isEmpty()) {
//                                        getEpisdoeDetailsCallback.setAdded("");
//                                    } else {
//                                        getEpisdoeDetailsCallback.setAdded(jsonObjectEpisodeDetails.getString("added"));
//                                    }
//                                    if (jsonObjectEpisodeDetails.getString("custom_sid") == null || jsonObjectEpisodeDetails.getString("custom_sid").isEmpty()) {
//                                        getEpisdoeDetailsCallback.setCustomSid("");
//                                    } else {
//                                        getEpisdoeDetailsCallback.setCustomSid(jsonObjectEpisodeDetails.getString("custom_sid"));
//                                    }
//                                    if (jsonObjectEpisodeDetails.getString("container_extension") == null || jsonObjectEpisodeDetails.getString("container_extension").isEmpty()) {
//                                        getEpisdoeDetailsCallback.setContainerExtension("");
//                                    } else {
//                                        getEpisdoeDetailsCallback.setContainerExtension(jsonObjectEpisodeDetails.getString("container_extension"));
//                                    }
//                                    this.episdoeDetailsCallbacksList.add(getEpisdoeDetailsCallback);
//                                }
//                            }
//                        }
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            try {
//                jsonObject = new JSONObject(jsonElement.toString());
//                jsonArrayEpisodes = null;
//                jsonArraySeasons = null;
//                if (jsonObject.getJSONObject("episodes") != null) {
//                    jsonArraySeasons = jsonObject.getJSONObject("seasons");
//                }
//                if (jsonObject.getJSONObject("episodes") != null) {
//                    jsonArrayEpisodes = jsonObject.getJSONObject("episodes");
//                }
//                this.episdoeDetailsCallbacksList.clear();
//                Iterator<?> keys = jsonArraySeasons.keys();
//                while (keys.hasNext()) {
//                    key = (String) keys.next() ;
//                    if (jsonArraySeasons.get(key) instanceof JSONObject) {
//                        seasonsDetailCallback = new SeasonsDetailCallback();
//                        if (((JSONObject) jsonArraySeasons.get(key)).get("air_date").toString().equals("null")) {
//                            seasonsDetailCallback.setAirDate("");
//                        } else if (((JSONObject) jsonArraySeasons.get(key)).get("air_date") == null || ((String) ((JSONObject) jsonArraySeasons.get(key)).get("air_date")).isEmpty()) {
//                            seasonsDetailCallback.setAirDate("");
//                        } else {
//                            seasonsDetailCallback.setAirDate((String) ((JSONObject) jsonArraySeasons.get(key)).get("air_date"));
//                        }
//                        if (((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("episode_count")) == null || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("episode_count")).intValue() == -1 || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("episode_count")).intValue() == 0) {
//                            seasonsDetailCallback.setEpisodeCount(Integer.valueOf(-1));
//                        } else {
//                            seasonsDetailCallback.setEpisodeCount((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("episode_count"));
//                        }
//                        if (((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("id")) == null || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("id")).intValue() == -1 || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("id")).intValue() == 0) {
//                            seasonsDetailCallback.setId(Integer.valueOf(-1));
//                        } else {
//                            seasonsDetailCallback.setId((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("id"));
//                        }
//                        if (((String) ((JSONObject) jsonArraySeasons.get(key)).get("name")) == null || ((String) ((JSONObject) jsonArraySeasons.get(key)).get("name")).isEmpty()) {
//                            seasonsDetailCallback.setName("");
//                        } else {
//                            seasonsDetailCallback.setName((String) ((JSONObject) jsonArraySeasons.get(key)).get("name"));
//                        }
//                        if (((String) ((JSONObject) jsonArraySeasons.get(key)).get("overview")) == null || ((String) ((JSONObject) jsonArraySeasons.get(key)).get("overview")).isEmpty()) {
//                            seasonsDetailCallback.setOverview("");
//                        } else {
//                            seasonsDetailCallback.setOverview((String) ((JSONObject) jsonArraySeasons.get(key)).get("overview"));
//                        }
//                        if (((Integer) ((JSONObject) jsonArraySeasons.get(key)).get(AppConst.SEASON_NUMBER)) == null || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get(AppConst.SEASON_NUMBER)).intValue() == -1 || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get(AppConst.SEASON_NUMBER)).intValue() == 0) {
//                            seasonsDetailCallback.setSeasonNumber(Integer.valueOf(-1));
//                        } else {
//                            seasonsDetailCallback.setSeasonNumber((Integer) ((JSONObject) jsonArraySeasons.get(key)).get(AppConst.SEASON_NUMBER));
//                        }
//                        if (((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover")) == null || ((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover")).isEmpty()) {
//                            seasonsDetailCallback.setCover("");
//                        } else {
//                            seasonsDetailCallback.setCover((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover"));
//                        }
//                        if (((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover_big")) == null || ((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover_big")).isEmpty()) {
//                            seasonsDetailCallback.setCoverBig("");
//                        } else {
//                            seasonsDetailCallback.setCoverBig((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover_big"));
//                        }
//                        this.seasonsDetailCallbacks.add(seasonsDetailCallback);
//                    }
//                }
//                if (jsonArrayEpisodes != null) {
//                    iterator = jsonArrayEpisodes.keys();
//                    while (iterator.hasNext()) {
//                        key = (String) iterator.next();
//                        if (jsonArrayEpisodes.get(key) instanceof JSONArray) {
//                            jsonArrayEpisode = new JSONArray(jsonArrayEpisodes.get(key).toString());
//                            for (i = 0; i < jsonArrayEpisode.length(); i++) {
//                                jsonObjectEpisodeDetails = jsonArrayEpisode.getJSONObject(i);
//                                getEpisdoeDetailsCallback = new GetEpisdoeDetailsCallback();
//                                if (jsonObjectEpisodeDetails.getString("id") == null || jsonObjectEpisodeDetails.getString("id").isEmpty()) {
//                                    getEpisdoeDetailsCallback.setId("");
//                                } else {
//                                    getEpisdoeDetailsCallback.setId(jsonObjectEpisodeDetails.getString("id"));
//                                }
//                                if (jsonObjectEpisodeDetails.getInt("season") == -1 || jsonObjectEpisodeDetails.getInt("season") == 0) {
//                                    getEpisdoeDetailsCallback.setId("");
//                                } else {
//                                    getEpisdoeDetailsCallback.setSeasonNumber(Integer.valueOf(jsonObjectEpisodeDetails.getInt("season")));
//                                }
//                                if (jsonObjectEpisodeDetails.getString("title") == null || jsonObjectEpisodeDetails.getString("title").isEmpty()) {
//                                    getEpisdoeDetailsCallback.setTitle("");
//                                } else {
//                                    getEpisdoeDetailsCallback.setTitle(jsonObjectEpisodeDetails.getString("title"));
//                                }
//                                if (jsonObjectEpisodeDetails.getString("direct_source") == null || jsonObjectEpisodeDetails.getString("direct_source").isEmpty()) {
//                                    getEpisdoeDetailsCallback.setDirectSource("");
//                                } else {
//                                    getEpisdoeDetailsCallback.setDirectSource(jsonObjectEpisodeDetails.getString("direct_source"));
//                                }
//                                if (jsonObjectEpisodeDetails.getString("added") == null || jsonObjectEpisodeDetails.getString("added").isEmpty()) {
//                                    getEpisdoeDetailsCallback.setAdded("");
//                                } else {
//                                    getEpisdoeDetailsCallback.setAdded(jsonObjectEpisodeDetails.getString("added"));
//                                }
//                                if (jsonObjectEpisodeDetails.getString("custom_sid") == null || jsonObjectEpisodeDetails.getString("custom_sid").isEmpty()) {
//                                    getEpisdoeDetailsCallback.setCustomSid("");
//                                } else {
//                                    getEpisdoeDetailsCallback.setCustomSid(jsonObjectEpisodeDetails.getString("custom_sid"));
//                                }
//                                if (jsonObjectEpisodeDetails.getString("container_extension") == null || jsonObjectEpisodeDetails.getString("container_extension").isEmpty()) {
//                                    getEpisdoeDetailsCallback.setContainerExtension("");
//                                } else {
//                                    getEpisdoeDetailsCallback.setContainerExtension(jsonObjectEpisodeDetails.getString("container_extension"));
//                                }
//                                this.episdoeDetailsCallbacksList.add(getEpisdoeDetailsCallback);
//                            }
//                        }
//                    }
//                }
//            } catch (JSONException e2) {
//                e2.printStackTrace();
//            }
//            try {
//                jsonObject = new JSONObject(jsonElement.toString());
//                jsonArrayEpisodes = null;
//                jsonArraySeasons = jsonObject.getJSONArray("seasons");
//                if (jsonObject.getJSONObject("episodes") != null) {
//                    jsonArrayEpisodes = jsonObject.getJSONObject("episodes");
//                }
//                this.episdoeDetailsCallbacksList.clear();
//                int k = 0;
//                while (k < jsonArraySeasons.length()) {
//                    seasonsDetailCallback = new SeasonsDetailCallback();
//                    String airDate = ((JSONObject) jsonArraySeasons.get(k)).getString("air_date");
//                    if (((JSONObject) jsonArraySeasons.get(k)).getString("air_date") == null || ((JSONObject) jsonArraySeasons.get(k)).getString("air_date").isEmpty()) {
//                        seasonsDetailCallback.setAirDate("");
//                    } else {
//                        seasonsDetailCallback.setAirDate(((JSONObject) jsonArraySeasons.get(k)).getString("air_date"));
//                    }
//                    if (((JSONObject) jsonArraySeasons.get(k)).getString("episode_count") == null || Integer.valueOf(((JSONObject) jsonArraySeasons.get(k)).getInt("episode_count")).intValue() == -1 || Integer.valueOf(((JSONObject) jsonArraySeasons.get(k)).getInt("episode_count")).intValue() == 0) {
//                        seasonsDetailCallback.setEpisodeCount(Integer.valueOf(-1));
//                    } else {
//                        seasonsDetailCallback.setEpisodeCount(Integer.valueOf(((JSONObject) jsonArraySeasons.get(k)).getInt("episode_count")));
//                    }
//                    if (Integer.valueOf(((JSONObject) jsonArraySeasons.get(k)).getInt("id")) == null || Integer.valueOf(((JSONObject) jsonArraySeasons.get(k)).getInt("id")).intValue() == -1 || Integer.valueOf(((JSONObject) jsonArraySeasons.get(k)).getInt("id")).intValue() == 0) {
//                        seasonsDetailCallback.setId(Integer.valueOf(-1));
//                    } else {
//                        seasonsDetailCallback.setId(Integer.valueOf(((JSONObject) jsonArraySeasons.get(k)).getInt("id")));
//                    }
//                    if (((JSONObject) jsonArraySeasons.get(k)).getString("name") == null || ((JSONObject) jsonArraySeasons.get(k)).getString("name").isEmpty()) {
//                        seasonsDetailCallback.setName("");
//                    } else {
//                        seasonsDetailCallback.setName(((JSONObject) jsonArraySeasons.get(k)).getString("name"));
//                    }
//                    if (((JSONObject) jsonArraySeasons.get(k)).getString("overview") == null || ((JSONObject) jsonArraySeasons.get(k)).getString("overview").isEmpty()) {
//                        seasonsDetailCallback.setOverview("");
//                    } else {
//                        seasonsDetailCallback.setOverview(((JSONObject) jsonArraySeasons.get(k)).getString("overview"));
//                    }
//                    if (Integer.valueOf(((JSONObject) jsonArraySeasons.get(k)).getInt(AppConst.SEASON_NUMBER)) == null || Integer.valueOf(((JSONObject) jsonArraySeasons.get(k)).getInt(AppConst.SEASON_NUMBER)).intValue() == -1 || Integer.valueOf(((JSONObject) jsonArraySeasons.get(k)).getInt(AppConst.SEASON_NUMBER)).intValue() == 0) {
//                        seasonsDetailCallback.setSeasonNumber(Integer.valueOf(-1));
//                    } else {
//                        seasonsDetailCallback.setSeasonNumber(Integer.valueOf(((JSONObject) jsonArraySeasons.get(k)).getInt(AppConst.SEASON_NUMBER)));
//                    }
//                    if (((JSONObject) jsonArraySeasons.get(k)).getString("cover") == null || ((JSONObject) jsonArraySeasons.get(k)).getString("cover").isEmpty()) {
//                        seasonsDetailCallback.setCover("");
//                    } else {
//                        seasonsDetailCallback.setCover(((JSONObject) jsonArraySeasons.get(k)).getString("cover"));
//                    }
//                    if (((JSONObject) jsonArraySeasons.get(k)).getString("cover_big") == null || ((JSONObject) jsonArraySeasons.get(k)).getString("cover_big").isEmpty()) {
//                        seasonsDetailCallback.setCoverBig("");
//                    } else {
//                        seasonsDetailCallback.setCoverBig(((JSONObject) jsonArraySeasons.get(k)).getString("cover_big"));
//                    }
//                    this.seasonsDetailCallbacks.add(seasonsDetailCallback);
//                    k++;
//                }
//                if (jsonArrayEpisodes != null) {
//                    iterator = jsonArrayEpisodes.keys();
//                    while (iterator.hasNext()) {
//                        key = (String) iterator.next();
//                        if (jsonArrayEpisodes.get(key) instanceof JSONArray) {
//                            jsonArrayEpisode = new JSONArray(jsonArrayEpisodes.get(key).toString());
//                            for (i = 0; i < jsonArrayEpisode.length(); i++) {
//                                jsonObjectEpisodeDetails = jsonArrayEpisode.getJSONObject(i);
//                                getEpisdoeDetailsCallback = new GetEpisdoeDetailsCallback();
//                                if (jsonObjectEpisodeDetails.getString("id") == null || jsonObjectEpisodeDetails.getString("id").isEmpty()) {
//                                    getEpisdoeDetailsCallback.setId("");
//                                } else {
//                                    getEpisdoeDetailsCallback.setId(jsonObjectEpisodeDetails.getString("id"));
//                                }
//                                if (jsonObjectEpisodeDetails.getInt("season") == -1 || jsonObjectEpisodeDetails.getInt("season") == 0) {
//                                    getEpisdoeDetailsCallback.setId("");
//                                } else {
//                                    getEpisdoeDetailsCallback.setSeasonNumber(Integer.valueOf(jsonObjectEpisodeDetails.getInt("season")));
//                                }
//                                if (jsonObjectEpisodeDetails.getString("title") == null || jsonObjectEpisodeDetails.getString("title").isEmpty()) {
//                                    getEpisdoeDetailsCallback.setTitle("");
//                                } else {
//                                    getEpisdoeDetailsCallback.setTitle(jsonObjectEpisodeDetails.getString("title"));
//                                }
//                                if (jsonObjectEpisodeDetails.getString("direct_source") == null || jsonObjectEpisodeDetails.getString("direct_source").isEmpty()) {
//                                    getEpisdoeDetailsCallback.setDirectSource("");
//                                } else {
//                                    getEpisdoeDetailsCallback.setDirectSource(jsonObjectEpisodeDetails.getString("direct_source"));
//                                }
//                                if (jsonObjectEpisodeDetails.getString("added") == null || jsonObjectEpisodeDetails.getString("added").isEmpty()) {
//                                    getEpisdoeDetailsCallback.setAdded("");
//                                } else {
//                                    getEpisdoeDetailsCallback.setAdded(jsonObjectEpisodeDetails.getString("added"));
//                                }
//                                if (jsonObjectEpisodeDetails.getString("custom_sid") == null || jsonObjectEpisodeDetails.getString("custom_sid").isEmpty()) {
//                                    getEpisdoeDetailsCallback.setCustomSid("");
//                                } else {
//                                    getEpisdoeDetailsCallback.setCustomSid(jsonObjectEpisodeDetails.getString("custom_sid"));
//                                }
//                                if (jsonObjectEpisodeDetails.getString("container_extension") == null || jsonObjectEpisodeDetails.getString("container_extension").isEmpty()) {
//                                    getEpisdoeDetailsCallback.setContainerExtension("");
//                                } else {
//                                    getEpisdoeDetailsCallback.setContainerExtension(jsonObjectEpisodeDetails.getString("container_extension"));
//                                }
//                                this.episdoeDetailsCallbacksList.add(getEpisdoeDetailsCallback);
//                            }
//                        }
//                    }
//                }
//            } catch (JSONException e22) {
//                e22.printStackTrace();
//            }
//        }
//        if (this.context != null) {
//            onFinish();
//            this.myRecyclerView.setHasFixedSize(true);
//            this.mLayoutManager = new LinearLayoutManager(this.context);
//            if ((getResources().getConfiguration().screenLayout & 15) == 3) {
//                this.mLayoutManager = new GridLayoutManager(this, 2);
//            } else {
//                this.mLayoutManager = new GridLayoutManager(this, 2);
//            }
//            this.myRecyclerView.setLayoutManager(this.mLayoutManager);
//            this.myRecyclerView.setVisibility(View.VISIBLE);
//            if (this.episdoeDetailsCallbacksList == null || this.seasonsDetailCallbacks == null || this.seasonsDetailCallbacks.size() <= 0) {
//                this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
//                this.myRecyclerView.setAdapter(this.mAdapter);
//                this.tvNoRecordFound.setVisibility(View.VISIBLE);
//                this.tvNoRecordFound.setText(getResources().getString(R.string.no_season_dound));
//                return;
//            }
//            this.mAdapter = new SeasonsAdapter(this.episdoeDetailsCallbacksList, this.seasonsDetailCallbacks, this.context, this.seriesName);
//            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
//            this.myRecyclerView.setAdapter(this.mAdapter);
//        }
//    }


    public void getSeriesEpisodeInfo(JsonElement jsonElement) {
        if (jsonElement != null) {
            JSONObject jsonObject;
            JSONObject jsonArrayEpisodes;
            Iterator<String> iterator;
            String key;
            JSONArray jsonArrayEpisode;
            int i;
            JSONObject jsonObjectEpisodeDetails;
            GetEpisdoeDetailsCallback getEpisdoeDetailsCallback;
            SeasonsDetailCallback seasonsDetailCallback;
            try {
                jsonObject = new JSONObject(jsonElement.toString());
                jsonArrayEpisodes = null;
                String seasons = jsonObject.getString("seasons");
                if (seasons.equals("null") || seasons.equals("[]")) {
                    if (jsonObject.getJSONObject("episodes") != null) {
                        jsonArrayEpisodes = jsonObject.getJSONObject("episodes");
                    }
                    this.episdoeDetailsCallbacksList.clear();
                    if (jsonArrayEpisodes != null) {
                        iterator = jsonArrayEpisodes.keys();
                        while (iterator.hasNext()) {
                            key = (String) iterator.next();
                            boolean seanValue = true;
                            if (jsonArrayEpisodes.get(key) instanceof JSONArray) {
                                jsonArrayEpisode = new JSONArray(jsonArrayEpisodes.get(key).toString());
                                for (i = 0; i < jsonArrayEpisode.length(); i++) {
                                    jsonObjectEpisodeDetails = jsonArrayEpisode.getJSONObject(i);
                                    getEpisdoeDetailsCallback = new GetEpisdoeDetailsCallback();
                                    seasonsDetailCallback = new SeasonsDetailCallback();
                                    if (jsonObjectEpisodeDetails.getString("id") == null || jsonObjectEpisodeDetails.getString("id").isEmpty()) {
                                        getEpisdoeDetailsCallback.setId("");
                                    } else {
                                        getEpisdoeDetailsCallback.setId(jsonObjectEpisodeDetails.getString("id"));
                                    }
                                    if (jsonObjectEpisodeDetails.getInt("season") == -1 || jsonObjectEpisodeDetails.getInt("season") == 0) {
                                        getEpisdoeDetailsCallback.setId("");
                                    } else {
                                        getEpisdoeDetailsCallback.setSeasonNumber(Integer.valueOf(jsonObjectEpisodeDetails.getInt("season")));
                                    }
                                    if (seanValue) {
                                        seanValue = false;
                                        if (jsonObjectEpisodeDetails.getInt("season") == -1 || jsonObjectEpisodeDetails.getInt("season") == 0) {
                                            seasonsDetailCallback.setSeasonNumber(Integer.valueOf(-1));
                                        } else {
                                            seasonsDetailCallback.setSeasonNumber(Integer.valueOf(jsonObjectEpisodeDetails.getInt("season")));
                                        }
                                        seasonsDetailCallback.setAirDate("");
                                        seasonsDetailCallback.setEpisodeCount(Integer.valueOf(-1));
                                        seasonsDetailCallback.setId(Integer.valueOf(-1));
                                        seasonsDetailCallback.setName("");
                                        seasonsDetailCallback.setOverview("");
                                        seasonsDetailCallback.setCoverBig("");
                                        seasonsDetailCallback.setCoverBig("");
                                        this.seasonsDetailCallbacks.add(seasonsDetailCallback);
                                    }
                                    if (jsonObjectEpisodeDetails.getString("title") == null || jsonObjectEpisodeDetails.getString("title").isEmpty()) {
                                        getEpisdoeDetailsCallback.setTitle("");
                                    } else {
                                        getEpisdoeDetailsCallback.setTitle(jsonObjectEpisodeDetails.getString("title"));
                                    }
                                    if (jsonObjectEpisodeDetails.getString("direct_source") == null || jsonObjectEpisodeDetails.getString("direct_source").isEmpty()) {
                                        getEpisdoeDetailsCallback.setDirectSource("");
                                    } else {
                                        getEpisdoeDetailsCallback.setDirectSource(jsonObjectEpisodeDetails.getString("direct_source"));
                                    }
                                    if (jsonObjectEpisodeDetails.getString("added") == null || jsonObjectEpisodeDetails.getString("added").isEmpty()) {
                                        getEpisdoeDetailsCallback.setAdded("");
                                    } else {
                                        getEpisdoeDetailsCallback.setAdded(jsonObjectEpisodeDetails.getString("added"));
                                    }
                                    if (jsonObjectEpisodeDetails.getString("custom_sid") == null || jsonObjectEpisodeDetails.getString("custom_sid").isEmpty()) {
                                        getEpisdoeDetailsCallback.setCustomSid("");
                                    } else {
                                        getEpisdoeDetailsCallback.setCustomSid(jsonObjectEpisodeDetails.getString("custom_sid"));
                                    }
                                    if (jsonObjectEpisodeDetails.getString("container_extension") == null || jsonObjectEpisodeDetails.getString("container_extension").isEmpty()) {
                                        getEpisdoeDetailsCallback.setContainerExtension("");
                                    } else {
                                        getEpisdoeDetailsCallback.setContainerExtension(jsonObjectEpisodeDetails.getString("container_extension"));
                                    }
                                    this.episdoeDetailsCallbacksList.add(getEpisdoeDetailsCallback);
                                }
                                continue;
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonObject = new JSONObject(jsonElement.toString());
                jsonArrayEpisodes = null;
                JSONObject jsonArraySeasons = null;
                if (jsonObject.getJSONObject("episodes") != null) {
                    jsonArraySeasons = jsonObject.getJSONObject("seasons");
                }
                if (jsonObject.getJSONObject("episodes") != null) {
                    jsonArrayEpisodes = jsonObject.getJSONObject("episodes");
                }
                this.episdoeDetailsCallbacksList.clear();
                Iterator<?> keys = jsonArraySeasons.keys();
                while (keys.hasNext()) {
                    key = (String) keys.next();
                    if (jsonArraySeasons.get(key) instanceof JSONObject) {
                        seasonsDetailCallback = new SeasonsDetailCallback();
                        if (((String) ((JSONObject) jsonArraySeasons.get(key)).get("air_date")) == null || ((String) ((JSONObject) jsonArraySeasons.get(key)).get("air_date")).isEmpty()) {
                            seasonsDetailCallback.setAirDate("");
                        } else {
                            seasonsDetailCallback.setAirDate((String) ((JSONObject) jsonArraySeasons.get(key)).get("air_date"));
                        }
                        if (((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("episode_count")) == null || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("episode_count")).intValue() == -1 || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("episode_count")).intValue() == 0) {
                            seasonsDetailCallback.setEpisodeCount(Integer.valueOf(-1));
                        } else {
                            seasonsDetailCallback.setEpisodeCount((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("episode_count"));
                        }
                        if (((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("id")) == null || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("id")).intValue() == -1 || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("id")).intValue() == 0) {
                            seasonsDetailCallback.setId(Integer.valueOf(-1));
                        } else {
                            seasonsDetailCallback.setId((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("id"));
                        }
                        if (((String) ((JSONObject) jsonArraySeasons.get(key)).get("name")) == null || ((String) ((JSONObject) jsonArraySeasons.get(key)).get("name")).isEmpty()) {
                            seasonsDetailCallback.setName("");
                        } else {
                            seasonsDetailCallback.setName((String) ((JSONObject) jsonArraySeasons.get(key)).get("name"));
                        }
                        if (((String) ((JSONObject) jsonArraySeasons.get(key)).get("overview")) == null || ((String) ((JSONObject) jsonArraySeasons.get(key)).get("overview")).isEmpty()) {
                            seasonsDetailCallback.setOverview("");
                        } else {
                            seasonsDetailCallback.setOverview((String) ((JSONObject) jsonArraySeasons.get(key)).get("overview"));
                        }
                        if (((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("season_number")) == null || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("season_number")).intValue() == -1 || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("season_number")).intValue() == 0) {
                            seasonsDetailCallback.setSeasonNumber(Integer.valueOf(-1));
                        } else {
                            seasonsDetailCallback.setSeasonNumber((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("season_number"));
                        }
                        if (((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover")) == null || ((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover")).isEmpty()) {
                            seasonsDetailCallback.setCover("");
                        } else {
                            seasonsDetailCallback.setCover((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover"));
                        }
                        if (((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover_big")) == null || ((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover_big")).isEmpty()) {
                            seasonsDetailCallback.setCoverBig("");
                        } else {
                            seasonsDetailCallback.setCoverBig((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover_big"));
                        }
                        this.seasonsDetailCallbacks.add(seasonsDetailCallback);
                    }
                }
                if (jsonArrayEpisodes != null) {
                    iterator = jsonArrayEpisodes.keys();
                    while (iterator.hasNext()) {
                        key = (String) iterator.next();
                        if (jsonArrayEpisodes.get(key) instanceof JSONArray) {
                            jsonArrayEpisode = new JSONArray(jsonArrayEpisodes.get(key).toString());
                            for (i = 0; i < jsonArrayEpisode.length(); i++) {
                                jsonObjectEpisodeDetails = jsonArrayEpisode.getJSONObject(i);
                                getEpisdoeDetailsCallback = new GetEpisdoeDetailsCallback();
                                if (jsonObjectEpisodeDetails.getString("id") == null || jsonObjectEpisodeDetails.getString("id").isEmpty()) {
                                    getEpisdoeDetailsCallback.setId("");
                                } else {
                                    getEpisdoeDetailsCallback.setId(jsonObjectEpisodeDetails.getString("id"));
                                }
                                if (jsonObjectEpisodeDetails.getInt("season") == -1 || jsonObjectEpisodeDetails.getInt("season") == 0) {
                                    getEpisdoeDetailsCallback.setId("");
                                } else {
                                    getEpisdoeDetailsCallback.setSeasonNumber(Integer.valueOf(jsonObjectEpisodeDetails.getInt("season")));
                                }
                                if (jsonObjectEpisodeDetails.getString("title") == null || jsonObjectEpisodeDetails.getString("title").isEmpty()) {
                                    getEpisdoeDetailsCallback.setTitle("");
                                } else {
                                    getEpisdoeDetailsCallback.setTitle(jsonObjectEpisodeDetails.getString("title"));
                                }
                                if (jsonObjectEpisodeDetails.getString("direct_source") == null || jsonObjectEpisodeDetails.getString("direct_source").isEmpty()) {
                                    getEpisdoeDetailsCallback.setDirectSource("");
                                } else {
                                    getEpisdoeDetailsCallback.setDirectSource(jsonObjectEpisodeDetails.getString("direct_source"));
                                }
                                if (jsonObjectEpisodeDetails.getString("added") == null || jsonObjectEpisodeDetails.getString("added").isEmpty()) {
                                    getEpisdoeDetailsCallback.setAdded("");
                                } else {
                                    getEpisdoeDetailsCallback.setAdded(jsonObjectEpisodeDetails.getString("added"));
                                }
                                if (jsonObjectEpisodeDetails.getString("custom_sid") == null || jsonObjectEpisodeDetails.getString("custom_sid").isEmpty()) {
                                    getEpisdoeDetailsCallback.setCustomSid("");
                                } else {
                                    getEpisdoeDetailsCallback.setCustomSid(jsonObjectEpisodeDetails.getString("custom_sid"));
                                }
                                if (jsonObjectEpisodeDetails.getString("container_extension") == null || jsonObjectEpisodeDetails.getString("container_extension").isEmpty()) {
                                    getEpisdoeDetailsCallback.setContainerExtension("");
                                } else {
                                    getEpisdoeDetailsCallback.setContainerExtension(jsonObjectEpisodeDetails.getString("container_extension"));
                                }
                                this.episdoeDetailsCallbacksList.add(getEpisdoeDetailsCallback);
                            }
                        }
                    }
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
            try {
                jsonObject = new JSONObject(jsonElement.toString());
                jsonArrayEpisodes = null;
                JSONArray jsonArraySeasons2 = jsonObject.getJSONArray("seasons");
                if (jsonObject.getJSONObject("episodes") != null) {
                    jsonArrayEpisodes = jsonObject.getJSONObject("episodes");
                }
                this.episdoeDetailsCallbacksList.clear();
                int k = 0;
                while (k < jsonArraySeasons2.length()) {
                    seasonsDetailCallback = new SeasonsDetailCallback();
                    String airDate = ((JSONObject) jsonArraySeasons2.get(k)).getString("air_date");
                    if (((JSONObject) jsonArraySeasons2.get(k)).getString("air_date") == null || ((JSONObject) jsonArraySeasons2.get(k)).getString("air_date").isEmpty()) {
                        seasonsDetailCallback.setAirDate("");
                    } else {
                        seasonsDetailCallback.setAirDate(((JSONObject) jsonArraySeasons2.get(k)).getString("air_date"));
                    }
                    if (((JSONObject) jsonArraySeasons2.get(k)).getString("episode_count") == null || Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("episode_count")).intValue() == -1 || Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("episode_count")).intValue() == 0) {
                        seasonsDetailCallback.setEpisodeCount(Integer.valueOf(-1));
                    } else {
                        seasonsDetailCallback.setEpisodeCount(Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("episode_count")));
                    }
                    if (Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("id")) == null || Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("id")).intValue() == -1 || Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("id")).intValue() == 0) {
                        seasonsDetailCallback.setId(Integer.valueOf(-1));
                    } else {
                        seasonsDetailCallback.setId(Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("id")));
                    }
                    if (((JSONObject) jsonArraySeasons2.get(k)).getString("name") == null || ((JSONObject) jsonArraySeasons2.get(k)).getString("name").isEmpty()) {
                        seasonsDetailCallback.setName("");
                    } else {
                        seasonsDetailCallback.setName(((JSONObject) jsonArraySeasons2.get(k)).getString("name"));
                    }
                    if (((JSONObject) jsonArraySeasons2.get(k)).getString("overview") == null || ((JSONObject) jsonArraySeasons2.get(k)).getString("overview").isEmpty()) {
                        seasonsDetailCallback.setOverview("");
                    } else {
                        seasonsDetailCallback.setOverview(((JSONObject) jsonArraySeasons2.get(k)).getString("overview"));
                    }
                    if (Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("season_number")) == null || Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("season_number")).intValue() == -1 || Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("season_number")).intValue() == 0) {
                        seasonsDetailCallback.setSeasonNumber(Integer.valueOf(-1));
                    } else {
                        seasonsDetailCallback.setSeasonNumber(Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("season_number")));
                    }
                    if (((JSONObject) jsonArraySeasons2.get(k)).getString("cover") == null || ((JSONObject) jsonArraySeasons2.get(k)).getString("cover").isEmpty()) {
                        seasonsDetailCallback.setCover("");
                    } else {
                        seasonsDetailCallback.setCover(((JSONObject) jsonArraySeasons2.get(k)).getString("cover"));
                    }
                    if (((JSONObject) jsonArraySeasons2.get(k)).getString("cover_big") == null || ((JSONObject) jsonArraySeasons2.get(k)).getString("cover_big").isEmpty()) {
                        seasonsDetailCallback.setCoverBig("");
                    } else {
                        seasonsDetailCallback.setCoverBig(((JSONObject) jsonArraySeasons2.get(k)).getString("cover_big"));
                    }
                    this.seasonsDetailCallbacks.add(seasonsDetailCallback);
                    k++;
                }
                if (jsonArrayEpisodes != null) {
                    iterator = jsonArrayEpisodes.keys();
                    while (iterator.hasNext()) {
                        key = (String) iterator.next();
                        if (jsonArrayEpisodes.get(key) instanceof JSONArray) {
                            jsonArrayEpisode = new JSONArray(jsonArrayEpisodes.get(key).toString());
                            for (i = 0; i < jsonArrayEpisode.length(); i++) {
                                jsonObjectEpisodeDetails = jsonArrayEpisode.getJSONObject(i);
                                getEpisdoeDetailsCallback = new GetEpisdoeDetailsCallback();
                                if (jsonObjectEpisodeDetails.getString("id") == null || jsonObjectEpisodeDetails.getString("id").isEmpty()) {
                                    getEpisdoeDetailsCallback.setId("");
                                } else {
                                    getEpisdoeDetailsCallback.setId(jsonObjectEpisodeDetails.getString("id"));
                                }
                                if (jsonObjectEpisodeDetails.getInt("season") == -1 || jsonObjectEpisodeDetails.getInt("season") == 0) {
                                    getEpisdoeDetailsCallback.setId("");
                                } else {
                                    getEpisdoeDetailsCallback.setSeasonNumber(Integer.valueOf(jsonObjectEpisodeDetails.getInt("season")));
                                }
                                if (jsonObjectEpisodeDetails.getString("title") == null || jsonObjectEpisodeDetails.getString("title").isEmpty()) {
                                    getEpisdoeDetailsCallback.setTitle("");
                                } else {
                                    getEpisdoeDetailsCallback.setTitle(jsonObjectEpisodeDetails.getString("title"));
                                }
                                if (jsonObjectEpisodeDetails.getString("direct_source") == null || jsonObjectEpisodeDetails.getString("direct_source").isEmpty()) {
                                    getEpisdoeDetailsCallback.setDirectSource("");
                                } else {
                                    getEpisdoeDetailsCallback.setDirectSource(jsonObjectEpisodeDetails.getString("direct_source"));
                                }
                                if (jsonObjectEpisodeDetails.getString("added") == null || jsonObjectEpisodeDetails.getString("added").isEmpty()) {
                                    getEpisdoeDetailsCallback.setAdded("");
                                } else {
                                    getEpisdoeDetailsCallback.setAdded(jsonObjectEpisodeDetails.getString("added"));
                                }
                                if (jsonObjectEpisodeDetails.getString("custom_sid") == null || jsonObjectEpisodeDetails.getString("custom_sid").isEmpty()) {
                                    getEpisdoeDetailsCallback.setCustomSid("");
                                } else {
                                    getEpisdoeDetailsCallback.setCustomSid(jsonObjectEpisodeDetails.getString("custom_sid"));
                                }
                                if (jsonObjectEpisodeDetails.getString("container_extension") == null || jsonObjectEpisodeDetails.getString("container_extension").isEmpty()) {
                                    getEpisdoeDetailsCallback.setContainerExtension("");
                                } else {
                                    getEpisdoeDetailsCallback.setContainerExtension(jsonObjectEpisodeDetails.getString("container_extension"));
                                }
                                this.episdoeDetailsCallbacksList.add(getEpisdoeDetailsCallback);
                            }
                        }
                    }
                }
            } catch (JSONException e22) {
                e22.printStackTrace();
            }
        }
        if (this.context != null) {
            onFinish();
            this.myRecyclerView.setHasFixedSize(true);
            this.mLayoutManager = new LinearLayoutManager(this.context);
            if ((getResources().getConfiguration().screenLayout & 15) == 3) {
                this.mLayoutManager = new GridLayoutManager(this, 2);
            } else {
                this.mLayoutManager = new GridLayoutManager(this, 1);
            }
            this.myRecyclerView.setLayoutManager(this.mLayoutManager);
            this.myRecyclerView.setVisibility(0);
            if (this.episdoeDetailsCallbacksList == null || this.seasonsDetailCallbacks == null || this.seasonsDetailCallbacks.size() <= 0) {
                this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
                this.myRecyclerView.setAdapter(this.mAdapter);
                this.tvNoRecordFound.setText(getResources().getString(R.string.no_season_dound));
                this.tvNoRecordFound.setVisibility(View.VISIBLE);
                return;
            }

            this.mAdapter = new SeasonsAdapter(episdoeDetailsCallbacksList, seasonsDetailCallbacks, this.context,"");
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            this.myRecyclerView.setAdapter(this.mAdapter);
        }
    }

    public void seriesError(String message) {
    }

    public void atStart() {
    }

    public void onFinish() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
    }

    public void onFailed(String errorMessage) {
    }

    public void onBackPressed() {
        if (this.frameLayout != null) {
            this.frameLayout.setVisibility(View.GONE);
        }
        if (this.myRecyclerView != null) {
            this.myRecyclerView.setClickable(true);
        }
        if (!(this.mAdapter == null || pbPagingLoader1 == null)) {
            this.mAdapter.setVisibiltygone(pbPagingLoader1);
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    public void onResume() {
        super.onResume();
        getWindow().setFlags(1024, 1024);
        if (this.frameLayout != null) {
            this.frameLayout.setVisibility(View.GONE);
        }
        if (this.mAdapter != null) {
            this.mAdapter.setVisibiltygone(pbPagingLoader1);
        }
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        if (this.loginPreferencesAfterLogin.getString("username", "").equals("") && this.loginPreferencesAfterLogin.getString("password", "").equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (this.context == null) {
        }
    }

    public void onClick(View view) {
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.toolbar.inflateMenu(R.menu.menu_search);
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
        if (id == R.id.action_logout1 && this.context != null) {
            new Builder(this.context, R.style.AlertDialogCustom).setTitle(getResources().getString(R.string.logout_title)).setMessage(getResources().getString(R.string.logout_message)).setPositiveButton(17039379, new C16722()).setNegativeButton(17039369, new C16711()).show();
        }
        if (id == R.id.menu_load_channels_vod1) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle("Confirmação para Atualização...");
            alertDialog.setMessage("Confirma o Procedimento?");
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("Sim", new C16733());
            alertDialog.setNegativeButton("Não", new C16744());
            alertDialog.show();
        }
        if (id == R.id.menu_load_tv_guide1) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle("Confirmação para Atualização...");
            alertDialog.setMessage("Confirma o Procedimento?");
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("Sim", new C16755());
            alertDialog.setNegativeButton("Não", new C16766());
            alertDialog.show();
        }
        if (id == R.id.action_search) {
            this.searchView = (SearchView) MenuItemCompat.getActionView(item);
            this.searchView.setQueryHint(getResources().getString(R.string.search_seasons));
            this.searchView.setIconifiedByDefault(false);
            this.searchView.setOnQueryTextListener(new C16777());
        }
        return super.onOptionsItemSelected(item);
    }
}
