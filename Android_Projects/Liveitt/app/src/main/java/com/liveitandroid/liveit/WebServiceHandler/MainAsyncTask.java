package com.liveitandroid.liveit.WebServiceHandler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import com.liveitandroid.liveit.R;
import java.util.List;

public class MainAsyncTask extends AsyncTask<String, Void, String> {
    Context context;
    Dialog dialogBox;
    List<ParamsGetter> getterList;
    Boolean isProgress;
    boolean isSuccess = false;
    MainAsynListener<String> listener;
    int receivedId;
    public CommonFunctions sSetconnection;
    String tag;
    String url;

    class C15351 implements Runnable {
        C15351() {
        }

        public void run() {
            MainAsyncTask.this.showCommonDialog(MainAsyncTask.this.context, "Loading data...");
        }
    }

    class C15362 implements Runnable {
        C15362() {
        }

        public void run() {
        }
    }

    class C15373 implements Runnable {
        C15373() {
        }

        public void run() {
        }
    }

    class C15384 implements OnClickListener {
        C15384() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    public MainAsyncTask(Context context, String url, int receivedId, MainAsynListener<String> listener, String tag, List<ParamsGetter> getterList, Boolean isProgress) {
        this.context = context;
        this.url = url;
        this.receivedId = receivedId;
        this.listener = listener;
        this.tag = tag;
        this.getterList = getterList;
        this.isProgress = isProgress;
    }

    protected void onPreExecute() {
        this.sSetconnection = new CommonFunctions();
        if (this.isProgress.booleanValue()) {
            try {
                if (this.dialogBox != null && this.dialogBox.isShowing()) {
                    cancelCommonDialog();
                }
                new Handler(Looper.getMainLooper()).post(new C15351());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected String doInBackground(String... arg0) {
        String mResult = null;
        try {
            mResult = CommonFunctions.getOkHttpClient(this.context, this.url, this.receivedId, this.tag, this.getterList);
            if (mResult != null) {
                this.isSuccess = true;
            } else {
                new Handler(Looper.getMainLooper()).post(new C15362());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mResult;
    }

    @TargetApi(11)
    protected void onPostExecute(String _result) {
        try {
            if (this.isSuccess) {
                if (_result == null) {
                    new Handler(Looper.getMainLooper()).post(new C15373());
                }
                Log.e("result><><><>", "" + _result);
                this.listener.onPostSuccess(_result, this.receivedId, this.isSuccess);
            } else {
                this.listener.onPostError(this.receivedId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.isProgress.booleanValue()) {
            try {
                cancelCommonDialog();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    protected void onCancelled() {
        super.onCancelled();
    }

    public boolean getConnectivityStatus(Context context) {
        NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetwork == null || (activeNetwork.getType() != 1 && activeNetwork.getType() != 0)) {
            return false;
        }
        return true;
    }

//    public void openInternetDialog(final Context c) {
//        Builder alertDialogBuilder = new Builder(c, R.style.DialogThemee);
//        alertDialogBuilder.setTitle("Internet Alert!");
//        alertDialogBuilder.setMessage("You are not connected to Internet..Please Enable Internet!").setCancelable(false).setPositiveButton("Sim", new OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//                Intent intent = new Intent("android.intent.action.MAIN", null);
//                intent.addCategory("android.intent.category.LAUNCHER");
//                intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings"));
//                intent.setFlags(268435456);
//                c.startActivity(intent);
//            }
//        }).setNegativeButton("Não", new C15384());
//        alertDialogBuilder.create().show();
//    }

    public void showCommonDialog(Context mContext, String message) {
        this.dialogBox = new Dialog(mContext, 16973840);
        this.dialogBox.setContentView(R.layout.layout_progress_bar);
        this.dialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.dialogBox.setCancelable(false);
        ((TextView) this.dialogBox.findViewById(R.id.message)).setText(message);
        try {
            if (((Activity) this.context).isFinishing()) {
                Log.e("FINISHED", "FINISHED");
            } else {
                this.dialogBox.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelCommonDialog() {
        try {
            if (this.dialogBox != null && this.dialogBox.isShowing()) {
                this.dialogBox.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
