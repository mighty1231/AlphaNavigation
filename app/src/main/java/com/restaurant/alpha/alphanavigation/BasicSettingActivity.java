package com.restaurant.alpha.alphanavigation;

import android.content.Context;
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

import com.restaurant.alpha.alphanavigation.TwoDMap.TwoDMapActivity;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapTapi;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.restaurant.alpha.alphanavigation.Floating.FloatingService;

import io.realm.Realm;
import io.realm.RealmResults;

public class BasicSettingActivity extends AppCompatActivity {
    public static final int STOP_REQUEST = 100;
    public static final int DESTINATION_REQUEST = 101;
    public TextView destination;
    private StopAdapter myStopAdapter;
    private HistoryAdapter myHistoryAdapter;
    private String destinationName;
    private double[] destinationPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_setting);

        startService(new Intent(getApplicationContext(), FloatingService.class));

        RecyclerView stopRecyclerView = (RecyclerView) findViewById(R.id.stop_recycler_view);
        RecyclerView historyRecyclerView = (RecyclerView)findViewById(R.id.history_recycler_view);
        ImageView addStops = (ImageView)findViewById(R.id.add_stop);
        destination = (TextView)findViewById(R.id.destination_name);
        TextView navigationStart = (TextView)findViewById(R.id.navigation_start);
        TextView deleteAllHistory = (TextView)findViewById(R.id.delete_all_history);

        assert(addStops != null);
        assert(stopRecyclerView != null);
        assert(destination != null);
        assert(navigationStart != null);
        assert(historyRecyclerView != null);
        assert (deleteAllHistory != null);

        destinationName = null;
        destinationPosition = null;
        /**
         * This is for connection
         */

        TMapTapi tmaptapi = new TMapTapi(this);
        tmaptapi.setSKPMapAuthentication ("7862e03c-f02d-3eba-a686-5a01ff03a257");

        LinearLayoutManager stopLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        stopLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stopRecyclerView.setLayoutManager(stopLinearLayoutManager);
        stopRecyclerView.setVerticalScrollBarEnabled(true);
        // 3. create an adapter
        this.myStopAdapter = new StopAdapter(getApplicationContext());
        // 4. set adapter

        stopRecyclerView.setAdapter(myStopAdapter);
        stopRecyclerView.setHasFixedSize(true);

        LinearLayoutManager historyLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        historyLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        historyRecyclerView.setLayoutManager(historyLinearLayoutManager);
        historyRecyclerView.setVerticalScrollBarEnabled(true);
        // 3. create an adapter
        this.myHistoryAdapter = new HistoryAdapter(this);
        // 4. set adapter

        historyRecyclerView.setAdapter(myHistoryAdapter);
        historyRecyclerView.setHasFixedSize(true);

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

        navigationStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(destinationPosition == null) {
                    Toast.makeText(getApplicationContext(), "Select Destination", Toast.LENGTH_SHORT).show();
                }
                else {
                    CommonData.getInstance().setDestination(new TMapPoint(destinationPosition[0], destinationPosition[1]));
                    CommonData.getInstance().setStops(myStopAdapter.getStopLocation());

                    Realm realm = Realm.getDefaultInstance();

                    // [BEGIN] Realm Transaction
                    realm.beginTransaction();

                    HistoryDB historyDB = realm.createObject(HistoryDB.class);
                    historyDB.setName(destinationName);
                    historyDB.setLatitude(destinationPosition[0]);
                    historyDB.setLongitude(destinationPosition[1]);

                    realm.commitTransaction();
                    // [COMMIT] Realm Transaction

                    final RealmResults<HistoryDB> historyDBs = realm.where(HistoryDB.class).findAll();
                    if(historyDBs.size() > HistoryAdapter.maxSize) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                historyDBs.get(HistoryAdapter.maxSize).deleteFromRealm(); // indirectly delete object
                            }
                        });
                    }
                    realm.close();

                    myHistoryAdapter.addLocation(destinationPosition[0], destinationPosition[1], destinationName);
                    Intent intent = new Intent(getApplicationContext(), TwoDMapActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        deleteAllHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Do you want to delete all history?");
                alertBuilder.setMessage("You will remove all data");

                alertBuilder.setPositiveButton("Delete All",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Realm realm = Realm.getDefaultInstance();

                                final RealmResults<HistoryDB> historyDBs = realm.where(HistoryDB.class).findAll();
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        historyDBs.deleteAllFromRealm();
                                    }
                                });
                                realm.close();

                                myHistoryAdapter.clearAllLocation();
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

        Realm realm = Realm.getDefaultInstance();

        RealmResults<HistoryDB> results = realm.where(HistoryDB.class).findAll();
        for (int i = 0; i < results.size(); i++) {
            HistoryDB hDB = results.get(i);
            myHistoryAdapter.addLocation(hDB.getLatitude(), hDB.getLongitude(), hDB.getName());
        }
        realm.close();
    }

    public void addStopInHistory (String name, TMapPoint position) {
        double[] pos = new double[2];
        pos[0] = position.getLatitude();
        pos[1] = position.getLongitude();
        this.myStopAdapter.addLocation(pos, name);
    }

    public void addDestinationInHistory(String name, TMapPoint position) {
        double[] pos = new double[2];
        pos[0] = position.getLatitude();
        pos[1] = position.getLongitude();
        destinationPosition = pos;
        destinationName = name;
        destination.setText(destinationName);
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
