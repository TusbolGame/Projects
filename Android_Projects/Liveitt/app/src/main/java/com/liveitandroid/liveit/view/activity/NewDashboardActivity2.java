package com.liveitandroid.liveit.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.helper.Urls;
import com.liveitandroid.liveit.helper.XMLParser;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.database.DatabaseUpdatedStatusDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;
import org.json.XML;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewDashboardActivity2 extends AppCompatActivity implements OnClickListener {
    @BindView(R.id.menu_extras)
    LinearLayout menu_extras;
    private Context context = this;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.updates_linear)
    LinearLayout updates_linear;
    @BindView(R.id.sair_linear)
    LinearLayout sair_linear;
    @BindView(R.id.menu_vod)
    LinearLayout menu_vod;
    @BindView(R.id.settings_linear)
    LinearLayout settings_linear;
    private long mBackPressed;
    @BindView(R.id.main_layout)
    LinearLayout main_layout;
    @BindView(R.id.time)
    TextView time;

    class C15575 implements DialogInterface.OnClickListener {
        C15575() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.logoutUser(context);
        }
    }


    class C15564 implements DialogInterface.OnClickListener {
        C15564() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C16471 implements Runnable {
        C16471() {
        }

        public void run() {
            try {
                String dateValue = Calendar.getInstance().getTime().toString();
                String currentCurrentTime = Utils.getTime(NewDashboardActivity2.this.context);
                String currentCurrentDate = Utils.getDate(dateValue);
                if (NewDashboardActivity2.this.time != null) {
                    NewDashboardActivity2.this.time.setText(currentCurrentTime);
                }
                if (NewDashboardActivity2.this.date != null) {
                    NewDashboardActivity2.this.date.setText(currentCurrentDate);
                }
            } catch (Exception e) {
            }
        }
    }

    class CountDownRunner implements Runnable {
        CountDownRunner() {
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    NewDashboardActivity2.this.doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e2) {
                }
            }
        }
    }

    public void doWork() {
        runOnUiThread(new C16471());
    }

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
                    this.view.setBackgroundResource(R.drawable.livetv_focused);
                } else if (this.view.getTag().equals("2")) {
                    this.view.setBackgroundResource(R.drawable.ondemand_focused);
                } else if (this.view.getTag().equals("3")) {
                    this.view.setBackgroundResource(R.drawable.catch_up_focused);
                } else if (this.view.getTag().equals("4")) {
                    this.view.setBackgroundResource(R.drawable.green_focused);
                } else if (this.view.getTag().equals("5")) {
                    this.view.setBackgroundResource(R.drawable.green_focused);
                } else if (this.view.getTag().equals("6")) {
                    this.view.setBackgroundResource(R.drawable.green_focused);
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
                } else if (this.view.getTag().equals("3")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_03);
                } else if (this.view.getTag().equals("4")) {
                    this.view.setBackgroundResource(R.drawable.riple_green);
                } else if (this.view.getTag().equals("5")) {
                    this.view.setBackgroundResource(R.drawable.riple_green);
                } else if (this.view.getTag().equals("6")) {
                    this.view.setBackgroundResource(R.drawable.riple_green);
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
        setContentView(R.layout.new_dashboard2_layout);
        ButterKnife.bind(this);
        changeStatusBarColor();
        hideSystemUi();
        new Thread(new CountDownRunner()).start();
        makeButtonsClickable();
        this.updates_linear.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.updates_linear));
        this.sair_linear.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.sair_linear));
        this.menu_vod.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_vod));
        this.settings_linear.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.settings_linear));
        this.menu_extras.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_extras));
        this.menu_vod.requestFocus();

        mSessionManager = new SessionManager(this.context);

        TextView tv_expiry_date = (TextView) findViewById(R.id.expiration_date);
        TextView tv_nome_suse = (TextView) findViewById(R.id.nome_user);
        TextView tv_email_suse = (TextView) findViewById(R.id.email_user);

        tv_expiry_date.setText("Fim: "+mSessionManager.getUserExpiryDate());
        tv_nome_suse.setText(""+mSessionManager.getUserNome());
        tv_email_suse.setText(""+mSessionManager.getUserEmail());

        fragment_fil_p2 = (LinearLayout) findViewById(R.id.main_layout);
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
        }
    }

    protected void onResume() {
        hideSystemUi();
        new Thread(new CountDownRunner()).start();
        super.onResume();
    }

    private void makeButtonsClickable() {
        this.menu_vod.setOnClickListener(this);
        this.menu_extras.setOnClickListener(this);
        this.updates_linear.setOnClickListener(this);
        this.settings_linear.setOnClickListener(this);
        this.sair_linear.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_vod:
                new ValidateAPP().execute();
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.sair_linear:
                new AlertDialog.Builder(this.context, R.style.AlertDialogCustom).setTitle(getResources().getString(R.string.logout_title)).setMessage(getResources().getString(R.string.logout_message)).setPositiveButton(17039379, new C15575()).setNegativeButton(17039369, new C15564()).show();
                return;
            case R.id.settings_linear:
                startActivity(new Intent(this, SettingsActivity2.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.updates_linear:
                startActivity(new Intent(this, AtualizaocesActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.menu_extras:
                startActivity(new Intent(this, ExtrasMenuActivity.class));
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
        if (this.mBackPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            return;
        }
        Toast.makeText(getBaseContext(), "Prima Novamente Voltar para Terminar o processo da aplicação.", 0).show();
        this.mBackPressed = System.currentTimeMillis();
    }
}
