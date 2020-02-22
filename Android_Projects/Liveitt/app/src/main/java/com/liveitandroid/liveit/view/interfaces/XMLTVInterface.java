package com.liveitandroid.liveit.view.interfaces;

import com.liveitandroid.liveit.model.callback.XMLTVCallback;

public interface XMLTVInterface extends BaseInterface {
    void epgXMLTV(XMLTVCallback xMLTVCallback);

    void epgXMLTVUpdateFailed(String str);
}
