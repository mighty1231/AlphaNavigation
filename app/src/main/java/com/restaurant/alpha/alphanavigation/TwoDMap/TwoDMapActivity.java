package com.restaurant.alpha.alphanavigation.TwoDMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.restaurant.alpha.alphanavigation.CommonData;
import com.restaurant.alpha.alphanavigation.R;
import com.skp.Tmap.TMapData.FindPathDataListenerCallback;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

/**
 * @author Snyo
 * A Class that shows 2D map and path. It is called by navigation.
 */
public class TwoDMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2d_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_2D);
        assert toolbar != null;
        toolbar.setTitle("Map");
        setSupportActionBar(toolbar);

        RelativeLayout mMainRelativeLayout = (RelativeLayout) findViewById(R.id.tmapapi2);
        final TMapView tmapview = new TMapView(this);

        assert mMainRelativeLayout != null;


        mMainRelativeLayout.addView(tmapview);

        TMapPoint startpoint = tmapview.getLocationPoint();
        TMapPoint endpoint = CommonData.getInstance().getDestination();
        ArrayList<TMapPoint> stops = CommonData.getInstance().getStops();
        if (CommonData.getInstance().getCurrentLocation() != null) {
            startpoint = CommonData.getInstance().getCurrentLocation();
            tmapview.setLocationPoint(CommonData.getInstance().getCurrentLocation().getLongitude(), CommonData.getInstance().getCurrentLocation().getLatitude());
        }
        TMapData tMapData = new TMapData();
        tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, startpoint, endpoint, stops, 0, new FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine pathdata) {
                tmapview.addTMapPath(pathdata);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_2d_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_back_to_3d) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
