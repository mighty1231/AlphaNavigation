package com.restaurant.alpha.alphanavigation;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skp.Tmap.TMapPOIItem;

import java.util.ArrayList;

/**
 * Created by HyunhoHa on 2016-06-04.
 */
public class SearchByNameAdapter extends RecyclerView.Adapter<SearchByNameAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TMapPOIItem> myData;

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchByNameAdapter(Context context) {
        this.context = context;
        myData = new ArrayList<TMapPOIItem>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SearchByNameAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_by_name_recycler_item_view, null);

        // create ViewHolder
        ViewHolder vh = new ViewHolder(itemLayoutView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your data at this position
        // - replace the contents of the view with that element
        holder.myTextView.setText(myData.get(position).getPOIName());
        String address = myData.get(position).getPOIAddress();
        int index = address.lastIndexOf(" ");
        holder.detailTextView.setText(myData.get(position).getPOIAddress().substring(0, index));
        holder.detailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Do you want to Select?");
                alertBuilder.setMessage("Adding Location");

                alertBuilder.setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                double[] pos = new double[2];
                                pos[0] = myData.get(position).getPOIPoint().getLatitude();
                                pos[1] =  myData.get(position).getPOIPoint().getLongitude();
                                String name = myData.get(position).getPOIName();

                                Intent intent = new Intent();
                                intent.putExtra("pos", pos);
                                intent.putExtra("name", name);
                                ((Activity)context).setResult(BasicSettingActivity.RESULT_OK, intent);
                                ((Activity)context).finish();
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
        holder.myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Do you want to Select?");
                alertBuilder.setMessage("Adding Location");

                alertBuilder.setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                double[] pos = new double[2];
                                pos[0] = myData.get(position).getPOIPoint().getLatitude();
                                pos[1] =  myData.get(position).getPOIPoint().getLongitude();
                                String name = myData.get(position).getPOIName();

                                Intent intent = new Intent();
                                intent.putExtra("pos", pos);
                                intent.putExtra("name", name);
                                ((Activity)context).setResult(BasicSettingActivity.RESULT_OK, intent);
                                ((Activity)context).finish();
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
        public TextView detailTextView;
        public ViewHolder(View view) {
            super(view);
            myTextView = (TextView)view.findViewById(R.id.name_title);
            detailTextView = (TextView)view.findViewById(R.id.name_detail);
        }
    }

    // Return the size of your data (invoked by the layout manager)
    public void addLocation(TMapPOIItem item) {
        myData.add(item);
    }

    public void deleteAllLocation() {
        myData.clear();
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }
}
