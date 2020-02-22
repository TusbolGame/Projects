package com.liveitandroid.liveit.view.app_shell;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.liveitandroid.liveit.BuildConfig;
import com.liveitandroid.liveit.R;
import io.realm.Realm;
import io.realm.RealmConfiguration.Builder;

public class AppController extends Application {
    public static AppController sInstance;
    public static final String TAG = AppController.class
            .getSimpleName();
    protected String userAgent;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    public static AppController getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static synchronized AppController getInstanceSyn() {
        AppController appController;
        synchronized (AppController.class) {
            appController = sInstance;
        }
        return appController;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        sInstance = this;
        this.userAgent = Util.getUserAgent(this, getString(R.string.app_name));
        Realm.init(this);
        Realm.setDefaultConfiguration(new Builder().name("com.liveitandroid.liveit").deleteRealmIfMigrationNeeded().schemaVersion(1).build());
    }

    public Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory((Context) this, (TransferListener) bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(this.userAgent, bandwidthMeter);
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals(BuildConfig.FLAVOR);
    }

    public void onLowMemory() {
        Glide.get(this).clearMemory();
        super.onLowMemory();
    }

    public void onTrimMemory(int level) {
        Glide.get(this).trimMemory(level);
        super.onTrimMemory(level);
    }
}
