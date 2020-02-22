package com.liveitandroid.liveit.view.interfaces;

import com.liveitandroid.liveit.model.callback.GetSeriesStreamCallback;
import com.liveitandroid.liveit.model.callback.GetSeriesStreamCategoriesCallback;
import com.liveitandroid.liveit.model.callback.LiveStreamCategoriesCallback;
import com.liveitandroid.liveit.model.callback.LiveStreamsCallback;
import com.liveitandroid.liveit.model.callback.VodCategoriesCallback;
import com.liveitandroid.liveit.model.callback.VodStreamsCallback;

import java.util.List;

public interface PlayerApiInterface extends BaseInterfaceV2 {
    void getSeriesCategories(List<GetSeriesStreamCategoriesCallback> list);

    void getSeriesStreamCatFailed(String str);

    void getSeriesStreams(List<GetSeriesStreamCallback> list);

    void getSeriesStreamsFailed(String str);

    void getCategories(List<LiveStreamCategoriesCallback> list);

    void getStreamCatFailed(String str);

    void getStreams(List<LiveStreamsCallback> list);

    void getStreamsFailed(String str);

    void getCategoriesVod(List<VodCategoriesCallback> list);

    void getStreamCatVodFailed(String str);

    void getStreamsVod(List<VodStreamsCallback> list);

    void getStreamsVodFailed(String str);
}
