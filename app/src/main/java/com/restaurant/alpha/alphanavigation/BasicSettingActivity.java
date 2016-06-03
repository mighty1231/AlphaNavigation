package com.restaurant.alpha.alphanavigation;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

public class BasicSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_setting);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.stop_recycler_view);

        ArrayList<String> item = new ArrayList<String>();
        item.add("Seoul");
        item.add("Busan");
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        StopAdapter mAdapter = new StopAdapter(getApplicationContext(), item);
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
    }
}
