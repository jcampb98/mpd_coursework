package com.coursework.gcu.trafficscotland;

import java.util.Date;

/**
 * Name: Joshua Campbell
 * Matric No: S2024472
 */

public class ParseClass {
    private RssItem rssItem;
    private String title;
    private String description;
    private float lat;
    private float lng;
    private Date pubDate;
    private Date startDate;
    private Date endDate;

    //this code was adapted from https://www.javatpoint.com/android-XMLPullParser-tutorial
    public ParseClass(RssItem rssItemType, String title, String description, float lat, float lng, Date pubDate, Date startDate, Date endDate) {
        this.rssItem = rssItemType;
        this.title = title;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.pubDate = pubDate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public Date getStartDate(){
        return startDate;
    }

    public Date getEndDate(){
        return endDate;
    }

    public RssItem getRssItemType() {
        return rssItem;
    }

    public void setRssItem(RssItem rssItemType) {
        this.rssItem = rssItemType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public void setStartDate(Date startDate){
        this.startDate = startDate;

    }

    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }
}
