package com.liveitandroid.liveit.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.database.DatabaseUpdatedStatusDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.presenter.XMLTVPresenter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class PlayerSelectionActivity extends AppCompatActivity implements OnClickListener {
    private int actionBarHeight;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    @BindView(R.id.spinner_player_tv)
    Spinner spinner1;
    @BindView(R.id.spinner_player_filmes_lista)
    Spinner spinner2;
    @BindView(R.id.spinner_player_filmes_app)
    Spinner spinner3;
    @BindView(R.id.spinner_player_gravacao)
    Spinner spinner4;
    @BindView(R.id.spinner_player_programas)
    Spinner spinner5;
    @BindView(R.id.spinner_player_praias)
    Spinner spinner6;
    @BindView(R.id.spinner_player_radios)
    Spinner spinner7;
    private Context context;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    SessionManager mSessionManager;
    Spinner spinner_player_gravacao, spinner_player_praias, spinner_player_radios, spinner_player_programas, spinner_player_tv, spinner_player_filmes_app, spinner_player_filmes_lista;
    String[] select_player_1, select_player_2;
    ArrayAdapter Adapter2, Adapter3,Adapter6, Adapter8, Adapter9, Adapter10, Adapter11;
    class C16621 implements Runnable {
        C16621() {
        }

        public void run() {
            try {
                String dateValue = Calendar.getInstance().getTime().toString();
                String currentCurrentTime = Utils.getTime(PlayerSelectionActivity.this.context);
                String currentCurrentDate = Utils.getDate(dateValue);
                if (PlayerSelectionActivity.this.time != null) {
                    PlayerSelectionActivity.this.time.setText(currentCurrentTime);
                }
                if (PlayerSelectionActivity.this.date != null) {
                    PlayerSelectionActivity.this.date.setText(currentCurrentDate);
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
                    PlayerSelectionActivity.this.doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e2) {
                }
            }
        }
    }

    private class OnFocusChangeAccountListener implements OnFocusChangeListener {
        private final View view;

        public OnFocusChangeAccountListener(View view) {
            this.view = view;
        }

        @SuppressLint({"ResourceType"})
        public void onFocusChange(View v, boolean hasFocus) {
            float to = 1.0f;
            if (hasFocus) {
                if (hasFocus) {
                    to = 1.05f;
                }

                if (this.view.getTag().equals("1")) {
                    performScaleXAnimation(to);
                    performScaleYAnimation(to);
                    this.view.setBackgroundResource(R.drawable.logout_btn_effect);
                } else if (this.view.getTag().equals("2")) {
                    performScaleXAnimation(to);
                    performScaleYAnimation(to);
                    this.view.setBackgroundResource(R.drawable.logout_btn_effect);
                } else if (this.view.getTag().equals("3")) {
                    performScaleXAnimation(to);
                    performScaleYAnimation(to);
                    this.view.setBackgroundResource(R.drawable.logout_btn_effect);
                } else if (this.view.getTag().equals("4")) {
                    performScaleXAnimation(to);
                    performScaleYAnimation(to);
                    this.view.setBackgroundResource(R.drawable.logout_btn_effect);
                } else if (this.view.getTag().equals("5")) {
                    performScaleXAnimation(to);
                    performScaleYAnimation(to);
                    this.view.setBackgroundResource(R.drawable.logout_btn_effect);
                } else if (this.view.getTag().equals("6")) {
                    performScaleXAnimation(to);
                    performScaleYAnimation(to);
                    this.view.setBackgroundResource(R.drawable.logout_btn_effect);
                } else {
                    performScaleXAnimation(1.12f);
                    performScaleYAnimation(1.12f);
                }
            } else if (!hasFocus) {
                if (hasFocus) {
                    to = 1.09f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                if (this.view.getTag().equals("1")) {
                    this.view.setBackgroundResource(R.color.white);
                } else if (this.view.getTag().equals("2")) {
                    this.view.setBackgroundResource(R.color.white);
                } else if (this.view.getTag().equals("3")) {
                    this.view.setBackgroundResource(R.color.white);
                } else if (this.view.getTag().equals("4")) {
                    this.view.setBackgroundResource(R.color.white);
                } else if (this.view.getTag().equals("5")) {
                    this.view.setBackgroundResource(R.color.white);
                } else if (this.view.getTag().equals("6")) {
                    this.view.setBackgroundResource(R.color.white);
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
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);
        ButterKnife.bind(this);
        focusInitialize();
        changeStatusBarColor();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getWindow().setFlags(1024, 1024);

        mSessionManager = new SessionManager(this);

        spinner_player_tv = (Spinner) findViewById(R.id.spinner_player_tv);
        spinner_player_filmes_lista = (Spinner) findViewById(R.id.spinner_player_filmes_lista);
        spinner_player_gravacao = (Spinner) findViewById(R.id.spinner_player_gravacao);
        spinner_player_praias = (Spinner) findViewById(R.id.spinner_player_praias);
        spinner_player_radios = (Spinner) findViewById(R.id.spinner_player_radios);
        spinner_player_programas = (Spinner) findViewById(R.id.spinner_player_programas);
        spinner_player_filmes_app = (Spinner) findViewById(R.id.spinner_player_filmes_app);

        select_player_1 = new String[]{"Player Embutido (Padrão)", "Player Embutido 2", "ExoPlayer"};
        select_player_2 = new String[]{"Player Embutido (Padrão)", "VLC Player", "MX Player", "System select"};
        initialize();

        new Thread(new CountDownRunner()).start();
    }

    public void doWork() {
        runOnUiThread(new C16621());
    }

    private void focusInitialize() {
        if (this.spinner1 != null) {
            this.spinner1.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.spinner1));
            this.spinner1.requestFocus();
        }
        if (this.spinner2 != null) {
            this.spinner2.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.spinner2));
        }
        if (this.spinner3 != null) {
            this.spinner3.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.spinner3));
        }
        if (this.spinner4 != null) {
            this.spinner4.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.spinner4));
        }
        if (this.spinner5 != null) {
            this.spinner5.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.spinner5));
        }
        if (this.spinner6 != null) {
            this.spinner6.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.spinner6));
        }
        if (this.spinner7 != null) {
            this.spinner7.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.spinner7));
        }}

    private void initialize() {
        this.context = this;

        Adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, select_player_1);
        Adapter2.setDropDownViewResource(R.layout.spinner_layout_liveit);
        spinner_player_tv.setAdapter(Adapter2);

        Adapter3 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, select_player_1);
        Adapter3.setDropDownViewResource(R.layout.spinner_layout_liveit);
        spinner_player_filmes_lista.setAdapter(Adapter3);

        Adapter6 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, select_player_1);
        Adapter6.setDropDownViewResource(R.layout.spinner_layout_liveit);
        spinner_player_gravacao.setAdapter(Adapter6);

        Adapter8 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, select_player_2);
        Adapter8.setDropDownViewResource(R.layout.spinner_layout_liveit);
        spinner_player_praias.setAdapter(Adapter8);

        Adapter9 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, select_player_2);
        Adapter9.setDropDownViewResource(R.layout.spinner_layout_liveit);
        spinner_player_programas.setAdapter(Adapter9);

        Adapter10 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, select_player_2);
        Adapter10.setDropDownViewResource(R.layout.spinner_layout_liveit);
        spinner_player_filmes_app.setAdapter(Adapter10);

        Adapter11 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, select_player_2);
        Adapter11.setDropDownViewResource(R.layout.spinner_layout_liveit);
        spinner_player_radios.setAdapter(Adapter11);

        spinner_player_tv.requestFocus();
        spinner_player_tv.setSelection(mSessionManager.getcheckedplayerTV());
        spinner_player_filmes_lista.setSelection(mSessionManager.getcheckedplayerFilmesLista());
        spinner_player_gravacao.setSelection(mSessionManager.getcheckedplayerGravacao());
        spinner_player_praias.setSelection(mSessionManager.getcheckedplayerPraias());
        spinner_player_radios.setSelection(mSessionManager.getcheckedplayerRadios());
        spinner_player_programas.setSelection(mSessionManager.getcheckedplayerProgramas());
        spinner_player_filmes_app.setSelection(mSessionManager.getcheckedplayerFilmesAPP());

        spinner_player_tv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String[] select_player_package = new String[]{"Embutido", "Embutido2", "ExoPlayer"};
                mSessionManager.setcheckedplayerTV(position);
                mSessionManager.setPlayerSelectedTV(select_player_1[position]);
                mSessionManager.setPackageSelectedTV(select_player_package[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        spinner_player_filmes_lista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String[] select_player_package = new String[]{"Embutido", "Embutido2", "ExoPlayer"};
                mSessionManager.setcheckedplayerFilmesLista(position);
                mSessionManager.setPlayerSelectedFilmesLista(select_player_1[position]);
                mSessionManager.setPackageSelectedFilmesLista(select_player_package[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        spinner_player_gravacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String[] select_player_package = new String[]{"Embutido", "Embutido2", "ExoPlayer"};
                mSessionManager.setcheckedplayerGravacao(position);
                mSessionManager.setPlayerSelectedGravacao(select_player_1[position]);
                mSessionManager.setPackageSelectedGravacao(select_player_package[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });


        spinner_player_filmes_app.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String[] select_player_package = new String[]{"Embutido", "org.videolan.vlc", "com.mxtech.videoplayer.ad", "System select"};
                mSessionManager.setcheckedplayerFilmesAPP(position);
                mSessionManager.setPlayerSelectedFilmesAPP(select_player_2[position]);
                mSessionManager.setPackageSelectedFilmesAPP(select_player_package[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        spinner_player_praias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String[] select_player_package = new String[]{"Embutido", "org.videolan.vlc", "com.mxtech.videoplayer.ad", "System select"};
                mSessionManager.setcheckedplayerPraias(position);
                mSessionManager.setPlayerSelectedPraias(select_player_2[position]);
                mSessionManager.setPackageSelectedPraias(select_player_package[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        spinner_player_radios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String[] select_player_package = new String[]{"Embutido", "org.videolan.vlc", "com.mxtech.videoplayer.ad", "System select"};
                mSessionManager.setcheckedplayerRadios(position);
                mSessionManager.setPlayerSelectedRadios(select_player_2[position]);
                mSessionManager.setPackageSelectedRadios(select_player_package[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        spinner_player_programas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String[] select_player_package = new String[]{"Embutido", "org.videolan.vlc", "com.mxtech.videoplayer.ad", "System select"};
                mSessionManager.setcheckedplayerProgramas(position);
                mSessionManager.setPlayerSelectedProgramas(select_player_2[position]);
                mSessionManager.setPackageSelectedProgramas(select_player_package[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });
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
        super.onBackPressed();
        //overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    public void startTvGuideActivity() {
        startActivity(new Intent(this, NewEPGActivity.class));
        finish();
    }

    public void startImportTvGuideActivity() {
        startActivity(new Intent(this, ImportEPGActivity.class));
        finish();
    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
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
}
