package com.liveitandroid.liveit.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.helper.Urls;
import com.liveitandroid.liveit.helper.XMLParser;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.database.DatabaseUpdatedStatusDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;

import org.json.JSONObject;
import org.json.XML;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VodMenuActivity extends AppCompatActivity implements OnClickListener {
    private static final int TIME_INTERVAL = 2000;
    @BindView(R.id.menu_filmes_app)
    LinearLayout menu_filmes_app;
    @BindView(R.id.menu_filmes_lista)
    LinearLayout menu_filmes_lista;
    private Context context = this;
    private long mBackPressed;
    @BindView(R.id.main_layout)
    LinearLayout main_layout;

    private class OnFocusChangeAccountListener implements OnFocusChangeListener {
        private final View view;

        public OnFocusChangeAccountListener(View view) {
            this.view = view;
        }

        @SuppressLint({"ResourceType"})
        public void onFocusChange(View v, boolean hasFocus) {
            float to = 2.0f;
            if (hasFocus) {
                if (hasFocus) {
                    to = 1.09f;
                } else {
                    to = 1.0f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                if (this.view.getTag().equals("1")) {
                    this.view.setBackgroundResource(R.drawable.live_tv_background);
                } else if (this.view.getTag().equals("2")) {
                    this.view.setBackgroundResource(R.drawable.on_demand_background);
                }
            } else if (!hasFocus) {
                if (hasFocus) {
                    to = 1.09f;
                } else {
                    to = 1.0f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                performAlphaAnimation(hasFocus);
                if (this.view.getTag().equals("1")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_01);
                } else if (this.view.getTag().equals("2")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_02);
                }
            }
        }

        private void performScaleXAnimation(float to) {
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(this.view, "scaleX", new float[]{to});
            scaleXAnimator.setDuration(150);
            scaleXAnimator.start();
        }

        private void performScaleYAnimation(float to) {
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(this.view, "scaleY", new float[]{to});
            scaleYAnimator.setDuration(150);
            scaleYAnimator.start();
        }

        private void performAlphaAnimation(boolean hasFocus) {
            if (hasFocus) {
                float toAlpha = hasFocus ? 0.6f : 0.5f;
                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this.view, "alpha", new float[]{toAlpha});
                alphaAnimator.setDuration(150);
                alphaAnimator.start();
            }
        }
    }
    public LinearLayout fragment_fil_p2;
    private SessionManager mSessionManager;
    @SuppressLint({"SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vod_menu_layout);
        ButterKnife.bind(this);
        changeStatusBarColor();
        hideSystemUi();
        makeButtonsClickable();
        this.menu_filmes_lista.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_filmes_lista));
        this.menu_filmes_app.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_filmes_app));
        this.menu_filmes_lista.requestFocus();

        mSessionManager = new SessionManager(this.context);
    }

    protected void onResume() {
        hideSystemUi();
        super.onResume();
    }

    private void makeButtonsClickable() {
        this.menu_filmes_lista.setOnClickListener(this);
        this.menu_filmes_app.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_filmes_lista:
                startActivity(new Intent(this, VodMenuFilmesListaActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.menu_filmes_app:
                new ValidateAPP().execute();
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            default:
                return;
        }
    }

    private AlertDialog alert2;
    private JSONObject jobj_userinfo, jobj_usertodo;
    private String app_name;
    public class ValidateAPP extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                jobj_usertodo = null;
                String mystring = getBaseContext().getApplicationInfo().loadLabel(getBaseContext().getPackageManager()).toString();
                app_name = getBaseContext().getApplicationInfo().loadLabel(getBaseContext().getPackageManager()).toString();
                String jsonStr = "";
                mystring = mystring.replace(" ", "%20");
                mystring = mystring.replace(" ", "%20");
                mystring = mystring.replace(" ", "%20");
                jsonStr = Urls.urlValid + mystring + "&tipo_app=APK";
                XMLParser parser = new XMLParser();
                String xml = parser.getXmlFromUrl(jsonStr);
                jobj_usertodo = XML.toJSONObject(xml);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jobj_usertodo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                jobj_usertodo = null;
                jobj_usertodo = jsonObject.getJSONObject("tudo");
                String erronovo = null;
                erronovo = jobj_usertodo.optString("erro");
                if (erronovo != "" && erronovo != null) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                    builder2.setTitle(app_name);
                    builder2.setMessage(erronovo);
                    builder2.setPositiveButton("Fechar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder2.show();
                } else {
                    jobj_userinfo = jobj_usertodo.getJSONObject("validacao");
                    String filmes_app = jobj_userinfo.optString("filmes_app");

                    mSessionManager.setTipoFilmesAPP(filmes_app);
                    if(filmes_app.equals("0"))
                    {
                        //startActivity(new Intent(this, VodMenuFilmesListaActivity.class));
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                        builder2.setTitle(app_name);
                        builder2.setMessage("De momento não é possível abertura deste menu. Use o Filmes Lista por agora. Tente mais tarde novamente.");
                        builder2.setPositiveButton("Fechar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder2.show();
                    } else if(filmes_app.equals("1"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                        builder2.setTitle(app_name);
                        builder2.setMessage("De momento não é possível abertura deste menu. Use o Filmes Lista por agora. Tente mais tarde novamente.");
                        builder2.setPositiveButton("Fechar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder2.show();
                    } else if(filmes_app.equals("2"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                        builder2.setTitle(app_name);
                        builder2.setMessage("De momento não é possível abertura deste menu. Use o Filmes Lista por agora. Tente mais tarde novamente.");
                        builder2.setPositiveButton("Fechar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder2.show();
                    } else if(filmes_app.equals("3"))
                    {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                        builder2.setTitle(app_name);
                        builder2.setMessage("De momento não é possível abertura deste menu. Use o Filmes Lista por agora. Tente mais tarde novamente.");
                        builder2.setPositiveButton("Fechar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder2.show();
                    } else if(filmes_app.equals("4"))
                    {
                        startActivity(new Intent(context, VodMenuFilmesApp4Activity.class));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public void hideSystemUi() {
        this.main_layout.setSystemUiVisibility(4871);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        return;
    }
}
