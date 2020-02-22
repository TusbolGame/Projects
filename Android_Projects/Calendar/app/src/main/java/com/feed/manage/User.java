package com.feed.manage;

public class User {
    String userID;
    String userEmail;
    String userLocation;
    String userTime;
    String userPic;


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }
    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

    public User(String userID, String userEmail, String userLocation, String userTime, String userPic) {
        this.userID = userID;
        this.userEmail = userEmail;
        this.userLocation = userLocation;
        this.userPic = userPic;
        this.userTime = userTime;
    }
    public User(){

    }
}
