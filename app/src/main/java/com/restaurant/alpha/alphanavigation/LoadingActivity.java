package com.restaurant.alpha.alphanavigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.skp.Tmap.TMapGpsManager;

public class LoadingActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{

    @Override
    public void onLocationChange(Location location) {
        Intent intent = new Intent(getApplicationContext(), BasicSettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ((AlphaNavigation) getApplication()).setLocationChangeCallback0(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((AlphaNavigation) getApplication()).deleteLocationChangeCallback0();
    }
}
