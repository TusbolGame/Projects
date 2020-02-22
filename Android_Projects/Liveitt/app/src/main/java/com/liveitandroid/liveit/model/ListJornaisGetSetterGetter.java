package com.liveitandroid.liveit.model;

import org.json.JSONArray;

/**
 * Created by kane on 3/20/17.
 */

public class ListJornaisGetSetterGetter implements java.io.Serializable {
    String name;
    String tipo;
    String canal;
    String imagem;
    String stream_url;
    String website;
    Integer id;
    JSONArray tatudo;

    public JSONArray getEpisodios() {
        return tatudo;
    }

    public void setEpisodios(JSONArray tatudo2) {
        this.tatudo = tatudo2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer idcan) {
        this.id = idcan;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String group) {
        this.tipo = group;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String logo) {
        this.imagem = logo;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String imagem) {
        this.canal = imagem;
    }

    public String getStream_url() {
        return stream_url;
    }

    public void setStream_url(String stream_url) {
        this.stream_url = stream_url;
    }

    public String getWebSite() {
        return website;
    }

    public void setWebSite(String websitess) {
        this.website = websitess;
    }
}
