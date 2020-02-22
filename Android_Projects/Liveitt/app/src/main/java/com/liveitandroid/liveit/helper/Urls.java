package com.liveitandroid.liveit.helper;


public interface Urls {
    /*==========Service Urls===========*/
    final String urlValid = "http://liveitkodi.com/PHP/validateAPP.php?nome_app=";

    /*==========Login Urls===========*/
    final String urlLogin1 = "PHP/liveit/loginAPP.php?username=";
    final String urlLogin2 = "&password=";
    final String urlHits = "PHP/liveit/buscarHits.php?";
    final String urlHitsGrupos = "PHP/liveit/buscarHitsGrupos.php?";
    final String urlPraias = "PHP/liveit/buscarpraias.php?";
    final String urlRadios = "PHP/liveit/buscarradios.php?";
    final String urlProgramas = "PHP/liveit/buscarnewtelenovelas.php?tipo=Programas";
    final String urlExtrasInfo = "PHP/liveit/buscarinfotelenovelas.php";
    final String urlJornais = "PHP/liveit/buscarnewtelenovelas.php?tipo=Jornais";

    /*==========Parse URL===========*/
    final String urlParseURL = "PHP/liveit/get_UrlData.php";

    /*===========Other Urls===========*/
    final String urlPlaystore = "https://play.google.com/store/apps/details?id=";
    final String urlMarket = "market://details?id=";
}
