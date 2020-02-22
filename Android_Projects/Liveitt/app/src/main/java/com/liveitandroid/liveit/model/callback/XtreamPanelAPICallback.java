package com.liveitandroid.liveit.model.callback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.liveitandroid.liveit.model.pojo.PanelAvailableChannelsPojo;
import com.liveitandroid.liveit.model.pojo.PanelCategoriesPojo;
import com.liveitandroid.liveit.model.pojo.PanelServerInfoPojo;
import com.liveitandroid.liveit.model.pojo.PanelUserInfoPojo;
import java.util.Map;

public class XtreamPanelAPICallback {
    @SerializedName("available_channels")
    @Expose
    public Map<String, PanelAvailableChannelsPojo> availableChannels;
    @SerializedName("categories")
    @Expose
    private PanelCategoriesPojo categories;
    @SerializedName("server_info")
    @Expose
    private PanelServerInfoPojo serverInfo;
    @SerializedName("user_info")
    @Expose
    private PanelUserInfoPojo userInfo;

    public PanelUserInfoPojo getUserInfo() {
        return this.userInfo;
    }

    public void setUserInfo(PanelUserInfoPojo userInfo) {
        this.userInfo = userInfo;
    }

    public PanelServerInfoPojo getServerInfo() {
        return this.serverInfo;
    }

    public void setServerInfo(PanelServerInfoPojo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public PanelCategoriesPojo getCategories() {
        return this.categories;
    }

    public void setCategories(PanelCategoriesPojo categories) {
        this.categories = categories;
    }

    public Map<String, PanelAvailableChannelsPojo> getAvailableChannels() {
        return this.availableChannels;
    }

    public void setAvailableChannels(PanelAvailableChannelsPojo availableChannels) {
        this.availableChannels = (Map) availableChannels;
    }
}
