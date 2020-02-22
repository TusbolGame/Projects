package com.liveitandroid.liveit.WebServiceHandler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import com.google.common.net.HttpHeaders;
import com.liveitandroid.liveit.WebServiceHandler.Webservices.getWebNames;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.FormBody.Builder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@SuppressLint({"SimpleDateFormat"})
public class CommonFunctions {
    private static String baseUrl = Webservices.getWebNames.Url;
    static MainAsynListener<String> listener;

    public static String getOkHttpClient(Context c, String Url, int flag, String tag, List<ParamsGetter> getters) {
        String strResponse = "";
        Log.e("onclick", " " + baseUrl + Url);
        try {
            Request.Builder requestt;
            int i;
            Builder formbody;
            OkHttpClient okk = new OkHttpClient();
            long j = 30;
            okk.newBuilder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(j, TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
            Request request = null;
            if (tag.equals("")) {
                request = new Request.Builder().url(baseUrl + Url).build();
            }
            if (tag.equalsIgnoreCase("GET")) {
                requestt = new Request.Builder();
                requestt.url(baseUrl + Url);
                if (getters != null) {
                    for (i = 0; i < getters.size(); i++) {
                        requestt.addHeader(((ParamsGetter) getters.get(i)).getKey(), ((ParamsGetter) getters.get(i)).getValues());
                    }
                }
                requestt.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
                request = requestt.build();
            }
            if (tag.equalsIgnoreCase("Form")) {
                formbody = new Builder();
                for (i = 0; i < getters.size(); i++) {
                    Log.e("KEY VLAUES", ">>>>    " + ((ParamsGetter) getters.get(i)).getKey() + "            " + ((ParamsGetter) getters.get(i)).getValues());
                    formbody.add(((ParamsGetter) getters.get(i)).getKey(), ((ParamsGetter) getters.get(i)).getValues());
                }
                request = new Request.Builder().url(baseUrl + Url).addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8").post(formbody.build()).build();
            }
            if (tag.equalsIgnoreCase("FormAPI")) {
                formbody = new Builder();
                for (i = 0; i < getters.size(); i++) {
                    Log.e("KEY VLAUES", ">>>>    " + ((ParamsGetter) getters.get(i)).getKey() + "            " + ((ParamsGetter) getters.get(i)).getValues());
                    formbody.add(((ParamsGetter) getters.get(i)).getKey(), ((ParamsGetter) getters.get(i)).getValues());
                }
                request = new Request.Builder().url(baseUrl + Url).addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8").post(formbody.build()).build();
            }
            if (tag.equalsIgnoreCase("DEL")) {
                requestt = new Request.Builder();
                requestt.url(baseUrl + Url);
                if (getters != null) {
                    for (i = 0; i < getters.size(); i++) {
                        requestt.addHeader(((ParamsGetter) getters.get(i)).getKey(), ((ParamsGetter) getters.get(i)).getValues());
                    }
                }
                requestt.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
                requestt.delete();
                request = requestt.build();
            }
            if (tag.equalsIgnoreCase("Multipart")) {
                MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
                MediaType MEDIA_TYPE_Video = MediaType.parse("video/*");
                MultipartBody.Builder builder = new MultipartBody.Builder();
                i = 0;
                while (i < getters.size()) {
                    if (((ParamsGetter) getters.get(i)).getFile() != null) {
                        MultipartBody.Builder type;
                        String key;
                        if (((ParamsGetter) getters.get(i)).getFile().getAbsolutePath().endsWith(".png") || ((ParamsGetter) getters.get(i)).getFile().getAbsolutePath().endsWith(".jpg") || ((ParamsGetter) getters.get(i)).getFile().getAbsolutePath().endsWith(".jpeg")) {
                            Log.e("KEY VLAUES FILE", ">>>>    " + ((ParamsGetter) getters.get(i)).getKey() + "            " + ((ParamsGetter) getters.get(i)).getFile());
                            type = builder.setType(MultipartBody.FORM);
                            key = ((ParamsGetter) getters.get(i)).getKey();
                            type.addFormDataPart(key, ((ParamsGetter) getters.get(i)).getFile().getName(), RequestBody.create(MEDIA_TYPE_PNG, ((ParamsGetter) getters.get(i)).getFile()));
                        }
                        if (((ParamsGetter) getters.get(i)).getFile().getAbsolutePath().endsWith(".mp4") || ((ParamsGetter) getters.get(i)).getFile().getAbsolutePath().endsWith(".mpeg") || ((ParamsGetter) getters.get(i)).getFile().getAbsolutePath().endsWith(".3gp") || ((ParamsGetter) getters.get(i)).getFile().getAbsolutePath().endsWith(".avi")) {
                            type = builder.setType(MultipartBody.FORM);
                            key = ((ParamsGetter) getters.get(i)).getKey();
                            type.addFormDataPart(key, ((ParamsGetter) getters.get(i)).getFile().getName(), RequestBody.create(MEDIA_TYPE_Video, ((ParamsGetter) getters.get(i)).getFile()));
                        }
                    } else {
                        Log.e("KEY VLAUES", ">>>>    " + ((ParamsGetter) getters.get(i)).getKey() + "            " + ((ParamsGetter) getters.get(i)).getValues());
                        builder.setType(MultipartBody.FORM).addFormDataPart(((ParamsGetter) getters.get(i)).getKey(), ((ParamsGetter) getters.get(i)).getValues());
                    }
                    i++;
                }
                request = new Request.Builder().url(baseUrl + Url).post(builder.build()).build();
            }
            strResponse = okk.newCall(request).execute().body().string();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            listener.onPostError(flag);
        } catch (IOException e2) {
            e2.printStackTrace();
            listener.onPostError(flag);
        } catch (Exception e3) {
            e3.printStackTrace();
            listener.onPostError(flag);
        }
        return strResponse;
    }
}
