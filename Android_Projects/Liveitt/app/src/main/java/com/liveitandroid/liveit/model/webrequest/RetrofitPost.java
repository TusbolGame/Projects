package com.liveitandroid.liveit.model.webrequest;

import com.google.gson.JsonElement;
import com.liveitandroid.liveit.model.callback.GetSeriesStreamCallback;
import com.liveitandroid.liveit.model.callback.GetSeriesStreamCategoriesCallback;
import com.liveitandroid.liveit.model.callback.LiveStreamCategoriesCallback;
import com.liveitandroid.liveit.model.callback.LiveStreamsCallback;
import com.liveitandroid.liveit.model.callback.LiveStreamsEpgCallback;
import com.liveitandroid.liveit.model.callback.LoginCallback;
import com.liveitandroid.liveit.model.callback.VodCategoriesCallback;
import com.liveitandroid.liveit.model.callback.VodInfoCallback;
import com.liveitandroid.liveit.model.callback.VodStreamsCallback;
import com.liveitandroid.liveit.model.callback.XMLTVCallback;
import com.liveitandroid.liveit.model.callback.XtreamPanelAPICallback;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitPost {
    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<GetSeriesStreamCallback>> allSeriesStreams(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4);

    @FormUrlEncoded
    @POST("/xmltv.php")
    Call<XMLTVCallback> epgXMLTV(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<LiveStreamCategoriesCallback>> liveStreamCategories(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<LiveStreamsCallback>> liveStreams(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4, @Field("category_id") String str5);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<LiveStreamsEpgCallback> liveStreamsEpg(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4, @Field("stream_id") Integer num);

    @FormUrlEncoded
    @POST("/panel_api.php")
    Call<XtreamPanelAPICallback> panelAPI(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<JsonElement> seasonsEpisode(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4, @Field("series_id") String str5);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<GetSeriesStreamCategoriesCallback>> seriesCategories(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<LoginCallback> validateLogin(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<VodCategoriesCallback>> vodCategories(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<VodInfoCallback> vodInfo(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4, @Field("vod_id") int i);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<VodStreamsCallback>> vodStreams(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4, @Field("category_id") String str5);
}