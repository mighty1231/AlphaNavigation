package com.restaurant.alpha.alphanavigation.TwoDMap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.restaurant.alpha.alphanavigation.CommonData;
import com.restaurant.alpha.alphanavigation.R;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapView;

public class SimpleTwoDMapActivity extends AppCompatActivity {
    TMapView tMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_2d_map);

        tMapView = new TMapView(this);

        RelativeLayout mMainRelativeLayout = (RelativeLayout) findViewById(R.id.layout_simple_tmap);
        assert mMainRelativeLayout != null;
        mMainRelativeLayout.addView(tMapView);

        tMapView.setLocationPoint(CommonData.getInstance().getCurrentLocation().getLongitude(), CommonData.getInstance().getCurrentLocation().getLatitude());
        tMapView.setCenterPoint(CommonData.getInstance().getCurrentLocation().getLongitude(), CommonData.getInstance().getCurrentLocation().getLatitude());
        tMapView.setCompassMode(true);

        TMapMarkerItem currentPoint = new TMapMarkerItem();
        currentPoint.setTMapPoint(CommonData.getInstance().getCurrentLocation());
        currentPoint.setVisible(TMapMarkerItem.VISIBLE);
        tMapView.addMarkerItem("currentLocation", currentPoint);
        tMapView.addTMapPath(CommonData.getInstance().getPathFound());
    }
}
