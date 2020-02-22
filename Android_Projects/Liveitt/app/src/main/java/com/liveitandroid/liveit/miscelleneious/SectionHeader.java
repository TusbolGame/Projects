package com.liveitandroid.liveit.miscelleneious;

import android.support.v7.widget.RecyclerView;

import com.liveitandroid.liveit.view.adapter.SubCategoriesChildAdapter;
import com.intrusoft.sectionedrecyclerview.Section;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.view.adapter.SubCategoriesChildAdapter;
import java.util.ArrayList;
import java.util.List;

public class SectionHeader implements Section<Child> {
    ArrayList<LiveStreamsDBModel> channelAvailable;
    RecyclerView childList;
    private List<Child> list;
    String sectionText;
    private SubCategoriesChildAdapter subCategoriesChildAdapter;

    public SectionHeader(RecyclerView childList, String sectionText, ArrayList<LiveStreamsDBModel> channelAvailable, SubCategoriesChildAdapter subCategoriesChildAdapter, List<Child> list) {
        this.childList = childList;
        this.sectionText = sectionText;
        this.channelAvailable = channelAvailable;
        this.subCategoriesChildAdapter = subCategoriesChildAdapter;
        this.list = list;
    }

    public String getSectionText() {
        return this.sectionText;
    }

    public List<Child> getChildItems() {
        return this.list;
    }

    public List<LiveStreamsDBModel> channelSelcted() {
        return this.channelAvailable;
    }
}
