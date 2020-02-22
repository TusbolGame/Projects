package com.liveitandroid.liveit;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.liveitandroid.liveit.view.utility.UtilsMethods;

import java.io.File;

public class Prefrences {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String RecordingPath;

    public Prefrences(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public File getRecordingDir() {
        String path = preferences.getString(RecordingPath, "");
        if(path == "")
            return UtilsMethods.getRecordingDir();
        else{
            File recDir = new File(path);
            if(!recDir.exists())
                recDir.mkdirs();
            return recDir;
        }
    }

    public void setRecordingPath(String recordingPath) {
        editor.putString(RecordingPath, recordingPath);
        editor.commit();
    }
}
