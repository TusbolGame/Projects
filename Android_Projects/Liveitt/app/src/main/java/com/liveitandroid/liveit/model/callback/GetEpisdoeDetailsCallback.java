package com.liveitandroid.liveit.model.callback;

import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;

import java.io.Serializable;
import java.util.Comparator;

public class GetEpisdoeDetailsCallback implements Serializable {

    public static Comparator<GetEpisdoeDetailsCallback> episodeComparator = new Comparator<GetEpisdoeDetailsCallback>() {
        @Override
        public int compare(GetEpisdoeDetailsCallback s1, GetEpisdoeDetailsCallback s2) {
                    if (AppConst.SORT_EPISODES == AppConst.SORT_EPISODES_A_TO_Z) {
                return s1.getTitle().toUpperCase().compareTo(s2.getTitle().toUpperCase());
            }
            if (AppConst.SORT_EPISODES == AppConst.SORT_EPISODES_Z_TO_A) {
                return s2.getTitle().toUpperCase().compareTo(s1.getTitle().toUpperCase());
            } else if (AppConst.SORT_EPISODES != AppConst.SORT_EPISODES_LASTADDED) {
                return 0;
            } else {
                return s2.getAdded().toUpperCase().compareTo(s1.getAdded().toUpperCase());
            }
        }

//    public int compare(GetEpisdoeDetailsCallback s1, GetEpisdoeDetailsCallback s2) {

//    }

    };

    @SerializedName("added")
    @Expose
    public String added;
    @SerializedName("container_extension")
    @Expose
    public String containerExtension;
    @SerializedName("custom_sid")
    @Expose
    public String customSid;
    @SerializedName("direct_source")
    @Expose
    public String directSource;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("season")
    @Expose
    public Integer seasonNumber;
    @SerializedName("title")
    @Expose
    public String title;

    public Integer getSeasonNumber() {
        return this.seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContainerExtension() {
        return this.containerExtension;
    }

    public void setContainerExtension(String containerExtension) {
        this.containerExtension = containerExtension;
    }

    public String getCustomSid() {
        return this.customSid;
    }

    public void setCustomSid(String customSid) {
        this.customSid = customSid;
    }

    public String getAdded() {
        return this.added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getDirectSource() {
        return this.directSource;
    }

    public void setDirectSource(String directSource) {
        this.directSource = directSource;
    }


}
