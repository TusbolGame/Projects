package com.liveitandroid.liveit.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.view.activity.NewDashboardActivity2;
import com.liveitandroid.liveit.view.interfaces.LiveStreamsInterface;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.model.callback.LiveStreamCategoriesCallback;
import com.liveitandroid.liveit.model.callback.LiveStreamsCallback;
import com.liveitandroid.liveit.model.callback.LiveStreamsEpgCallback;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.model.pojo.EpgListingPojo;
import com.liveitandroid.liveit.model.pojo.XMLTVProgrammePojo;
import com.liveitandroid.liveit.view.activity.NewDashboardActivity;
import com.liveitandroid.liveit.view.activity.SettingsActivity;
import com.liveitandroid.liveit.view.adapter.SubTVArchiveAdapter;
import com.liveitandroid.liveit.view.interfaces.LiveStreamsInterface;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SubTVArchiveFragment extends Fragment implements LiveStreamsInterface, OnNavigationItemSelectedListener {
    public static final String ACTIVE_LIVE_STREAM_CATEGORY_ID = "";
    public static final String ACTIVE_LIVE_STREAM_CHANNEL_ID = "";
    public static final String ACTIVE_LIVE_STREAM_ICON = "";
    public static final String ACTIVE_LIVE_STREAM_NAME = "";
    public static final String ACTIVE_LIVE_STREAM_NUM = "";
    public static final String LIVE_STREAMS_EPG = "";
    private SubTVArchiveAdapter LiveStreamsEpgAdapter;
    int actionBarHeight;
    public Context context;
    private List<EpgListingPojo> dataSet;
    DatabaseHandler database;
    private ArrayList<LiveStreamsCallback> favouriteStreams = new ArrayList();
    private String getActiveLiveStreamCategoryId;
    private String getActiveLiveStreamChannelDuration;
    private String getActiveLiveStreamChannelId;
    private String getActiveLiveStreamIcon;
    private String getActiveLiveStreamId;
    private String getActiveLiveStreamName;
    private String getActiveLiveStreamNum;
    private LayoutManager layoutManager;
    private SharedPreferences loginPreferencesSharedPref;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    SearchView searchView;
    private Toolbar toolbar;
    TypedValue tv;
    @BindView(R.id.tv_no_record_found)
    TextView tvNoRecordFound;
    @BindView(R.id.tv_noStream)
    TextView tvNoStream;
    Unbinder unbinder;

    class C19821 implements OnClickListener {
        C19821() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C19832 implements OnClickListener {
        C19832() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.logoutUser(SubTVArchiveFragment.this.context);
        }
    }

    public static SubTVArchiveFragment newInstance(String category_id, ArrayList<XMLTVProgrammePojo> liveStreamEpgCallback, String opened_stream_id, String opened_num, String opened_channel_name, String opened_channel_icon, String opened_channel_id, String opened_channel_duration) {
        Bundle args = new Bundle();
        args.putString("ACTIVE_LIVE_STREAM_CATEGORY_ID", category_id);
        args.putString("ACTIVE_LIVE_STREAM_ID", opened_stream_id);
        args.putString("ACTIVE_LIVE_STREAM_NUM", opened_num);
        args.putString("ACTIVE_LIVE_STREAM_NAME", opened_channel_name);
        args.putString("ACTIVE_LIVE_STREAM_ICON", opened_channel_icon);
        args.putString("ACTIVE_LIVE_STREAM_CHANNEL_ID", opened_channel_id);
        args.putString("ACTIVE_LIVE_STREAM_CHANNEL_DURATION", opened_channel_duration);
        args.putSerializable("LIVE_STREAMS_EPG", liveStreamEpgCallback);
        SubTVArchiveFragment fragment = new SubTVArchiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setEpgtoAdapter() {
        this.getActiveLiveStreamCategoryId = getArguments().getString("ACTIVE_LIVE_STREAM_CATEGORY_ID");
        this.getActiveLiveStreamId = getArguments().getString("ACTIVE_LIVE_STREAM_ID");
        this.getActiveLiveStreamNum = getArguments().getString("ACTIVE_LIVE_STREAM_NUM");
        this.getActiveLiveStreamName = getArguments().getString("ACTIVE_LIVE_STREAM_NAME");
        this.getActiveLiveStreamIcon = getArguments().getString("ACTIVE_LIVE_STREAM_ICON");
        this.getActiveLiveStreamChannelId = getArguments().getString("ACTIVE_LIVE_STREAM_CHANNEL_ID");
        this.getActiveLiveStreamChannelDuration = getArguments().getString("ACTIVE_LIVE_STREAM_CHANNEL_DURATION");
        Serializable getAllLiveStreamsEpg = getArguments().getSerializable("LIVE_STREAMS_EPG");
        if (this.getActiveLiveStreamCategoryId != null && getAllLiveStreamsEpg != null) {
            ArrayList<XMLTVProgrammePojo> liveStreamEpgCallback = (ArrayList) getAllLiveStreamsEpg;
            String Title = "";
            int nowPlayingPosition = 0;
            boolean nowPlayingFlag = false;
            int j = 0;
            ArrayList sortedArray = new ArrayList();
            for (int i = 0; i < liveStreamEpgCallback.size(); i++) {
                String startDateTime = ((XMLTVProgrammePojo) liveStreamEpgCallback.get(i)).getStart();
                String stopDateTime = ((XMLTVProgrammePojo) liveStreamEpgCallback.get(i)).getStop();
                Title = ((XMLTVProgrammePojo) liveStreamEpgCallback.get(i)).getTitle();
                String Desc = ((XMLTVProgrammePojo) liveStreamEpgCallback.get(i)).getDesc();
                Long epgStartDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(startDateTime));
                Long epgStopDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(stopDateTime));
                String currentFormatDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(epgStartDateToTimestamp);
                if (currentFormatDate != null) {
                    if (currentFormatDate.equals(this.getActiveLiveStreamCategoryId)) {
                        sortedArray.add(liveStreamEpgCallback.get(i));
                        j++;
                    }
                }
                if (Utils.isEventVisible(epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), getActivity())) {
                    nowPlayingFlag = true;
                    nowPlayingPosition = j - 1;
                    break;
                }
            }
            this.LiveStreamsEpgAdapter = new SubTVArchiveAdapter(sortedArray, nowPlayingPosition, nowPlayingFlag, this.getActiveLiveStreamCategoryId, this.getActiveLiveStreamId, this.getActiveLiveStreamNum, this.getActiveLiveStreamName, this.getActiveLiveStreamIcon, this.getActiveLiveStreamChannelId, this.getActiveLiveStreamChannelDuration, getContext());
            this.myRecyclerView.setAdapter(this.LiveStreamsEpgAdapter);
            initialize(nowPlayingPosition + 1);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_epg, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        ActivityCompat.invalidateOptionsMenu(getActivity());
        setHasOptionsMenu(true);
        setToolbarLogoImagewithSearchView();
        setEpgtoAdapter();
        return view;
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
    private SessionManager mSessionManager;
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if(mSessionManager.getFilmesAPP().equals("4") || mSessionManager.getFilmesAPP().equals(4)){
                startActivity(new Intent(this.context, NewDashboardActivity.class));
            }else{
                startActivity(new Intent(this.context, NewDashboardActivity2.class));
            }
        }
        if (id == R.id.nav_settings) {
            startActivity(new Intent(this.context, SettingsActivity.class));
        }
        if (id == R.id.action_logout1 && this.context != null) {
            new Builder(this.context, R.style.AlertDialogCustom).setTitle(getResources().getString(R.string.logout_title)).setMessage(getResources().getString(R.string.logout_message)).setPositiveButton(17039379, new C19832()).setNegativeButton(17039369, new C19821()).show();
        }
        return false;
    }

    private void initialize(int nowPlayingPosition) {
        this.context = getContext();
        mSessionManager = new SessionManager(this.context);
        if (this.myRecyclerView != null && this.context != null) {
            this.myRecyclerView.setHasFixedSize(true);
            this.layoutManager = new LinearLayoutManager(getContext());
            this.myRecyclerView.setLayoutManager(this.layoutManager);
            this.myRecyclerView.smoothScrollToPosition(nowPlayingPosition);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.unbinder.unbind();
    }

    public void liveStreamCategories(List<LiveStreamCategoriesCallback> list) {
    }

    public void liveStreams(List<LiveStreamsCallback> list, ArrayList<FavouriteDBModel> arrayList) {
    }

    public void liveStreamsEpg(LiveStreamsEpgCallback liveStreamsEpgCallback, TextView tvActiveChannel, TextView tvNextChannel) {
    }

    public void atStart() {
    }

    public void onFinish() {
    }

    public void onFailed(String message) {
        Utils.showToast(this.context, AppConst.NETWORK_ERROR_OCCURED);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
