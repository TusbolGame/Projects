package com.liveitandroid.liveit.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.EpgChannelModel;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.callback.SeriesDBModel;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.model.database.DatabaseUpdatedStatusDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.database.PasswordStatusDBModel;
import com.liveitandroid.liveit.model.database.SeriesStreamsDatabaseHandler;
import com.liveitandroid.liveit.view.activity.NewDashboardActivity;
import com.liveitandroid.liveit.view.activity.NewDashboardActivity2;
import com.liveitandroid.liveit.view.activity.SettingsActivity;
import com.liveitandroid.liveit.view.adapter.LiveStreamListViewAdapter;
import com.liveitandroid.liveit.view.adapter.SeriesStreamsAdapter;
import java.util.ArrayList;
import java.util.Iterator;

public class SeriesTabFragment extends Fragment implements OnNavigationItemSelectedListener {
    public static final String ACTIVE_SERIES_TAB_CATEGORY_ID = "";
    private SharedPreferences SharedPreferencesSort;
    private Editor SharedPreferencesSortEditor;
    int actionBarHeight;
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    private PopupWindow changeSortPopUp;
    public Context context;
    DatabaseHandler database;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel();
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel();
    private Editor editor;
    ArrayList<EpgChannelModel> epgChannelModelList = new ArrayList();
    LiveStreamsDBModel favouriteStream = new LiveStreamsDBModel();
    private ArrayList<SeriesDBModel> favouriteStreams = new ArrayList();
    private String getActiveLiveStreamCategoryId;
    private LayoutManager layoutManager;
    private ArrayList<String> listPassword = new ArrayList();
    private ArrayList<LiveStreamsDBModel> liveListDetail;
    private ArrayList<SeriesDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailableNew;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlckedDetail;
    LiveStreamDBHandler liveStreamDBHandler;
    private LiveStreamListViewAdapter liveStreamsListViewAdapter;
    private SharedPreferences loginPreferencesSharedPref;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    private SharedPreferences pref;
    SearchView searchView;
    private SeriesStreamsAdapter seriesStreamsAdapter;
    private Toolbar toolbar;
    TypedValue tv;
    @BindView(R.id.tv_no_record_found)
    TextView tvNoRecordFound;
    @BindView(R.id.tv_noStream)
    TextView tvNoStream;
    Unbinder unbinder;

    class C19781 implements OnClickListener {
        C19781() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C19792 implements OnClickListener {
        C19792() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.logoutUser(SeriesTabFragment.this.context);
        }
    }

    class C19803 implements View.OnClickListener {
        C19803() {
        }

        public void onClick(View view) {
            SeriesTabFragment.this.changeSortPopUp.dismiss();
        }
    }

    public static SeriesTabFragment newInstance(String category_id) {
        Bundle args = new Bundle();
        args.putString("", category_id);
        SeriesTabFragment fragment = new SeriesTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActiveLiveStreamCategoryId = getArguments().getString("");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_streams, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        this.SharedPreferencesSort = getActivity().getSharedPreferences(AppConst.LOGIN_PREF_SORT_SERIES, 0);
        this.SharedPreferencesSortEditor = this.SharedPreferencesSort.edit();
        if (this.SharedPreferencesSort.getString(AppConst.LOGIN_PREF_SORT, "").equals("")) {
            this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.PASSWORD_UNSET);
            this.SharedPreferencesSortEditor.commit();
        }
        ActivityCompat.invalidateOptionsMenu(getActivity());
        setHasOptionsMenu(true);
        setToolbarLogoImagewithSearchView();
        this.pref = getActivity().getSharedPreferences(AppConst.LIST_GRID_VIEW, 0);
        this.editor = this.pref.edit();
        AppConst.LIVE_FLAG = this.pref.getInt(AppConst.LIVE_STREAM, 0);
        if (AppConst.LIVE_FLAG == 1) {
            initialize();
        } else {
            initialize1();
        }
        if (this.getActiveLiveStreamCategoryId != null && this.getActiveLiveStreamCategoryId.equals("-1")) {
            getFavourites();
        }
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
            new Builder(this.context, R.style.AlertDialogCustom).setTitle(getResources().getString(R.string.logout_title)).setMessage(getResources().getString(R.string.logout_message)).setPositiveButton(17039379, new C19792()).setNegativeButton(17039369, new C19781()).show();
        }
        if (id == R.id.action_search || id == R.id.menu_load_channels_vod1 || id == R.id.menu_load_tv_guide1) {
            return true;
        }
        if (id == R.id.layout_view_grid) {
            this.editor.putInt(AppConst.LIVE_STREAM, 1);
            this.editor.commit();
            initialize();
        }
        if (id == R.id.layout_view_linear) {
            this.editor.putInt(AppConst.LIVE_STREAM, 0);
            this.editor.commit();
            initialize1();
        }
        if (id == R.id.menu_sort) {
            showSortPopup(getActivity());
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
        last_added.setVisibility(8);
        if (sort.equals("1")) {
            last_added.setChecked(true);
        } else if (sort.equals(AppConst.DB_EPG_ID)) {
            atoz.setChecked(true);
        } else if (sort.equals("3")) {
            ztoa.setChecked(true);
        } else {
            normal.setChecked(true);
        }
        closedBT.setOnClickListener(new C19803());
        savePasswordBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RadioButton selectedPlayer1 = (RadioButton) layout.findViewById(rgRadio.getCheckedRadioButtonId());
                if (selectedPlayer1.getText().toString().equals(SeriesTabFragment.this.getResources().getString(R.string.sort_last_added))) {
                    SeriesTabFragment.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, "1");
                    SeriesTabFragment.this.SharedPreferencesSortEditor.commit();
                } else if (selectedPlayer1.getText().toString().equals(SeriesTabFragment.this.getResources().getString(R.string.sort_atoz))) {
                    SeriesTabFragment.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.DB_EPG_ID);
                    SeriesTabFragment.this.SharedPreferencesSortEditor.commit();
                } else if (selectedPlayer1.getText().toString().equals(SeriesTabFragment.this.getResources().getString(R.string.sort_ztoa))) {
                    SeriesTabFragment.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, "3");
                    SeriesTabFragment.this.SharedPreferencesSortEditor.commit();
                } else {
                    SeriesTabFragment.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.PASSWORD_UNSET);
                    SeriesTabFragment.this.SharedPreferencesSortEditor.commit();
                }
                SeriesTabFragment.this.pref = SeriesTabFragment.this.getActivity().getSharedPreferences(AppConst.LIST_GRID_VIEW, 0);
                SeriesTabFragment.this.editor = SeriesTabFragment.this.pref.edit();
                AppConst.LIVE_FLAG = SeriesTabFragment.this.pref.getInt(AppConst.LIVE_STREAM, 0);
                if (AppConst.LIVE_FLAG == 1) {
                    SeriesTabFragment.this.initialize();
                } else {
                    SeriesTabFragment.this.initialize1();
                }
                SeriesTabFragment.this.changeSortPopUp.dismiss();
            }
        });
    }

    private void initialize() {
        this.context = getContext();
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        if (this.myRecyclerView != null && this.context != null) {
            this.myRecyclerView.setHasFixedSize(true);
            this.layoutManager = new GridLayoutManager(getContext(), Utils.getNumberOfColumns(this.context) + 1);
            this.myRecyclerView.setLayoutManager(this.layoutManager);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            this.loginPreferencesSharedPref = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            String username = this.loginPreferencesSharedPref.getString("username", "");
            String password = this.loginPreferencesSharedPref.getString("password", "");
            setUpdatabaseResult();
        }
    }

    private void initialize1() {
        this.context = getContext();
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        if (this.myRecyclerView != null && this.context != null) {
            this.myRecyclerView.setHasFixedSize(true);
            this.layoutManager = new LinearLayoutManager(getContext());
            this.myRecyclerView.setLayoutManager(this.layoutManager);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            this.loginPreferencesSharedPref = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            String username = this.loginPreferencesSharedPref.getString("username", "");
            String password = this.loginPreferencesSharedPref.getString("password", "");
            setUpdatabaseResult();
        }
    }

    private void setUpdatabaseResult() {
        atStart();
        if (this.context != null) {
            LiveStreamDBHandler liveStreamDBHandler = new LiveStreamDBHandler(this.context);
            SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler = new SeriesStreamsDatabaseHandler(this.context);
            this.categoryWithPasword = new ArrayList();
            this.liveListDetailUnlcked = new ArrayList();
            this.liveListDetailUnlckedDetail = new ArrayList();
            this.liveListDetailAvailable = new ArrayList();
            this.liveListDetailAvailableNew = new ArrayList();
            this.liveListDetail = new ArrayList();
            ArrayList<SeriesDBModel> channelAvailable = seriesStreamsDatabaseHandler.getAllSeriesStreamsWithCategoryId(this.getActiveLiveStreamCategoryId);
            this.liveListDetailAvailable = channelAvailable;
            if (this.liveListDetailAvailable == null || this.myRecyclerView == null || this.liveListDetailAvailable.size() == 0) {
                onFinish();
                return;
            }
            onFinish();
            this.liveListDetailAvailable = channelAvailable;
            this.seriesStreamsAdapter = new SeriesStreamsAdapter(this.liveListDetailAvailable, getContext());
            this.myRecyclerView.setAdapter(this.seriesStreamsAdapter);
            this.seriesStreamsAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<LiveStreamsDBModel> getUnlockedCategories(ArrayList<LiveStreamsDBModel> liveListDetail, ArrayList<String> listPassword) {
        Iterator it = liveListDetail.iterator();
        while (it.hasNext()) {
            LiveStreamsDBModel user1 = (LiveStreamsDBModel) it.next();
            boolean flag = false;
            Iterator it2 = listPassword.iterator();
            while (it2.hasNext()) {
                if (user1.getCategoryId().equals((String) it2.next())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.liveListDetailUnlcked.add(user1);
            }
        }
        return this.liveListDetailUnlcked;
    }

    private ArrayList<String> getPasswordSetCategories() {
        this.categoryWithPasword = this.liveStreamDBHandler.getAllPasswordStatus();
        if (this.categoryWithPasword != null) {
            Iterator it = this.categoryWithPasword.iterator();
            while (it.hasNext()) {
                PasswordStatusDBModel listItemLocked = (PasswordStatusDBModel) it.next();
                if (listItemLocked.getPasswordStatus().equals("1")) {
                    this.listPassword.add(listItemLocked.getPasswordStatusCategoryId());
                }
            }
        }
        return this.listPassword;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.unbinder.unbind();
    }

    public void getFavourites() {
        this.favouriteStreams.clear();
        if (this.myRecyclerView != null) {
            this.myRecyclerView.setAdapter(this.seriesStreamsAdapter);
        }
        if (this.context != null) {
            this.database = new DatabaseHandler(this.context);
            Iterator it = this.database.getAllFavourites("series").iterator();
            while (it.hasNext()) {
                SeriesDBModel channelAvailable = new SeriesStreamsDatabaseHandler(this.context).getSeriesStreamsWithSeriesId(String.valueOf(((FavouriteDBModel) it.next()).getStreamID()));
                if (channelAvailable != null) {
                    this.favouriteStreams.add(channelAvailable);
                }
            }
            if (!(this.myRecyclerView == null || this.favouriteStreams == null || this.favouriteStreams.size() == 0)) {
                onFinish();
                this.seriesStreamsAdapter = new SeriesStreamsAdapter(this.favouriteStreams, getContext());
                this.myRecyclerView.setAdapter(this.seriesStreamsAdapter);
                this.seriesStreamsAdapter.notifyDataSetChanged();
                this.tvNoStream.setVisibility(4);
            }
            if (this.tvNoStream != null && this.favouriteStreams != null && this.favouriteStreams.size() == 0) {
                onFinish();
                if (this.myRecyclerView != null) {
                    this.myRecyclerView.setAdapter(this.seriesStreamsAdapter);
                }
                this.tvNoStream.setVisibility(0);
            }
        }
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

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
