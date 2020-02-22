package com.liveitandroid.liveit.view.interfaces;

import android.widget.TextView;

import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.model.callback.LiveStreamCategoriesCallback;
import com.liveitandroid.liveit.model.callback.LiveStreamsCallback;
import com.liveitandroid.liveit.model.callback.LiveStreamsEpgCallback;
import java.util.ArrayList;
import java.util.List;

public interface LiveStreamsInterface extends BaseInterface {
    void liveStreamCategories(List<LiveStreamCategoriesCallback> list);

    void liveStreams(List<LiveStreamsCallback> list, ArrayList<FavouriteDBModel> arrayList);

    void liveStreamsEpg(LiveStreamsEpgCallback liveStreamsEpgCallback, TextView textView, TextView textView2);
}
