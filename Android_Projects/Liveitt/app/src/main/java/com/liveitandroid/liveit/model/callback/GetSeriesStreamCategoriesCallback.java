package com.liveitandroid.liveit.model.callback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSeriesStreamCategoriesCallback {
    @SerializedName("category_id")
    @Expose
    public String categoryId;
    @SerializedName("category_name")
    @Expose
    public String categoryName;

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
