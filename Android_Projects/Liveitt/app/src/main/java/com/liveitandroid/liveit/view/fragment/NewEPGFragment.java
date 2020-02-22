package com.liveitandroid.liveit.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.EpgChannelModel;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.model.database.DatabaseUpdatedStatusDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.view.activity.NewDashboardActivity;
import com.liveitandroid.liveit.view.activity.NewDashboardActivity2;
import com.liveitandroid.liveit.view.activity.SettingsActivity;
import com.liveitandroid.liveit.view.nstplayer.IjkVideoView;
import com.liveitandroid.liveit.view.utility.epg.EPG;
import com.liveitandroid.liveit.view.utility.epg.EPGClickListener;
import com.liveitandroid.liveit.view.utility.epg.EPGData;
import com.liveitandroid.liveit.view.utility.epg.domain.EPGChannel;
import com.liveitandroid.liveit.view.utility.epg.domain.EPGEvent;
import com.liveitandroid.liveit.view.utility.epg.misc.EPGDataListener;
import com.liveitandroid.liveit.view.utility.epg.service.EPGService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class NewEPGFragment extends Fragment implements OnNavigationItemSelectedListener {
    public static final String ACTIVE_LIVE_STREAM_CATEGORY_ID = "";
    public static final String ACTIVE_LIVE_STREAM_CATEGORY_NAME = "";
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    int actionBarHeight;
    @BindView(R.id.app_video_box)
    RelativeLayout app_video_box;
    @BindView(R.id.app_video_loading)
    ProgressBar app_video_loading;
    @BindView(R.id.app_video_status)
    LinearLayout app_video_status;
    @BindView(R.id.app_video_status_text)
    TextView app_video_status_text;
    public Context context;
    @BindView(R.id.current_event)
    TextView currentEvent;
    @BindView(R.id.current_event_description)
    TextView currentEventDescription;
    private TextView currentEventDescriptionTextView;
    private TextView currentEventTextView;
    @BindView(R.id.current_event_time)
    TextView currentEventTime;
    private TextView currentEventTimeTextView;
    private SimpleDateFormat currentTimeFormat;
    DatabaseHandler database;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel();
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel();
    private int devEnterCounter = 0;
    private boolean devMode = false;
    @BindView(R.id.epg)
    EPG epg;
    ArrayList<EpgChannelModel> epgChannelModelList = new ArrayList();
    @BindView(R.id.rl_newepg_fragment)
    RelativeLayout epgFragment;
    @BindView(R.id.ll_epg_view)
    LinearLayout epgView;
    LiveStreamsDBModel favouriteStream = new LiveStreamsDBModel();
    private ArrayList<LiveStreamsDBModel> favouriteStreams = new ArrayList();
    private String getActiveLiveStreamCategoryId;
    private String getActiveLiveStreamCategoryName;
    private LayoutManager layoutManager;
    LiveStreamDBHandler liveStreamDBHandler;
    public SharedPreferences loginPreferencesSharedPref;
    public SharedPreferences loginPreferencesSharedPrefLogin;
    public SharedPreferences loginPreferencesSharedPref_allowed_format;
    public SharedPreferences loginPreferencesSharedPref_opened_video;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    private Handler periodicTaskHandler;
    @BindView(R.id.rl_add_channel_to_fav)
    RelativeLayout rl_add_channel_to_fav;
    SearchView searchView;
    private Toolbar toolbar;
    TypedValue tv;
    @BindView(R.id.tv_no_record_found)
    TextView tvNoRecordFound;
    @BindView(R.id.tv_noStream)
    TextView tvNoStream;
    @BindView(R.id.tv_view_provider)
    TextView tvViewProvider;
    @BindView(R.id.tv_cat_title)
    TextView tv_cat_title;
    Unbinder unbinder;
    @BindView(R.id.video_view)
    IjkVideoView videoView;

    class C19611 implements OnKeyListener {
        C19611() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            return (event.getAction() == 1 || (!(keyCode == 20 || keyCode == 19 || keyCode == 22 || keyCode == 21 || keyCode == 23 || keyCode == 66) || NewEPGFragment.this.epg == null)) ? false : NewEPGFragment.this.epg.onKeyDown(keyCode, event);
        }
    }

    class AnonymousClass1AsyncLoadEPGData extends AsyncTask<Void, Void, EPGData> {
        EPG epg;
        final /* synthetic */ String val$categoryID;
        final /* synthetic */ RelativeLayout val$epgFragment;

        public AnonymousClass1AsyncLoadEPGData(EPG epg, String str, RelativeLayout relativeLayout) {
            this.val$categoryID = str;
            this.val$epgFragment = relativeLayout;
            this.epg = epg;
        }

        protected EPGData doInBackground(Void... voids) {
            return new EPGService(NewEPGFragment.this.context).getData(new EPGDataListener(this.epg), 0, this.val$categoryID);
        }

        protected void onPostExecute(EPGData epgData) {
            int totalChannels = 0;
            if (epgData != null) {
                totalChannels = epgData.getChannelCount();
            }
            this.epg.setEPGData(epgData);
            this.epg.recalculateAndRedraw(null, false, this.val$epgFragment, this.epg);
            if (NewEPGFragment.this.epgView != null && totalChannels > 0) {
                NewEPGFragment.this.epgView.setVisibility(0);
            } else if (NewEPGFragment.this.epgView != null) {
                NewEPGFragment.this.epgView.setVisibility(8);
                NewEPGFragment.this.tvNoRecordFound.setVisibility(0);
                NewEPGFragment.this.tvNoRecordFound.setText(NewEPGFragment.this.getResources().getString(R.string.no_epg_guide_found));
            }
            if (NewEPGFragment.this.pbLoader != null) {
                NewEPGFragment.this.pbLoader.setVisibility(4);
            }
        }
    }

    class C19633 implements OnClickListener {
        C19633() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C19644 implements OnClickListener {
        C19644() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.logoutUser(NewEPGFragment.this.context);
        }
    }

    public static NewEPGFragment newInstance(String category_id, String liveStreamCategoriesName) {
        Bundle args = new Bundle();
        args.putString("ACTIVE_LIVE_STREAM_CATEGORY_ID", category_id);
        args.putString("ACTIVE_LIVE_STREAM_CATEGORY_NAME", liveStreamCategoriesName);
        NewEPGFragment fragment = new NewEPGFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActiveLiveStreamCategoryId = getArguments().getString("ACTIVE_LIVE_STREAM_CATEGORY_ID");
        this.getActiveLiveStreamCategoryName = getArguments().getString("ACTIVE_LIVE_STREAM_CATEGORY_NAME");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_epg_streams, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        ActivityCompat.invalidateOptionsMenu(getActivity());
        setHasOptionsMenu(true);
        setToolbarLogoImagewithSearchView();
        initialize();
        this.currentEvent.setSelected(true);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getView() != null) {
            getView().setOnKeyListener(new C19611());
        }
    }

    void onCreateEPG(boolean clicked) {
        this.loginPreferencesSharedPref = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SELECTED_PLAYER, 0);
        this.loginPreferencesSharedPrefLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        final String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
        this.loginPreferencesSharedPref_allowed_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_ALLOWED_FORMAT, 0);
        String username = this.loginPreferencesSharedPrefLogin.getString("username", "");
        String password = this.loginPreferencesSharedPrefLogin.getString("password", "");
        String allowedFormat = this.loginPreferencesSharedPref_allowed_format.getString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "");
        String serverUrl = this.loginPreferencesSharedPrefLogin.getString(AppConst.LOGIN_PREF_SERVER_URL, AppConst.BASE_URL);
        String mFilePath = "http://" + serverUrl + ":" + this.loginPreferencesSharedPrefLogin.getString(AppConst.LOGIN_PREF_SERVER_PORT, AppConst.BASE_URL) + "/live/" + username + "/" + password + "/";
        this.currentEventTextView = this.currentEvent;
        this.currentEventTimeTextView = this.currentEventTime;
        this.currentEventDescriptionTextView = this.currentEventDescription;
        this.tv_cat_title.setText(this.getActiveLiveStreamCategoryName);
        this.epg.setCurrentEventTextView(this.currentEventTextView);
        this.epg.setCurrentEventTimeTextView(this.currentEventTimeTextView);
        this.epg.setCurrentEventDescriptionTextView(this.currentEventDescriptionTextView);
        this.epg.setVideoPathUrl(mFilePath);
        this.epg.setExtensionType(allowedFormat);
        this.epg.setVideoView(this.videoView);
        this.epg.setLoader(this.app_video_loading);
        this.epg.setVideoStatus(this.app_video_status);
        this.epg.setVideoStatusText(this.app_video_status_text);
        this.periodicTaskHandler = new Handler();
        this.epg.setEPGClickListener(new EPGClickListener() {
            public void onChannelClicked(int channelPosition, EPGChannel epgChannel) {
                int streamID = Integer.parseInt(epgChannel.getStreamID());
                String name = epgChannel.getName();
                String num = epgChannel.getNum();
                String epgChannelId = epgChannel.getEpgChannelID();
                String channelLogo = epgChannel.getImageURL();
                if (NewEPGFragment.this.epg != null) {
                    NewEPGFragment.this.epg.clearEPGImageCache();
                    NewEPGFragment.this.epg.destroyVideoPlayBack();
                }
                NewEPGFragment.this.epg.playVideo(streamID);
                //Utils.playWithPlayer(NewEPGFragment.this.context, selectedPlayer, streamID, "live", num, name, epgChannelId, channelLogo,epgChannel.getCateID());
            }

            public void onEventClicked(int channelPosition, int programPosition, EPGEvent epgEvent) {
                int streamID = Integer.parseInt(epgEvent.getChannel().getStreamID());
                String name = epgEvent.getChannel().getName();
                String num = epgEvent.getChannel().getNum();
                String epgChannelId = epgEvent.getChannel().getEpgChannelID();
                String channelLogo = epgEvent.getChannel().getImageURL();
                NewEPGFragment.this.epg.selectEvent(epgEvent, true);
                if (NewEPGFragment.this.epg != null) {
                    NewEPGFragment.this.epg.clearEPGImageCache();
                    NewEPGFragment.this.epg.destroyVideoPlayBack();
                }
                NewEPGFragment.this.epg.playVideo(streamID);
                //Utils.playWithPlayerEPG(NewEPGFragment.this.context, selectedPlayer, streamID, "live", num, name, epgChannelId, channelLogo,epgEvent.getChannel().getCateID());
            }

            public void onResetButtonClicked() {
                NewEPGFragment.this.epg.recalculateAndRedraw(null, true, NewEPGFragment.this.epgFragment, NewEPGFragment.this.epg);
            }
        });
        if (clicked && this.epg != null) {
            EPGEvent epgEvent = this.epg.getSelectedEvent();
            int streamID = Integer.parseInt(epgEvent.getChannel().getStreamID());
            String name = epgEvent.getChannel().getName();
            String num = epgEvent.getChannel().getNum();
            String epgChannelId = epgEvent.getChannel().getEpgChannelID();
            String channelLogo = epgEvent.getChannel().getImageURL();
            this.epg.selectEvent(epgEvent, true);
            if (this.epg != null) {
                this.epg.clearEPGImageCache();
                this.epg.destroyVideoPlayBack();
            }
            NewEPGFragment.this.epg.playVideo(streamID);
        }
    }

    public void onResume() {
        this.loginPreferencesSharedPref_opened_video = this.context.getSharedPreferences(AppConst.LOGIN_PREF_OPENED_VIDEO, 0);
        int opened_video_id = this.loginPreferencesSharedPref_opened_video.getInt(AppConst.LOGIN_PREF_OPENED_VIDEO_ID, 0);
        if (opened_video_id != 0) {
            Editor loginPreferencesEditor = this.loginPreferencesSharedPref_opened_video.edit();
            loginPreferencesEditor.clear();
            loginPreferencesEditor.apply();
            if (this.epg != null) {
                this.epg.playVideo(opened_video_id);
            }
        }
        super.onResume();
    }

    private void setToolbarLogoImagewithSearchView() {
        this.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (this.context != null && this.toolbar != null) {
            TypedValue tv = new TypedValue();
            if (this.context.getTheme().resolveAttribute(16843499, tv, true)) {
                TypedValue.complexToDimensionPixelSize(tv.data, this.context.getResources().getDisplayMetrics());
            }
            for (int i = 0; i < this.toolbar.getChildCount(); i++) {
                if (this.toolbar.getChildAt(i) instanceof ActionMenuView) {
                    ((LayoutParams) this.toolbar.getChildAt(i).getLayoutParams()).gravity = 16;
                }
            }
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this.context, NewDashboardActivity.class));
        }
        if (id == R.id.nav_settings) {
            startActivity(new Intent(this.context, SettingsActivity.class));
        }
        if (id == R.id.action_logout1 && this.context != null) {
            new Builder(this.context, R.style.AlertDialogCustom).setTitle(getResources().getString(R.string.logout_title)).setMessage(getResources().getString(R.string.logout_message)).setPositiveButton("Sim", new C19644()).setNegativeButton("Não", new C19633()).show();
        }
        return false;
    }

    private boolean getChannelEPGUpdateStatus() {
        if (this.liveStreamDBHandler == null || this.databaseUpdatedStatusDBModelLive == null || this.databaseUpdatedStatusDBModelEPG == null) {
            return false;
        }
        this.databaseUpdatedStatusDBModelLive = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_CHANNELS, "1");
        this.databaseUpdatedStatusDBModelEPG = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID);
        if (this.databaseUpdatedStatusDBModelLive == null || this.databaseUpdatedStatusDBModelEPG == null || this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState() == null || this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState() == null || !this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH) || !this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH)) {
            return false;
        }
        return true;
    }

    private void initialize() {
        this.context = getContext();
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        if (this.context != null) {
            onCreateEPG(false);
            if (this.getActiveLiveStreamCategoryId.equals("-1")) {
                ArrayList<FavouriteDBModel> favourites = getFavourites();
                if (favourites == null || favourites.size() == 0) {
                    if (this.pbLoader != null) {
                        this.pbLoader.setVisibility(4);
                    }
                    if (this.tvNoStream != null) {
                        this.tvNoStream.setVisibility(View.VISIBLE);
                    }
                    if (this.rl_add_channel_to_fav != null) {
                        this.rl_add_channel_to_fav.setVisibility(View.VISIBLE);
                        return;
                    }
                    return;
                }
                onWindowFocusChanged(this.getActiveLiveStreamCategoryId, this.epgFragment, R.id.epg);
            } else if (this.liveStreamDBHandler.getLiveStreamsCount(this.getActiveLiveStreamCategoryId) != 0 || this.getActiveLiveStreamCategoryId.equals(AppConst.PASSWORD_UNSET)) {
                onWindowFocusChanged(this.getActiveLiveStreamCategoryId, this.epgFragment, R.id.epg);
            } else {
                if (this.pbLoader != null) {
                    this.pbLoader.setVisibility(4);
                }
                if (this.tvNoStream != null) {
                    this.tvNoStream.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public ArrayList<FavouriteDBModel> getFavourites() {
        if (this.context != null) {
            this.database = new DatabaseHandler(this.context);
            if (this.database != null) {
                ArrayList<FavouriteDBModel> allFavourites = this.database.getAllFavourites("live");
                if (!(allFavourites == null || allFavourites.size() == 0)) {
                    return allFavourites;
                }
            }
        }
        return null;
    }

    public void onDestroyView() {
        if (this.epg != null) {
            this.epg.clearEPGImageCache();
            this.epg.destroyVideoPlayBack();
        }
        this.loginPreferencesSharedPref_opened_video = this.context.getSharedPreferences(AppConst.LOGIN_PREF_OPENED_VIDEO, 0);
        Editor loginPreferencesEditor = this.loginPreferencesSharedPref_opened_video.edit();
        loginPreferencesEditor.clear();
        loginPreferencesEditor.apply();
        super.onDestroyView();
        this.unbinder.unbind();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private void updateImageCropping(ImageView imageView) {
        Matrix matrix = imageView.getImageMatrix();
        float imageHeight = (float) imageView.getDrawable().getIntrinsicHeight();
        float scaleRatio = ((float) getResources().getDisplayMetrics().widthPixels) / ((float) imageView.getDrawable().getIntrinsicWidth());
        matrix.postScale(scaleRatio, scaleRatio);
        matrix.postTranslate(0.0f, (-1.0f * imageHeight) * 0.3f);
        imageView.setImageMatrix(matrix);
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
        this.periodicTaskHandler.removeCallbacksAndMessages(null);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void onWindowFocusChanged(String categoryID, RelativeLayout epgFragment, int epg) {
        int hrs = (((TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings()) / 1000) / 60) / 60;
        new AnonymousClass1AsyncLoadEPGData(this.epg, categoryID, epgFragment).execute(new Void[0]);
    }

    @OnClick({R.id.app_video_box})
    public void onViewClicked() {
        onCreateEPG(true);
    }
}
