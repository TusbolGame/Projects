package com.liveitandroid.liveit.model.callback;

public class SeriesDBModel {
    private String cast;
    private String categoryId;
    private String cover;
    private String director;
    private String genre;
    private int idAuto;
    private String last_modified;
    private String name;
    private String num;
    private String plot;
    private String rating;
    private String youtube_trailer;
    private String releaseDate;
    private int seriesID;
    private String streamType;

    public SeriesDBModel() {

    }

    public String getYoutube() {
        return this.youtube_trailer;
    }

    public void setYoutube(String imdbId) {
        if(!(imdbId.equals("") || imdbId.equals(null))){
            this.youtube_trailer = imdbId;
        }else{
            this.youtube_trailer = "";
        }
    }

    public String getlast_modified() {
        return this.last_modified;
    }

    public void setlast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    public String getrating() {
        return this.rating;
    }

    public void setrating(String rating) {
        this.rating = rating;
    }

    public int getIdAuto() {
        return this.idAuto;
    }

    public void setIdAuto(int idAuto) {
        this.idAuto = idAuto;
    }

    public String getNum() {
        return this.num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreamType() {
        return this.streamType;
    }

    public void setStreamType(String streamType) {
        this.streamType = streamType;
    }

    public int getseriesID() {
        return this.seriesID;
    }

    public void setseriesID(int seriesID) {
        this.seriesID = seriesID;
    }

    public String getcover() {
        return this.cover;
    }

    public void setcover(String cover) {
        this.cover = cover;
    }

    public String getplot() {
        return this.plot;
    }

    public void setplot(String plot) {
        this.plot = plot;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getcast() {
        return this.cast;
    }

    public void setcast(String cast) {
        this.cast = cast;
    }

    public String getdirector() {
        return this.director;
    }

    public void setdirector(String director) {
        this.director = director;
    }

    public String getgenre() {
        return this.genre;
    }

    public void setgenre(String genre) {
        this.genre = genre;
    }

    public String getreleaseDate() {
        return this.releaseDate;
    }

    public void setreleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
