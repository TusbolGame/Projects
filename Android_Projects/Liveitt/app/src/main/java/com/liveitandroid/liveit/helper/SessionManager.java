package com.liveitandroid.liveit.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class SessionManager {

    // Shared preferences file name
    private static final String PREF_NAME = "liveit";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_SELECTED = "isSelectedIn";
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();
    private static String PASSWORD = "loggedUserPASSWORD";
    private static String NAME = "userName";
    private static String NAMEADULT = "NAMEADULT";
    private static String STATUS = "userStatus";
    private static String STATUSType = "userStatusType";
    private static String EXPIRY_DATE = "userExpiryDate";
    private static String IS_TRIAL = "userIsTrial";
    private static String MAX_CONNECTIONS = "userMaxConnections";
    private static String NOME = "userNome";
    private static String NOMEServidor = "userServ";
    private static String NOMEAPP = "APPNome";
    private static String MELHORAAPP = "MELHORAAPP";
    private static String EMAIL = "userEmail";
    private static String NOMECANAL = "nomecanal";
    private static String NOMECANALID = "nomecanalid";
    private static String ADULTOSPASS = "userAdultospass";
    private static String EMAILADMIN = "emailadmin";
    private static String WEBSITE = "website";
    private static String KODIUSER = "kodiuser";
    private static String KODISENHA = "kodisenha";
    private static String KODIAPI = "kodiapi";
    private static String verDOR = "verDORAdd";
    private static String verUserID = "verUserID";
    private static String verDORVODList = "verDORVODList";
    private static String KODITOKEN = "koditoken";
    private static String KODIID = "kodiid";
    private static String FilmesAPPID = "Filmesid";
    private static String KODIGRANT = "kodigrant";
    private static String KODISECRET = "kodisecret";
    private static String KODISITE = "kodisite";
    private static String SERVER_URL = "serverUrl";
    private static String SERVER_PORT = "serverPort";
    private static String USER_AUTH = "userAuth";
    private static String EPG_URL = "epgUrl";
    private static String TMDB_1 = "TMDB_1";
    private static String TMDB_2 = "TMDB_2";
    private static String TMDB_3 = "TMDB_3";
    private static String TMDB_4 = "TMDB_4";
    private static String DNS_URL = "dnsUrl";
    private static String SERIES = "seriesue";
    private static String ORDENA = "ordenalive";
    private static String UserAcess_Token = "useracesstoken";
    private static String Fundo = "Fundoasda";
    private static String UserClient_Secret = "userclientsecret";
    private static String ADULT_PASSWORD = "adultPassword";

    private static String SELECTED_PLAYER_ITEM_TV_1 = "SELECTED_PLAYER_ITEM_TV_T_1";
    private static String SELECTED_CHECKED_ITEM_TV_1 = "SELECTED_CHECKED_ITEM_TV_T_1";
    private static String SELECTED_PACKAGE_TV_1 = "SELECTED_PACKAGE_TV_T_1";
    private static String SELECTED_PACKAGE_TV_Novo = "SELECTED_PACKAGE_TV_T_New";

    private static String SELECTED_CHECKED_ITEM_Hits = "SELECTED_CHECKED_ITEM_Hits_T";
    private static String SELECTED_PLAYER_ITEM_Hits = "SELECTED_PLAYER_ITEM_Hits_T";
    private static String SELECTED_PACKAGE_Hits = "SELECTED_PACKAGE_Hits_T";

    private static String SELECTED_PLAYER_Praias_1 = "SELECTED_PLAYER_Praias_T_1";
    private static String SELECTED_CHECKED_Praias_1 = "SELECTED_CHECKED_Praias_T_1";
    private static String SELECTED_PACKAGE_Praias_1 = "SELECTED_PACKAGE_Praias_T_1";

    private static String SELECTED_PLAYER_FilmesAPP_1 = "SELECTED_PLAYER_FilmesAPP_1";
    private static String SELECTED_CHECKED_FilmesAPP_1 = "SELECTED_CHECKED_FilmesAPP_1";
    private static String SELECTED_PACKAGE_FilmesAPP_1 = "SELECTED_PACKAGE_FilmesAPP_1";

    private static String SELECTED_PLAYER_Gravacao_1 = "SELECTED_PLAYER_Gravacao_T_1";
    private static String SELECTED_CHECKED_Gravacao_1 = "SELECTED_CHECKED_Gravacao_T_1";
    private static String SELECTED_PACKAGE_Gravacao_1 = "SELECTED_PACKAGE_Gravacao_T_1";

    private static String SELECTED_PLAYER_FilmesLista_1 = "SELECTED_PLAYER_FilmesLista_T_1";
    private static String SELECTED_CHECKED_FilmesLista_1 = "SELECTED_CHECKED_FilmesLista_T_1";
    private static String SELECTED_PACKAGE_FilmesLista_1 = "SELECTED_PACKAGE_FilmesLista_T_1";

    private static String SELECTED_PLAYER_Radios_1 = "SELECTED_PLAYER_Radios_T_1";
    private static String SELECTED_CHECKED_Radios_1 = "SELECTED_CHECKED_Radios_T_1";
    private static String SELECTED_PACKAGE_Radios_1 = "SELECTED_PACKAGE_Radios_T_1";

    private static String SELECTED_PLAYER_Programas_1 = "SELECTED_PLAYER_Programas_T_1";
    private static String SELECTED_CHECKED_Programas_1 = "SELECTED_CHECKED_Programas_T_1";
    private static String SELECTED_PACKAGE_Programas_1 = "SELECTED_PACKAGE_Programas_T_1";


    private static String SELECTED_PAGE_ITEM = "SelectedcheckedPageitem";
    private static String SELECTED_PAGE = "SelectedPage";
    private static String SELECTED_LOCAL_ITEM = "Selectedlocalitem";
    private static String ADULTDIALOGPASSWORD = "adultDialogPassword";
    private static String WIFI_IS_PAGESELECTED = "isWIFIselected";
    private static String STATUSYES = "statusYes";
    private static String KEY_IS_BOOT = "isboot";
    private static String KEY_IS_BOOT_TV = "isboottv";
    private static String KEY_IS_BOOT_IPTVCORE = "isboottvcore";
    private static String KEY_IS_BOOT_Exceptional = "isbootExceptional";
    private static String KEY_EPG = "isEPG";
    private static String SELECTED_CHECKE_N = "SELECTED_CHECKE_N_N_";
    private static String SELECTED_CHECKE_Color = "SELECTED_CHECKE_Color";
    private static String SELECTED_CHECKEDMOVIE = "Selectedcheckedmovie";
    private static String KEY_IS_CHANNELSELECTED = "isChannelselected";
    private static String DATA_IS_INSERTED = "isDataInserted";
    private static String TABDATA_IS_INSERTED = "isTabDataInserted";
    private static String SERVER_DETAILS_FETCHED = "isServerDetailsFetched";
    private static String SELECTED_MENU_VIEW2 = "selectedMenuView2";
    private static String SELECTED_MENU_VIEWVOD = "selectedMenuViewVOD";
    private static String SELECTED_MENU_SORT = "selectedMenuSort";
    private static String KEY_LAYOUTMANGER_N = "layoutmanegerN";
    private static String KEY_SORTED_BYNAME = "sortedbyname";
    private static String KEY_SORTED_BYNUMBER = "sortedbynumber";
    private static String KEY_SORTED_BY_URL = "sortedbyurl";
    private static String KEY_PLAYING_EPG_URL = "playingepg";
    private static String SELECTED_CHECKEDITEM_T = "SELECTED_CHECKEDITEM_T";
    private static String CANALPROGRAMA = "canalprograma";
    private static String WEBSITEPROGRAMA = "websiteprograma";
    private static String TipoFilmesAPP = "TipoFilmesAPPT";
    private static String TIPOPROGRAMA = "tipoprograma";
    private static String INITAPP = "inicioprograma";
    private static String INITBD = "INICIOBD";
    private static String INITBDF = "INICIOBDF";
    private static String SELECTED_Server_T = "SELECTED_Server_TT";
    private static String INITAPP2 = "inicioprogramaprim";
    private static String URLPROGRAMA = "urloprograma";
    private static String TABPROGRAMA = "taboprograma";
    private static String TAB2PROGRAMA = "tabo2programa";
    private static String NOMEPROGRAMA = "nomeoprograma";
    private static String PackUser = "packusernovo";
    private static String IMAGEPROGRAMA = "imageoprograma";
    private static String PageMovie = "PageMovie";
    private static String verSBD2 = "verSBD2asasd";
    private static String verSBD3 = "verSBD3asasd";
    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    String erros = "";
    AlertDialog alert2;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getInicio() {
        return pref.getString(INITAPP, "");
    }

    public void setInicio(String inittAP) {
        editor.putString(INITAPP, inittAP);
        editor.commit();
    }

    public int getVersBD() {
        return pref.getInt(verSBD2, 0);
    }

    public String getInicioAPP() {
        return pref.getString(INITAPP2, "primeiro");
    }

    public void setInicioAPP(String inittAP2) {
        editor.putString(INITAPP2, inittAP2);
        editor.commit();
    }

    public String getTipoPrograma() {
        return pref.getString(TIPOPROGRAMA, "");
    }

    public void setTipoPrograma(String dnsUrl) {
        editor.putString(TIPOPROGRAMA, dnsUrl);
        editor.commit();
    }

    public String getNomePrograma() {
        return pref.getString(NOMEPROGRAMA, "");
    }

    public void setNomePrograma(String dnsUrl) {
        editor.putString(NOMEPROGRAMA, dnsUrl);
        editor.commit();
    }

    public String getPackUser() {
        return pref.getString(PackUser, "");
    }

    public void setPackUser(String packus) {
        editor.putString(PackUser, packus);
        editor.commit();
    }

    public String getImagePrograma() {
        return pref.getString(IMAGEPROGRAMA, "");
    }

    public void setImagePrograma(String dnsUrl) {
        editor.putString(IMAGEPROGRAMA, dnsUrl);
        editor.commit();
    }

    public String getCanalPrograma() {
        return pref.getString(CANALPROGRAMA, "");
    }

    public void setCanalPrograma(String dnsUrl) {
        editor.putString(CANALPROGRAMA, dnsUrl);
        editor.commit();
    }

    public String getWebSitePrograma() {
        return pref.getString(WEBSITEPROGRAMA, "");
    }

    public void setWebSitePrograma(String dnsUrl) {
        editor.putString(WEBSITEPROGRAMA, dnsUrl);
        editor.commit();
    }


    public String getTipoFilmesAPP() {
        return pref.getString(TipoFilmesAPP, "0");
    }

    public void setTipoFilmesAPP(String filapp) {
        editor.putString(TipoFilmesAPP, filapp);
        editor.commit();
    }

    public String getUrlPrograma() {
        return pref.getString(URLPROGRAMA, "");
    }

    public void setUrlPrograma(String dnsUrl) {
        editor.putString(URLPROGRAMA, dnsUrl);
        editor.commit();
    }

    public String getTabProgramSel() {
        return pref.getString(TABPROGRAMA, "");
    }

    public void setTabProgramSel(String asdas) {
        editor.putString(TABPROGRAMA, asdas);
        editor.commit();
    }

    public String getTab2ProgramSel() {
        return pref.getString(TAB2PROGRAMA, "");
    }

    public void setTab2ProgramSel(String dnsUrl) {
        editor.putString(TAB2PROGRAMA, dnsUrl);
        editor.commit();
    }

    /*===============set Login===============*/
    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();    // commit changes
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String getUserPASSWORD() {
        return pref.getString(PASSWORD, "");
    }

    /*===============User Password===============*/
    public void setUserPASSWORD(String loggedUserPASSWORD) {

        editor.putString(PASSWORD, loggedUserPASSWORD);
        editor.commit();    // commit changes
    }

    public boolean setArrayAnos(JSONArray array, String arrayName, Context mContext) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length());
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject tmpObj = array.getJSONObject(i);
                editor.putString(arrayName + "_" + i, tmpObj.optString("ID"));
                editor.putString(arrayName + "__" + i, tmpObj.optString("Nome"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editor.commit();
    }

    public JSONArray loadArrayAnos(String arrayName, Context mContext) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < size; i++) {
                JSONObject objqua = new JSONObject();
                objqua.put("ID", prefs.getString(arrayName + "_" + i, null));
                objqua.put("Nome", prefs.getString(arrayName + "__" + i, null));
                array.put(objqua);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return array;
    }

    public boolean setArrayExtras(JSONArray array, String arrayName, Context mContext) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length());
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject tmpObj = array.getJSONObject(i);
                editor.putString(arrayName + "_" + i, tmpObj.optString("stream_id"));
                editor.putString(arrayName + "__" + i, tmpObj.optString("stream_title"));
                editor.putString(arrayName + "___" + i, tmpObj.optString("stream_logo"));
                editor.putString(arrayName + "____" + i, tmpObj.optString("stream_url"));
                editor.putString(arrayName + "_____" + i, tmpObj.optString("stream_grupo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editor.commit();
    }

    public JSONArray loadArrayExtras(String arrayName, Context mContext) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < size; i++) {
                JSONObject objqua = new JSONObject();
                objqua.put("stream_id", prefs.getString(arrayName + "_" + i, null));
                objqua.put("stream_title", prefs.getString(arrayName + "__" + i, null));
                objqua.put("stream_logo", prefs.getString(arrayName + "___" + i, null));
                objqua.put("stream_url", prefs.getString(arrayName + "____" + i, null));
                objqua.put("stream_grupo", prefs.getString(arrayName + "_____" + i, null));
                array.put(objqua);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return array;
    }

    public void setServerCheck(Integer checkeditem) {
        editor.putInt(SELECTED_Server_T, checkeditem);
        editor.commit();
    }

    public int getServerCheck() {
        return pref.getInt(SELECTED_Server_T, 0);
    }

    public boolean setArrayProgramas(JSONArray array, String arrayName, Context mContext) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length());
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject tmpObj = array.getJSONObject(i);
                editor.putString(arrayName + "_" + i, tmpObj.optString("stream_website"));
                editor.putString(arrayName + "__" + i, tmpObj.optString("stream_title"));
                editor.putString(arrayName + "___" + i, tmpObj.optString("stream_logo"));
                editor.putString(arrayName + "____" + i, tmpObj.optString("stream_url"));
                editor.putString(arrayName + "_____" + i, tmpObj.optString("stream_grupo"));
                editor.putString(arrayName + "______" + i, tmpObj.optString("stream_tipo"));
                editor.putString(arrayName + "_______" + i, tmpObj.optString("stream_canal"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editor.commit();
    }

    public JSONArray loadArrayProgramas(String arrayName, Context mContext) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < size; i++) {
                JSONObject objqua = new JSONObject();
                objqua.put("stream_website", prefs.getString(arrayName + "_" + i, null));
                objqua.put("stream_title", prefs.getString(arrayName + "__" + i, null));
                objqua.put("stream_logo", prefs.getString(arrayName + "___" + i, null));
                objqua.put("stream_url", prefs.getString(arrayName + "____" + i, null));
                objqua.put("stream_grupo", prefs.getString(arrayName + "_____" + i, null));
                objqua.put("stream_tipo", prefs.getString(arrayName + "______" + i, null));
                objqua.put("stream_canal", prefs.getString(arrayName + "_______" + i, null));
                array.put(objqua);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return array;
    }


    public boolean setArrayCategorias2(JSONArray array, String arrayName, Context mContext) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length());
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject tmpObj = array.getJSONObject(i);
                editor.putString(arrayName + "_" + i, tmpObj.optString("ID"));
                editor.putString(arrayName + "__" + i, tmpObj.optString("Nome"));
                editor.putString(arrayName + "___" + i, tmpObj.optString("Imagem"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editor.commit();
    }

    public JSONArray loadArrayCategorias2(String arrayName, Context mContext) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < size; i++) {
                JSONObject objqua = new JSONObject();
                objqua.put("ID", prefs.getString(arrayName + "_" + i, null));
                objqua.put("Nome", prefs.getString(arrayName + "__" + i, null));
                objqua.put("Imagem", prefs.getString(arrayName + "___" + i, null));
                array.put(objqua);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return array;
    }



    public boolean setArrayCategorias3(JSONArray array, String arrayName, Context mContext) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length());
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject tmpObj = array.getJSONObject(i);
                editor.putString(arrayName + "_" + i, tmpObj.optString("ID"));
                editor.putString(arrayName + "__" + i, tmpObj.optString("Nome"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editor.commit();
    }

    public JSONArray loadArrayCategorias3(String arrayName, Context mContext) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < size; i++) {
                JSONObject objqua = new JSONObject();
                objqua.put("ID", prefs.getString(arrayName + "_" + i, null));
                objqua.put("Nome", prefs.getString(arrayName + "__" + i, null));
                array.put(objqua);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return array;
    }

    public boolean setArrayCategorias(JSONArray array, String arrayName, Context mContext) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length());
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject tmpObj = array.getJSONObject(i);
                editor.putString(arrayName + "_" + i, tmpObj.optString("id_categoria"));
                editor.putString(arrayName + "__" + i, tmpObj.optString("categorias"));
                editor.putString(arrayName + "___" + i, tmpObj.optString("tipo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editor.commit();
    }

    public JSONArray loadArrayCategorias(String arrayName, Context mContext) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < size; i++) {
                JSONObject objqua = new JSONObject();
                objqua.put("id_categoria", prefs.getString(arrayName + "_" + i, null));
                objqua.put("categorias", prefs.getString(arrayName + "__" + i, null));
                objqua.put("tipo", prefs.getString(arrayName + "___" + i, null));
                array.put(objqua);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return array;
    }

    public boolean setArray(JSONArray array, String arrayName, Context mContext) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length());
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject tmpObj = array.getJSONObject(i);
                editor.putString(arrayName + "_" + i, tmpObj.optString("Nome"));
                editor.putString(arrayName + "__" + i, tmpObj.optString("Imagem"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editor.commit();
    }

    public JSONArray loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < size; i++) {
                JSONObject objqua = new JSONObject();
                objqua.put("Nome", prefs.getString(arrayName + "_" + i, null));
                objqua.put("Imagem", prefs.getString(arrayName + "__" + i, null));
                array.put(objqua);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return array;
    }

    public boolean setArrayCanais(JSONArray array, String arrayName) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length());
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject tmpObj = array.getJSONObject(i);
                editor.putString(arrayName + "_" + i, tmpObj.optString("stream_num"));
                editor.putString(arrayName + "__" + i, tmpObj.optString("stream_title"));
                editor.putString(arrayName + "___" + i, tmpObj.optString("stream_name"));
                editor.putString(arrayName + "____" + i, tmpObj.optString("stream_url"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editor.commit();
    }

    public JSONArray loadArrayCanais(String arrayName) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < size; i++) {
                JSONObject objqua = new JSONObject();
                objqua.put("stream_num", prefs.getString(arrayName + "_" + i, null));
                objqua.put("stream_title", prefs.getString(arrayName + "__" + i, null));
                objqua.put("stream_name", prefs.getString(arrayName + "___" + i, null));
                objqua.put("stream_url", prefs.getString(arrayName + "____" + i, null));
                array.put(objqua);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return array;
    }

    public boolean setArrayEpisodios(JSONArray array, String arrayName) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length());
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject tmpObj = array.getJSONObject(i);
                editor.putString(arrayName + "_" + i, tmpObj.optString("id"));
                editor.putString(arrayName + "__" + i, tmpObj.optString("title"));
                editor.putString(arrayName + "___" + i, tmpObj.optString("container_extension"));
                editor.putString(arrayName + "____" + i, tmpObj.optString("plot"));
                editor.putString(arrayName + "_____" + i, tmpObj.optString("cast"));
                editor.putString(arrayName + "______" + i, tmpObj.optString("rating"));
                editor.putString(arrayName + "_______" + i, tmpObj.optString("releasedate"));
                editor.putString(arrayName + "________" + i, tmpObj.optString("movie_image"));
                editor.putString(arrayName + "_________" + i, tmpObj.optString("genre"));
                editor.putString(arrayName + "__________" + i, tmpObj.optString("imdb_id"));
                editor.putString(arrayName + "___________" + i, tmpObj.optString("duration"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editor.commit();
    }

    public JSONArray loadArrayEpisodios(String arrayName) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < size; i++) {
                JSONObject objqua = new JSONObject();
                objqua.put("id", prefs.getString(arrayName + "_" + i, null));
                objqua.put("title", prefs.getString(arrayName + "__" + i, null));
                objqua.put("container_extension", prefs.getString(arrayName + "___" + i, null));
                objqua.put("plot", prefs.getString(arrayName + "____" + i, null));
                objqua.put("cast", prefs.getString(arrayName + "_____" + i, null));
                objqua.put("rating", prefs.getString(arrayName + "______" + i, null));
                objqua.put("releasedate", prefs.getString(arrayName + "_______" + i, null));
                objqua.put("movie_image", prefs.getString(arrayName + "________" + i, null));
                objqua.put("genre", prefs.getString(arrayName + "_________" + i, null));
                objqua.put("imdb_id", prefs.getString(arrayName + "__________" + i, null));
                objqua.put("duration", prefs.getString(arrayName + "___________" + i, null));
                array.put(objqua);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return array;
    }




    public boolean setEpisodio(JSONArray array, String arrayName) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length());
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject tmpObj = array.getJSONObject(i);
                editor.putString(arrayName + "_" + i, tmpObj.optString("stream_imagem"));
                editor.putString(arrayName + "__" + i, tmpObj.optString("stream_name"));
                editor.putString(arrayName + "___" + i, tmpObj.optString("stream_url"));
                editor.putString(arrayName + "____" + i, tmpObj.optString("stream_rating"));
                editor.putString(arrayName + "_____" + i, tmpObj.optString("stream_temp"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editor.commit();
    }

    public JSONArray loadArrayEpisodio(String arrayName) {
        SharedPreferences prefs = _context.getSharedPreferences(PREF_NAME, 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < size; i++) {
                JSONObject objqua = new JSONObject();
                objqua.put("stream_imagem", prefs.getString(arrayName + "_" + i, null));
                objqua.put("stream_name", prefs.getString(arrayName + "__" + i, null));
                objqua.put("stream_url", prefs.getString(arrayName + "___" + i, null));
                objqua.put("stream_rating", prefs.getString(arrayName + "____" + i, null));
                objqua.put("stream_temp", prefs.getString(arrayName + "_____" + i, null));
                array.put(objqua);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return array;
    }

    public String ProcuraImageGrupo(String nameg, String icon, Context coooco) {
        String iconPass = "http://liveitkodi.com/Images/icon.png";
        Boolean mudou = false;
        JSONArray Grupos = loadArray("Grupos", coooco);
        if (Grupos != null) {
            String arrpp = serchMessages(nameg, "Nome", Grupos);

            if (!arrpp.equals("")) {
                iconPass = "http://liveitkodi.com/Logos/" + arrpp;
            } else {
                if (!icon.equals("")) {
                    if (icon.contains("http")) {
                        iconPass = icon;
                    }
                }
            }
        } else {
            iconPass = icon;
        }

        return iconPass;
    }

    public String ProcuraImageCanal(String nameg, String icon, Context coooco) {
        String iconPass = "http://liveitkodi.com/Images/icon.png";
        JSONArray Canais = loadArray("Canais", coooco);

        if (Canais != null) {
            String arrpp = serchMessages(nameg, "Nome", Canais);
            if (!arrpp.equals("")) {
                iconPass = "http://liveitkodi.com/Logos/" + arrpp;
            } else {
                if (!icon.equals("")) {
                    if (icon.contains("http")) {
                        iconPass = AlteraCaracteres(icon);
                    }
                }
            }
        } else {
            iconPass = icon;
        }

        return iconPass;
    }

    public String ProcuraImageCanal2(String nameg, String icon, Context coooco) {
        String iconPass = "http://liveitkodi.com/Images/icon.png";
        JSONArray Canais = loadArray("Canais", coooco);

        if (Canais != null) {
            String arrpp = serchMessages(nameg, "Nome", Canais);
            if (!arrpp.equals("")) {
                iconPass = "http://liveitkodi.com/Logos/" + arrpp;
            } else {
                if (!icon.equals("")) {
                    if (icon.contains("http")) {
                        iconPass = icon;
                    }
                }
            }
        } else {
            iconPass = icon;
        }

        return iconPass;
    }

    public String AlteraCaracteres(String replace) {
        String alte = replace;

        alte = alte.replaceAll("\\\\u003d", "=");
        alte = alte.replaceAll("\\\\u0026", "&");
        alte = alte.replaceAll("\\\\u0027", "'");
        alte = alte.replaceAll("\\\\u00e0", "à");
        alte = alte.replaceAll("\\\\u00e1", "á");
        alte = alte.replaceAll("\\\\u00e2", "â");
        alte = alte.replaceAll("\\\\u00e3", "ã");

        alte = alte.replaceAll("\\\\u00e9", "é");
        alte = alte.replaceAll("\\\\u00c9", "É");
        alte = alte.replaceAll("\\\\u00ea", "ê");
        alte = alte.replaceAll("\\\\u00e8", "è");

        alte = alte.replaceAll("\\\\u00ed", "í");
        alte = alte.replaceAll("\\\\u00ee", "î");

        alte = alte.replaceAll("\\\\u00f3", "ó");
        alte = alte.replaceAll("\\\\u00f3", "ó");
        alte = alte.replaceAll("\\\\u00f5", "õ");
        alte = alte.replaceAll("\\\\u00d3", "Ó");

        alte = alte.replaceAll("\\\\u00f9", "ù");
        alte = alte.replaceAll("\\\\u00fb", "û");
        alte = alte.replaceAll("\\\\u00fa", "Ù");

        alte = alte.replaceAll("\\\\u00e7", "ç");

        alte = alte.replaceAll("\\\\u00c0", "À");
        alte = alte.replaceAll("\\\\u00c3", "Ã");
        alte = alte.replaceAll("\\\\u00c1", "Á");
        alte = alte.replaceAll("\\\\u00c7", "Ç");

        alte = alte.replaceAll("\\\\u2022", "•");
        alte = alte.replaceAll("\\\\u25cf", "●");
        alte = alte.replaceAll("\\\\u2605", "★");
        alte = alte.replaceAll("\\\\u25ba", "►");
        alte = alte.replaceAll("\\\\u25c4", "◄");

        alte = alte.replaceAll("\\\\u261b", "☛");
        alte = alte.replaceAll("\\\\\u261a", "☚");

        alte = alte.replaceAll("\\\\/", "/");
        alte = alte.replaceAll("\\\\u00aa", "ª");
        return alte;
    }

    public String serchMessages(String StrPesquis, String pesqq, JSONArray arrq) {
        String nova = "";
        if (StrPesquis != "") {
            String itemPesquisa = "";
            if (arrq != null) {
                for (int n = 0; n < arrq.length(); n++) {
                    JSONObject tmpObj = new JSONObject();
                    try {
                        tmpObj = arrq.getJSONObject(n);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String pesqqq = tmpObj.optString("Nome");
                    if (pesqqq != null) {
                        itemPesquisa = pesqqq.toLowerCase();
                    }

                    String pequeno = StrPesquis.toLowerCase();
                    if (!itemPesquisa.equals("")) {
                        if (itemPesquisa.contains(pequeno)) {
                            nova = tmpObj.optString("Imagem");
                            break;
                        }
                    }
                }
            }
        }
        return nova;
    }

    public String getADULT() {
        return pref.getString(NAMEADULT, "");
    }

    /*===============User Name===============*/
    public void setADULT(String userName) {
        editor.putString(NAMEADULT, userName);
        editor.commit();
    }



    public String getUserName() {
        return pref.getString(NAME, "");
    }

    /*===============User Name===============*/
    public void setUserName(String userName) {
        editor.putString(NAME, userName);
        editor.commit();
    }

    public String getUserEmail() {
        return pref.getString(EMAIL, "");
    }

    /*===============User Email===============*/
    public void setUserEmail(String userEmail) {
        editor.putString(EMAIL, userEmail);
        editor.commit();
    }

    public String getUserAdultos() {
        return pref.getString(ADULTOSPASS, "");
    }

    /*===============User Email===============*/
    public void setUserAdultos(String userAdul) {
        editor.putString(ADULTOSPASS, userAdul);
        editor.commit();
    }

    public String getCanalSele() {
        return pref.getString(NOMECANAL, "");
    }

    /*===============User Email===============*/
    public void setCanalSele(String nomecan) {
        editor.putString(NOMECANAL, nomecan);
        editor.commit();
    }

    public String getCanalID() {
        return pref.getString(NOMECANALID, "");
    }

    /*===============User Email===============*/
    public void setCanalID(String userCanid) {
        editor.putString(NOMECANALID, userCanid);
        editor.commit();
    }

    public String getEmailAdmin() {
        return pref.getString(EMAILADMIN, "0");
    }

    /*===============User Status===============*/
    public void setEmailAdmin(String userStatus) {
        editor.putString(EMAILADMIN, userStatus);
        editor.commit();
    }

    /*===============User Status===============*/
    public void setkodiUser(String userStatus) {
        editor.putString(KODIUSER, userStatus);
        editor.commit();
    }

    public String getkodiUser() {
        return pref.getString(KODIUSER, "0");
    }

    /*===============User Status===============*/
    public void setkodiSenha(String userStatus) {
        editor.putString(KODISENHA, userStatus);
        editor.commit();
    }

    public String getkodiSenha() {
        return pref.getString(KODISENHA, "0");
    }

    public String getUserStatus() {
        return pref.getString(STATUS, "0");
    }

    /*===============User Status===============*/
    public void setUserStatus(String userStatus) {
        editor.putString(STATUS, userStatus);
        editor.commit();
    }


    public String getUserType() {
        return pref.getString(STATUSType, "0");
    }

    /*===============User Status===============*/
    public void setUserType(String userStatus) {
        editor.putString(STATUSType, userStatus);
        editor.commit();
    }

    /*===============User Status===============*/
    public void setkodiApi(String userStatus) {
        editor.putString(KODIAPI, userStatus);
        editor.commit();
    }

    public String getkodiApi() {
        return pref.getString(KODIAPI, "0");
    }


    public int getorientation() {
        return pref.getInt(verDOR, 0);
    }

    public void setorientation(Integer verSBD) {
        editor.putInt(verDOR, verSBD);
        editor.commit();
    }

    public String getUserID() {
        return pref.getString(verUserID, "");
    }

    public void setUserID(String verUser) {
        editor.putString(verUserID, verUser);
        editor.commit();
    }


    public int getorientationVODList() {
        return pref.getInt(verDORVODList, 0);
    }

    public void setorientationVODList(Integer verSBD) {
        editor.putInt(verDORVODList, verSBD);
        editor.commit();
    }

    public void setkodiToken(String userStatus) {
        editor.putString(KODITOKEN, userStatus);
        editor.commit();
    }

    public String getkodiToken() {
        return pref.getString(KODITOKEN, "");
    }

    /*===============User Status===============*/
    public void setkodiSite(String userStatus) {
        editor.putString(KODISITE, userStatus);
        editor.commit();
    }

    public String getkodiSite() {
        return pref.getString(KODISITE, "0");
    }

    /*===============User Status===============*/
    public void setkodiGrant(String userStatus) {
        editor.putString(KODIGRANT, userStatus);
        editor.commit();
    }

    public String getkodiGrant() {
        return pref.getString(KODIGRANT, "0");
    }

    /*===============User Status===============*/
    public void setFilmesAPP(String userStatus) {
        editor.putString(FilmesAPPID, userStatus);
        editor.commit();
    }

    public String getFilmesAPP() {
        return pref.getString(FilmesAPPID, "0");
    }

    /*===============User Status===============*/
    public void setkodiID(String userStatus) {
        editor.putString(KODIID, userStatus);
        editor.commit();
    }

    public String getkodiID() {
        return pref.getString(KODIID, "0");
    }

    /*===============User Status===============*/
    public void setkodiSecret(String userStatus) {
        editor.putString(KODISECRET, userStatus);
        editor.commit();
    }

    public String getkodiSecret() {
        return pref.getString(KODISECRET, "0");
    }

    public String getWebSite() {
        return pref.getString(WEBSITE, "0");
    }

    /*===============User Status===============*/
    public void setWebSite(String userStatus) {
        editor.putString(WEBSITE, userStatus);
        editor.commit();
    }

    public String getUserExpiryDate() {
        return pref.getString(EXPIRY_DATE, "0");
    }

    /*===============User Expiry date===============*/
    public void setUserExpiryDate(String userExpiryDate) {
        editor.putString(EXPIRY_DATE, userExpiryDate);
        editor.commit();
    }

    public String getUserIsTrial() {
        return pref.getString(IS_TRIAL, "0");
    }

    /*===============User is Trial===============*/
    public void setUserIsTrial(String userIsTrial) {
        editor.putString(IS_TRIAL, userIsTrial);
        editor.commit();
    }

    public String getUserMaxConnections() {
        return pref.getString(MAX_CONNECTIONS, "0");
    }

    /*===============User Max Connections===============*/
    public void setUserMaxConnections(String userMaxConnections) {
        editor.putString(MAX_CONNECTIONS, userMaxConnections);
        editor.commit();
    }

    public String getUserNome() {
        return pref.getString(NOME, "");
    }

    /*===============User Max Connections===============*/
    public void setUserNome(String userNome) {
        editor.putString(NOME, userNome);
        editor.commit();
    }

    public String getUserServidor() {
        return pref.getString(NOMEServidor, "");
    }

    /*===============User Max Connections===============*/
    public void setUserServidor(String userserv) {
        editor.putString(NOMEServidor, userserv);
        editor.commit();
    }

    public String getNameAPP() {
        return pref.getString(NOMEAPP, "");
    }

    /*===============User Max Connections===============*/
    public void setNameAPP(String userNome) {
        editor.putString(NOMEAPP, userNome);
        editor.commit();
    }

    public String getMelhoramentos() {
        return pref.getString(MELHORAAPP, "");
    }

    /*===============User Max Connections===============*/
    public void setMelhoramentos(String mememme) {
        editor.putString(MELHORAAPP, mememme);
        editor.commit();
    }


    public String getServerUrl() {
        return pref.getString(SERVER_URL, "0");
    }

    /*===============Server Url===============*/
    public void setServerUrl(String serverUrl) {
        editor.putString(SERVER_URL, serverUrl);
        editor.commit();
    }

    public String getServerPort() {
        return pref.getString(SERVER_PORT, "0");
    }

    /*===============Server Port===============*/
    public void setServerPort(String serverPort) {
        editor.putString(SERVER_PORT, serverPort);
        editor.commit();
    }

    public String getUserAuth() {
        return pref.getString(USER_AUTH, "0");
    }

    /*===============User Authentication===============*/
    public void setUserAuth(String userAuth) {
        editor.putString(USER_AUTH, userAuth);
        editor.commit();
    }

    public String getEPGUrl() {
        return pref.getString(EPG_URL, "");
    }

    /*===============EPG Url===============*/
    public void setEPGUrl(String epgUrl) {
        editor.putString(EPG_URL, epgUrl);
        editor.commit();
    }


    public String getTMDBUrl() {
        return pref.getString(TMDB_1, "");
    }

    /*===============EPG Url===============*/
    public void setTMDBUrl(String tmdb1) {
        editor.putString(TMDB_1, tmdb1);
        editor.commit();
    }


    public String getTMDBAPI() {
        return pref.getString(TMDB_2, "");
    }

    /*===============EPG Url===============*/
    public void setTMDBAPI(String tmdb1) {
        editor.putString(TMDB_2, tmdb1);
        editor.commit();
    }

    public String getTMDBIMG() {
        return pref.getString(TMDB_3, "");
    }

    /*===============EPG Url===============*/
    public void setTMDBIMG(String tmdb1) {
        editor.putString(TMDB_3, tmdb1);
        editor.commit();
    }

    public String getSeriesCat() {
        return pref.getString(TMDB_4, "");
    }

    /*===============EPG Url===============*/
    public void setSeriesCat(String tmdb1) {
        editor.putString(TMDB_4, tmdb1);
        editor.commit();
    }

    public String getDNSUrl() {
        return pref.getString(DNS_URL, "");
    }


    /*===============DNS Url===============*/
    public void setDNSUrl(String dnsUrl) {
        editor.putString(DNS_URL, dnsUrl);
        editor.commit();
    }

    public String getSeries() {
        return pref.getString(SERIES, "");
    }


    /*===============DNS Url===============*/
    public void setSeries(String serr) {
        editor.putString(SERIES, serr);
        editor.commit();
    }

    public String getOrdena() {
        return pref.getString(ORDENA, "");
    }


    /*===============DNS Url===============*/
    public void setOrdena(String serr) {
        editor.putString(ORDENA, serr);
        editor.commit();
    }

    public String getUserAcess_Token() {
        return pref.getString(UserAcess_Token, "");
    }

    /*===============DNS Url===============*/
    public void setUserAcess_Token(String dnsUrl) {
        editor.putString(UserAcess_Token, dnsUrl);
        editor.commit();
    }


    public String getUserFundo() {
        return pref.getString(Fundo, "");
    }

    /*===============DNS Url===============*/
    public void setUserFundo(String ddsads) {
        editor.putString(Fundo, ddsads);
        editor.commit();
    }


    public String getClient_Secret() {
        return pref.getString(UserClient_Secret, "");
    }

    /*===============DNS Url===============*/
    public void setClient_Secret(String dnsUrl) {
        editor.putString(UserClient_Secret, dnsUrl);
        editor.commit();
    }

    public String getAdultPassword() {
        return pref.getString(ADULT_PASSWORD, "");
    }

    /*===============Adult Password===============*/
    public void setAdultPassword(String adultPassword) {
        editor.putString(ADULT_PASSWORD, adultPassword);
        editor.commit();
    }

    /*===============set Select===============*/
    public void setselect(boolean isSelected) {

        editor.putBoolean(KEY_IS_SELECTED, isSelected);
        editor.commit();    // commit changes
        Log.d(TAG, "User selected session modified!");
    }

    public boolean isSelected() {
        return pref.getBoolean(KEY_IS_SELECTED, false);
    }

    public String getPlayingEPGUrl() {
        return pref.getString(KEY_PLAYING_EPG_URL, "");
    }

    public void setPlayingEPGUrl(String epg_url) {

        editor.putString(KEY_PLAYING_EPG_URL, epg_url);
        editor.commit();    // commit changes
        Log.d(TAG, "User selected session modified!");
    }

    public void setcheckeditem(Integer checkeditem) {
        editor.putInt(SELECTED_CHECKEDITEM_T, checkeditem);
        editor.commit();
    }

    public int getcheckeditem() {
        return pref.getInt(SELECTED_CHECKEDITEM_T, 1);
    }

    /*===============set Player TV===============*/
    public void setcheckedplayerTV(Integer checkedPlayeritem) {
        editor.putInt(SELECTED_CHECKED_ITEM_TV_1, checkedPlayeritem);
        editor.commit();
    }

    public int getcheckedplayerTV() {
        return pref.getInt(SELECTED_CHECKED_ITEM_TV_1, 0);
    }

    public String getPlayerSelectedTV() {
        return pref.getString(SELECTED_PLAYER_ITEM_TV_1, "Embutido");
    }

    public void setPlayerSelectedTV(String SelectedPackage) {
        editor.putString(SELECTED_PLAYER_ITEM_TV_1, SelectedPackage);
        editor.commit();
    }

    public String getPackageSelectedTV() {
        return pref.getString(SELECTED_PACKAGE_TV_1, "Player Embutido (Padrão)");
    }

    public String getPackageSelectedTVNovo() {
        return pref.getString(SELECTED_PACKAGE_TV_Novo, "com.mxtech.videoplayer.ad");
    }

    public void setPackageSelectedNovo(String SelectedPackage) {
        editor.putString(SELECTED_PACKAGE_TV_Novo, SelectedPackage);
        editor.commit();
    }

    public void setPackageSelectedTV(String SelectedPackage) {
        editor.putString(SELECTED_PACKAGE_TV_1, SelectedPackage);
        editor.commit();
    }

    public void setcheckedplayerHits(Integer checkedPlayeritem) {
        editor.putInt(SELECTED_CHECKED_ITEM_Hits, checkedPlayeritem);
        editor.commit();
    }

    public int getcheckedplayerHits() {
        return pref.getInt(SELECTED_CHECKED_ITEM_Hits, 0);
    }

    public String getPlayerSelectedHits() {
        return pref.getString(SELECTED_PLAYER_ITEM_Hits, "Embutido");
    }

    public void setPlayerSelectedHits(String SelectedPackage) {
        editor.putString(SELECTED_PLAYER_ITEM_Hits, SelectedPackage);
        editor.commit();
    }

    public String getPackageSelectedHits() {
        return pref.getString(SELECTED_PACKAGE_Hits, "");
    }

    public void setPackageSelectedHits(String SelectedPackage) {
        editor.putString(SELECTED_PACKAGE_Hits, SelectedPackage);
        editor.commit();
    }





    /*===============set Player FilmesAPP===============*/
    public void setcheckedplayerFilmesAPP(Integer checkedPlayeritem) {
        editor.putInt(SELECTED_CHECKED_FilmesAPP_1, checkedPlayeritem);
        editor.commit();
    }

    public int getcheckedplayerFilmesAPP() {
        return pref.getInt(SELECTED_CHECKED_FilmesAPP_1, 2);
    }

    public String getPlayerSelectedFilmesAPP() {
        return pref.getString(SELECTED_PLAYER_FilmesAPP_1, "MX Player");
    }

    public void setPlayerSelectedFilmesAPP(String SelectedPackage) {
        editor.putString(SELECTED_PLAYER_FilmesAPP_1, SelectedPackage);
        editor.commit();
    }

    public String getPackageSelectedFilmesAPP() {
        return pref.getString(SELECTED_PACKAGE_FilmesAPP_1, "com.mxtech.videoplayer.ad");
    }

    public void setPackageSelectedFilmesAPP(String SelectedPackage) {
        editor.putString(SELECTED_PACKAGE_FilmesAPP_1, SelectedPackage);
        editor.commit();
    }


    /*===============set Player Praias===============*/
    public void setcheckedplayerPraias(Integer checkedPlayeritem) {
        editor.putInt(SELECTED_CHECKED_Praias_1, checkedPlayeritem);
        editor.commit();
    }

    public int getcheckedplayerPraias() {
        return pref.getInt(SELECTED_CHECKED_Praias_1, 0);
    }

    public String getPlayerSelectedPraias() {
        return pref.getString(SELECTED_PLAYER_Praias_1, "Embutido");
    }

    public void setPlayerSelectedPraias(String SelectedPackage) {
        editor.putString(SELECTED_PLAYER_Praias_1, SelectedPackage);
        editor.commit();
    }

    public String getPackageSelectedPraias() {
        return pref.getString(SELECTED_PACKAGE_Praias_1, "Player Embutido (Padrão)");
    }

    public void setPackageSelectedPraias(String SelectedPackage) {
        editor.putString(SELECTED_PACKAGE_Praias_1, SelectedPackage);
        editor.commit();
    }

    /*===============set Player Gravacao===============*/
    public void setcheckedplayerGravacao(Integer checkedPlayeritem) {
        editor.putInt(SELECTED_CHECKED_Gravacao_1, checkedPlayeritem);
        editor.commit();
    }

    public int getcheckedplayerGravacao() {
        return pref.getInt(SELECTED_CHECKED_Gravacao_1, 0);
    }

    public String getPlayerSelectedGravacao() {
        return pref.getString(SELECTED_PLAYER_Gravacao_1, "Embutido");
    }

    public void setPlayerSelectedGravacao(String SelectedPackage) {
        editor.putString(SELECTED_PLAYER_Gravacao_1, SelectedPackage);
        editor.commit();
    }

    public String getPackageSelectedGravacao() {
        return pref.getString(SELECTED_PACKAGE_Gravacao_1, "Player Embutido (Padrão)");
    }

    public void setPackageSelectedGravacao(String SelectedPackage) {
        editor.putString(SELECTED_PACKAGE_Gravacao_1, SelectedPackage);
        editor.commit();
    }

    /*===============set Player Programas===============*/
    public void setcheckedplayerProgramas(Integer checkedPlayeritem) {
        editor.putInt(SELECTED_CHECKED_Programas_1, checkedPlayeritem);
        editor.commit();
    }

    public int getcheckedplayerProgramas() {
        return pref.getInt(SELECTED_CHECKED_Programas_1, 0);
    }

    public String getPlayerSelectedProgramas() {
        return pref.getString(SELECTED_PLAYER_Programas_1, "Embutido");
    }

    public void setPlayerSelectedProgramas(String SelectedPackage) {
        editor.putString(SELECTED_PLAYER_Programas_1, SelectedPackage);
        editor.commit();
    }

    public String getPackageSelectedProgramas() {
        return pref.getString(SELECTED_PACKAGE_Programas_1, "Player Embutido (Padrão)");
    }

    public void setPackageSelectedProgramas(String SelectedPackage) {
        editor.putString(SELECTED_PACKAGE_Programas_1, SelectedPackage);
        editor.commit();
    }

    /*===============set Player Radios===============*/
    public void setcheckedplayerRadios(Integer checkedPlayeritem) {
        editor.putInt(SELECTED_CHECKED_Radios_1, checkedPlayeritem);
        editor.commit();
    }

    public int getcheckedplayerRadios() {
        return pref.getInt(SELECTED_CHECKED_Radios_1, 0);
    }

    public String getPlayerSelectedRadios() {
        return pref.getString(SELECTED_PLAYER_Radios_1, "Embutido");
    }

    public void setPlayerSelectedRadios(String SelectedPackage) {
        editor.putString(SELECTED_PLAYER_Radios_1, SelectedPackage);
        editor.commit();
    }

    public String getPackageSelectedRadios() {
        return pref.getString(SELECTED_PACKAGE_Radios_1, "Player Embutido (Padrão)");
    }

    public void setPackageSelectedRadios(String SelectedPackage) {
        editor.putString(SELECTED_PACKAGE_Radios_1, SelectedPackage);
        editor.commit();
    }

    /*===============set Player FilmesLista===============*/
    public void setcheckedplayerFilmesLista(Integer checkedPlayeritem) {
        editor.putInt(SELECTED_CHECKED_FilmesLista_1, checkedPlayeritem);
        editor.commit();
    }

    public int getcheckedplayerFilmesLista() {
        return pref.getInt(SELECTED_CHECKED_FilmesLista_1, 0);
    }

    public String getPlayerSelectedFilmesLista() {
        return pref.getString(SELECTED_PLAYER_FilmesLista_1, "Embutido");
    }

    public void setPlayerSelectedFilmesLista(String SelectedPackage) {
        editor.putString(SELECTED_PLAYER_FilmesLista_1, SelectedPackage);
        editor.commit();
    }

    public String getPackageSelectedFilmesLista() {
        return pref.getString(SELECTED_PACKAGE_FilmesLista_1, "Player Embutido (Padrão)");
    }

    public void setPackageSelectedFilmesLista(String SelectedPackage) {
        editor.putString(SELECTED_PACKAGE_FilmesLista_1, SelectedPackage);
        editor.commit();
    }

    public String getLocalSelected() {
        return pref.getString(SELECTED_LOCAL_ITEM, "pt");
    }

    /*===============local SelectedElements===============*/
    public void setLocalSelected(String SelectedLanguage) {
        editor.putString(SELECTED_LOCAL_ITEM, SelectedLanguage);
        editor.commit();
    }

    /*===============User pageSelectedElements===============*/
    public void setPageSelected(String SelectedPage) {
        editor.putString(SELECTED_PAGE, SelectedPage);
        editor.commit();
    }

    /*===============page Select===============*/
    public void setcheckedpage(Integer checkedpageitem) {
        editor.putInt(SELECTED_PAGE_ITEM, checkedpageitem);
        editor.commit();
    }

    public int getcheckedpage() {
        return pref.getInt(SELECTED_PAGE_ITEM, 0);
    }

    public int getMenuView() {
        return pref.getInt(SELECTED_MENU_VIEW2, 0);
    }

    /*===============Menu View Change===============*/
    public void setMenuView(Integer selectedMenuView) {
        editor.putInt(SELECTED_MENU_VIEW2, selectedMenuView);
        editor.commit();
    }


    public int getMenuViewVOD() {
        return pref.getInt(SELECTED_MENU_VIEWVOD, 0);
    }

    /*===============Menu View Change===============*/
    public void setMenuViewVOD(Integer selectedMenuView) {
        editor.putInt(SELECTED_MENU_VIEWVOD, selectedMenuView);
        editor.commit();
    }

    public int getSorting() {
        return pref.getInt(SELECTED_MENU_SORT, 0);
    }

    /*===============Menu Sorting===============*/
    public void setSorting(Integer selectedMenuSort) {
        editor.putInt(SELECTED_MENU_SORT, selectedMenuSort);
        editor.commit();
    }

    /*============Layout View Change==============*/
    public void setlayout(String linearViewHorizontal) {
        editor.putString(KEY_LAYOUTMANGER_N, linearViewHorizontal).commit();
    }

    public String getlayout() {
        return pref.getString(KEY_LAYOUTMANGER_N, "linearViewVertical");
    }

    public String getAdultDialogPassword() {
        return pref.getString(ADULTDIALOGPASSWORD, "");
    }

    /*===============Adult Password===============*/
    public void setAdultDialogPassword(String adultDialogPassword) {
        editor.putString(ADULTDIALOGPASSWORD, adultDialogPassword);
        editor.commit();
    }

    /*===============set wifiSelect===============*/
    public void setWifi(boolean isWIFIselected) {

        editor.putBoolean(WIFI_IS_PAGESELECTED, isWIFIselected);
        editor.commit();    // commit changes
        Log.d(TAG, "User login session modified!");
    }

    public boolean isWIFIselected() {
        return pref.getBoolean(WIFI_IS_PAGESELECTED, false);
    }

    public String getWifiStatus() {
        return pref.getString(STATUSYES, "No");
    }

    /*===============Wifi Status Update===============*/
    public void setWifiStatus(String statusYes) {
        editor.putString(STATUSYES, statusYes);
        editor.commit();
    }

    /*===============set ChannelNo===============*/
    public void setshowchannelno(boolean isChannelselected) {

        editor.putBoolean(KEY_IS_CHANNELSELECTED, isChannelselected);
        editor.commit();
    }

    public boolean isChannelnoselected() {
        return pref.getBoolean(KEY_IS_CHANNELSELECTED, false);
    }

    public boolean isBoot() {
        return pref.getBoolean(KEY_IS_BOOT, false);
    }

    /*===============set ChannelNo===============*/
    public void setBoot(boolean isbootselected) {
        editor.putBoolean(KEY_IS_BOOT, isbootselected);
        editor.commit();
    }

    public boolean isBootIPTVCORE() {
        return pref.getBoolean(KEY_IS_BOOT_IPTVCORE, true);
    }

    public void setBootIPTVCORE(boolean isbootselected) {
        editor.putBoolean(KEY_IS_BOOT_IPTVCORE, isbootselected);
        editor.commit();
    }

    public boolean isBootExceptional() {
        return pref.getBoolean(KEY_IS_BOOT_Exceptional, false);
    }

    public void setBootExceptional(boolean isbootselected) {
        editor.putBoolean(KEY_IS_BOOT_Exceptional, isbootselected);
        editor.commit();
    }

    /*===============User PlayerSelectedElements===============*/
    public void setcheckedEPG(Integer SelectedPlayer) {
        editor.putInt(SELECTED_CHECKE_N, SelectedPlayer);
        editor.commit();
    }

    public int getcheckedEPG() {
        return pref.getInt(SELECTED_CHECKE_N, 0);
    }


    /*===============User PlayerSelectedElements===============*/
    public void setcheckedColor(Integer SelectedPlayer) {
        editor.putInt(SELECTED_CHECKE_Color, SelectedPlayer);
        editor.commit();
    }

    public int getcheckedColor() {
        return pref.getInt(SELECTED_CHECKE_Color, 0);
    }

    /*===============User PlayerSelectedElements===============*/
    public void setcheckedMovie(Integer SelectedPlayer) {
        editor.putInt(SELECTED_CHECKEDMOVIE, SelectedPlayer);
        editor.commit();
    }

    public int getcheckedMovie() {
        return pref.getInt(SELECTED_CHECKEDMOVIE, 2);
    }


    /*===============Insert All Channels data to sqLite===============*/
    public void setisDataInserted(boolean isDataInserted) {

        editor.putBoolean(DATA_IS_INSERTED, isDataInserted);
        editor.commit();    // commit changes
//        Log.d(TAG, "User login session modified!");
    }

    public boolean isDataInserted() {
        return pref.getBoolean(DATA_IS_INSERTED, false);
    }


    /*===============Check Tab Data inserted or not===============*/
    public void setisTabDataInserted(boolean isTabDataInserted) {

        editor.putBoolean(TABDATA_IS_INSERTED, isTabDataInserted);
        editor.commit();    // commit changes
//        Log.d(TAG, "User login session modified!");
    }

    public boolean isTabDataInserted() {
        return pref.getBoolean(TABDATA_IS_INSERTED, false);
    }

    public boolean isServerDetailsFetched() {
        return pref.getBoolean(SERVER_DETAILS_FETCHED, false);
    }

    /*===============Check Server Data Fetched or not===============*/
    public void setServerDetailsFetched(boolean isServerDetailsFetched) {

        editor.putBoolean(SERVER_DETAILS_FETCHED, isServerDetailsFetched);
        editor.commit();    // commit changes
//        Log.d(TAG, "User login session modified!");
    }

    /*===============check sorted by name===============*/
    public void setsortedbyname(boolean isSortedByname) {

        editor.putBoolean(KEY_SORTED_BYNAME, isSortedByname);
        editor.commit();    // commit changes
//        Log.d(TAG, "User login session modified!");
    }

    public boolean isSortedbyname() {
        return pref.getBoolean(KEY_SORTED_BYNAME, false);
    }

    /*===============check sorted by number===============*/
    public void setsortedbynumber(boolean isSortedBynumber) {

        editor.putBoolean(KEY_SORTED_BYNUMBER, isSortedBynumber);
        editor.commit();    // commit changes
//        Log.d(TAG, "User login session modified!");
    }

    public boolean isSortedbynumber() {
        return pref.getBoolean(KEY_SORTED_BYNUMBER, true);
    }

    /*===============check sorted by URL===============*/
    public void setsortedbyURL(boolean isSortedBynumber) {

        editor.putBoolean(KEY_SORTED_BY_URL, isSortedBynumber);
        editor.commit();    // commit changes
//        Log.d(TAG, "User login session modified!");
    }

    public boolean isSortedbyURL() {
        return pref.getBoolean(KEY_SORTED_BY_URL, false);
    }


}