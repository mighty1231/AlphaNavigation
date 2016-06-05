package com.restaurant.alpha.alphanavigation;

import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skp.Tmap.TMapAddressInfo;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapMarkerItem2;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

public class SelectLocationByMapActivity extends AppCompatActivity  {
    private TMapView tmapview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location_by_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RelativeLayout locationByNameRelativeLayout = (RelativeLayout)findViewById(R.id.tmapapi);
        assert(locationByNameRelativeLayout != null);
        this.tmapview = new TMapView(this);
        tmapview.setTrackingMode(true);
        if(CommonData.getInstance().getCurrentLocation() != null) {
            tmapview.setCenterPoint(CommonData.getInstance().getCurrentLocation().getLongitude(), CommonData.getInstance().getCurrentLocation().getLatitude());
            tmapview.setLocationPoint(CommonData.getInstance().getCurrentLocation().getLongitude(), CommonData.getInstance().getCurrentLocation().getLatitude());
        }
        locationByNameRelativeLayout.addView(tmapview);

        this.tmapview.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                TMapMarkerItem2 tMapMarkerItem2 = new TMapMarkerItem2();
                TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
                tMapMarkerItem2.setTMapPoint(point);
                tMapMarkerItem.setTMapPoint(point);
                tMapMarkerItem.setVisible(TMapMarkerItem.VISIBLE);
                tmapview.addMarkerItem2("location", tMapMarkerItem2);
                tmapview.addMarkerItem("location", tMapMarkerItem);
                return true;
            }
            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> markerlist,ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                return true;
            }
        });

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
            ArrayList<TMapMarkerItem2> tMapMarkerItem2ArrayList = this.tmapview.getAllMarkerItem2();

            if (tMapMarkerItem2ArrayList.size() > 0) {
                TMapMarkerItem2 tMapMarkerItem2 = tMapMarkerItem2ArrayList.get(0);
                TMapPoint tMapPoint = tMapMarkerItem2.getTMapPoint();

                final double[] position = new double[2];
                position[0] = tMapPoint.getLatitude();
                position[1] = tMapPoint.getLongitude();

                TMapData tMapData = new TMapData();
                tMapData.reverseGeocoding(position[0], position[1], "A04", new TMapData.reverseGeocodingListenerCallback() {
                    @Override
                    public void onReverseGeocoding(TMapAddressInfo tMapAddressInfo) {
                        Intent intent = new Intent();
                        intent.putExtra("pos", position);
                        if (tMapAddressInfo.strBuildingName != null) intent.putExtra("name", tMapAddressInfo.strBuildingName);
                        else if (tMapAddressInfo.strRoadName != null) intent.putExtra("name", tMapAddressInfo.strRoadName);
                        else intent.putExtra("name", tMapAddressInfo.strFullAddress);
                        setResult(BasicSettingActivity.RESULT_OK, intent);
                        finish();
                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "Choose the location", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
