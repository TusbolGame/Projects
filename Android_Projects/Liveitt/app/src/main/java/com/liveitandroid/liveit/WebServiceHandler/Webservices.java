package com.liveitandroid.liveit.WebServiceHandler;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class Webservices {
    public static Webservices getWebservices;
    public static List<ParamsGetter> getterList = new ArrayList();
    Context context;

    public static class getWebNames {
        public static String Url = "http://hd.guardatv.eu:8000/";
        public static String resetpswd2 = "reset_password";
        public static String sequrity = "";
    }

    public Webservices(Context context) {
        this.context = context;
    }

    public void SequrityLink(MainAsynListener<String> _context) {
        new MainAsyncTask(this.context, getWebNames.sequrity, 1, _context, "Form", getterList, Boolean.valueOf(false)).execute(new String[0]);
    }

    public void Resetpaswd2(MainAsynListener<String> _context) {
        getterList.add(m5P("token", ""));
        new MainAsyncTask(this.context, getWebNames.resetpswd2, 1, _context, "Form", getterList, Boolean.valueOf(true)).execute(new String[0]);
    }

    public static ParamsGetter m5P(String key, String value) {
        return new ParamsGetter(key, value);
    }
}
