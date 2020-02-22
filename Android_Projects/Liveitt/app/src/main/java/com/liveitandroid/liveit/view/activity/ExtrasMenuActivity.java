package com.liveitandroid.liveit.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExtrasMenuActivity extends AppCompatActivity implements OnClickListener {
    private static final int TIME_INTERVAL = 2000;
    @BindView(R.id.menu_hits)
    LinearLayout menu_hits;
    @BindView(R.id.menu_radios)
    LinearLayout menu_radios;
    @BindView(R.id.menu_praias)
    LinearLayout menu_praias;
    @BindView(R.id.menu_jornais)
    LinearLayout menu_jornais;
    @BindView(R.id.menu_programas)
    LinearLayout menu_programas;
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
                } else if (this.view.getTag().equals("3")) {
                    this.view.setBackgroundResource(R.drawable.catch_background);
                } else if (this.view.getTag().equals("4")) {
                    this.view.setBackgroundResource(R.drawable.live_tv_background);
                } else if (this.view.getTag().equals("5")) {
                    this.view.setBackgroundResource(R.drawable.catch_background);
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
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_04);
                } else if (this.view.getTag().equals("5")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_01);
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
        setContentView(R.layout.extras_menu_layout);
        ButterKnife.bind(this);
        changeStatusBarColor();
        hideSystemUi();
        makeButtonsClickable();
        this.menu_hits.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_hits));
        this.menu_praias.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_praias));
        this.menu_jornais.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_jornais));
        this.menu_programas.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_programas));
        this.menu_radios.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.menu_radios));

        this.menu_programas.requestFocus();

        mSessionManager = new SessionManager(this.context);
    }

    protected void onResume() {
        hideSystemUi();
        super.onResume();
    }

    private void makeButtonsClickable() {
        this.menu_hits.setOnClickListener(this);
        this.menu_praias.setOnClickListener(this);
        this.menu_jornais.setOnClickListener(this);
        this.menu_programas.setOnClickListener(this);
        this.menu_radios.setOnClickListener(this);
    }

    public Intent i;
    public void onClick(View view) {
        String app_name = getBaseContext().getApplicationInfo().loadLabel(getBaseContext().getPackageManager()).toString();
        AlertDialog.Builder builder2;
        switch (view.getId()) {
            case R.id.menu_programas:
                startActivity(new Intent(this, ProgramasActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.menu_jornais:
                startActivity(new Intent(this, JornaisActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.menu_praias:
                startActivity(new Intent(this, PraiasActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.menu_hits:
                startActivity(new Intent(this, HitsActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            case R.id.menu_radios:
                startActivity(new Intent(this, RadiosActivity.class));
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                return;
            default:
                return;
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
