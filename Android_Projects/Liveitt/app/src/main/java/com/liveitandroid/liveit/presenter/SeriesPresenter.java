package com.liveitandroid.liveit.presenter;

import android.content.Context;

import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.view.interfaces.SeriesInterface;
import com.google.gson.JsonElement;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.webrequest.RetrofitPost;
import com.liveitandroid.liveit.view.interfaces.SeriesInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SeriesPresenter {
    private Context context;
    private SeriesInterface seriesInterface;

    class C15471 implements Callback<JsonElement> {
        C15471() {
        }

        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
            if (response != null && response.body() != null) {
                SeriesPresenter.this.seriesInterface.getSeriesEpisodeInfo((JsonElement) response.body());
            }
        }

        public void onFailure(Call<JsonElement> call, Throwable t) {
            SeriesPresenter.this.seriesInterface.onFinish();
            SeriesPresenter.this.seriesInterface.onFailed(t.getMessage());
            SeriesPresenter.this.seriesInterface.seriesError(t.getMessage());
        }
    }

    public SeriesPresenter(Context context, SeriesInterface seriesInterface) {
        this.context = context;
        this.seriesInterface = seriesInterface;
    }

    public void getSeriesEpisode(String username, String password, String seriesId) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).seasonsEpisode(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_SERIES_INFO, seriesId).enqueue(new C15471());
        }
    }
}
