package com.liveitandroid.liveit.model;

import java.util.ArrayList;

/**
 * Created by kane on 3/15/17.
 */

public class ListMoviesSetterGetter implements java.io.Serializable {

    String praias_id;
    String praias_name;
    String praias_name_en;
    String praias_legenda;
    String praias_group;
    String praias_imagem;
    String praias_url;
    String praias_imageGroup;
    String praias_disp;
    String praias_desc;
    String praias_url2;
    String praias_url3;
    String praias_url4;
    String praias_url5;

    String ano;
    String ano2;
    String categoria;
    String categoria2;
    String categoria3;
    String atores;
    String director;
    String rating;
    String trailer;
    String Estado;
    String Fim;
    String imdb;

    String pageNavNum;
    String pageNavNum2 = "";
    String pageNavNumback = "";
    String pageMaxNum;

    String temporadas;
    ArrayList<ListMoviesSetterGetter> list_itemQualidades;

    public ArrayList<ListMoviesSetterGetter> getQualidades() {
        return list_itemQualidades;
    }

    public void setQualidades(ArrayList<ListMoviesSetterGetter> qualii)
    {
        this.list_itemQualidades = qualii;
    }

    public String getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(String temporadas) {
        this.temporadas = temporadas;
    }

    public String getPageMaxNum() {
        return pageMaxNum;
    }

    public void setPageMaxNum(String pageMaxNum) {
        this.pageMaxNum = pageMaxNum;
    }

    public String getPageNavNum() {
        return pageNavNum;
    }

    public void setPageNavNum(String pageNavNum) {
        this.pageNavNum = pageNavNum;
    }

    public String getPageNavNum2() {
        return pageNavNum2;
    }

    public void setPageNavNum2(String pageNavNum2) {
        this.pageNavNum2 = pageNavNum2;
    }

    public String getPageNavNumback() {
        return pageNavNumback;
    }

    public void setPageNavNumback(String pageNavNumback) {
        this.pageNavNumback = pageNavNumback;
    }


    public String getPraias_url2() {
        return praias_url2;
    }

    public void setPraias_url2(String praias_url2) {
        this.praias_url2 = praias_url2;
    }

    public String getPraias_url3() {
        return praias_url3;
    }

    public void setPraias_url3(String praias_url3) {
        this.praias_url3 = praias_url3;
    }

    public String getPraias_url4() {
        return praias_url4;
    }

    public void setPraias_url4(String praias_url4) {
        this.praias_url4 = praias_url4;
    }

    public String getPraias_url5() {
        return praias_url5;
    }

    public void setPraias_url5(String praias_url5) {
        this.praias_url5 = praias_url5;
    }


    public String getPraias_id() {
        return praias_id;
    }

    public void setPraias_id(String praias_id) {
        this.praias_id = praias_id;
    }

    public String getPraias_name() {
        return praias_name;
    }

    public void setPraias_name(String praias_name) {
        this.praias_name = praias_name;
    }

    public String getPraias_name_en() {
        return praias_name_en;
    }

    public void setPraias_name_en(String praias_name_en) {
        this.praias_name_en = praias_name_en;
    }

    public String getPraias_legenda() {
        return praias_legenda;
    }

    public void setPraias_legenda(String praias_legenda) {
        this.praias_legenda = praias_legenda;
    }

    public String getPraias_group() {
        return praias_group;
    }

    public void setPraias_group(String praias_group) {
        this.praias_group = praias_group;
    }

    public String getPraias_imagem() {
        return praias_imagem;
    }

    public void setPraias_imagem(String praias_imagem) {
        this.praias_imagem = praias_imagem;
    }

    public String getPraias_url() {
        return praias_url;
    }

    public void setPraias_url(String praias_url) {
        this.praias_url = praias_url;
    }

    public String getPraias_imageGroup() {
        return praias_imageGroup;
    }

    public void setPraias_imageGroup(String praias_imageGroup) {
        this.praias_imageGroup = praias_imageGroup;
    }

    public String getPraias_disp() {
        return praias_disp;
    }

    public void setPraias_disp(String praias_disp) {
        this.praias_disp = praias_disp;
    }

    public String getPraias_desc() {
        return praias_desc;
    }

    public void setPraias_desc(String praias_desc) {
        this.praias_desc = praias_desc;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getAno2() {
        return ano2;
    }

    public void setAno2(String ano2) {
        this.ano2 = ano2;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria2() {
        return categoria2;
    }

    public void setCategoria2(String categoria2) {
        this.categoria2 = categoria2;
    }

    public String getCategoria3() {
        return categoria3;
    }

    public void setCategoria3(String categoria3) {
        this.categoria3 = categoria3;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public String getFim() {
        return Fim;
    }

    public void setFim(String Fim) {
        this.Fim = Fim;
    }


    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }


}
