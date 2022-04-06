package com.coursework.gcu.trafficscotland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * Name: Joshua Campbell
 * Matric No: S2024472
 */

public class MainActivity extends AppCompatActivity {

    private RadioButton dateSelector;
    private RadioButton roadSelector;
    private RadioButton currentIncidentsSelector;
    private RadioButton currentRoadworksSelector;
    private RadioButton plannedRoadworksSelector;
    private RadioButton noneSelector;
    private TextView mErrorDisplay;
    private Button getFeed;
    private TextView userInput;
    private ArrayList<ParseClass> mParseClass = new ArrayList<ParseClass>();
    private RecyclerView mRecyclerView;
    private ItemListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;
    private Parcelable mListState;

    String currentRoadworkURL = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    String plannedRoadworkURL = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    String currentIncidentURL = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String dateAsString = "";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("user_input", userInput.getText().toString());

        if (noneSelector.isChecked()) {
            outState.putBoolean("noneState", true);
        } else {
            outState.putBoolean("noneState", false);
        }

        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable("traffic_data_state", mListState);
        outState.putParcelableArrayList("traffic_data", mParseClass);
        Log.v("SAVE", "onSaveInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Bundle outState = new Bundle();
        outState.putString("user_input", userInput.getText().toString());

        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable("traffic_data_state", mListState);
        outState.putParcelableArrayList("traffic_data", mParseClass);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag", "App is created");

        getFeed = findViewById(R.id.get_feed_btn);
        dateSelector = findViewById(R.id.date_radio_btn);
        roadSelector = findViewById(R.id.road_radio_btn);
        noneSelector = findViewById(R.id.none_radio_button);
        mErrorDisplay = findViewById(R.id.error_display);
        currentRoadworksSelector = findViewById(R.id.current_roadworks_radio_btn);
        plannedRoadworksSelector = findViewById(R.id.planned_roadworks_radio_btn);
        currentIncidentsSelector = findViewById(R.id.current_incedents_radio_btn);
        mProgressBar = findViewById(R.id.progressBar);
        userInput = findViewById(R.id.user_input);

        if (savedInstanceState != null) {
            // This is required because the default state of user input is disabled on Activity
            // creation, so when a rotation happens with state the None radioButton not selected,
            // it keeps the userInput enabled with the users text input.
            boolean noneState = savedInstanceState.getBoolean("noneState");
            if (noneState) {
                userInput.setEnabled(false);
            } else {
                userInput.setEnabled(true);
            }

            // This is to collect the parcelized traffic data in the arrayList and set it up
            // before the activity is recreated
            mListState = savedInstanceState.getParcelable("traffic_data_state");
            mParseClass = savedInstanceState.getParcelableArrayList("traffic_data");
            //mRecyclerView.getAdapter().notifyDataSetChanged();
        }

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new ItemListAdapter(this, mParseClass);

        // Give the RecyclerView a default layout manager.
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateAsString = Integer.toString(dayOfMonth) + "/" + Integer.toString((monthOfYear + 1)) + "/" + Integer.toString(year);
                userInput.setText(dateAsString);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        getFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectOption1 = ""; // none as the initial selection
                String selectOption2 = currentRoadworkURL; // set this as initial selection
                String handlerSelection = ""; // set to either d or r for date or road
                if (dateSelector.isChecked()) {
                    selectOption1 = userInput.getText().toString();
                    handlerSelection = "d";
                    //new RssFeed(mRecyclerView).execute(currentRoadworksUrl, dateInput.getText().toString());
                } else if (roadSelector.isChecked()) {
                    selectOption1 = userInput.getText().toString();
                    handlerSelection = "r";
                } else if (noneSelector.isChecked()) {
                    selectOption1 = ""; // reset it to nothing incase the buffer has previous input
                }
                // Second check box selections for roadworks selection
                if (currentRoadworksSelector.isChecked()) {
                    selectOption2 = currentRoadworkURL;
                    //selectOption2 = new Thread(new Task(currentRoadworkURL.start()));
                } else if (plannedRoadworksSelector.isChecked()) {
                    selectOption2 = plannedRoadworkURL;
                    //selectOption2 = new Thread(new Task(plannedRoadworkURL.start()));
                } else if (currentIncidentsSelector.isChecked()) {
                    selectOption2 = currentIncidentURL;
                    //selectOption2 = new Thread(new Task(currentIncidentURL.start()));
                }

                // check if an options has been selected for selectOption1
                if (selectOption1.isEmpty()) {
                    new RssItem(mRecyclerView, mProgressBar, mParseClass, mErrorDisplay).execute(selectOption2);
                } else {
                    new RssItem(mRecyclerView, mProgressBar, mParseClass, mErrorDisplay).execute(selectOption2, selectOption1, handlerSelection);
                }

                // close the keypad on button presses
                userInput.clearFocus();
                closeKeyboard(true);
            }
        });
        dateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTime.show();
                userInput.setEnabled(true);
            }
        });
        noneSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInput.setText("");
                userInput.setEnabled(false); // Disable user input when none is selected
            }
        });
        roadSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInput.setText("");
                userInput.setEnabled(true);
                userInput.setShowSoftInputOnFocus(true);
                //closeKeyboard(false);
            }
        });
    }

    private void closeKeyboard(boolean b) {
        View view = this.getCurrentFocus();
        if (b) {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }
}