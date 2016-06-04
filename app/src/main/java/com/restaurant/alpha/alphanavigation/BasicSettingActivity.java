package com.restaurant.alpha.alphanavigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.restaurant.alpha.alphanavigation.Floating.FloatingService;

public class BasicSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_setting);

        startService(new Intent(getApplicationContext(), FloatingService.class));
//        Button but = (Button) findViewById(R.id.servicetest);
//
//        but.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        }
    }
}
