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
import com.liveitandroid.liveit.view.adapter.VodAdapterApp;
import com.liveitandroid.liveit.view.adapter.VodAdapterAppEpisodios;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VodMenuFilmesAPP4SerEpisodesSelectActivity extends AppCompatActivity implements OnClickListener {
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
    private VodAdapterAppEpisodios vodSubCatAdpaterNew;
    String sel_type = "";
    String sel_year = "";
    String sel_page = "";
    String sel_Quality = "";
    String selnome = "";
    int page_num = 0;
    String sel_temporada;
    String sel_nome,sel_nome2,sel_image;
    String sel_videoID;
    String sel_view = "";
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_vod_new_flow_subcategories);//activity_vod_new_flow_subcategories
        ButterKnife.bind(this);

        Intent intent = getIntent();

        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        sel_view = getIntent().getStringExtra("selview");
        sel_type = getIntent().getStringExtra("selType");
        sel_year = getIntent().getStringExtra("selyear");
        sel_videoID = getIntent().getStringExtra("selvideo");
        sel_temporada = getIntent().getStringExtra("seltemporada");
        sel_nome = getIntent().getStringExtra("selnome");
        sel_nome2 = getIntent().getStringExtra("selnome2");
        sel_page = getIntent().getStringExtra("selpage");
        page_num = 0;

        mSessionManager = new SessionManager(this.context);

        toolbar.setTitle(sel_nome + " ** "+getResources().getString(R.string.menu_42)+sel_temporada+" **");

        new FetchFilmList().execute();
    }

    public void setData() {
        if (subcat_list != null && subcat_list.size() == 0) {
            setContentView(R.layout.activity_series_layout);
            ButterKnife.bind(this);
            atStart();
            initialize1();
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
        this.vodCategoryName.setText(selnome);
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
            this.gridLayoutManager = new GridLayoutManager(this, Utils.getNumberOfColumns(this.context) + 1);
            this.myRecyclerView.setLayoutManager(this.gridLayoutManager);
            this.myRecyclerView.setHasFixedSize(true);
            onFinish();
            this.vodSubCatAdpaterNew = new VodAdapterAppEpisodios(subCategoryList, this.context);
            this.myRecyclerView.setAdapter(this.vodSubCatAdpaterNew);
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
        //overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
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

    public void goPageNaviFunction(ListMoviesSetterGetter selItem, String pageType) {
        if (selItem == null)
            return;
        if (pageType.equals("back")) {
            page_num = new Integer(selItem.getPageNavNumback()) ;
        } else {
            page_num = new Integer(selItem.getPageNavNum2());
        }
        mSessionManager.setcheckedpage(page_num);
        subcat_list = new ArrayList<ListMoviesSetterGetter>();
        sel_page = ""+page_num;
        new FetchFilmList().execute();
    }

    public class FetchFilmList extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            try {
                subcat_list = new ArrayList<ListMoviesSetterGetter>();
                programas_url = mSessionManager.getUserAcess_Token() + "PHP/liveit/tugaliveit.php?tipo=series&genero=temporada&temporada="+sel_temporada+"&serie="+sel_videoID+"&client_secret=" + mSessionManager.getClient_Secret();
                JSONPar jsonPar = new JSONPar();
                jsonStr = jsonPar.makeHttpRequest4(programas_url, "GET");
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
                        jsonCanais = jsonProgramList.getJSONArray("item");

                        int _len = jsonCanais.length();
                        for (int i = 0; i < _len; i++) {
                            JSONObject tmpObj = jsonCanais.getJSONObject(i);
                            if (tmpObj != null) {
                                String gid = "";
                                if(tmpObj.has("ID"))
                                {
                                    gid = tmpObj.optString("ID");
                                }

                                String chName = "";
                                if(tmpObj.has("Nome"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Nome"), Base64.DEFAULT);
                                    chName = new String(data, "UTF-8");
                                }

                                String chNameEN = "";
                                if(tmpObj.has("NomeEN"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("NomeEN"), Base64.DEFAULT);
                                    chNameEN = new String(data, "UTF-8");
                                }

                                String episodio = "";
                                if(tmpObj.has("Episodio"))
                                {
                                    episodio = "Episódio "+tmpObj.optString("Episodio")+": ";
                                }

                                String url1 = "";
                                if(tmpObj.has("Url"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Url"), Base64.DEFAULT);
                                    url1 = new String(data, "UTF-8");
                                }

                                String url2 = "";
                                if(tmpObj.has("Url1"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Url1"), Base64.DEFAULT);
                                    url2 = new String(data, "UTF-8");
                                }

                                String url3 = "";
                                if(tmpObj.has("Url2"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Url2"), Base64.DEFAULT);
                                    url3 = new String(data, "UTF-8");
                                }

                                String url4 = "";
                                if(tmpObj.has("Url3"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Url3"), Base64.DEFAULT);
                                    url4 = new String(data, "UTF-8");
                                }

                                String url5 = "";
                                if(tmpObj.has("Url4"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Url4"), Base64.DEFAULT);
                                    url5 = new String(data, "UTF-8");
                                }

                                String legenda = "";
                                if(tmpObj.has("Legendas"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Legendas"), Base64.DEFAULT);
                                    legenda = new String(data, "UTF-8");
                                }

                                String logonovo = "";
                                if(tmpObj.has("Imagem"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Imagem"), Base64.DEFAULT);
                                    if (new String(data, "UTF-8").contains("http")) {
                                        logonovo = new String(data, "UTF-8");
                                    }
                                    else
                                    {
                                        logonovo = mSessionManager.getkodiSite() + new String(data, "UTF-8");
                                    }
                                }

                                ListMoviesSetterGetter mListPraiasSetterGetter = new ListMoviesSetterGetter();
                                mListPraiasSetterGetter.setPraias_id(gid);
                                String pt = "";
                                String br = "";
                                String semLegenda = "";

                                if (legenda.equals("") || legenda.equals("semlegenda")) {
                                    semLegenda = " (S/ LEGENDA) ";
                                }
                                else
                                {
                                    if (legenda.equals("liveit")) {
                                        legenda = "";
                                    } else if (legenda.contains("http")) {
                                        legenda = legenda;
                                    } else if (legenda.contains("subs/")) {
                                        legenda = mSessionManager.getkodiSite() + legenda;
                                    }else
                                    {
                                        legenda = mSessionManager.getUserAcess_Token() + "legendas/" +legenda;
                                    }
                                }

                                if (br != "" || pt != "") {
                                    mListPraiasSetterGetter.setPraias_name(episodio + pt + br + chName);
                                } else {
                                    mListPraiasSetterGetter.setPraias_name(episodio + semLegenda + chName);
                                }
                                mListPraiasSetterGetter.setPraias_name_en(chNameEN);
                                mListPraiasSetterGetter.setPraias_legenda(legenda);
                                mListPraiasSetterGetter.setPraias_imagem(logonovo);
                                mListPraiasSetterGetter.setPraias_imageGroup(logonovo);
                                mListPraiasSetterGetter.setPraias_desc("reiepisodio");
                                mListPraiasSetterGetter.setPraias_group(chName);
                                mListPraiasSetterGetter.setPraias_url(url1);
                                mListPraiasSetterGetter.setPraias_url2(url2);
                                mListPraiasSetterGetter.setPraias_url3(url3);
                                mListPraiasSetterGetter.setPraias_url4(url4);
                                mListPraiasSetterGetter.setPraias_url5(url5);
                                subcat_list.add(mListPraiasSetterGetter);
                            }
                        }
                    } catch (Exception e) {
                        JSONObject tmpObj = null;
                        try {
                            tmpObj = jsonProgramList.getJSONObject("item");
                            if (tmpObj != null) {
                                String gid = "";
                                if(tmpObj.has("ID"))
                                {
                                    gid = tmpObj.optString("ID");
                                }

                                String chName = "";
                                if(tmpObj.has("Nome"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Nome"), Base64.DEFAULT);
                                    chName = new String(data, "UTF-8");
                                }

                                String chNameEN = "";
                                if(tmpObj.has("NomeEN"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("NomeEN"), Base64.DEFAULT);
                                    chNameEN = new String(data, "UTF-8");
                                }

                                String episodio = "";
                                if(tmpObj.has("Episodio"))
                                {
                                    episodio = "Episódio "+tmpObj.optString("Episodio")+": ";
                                }

                                String url1 = "";
                                if(tmpObj.has("Url"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Url"), Base64.DEFAULT);
                                    url1 = new String(data, "UTF-8");
                                }

                                String url2 = "";
                                if(tmpObj.has("Url1"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Url1"), Base64.DEFAULT);
                                    url2 = new String(data, "UTF-8");
                                }

                                String url3 = "";
                                if(tmpObj.has("Url2"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Url2"), Base64.DEFAULT);
                                    url3 = new String(data, "UTF-8");
                                }

                                String url4 = "";
                                if(tmpObj.has("Url3"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Url3"), Base64.DEFAULT);
                                    url4 = new String(data, "UTF-8");
                                }

                                String url5 = "";
                                if(tmpObj.has("Url4"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Url4"), Base64.DEFAULT);
                                    url5 = new String(data, "UTF-8");
                                }

                                String legenda = "";
                                if(tmpObj.has("Legendas"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Legendas"), Base64.DEFAULT);
                                    legenda = new String(data, "UTF-8");
                                }

                                String logonovo = "";
                                if(tmpObj.has("Imagem"))
                                {
                                    byte[] data = Base64.decode(tmpObj.optString("Imagem"), Base64.DEFAULT);
                                    if (new String(data, "UTF-8").contains("http")) {
                                        logonovo = new String(data, "UTF-8");
                                    }
                                    else
                                    {
                                        logonovo = mSessionManager.getkodiSite() + new String(data, "UTF-8");
                                    }
                                }

                                ListMoviesSetterGetter mListPraiasSetterGetter = new ListMoviesSetterGetter();
                                mListPraiasSetterGetter.setPraias_id(gid);
                                String pt = "";
                                String br = "";
                                String semLegenda = "";

                                if (legenda.equals("") || legenda.equals("semlegenda")) {
                                    semLegenda = " (S/ LEGENDA) ";
                                }
                                else
                                {
                                    if (legenda.equals("liveit")) {
                                        legenda = "";
                                    } else if (legenda.contains("http")) {
                                        legenda = legenda;
                                    } else if (legenda.contains("subs/")) {
                                        legenda = mSessionManager.getkodiSite() + legenda;
                                    }else
                                    {
                                        legenda = mSessionManager.getUserAcess_Token() + "legendas/" +legenda;
                                    }
                                }

                                if (br != "" || pt != "") {
                                    mListPraiasSetterGetter.setPraias_name(episodio + pt + br + chName);
                                } else {
                                    mListPraiasSetterGetter.setPraias_name(episodio + semLegenda + chName);
                                }
                                mListPraiasSetterGetter.setPraias_name_en(chNameEN);
                                mListPraiasSetterGetter.setPraias_legenda(legenda);
                                mListPraiasSetterGetter.setPraias_imagem(logonovo);
                                mListPraiasSetterGetter.setPraias_imageGroup(logonovo);
                                mListPraiasSetterGetter.setPraias_desc("reiepisodio");
                                mListPraiasSetterGetter.setPraias_group(chName);
                                mListPraiasSetterGetter.setPraias_url(url1);
                                mListPraiasSetterGetter.setPraias_url2(url2);
                                mListPraiasSetterGetter.setPraias_url3(url3);
                                mListPraiasSetterGetter.setPraias_url4(url4);
                                mListPraiasSetterGetter.setPraias_url5(url5);
                                subcat_list.add(mListPraiasSetterGetter);
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
