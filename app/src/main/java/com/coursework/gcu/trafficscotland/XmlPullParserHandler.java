package com.coursework.gcu.trafficscotland;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Name: Joshua Campbell
 * Matric No: S2024472
 */

//Some code was adapted from https://www.javatpoint.com/android-XMLPullParser-tutorial
public class XmlPullParserHandler {
    private String urlSource;
    private ArrayList<ParseClass> rssItemList = new ArrayList<>();
    private RssItem rssItemData;

    public XmlPullParserHandler(String urlSource, RssItem rssItemData, ParseComplete callback) {
        this.urlSource = urlSource;
        this.rssItemData = rssItemData;

        new ProcessInBackground(callback).execute();
    }

    //Get stream of data from url
    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();

        } catch (IOException ex) {
            return null;
        }
    }

    public class ProcessInBackground extends AsyncTask<Integer, Void, Exception> {
        Exception exception = null;
        private ParseComplete completeListener;

        //Initialise the listener to the listener passed in
        public ProcessInBackground(ParseComplete listener) {
            this.completeListener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("MyTag", "Loading RSS feed");
        }

        //this code was adapted from https://www.youtube.com/watch?v=i7aGM8uy2T0&ab_channel=MohamedShehab
        @Override
        protected Exception doInBackground(Integer... integers) {
            //Variables to store properties of the current rssItem
            String rssItemTitle = "";
            String rssItemDescription = "";
            float rssItemLat = 0;
            float rssItemLng = 0;
            Date rssItemDate = new Date();
            Date rssItemStartDate = new Date();
            Date rssItemEndDate = new Date();

            try {
                URL url = new URL(urlSource);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(getInputStream(url), "UTF_8");

                //Saves whether the reader is inside an <item> tag
                boolean readingItem = false;
                //Get the type of current event e.g. START_TAG or END_TAG ... etc
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {

                    //If eventType is start of a tag
                    if (eventType == XmlPullParser.START_TAG) {

                        //Check tag name to be an item
                        if (xpp.getName().equals("item")) {
                            //Reading an item is now true
                            readingItem = true;

                        }

                        //Check tag name to be title, and ensure readingItem true as also title tag outside item.
                        if (xpp.getName().equals("title") && readingItem) {
                            rssItemTitle = xpp.nextText();
                        }

                        //Check tag name to be title, and ensure readingItem true as also title tag outside item.
                        if (xpp.getName().equals("description") && readingItem) {

                            //Description contains various pieces of valuable information.
                            //From this, we can extract: Start date, end date, and the rest of the description.
                            //Each piece of info is seperated by '<br />'.

                            String[] dateData = xpp.nextText().split("<br />");

                            //Date format holder
                            DateFormat format = new SimpleDateFormat("EE, dd MMMM yyyy - HH:mm", Locale.ENGLISH);

                            try {

                                for (String data : dateData) {
                                    if (data.startsWith("Start Date: ")) {
                                        String temp = data.replace("Start Date: ", "");
                                        rssItemStartDate = format.parse(temp);
                                    } else if (data.startsWith("End Date:")) {
                                        String temp = data.replace("End Date: ", "");
                                        rssItemEndDate = format.parse(temp);
                                    } else {
                                        rssItemDescription = data;
                                    }

                                }

                            } catch (ParseException e) {
                                Log.e("MyTag", "Could not parse date " + xpp.nextText() + " from XML on line " + xpp.getLineNumber());
                            }
                        }

                        //Check tag name to be title, no need to ensure readingItem as there is no georss:point tags outside <item>
                        if (xpp.getName().equals("georss:point")) {
                            String lngLatTemp = xpp.nextText();
                            String[] lngLatSplit = lngLatTemp.split(" ");

                            rssItemLat = Float.parseFloat(lngLatSplit[0]);
                            rssItemLng = Float.parseFloat(lngLatSplit[1]);
                        }

                        //Check tag name to be pubDate, no need to ensure readingItem as there is no pubDate tags outside <item>
                        if (xpp.getName().equals("pubDate")) {
                            //Parse the date from a string to a java Date object.
                            try {
                                String string = xpp.nextText();
                                DateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                                rssItemDate = format.parse(string);
                            } catch (ParseException e) {
                                Log.e("MyTag", "Could not parse date " + xpp.nextText() + " from XML on line " + xpp.getLineNumber());
                                Log.e("error:", e.getMessage());
                            }

                            //As it is the last tag of an item, create the parseRss from the data collected
                            ParseClass parseRss = new ParseClass(rssItemData, rssItemTitle, rssItemDescription, rssItemLat, rssItemLng, rssItemDate, rssItemStartDate, rssItemEndDate);
                            //Add item to list
                            rssItemList.add(parseRss);
                        }

                    } else if (eventType == XmlPullParser.END_TAG) {
                        readingItem = false;
                    }

                    //Increment to next
                    eventType = xpp.next();
                }


            } catch (MalformedURLException ex) {
                Log.e("MyTag", "Invalid URL");
                exception = ex;
            } catch (XmlPullParserException ex) {
                Log.e("MyTag", "Could not parse XML");
                exception = ex;
            } catch (IOException ex) {
                Log.e("MyTag", "IOException");
                exception = ex;
            }
            return exception;
        }

        @Override
        protected void onPostExecute(Exception e) {
            super.onPostExecute(e);

            //Loaded XML
            Log.i("MyTag", "RSS feed loaded");

            completeListener.onParseComplete(rssItemList);

        }
    }
}
