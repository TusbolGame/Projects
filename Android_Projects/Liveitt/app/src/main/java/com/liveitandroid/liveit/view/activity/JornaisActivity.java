package com.liveitandroid.liveit.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.JSONPar;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.helper.Urls;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.liveitandroid.liveit.view.adapter.VodAdapter;
import com.liveitandroid.liveit.view.adapter.VodAdapterRest;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JornaisActivity extends AppCompatActivity implements OnClickListener {
    private static final String JSON = "";
    int actionBarHeight;
    static ProgressBar pbPagingLoader1;
    TextView clientNameTv;
    public String api_type = "";
    private SessionManager mSessionManager;
    private String jsonStr;
    private JSONObject jsonFilmObject;
    public String programas_url;
    private Context context;
    private ProgressDialog progressDialog;
    ArrayList<ListMoviesSetterGetter> subcat_list = new ArrayList<ListMoviesSetterGetter>();
    GridLayoutManager gridLayoutManager;
    boolean isSubcaetgroyAvail = false;
    private LayoutManager layoutManager;
    private LayoutManager mLayoutManager;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    @BindView(R.id.rl_sub_cat)
    RelativeLayout rl_sub_cat;
    SearchView searchView;
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
    private VodAdapter vodAdapter;
    @BindView(R.id.tv_settings)
    TextView vodCategoryName;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    private VodAdapterRest vodSubCatAdpaterNew;
    String sel_type = "";
    String sel_year = "";
    String sel_page = "";
    String sel_Quality = "";
    String selnome = "";
    int page_num = 0;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_vod_new_flow_subcategories);//activity_vod_new_flow_subcategories
        ButterKnife.bind(this);
        context = this;
        mSessionManager = new SessionManager(this.context);

        new FetchFilmList().execute();
    }

    public void setData() {
        if (subcat_list != null && subcat_list.size() == 0) {
            setContentView(R.layout.activity_series_layout);
            ButterKnife.bind(this);
            atStart();
            initialize();
            //break;
        }else{
            setContentView(R.layout.activity_vod_new_flow_subcategories);
            ButterKnife.bind(this);
            atStart();
            this.isSubcaetgroyAvail = true;
            setSubCategoryLayout(subcat_list);
        }

        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        if (this.appbarToolbar != null) {
            this.appbarToolbar.setBackground(getResources().getDrawable(R.drawable.vod_backgound));
        }
        changeStatusBarColor();

        vodCategoryName.setText(getResources().getString(R.string.menu_22));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return false;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setSubCategoryLayout(ArrayList<ListMoviesSetterGetter> subCategoryList) {
        initializeSubCat(subCategoryList);
    }

    private void initializeSubCat(ArrayList<ListMoviesSetterGetter> subCategoryList) {
        if (this.myRecyclerView != null && this.context != null) {
            this.myRecyclerView.setHasFixedSize(true);
            this.layoutManager = new LinearLayoutManager(this.context);
            this.myRecyclerView.setLayoutManager(this.layoutManager);
            this.myRecyclerView.setHasFixedSize(true);
            onFinish();
            this.vodSubCatAdpaterNew = new VodAdapterRest(subCategoryList, this.context);
            this.myRecyclerView.setAdapter(this.vodSubCatAdpaterNew);
        }
    }

    private void initialize() {
        if (this.myRecyclerView != null && this.context != null) {
            this.myRecyclerView.setHasFixedSize(true);
            this.layoutManager = new GridLayoutManager(this.context, Utils.getNumberOfColumns(this.context) + 1);
            this.myRecyclerView.setLayoutManager(this.layoutManager);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    private void initialize1() {
        if (this.myRecyclerView != null && this.context != null) {
            this.myRecyclerView.setHasFixedSize(true);
            this.layoutManager = new LinearLayoutManager(this.context);
            this.myRecyclerView.setLayoutManager(this.layoutManager);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
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
        if (!(this.vodSubCatAdpaterNew == null || pbPagingLoader1 == null)) {
            this.vodSubCatAdpaterNew.setVisibiltygone(pbPagingLoader1);
        }
        if (this.myRecyclerView != null) {
            this.myRecyclerView.setClickable(true);
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    public void onResume() {
        super.onResume();
        getWindow().setFlags(1024, 1024);
        if (this.context != null) {
            onFinish();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_header_title:
                startActivity(new Intent(this, NewDashboardActivity.class));
                return;
            default:
                return;
        }
    }

    public void atStart() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
    }

    public void onFinish() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
    }


    public boolean ProcuraGrupo(String grupChega)
    {
        Boolean reiaa = true;
        for (ListMoviesSetterGetter reideus : subcat_list) {
            if(reideus.getPraias_name().equals(grupChega))
            {
                reiaa = false;
                break;
            }
        }
        return reiaa;
    }

    public class FetchFilmList extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePairs;
            try {
                String praias_url = mSessionManager.getUserAcess_Token() + Urls.urlJornais + "&client_secret=" + mSessionManager.getClient_Secret();
                JSONPar jsonPar = new JSONPar();

                nameValuePairs = new ArrayList<NameValuePair>();
                jsonStr = jsonPar.makeHttpRequest(praias_url, "POST", nameValuePairs);
                jsonFilmObject = null;
                jsonFilmObject = XML.toJSONObject(jsonStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonFilmObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonProgramList) {
            super.onPostExecute(jsonProgramList);
            JSONArray jsonCanais = null;
            JSONObject jobj_todo = null;
            JSONArray jsonCanaisTodos = new JSONArray();
            ListMoviesSetterGetter item;
            subcat_list.clear();
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (jsonProgramList == null) {
                tvNoStream.setVisibility(View.VISIBLE);
                return;
            } else {
                if (jsonProgramList.length() == 0) {
                    tvNoStream.setVisibility(View.VISIBLE);
                    return;
                } else {
                    try {
                        jobj_todo = jsonProgramList.getJSONObject("root");
                        jsonCanais = jobj_todo.getJSONArray("canais");
                        int _len = jsonCanais.length();
                        for (int i = 0; i < _len; i++) {
                            JSONObject tmpObj = jsonCanais.getJSONObject(i);
                            if (tmpObj != null) {
                                String ghName = tmpObj.optString("Grupo");
                                byte[] data = Base64.decode(tmpObj.optString("Nome"), Base64.DEFAULT);
                                byte[] data1 = Base64.decode(tmpObj.optString("Url"), Base64.DEFAULT);
                                byte[] data2 = Base64.decode(tmpObj.optString("WebSite"), Base64.DEFAULT);
                                String chName = new String(data, "ISO-8859-1");
                                String churl = new String(data1, "ISO-8859-1");
                                String chwebsite = new String(data2, "ISO-8859-1");

                                ListMoviesSetterGetter mProgramSetterGetter = new ListMoviesSetterGetter();
                                mProgramSetterGetter.setPraias_disp(chwebsite);
                                mProgramSetterGetter.setPraias_imageGroup(ghName);
                                mProgramSetterGetter.setPraias_name(chName);
                                mProgramSetterGetter.setPraias_url(churl);
                                mProgramSetterGetter.setPraias_group("jornais");
                                subcat_list.add(mProgramSetterGetter);
                            }
                        }
                    } catch (Exception e) {
                        JSONObject tmpObj = null;
                        try {
                            jobj_todo = jsonProgramList.getJSONObject("root");
                            tmpObj = jobj_todo.getJSONObject("canais");
                            if (tmpObj != null) {
                                String ghName = tmpObj.optString("Grupo");
                                byte[] data = Base64.decode(tmpObj.optString("Nome"), Base64.DEFAULT);
                                byte[] data1 = Base64.decode(tmpObj.optString("Url"), Base64.DEFAULT);
                                byte[] data2 = Base64.decode(tmpObj.optString("WebSite"), Base64.DEFAULT);
                                String chName = new String(data, "ISO-8859-1");
                                String churl = new String(data1, "ISO-8859-1");
                                String chwebsite = new String(data2, "ISO-8859-1");

                                ListMoviesSetterGetter mProgramSetterGetter = new ListMoviesSetterGetter();
                                mProgramSetterGetter.setPraias_disp(chwebsite);
                                mProgramSetterGetter.setPraias_imageGroup(ghName);
                                mProgramSetterGetter.setPraias_name(chName);
                                mProgramSetterGetter.setPraias_url(churl);
                                mProgramSetterGetter.setPraias_group("jornais");
                                subcat_list.add(mProgramSetterGetter);
                            }
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                }
            }
            setData();
        }
    }

    public void progressBar(ProgressBar pbPagingLoader) {
        pbPagingLoader1 = pbPagingLoader;
    }
}
