package com.coursework.gcu.trafficscotland;

import android.os.Parcel;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Name: Gavin Ross
 * Matric No: S1821951
 */

public class XmlPullParserHandler {
    private static final String ns = null;
    ArrayList<ParseClass> trafficDataList = new ArrayList<>();
    private static RssDateConverter rssDate;

    public XmlPullParserHandler() {
        rssDate = new RssDateConverter();
    }

    public ArrayList<ParseClass> parse(String string) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        String parsedResult = "";

        xpp.setInput(new StringReader (string));
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
            } else if(eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals("item")) {
                    ParseClass trafficDataObj = new ParseClass();
                    eventType = xpp.nextTag();
                    if (xpp.getName().equals("title")) {
                        eventType = xpp.next();
                        trafficDataObj.setTitle(xpp.getText());
                        Log.e("MyTag", xpp.getText());
                        eventType = xpp.nextTag(); // </title> end tag
                        eventType = xpp.nextTag(); // <description>
                        Log.e("MyTag", xpp.getName());
                    }
                    if (xpp.getName().equals("description")) {
                        eventType = xpp.next();
                        trafficDataObj.setDescription(getDescription(xpp.getText()));// use the getDesc to extract
                        // extract the long versions of start and end date from description
                        // if it's the current incidents feed, theres no dates so it returns null
                        String[] startAndEndDates = getDates(xpp.getText());
                        if (startAndEndDates != null) {
                            // convert each one to Calendar object and add to trafficDataObj
                            trafficDataObj.setStartDate(rssDate.convertRssDateToObj(startAndEndDates[0]));
                            trafficDataObj.setEndDate(rssDate.convertRssDateToObj(startAndEndDates[1]));
                            // Convert long date to short version and add to trafficDataObj (String)
                            trafficDataObj.setStartDateAsString(rssDate.convertLongDateToShort(startAndEndDates[0]));
                            trafficDataObj.setEndDateAsString(rssDate.convertLongDateToShort(startAndEndDates[1]));
                            // set the length of time the roadworks will last
                            trafficDataObj.setRoadworksLength(rssDate.numberOfDays(trafficDataObj.getStartDate(), trafficDataObj.getEndDate()));
                        }
                        //Log.e("startDate", trafficDataObj.getStartDate());
                        //Log.e("endDate", trafficDataObj.getEndDate());

                        Log.e("MyTag", xpp.getText());
                        eventType = xpp.nextTag(); // </title> end tag
                        eventType = xpp.nextTag(); // <... next tag

                    }
                    if (xpp.getName().equals("point")) {
                        eventType = xpp.next();
                        trafficDataObj.setGeorss(xpp.getText());
                        Log.e("MyTag", xpp.getText());
                        eventType = xpp.nextTag(); // </title> end tag
                        eventType = xpp.nextTag(); // <description>
                    }
                    eventType = xpp.nextTag(); // </title> end tag
                    eventType = xpp.nextTag(); // <description>
                    eventType = xpp.nextTag(); // </title> end tag
                    eventType = xpp.nextTag(); // <description>
                    if (xpp.getName().equals("pubDate")) {
                        eventType = xpp.next();
                        trafficDataObj.setPubDate(rssDate.convertLongDateToShort(xpp.getText()));
                        Log.v("MyTag", xpp.getText());
                        eventType = xpp.nextTag(); // </title> end tag
                        eventType = xpp.nextTag(); // <description>
                    }
                    trafficDataList.add(trafficDataObj);
                }
            }
            eventType = xpp.next();
        }
        System.out.println("End document");
        return trafficDataList;
    }

    public String[] getDates(String date) throws StringIndexOutOfBoundsException {
        if (date.indexOf("Start Date: ") == -1 || date.indexOf("End Date: ") == -1) {
            return null;
        }

        else {
            String startDateIndex = date.substring(date.indexOf("Start Date: "), date.indexOf(':'));
            String data1 = date.substring(startDateIndex.length() + 2, date.indexOf('<'));
            String leftOverString = date.substring(date.indexOf('>'));

            String endDateIndex = leftOverString.substring(leftOverString.indexOf("End Date: "), date.indexOf(':'));
            String data2 = "";
            if (date.indexOf("<br />Delay") != -1) {
                data2 = leftOverString.substring(endDateIndex.length() + 2, leftOverString.indexOf('<'));
            } else {
                data2 = leftOverString.substring(endDateIndex.length() + 2);
            }
            String[] results = new String[2];
            results[0] = data1;
            results[1] = data2;
            return results;
        }

    }

    public String getDescription(String desc) {
        int result = desc.lastIndexOf("<br />");
        if (result == -1) {
            return desc;
        } else {
            return desc.substring(result+6, desc.length());
        }
    }
}
