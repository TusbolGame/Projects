package com.liveitandroid.liveit.view.interfaces;

import com.liveitandroid.liveit.model.callback.VodInfoCallback;

public interface VodInterface extends BaseInterface {
    void vodInfo(VodInfoCallback vodInfoCallback);

    void vodInfoError(String str);
}
