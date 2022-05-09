package com.musiquitaapp.models;

import java.io.Serializable;
import java.util.List;

public class YoutubeQuery implements Serializable {
    PageInfo pageInfo;
    List<Items> items;

    public YoutubeQuery() {
    }

    public YoutubeQuery(PageInfo pageInfo, List<Items> items) {
        this.pageInfo = pageInfo;
        this.items = items;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }
}
