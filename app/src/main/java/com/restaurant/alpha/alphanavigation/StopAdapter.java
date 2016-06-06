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
import android.widget.Toast;

import com.skp.Tmap.TMapPoint;

import java.util.ArrayList;

/**
 * Created by HyunhoHa on 2016-06-01.
 */
public class StopAdapter extends RecyclerView.Adapter<StopAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> myData;
    private ArrayList<TMapPoint> stopLocation;

    // Provide a suitable constructor (depends on the kind of dataset)
    public StopAdapter(Context context) {
        this.context = context;
        stopLocation = new ArrayList<TMapPoint>();
        myData = new ArrayList<String>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stop_recycler_item_view, parent, false);

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
        holder.myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Do you want to delete?");
                alertBuilder.setMessage("You will remove this data");

                alertBuilder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myData.remove(position);
                                stopLocation.remove(position);
                                notifyDataSetChanged();
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
        holder.myImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Do you want to delete?");
                alertBuilder.setMessage("You will remove this data");

                alertBuilder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myData.remove(position);
                                stopLocation.remove(position);
                                notifyDataSetChanged();
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
        public ImageView myImageView;
        public ViewHolder(View view) {
            super(view);
            myImageView = (ImageView)view.findViewById(R.id.minus);
            myTextView = (TextView)view.findViewById(R.id.title);
        }
    }

    public void addLocation(double[] position, String name) {
        if(myData.size() >= 3) {
            Toast.makeText(context, "Cannot add more than 3 stops", Toast.LENGTH_SHORT).show();
        }
        else {
            this.stopLocation.add(new TMapPoint(position[0], position[1]));
            myData.add(name);
            notifyDataSetChanged();
        }
    }

    public ArrayList<TMapPoint> getStopLocation() {
        return stopLocation;
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }
}
