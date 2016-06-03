package com.restaurant.alpha.alphanavigation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.skp.Tmap.TMapView;

public class SelectLocationByNameActivity extends AppCompatActivity {
    private RelativeLayout mMainRelativeLayout =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location_by_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mMainRelativeLayout =(RelativeLayout)findViewById(R.id.tmapapi);
        TMapView tmapview = new TMapView(this);        // TmapView생성

        mMainRelativeLayout.addView(tmapview);

        tmapview.setSKPMapApiKey("7862e03c-f02d-3eba-a686-5a01ff03a257");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_location_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_send) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
