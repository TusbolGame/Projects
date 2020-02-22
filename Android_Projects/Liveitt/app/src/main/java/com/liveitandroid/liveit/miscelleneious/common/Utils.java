package com.liveitandroid.liveit.miscelleneious.common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.liveitandroid.liveit.view.activity.AudioActivity;
import com.liveitandroid.liveit.view.activity.ExoPlayerExtraActivity;
import com.liveitandroid.liveit.view.activity.ImportEPGActivity;
import com.liveitandroid.liveit.view.activity.ImportStreamsActivity;
import com.liveitandroid.liveit.view.activity.LiveActivityNewFlow;
import com.liveitandroid.liveit.player.LiveitPlayerActivity;
import com.liveitandroid.liveit.view.activity.LoginActivity;
import com.liveitandroid.liveit.view.activity.MXPlayerSeriesActivity;
import com.liveitandroid.liveit.view.activity.MxPlayerArchiveActivity;
import com.liveitandroid.liveit.view.activity.MxPlayerLiveStreamsActivity;
import com.liveitandroid.liveit.view.activity.MxPlayerVodActivity;
import com.liveitandroid.liveit.view.activity.MxPlayerVodAppActivity;
import com.liveitandroid.liveit.view.activity.VLCPlayerArchiveActivity;
import com.liveitandroid.liveit.view.activity.VLCPlayerExtrasActivity;
import com.liveitandroid.liveit.view.activity.VLCPlayerLiveStreamsActivity;
import com.liveitandroid.liveit.view.activity.VLCPlayerSeriesActivity;
import com.liveitandroid.liveit.view.activity.VLCPlayerVodActivity;
import com.liveitandroid.liveit.view.activity.VodActivityNewFlow;
import com.liveitandroid.liveit.view.activity.XPlayerVodAppActivity;
import com.liveitandroid.liveit.view.exoplayer2.PlayerActivity;
import com.liveitandroid.liveit.view.exoplayer2.PlayerArchiveActivity;
import com.liveitandroid.liveit.view.exoplayer2.PlayerSeriesActivity;
import com.liveitandroid.liveit.view.exoplayer2.PlayerVodActivity;
import com.liveitandroid.liveit.view.nstplayer.NSTPlayerActivity;
import com.liveitandroid.liveit.view.nstplayer.NSTPlayerArchiveActivity;
import com.liveitandroid.liveit.view.nstplayer.NSTPlayerSeriesActivity;
import com.liveitandroid.liveit.view.nstplayer.NSTPlayerSkyActivity;
import com.liveitandroid.liveit.view.nstplayer.NSTPlayerVodActivity;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.helper.Urls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.joda.time.LocalDateTime;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class Utils {
    private static SharedPreferences SharedPreferencesSort;
    private static Editor SharedPreferencesSortEditor;
    private static PopupWindow changeSortPopUp;
    private static SharedPreferences loginPreferencesAfterLogin;
    private static SharedPreferences loginPreferences_layout;
    private static Intent NSTPlayerArchiveIntent;
    SessionManager mSessionManager;
    private static final String _MX_PLAYER_PACKAGE_NAME_PRO = "com.mxtech.videoplayer.pro";
    static class C15401 implements OnClickListener {
        C15401() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    public static AlertDialog showAlertBox(Context context, String message) {
        if (context == null || message.isEmpty()) {
            return null;
        }
        Builder builder1 = new Builder(context);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok", new C15401());
        AlertDialog alert11 = builder1.create();
        alert11.show();
        return alert11;
    }

    public static void showToast(Context context, String message) {
        if (context != null && message != "" && !message.isEmpty()) {
            Toast.makeText(context, message, 0).show();
        }
    }

    /*===========Check App is installed or not===============*/
    public static boolean appInstalledOrNot(String uri, Context context) {

        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }

        return app_installed;
    }

    public static int convertDpToPixel(Context ctx, int dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    /*===========Dialog to redirect Playstore===============*/
    public static void installPlayerfromPlaystore(final Context context, String PlayerSeee, String PackageSee) {
        final String PackageSe = PackageSee;
        final String PlayerSe = PlayerSeee;
        new AlertDialog.Builder(context)
                .setTitle(PlayerSe + " não instalado")
                .setMessage("Por favor Instale: " + PlayerSeee)
                .setCancelable(false)
                .setPositiveButton("Instalar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                        i.setData(Uri.parse(Urls.urlPlaystore + PackageSe));
                        context.startActivity(i);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();

    }

    /*===========Dialog to redirect Playstore===============*/
    public static void DebugPopup(final Context context, String PlayerSeee, String PackageSee, String nomeVid, String urlVid, String subsVid, String tipoloa, Boolean inssas) {
        final String PackageSe = PackageSee;
        final String PlayerSe = PlayerSeee;
        new AlertDialog.Builder(context)
                .setTitle("Mensagem Nova")
                .setMessage("Player: " + PlayerSeee + "\n\n" +
                        "Package: " + PackageSee + "\n\n" +
                        "Nome: " + nomeVid + "\n\n" +
                        "URL: " + urlVid + "\n\n" +
                        "SUBS: " + subsVid + "\n\n" +
                        "Instalado: " + inssas + "\n\n" +
                        "Tipologia: " + tipoloa)
                .setCancelable(false)
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public static Retrofit retrofitObject(Context context) {
        if (context != null) {
            String serverUrl = context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_SERVER_URL, 0).getString(AppConst.LOGIN_PREF_SERVER_URL_MAG, AppConst.BASE_URL);
            Log.e("URl from Back", ">>>>>>>>" + serverUrl);
            if (!(serverUrl.startsWith("http://") || serverUrl.startsWith("https://"))) {
                serverUrl = "http://" + serverUrl;
            }
            if (serverUrl.endsWith("/c")) {
                serverUrl = serverUrl.substring(0, serverUrl.length() - 2);
            }
            if (!serverUrl.endsWith("/")) {
                serverUrl = serverUrl + "/";
            }
            if (Patterns.WEB_URL.matcher(serverUrl).matches()) {
                return new Retrofit.Builder().baseUrl(serverUrl).client(new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).followRedirects(false).build()).addConverterFactory(GsonConverterFactory.create()).build();
            }
        }
        return null;
    }

    public static Retrofit retrofitObjectXML(Context context) {
        if (context != null) {
            String serverUrl = context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_SERVER_URL, 0).getString(AppConst.LOGIN_PREF_SERVER_URL_MAG, AppConst.BASE_URL);
            if (!(serverUrl.startsWith("http://") || serverUrl.startsWith("https://"))) {
                serverUrl = "http://" + serverUrl;
            }
            if (serverUrl.endsWith("/c")) {
                serverUrl = serverUrl.substring(0, serverUrl.length() - 2);
            }
            if (!serverUrl.endsWith("/")) {
                serverUrl = serverUrl + "/";
            }
            if (Patterns.WEB_URL.matcher(serverUrl).matches()) {
                return new Retrofit.Builder().baseUrl(serverUrl).client(new OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS).writeTimeout(100, TimeUnit.SECONDS).readTimeout(100, TimeUnit.SECONDS).build()).addConverterFactory(SimpleXmlConverterFactory.create()).build();
            }
        }
        return null;
    }

    public static boolean getNetworkType(Context context) {
        boolean networkType = false;
        if (context == null) {
            return false;
        }
        NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetwork == null) {
            return false;
        }
        if (activeNetwork.getType() == 1) {
            networkType = true;
        } else if (activeNetwork.getType() == 0) {
            networkType = true;
        }
        return networkType;
    }

    public static int getNumberOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((((float) displayMetrics.widthPixels) / displayMetrics.density) / 180.0f);
    }

    public static long epgTimeConverter(String str) {
        int i = 15;
        int i2 = 0;
        if (str == null) {
            return 0;
        }
        try {
            if (str.length() >= 18) {
                if (str.charAt(15) == '+') {
                    i = 16;
                }
                i2 = Integer.parseInt(str.substring(i, 18)) * 60;
            }
            if (str.length() >= 19) {
                i2 += Integer.parseInt(str.substring(18));
            }
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateTimeFormat.parse(str.substring(0, 14)).getTime() - ((long) ((i2 * 60) * 1000));
        } catch (Throwable e) {
            Log.e("XMLTVReader", "Exception", e);
            return 0;
        }
    }

    public static void playWithPlayer(Context context, String selectedPlayer, int streamId, String streamType, String num, String name, String epgChannelId, String epgChannelLogo, String catgID) {
        if (context == null) {
            return;
        }

        SessionManager mSessionManager = new SessionManager(context);
        selectedPlayer = mSessionManager.getPlayerSelectedTV();

        if (selectedPlayer.equals(context.getResources().getString(R.string.vlc_player))) {
            Intent VlcPlayerIntent = new Intent(context, VLCPlayerLiveStreamsActivity.class);
            VlcPlayerIntent.putExtra("OPENED_STREAM_ID", streamId);
            VlcPlayerIntent.putExtra("STREAM_TYPE", streamType);
            context.startActivity(VlcPlayerIntent);
        } else if (selectedPlayer.equals(context.getResources().getString(R.string.mx_player))) {
            Intent MxPlayerIntent = new Intent(context, MxPlayerLiveStreamsActivity.class);
            MxPlayerIntent.putExtra("OPENED_STREAM_ID", streamId);
            MxPlayerIntent.putExtra("STREAM_TYPE", streamType);
            context.startActivity(MxPlayerIntent);
        } else if (selectedPlayer.equals(context.getResources().getString(R.string.exo_player))) {
            Intent ExoplayerIntent = new Intent(context, PlayerActivity.class);
            ExoplayerIntent.setAction("com.google.android.exoplayer.demo.action.VIEW_LIST");
            ExoplayerIntent.putExtra("VIDEO_NUM", Integer.parseInt(num));
            ExoplayerIntent.putExtra("VIDEO_TITLE", name);
            ExoplayerIntent.putExtra("CAT_ID", catgID);
            ExoplayerIntent.putExtra("VIDEO_ID", streamId);
            ExoplayerIntent.putExtra("EPG_CHANNEL_ID", epgChannelId);
            ExoplayerIntent.putExtra("EPG_CHANNEL_LOGO", epgChannelLogo);
            context.startActivity(ExoplayerIntent);
        } else if (selectedPlayer.equals("Player Embutido 2") || selectedPlayer.equals("Embutido2")) {
            Intent NSTPlayerIntent = new Intent(context, NSTPlayerActivity.class);
            NSTPlayerIntent.putExtra("OPENED_STREAM_ID", streamId);
            NSTPlayerIntent.putExtra("STREAM_TYPE", streamType);
            int index = 0;
            try {
                index = Integer.parseInt(num);
            } catch (NumberFormatException e) {
            }
            NSTPlayerIntent.putExtra("VIDEO_NUM", index);
            NSTPlayerIntent.putExtra("VIDEO_TITLE", name);
            NSTPlayerIntent.putExtra("EPG_CHANNEL_ID", epgChannelId);
            NSTPlayerIntent.putExtra("EPG_CHANNEL_LOGO", epgChannelLogo);
            context.startActivity(NSTPlayerIntent);
        } else {
            playWithSkyPlayer(context,selectedPlayer,streamId,streamType,num,name,epgChannelId,epgChannelLogo,catgID);
        }
    }

    public static void playSeries(Context context, String selectedPlayer, int streamId, String streamType, String containerExtension, String num, String name) {
        if (context == null) {
            return;
        }

        SessionManager mSessionManager = new SessionManager(context);
        selectedPlayer = mSessionManager.getPlayerSelectedFilmesLista();

        if (selectedPlayer.equals(context.getResources().getString(R.string.vlc_player))) {
            Intent VlcPlayerIntent = new Intent(context, VLCPlayerSeriesActivity.class);
            VlcPlayerIntent.putExtra("OPENED_STREAM_ID", streamId);
            VlcPlayerIntent.putExtra("STREAM_TYPE", streamType);
            VlcPlayerIntent.putExtra("CONTAINER_EXTENSION", containerExtension);
            context.startActivity(VlcPlayerIntent);
        } else if (selectedPlayer.equals(context.getResources().getString(R.string.mx_player))) {
            Intent MxPlayerIntent = new Intent(context, MXPlayerSeriesActivity.class);
            MxPlayerIntent.putExtra("OPENED_STREAM_ID", streamId);
            MxPlayerIntent.putExtra("STREAM_TYPE", streamType);
            MxPlayerIntent.putExtra("CONTAINER_EXTENSION", containerExtension);
            context.startActivity(MxPlayerIntent);
        } else if (selectedPlayer.equals(context.getResources().getString(R.string.exo_player))) {
            Intent ExoplayerIntent = new Intent(context, PlayerSeriesActivity.class);
            ExoplayerIntent.setAction("com.google.android.exoplayer.demo.action.VIEW");
            ExoplayerIntent.putExtra("VIDEO_NUM", Integer.parseInt(num));
            ExoplayerIntent.putExtra("VIDEO_TITLE", name);
            ExoplayerIntent.putExtra("VIDEO_ID", streamId);
            ExoplayerIntent.putExtra("EXTENSION_TYPE", containerExtension);
            context.startActivity(ExoplayerIntent);
        } else {
            Intent NSTPlayerVodIntent = new Intent(context, NSTPlayerSeriesActivity.class);
            NSTPlayerVodIntent.putExtra("VIDEO_ID", streamId);
            NSTPlayerVodIntent.putExtra("VIDEO_TITLE", name);
            NSTPlayerVodIntent.putExtra("EXTENSION_TYPE", containerExtension);
            NSTPlayerVodIntent.putExtra("VIDEO_NUM", Integer.parseInt(num));
            context.startActivity(NSTPlayerVodIntent);
        }
    }

    public static void playWithSkyPlayer(Context context, String selectedPlayer, int streamId, String streamType, String num, String name, String epgChannelId, String epgChannelLogo, String catID) {
        if (context != null) {
            Intent NSTPlayerIntent = new Intent(context, NSTPlayerSkyActivity.class);
            NSTPlayerIntent.putExtra("OPENED_STREAM_ID", streamId);
            NSTPlayerIntent.putExtra("STREAM_TYPE", streamType);
            int index = 0;
            try {
                index = Integer.parseInt(num);
            } catch (NumberFormatException e) {
            }
            NSTPlayerIntent.putExtra("VIDEO_NUM", index);
            NSTPlayerIntent.putExtra("VIDEO_TITLE", name);
            NSTPlayerIntent.putExtra("EPG_CHANNEL_ID", epgChannelId);
            NSTPlayerIntent.putExtra("EPG_CHANNEL_LOGO", epgChannelLogo);
            NSTPlayerIntent.putExtra("OPENED_CAT_ID", catID);
            context.startActivity(NSTPlayerIntent);
        }
    }

    public static void playWithPlayerArchive(Context context, String selectedPlayer, int streamId, String num, String name, String epgChannelId, String epgChannelLogo, String getStartFormatedTime, String streamChannelDuration, String getStopTime) {
        if (context == null) {
            return;
        }

        SessionManager mSessionManager = new SessionManager(context);
        selectedPlayer = mSessionManager.getPlayerSelectedGravacao();

        if (selectedPlayer.equals(context.getResources().getString(R.string.vlc_player))) {
            Intent VlcPlayerIntent = new Intent(context, VLCPlayerArchiveActivity.class);
            VlcPlayerIntent.putExtra("OPENED_STREAM_ID", streamId);
            VlcPlayerIntent.putExtra("STREAM_TYPE", "live");
            VlcPlayerIntent.putExtra("STREAM_START_TIME", getStartFormatedTime);
            VlcPlayerIntent.putExtra("STREAM_STOP_TIME", getStopTime);
            context.startActivity(VlcPlayerIntent);
        } else if (selectedPlayer.equals(context.getResources().getString(R.string.mx_player))) {
            Intent MxPlayerIntent = new Intent(context, MxPlayerArchiveActivity.class);
            MxPlayerIntent.putExtra("OPENED_STREAM_ID", streamId);
            MxPlayerIntent.putExtra("STREAM_TYPE", "live");
            MxPlayerIntent.putExtra("STREAM_START_TIME", getStartFormatedTime);
            MxPlayerIntent.putExtra("STREAM_STOP_TIME", getStopTime);
            context.startActivity(MxPlayerIntent);
        } else if (selectedPlayer.equals(context.getResources().getString(R.string.exo_player))) {
            NSTPlayerArchiveIntent = new Intent(context, PlayerArchiveActivity.class);
            NSTPlayerArchiveIntent.putExtra("OPENED_STREAM_ID", streamId);
            NSTPlayerArchiveIntent.setAction("com.google.android.exoplayer.demo.action.VIEW");
            NSTPlayerArchiveIntent.putExtra("VIDEO_NUM", Integer.parseInt(num));
            NSTPlayerArchiveIntent.putExtra("VIDEO_TITLE", name);
            NSTPlayerArchiveIntent.putExtra("EPG_CHANNEL_ID", epgChannelId);
            NSTPlayerArchiveIntent.putExtra("EPG_CHANNEL_LOGO", epgChannelLogo);
            NSTPlayerArchiveIntent.putExtra("STREAM_START_TIME", getStartFormatedTime);
            NSTPlayerArchiveIntent.putExtra("STREAM_DURATION", streamChannelDuration);
            NSTPlayerArchiveIntent.putExtra("STREAM_STOP_TIME", getStopTime);
            context.startActivity(NSTPlayerArchiveIntent);
        } else {
            NSTPlayerArchiveIntent = new Intent(context, NSTPlayerArchiveActivity.class);
            NSTPlayerArchiveIntent.putExtra("OPENED_STREAM_ID", streamId);
            NSTPlayerArchiveIntent.putExtra("STREAM_TYPE", "live");
            NSTPlayerArchiveIntent.putExtra("VIDEO_NUM", Integer.parseInt(num));
            NSTPlayerArchiveIntent.putExtra("VIDEO_TITLE", name);
            NSTPlayerArchiveIntent.putExtra("EPG_CHANNEL_ID", epgChannelId);
            NSTPlayerArchiveIntent.putExtra("EPG_CHANNEL_LOGO", epgChannelLogo);
            NSTPlayerArchiveIntent.putExtra("STREAM_START_TIME", getStartFormatedTime);
            NSTPlayerArchiveIntent.putExtra("STREAM_DURATION", streamChannelDuration);
            NSTPlayerArchiveIntent.putExtra("STREAM_STOP_TIME", getStopTime);
            context.startActivity(NSTPlayerArchiveIntent);
        }
    }

    public static void playWithPlayerVOD(Context context, String selectedPlayer, int streamId, String streamType, String containerExtension, String num, String name, String username, String password, String serverurll, String serverport) {
        if (context == null) {
            return;
        }
        SessionManager mSessionManager = new SessionManager(context);
        selectedPlayer = mSessionManager.getPlayerSelectedFilmesLista();
        if (selectedPlayer.equals(context.getResources().getString(R.string.vlc_player))) {
            Intent VlcPlayerIntent = new Intent(context, VLCPlayerVodActivity.class);
            VlcPlayerIntent.putExtra("OPENED_STREAM_ID", streamId);
            VlcPlayerIntent.putExtra("STREAM_TYPE", streamType);
            VlcPlayerIntent.putExtra("CONTAINER_EXTENSION", containerExtension);
            context.startActivity(VlcPlayerIntent);
        } else if (selectedPlayer.equals(context.getResources().getString(R.string.mx_player))) {
            Intent MxPlayerIntent = new Intent(context, MxPlayerVodActivity.class);
            MxPlayerIntent.putExtra("OPENED_STREAM_ID", streamId);
            MxPlayerIntent.putExtra("STREAM_TYPE", streamType);
            MxPlayerIntent.putExtra("CONTAINER_EXTENSION", containerExtension);
            context.startActivity(MxPlayerIntent);
        } else if (selectedPlayer.equals(context.getResources().getString(R.string.exo_player))) {
            Intent ExoplayerIntent = new Intent(context, PlayerVodActivity.class);
            ExoplayerIntent.setAction("com.google.android.exoplayer.demo.action.VIEW_LIST");
            ExoplayerIntent.putExtra("VIDEO_NUM", Integer.parseInt(num));
            ExoplayerIntent.putExtra("VIDEO_TITLE", name);
            ExoplayerIntent.putExtra("VIDEO_ID", streamId);
            ExoplayerIntent.putExtra("EXTENSION_TYPE", containerExtension);
            context.startActivity(ExoplayerIntent);
        } else {
            Intent NSTPlayerVodIntent = new Intent(context, NSTPlayerVodActivity.class);
            NSTPlayerVodIntent.putExtra("VIDEO_ID", streamId);
            NSTPlayerVodIntent.putExtra("VIDEO_TITLE", name);
            NSTPlayerVodIntent.putExtra("EXTENSION_TYPE", containerExtension);
            NSTPlayerVodIntent.putExtra("VIDEO_NUM", Integer.parseInt(num));
            context.startActivity(NSTPlayerVodIntent);

            //showAlertBox(context, "http://" + serverurll + ":" + serverport + "/movie/" + username + "/" + password + "/"+streamId+"."+containerExtension);
        }
    }

    public static void PlayerSetNovo(ListMoviesSetterGetter esco, Context context, String tipologia)
    {
        SessionManager mSessionManager = new SessionManager(context);
        String selectedPlayer = "";
        String packagePlayer = "";
        if(tipologia.equals("FilmesAPP"))
        {
            packagePlayer = mSessionManager.getPackageSelectedFilmesAPP();
            selectedPlayer = mSessionManager.getPlayerSelectedFilmesAPP();
        }else if(tipologia.equals("Praias"))
        {
            esco.setPraias_legenda("");
            packagePlayer = mSessionManager.getPackageSelectedPraias();
            selectedPlayer = mSessionManager.getPlayerSelectedPraias();
        }else if(tipologia.equals("Programas"))
        {
            esco.setPraias_legenda("");
            packagePlayer = mSessionManager.getPackageSelectedProgramas();
            selectedPlayer = mSessionManager.getPlayerSelectedProgramas();
        }else if(tipologia.equals("Radios"))
        {
            esco.setPraias_legenda("");
            packagePlayer = mSessionManager.getPackageSelectedRadios();
            selectedPlayer = mSessionManager.getPlayerSelectedRadios();
        }


        if (selectedPlayer.equals(context.getResources().getString(R.string.vlc_player))) {
            Intent vlcIntent = new Intent(context, VLCPlayerExtrasActivity.class);
            vlcIntent.putExtra("nameMovie", esco.getPraias_name());
            vlcIntent.putExtra("urlMovie", esco.getPraias_url());
            vlcIntent.putExtra("subs", esco.getPraias_legenda());
            context.startActivity(vlcIntent);
        } else if (selectedPlayer.equals(context.getResources().getString(R.string.mx_player))) {
            Intent MxPlayerIntent = new Intent(context, MxPlayerVodAppActivity.class);
            MxPlayerIntent.putExtra("nameMovie", esco.getPraias_name());
            MxPlayerIntent.putExtra("urlMovie", esco.getPraias_url());
            MxPlayerIntent.putExtra("subs", esco.getPraias_legenda());
            context.startActivity(MxPlayerIntent);
        } else if (selectedPlayer.equals(context.getResources().getString(R.string.x_player))) {
            Intent XPlayerIntent = new Intent(context, XPlayerVodAppActivity.class);
            XPlayerIntent.putExtra("nameMovie", esco.getPraias_name());
            XPlayerIntent.putExtra("urlMovie", esco.getPraias_url());
            XPlayerIntent.putExtra("subs", esco.getPraias_legenda());
            context.startActivity(XPlayerIntent);
        } else if (selectedPlayer.equals("Embutido") || selectedPlayer.equals("") || selectedPlayer.equals("Player Embutido (Padrão)")) {
            if(tipologia.equals("Radios"))
            {
                playWhenPlayerRadios(esco,context);
            }else{
                /*Intent LiveplayerIntent = new Intent(context, LiveitPlayerActivity.class);
                LiveplayerIntent.putExtra("nameMovie", esco.getPraias_name());
                LiveplayerIntent.putExtra("urlMovie", esco.getPraias_url());
                LiveplayerIntent.putExtra("subs", esco.getPraias_legenda());
                context.startActivity(LiveplayerIntent);*/


                Intent ExoplayerIntent = new Intent(context, ExoPlayerExtraActivity.class);
                ExoplayerIntent.putExtra("nameMovie", esco.getPraias_name());
                ExoplayerIntent.putExtra("urlMovie", esco.getPraias_url());
                ExoplayerIntent.putExtra("subs", esco.getPraias_legenda());
                context.startActivity(ExoplayerIntent);
            }
        }else{
            playWhenSystemSelect(esco.getPraias_url(), esco.getPraias_name(), esco.getPraias_legenda(), context);
        }
    }

    public static void playWhenSystemSelect(String playing_url, String nomePath, String playing_subsurl, Context connt) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra("title", nomePath);
        intent.setDataAndType(Uri.parse(playing_url), "video/*");
        try {
            Uri urisub = Uri.parse(playing_subsurl);
            intent.putExtra("subtitles", urisub);
            intent.putExtra("subtitles_location", urisub);
            Subtitle subtitle0_0 = new Subtitle(playing_subsurl);
            subtitle0_0.name = nomePath;
            subtitle0_0.filename = playing_subsurl;

            Parcelable[] parcels = new Parcelable[1];
            parcels[0] = subtitle0_0.uri;

            intent.putExtra("subs", parcels);
        } catch (Exception e) {

        }
        connt.startActivity(intent);
    }

    public static void playWhenPlayerRadios(ListMoviesSetterGetter moviepass, Context cont) {
        Intent video = new Intent(cont, AudioActivity.class);
        video.putExtra("movie", moviepass);
        cont.startActivity(video);
    }

    private static class Subtitle {
        /**
         * Subtitle URI
         */
        final Uri uri;

        /**
         * (Optional) Custom subtitle name.
         */
        String name;

        /**
         * (Optional) File name.
         */
        String filename;


        Subtitle(Uri uri) {
            if (uri.getScheme() == null)
                throw new IllegalStateException("Scheme is missed for subtitle URI " + uri);

            this.uri = uri;
        }

        Subtitle(String uriStr) {
            this(Uri.parse(uriStr));
        }
    }

    public static String parseDateToddMMyyyy(String time) {
        String updatedDate = "";
        try {
            return new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US).parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return updatedDate;
        }
    }

    public static String parseDateToddMMyyyy1(String time) {
        String dateStr = "Mon Jun 18 00:00:00 IST 2012";
        Date date = null;
        try {
            date = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.US).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String formatedDate = cal.get(5) + "/" + (cal.get(2) + 1) + "/" + cal.get(1);
        System.out.println("formatedDate : " + formatedDate);
        return formatedDate;
    }

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return TextUtils.isEmpty(str.trim());
    }

    public static int getPositionOfEPG(String string) {
        Object obj = -1;
        switch (string.hashCode()) {
            case 48:
                if (string.equals(AppConst.PASSWORD_UNSET)) {
                    obj = 12;
                    break;
                }
                break;
            case 1382:
                if (string.equals("+1")) {
                    obj = 13;
                    break;
                }
                break;
            case 1383:
                if (string.equals("+2")) {
                    obj = 14;
                    break;
                }
                break;
            case 1384:
                if (string.equals("+3")) {
                    obj = 15;
                    break;
                }
                break;
            case 1385:
                if (string.equals("+4")) {
                    obj = 16;
                    break;
                }
                break;
            case 1386:
                if (string.equals("+5")) {
                    obj = 17;
                    break;
                }
                break;
            case 1387:
                if (string.equals("+6")) {
                    obj = 18;
                    break;
                }
                break;
            case 1388:
                if (string.equals("+7")) {
                    obj = 19;
                    break;
                }
                break;
            case 1389:
                if (string.equals("+8")) {
                    obj = 20;
                    break;
                }
                break;
            case 1390:
                if (string.equals("+9")) {
                    obj = 21;
                    break;
                }
                break;
            case 1444:
                if (string.equals("-1")) {
                    obj = 11;
                    break;
                }
                break;
            case 1445:
                if (string.equals("-2")) {
                    obj = 10;
                    break;
                }
                break;
            case 1446:
                if (string.equals("-3")) {
                    obj = 9;
                    break;
                }
                break;
            case 1447:
                if (string.equals("-4")) {
                    obj = 8;
                    break;
                }
                break;
            case 1448:
                if (string.equals("-5")) {
                    obj = 7;
                    break;
                }
                break;
            case 1449:
                if (string.equals("-6")) {
                    obj = 6;
                    break;
                }
                break;
            case 1450:
                if (string.equals("-7")) {
                    obj = 5;
                    break;
                }
                break;
            case 1451:
                if (string.equals("-8")) {
                    obj = 4;
                    break;
                }
                break;
            case 1452:
                if (string.equals("-9")) {
                    obj = 3;
                    break;
                }
                break;
            case 42890:
                if (string.equals("+10")) {
                    obj = 22;
                    break;
                }
                break;
            case 42891:
                if (string.equals("+11")) {
                    obj = 23;
                    break;
                }
                break;
            case 42892:
                if (string.equals("+12")) {
                    obj = 24;
                    break;
                }
                break;
            case 44812:
                if (string.equals("-10")) {
                    obj = 2;
                    break;
                }
                break;
            case 44813:
                if (string.equals("-11")) {
                    obj = 1;
                    break;
                }
                break;
            case 44814:
                if (string.equals("-12")) {
                    obj = null;
                    break;
                }
                break;
        }
        switch ((int)obj) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            case 11:
                return 11;
            case 12:
                return 12;
            case 13:
                return 13;
            case 14:
                return 14;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;
            case 19:
                return 19;
            case 20:
                return 20;
            case 21:
                return 21;
            case 22:
                return 22;
            case 23:
                return 23;
            case 24:
                return 24;
            default:
                return 12;
        }
    }

    public static int getMilliSeconds(String epgShift) {
        if (epgShift == null || epgShift.isEmpty()) {
            return 0;
        }
        if (epgShift.contains("+")) {
            return ((Integer.parseInt(epgShift.split("\\+")[1]) * 60) * 60) * 1000;
        }
        if (epgShift.contains("-")) {
            return (((-Integer.parseInt(epgShift.split("\\-")[1])) * 60) * 60) * 1000;
        }
        return 0;
    }

    public static boolean isEventVisible(long start, long end, Context context) {
        if (context == null) {
            return false;
        }
        long currentTime = LocalDateTime.now().toDateTime().getMillis() + getTimeShiftMilliSeconds(context);
        if (start > currentTime || end < currentTime) {
            return false;
        }
        return true;
    }

    public static long getTimeShiftMilliSeconds(Context context) {
        if (context == null) {
            return 0;
        }
        loginPreferencesAfterLogin = context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        return (long) getMilliSeconds(loginPreferencesAfterLogin.getString(AppConst.LOGIN_PREF_SELECTED_EPG_SHIFT, ""));
    }

    public static int getPercentageLeft(long start, long end, Context context) {
        if (context == null) {
            return 0;
        }
        long now = LocalDateTime.now().toDateTime().getMillis() + getTimeShiftMilliSeconds(context);
        if (start >= end || now >= end) {
            return 0;
        }
        if (now <= start) {
            return 100;
        }
        return (int) (((end - now) * 100) / (end - start));
    }

    public static void logoutUser(Context context) {
        if (context != null) {
            Toast.makeText(context, context.getString(R.string.logged_out), 0).show();
            Intent intentLogout = new Intent(context, LoginActivity.class);
            SharedPreferences loginPreferences = context.getSharedPreferences(AppConst.SHARED_PREFERENCE, 0);
            Boolean saveLogin = Boolean.valueOf(loginPreferences.getBoolean(AppConst.PREF_SAVE_LOGIN, false));
            SessionManager mSessionManager = new SessionManager(context);
            if (!saveLogin.booleanValue()) {
                Editor loginPreferencesEditor = context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0).edit();
                loginPreferencesEditor.clear();
                loginPreferencesEditor.commit();
                mSessionManager.setInicio("");
            }
            context.startActivity(intentLogout);
        }
    }

    public static void set_layout_live(Context context) {
        context.startActivity(new Intent(context, LiveActivityNewFlow.class));
    }

    public static void set_layout_vod(Context context) {
        context.startActivity(new Intent(context, VodActivityNewFlow.class));
    }

    public static String getTime(Context context) {
        String Time = "";
        if (context != null) {
            try {
                Time = new SimpleDateFormat(context.getSharedPreferences(AppConst.LOGIN_PREF_TIME_FORMAT, 0).getString(AppConst.LOGIN_PREF_TIME_FORMAT, ""), Locale.US).format(new Date());
            } catch (Exception e) {
                return Time;
            }
        }
        return Time;
    }

    public static String getDate(String time) {
        String updatedDate = "";
        try {
            return new SimpleDateFormat(" dd MMMM, yyyy").format(new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US).parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return updatedDate;
        }
    }

    public static void loadChannelsAndVod(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, ImportStreamsActivity.class));
        }
    }

    public static void loadTvGuid(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, ImportEPGActivity.class));
        }
    }
}
