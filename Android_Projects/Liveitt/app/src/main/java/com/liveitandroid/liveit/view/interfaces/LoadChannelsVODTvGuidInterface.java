package com.liveitandroid.liveit.view.interfaces;

import com.liveitandroid.liveit.model.callback.XMLTVCallback;
import com.liveitandroid.liveit.model.callback.XtreamPanelAPICallback;

public interface LoadChannelsVODTvGuidInterface {
    void laodTvGuideFailed(String str, String str2);

    void loadChannelsAndVOD(XtreamPanelAPICallback xtreamPanelAPICallback, String str);

    void loadChannelsAndVodFailed(String str, String str2);

    void loadTvGuide(XMLTVCallback xMLTVCallback);
}
