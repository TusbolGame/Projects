package com.liveitandroid.liveit.model.callback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liveitandroid.liveit.model.pojo.EpgListingPojo;
import java.io.Serializable;
import java.util.List;

public class LiveStreamsEpgCallback implements Serializable {
    @SerializedName("epg_listings")
    @Expose
    private List<EpgListingPojo> epgListingPojos = null;

    public List<EpgListingPojo> getEpgListingPojos() {
        return this.epgListingPojos;
    }

    public void setEpgListingPojos(List<EpgListingPojo> epgListingPojos) {
        this.epgListingPojos = epgListingPojos;
    }
}
