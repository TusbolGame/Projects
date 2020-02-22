package com.liveitandroid.liveit.model;

import android.util.Log;

import com.liveitandroid.liveit.miscelleneious.common.AppConst;

import java.util.ArrayList;
import java.util.Comparator;

public class LiveStreamsDBModel extends ArrayList<LiveStreamsDBModel> {
    private String added;
    private String categoryId;
    private String categoryName;
    private String contaiinerExtension;
    private String customSid;
    private String directSource;
    private String epgChannelId;
    private String epgDesc;
    private Long epgEndDate;
    private int epgPercentage;
    private Long epgStartDate;
    private String epgTitle;
    private int idAuto;
    private String live;
    private String name;
    private String num;
    private String seriesNo;
    private String streamIcon;
    private String streamId;
    private String streamType;
    private String tvArchive;
    private String tvArchiveDuration;
    private String typeName;
    private String youtube_trailer;

    public LiveStreamsDBModel() {

    }

    public String getYoutube() {
        return this.youtube_trailer;
    }

    public void setYoutube(String imdbId) {
        if (!(imdbId.equals("") || imdbId.equals(null))) {
            this.youtube_trailer = imdbId;
        } else {
            this.youtube_trailer = "";
        }
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSeriesNo() {
        return this.seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getLive() {
        return this.live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public String getContaiinerExtension() {
        return this.contaiinerExtension;
    }

    public void setContaiinerExtension(String contaiinerExtension) {
        this.contaiinerExtension = contaiinerExtension;
    }

    public LiveStreamsDBModel(String num, String name, String streamType, String streamId, String streamIcon, String epgChannelId, String added, String categoryId, String customSid, String tvArchive, String directSource, String tvArchiveDuration, String typeName, String categoryName, String seriesNo, String live, String contaiinerExtension, int epgPercentage, Long epgStartDate, Long epgEndDate, String epgTitle, String epgDesc, String youtube) {
        this.num = num;
        this.name = name;
        this.streamType = streamType;
        this.streamId = streamId;
        this.streamIcon = streamIcon;
        this.epgChannelId = epgChannelId;
        this.added = added;
        this.categoryId = categoryId;
        this.customSid = customSid;
        this.tvArchive = tvArchive;
        this.directSource = directSource;
        this.tvArchiveDuration = tvArchiveDuration;
        this.typeName = typeName;
        this.categoryName = categoryName;
        this.seriesNo = seriesNo;
        this.live = live;
        this.contaiinerExtension = contaiinerExtension;
        this.epgPercentage = epgPercentage;
        this.epgStartDate = epgStartDate;
        this.epgEndDate = epgEndDate;
        this.epgTitle = epgTitle;
        this.epgDesc = epgDesc;
        this.youtube_trailer = youtube;
    }


    public void details() {
        Log.d("TmpLogs",
                this.num + ", name = " +
                        this.name + ", streamType= " +
                        this.streamType + ", streamId = " +
                        this.streamId + ", streamIcon = " +
                        this.streamIcon + ", epgChannelId = " +
                        this.epgChannelId + ", added = " +
                        this.added + ", categoryId = " +
                        this.categoryId + ", customSid = " +
                        this.customSid + ", tvArchive = " +
                        this.tvArchive + ", directSource = " +
                        this.directSource + ", tvArchiveDuration = " +
                        this.tvArchiveDuration + ", typeName = " +
                        this.typeName + ", categoryName = " +
                        this.categoryName + ", seriesNo = " +
                        this.seriesNo + ", live = " +
                        this.live + ", contaiinerExtension = " +
                        this.contaiinerExtension + ", epgPercentage = " +
                        this.epgPercentage + ", epgStartDate = " +
                        this.epgStartDate + ", epgEndDate = " +
                        this.epgEndDate + ", epgTitle = " +
                        this.epgTitle + ", epgDesc = " +
                        this.epgDesc + ", youtube_trailer = " +
                        this.youtube_trailer);
    }

    public int getEpgPercentage() {
        return this.epgPercentage;
    }

    public Long getEpgStartDate() {
        return this.epgStartDate;
    }

    public Long getEpgEndDate() {
        return this.epgEndDate;
    }

    public String getEpgTitle() {
        return this.epgTitle;
    }

    public String getEpgDesc() {
        return this.epgDesc;
    }

    public void setEpgPercentage(int epgPercentage) {
        this.epgPercentage = epgPercentage;
    }

    public void setEpgStartDate(Long epgStartDate) {
        this.epgStartDate = epgStartDate;
    }

    public void setEpgEndDate(Long epgEndDate) {
        this.epgEndDate = epgEndDate;
    }

    public void setEpgTitle(String epgTitle) {
        this.epgTitle = epgTitle;
    }

    public void setEpgDesc(String epgDesc) {
        this.epgDesc = epgDesc;
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

    public String getStreamId() {
        return this.streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getStreamIcon() {
        return this.streamIcon;
    }

    public void setStreamIcon(String streamIcon) {
        this.streamIcon = streamIcon;
    }

    public String getEpgChannelId() {
        return this.epgChannelId;
    }

    public void setEpgChannelId(String epgChannelId) {
        this.epgChannelId = epgChannelId;
    }

    public String getAdded() {
        return this.added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCustomSid() {
        return this.customSid;
    }

    public void setCustomSid(String customSid) {
        this.customSid = customSid;
    }

    public String getTvArchive() {
        return this.tvArchive;
    }

    public void setTvArchive(String tvArchive) {
        this.tvArchive = tvArchive;
    }

    public String getDirectSource() {
        return this.directSource;
    }

    public void setDirectSource(String directSource) {
        this.directSource = directSource;
    }

    public String getTvArchiveDuration() {
        return this.tvArchiveDuration;
    }

    public void setTvArchiveDuration(String tvArchiveDuration) {
        this.tvArchiveDuration = tvArchiveDuration;
    }

    /*Comparator for sorting the list by roll no*/
    public static Comparator<LiveStreamsDBModel> StuRollno = new Comparator<LiveStreamsDBModel>() {

        public int compare(LiveStreamsDBModel s1, LiveStreamsDBModel s2) {

            int rollno1 = new Integer(s1.getStreamId());
            int rollno2 = new Integer(s2.getStreamId());

            return rollno1 - rollno2;
        }
    };

    /*Comparator for sorting the list by roll no*/
    public static Comparator<LiveStreamsDBModel> StuRollnoNum = new Comparator<LiveStreamsDBModel>() {

        public int compare(LiveStreamsDBModel s1, LiveStreamsDBModel s2) {

            int rollno1 = new Integer(s1.getNum());
            int rollno2 = new Integer(s2.getNum());

            return rollno1 - rollno2;
        }
    };

    /*Comparator for sorting the list by roll no*/
    public static Comparator<LiveStreamsDBModel> StuRollnoNome = new Comparator<LiveStreamsDBModel>() {
        public int compare(LiveStreamsDBModel s1, LiveStreamsDBModel s2) {
            //ascending order
            return s1.getName().compareTo(s2.getName());

            //descending order
            //return s2.getName().compareTo(s1.getName());
        }
    };
}
