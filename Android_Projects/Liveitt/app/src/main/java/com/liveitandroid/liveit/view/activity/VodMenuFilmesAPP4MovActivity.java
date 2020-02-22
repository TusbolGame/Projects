package com.liveitandroid.liveit.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.model.ListPraiasSetterGetter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VodMenuFilmesAPP4MovActivity extends AppCompatActivity implements OnClickListener {
    private static final int TIME_INTERVAL = 2000;
    @BindView(R.id.menu_todos_vod)
    LinearLayout menu_todos_vod;
    @BindView(R.id.menu_destaques_vod)
    LinearLayout menu_destaques_vod;
    @BindView(R.id.menu_anos_vod)
    LinearLayout menu_anos_vod;
    @BindView(R.id.menu_categoria_vod)
    LinearLayout menu_categoria_vod;
    @BindView(R.id.menu_imdb_vod)
    LinearLayout menu_imdb_vod;
    @BindView(R.id.menu_kids_vod)
    LinearLayout menu_kids_vod;
    @BindView(R.id.menu_search_vod)
    LinearLayout menu_search_vod;
    @BindView(R.id.menu_favoritos_vod)
    LinearLayout menu_favoritos_vod;
    private Context context = this;
    private long mBackPressed;
    @BindView(R.id.main_layout)
    LinearLayout main_layout;
    ArrayList<ListPraiasSetterGetter> list_item_categories = new ArrayList<ListPraiasSetterGetter>();
    ArrayList<ListPraiasSetterGetter> list_item_sagas = new ArrayList<ListPraiasSetterGetter>();
    ArrayList<ListPraiasSetterGetter> list_item_anos = new ArrayList<ListPraiasSetterGetter>();
    AlertDialog alert;

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
                this.view.setBackgroundResource(R.drawable.live_tv_background);
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
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_04);
                }else if (this.view.getTag().equals("5")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_01);
                }else if (this.view.getTag().equals("6")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_02);
                }else if (this.view.getTag().equals("7")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_03);
                }else if (this.view.getTag().equals("8")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_04);
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

    JSONArray jsonArray, jsonArrayAnos;
    private SessionManager mSessionManager;
    @SuppressLint({"SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vod_menu_app_filmes);
        ButterKnife.bind(this);
        changeStatusBarColor();
        hideSystemUi();
        makeButtonsClickable();
        this.menu_todos_vod.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_todos_vod));
        this.menu_destaques_vod.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_destaques_vod));
        this.menu_anos_vod.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_anos_vod));
        this.menu_categoria_vod.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_categoria_vod));
        this.menu_imdb_vod.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_imdb_vod));
        this.menu_kids_vod.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_kids_vod));
        this.menu_search_vod.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_search_vod));
        this.menu_favoritos_vod.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_favoritos_vod));

        this.menu_todos_vod.requestFocus();
        mSessionManager = new SessionManager(this.context);

        jsonArray = mSessionManager.loadArrayCategorias2("gruposMr", context);

        try {
            for (int n = 0; n < jsonArray.length(); n++) {
                JSONObject tmpObj = jsonArray.getJSONObject(n);
                if (tmpObj != null) {
                    String chid = tmpObj.optString("ID");
                    String chName = mSessionManager.AlteraCaracteres(tmpObj.optString("Nome"));
                    String chImagem = tmpObj.optString("Imagem");

                    ListPraiasSetterGetter mProgramSetterGetter = new ListPraiasSetterGetter();
                    mProgramSetterGetter.setPraias_id(chid);
                    mProgramSetterGetter.setPraias_name(chName);
                    mProgramSetterGetter.setPraias_imageGroup(chImagem);
                    list_item_categories.add(mProgramSetterGetter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonArray = mSessionManager.loadArrayCategorias3("sagasMr", context);

        try {
            for (int n = 0; n < jsonArray.length(); n++) {
                JSONObject tmpObj = jsonArray.getJSONObject(n);
                if (tmpObj != null) {
                    String chid = tmpObj.optString("ID");
                    String chName = mSessionManager.AlteraCaracteres(tmpObj.optString("Nome"));

                    ListPraiasSetterGetter mProgramSetterGetter = new ListPraiasSetterGetter();
                    mProgramSetterGetter.setPraias_id(chid);
                    mProgramSetterGetter.setPraias_name(chName);
                    list_item_sagas.add(mProgramSetterGetter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonArrayAnos = mSessionManager.loadArrayAnos("AnosMr", context);
        try {
            for (int n = 0; n < jsonArrayAnos.length(); n++) {
                JSONObject tmpObj = jsonArrayAnos.getJSONObject(n);
                if (tmpObj != null) {
                    String chid = tmpObj.optString("ID");
                    String chName = mSessionManager.AlteraCaracteres(tmpObj.optString("Nome"));

                    ListPraiasSetterGetter mProgramSetterGetter = new ListPraiasSetterGetter();
                    mProgramSetterGetter.setPraias_id(chid);
                    mProgramSetterGetter.setPraias_name(chName);
                    list_item_anos.add(mProgramSetterGetter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    protected void onResume() {
        hideSystemUi();
        super.onResume();
    }

    private void makeButtonsClickable() {
        this.menu_todos_vod.setOnClickListener(this);
        this.menu_destaques_vod.setOnClickListener(this);
        this.menu_anos_vod.setOnClickListener(this);
        this.menu_categoria_vod.setOnClickListener(this);
        this.menu_imdb_vod.setOnClickListener(this);
        this.menu_kids_vod.setOnClickListener(this);
        this.menu_search_vod.setOnClickListener(this);
        this.menu_favoritos_vod.setOnClickListener(this);
    }

    public Intent i;
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_todos_vod:
                List<String> appNames2 = new ArrayList<String>();
                for (ListPraiasSetterGetter info : list_item_sagas) {
                    appNames2.add(info.getPraias_name());
                }

                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);

                builder2.setItems(appNames2.toArray(new CharSequence[appNames2.size()]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        ListPraiasSetterGetter selectedItem = list_item_sagas.get(item);
                        i = new Intent(context, VodMenuFilmesAPP4MovSelectActivity.class);
                        i.putExtra("selType", "sagas");
                        i.putExtra("selyear", selectedItem.getPraias_id());
                        i.putExtra("selnome", "Saga: " + selectedItem.getPraias_name());
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                    }

                });
                builder2.setIcon(R.drawable.questionmark);
                builder2.show();
                return;
                /*i = new Intent(context, VodMenuFilmesAPP4MovSelectActivity.class);
                i.putExtra("selType", "todos");
                i.putExtra("selyear", "");
                i.putExtra("selnome", "Todos");
                startActivity(i);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;*/
            case R.id.menu_destaques_vod:
                i = new Intent(context, VodMenuFilmesAPP4MovSelectActivity.class);
                i.putExtra("selType", "destaques");
                i.putExtra("selyear", "");
                i.putExtra("selnome", "Destaques");
                startActivity(i);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.menu_anos_vod:
                List<String> appNames = new ArrayList<>();
                for (ListPraiasSetterGetter info : list_item_anos) {
                    appNames.add(info.getPraias_name());
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(appNames.toArray(new CharSequence[appNames.size()]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        ListPraiasSetterGetter selectedItem = list_item_anos.get(item);
                        i = new Intent(context, VodMenuFilmesAPP4MovSelectActivity.class);
                        i.putExtra("selType", "anos");
                        i.putExtra("selyear", selectedItem.getPraias_id());
                        i.putExtra("selnome", "Ano: " + selectedItem.getPraias_name());
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                    }
                });

                builder.setIcon(R.drawable.questionmark);
                builder.show();
                return;
            case R.id.menu_categoria_vod:
                List<String> appNames3 = new ArrayList<String>();
                for (ListPraiasSetterGetter info : list_item_categories) {
                    appNames3.add(info.getPraias_name());
                }

                AlertDialog.Builder builder3 = new AlertDialog.Builder(context);

                builder3.setItems(appNames3.toArray(new CharSequence[appNames3.size()]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        ListPraiasSetterGetter selectedItem = list_item_categories.get(item);
                        i = new Intent(context, VodMenuFilmesAPP4MovSelectActivity.class);
                        i.putExtra("selType", "categoria");
                        i.putExtra("selyear", selectedItem.getPraias_id());
                        i.putExtra("selnome", "Categoria: " + selectedItem.getPraias_name());
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                    }

                });
                builder3.setIcon(R.drawable.questionmark);
                builder3.show();
                return;
            case R.id.menu_imdb_vod:
                i = new Intent(context, VodMenuFilmesAPP4MovSelectActivity.class);
                i.putExtra("selType", "rating");
                i.putExtra("selyear", "");
                i.putExtra("selnome", "Por Ranking IMDB");
                startActivity(i);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.menu_kids_vod:
                i = new Intent(context, VodMenuFilmesAPP4MovSelectActivity.class);
                i.putExtra("selType", "kids");
                i.putExtra("selyear", "");
                i.putExtra("selnome", "Para Crianças");
                startActivity(i);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.menu_favoritos_vod:
                i = new Intent(context, VodMenuFilmesAPP4MovSelectActivity.class);
                i.putExtra("selType", "favoritos");
                i.putExtra("selyear", "");
                i.putExtra("selnome", "Meus Filmes Favoritos");
                startActivity(i);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.menu_search_vod:
                showSearchFilmTextDialog();
                return;
            default:
                return;
        }
    }


    CharSequence[] selPesquisaMenu = {"Pesquisa"};

    private int pesquisa = 0;
    public void showSearchFilmTextDialog() {
        int cnt = 3;
        selPesquisaMenu = new CharSequence[cnt];
        selPesquisaMenu[0] =  "Por: Nome PT ou Nome EN";
        selPesquisaMenu[1] =  "Por: Realizador";
        selPesquisaMenu[2] =  "Por: Actor";

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Escolha o tipo de pesquisa?");
        alertDialog.setItems(selPesquisaMenu, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(item == 0)
                {
                    pesquisa = 0;
                }else if(item == 1)
                {
                    pesquisa = 1;

                }else if(item == 2)
                {
                    pesquisa = 2;
                }
                showSearchFilmTextDialog2();
            }

        });
        alertDialog.setIcon(R.drawable.questionmark);
        alertDialog.show();
    }

    public void showSearchFilmTextDialog2() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_adult_password, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setIcon(R.drawable.questionmark);
        final EditText serachWord_Txt = (EditText) promptsView.findViewById(R.id.et_adult_password);
        final TextView txt_title = (TextView) promptsView.findViewById(R.id.textView1);
        txt_title.setText("Pesquisar:");

        // set dialog message
        alertDialogBuilder
                .setCancelable(true)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                /** DO THE METHOD HERE WHEN PROCEED IS CLICKED*/
                                String search_txt = (serachWord_Txt.getText()).toString();

                                /** CHECK FOR USER'S INPUT **/
                                if (!serachWord_Txt.equals("")) {
                                    Intent i = new Intent(context, VodMenuFilmesAPP4MovSelectActivity.class);
                                    i.putExtra("selType", "pesquisa"+pesquisa);
                                    i.putExtra("selnome", "Pesquisa de: " + search_txt);

                                    search_txt = search_txt.replace(" ", "%20");
                                    search_txt = search_txt.replace(" ", "%20");

                                    i.putExtra("selyear", search_txt);
                                    i.putExtra("selQuality", 2);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.fade_out, R.anim.fade_in);

                                } else {
//                                    Log.d(user_text,"string is empty");
                                    String message = "Inserir algo para pesquisar!";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setIcon(R.drawable.questionmark);
                                    builder.setTitle(mSessionManager.getNameAPP() + " - Erro");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("Cancelar", null);
                                    builder.setNegativeButton("Tentar de novo", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            showSearchFilmTextDialog();
                                        }
                                    });
                                    builder.show();
                                }
                            }
                        })
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }

                        }

                );
        // show it
        alertDialogBuilder.show();
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
