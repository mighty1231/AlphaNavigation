package com.restaurant.alpha.alphanavigation.TwoDMap;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.restaurant.alpha.alphanavigation.AlphaNavigation;
import com.restaurant.alpha.alphanavigation.CameraNavigation.CameraNavigationActivity;
import com.restaurant.alpha.alphanavigation.CommonData;
import com.restaurant.alpha.alphanavigation.R;
import com.skp.Tmap.TMapData.FindPathDataListenerCallback;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

import static java.lang.Math.*;

/**
 * @author AlphaGoRestaurant
 *         A Class that shows 2D map and path. It is called by navigation.
 */
public class TwoDMapActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    private Toolbar toolbar = null;
    private TMapView tMapView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2d_map);

        ((AlphaNavigation) getApplication()).setLocationChangeCallback(this);

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_2D);
        assert toolbar != null;
        toolbar.setTitle("Map");
        setSupportActionBar(toolbar);

        tMapView = new TMapView(this);

        // Add tmap view
        RelativeLayout mMainRelativeLayout = (RelativeLayout) findViewById(R.id.layout_tMapView);
        assert mMainRelativeLayout != null;
        mMainRelativeLayout.addView(tMapView);

        TMapPoint startPoint = getCurrentLocation(5);
        TMapPoint endPoint = CommonData.getInstance().getDestination();
        ArrayList<TMapPoint> stops = CommonData.getInstance().getStops();
        if ( startPoint == null ) {
            Toast.makeText(getApplicationContext(),
                    "Cannot find current location",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        assert startPoint != null;
        double sLongitude = startPoint.getLongitude();
        double sLatitude = startPoint.getLatitude();
        tMapView.setLocationPoint(sLongitude, sLatitude);
        tMapView.setCenterPoint(sLongitude, sLatitude);
        tMapView.setCompassMode(true);

        // Find pedestrian path
        TMapData tMapData = new TMapData();
        tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH,
                startPoint, endPoint, stops, 0, new FindPathDataListenerCallback() {
                    @Override
                    public void onFindPathData(TMapPolyLine pathData) {
                        tMapView.addTMapPath(pathData);
                        TMapPolyLine simpledPathData = separateLine(pathData.getLinePoint());
//                        tMapView.addTMapPolyLine("SIMPLE", simpledPathData);

                        CommonData.getInstance().setPathFound(pathData.getLinePoint());

                        int i = 0;
                        for (TMapPoint point : simpledPathData.getLinePoint()) {
                            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
                            tMapMarkerItem.setTMapPoint(point);
                            tMapMarkerItem.setVisible(TMapMarkerItem.VISIBLE);
                            tMapView.addMarkerItem("TEST" + i++, tMapMarkerItem);
                        }
                    }

                    public TMapPolyLine separateLine(ArrayList<TMapPoint> points) {
                        TMapPolyLine simpledPathData = new TMapPolyLine();
                        int size = points.size();

                        simpledPathData.addLinePoint(points.get(0));
                        separateLineRec(points, simpledPathData, 0, size - 1, 40);
                        simpledPathData.addLinePoint(points.get(size - 1));

                        Log.d("Path find", simpledPathData.getLinePoint().size() + "");
                        Log.d("Path find", simpledPathData.getLinePoint().toString());

                        return simpledPathData;
                    }

                    public void separateLineRec(ArrayList<TMapPoint> points, TMapPolyLine result,
                                                int s, int e, double epsilon) {
                        TMapPoint endPoint1 = points.get(s);
                        TMapPoint endPoint2 = points.get(e);
                        int maxIdx = -1;
                        double maxDist = epsilon;
                        for (int i = s + 1; i <= e - 1; ++i) {
                            TMapPoint p = points.get(i);
                            double dist = getDistanceToLine(p, endPoint1, endPoint2);
                            Log.d("Distance to point", i + ": " + dist + " (Line: " + s + ", " + e + ")");
                            if ( maxDist < dist ) {
                                Log.d("YES", "GANG");
                                maxIdx = i;
                                maxDist = dist;
                            }
                        }
                        if ( maxIdx != -1 ) {
                            separateLineRec(points, result, s, maxIdx, epsilon);
                            result.addLinePoint(points.get(maxIdx));
                            separateLineRec(points, result, maxIdx, e, epsilon);
                        }
                    }

                    public double getDistanceToLine(TMapPoint p, TMapPoint a, TMapPoint b) {
                        double lat1 = p.getLatitude();
                        double lon1 = p.getLongitude();
                        double lat2 = a.getLatitude();
                        double lon2 = a.getLongitude();
                        double lat3 = b.getLatitude();
                        double lon3 = b.getLongitude();

                        // meter scale
                        // double ax = 0;
                        // double ay = 0;
                        double px = abs(lat2 - lat1) * 110 * 1000;
                        double py = abs(lon2 - lon1) * 88.7 * 1000;
                        double bx = abs(lat3 - lat2) * 110 * 1000;
                        double by = abs(lon3 - lon2) * 88.7 * 1000;

                        double normalDistance = sqrt(bx * bx + by * by);
                        return normalDistance > 0.0000001 ? abs(px * by - py * bx) / normalDistance : 0;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_2d_view, menu);
        return true;
    }

    @Override
    public void onLocationChange(Location location) {
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
        Toast.makeText(getApplicationContext(),
                "Updating",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_back_to_3d:
                finish();
                break;
            case R.id.action_current:
                TMapPoint p = AlphaNavigation.gps.getLocation();
                tMapView.setCenterPoint(p.getLongitude(), p.getLatitude(), true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((AlphaNavigation) getApplication()).deleteLocationChangeCallback();
    }

    public TMapPoint getCurrentLocation(int atmptnumber) {
        TMapPoint p;
        do {
            p = AlphaNavigation.gps.getLocation();
            if ( p.getLongitude() * p.getLatitude() > 0 )
                return p;
            --atmptnumber;
        } while (atmptnumber > 0);

        return null;
    }
}
