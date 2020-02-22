package com.liveitandroid.liveit;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

public class Movie implements Serializable {

    private static final String TAG = Movie.class.getSimpleName();

    static final long serialVersionUID = 727566175075960653L;
    private Integer id;
    private String epg_id;
    private String lang;
    private String channel_id;
    private String has_archive;
    private String title;
    private String studio;
    private String description;
    private String cardImageUrl;
    private String videoUrl;
    private String quality;
    private String uri;
    private String data_uri;
    private String cat_id;
    private String permalink;
    private String stream_uniq_id;
    private String movie_uniq_id;
    private String subtitle = "";
    private String TipoPrograma;
    private String CanalPrograma;
    private String WebSitePrograma;
    private String Start;
    private String End;
    private Integer UrlID;
    private String Start_timestamp;
    private String Stop_timestamp;
    private String Now_playing;
    private String UrlPrograma;
    private String Sub;
    private Integer Num = 0;
    private String aberto;
    private String Favorito;
    private String Tipo;
    private String Canal;
    private String praias_id;
    private String Praias_url2;
    private String Praias_url3;
    private String Praias_url4;
    private String Imdb;
    private String Rating;
    private String Director;
    private String Trailer;
    String Estado;
    String Fim;
    private String PageNavNum;
    private String praias_name;
    private String praias_name_en;
    private Drawable image_url;
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
    String ano;
    String tipologia;
    String ano2;
    String categoria;
    String categoria2;
    String categoria3;
    String atores;
    String director;
    String rating;
    String trailer;
    String imdb;
    String realisado;
    String duracao;
    String pageMaxNum;
    String pageNavNum2 = "";
    String pageNavNumback = "";
    String descricao;

    String temporadas;
    Integer info;
    ArrayList<Movie> list_itemQualidades;

    public ArrayList<Movie> getQualidades() {
        return list_itemQualidades;
    }

    public void setQualidades(ArrayList<Movie> qualii)
    {
        this.list_itemQualidades = qualii;
    }
    public Integer getInfo() {
        return info;
    }

    public void setInfo(Integer idinfo) {
        this.info = idinfo;
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descri) {
        this.descricao = descri;
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

    public String getRealisado() {
        return realisado;
    }

    public void setRealisado(String reall) {
        this.realisado = reall;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String ddd) {
        this.duracao = ddd;
    }

    public void setPageMaxNum(String pageMaxNum) {
        this.pageMaxNum = pageMaxNum;
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

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipps) {
        this.tipologia = tipps;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEpg_id() {
        return epg_id;
    }

    public void setEpg_id(String epg_id) {
        this.epg_id = epg_id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getHas_archive() {
        return has_archive;
    }

    public void setHas_archive(String has_archive) {
        this.has_archive = has_archive;
    }

    public String getTitle() {
        return title;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getQuality() {
        return quality;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPraias_group() {
        return praias_group;
    }

    public void setPraias_group(String praias_group) {
        this.praias_group = praias_group;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.cardImageUrl = cardImageUrl;
    }

    public Movie() {
    }

    public URI getCardImageURI() {
        try {
            return new URI(getCardImageUrl());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", cardImageUrl='" + cardImageUrl + '\'' +
                ",videourl='"+ videoUrl + '\''+
                ",uri='"+ uri + '\''+
                '}';
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public String getData_uri() {
        return data_uri;
    }

    public void setData_uri(String data_uri) {
        this.data_uri = data_uri;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getStream_uniq_id() {
        return stream_uniq_id;
    }

    public void setStream_uniq_id(String stream_uniq_id) {
        this.stream_uniq_id = stream_uniq_id;
    }

    public String getMovie_uniq_id() {
        return movie_uniq_id;
    }

    public void setMovie_uniq_id(String movie_uniq_id) {
        this.movie_uniq_id = movie_uniq_id;
    }

    public Drawable getImage_url() {
        return image_url;
    }

    public void setImage_url(Drawable image_url) {
        this.image_url = image_url;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTipoPrograma() {
        return TipoPrograma;
    }

    public void setTipoPrograma(String tipoPrograma) {
        TipoPrograma = tipoPrograma;
    }

    public String getCanalPrograma() {
        return CanalPrograma;
    }

    public void setCanalPrograma(String canalPrograma) {
        CanalPrograma = canalPrograma;
    }

    public String getWebSitePrograma() {
        return WebSitePrograma;
    }

    public void setWebSitePrograma(String webSitePrograma) {
        WebSitePrograma = webSitePrograma;
    }

    public String getUrlPrograma() {
        return UrlPrograma;
    }

    public void setUrlPrograma(String urlPrograma) {
        UrlPrograma = urlPrograma;
    }

    public String getStart() {
        return Start;
    }

    public void setStart(String start) {
        Start = start;
    }

    public String getEnd() {
        return End;
    }

    public void setEnd(String end) {
        End = end;
    }

    public String getStart_timestamp() {
        return Start_timestamp;
    }

    public void setStart_timestamp(String start_timestamp) {
        Start_timestamp = start_timestamp;
    }

    public String getStop_timestamp() {
        return Stop_timestamp;
    }

    public void setStop_timestamp(String stop_timestamp) {
        Stop_timestamp = stop_timestamp;
    }

    public String getNow_playing() {
        return Now_playing;
    }

    public void setNow_playing(String now_playing) {
        Now_playing = now_playing;
    }

    public String getSub() {
        return Sub;
    }

    public void setSub(String sub) {
        Sub = sub;
    }

    public Integer getNum() {
        return Num;
    }

    public void setNum(Integer num) {
        Num = num;
    }

    public String getAberto() {
        return aberto;
    }

    public void setAberto(String aberto) {
        this.aberto = aberto;
    }

    public String getFavorito() {
        return Favorito;
    }

    public void setFavorito(String favorito) {
        Favorito = favorito;
    }

    public Integer getUrlID() {
        return UrlID;
    }

    public void setUrlID(Integer urlID) {
        UrlID = urlID;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getCanal() {
        return Canal;
    }

    public void setCanal(String canal) {
        Canal = canal;
    }

    public String getPraias_url2() {
        return Praias_url2;
    }

    public void setPraias_url2(String praias_url2) {
        Praias_url2 = praias_url2;
    }

    public String getPraias_url3() {
        return Praias_url3;
    }

    public void setPraias_url3(String praias_url3) {
        Praias_url3 = praias_url3;
    }

    public String getPraias_url4() {
        return Praias_url4;
    }

    public void setPraias_url4(String praias_url4) {
        Praias_url4 = praias_url4;
    }

    public String getImdb() {
        return Imdb;
    }

    public void setImdb(String imdb) {
        Imdb = imdb;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getTrailer() {
        return Trailer;
    }

    public void setTrailer(String trailer) {
        Trailer = trailer;
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

    public String getPraias_desc() {
        return praias_desc;
    }

    public void setPraias_desc(String praias_desc) {
        this.praias_desc = praias_desc;
    }

    public String getPageNavNum() {
        return PageNavNum;
    }

    public void setPageNavNum(String pageNavNum) {
        PageNavNum = pageNavNum;
    }
}