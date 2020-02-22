package com.liveitandroid.liveit.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;

import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.view.interfaces.LoginInterface;
import com.google.common.net.HttpHeaders;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.callback.LoginCallback;
import com.liveitandroid.liveit.model.webrequest.RetrofitPost;
import com.liveitandroid.liveit.view.interfaces.LoginInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginPresenter {
    private Context context;
    private LoginInterface loginInteface;
    private SharedPreferences loginPreferencesServerURl;
    private Editor loginPreferencesServerURlPut;

    public LoginPresenter(LoginInterface loginInteface, Context context) {
        this.loginInteface = loginInteface;
        this.context = context;
    }

    public void validateLogin(final String username, final String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).validateLogin(AppConst.CONTENT_TYPE, username, password).enqueue(new Callback<LoginCallback>() {
                public void onResponse(@NonNull Call<LoginCallback> call, @NonNull Response<LoginCallback> response) {
                    LoginPresenter.this.loginInteface.atStart();
                    if (response.isSuccessful()) {
                        LoginPresenter.this.loginInteface.validateLogin((LoginCallback) response.body(), AppConst.VALIDATE_LOGIN);
                        LoginPresenter.this.loginInteface.onFinish();
                    } else if (response.code() == 301 || response.code() == 302) {
                        String location = response.raw().header(HttpHeaders.LOCATION);
                        if (location != null) {
                            String[] newLocation = location.split("/player_api.php");
                            LoginPresenter.this.loginPreferencesServerURl = LoginPresenter.this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_SERVER_URL, 0);
                            LoginPresenter.this.loginPreferencesServerURlPut = LoginPresenter.this.loginPreferencesServerURl.edit();
                            LoginPresenter.this.loginPreferencesServerURlPut.putString(AppConst.LOGIN_PREF_SERVER_URL_MAG, newLocation[0]);
                            LoginPresenter.this.loginPreferencesServerURlPut.apply();
                            LoginPresenter.this.validateLogin(username, password);
                            return;
                        }
                        LoginPresenter.this.loginInteface.onFinish();
                        LoginPresenter.this.loginInteface.onFailed(AppConst.INVALID_REQUEST);
                    } else if (response.code() == 404) {
                        LoginPresenter.this.loginInteface.onFinish();
                        LoginPresenter.this.loginInteface.onFailed("ERROR Code 404: Network error occured! Please try again");
                    } else if (response.body() == null) {
                        LoginPresenter.this.loginInteface.onFinish();
                        if (LoginPresenter.this.context != null) {
                            LoginPresenter.this.loginInteface.onFailed(LoginPresenter.this.context.getResources().getString(R.string.invalid_request));
                        }
                    }
                }

                public void onFailure(@NonNull Call<LoginCallback> call, @NonNull Throwable t) {
                    if (t.getMessage() != null && t.getMessage().contains("Unable to resolve host")) {
                        LoginPresenter.this.loginInteface.onFinish();
                        LoginPresenter.this.loginInteface.onFailed("Unable to resolve host");
                    } else if (t.getMessage() == null || !t.getMessage().contains("Failed to connect")) {
                        LoginPresenter.this.loginInteface.onFinish();
                        if (t.getMessage() != null) {
                            LoginPresenter.this.loginInteface.onFailed(t.getMessage());
                        } else {
                            LoginPresenter.this.loginInteface.onFailed("ERROR Code 1: Network error occured! Please try again");
                        }
                    } else {
                        LoginPresenter.this.loginInteface.onFinish();
                        LoginPresenter.this.loginInteface.onFailed(AppConst.FAILED_TO_CONNECT);
                    }
                }
            });
        } else if (retrofitObject == null) {
            this.loginInteface.stopLoader();
        }
    }
}
