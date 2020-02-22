package com.liveitandroid.liveit.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.view.interfaces.XtreamPanelAPIInterface;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.callback.XtreamPanelAPICallback;
import com.liveitandroid.liveit.model.webrequest.RetrofitPost;
import com.liveitandroid.liveit.view.interfaces.XtreamPanelAPIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class XtreamPanelAPIPresenter {
    private Context context;
    private XtreamPanelAPIInterface xtreamPanelAPIInterface;

    public XtreamPanelAPIPresenter(XtreamPanelAPIInterface xtreamPanelAPIInterface, Context context) {
        this.xtreamPanelAPIInterface = xtreamPanelAPIInterface;
        this.context = context;
    }

    public void panelAPI(final String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).panelAPI(AppConst.CONTENT_TYPE, username, password).enqueue(new Callback<XtreamPanelAPICallback>() {
                public void onResponse(@NonNull Call<XtreamPanelAPICallback> call, @NonNull Response<XtreamPanelAPICallback> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        //XtreamPanelAPIPresenter.this.xtreamPanelAPIInterface.panelAPI((XtreamPanelAPICallback) response.body(), username);
                    } else if (response.body() == null) {
                        XtreamPanelAPIPresenter.this.xtreamPanelAPIInterface.panelApiFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                        XtreamPanelAPIPresenter.this.xtreamPanelAPIInterface.onFinish();
                        if (XtreamPanelAPIPresenter.this.context != null) {
                            XtreamPanelAPIPresenter.this.xtreamPanelAPIInterface.onFailed(XtreamPanelAPIPresenter.this.context.getResources().getString(R.string.invalid_request));
                        }
                    }
                }

                public void onFailure(@NonNull Call<XtreamPanelAPICallback> call, @NonNull Throwable t) {
                    XtreamPanelAPIPresenter.this.xtreamPanelAPIInterface.panelApiFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                    XtreamPanelAPIPresenter.this.xtreamPanelAPIInterface.onFinish();
                    XtreamPanelAPIPresenter.this.xtreamPanelAPIInterface.onFailed(t.getMessage());
                }
            });
        }
    }
}
