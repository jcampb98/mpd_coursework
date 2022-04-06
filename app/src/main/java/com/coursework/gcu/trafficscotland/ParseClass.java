package com.coursework.gcu.trafficscotland;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Name: Joshua Campbell
 * Matric No: S2024472
 */

public class ParseClass implements Parcelable {
    private String title;
    private String description;
    private String georss;
    private String pubDate;
    private Calendar startDate;
    private Calendar endDate;
    private String startDateAsString;
    private String endDateAsString;
    private long roadworksLength;

    protected ParseClass(Parcel in) {
        title = in.readString();
        description = in.readString();;
        georss = in.readString();
        pubDate = in.readString();
        startDateAsString = in.readString();
        endDateAsString = in.readString();
        roadworksLength = in.readLong();
    }

    public static final Creator<ParseClass> CREATOR = new Creator<ParseClass>() {
        @Override
        public ParseClass createFromParcel(Parcel in) {
            return new ParseClass(in);
        }

        @Override
        public ParseClass[] newArray(int size) {
            return new ParseClass[size];
        }
    };

    public ParseClass() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.georss);
        dest.writeString(this.pubDate);
        dest.writeString(this.startDateAsString);
        dest.writeString(this.endDateAsString);
        dest.writeLong(this.roadworksLength);
    }

    public long getRoadworksLength() {
        return roadworksLength;
    }

    public void setRoadworksLength(long roadworksLength) {
        this.roadworksLength = roadworksLength;
    }

    public String getStartDateAsString() {
        return startDateAsString;
    }

    public void setStartDateAsString(String startDateAsString) {
        this.startDateAsString = startDateAsString;
    }

    public String getEndDateAsString() {
        return endDateAsString;
    }

    public void setEndDateAsString(String endDateAsString) {
        this.endDateAsString = endDateAsString;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeorss() {
        return georss;
    }

    public void setGeorss(String georss) {
        this.georss = georss;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }
}
