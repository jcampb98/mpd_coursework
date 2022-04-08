package com.coursework.gcu.trafficscotland;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Joshua Campbell S2024472
 */

public class RssAdapter extends ArrayAdapter<ParseClass> {
    Date filterDate = new Date();
    ArrayList<ParseClass> items = new ArrayList<ParseClass>();
    public RssAdapter(Context context, ArrayList<ParseClass> items){
        super(context,0,items);

        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        ParseClass parseItem = getItem(pos);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.datalist_item, parent, false);

        TextView itemTitle = convertView.findViewById(R.id.itemTitle);
        TextView itemDescription = convertView.findViewById(R.id.itemDescription);
        TextView itemStartDate = convertView.findViewById(R.id.itemStartDate);
        TextView itemEndDate = convertView.findViewById(R.id.itemEndDate);

        itemTitle.setText(parseItem.getTitle());
        itemDescription.setText(parseItem.getDescription());

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        itemStartDate.setText("Start Date: " + format.format(parseItem.getStartDate()));
        itemEndDate.setText("End Date: " + format.format(parseItem.getEndDate()));

        return convertView;
    }

    public void refresh(ArrayList<ParseClass> items)
    {
        this.items = items;
        notifyDataSetChanged();
    }
}
