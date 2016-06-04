package com.restaurant.alpha.alphanavigation;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skp.Tmap.TMapTapi;

import java.util.ArrayList;

public class BasicSettingActivity extends AppCompatActivity {
    public static final int STOP_REQUEST = 100;
    public static final int DESTINATION_REQUEST = 101;
    public TextView destination;
    private StopAdapter myStopAdapter;
    private String destinationName;
    private double[] destinationPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_setting);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.stop_recycler_view);
        ImageView addStops = (ImageView)findViewById(R.id.add_stop);
        destination = (TextView)findViewById(R.id.destination_name);

        assert(addStops != null);
        assert(recyclerView != null);
        assert(destination != null);

        destinationName = null;
        destinationPosition = new double[2];
        /**
         * This is for connection
         */

        TMapTapi tmaptapi = new TMapTapi(this);
        tmaptapi.setSKPMapAuthentication ("7862e03c-f02d-3eba-a686-5a01ff03a257");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        this.myStopAdapter = new StopAdapter(this);
        // 4. set adapter
        recyclerView.setAdapter(myStopAdapter);

        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Select the below one");
                final CharSequence[] items = {"Select Location By Name", "Select Location By Map"};
                alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), items[which], Toast.LENGTH_SHORT).show();
                        Intent intent = null;
                        if (which == 0) {
                            intent = new Intent(getApplicationContext(), SelectLocationByNameActivity.class);
                        } else {
                            intent = new Intent(getApplicationContext(), SelectLocationByMapActivity.class);
                        }
                        startActivityForResult(intent, DESTINATION_REQUEST);
                    }
                });

                alertBuilder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        });
        addStops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myStopAdapter.getItemCount() >= 3) {
                    Toast.makeText(getApplicationContext(), "Cannot add more than 3 stops", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                    alertBuilder.setTitle("Select the below one");
                    final CharSequence[] items = {"Select Location By Name", "Select Location By Map"};
                    alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), items[which], Toast.LENGTH_SHORT).show();
                            Intent intent = null;
                            if (which == 0) {
                                intent = new Intent(getApplicationContext(), SelectLocationByNameActivity.class);
                            } else {
                                intent = new Intent(getApplicationContext(), SelectLocationByMapActivity.class);
                            }
                            startActivityForResult(intent, STOP_REQUEST);
                        }
                    });

                    alertBuilder.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == STOP_REQUEST && resultCode == RESULT_OK && intent != null) {
            double[] position = intent.getDoubleArrayExtra("pos");
            String name = intent.getStringExtra("name");
            this.myStopAdapter.addLocation(position, name);
        }
        else if (requestCode == DESTINATION_REQUEST && resultCode == RESULT_OK && intent != null) {
            destinationPosition = intent.getDoubleArrayExtra("pos");
            destinationName = intent.getStringExtra("name");
            destination.setText(destinationName);
        }
    }
}
