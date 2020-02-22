package com.liveitandroid.liveit.WebServiceHandler;

public interface MainAsynListener<T> {
    void onPostError(int i);

    void onPostSuccess(T t, int i, boolean z);
}
