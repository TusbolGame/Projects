package com.liveitandroid.liveit.model.callback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UserLoginInfoCallback {
    @SerializedName("active_cons")
    @Expose
    private String activeCons;
    @SerializedName("allowed_output_formats")
    @Expose
    private List<String> allowedOutputFormats = null;
    @SerializedName("auth")
    @Expose
    private Integer auth;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("exp_date")
    @Expose
    private String expDate;
    @SerializedName("is_trial")
    @Expose
    private String isTrial;
    @SerializedName("max_connections")
    @Expose
    private String maxConnections;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("username")
    @Expose
    private String username;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getAuth() {
        return this.auth;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpDate() {
        return this.expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getIsTrial() {
        return this.isTrial;
    }

    public void setIsTrial(String isTrial) {
        this.isTrial = isTrial;
    }

    public String getActiveCons() {
        return this.activeCons;
    }

    public void setActiveCons(String activeCons) {
        this.activeCons = activeCons;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getMaxConnections() {
        return this.maxConnections;
    }

    public void setMaxConnections(String maxConnections) {
        this.maxConnections = maxConnections;
    }

    public List<String> getAllowedOutputFormats() {
        return this.allowedOutputFormats;
    }

    public void setAllowedOutputFormats(List<String> allowedOutputFormats) {
        this.allowedOutputFormats = allowedOutputFormats;
    }
}
