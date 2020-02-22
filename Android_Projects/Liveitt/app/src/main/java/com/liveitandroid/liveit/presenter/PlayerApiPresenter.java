package com.liveitandroid.liveit.presenter;

import android.content.Context;

import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.callback.LiveStreamCategoriesCallback;
import com.liveitandroid.liveit.model.callback.LiveStreamsCallback;
import com.liveitandroid.liveit.model.callback.VodCategoriesCallback;
import com.liveitandroid.liveit.model.callback.VodStreamsCallback;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.database.SeriesStreamsDatabaseHandler;
import com.liveitandroid.liveit.view.activity.ImportStreamsActivity;
import com.liveitandroid.liveit.view.interfaces.PlayerApiInterface;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.callback.GetSeriesStreamCallback;
import com.liveitandroid.liveit.model.callback.GetSeriesStreamCategoriesCallback;
import com.liveitandroid.liveit.model.webrequest.RetrofitPost;
import com.liveitandroid.liveit.view.interfaces.PlayerApiInterface;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlayerApiPresenter {
    private Context context;
    private PlayerApiInterface playerApiInterface;
    LiveStreamDBHandler liveStreamDBHandler;
    private SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler;
    class C15451live implements Callback<List<LiveStreamCategoriesCallback>> {
        C15451live() {
        }

        public void onResponse(Call<List<LiveStreamCategoriesCallback>> call, Response<List<LiveStreamCategoriesCallback>> response) {
            if (response.body() != null && response.isSuccessful()) {
                PlayerApiPresenter.this.playerApiInterface.getCategories((List) response.body());
            } else if (response.body() == null) {
                PlayerApiPresenter.this.playerApiInterface.getStreamCatFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                PlayerApiPresenter.this.playerApiInterface.onFinish();
            }
        }

        public void onFailure(Call<List<LiveStreamCategoriesCallback>> call, Throwable t) {
            PlayerApiPresenter.this.playerApiInterface.getStreamCatFailed(AppConst.DB_UPDATED_STATUS_FAILED);
            PlayerApiPresenter.this.playerApiInterface.onFinish();
        }
    }

    class C15451vod implements Callback<List<VodCategoriesCallback>> {
        C15451vod() {
        }

        public void onResponse(Call<List<VodCategoriesCallback>> call, Response<List<VodCategoriesCallback>> response) {
            if (response.body() != null && response.isSuccessful()) {
                PlayerApiPresenter.this.playerApiInterface.getCategoriesVod((List) response.body());
            } else if (response.body() == null) {
                PlayerApiPresenter.this.playerApiInterface.getStreamCatVodFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                PlayerApiPresenter.this.playerApiInterface.onFinish();
            }
        }

        public void onFailure(Call<List<VodCategoriesCallback>> call, Throwable t) {
            PlayerApiPresenter.this.playerApiInterface.getStreamCatVodFailed(AppConst.DB_UPDATED_STATUS_FAILED);
            PlayerApiPresenter.this.playerApiInterface.onFinish();
        }
    }

    class C15451 implements Callback<List<GetSeriesStreamCategoriesCallback>> {
        C15451() {
        }

        public void onResponse(Call<List<GetSeriesStreamCategoriesCallback>> call, Response<List<GetSeriesStreamCategoriesCallback>> response) {
            if (response.body() != null && response.isSuccessful()) {
                PlayerApiPresenter.this.playerApiInterface.getSeriesCategories((List) response.body());
            } else if (response.body() == null) {
                PlayerApiPresenter.this.playerApiInterface.getSeriesStreamCatFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                PlayerApiPresenter.this.playerApiInterface.onFinish();
            }
        }

        public void onFailure(Call<List<GetSeriesStreamCategoriesCallback>> call, Throwable t) {
            PlayerApiPresenter.this.playerApiInterface.getSeriesStreamCatFailed(AppConst.DB_UPDATED_STATUS_FAILED);
            PlayerApiPresenter.this.playerApiInterface.onFinish();
        }
    }


    class C15462live implements Callback<List<LiveStreamsCallback>> {
        C15462live() {
        }

        public void onResponse(Call<List<LiveStreamsCallback>> call, Response<List<LiveStreamsCallback>> response) {
            if (response.body() != null && response.isSuccessful()) {
                PlayerApiPresenter.this.playerApiInterface.getStreams((List) response.body());
            } else if (response.body() == null) {
                PlayerApiPresenter.this.playerApiInterface.getStreamsFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                PlayerApiPresenter.this.playerApiInterface.onFinish();
                if (PlayerApiPresenter.this.context != null) {
                    PlayerApiPresenter.this.playerApiInterface.onFailed(PlayerApiPresenter.this.context.getResources().getString(R.string.invalid_request));
                }
            }
        }

        public void onFailure(Call<List<LiveStreamsCallback>> call, Throwable t) {
            PlayerApiPresenter.this.playerApiInterface.getStreamsFailed(AppConst.DB_UPDATED_STATUS_FAILED);
            PlayerApiPresenter.this.playerApiInterface.onFinish();
            PlayerApiPresenter.this.playerApiInterface.onFailed(t.getMessage());
        }
    }


    class C15462vod implements Callback<List<VodStreamsCallback>> {
        C15462vod() {
        }

        public void onResponse(Call<List<VodStreamsCallback>> call, Response<List<VodStreamsCallback>> response) {
            if (response.body() != null && response.isSuccessful()) {
                PlayerApiPresenter.this.playerApiInterface.getStreamsVod((List) response.body());
            } else if (response.body() == null) {
                PlayerApiPresenter.this.playerApiInterface.getStreamsVodFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                PlayerApiPresenter.this.playerApiInterface.onFinish();
                if (PlayerApiPresenter.this.context != null) {
                    PlayerApiPresenter.this.playerApiInterface.onFailed(PlayerApiPresenter.this.context.getResources().getString(R.string.invalid_request));
                }
            }
        }

        public void onFailure(Call<List<VodStreamsCallback>> call, Throwable t) {
            PlayerApiPresenter.this.playerApiInterface.getStreamsVodFailed(AppConst.DB_UPDATED_STATUS_FAILED);
            PlayerApiPresenter.this.playerApiInterface.onFinish();
            PlayerApiPresenter.this.playerApiInterface.onFailed(t.getMessage());
        }
    }

    class C15462 implements Callback<List<GetSeriesStreamCallback>> {
        C15462() {
        }

        public void onResponse(Call<List<GetSeriesStreamCallback>> call, Response<List<GetSeriesStreamCallback>> response) {
            if (response.body() != null && response.isSuccessful()) {
                PlayerApiPresenter.this.playerApiInterface.getSeriesStreams((List) response.body());
            } else if (response.body() == null) {
                PlayerApiPresenter.this.playerApiInterface.getSeriesStreamsFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                PlayerApiPresenter.this.playerApiInterface.onFinish();
                if (PlayerApiPresenter.this.context != null) {
                    PlayerApiPresenter.this.playerApiInterface.onFailed(PlayerApiPresenter.this.context.getResources().getString(R.string.invalid_request));
                }
            }
        }

        public void onFailure(Call<List<GetSeriesStreamCallback>> call, Throwable t) {
            PlayerApiPresenter.this.playerApiInterface.getSeriesStreamsFailed(AppConst.DB_UPDATED_STATUS_FAILED);
            PlayerApiPresenter.this.playerApiInterface.onFinish();
            PlayerApiPresenter.this.playerApiInterface.onFailed(t.getMessage());
        }
    }

    public PlayerApiPresenter(Context context, PlayerApiInterface playerApiInterface) {
        this.context = context;
        this.playerApiInterface = playerApiInterface;
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        this.seriesStreamsDatabaseHandler = new SeriesStreamsDatabaseHandler(this.context);
    }


    public void getStreamCat(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).liveStreamCategories(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_LIVE_CATEGORIES).enqueue(new C15451live());
        }
    }

    public void getStream(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).liveStreams(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_LIVE_STREAMS,"0").enqueue(new C15462live());
        }
    }

    public void getStreamVodCat(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).vodCategories(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_VOD_CATEGORIES).enqueue(new C15451vod());
        }
    }

    public void getStreamVod(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).vodStreams(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_VOD_STREAMS,"0").enqueue(new C15462vod());
        }
    }

    public void getSeriesStreamCat(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).seriesCategories(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_SERIES_CATEGORIES).enqueue(new C15451());
        }
    }

    public void getSeriesStream(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).allSeriesStreams(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_SERIES_STREAMS).enqueue(new C15462());
        }
    }
}
