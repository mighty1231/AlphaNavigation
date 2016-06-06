package com.restaurant.alpha.alphanavigation;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skp.Tmap.TMapPoint;

import java.util.ArrayList;

/**
 * Created by HyunhoHa on 2016-06-05.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    public static int maxSize = 10;
    private Context context;
    private ArrayList<String> myData;
    private ArrayList<TMapPoint> historyLocation;

    // Provide a suitable constructor (depends on the kind of dataset)
    public HistoryAdapter(Context context) {
        this.context = context;
        historyLocation = new ArrayList<TMapPoint>();
        myData = new ArrayList<String>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_recycler_item_view, parent, false);

        // create ViewHolder
        ViewHolder vh = new ViewHolder(itemLayoutView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your data at this position
        // - replace the contents of the view with that element
        holder.myTextView.setText(myData.get(position));
        holder.addToDestinationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Do you want set this location in destination?");
                alertBuilder.setMessage("You will change destination");

                alertBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((BasicSettingActivity)context).addDestinationInHistory(myData.get(position), historyLocation.get(position));
                            }
                        });
                alertBuilder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        });
        holder.addToStopImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Do you want add this location in stops?");
                alertBuilder.setMessage("You will add stops");

                alertBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((BasicSettingActivity)context).addStopInHistory(myData.get(position), historyLocation.get(position));
                            }
                        });
                alertBuilder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        });
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView myTextView;
        public ImageView addToStopImageView;
        public ImageView addToDestinationImageView;
        public ViewHolder(View view) {
            super(view);
            myTextView = (TextView)view.findViewById(R.id.history_title);
            addToStopImageView = (ImageView)view.findViewById(R.id.add_to_stop);
            addToDestinationImageView = (ImageView)view.findViewById(R.id.add_to_destination);
        }
    }

    public void addLocation(double latitude, double longitude , String name) {
        this.historyLocation.add(0, new TMapPoint(latitude, longitude));
        this.myData.add(0, name);
        if (myData.size() > maxSize) {
            this.historyLocation.remove(maxSize);
            this.myData.remove(maxSize);
        }
        notifyDataSetChanged();
    }

    public void clearAllLocation () {
        this.historyLocation.clear();
        this.myData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }
}
