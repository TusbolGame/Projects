package com.liveitandroid.liveit.WebServiceHandler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Globals {
    public static String RandomNumber = "";
    public static int frag_ID;
    public static Fragment fragment;
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;
    public static Webservices getWebservices;
    public static List<ParamsGetter> getterList = new ArrayList();
    public static JSONArray jsonArr;
    public static JSONObject jsonObj;

    public static String m4S(String value) {
        try {
            if (jsonObj.getString(value) == null || jsonObj.getString(value).equals("null") || jsonObj.getString(value).equals("")) {
                return "";
            }
            return jsonObj.getString(value);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static ParamsGetter m3P(String key, String value) {
        return new ParamsGetter(key, value);
    }
}
