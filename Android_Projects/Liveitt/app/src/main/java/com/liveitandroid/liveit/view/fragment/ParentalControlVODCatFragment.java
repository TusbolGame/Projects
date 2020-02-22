package com.liveitandroid.liveit.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.model.database.DatabaseUpdatedStatusDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.view.activity.ImportEPGActivity;
import com.liveitandroid.liveit.view.activity.ParentalControlActivitity;
import com.liveitandroid.liveit.view.adapter.ParentalControlVODCatAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParentalControlVODCatFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context context;
    ParentalControlActivitity dashboardActivity;
    @BindView(R.id.empty_view)
    TextView emptyView;
    private Typeface fontOPenSansBold;
    LiveStreamDBHandler liveStreamDBHandler;
    private ParentalControlVODCatAdapter mAdapter;
    private LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;
    private FragmentActivity myContext;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    private ProgressDialog progressDialog;
    private SearchView searchView;
    private Toolbar toolbar;
    Unbinder unbinder;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    class C19721 implements OnQueryTextListener {
        C19721() {
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            if (ParentalControlVODCatFragment.this.pbLoader != null) {
                ParentalControlVODCatFragment.this.pbLoader.setVisibility(4);
            }
            if (ParentalControlVODCatFragment.this.progressDialog != null) {
                ParentalControlVODCatFragment.this.progressDialog.dismiss();
            }
            if (!(ParentalControlVODCatFragment.this.emptyView == null || ParentalControlVODCatFragment.this.mAdapter == null)) {
                ParentalControlVODCatFragment.this.emptyView.setVisibility(8);
                ParentalControlVODCatFragment.this.mAdapter.filter(newText, ParentalControlVODCatFragment.this.emptyView, ParentalControlVODCatFragment.this.progressDialog);
            }
            return true;
        }
    }

    class C19732 implements OnClickListener {
        C19732() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    class C19743 implements OnClickListener {
        C19743() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C19754 implements OnClickListener {
        C19754() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadTvGuid(ParentalControlVODCatFragment.this.context);
        }
    }

    class C19765 implements OnClickListener {
        C19765() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    public static ParentalControlVODCatFragment newInstance(String param1, String param2) {
        ParentalControlVODCatFragment fragment = new ParentalControlVODCatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        View view = inflater.inflate(R.layout.fragment_parental_control_vodcat, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        initializeData();
        setMenuBar();
        return view;
    }

    private void initializeData() {
        this.context = getContext();
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        this.fontOPenSansBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/open_sans.ttf");
        this.dashboardActivity = (ParentalControlActivitity) this.context;
        this.myRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(getContext());
        this.myRecyclerView.setLayoutManager(this.mLayoutManager);
        ArrayList<LiveStreamCategoryIdDBModel> liveCategories = new LiveStreamDBHandler(this.context).getAllMovieCategoriesHavingParentIdZero();
        Map<String, String> map = new HashMap();
        if (liveCategories != null) {
            Iterator it = liveCategories.iterator();
            while (it.hasNext()) {
                LiveStreamCategoryIdDBModel listItem = (LiveStreamCategoryIdDBModel) it.next();
                map.put(listItem.getLiveStreamCategoryID(), listItem.getLiveStreamCategoryName());
            }
        }
        String[] categoriesArray = (String[]) map.values().toArray(new String[0]);
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
        if (liveCategories != null && liveCategories.size() > 0 && this.myRecyclerView != null && this.emptyView != null) {
            this.myRecyclerView.setVisibility(0);
            this.emptyView.setVisibility(8);
            this.mAdapter = new ParentalControlVODCatAdapter(liveCategories, getContext(), this.dashboardActivity, this.fontOPenSansBold);
            this.myRecyclerView.setAdapter(this.mAdapter);
        } else if (this.myRecyclerView != null && this.emptyView != null) {
            this.myRecyclerView.setVisibility(8);
            this.emptyView.setVisibility(0);
            this.emptyView.setText("No VOD Categories Found");
        }
    }

    private void setMenuBar() {
        setHasOptionsMenu(true);
        this.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    public void onButtonPressed(Uri uri) {
        if (this.mListener != null) {
            this.mListener.onFragmentInteraction(uri);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            return;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            menu.clear();
        }
        this.toolbar.inflateMenu(R.menu.menu_search);
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

    public void onPrepareOptionsMenu(Menu menu) {
    }
}
