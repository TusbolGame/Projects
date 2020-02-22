package com.liveitandroid.liveit.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VodInfoPojo {
    @SerializedName("cast")
    @Expose
    private String cast;
    @SerializedName("director")
    @Expose
    private String director;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;
    @SerializedName("movie_image")
    @Expose
    private String movieImage;
    @SerializedName("plot")
    @Expose
    private String plot;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("releasedate")
    @Expose
    private String releasedate;
    @SerializedName("youtube_trailer")
    @Expose
    private String youtube_trailer;

    public String getImdbId() {
        return this.imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
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

    public String getMovieImage() {
        return this.movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlot() {
        return this.plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getCast() {
        return this.cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getRating() {
        return this.rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDirector() {
        return this.director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getReleasedate() {
        return this.releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
