package com.evilkingmedia.livetv;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.evilkingmedia.share.ShareActivity;
import com.google.android.gms.ads.AdView;

import com.evilkingmedia.Constant;
import com.evilkingmedia.R;
import com.evilkingmedia.demand.WebViewActivity;
import com.evilkingmedia.utility.ApiClient;
import com.evilkingmedia.utility.CheckXml;
import com.evilkingmedia.utility.CustomKeyboardHandler;
import com.evilkingmedia.utility.GetXmlInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveActivityCategory extends AppCompatActivity {
    LinearLayout listeiptv, tvshare, m3u_creator, tfb, tvwebcaster, harem, list_protection;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_live_category);
        } else {
            setContentView(R.layout.activity_live_category_portrait);
        }

        listeiptv = findViewById(R.id.listeiptvLayout);
        tvshare = findViewById(R.id.tvshareLayout);
        m3u_creator = findViewById(R.id.m3ucreator_layout);
        tfb = findViewById(R.id.tfbLayout);
        tvwebcaster = findViewById(R.id.tvwebcasterLayout);
        list_protection = findViewById(R.id.listprotection_layout);
        harem = findViewById(R.id.haremLayout);

        adView = findViewById(R.id.adView);
        Constant.showAdmob(this, adView);

        listeiptv.setOnClickListener(view -> Constant.openExternalBrowser(LiveActivityCategory.this, Constant.LIVETV_LISTEIPTV_URL));

        tvshare.setOnClickListener(view -> {
            Intent intent = new Intent(LiveActivityCategory.this, ShareActivity.class);
            intent.putExtra("type", "live");
            startActivity(intent);
        });

        m3u_creator.setOnClickListener(view -> {
            Intent intent = new Intent(LiveActivityCategory.this, WebViewActivity.class);
            intent.putExtra("videoUrl", Constant.LIVETV_M3U_CREATOR_URL);
            startActivity(intent);
        });

        tfb.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.TFBUrl));
            startActivity(intent);
        });

        tvwebcaster.setOnClickListener(v -> Constant.openWVCapp(LiveActivityCategory.this, Constant.TV_WEB_CASTER_URL));

        harem.setOnClickListener(v -> {
            Dialog password_dialog = new Dialog(LiveActivityCategory.this);
            password_dialog.setContentView(R.layout.harempassword_dialog);
            TextView negativeBtn, positiveBtn;
            EditText etPassword;
            negativeBtn = password_dialog.findViewById(R.id.negativeBtn);
            positiveBtn = password_dialog.findViewById(R.id.positiveBtn);
            etPassword = password_dialog.findViewById(R.id.etPassword);

            etPassword.setFocusable(true);
            CustomKeyboardHandler.showKeyboard(LiveActivityCategory.this);

            etPassword.setOnFocusChangeListener((v1, hasFocus) -> {
                if (hasFocus){
                    CustomKeyboardHandler.showKeyboard(LiveActivityCategory.this);
                } else {
                    CustomKeyboardHandler.hiddenKeyboard(LiveActivityCategory.this, password_dialog.getWindow().getDecorView().getWindowToken());
                }
            });

            negativeBtn.setOnClickListener(v12 -> password_dialog.dismiss());

            positiveBtn.setOnClickListener(v13 -> {
                String manual_pass = etPassword.getText().toString();
                if (manual_pass.isEmpty()){
                    Toast.makeText(LiveActivityCategory.this, "Please enter a valid password!", Toast.LENGTH_LONG).show();
                } else {
                    ProgressDialog progressDialog = new ProgressDialog(LiveActivityCategory.this);
                    progressDialog.setMessage("Checking your password from server...");
                    progressDialog.show();
                    Call<GetXmlInfo> apiCall = ApiClient.getApiXmlClient().getXml();
                    apiCall.enqueue(new Callback<GetXmlInfo>() {
                        @Override
                        public void onResponse(Call<GetXmlInfo> call, Response<GetXmlInfo> response) {
                            assert response.body() != null;
                            if (manual_pass.equals(response.body().getHaremPass())){
                                CustomKeyboardHandler.hiddenKeyboard(LiveActivityCategory.this, etPassword.getWindowToken());
                                startActivity(new Intent(LiveActivityCategory.this, HaremActivity.class));
                                password_dialog.dismiss();
                                progressDialog.dismiss();
                            } else {
                                Dialog alertDialog = new Dialog(LiveActivityCategory.this);
                                alertDialog.setContentView(R.layout.notification_dialog);
                                LinearLayout passwordAlertLy = alertDialog.findViewById(R.id.passwordAlertLy);
                                TextView passalert_description = alertDialog.findViewById(R.id.passalert_description);
                                TextView negativeBtn1 = alertDialog.findViewById(R.id.negativeBtn);
                                TextView positiveBtn1 = alertDialog.findViewById(R.id.positiveBtn);
                                passwordAlertLy.setVisibility(View.VISIBLE);
                                negativeBtn1.setVisibility(View.GONE);
                                positiveBtn1.setText("OK");
                                String alertStr = "Your password is not correct! Please ask to admin!";
                                passalert_description.setText(alertStr);
                                positiveBtn1.setOnClickListener(v131 -> alertDialog.dismiss());
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alertDialog.getWindow().setLayout(getResources().getDimensionPixelSize(R.dimen.popup_width), WindowManager.LayoutParams.WRAP_CONTENT);
                                alertDialog.show();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<GetXmlInfo> call, Throwable t) {
                            Log.d("EKM", t.getMessage());
                        }
                    });
                }
            });

            password_dialog.show();
        });

        list_protection.setOnClickListener(v -> {
            Intent intent = new Intent(LiveActivityCategory.this, WebViewActivity.class);
            intent.putExtra("videoUrl", Constant.LIVETV_LIST_PROTECTION_URL);
            startActivity(intent);
        });

        Constant.setFocusEvent(this, listeiptv, tvshare, m3u_creator, tfb, tvwebcaster, harem, list_protection);
        CheckXml.checkXml(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null){
            adView.resume();
        }
        CheckXml.checkXml(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null){
            adView.destroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adView != null){
            adView.pause();
        }
    }
}

