package com.coursework.gcu.trafficscotland;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Name: Joshua Campbell
 * Matric No: S2024472
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.RssViewDataHolder>{
    private ArrayList<ParseClass> mTrafficDataList;
    private LayoutInflater mInflater;
    private Context context;

    public ItemListAdapter(Context context, ArrayList<ParseClass> mTrafficDataList) {
        mInflater = LayoutInflater.from(context);
        this.mTrafficDataList = mTrafficDataList;
        this.context = context;
    }

    class RssViewDataHolder extends RecyclerView.ViewHolder {
        public TextView titleItemView;
        public ImageView iconImageView;
        public TextView startDateItemView;
        public TextView endDateItemView;
        final ItemListAdapter mAdapter;

        public RssViewDataHolder(@NonNull View itemView, ItemListAdapter adapter) {
            super(itemView);
            titleItemView = itemView.findViewById(R.id.title);
            iconImageView = itemView.findViewById(R.id.imageView);
            iconImageView.setImageResource(R.drawable.ic_baseline_place_24);
            startDateItemView = itemView.findViewById(R.id.start_date);
            endDateItemView = itemView.findViewById(R.id.end_date);
            this.mAdapter = adapter;
        }
    }

    @Override
    public ItemListAdapter.RssViewDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.activity_date_rss,
                parent, false);
        return new RssViewDataHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemListAdapter.RssViewDataHolder holder, int position) {
        final ParseClass mCurrent = mTrafficDataList.get(position);
        holder.titleItemView.setText(mCurrent.getTitle());
        holder.startDateItemView.setText(mCurrent.getStartDateAsString());
        holder.endDateItemView.setText(mCurrent.getEndDateAsString());
        setTrafficIcon(holder.iconImageView, mCurrent);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("onClick", "traffic data list item");
                Intent intent = new Intent(context, RssDataActivity.class);
                intent.putExtra("TRAFFIC_DATA", mCurrent);
                context.startActivity(intent);
            }
        });
    }

    public void setTrafficIcon(ImageView view, ParseClass tdm) {
        if (tdm.getRoadworksLength() < 2) {
            view.setImageResource(R.drawable.ic_baseline_place_24);
        } else if (tdm.getRoadworksLength() < 8) {
            view.setImageResource(R.drawable.ic_baseline_report_problem_24);
        }
        else {
            view.setImageResource(R.drawable.ic_baseline_pin_drop_24);
        }
    }

    @Override
    public int getItemCount() {
        return mTrafficDataList.size();
    }
}
