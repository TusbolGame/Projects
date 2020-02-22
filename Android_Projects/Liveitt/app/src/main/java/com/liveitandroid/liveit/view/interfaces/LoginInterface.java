package com.liveitandroid.liveit.view.interfaces;

import com.liveitandroid.liveit.model.callback.LoginCallback;

public interface LoginInterface extends BaseInterface {
    void stopLoader();

    void validateLogin(LoginCallback loginCallback, String str);
}
