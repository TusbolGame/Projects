package com.liveitandroid.liveit.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.view.interfaces.XMLTVInterface;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.callback.XMLTVCallback;
import com.liveitandroid.liveit.model.webrequest.RetrofitPost;
import com.liveitandroid.liveit.view.interfaces.XMLTVInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class XMLTVPresenter {
    private Context context;
    private XMLTVInterface xmlTvInterface;

    class C15491 implements Callback<XMLTVCallback> {
        C15491() {
        }

        public void onResponse(@NonNull Call<XMLTVCallback> call, @NonNull Response<XMLTVCallback> response) {
            if (response.isSuccessful()) {
                XMLTVPresenter.this.xmlTvInterface.epgXMLTV((XMLTVCallback) response.body());
            } else if (response.body() == null) {
                XMLTVPresenter.this.xmlTvInterface.epgXMLTVUpdateFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                if (XMLTVPresenter.this.context != null) {
                    XMLTVPresenter.this.xmlTvInterface.onFailed(XMLTVPresenter.this.context.getResources().getString(R.string.invalid_request));
                }
            }
        }

        public void onFailure(@NonNull Call<XMLTVCallback> call, @NonNull Throwable t) {
            XMLTVPresenter.this.xmlTvInterface.epgXMLTVUpdateFailed(AppConst.DB_UPDATED_STATUS_FAILED);
            XMLTVPresenter.this.xmlTvInterface.onFinish();
            XMLTVPresenter.this.xmlTvInterface.onFailed(t.getMessage());
        }
    }

    public XMLTVPresenter(XMLTVInterface xmlTvInterface, Context context) {
        this.xmlTvInterface = xmlTvInterface;
        this.context = context;
    }

    public void epgXMLTV(String username, String password) {
        this.xmlTvInterface.atStart();
        Retrofit retrofitObject = Utils.retrofitObjectXML(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).epgXMLTV(AppConst.CONTENT_TYPE, username, password).enqueue(new C15491());
        }
    }
}
