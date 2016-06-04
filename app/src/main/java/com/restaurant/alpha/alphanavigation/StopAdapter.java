package com.restaurant.alpha.alphanavigation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by HyunhoHa on 2016-06-01.
 */
public class StopAdapter extends RecyclerView.Adapter<StopAdapter.ViewHolder> {
    Context context;
    private ArrayList<String> myData;

    // Provide a suitable constructor (depends on the kind of dataset)
    public StopAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        myData = data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_view, null);

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
            @Override()
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("항목중에 하나를 선택하세요.");
                final CharSequence[] items = {"이름으로 검색하기", "지도에서 검색하기"};
                alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, items[which], Toast.LENGTH_SHORT).show();
                                Intent intent = null;
                                if (which == 0) {
                                    intent = new Intent(context, SelectLocationByNameActivity.class);
                                }
                                else {
                                    intent = new Intent(context, SelectLocationByMapActivity.class);
                                }
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        });


                alertBuilder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
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

    // Return the size of your data (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myData.size();
    }
}
