package com.feed.manage;

public class Article {
    String articleID;
    String articleTitle;
    String articleContent;
    String attachID;
    String userEmail;
    String location;

    public String getLocation(){
        return location;
    }

    public void setLocation(String mLocation){
        this.location = mLocation;
    }
    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public String getAttachID() {
        return attachID;
    }

    public void setAttachID(String attachID) {
        this.attachID = attachID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Article(){

    }
    public Article(String articleID, String articleTitle, String articleContent, String attachID, String userEmail, String location) {
        this.articleID = articleID;
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.attachID = attachID;
        this.userEmail = userEmail;
        this.location = location;
    }
}
