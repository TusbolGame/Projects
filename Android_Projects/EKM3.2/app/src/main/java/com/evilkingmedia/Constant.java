package com.evilkingmedia;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Constant {

//    public static String Search_Video_Url = "https://www.dailymotion.com/it"; // old
    public static String Search_Video_Url = "https://openload.click"; // old

//    public static String Video_Vari_M3u_Url = "xmtv://aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3LzlQZHhwYlBiLy5tM3U="; //old
    public static String Video_Vari_M3u_Url = "xmtv://aHR0cDovL3N1cGVybXlzcGFjZS54eXovRUtNL0hPT09PT09PT09PT09PT01FL3ZpZGVvdmFyaS5tM3UK";

//    public static String DEMAND_RAI_M3U_URL = "xmtv://aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3L2FHdEt4bnQ2Ly5tM3U=";     //old
    public static String DEMAND_RAI_M3U_URL = "xmtv://aHR0cDovL3N1cGVybXlzcGFjZS54eXovRUtNL0hPT09PT09PT09PT09PT01FL3JhaWhvbWUubTN1Cg==";

//    public static String DEMAND_MEDIASET_M3U_URL = "xmtv://aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3L1lia2QxS1Z3Ly5tM3U=";  //old
    public static String DEMAND_MEDIASET_M3U_URL = "xmtv://aHR0cDovL3N1cGVybXlzcGFjZS54eXovRUtNL0hPT09PT09PT09PT09PT01FL21lZHZvZGhvbWUubTN1Cg==";




    public static String ALTA_FILM_URL = "https://altadefinizionehd.pro/";
    public static String SERIESURL1 = "http://www.seriehd.me/";
    public static String SERIESURL1_SUB = "http://www.seriehd.me/serie-tv-streaming/";
    public static String SERIESURL2 = "https://streaminghd.fun/serietv/";
    public static String SERIESURL3 = "https://www.cinemasubito.org/serie";



//    public static String SPORTSBYDOCURL = "xmtv://aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3L1o3M2Q2SGRSLy5tM3U="; //old
    public static String SPORTSBYDOCURL = "xmtv://aHR0cDovL3N1cGVybXlzcGFjZS54eXovRUtNL0hPT09PT09PT09PT09PT01FL3Nwb3J0Ynlkb2MubTN1";




    public static String SERIESURL4 = "https://www.filmsenzalimiti.info/serie-tv/";
    public static String SERIESURL4_search = "https://www.filmsenzalimiti.info/";
    public static String SPORTSURL1 = "http://hdstreams.club/";
    public static String SPORTSURL2 = "https://streamingsports.me/";
    public static String EPGURL = "http://guidatv.quotidiano.net/";
    public static String SPORTSURL3 = "http://www.sportcategory.com/webmasters/webmaster_iframe.php";
    public static String SPORTSEPGURL = "https://www.diretta.it/";
    public static String SPORTSURL6 = "https://www.goalsarena.org/en/video";
    public static String SPORTSURL7 = "https://sporteventz.com/en/";
    public static String SPORTS_MYP2P_URL = "http://www.myp2p.biz/";
    public static String SPORTS_CRICFREE_URL = "https://cricfree.stream/";
    public static String SPORTS_HULK_STREAM_URL = "https://www.hulkstream.com/";
    public static String SPORTS_EK_SPORT_URL = "https://www.evilkingmedia.com/ek-sport/";
    public static String SPORTS_SOCCER_URL = "https://www.soccerhighlightstoday.com/";
    public static String SPORTS_FOOTBALL_ON_DEMAND_URL = "http://fullmatchsports.com/";
    public static String SPORTS_USA_GOALS_DNS_URL = "https://www.usagoals.net/portal.html";

//    public static String MUSICURL4 = "http://supermyspace.xyz/EKM/HOME/meteoevil.m3u"; //old
    public static String MUSICURL4 = "http://supermyspace.xyz/EKM/HOOOOOOOOOOOOOOME/ekmmusic.m3u";


    public static String MUSICA_SHAZAM_URL = "https://www.acrcloud.com/identify-songs-music-recognition-online";
    public static String MUSICA_LISTEIPTV_URL = "xmtv://aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3L3NZQ1dIUUFLLy5tM3U=";


//    public static String METEOURL = "xmtv://aHR0cHM6Ly9wYXN0ZWJpbi5jb20vU2t4Rk1YZkwvLm0zdQ=="; //old
    public static String METEOURL = "xmtv://aHR0cDovL3N1cGVybXlzcGFjZS54eXovRUtNL0hPT09PT09PT09PT09PT01FL21ldGVvZXZpbC5tM3UK";

    public static String CARTOONURL1 = "http://www.animehdita.org/";

    public static boolean isCategory = false;
    public static String EVILKINGLISTEITTVURL = "xmtv://aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3L1VaV0tYYVVVLy5tM3U=";
    public static String EVILKINGGUIDEURL = "https://www.evilkingmedia.com/come-vedere-e-scaricare-film-e-serie-tv-con-evil-king-media/";
    public static String EVILKINGSERIESURL = "https://www.evilkingmedia.com/serie-tv/";
    public static String EVILKINGMOVIEURL = "https://www.evilkingmedia.com/film/";

    public static String EVILKINGSPORTSURL = "https://www.evilkingmedia.com/sport/";
    public static String EVILKINGCARTOONURL = "https://www.evilkingmedia.com/cartoons/";
    public static String EKM_CARTOONS_URL = "xmtv://aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3L2I5dEF1ODFlLy5tM3U=";
    public static String ANIMEUNITY_SITE_URL = "https://animeunity.it/";
    public static String TV_WEB_CASTER_URL = "https://www.evilkingmedia.com/ekm-tv/";
    public static String TvWorldSite_URL = "https://www.firstonetv.live";

    public static String EVILKINGDOCUMENTARIURL = "https://www.evilkingmedia.com/documentari/";

    public static String WVC_packageName = "com.instantbits.cast.webvideo";
    public static String WVC_playstore = "https://play.google.com/store/apps/details?id=com.instantbits.cast.webvideo";

    public static String Video_Guide_URL = "xmtv://aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3L1NmWm5zQUdrLy5tM3U=";
    public static String TFBUrl = "https://t.me/TuttoFreeBot";



//    public static String LIVETV_HAREM_LISTEIPTV_URL = "xmtv://aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3L2pTYjk0UEJ1Ly5tM3U=";  //old
//    public static String LIVETV_HAREM_WEBCASTER_URL = "https://www.evilkingmedia.com/harem";  //old
    public static String LIVETV_HAREM_LISTEIPTV_URL = "xmtv://aHR0cDovL3N1cGVybXlzcGFjZS54eXovRUtNL0hPT09PT09PT09PT09PT01FL2VrbXh4eC5tM3UK";
//    public static String LIVETV_HAREM_WEBCASTER_URL = "http://supermyspace.xyz/EKM/HOOOOOOOOOOOOOOME/ekmxxx.m3u"; //old
    public static String LIVETV_HAREM_WEBCASTER_URL = "https://www.evilkingmedia.com/harem/";



    public static String LIVETV_M3U_CREATOR_URL = "http://tvtvtv.ru/tools/plc_eng.php";
    public static String LIVETV_LIST_PROTECTION_URL = "http://xmtvplayer.com/xmtvs-protection";
    public static String LIVETV_LISTEIPTV_URL = "xmtv://aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3L2N0YkhUWHRGLy5tM3U=";
    private static String chosePlayer = "";




    public static String TELEGRAM_BOT_URL = "https://t.me/evilkinghelp_bot";
    public static String CINEMA_SHARE_URL = "http://supermyspace.xyz/filmshare.m3u";
    public static String TV_SHARE_URL = "http://supermyspace.xyz/TVShare.m3u";



    public static void setFocusEvent(Context context, LinearLayout ...linearLayouts){
        for (int i = 0; i < linearLayouts.length; i++){
            linearLayouts[i].setFocusable(true);
            linearLayouts[i].setBackground(context.getResources().getDrawable(R.drawable.section_selection_background));
        }
    }

    public static void showAdmob(Context context, AdView adView){
        MobileAds.initialize(context, context.getString(R.string.ads_app_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public static void playInWuffyOrXmtv(final Context ctx, final String url) {
        if(!appInstalledOrNot(ctx, "co.wuffy.player") && !appInstalledOrNot(ctx, "com.xmtex.videoplayer.ads")){
            Toast.makeText(ctx, "Installed Wuffy Player or Xmtv Player To Play This Video",
                    Toast.LENGTH_LONG).show();
        }else{
            if(!appInstalledOrNot(ctx, "co.wuffy.player")){
                gotoXmtv(ctx, url);
            }else{
                if(!appInstalledOrNot(ctx, "com.xmtex.videoplayer.ads")){
                    gotoWuffy(ctx, url);
                }else{
                    SharedPreferences savedPlayer = ctx.getSharedPreferences("defaultPlayer", ctx.MODE_PRIVATE);
                    String player = savedPlayer.getString("chose", "");
                    if(player.equals("wuffy")){
                        gotoWuffy(ctx, url);
                    } else if (player.equals("xmtv")){
                        gotoXmtv(ctx, url);
                    } else {
                        LayoutInflater li = LayoutInflater.from(ctx);
                        View promptsView = li.inflate(R.layout.open_with_dialog, null);
                        final AlertDialog chooser = new AlertDialog.Builder(ctx).setView(promptsView).create();
                        final LinearLayout wuffyLy = promptsView.findViewById(R.id.wuffyPlayer);
                        final LinearLayout xmtvLy = promptsView.findViewById(R.id.xmtvPlayer);
                        final Button always = promptsView.findViewById(R.id.alwaysBtn);
                        final Button justonce = promptsView.findViewById(R.id.justonceBtn);
                        always.setEnabled(false);
                        justonce.setEnabled(false);

                        wuffyLy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                wuffyLy.setBackgroundColor(Color.parseColor("#FF62B1FC"));
                                xmtvLy.setBackgroundColor(Color.parseColor("#ffffff"));
                                always.setEnabled(true);
                                always.setTextColor(Color.parseColor("#000000"));
                                justonce.setEnabled(true);
                                justonce.setTextColor(Color.parseColor("#000000"));
                                chosePlayer = "wuffy";
                            }
                        });
                        xmtvLy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                xmtvLy.setBackgroundColor(Color.parseColor("#FF62B1FC"));
                                wuffyLy.setBackgroundColor(Color.parseColor("#ffffff"));
                                always.setEnabled(true);
                                always.setTextColor(Color.parseColor("#000000"));
                                justonce.setEnabled(true);
                                justonce.setTextColor(Color.parseColor("#000000"));
                                chosePlayer = "xmtv";
                            }
                        });

                        always.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences.Editor pref = ctx.getSharedPreferences("defaultPlayer", ctx.MODE_PRIVATE).edit();
                                switch (chosePlayer) {
                                    case "wuffy":
                                        pref.putString("chose", "wuffy");
                                        pref.apply();
                                        gotoWuffy(ctx, url);
                                        chooser.dismiss();
                                        break;
                                    case "xmtv":
                                        pref.putString("chose", "xmtv");
                                        pref.apply();
                                        gotoXmtv(ctx, url);
                                        chooser.dismiss();
                                        break;
                                }
                            }
                        });

                        justonce.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (chosePlayer) {
                                    case "wuffy":
                                        gotoWuffy(ctx, url);
                                        chooser.dismiss();
                                        break;
                                    case "xmtv":
                                        gotoXmtv(ctx, url);
                                        chooser.dismiss();
                                        break;
                                }
                            }
                        });

                        chooser.show();
                    }
                }
            }
        }
    }

    private static void gotoWuffy(Context context, String url){
        Bundle bnd = new Bundle();
        bnd.putString("path",url);
        Intent intent = new Intent();
        intent.setClassName("co.wuffy.player", "org.wuffy.videoplayer.WuffyPlayer");
        intent.putExtras(bnd);
        context.startActivity(intent);
    }

    private static void gotoXmtv(Context context, String url){
        Bundle bnd = new Bundle();
        bnd.putString("path",url);
        Intent intent = new Intent();
        intent.setClassName("com.xmtex.videoplayer.ads", "org.zeipel.videoplayer.XMTVPlayer");
        intent.putExtras(bnd);
        context.startActivity(intent);
    }

    public static void playDNSChanger(Context ctx) {
        if(!appInstalledOrNot(ctx, "net.mypush.dnsswitch")){
            ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=net.mypush.dnsswitch")));
            Toast.makeText(ctx, "Please installed DNS Changer.", Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent();
            intent.setClassName("net.mypush.dnsswitch", "net.mypush.dnsswitch.MainActivity");
            ctx.startActivity(intent);
        }
    }

    public static void playVPNChanger(Context ctx) {
        if(!appInstalledOrNot(ctx, "ufovpn.free.unblock.proxy.vpn")){
            ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=ufovpn.free.unblock.proxy.vpn")));
            Toast.makeText(ctx, "Please installed Secure VPN.", Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent();
            intent.setClassName("ufovpn.free.unblock.proxy.vpn", "ufovpn.free.unblock.proxy.vpn.home.ui.WelcomeActivity");
            ctx.startActivity(intent);
        }
    }

    public static boolean appInstalledOrNot(Context ctx,String uri) {
        PackageManager pm = ctx.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static void openWVCapp(Context ctx, String url)
    {
        if(!appInstalledOrNot(ctx, WVC_packageName)){
            alertDialogWVC(ctx);
        }else {
            ClipboardManager clipboard = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", url);
            clipboard.setPrimaryClip(clip);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri videoUri = Uri.parse(url);
            intent.setDataAndType(videoUri, "application/x-mpegURL");
            intent.setPackage(WVC_packageName);
            intent.setClassName(WVC_packageName, "com.instantbits.cast.webvideo.WebBrowser");
            ctx.startActivity(intent);
        }
    }

    public static void openExternalBrowser(Context ctx, String url){
        if (url.substring(0,4).equals("xmtv")) {
            if(!isPackageInstalled("co.wuffy.player",ctx.getPackageManager())) {
                new AlertDialog.Builder(ctx).setTitle("Wuffy Player needed")
                        .setMessage("Please download Wuffy Player to see this video on google store.")
                        .setPositiveButton("Okay", null)
                        .show();
                return;
            }
//            url = "http://supermyspace.xyz/EKM/HOOOOOOOOOOOOOOME/ekmliste.m3u";
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        ctx.startActivity(intent);
    }

    private static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            return packageManager.getApplicationInfo(packageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void alertDialogWVC(final Context ctx)
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
        alertDialog.setTitle("");
        alertDialog.setMessage("Installa l'applicazione Web Video Caster per aprire questo link");

        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Sì", (dialog, id) -> {
            try {
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + WVC_packageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + WVC_packageName)));
            }
        });
        alertDialog.setNegativeButton("No", (dialog, id) -> dialog.cancel());

        AlertDialog alert = alertDialog.create();
        alert.show();

    }

}
