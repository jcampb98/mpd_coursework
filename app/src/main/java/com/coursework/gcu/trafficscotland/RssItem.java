package com.coursework.gcu.trafficscotland;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Name: Joshua Campbell
 * Matric No: S2024472
 */

public class RssItem extends AsyncTask<String, String, ArrayList<ParseClass>>{
    private WeakReference<TextView> mTextView;
    private WeakReference<RecyclerView> mRecyclerView;
    private WeakReference<ProgressBar> mProgressBar;
    private WeakReference<TextView> mErrorMessage;

    private ArrayList<ParseClass> mParseClass;

    private String result = "";
    private String userInput = "";
    private String handlerSelection = "";
    private RssDateConverter rssDateConverter; // used to parse userInputDate to Calendar object

    RssItem(TextView textView) {
        mTextView = new WeakReference<>(textView);
    }

    RssItem(RecyclerView recyclerView, ProgressBar progressBar, ArrayList<ParseClass> parseItemList, TextView errorMessage) {
        mRecyclerView = new WeakReference<>(recyclerView);
        mProgressBar = new WeakReference<>(progressBar);
        mErrorMessage = new WeakReference<>(errorMessage);
        rssDateConverter = new RssDateConverter();
        this.mParseClass = parseItemList;
    }

    @Override
    protected ArrayList doInBackground(String... strings) {
        URL aurl;
        URLConnection yc;
        BufferedReader in = null;
        String inputLine = "";
        // if the strings has a second value of the date or road from the user
        if (strings.length > 1) {
            String date = strings[1]; // Date input from user
            userInput = strings[1]; // pass the user input date to variable so onPostExecute can access it
            handlerSelection = strings[2];
            Log.v("DATE FROM USER", date);
        } else {
            // clear the userInput string incase there was a previous entry
            userInput = "";
        }

        try {

            Log.e("MyTag","in try");
            aurl = new URL(strings[0]); // first string from input arg
            yc = aurl.openConnection();
            in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            //
            // Throw away the first 2 header lines before parsing
            //

            while ((inputLine = in.readLine()) != null) {
                result = result + inputLine;
                Log.e("MyTag",inputLine);

            }
            in.close();
        }
        catch (IOException ae)
        {
            Log.e("MyTag", ae.toString());
        }

        ArrayList<ParseClass> newTrafficData = new ArrayList<ParseClass>(); // assigned this in contructor
        XmlPullParserHandler trafficXMLParser = new XmlPullParserHandler();
        try {
            newTrafficData = trafficXMLParser.parse(result);
            // if there is a present input from the user, filter the data
            if (!userInput.isEmpty()) {
                // select the right filter method from accessing the handlerSelection value
                if (handlerSelection.equals("d")) {
                    newTrafficData = filterByDate(rssDateConverter.convertStringToDate(userInput), newTrafficData);
                } else if (handlerSelection.equals("r")) {
                    newTrafficData = filterByRoad(userInput, newTrafficData);
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newTrafficData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressBar.get().setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(ArrayList newTrafficData) {
        super.onPostExecute(newTrafficData);
        String parsedString = "";

        this.mParseClass.clear();
        this.mParseClass.addAll(newTrafficData);

        mRecyclerView.get().getAdapter().notifyDataSetChanged();

        mProgressBar.get().setVisibility(View.GONE);
        // display an error message if there is no results
        if (mParseClass.isEmpty()) {
            mErrorMessage.get().setVisibility(View.VISIBLE);
        } else {
            mErrorMessage.get().setVisibility(View.GONE);
        }
    }

    public ArrayList<ParseClass> filterByDate(Calendar date, ArrayList list) {
        ArrayList<ParseClass> filteredTrafficDataList = new ArrayList<>();
        Iterator<ParseClass> dataModelIterator = list.iterator();
        while (dataModelIterator.hasNext()) {
            ParseClass trafficParseClass = dataModelIterator.next();
            // if the date is in the range of the trafficData's start and end date's range
            if (date.compareTo(trafficParseClass.getStartDate()) >= 0 &&
                    date.compareTo(trafficParseClass.getEndDate()) <= 0) {
                filteredTrafficDataList.add(trafficParseClass);
            }
        }
        return filteredTrafficDataList;
    }

    public ArrayList<ParseClass> filterByRoad(String road, ArrayList list) {
        ArrayList<ParseClass> filteredTrafficDataList = new ArrayList<>();
        Iterator<ParseClass> dataModelIterator = list.iterator();
        while (dataModelIterator.hasNext()) {
            ParseClass trafficDataModel = dataModelIterator.next();
            try {
                if (trafficDataModel.getTitle().contains(road)) {
                    filteredTrafficDataList.add(trafficDataModel);
                }
            } catch (Exception e) {

            }
        }
        return filteredTrafficDataList;
    }
}
