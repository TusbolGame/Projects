package com.liveitandroid.liveit.view.interfaces;

import com.liveitandroid.liveit.model.callback.XtreamPanelAPICallback;

public interface XtreamPlayerAPIInterface extends BaseInterface {
    void panelAPI(XtreamPanelAPICallback xtreamPanelAPICallback, String str);

    void panelApiFailed(String str);
}