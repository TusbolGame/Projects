package com.liveitandroid.liveit.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.view.interfaces.SeriesInterface;
import com.google.gson.JsonElement;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.AdapterSectionRecycler;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.callback.GetEpisdoeDetailsCallback;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.model.database.DatabaseUpdatedStatusDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.presenter.SeriesPresenter;
import com.liveitandroid.liveit.presenter.XMLTVPresenter;
import com.liveitandroid.liveit.view.adapter.EpisodeDetailAdapter;
import com.liveitandroid.liveit.view.adapter.SeasonsAdapter;
import com.liveitandroid.liveit.view.adapter.SeriesAdapter;
import com.liveitandroid.liveit.view.adapter.VodSubCatAdpaterNew;
import com.liveitandroid.liveit.view.interfaces.SeriesInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EpisodeDetailActivity extends AppCompatActivity implements OnClickListener, SeriesInterface {
    static ProgressBar pbPagingLoader1;
    private static ArrayList<LiveStreamCategoryIdDBModel> subCategoryList = new ArrayList();
    private static ArrayList<LiveStreamCategoryIdDBModel> subCategoryListFinal = new ArrayList();
    private static ArrayList<LiveStreamCategoryIdDBModel> subCategoryListFinal_menu = new ArrayList();
    private SharedPreferences SharedPreferencesSort;
    private Editor SharedPreferencesSortEditor;
    int actionBarHeight;
    @BindView(R.id.main_layout)
    LinearLayout activityLogin;
    private AdapterSectionRecycler adapterRecycler;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    ArrayList<LiveStreamCategoryIdDBModel> categoriesList;
    private PopupWindow changeSortPopUp;
    TextView clientNameTv;
    @BindView(R.id.content_drawer)
    RelativeLayout contentDrawer;
    private Context context;
    DatabaseHandler database;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel();
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel();
    private Editor editor;
    private ArrayList<GetEpisdoeDetailsCallback> episdoeDetailsCallbacksList = new ArrayList();
    private final ArrayList<GetEpisdoeDetailsCallback> episdoeDetailsCallbacksListDefault = new ArrayList();
    private final List<GetEpisdoeDetailsCallback> episdoeDetailsCallbacksListDefault_seasons = new ArrayList();
    private ArrayList<GetEpisdoeDetailsCallback> episdoeDetailsCallbacksListRefined = new ArrayList();
    private List<GetEpisdoeDetailsCallback> episdoeDetailsCallbacksListRefined_seasons = new ArrayList();
    private List<GetEpisdoeDetailsCallback> episdoeDetailsList = new ArrayList();
    private List<GetEpisdoeDetailsCallback> episdoeDetailsListFinal = new ArrayList();
    private EpisodeDetailAdapter episodeDetailAdapter;
    private ArrayList<LiveStreamsDBModel> favouriteStreams = new ArrayList();
    private String getActiveLiveStreamCategoryId = "";
    private String getActiveLiveStreamCategoryName = "";
    GridLayoutManager gridLayoutManager;
    boolean isEpisode = true;
    private boolean isSubcaetgroy = false;
    boolean isSubcaetgroyAvail = false;
    private LayoutManager layoutManager;
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesSharedPref;
    private SeasonsAdapter mAdapter;
    private LayoutManager mLayoutManager;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;
    @BindView(R.id.rl_sub_cat)
    RelativeLayout rl_sub_cat;
    SearchView searchView;
    private String seasonCaverBig = "";
    private int seasonNumber = -1;
    private SeasonsAdapter seasonsAdapter;
    private String seriesCover = "";
    private String seriesId = "";
    private String seriesName = "";
    @BindView(R.id.tv_settings)
    TextView seriesNameTV;
    private SeriesPresenter seriesPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TypedValue tv;
    @BindView(R.id.tv_no_record_found)
    TextView tvNoRecordFound;
    @BindView(R.id.tv_noStream)
    TextView tvNoStream;
    @BindView(R.id.tv_view_provider)
    TextView tvViewProvider;
    private String userName = "";
    private String userPassword = "";
    private SeriesAdapter vodAdapter;
    private VodSubCatAdpaterNew vodSubCatAdpaterNew;
    private XMLTVPresenter xmltvPresenter;

    class C15951 implements DialogInterface.OnClickListener {
        C15951() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C15962 implements DialogInterface.OnClickListener {
        C15962() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.logoutUser(EpisodeDetailActivity.this.context);
        }
    }

    class C15973 implements DialogInterface.OnClickListener {
        C15973() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsAndVod(EpisodeDetailActivity.this.context);
        }
    }

    class C15984 implements DialogInterface.OnClickListener {
        C15984() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C15995 implements DialogInterface.OnClickListener {
        C15995() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadTvGuid(EpisodeDetailActivity.this.context);
        }
    }

    class C16006 implements DialogInterface.OnClickListener {
        C16006() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C16017 implements OnQueryTextListener {
        C16017() {
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            EpisodeDetailActivity.this.tvNoRecordFound.setVisibility(View.GONE);
            if (!(EpisodeDetailActivity.this.episodeDetailAdapter == null || EpisodeDetailActivity.this.tvNoRecordFound == null || EpisodeDetailActivity.this.tvNoRecordFound.getVisibility() == 0)) {
                EpisodeDetailActivity.this.episodeDetailAdapter.filter(newText, EpisodeDetailActivity.this.tvNoRecordFound);
            }
            return false;
        }
    }

    class C16028 implements OnClickListener {
        C16028() {
        }

        public void onClick(View view) {
            EpisodeDetailActivity.this.changeSortPopUp.dismiss();
        }
    }
    private SessionManager mSessionManager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);
        ButterKnife.bind(this);
        getWindow().setFlags(1024, 1024);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        if (this.appbarToolbar != null) {
            this.appbarToolbar.setBackground(getResources().getDrawable(R.drawable.vod_backgound));
        }
        this.SharedPreferencesSort = getSharedPreferences(AppConst.LOGIN_PREF_SORT_EPISODES, 0);
        this.SharedPreferencesSortEditor = this.SharedPreferencesSort.edit();
        if (this.SharedPreferencesSort.getString(AppConst.LOGIN_PREF_SORT, "").equals("")) {
            this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.PASSWORD_UNSET);
            this.SharedPreferencesSortEditor.commit();
        }
        setGridView();
        initializeSideDrawer();
        initializeV();
        mSessionManager = new SessionManager(this.context);
    }

    private void setGridView() {
        if (this.myRecyclerView != null) {
            this.context = this;
            this.myRecyclerView.setHasFixedSize(true);
            this.layoutManager = new GridLayoutManager(this.context, Utils.getNumberOfColumns(this.context) + 1);
            this.myRecyclerView.setLayoutManager(this.layoutManager);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    private void initializeSideDrawer() {
        changeStatusBarColor();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    private void initializeV() {
        this.context = this;
        this.seriesPresenter = new SeriesPresenter(this.context, this);
        Intent intent = getIntent();
        if (intent != null) {
            this.seriesId = intent.getStringExtra(AppConst.SERIES_SERIES_ID);
            this.seriesCover = intent.getStringExtra(AppConst.SERIES_COVER);
            this.seriesName = intent.getStringExtra(AppConst.SERIES_NAME);
            this.seasonCaverBig = intent.getStringExtra(AppConst.SEASON_COVER_BIG);
            this.seasonNumber = intent.getIntExtra(AppConst.SEASON_NUMBER, -1);
            this.episdoeDetailsList = (List) getIntent().getSerializableExtra(AppConst.EPISODELIST);
            if (!(this.seriesNameTV == null || this.seriesName == null || this.seriesName.isEmpty())) {
                this.seriesNameTV.setText(this.seriesName);
                this.seriesNameTV.setSelected(true);
            }
            if (this.seasonNumber != -1 && this.seasonNumber != 0 && this.episdoeDetailsList != null && this.episdoeDetailsList.size() > 0) {
                if (this.pbLoader != null) {
                    this.pbLoader.setVisibility(4);
                }
                this.isEpisode = false;
                setEpisode(this.episdoeDetailsList, this.seasonNumber, this.seasonCaverBig);
            } else if (this.seriesId == null) {
                onFinish();
                if (this.tvNoStream != null) {
                    this.tvNoStream.setVisibility(0);
                }
            }
        }
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        String username = this.loginPreferencesAfterLogin.getString("username", "");
        String password = this.loginPreferencesAfterLogin.getString("password", "");
        if (this.seriesId != null && !this.seriesId.isEmpty() && this.seriesPresenter != null && username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            this.isEpisode = true;
            this.seriesPresenter.getSeriesEpisode(username, password, this.seriesId);
        }
    }

    private void setEpisode(List<GetEpisdoeDetailsCallback> episdoeDetailsList, int seasonNumber, String seasonCaverBig) {
        if (this.episdoeDetailsListFinal != null) {
            this.episdoeDetailsListFinal.clear();
        }
        for (GetEpisdoeDetailsCallback episodeItem : episdoeDetailsList) {
            if (episodeItem.getSeasonNumber().intValue() == seasonNumber) {
                this.episdoeDetailsListFinal.add(episodeItem);
                this.episdoeDetailsCallbacksListDefault_seasons.add(episodeItem);
            }
        }
        setSeasonsList();
    }

    public void setSeasonsList() {
        String sort = this.SharedPreferencesSort.getString(AppConst.LOGIN_PREF_SORT, "");
        if (sort.equals("1")) {
            AppConst.SORT_EPISODES = AppConst.SORT_EPISODES_LASTADDED;
            Collections.sort(this.episdoeDetailsListFinal, GetEpisdoeDetailsCallback.episodeComparator);
            this.episdoeDetailsCallbacksListRefined_seasons = this.episdoeDetailsListFinal;
        } else if (sort.equals(AppConst.DB_EPG_ID)) {
            AppConst.SORT_EPISODES = AppConst.SORT_EPISODES_A_TO_Z;
            Collections.sort(this.episdoeDetailsListFinal, GetEpisdoeDetailsCallback.episodeComparator);
            this.episdoeDetailsCallbacksListRefined_seasons = this.episdoeDetailsListFinal;
        } else if (sort.equals("3")) {
            AppConst.SORT_EPISODES = AppConst.SORT_EPISODES_Z_TO_A;
            Collections.sort(this.episdoeDetailsListFinal, GetEpisdoeDetailsCallback.episodeComparator);
            this.episdoeDetailsCallbacksListRefined_seasons = this.episdoeDetailsListFinal;
        } else {
            this.episdoeDetailsCallbacksListRefined_seasons = this.episdoeDetailsCallbacksListDefault_seasons;
        }
        if (this.episdoeDetailsCallbacksListRefined_seasons == null || this.myRecyclerView == null || this.episdoeDetailsCallbacksListRefined_seasons.size() == 0) {
            onFinish();
            if (this.tvNoStream != null) {
                this.tvNoStream.setVisibility(0);
                return;
            }
            return;
        }
        onFinish();
        this.episodeDetailAdapter = new EpisodeDetailAdapter(this.episdoeDetailsCallbacksListRefined_seasons, this.context, this.seasonCaverBig);
        this.myRecyclerView.setAdapter(this.episodeDetailAdapter);
        this.episodeDetailAdapter.notifyDataSetChanged();
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
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
    }

    public void onFailed(String errorMessage) {
    }

    public void getSeriesEpisodeInfo(JsonElement jsonElement) {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
        if (jsonElement != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonElement.toString());
                JSONObject jsonArrayEpisodes = null;
                if (jsonObject.getJSONObject("episodes") != null) {
                    jsonArrayEpisodes = jsonObject.getJSONObject("episodes");
                }
                this.episdoeDetailsCallbacksList.clear();
                if (jsonArrayEpisodes != null) {
                    Iterator<String> iterator = jsonArrayEpisodes.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        if (jsonArrayEpisodes.get(key) instanceof JSONArray) {
                            JSONArray jsonArrayEpisode = new JSONArray(jsonArrayEpisodes.get(key).toString());
                            for (int i = 0; i < jsonArrayEpisode.length(); i++) {
                                JSONObject jsonObjectEpisodeDetails = jsonArrayEpisode.getJSONObject(i);
                                GetEpisdoeDetailsCallback getEpisdoeDetailsCallback = new GetEpisdoeDetailsCallback();
                                if (jsonObjectEpisodeDetails.getString("id") == null || jsonObjectEpisodeDetails.getString("id").isEmpty()) {
                                    getEpisdoeDetailsCallback.setId("");
                                } else {
                                    getEpisdoeDetailsCallback.setId(jsonObjectEpisodeDetails.getString("id"));
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
                                this.episdoeDetailsCallbacksListDefault.add(getEpisdoeDetailsCallback);
                            }
                            continue;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        set_episode_data();
    }

    public void set_episode_data() {
        String sort = this.SharedPreferencesSort.getString(AppConst.LOGIN_PREF_SORT, "");
        if (sort.equals("1")) {
            AppConst.SORT_EPISODES = AppConst.SORT_EPISODES_LASTADDED;
            Collections.sort(this.episdoeDetailsCallbacksList, GetEpisdoeDetailsCallback.episodeComparator);
            this.episdoeDetailsCallbacksListRefined = this.episdoeDetailsCallbacksList;
        } else if (sort.equals(AppConst.DB_EPG_ID)) {
            AppConst.SORT_EPISODES = AppConst.SORT_EPISODES_A_TO_Z;
            Collections.sort(this.episdoeDetailsCallbacksList, GetEpisdoeDetailsCallback.episodeComparator);
            this.episdoeDetailsCallbacksListRefined = this.episdoeDetailsCallbacksList;
        } else if (sort.equals("3")) {
            AppConst.SORT_EPISODES = AppConst.SORT_EPISODES_Z_TO_A;
            Collections.sort(this.episdoeDetailsCallbacksList, GetEpisdoeDetailsCallback.episodeComparator);
            this.episdoeDetailsCallbacksListRefined = this.episdoeDetailsCallbacksList;
        } else {
            this.episdoeDetailsCallbacksListRefined = this.episdoeDetailsCallbacksListDefault;
        }
        if (this.episdoeDetailsCallbacksListRefined != null && this.myRecyclerView != null && this.episdoeDetailsCallbacksListRefined.size() != 0) {
            this.episodeDetailAdapter = new EpisodeDetailAdapter(this.episdoeDetailsCallbacksListRefined, this.context, this.seriesCover);
            this.myRecyclerView.setAdapter(this.episodeDetailAdapter);
        } else if (this.tvNoStream != null) {
            this.tvNoStream.setVisibility(0);
        }
    }

    public void seriesError(String message) {
    }

    public void progressBar(ProgressBar pbPagingLoader) {
        pbPagingLoader1 = pbPagingLoader;
    }

    public void onBackPressed() {
        this.myRecyclerView.setClickable(true);
        this.mAdapter = new SeasonsAdapter();
        if (this.mAdapter != null) {
            this.mAdapter.setVisibiltygone(pbPagingLoader1);
        }
        super.onBackPressed();
        //overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    public void onResume() {
        super.onResume();
        getWindow().setFlags(1024, 1024);
        this.mAdapter = new SeasonsAdapter();
        if (this.mAdapter != null) {
            this.mAdapter.setVisibiltygone(pbPagingLoader1);
        }
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        if (this.loginPreferencesAfterLogin.getString("username", "").equals("") && this.loginPreferencesAfterLogin.getString("password", "").equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (this.context == null) {
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.toolbar.inflateMenu(R.menu.menu_search_episodes);
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
        if (id == R.id.action_logout1 && this.context != null) {
            new Builder(this.context, R.style.AlertDialogCustom).setTitle(getResources().getString(R.string.logout_title)).setMessage(getResources().getString(R.string.logout_message)).setPositiveButton(17039379, new C15962()).setNegativeButton(17039369, new C15951()).show();
        }
        if (id == R.id.menu_load_channels_vod1) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle("Confirmação para Atualização...");
            alertDialog.setMessage("Confirma o Procedimento?");
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("Sim", new C15973());
            alertDialog.setNegativeButton("Não", new C15984());
            alertDialog.show();
        }
        if (id == R.id.menu_load_tv_guide1) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle("Confirmação para Atualização...");
            alertDialog.setMessage("Confirma o Procedimento?");
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("Sim", new C15995());
            alertDialog.setNegativeButton("Não", new C16006());
            alertDialog.show();
        }
        if (id == R.id.action_search) {
            this.searchView = (SearchView) MenuItemCompat.getActionView(item);
            this.searchView.setQueryHint(getResources().getString(R.string.search_episodes));
            this.searchView.setIconifiedByDefault(false);
            this.searchView.setOnQueryTextListener(new C16017());
        }
        if (id == R.id.menu_sort) {
            showSortPopup(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortPopup(Activity context) {
        final View layout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.sort_layout, (RelativeLayout) context.findViewById(R.id.rl_password_prompt));
        this.changeSortPopUp = new PopupWindow(context);
        this.changeSortPopUp.setContentView(layout);
        this.changeSortPopUp.setWidth(-1);
        this.changeSortPopUp.setHeight(-1);
        this.changeSortPopUp.setFocusable(true);
        this.changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());
        this.changeSortPopUp.showAtLocation(layout, 17, 0, 0);
        Button savePasswordBT = (Button) layout.findViewById(R.id.bt_save_password);
        Button closedBT = (Button) layout.findViewById(R.id.bt_close);
        final RadioGroup rgRadio = (RadioGroup) layout.findViewById(R.id.rg_radio);
        RadioButton normal = (RadioButton) layout.findViewById(R.id.rb_normal);
        RadioButton last_added = (RadioButton) layout.findViewById(R.id.rb_lastadded);
        RadioButton atoz = (RadioButton) layout.findViewById(R.id.rb_atoz);
        RadioButton ztoa = (RadioButton) layout.findViewById(R.id.rb_ztoa);
        String sort = this.SharedPreferencesSort.getString(AppConst.LOGIN_PREF_SORT, "");
        if (sort.equals("1")) {
            last_added.setChecked(true);
        } else if (sort.equals(AppConst.DB_EPG_ID)) {
            atoz.setChecked(true);
        } else if (sort.equals("3")) {
            ztoa.setChecked(true);
        } else {
            normal.setChecked(true);
        }
        closedBT.setOnClickListener(new C16028());
        savePasswordBT.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RadioButton selectedPlayer1 = (RadioButton) layout.findViewById(rgRadio.getCheckedRadioButtonId());
                if (selectedPlayer1.getText().toString().equals(EpisodeDetailActivity.this.getResources().getString(R.string.sort_last_added))) {
                    EpisodeDetailActivity.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, "1");
                    EpisodeDetailActivity.this.SharedPreferencesSortEditor.commit();
                } else if (selectedPlayer1.getText().toString().equals(EpisodeDetailActivity.this.getResources().getString(R.string.sort_atoz))) {
                    EpisodeDetailActivity.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.DB_EPG_ID);
                    EpisodeDetailActivity.this.SharedPreferencesSortEditor.commit();
                } else if (selectedPlayer1.getText().toString().equals(EpisodeDetailActivity.this.getResources().getString(R.string.sort_ztoa))) {
                    EpisodeDetailActivity.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, "3");
                    EpisodeDetailActivity.this.SharedPreferencesSortEditor.commit();
                } else {
                    EpisodeDetailActivity.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.PASSWORD_UNSET);
                    EpisodeDetailActivity.this.SharedPreferencesSortEditor.commit();
                }
                if (EpisodeDetailActivity.this.isEpisode) {
                    EpisodeDetailActivity.this.set_episode_data();
                } else {
                    EpisodeDetailActivity.this.setSeasonsList();
                }
                EpisodeDetailActivity.this.changeSortPopUp.dismiss();
            }
        });
    }

    private void loadTvGuid() {
        if (this.context != null) {
            SharedPreferences loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            Editor loginPrefsEditor = loginPreferencesAfterLogin.edit();
            if (loginPrefsEditor != null) {
                loginPrefsEditor.putString(AppConst.SKIP_BUTTON_PREF, "autoLoad");
                loginPrefsEditor.commit();
                String skipButton = loginPreferencesAfterLogin.getString(AppConst.SKIP_BUTTON_PREF, "");
                new LiveStreamDBHandler(this.context).makeEmptyEPG();
                startActivity(new Intent(this.context, ImportEPGActivity.class));
            }
        }
    }

    public void hideSystemUi() {
        this.activityLogin.setSystemUiVisibility(4871);
    }

    public void onClick(View view) {
    }
}
