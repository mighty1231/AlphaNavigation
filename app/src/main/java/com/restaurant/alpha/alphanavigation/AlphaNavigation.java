package com.restaurant.alpha.alphanavigation;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.restaurant.alpha.alphanavigation.Util.SensorFusionListener;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AlphaNavigation extends Application implements TMapGpsManager.onLocationChangedCallback {

    private boolean firstLocation = false;
    private boolean startNavigation = false;
    public static TMapGpsManager gps = null;

    public TMapGpsManager.onLocationChangedCallback callback1 = null;
    public TMapGpsManager.onLocationChangedCallback callback2 = null;

    public void onCreate() {
        super.onCreate();

        // initialize singleton(s)
        CommonData.getInstance();
        SensorFusionListener.getInstance(this);


        gps = new TMapGpsManager(this);
        gps.setMinTime(500);
        gps.setMinDistance(1);
        gps.setProvider(TMapGpsManager.NETWORK_PROVIDER);
        gps.OpenGps();

        /**
         * initialize db, and... such things here
         * see https://github.com/wordpress-mobile/WordPress-Android/blob/develop/WordPress/src/main/java/org/wordpress/android/WordPress.java
         * */

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("alphaNavigation.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    @Override
    public void onLocationChange(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        CommonData.getInstance().setCurrentLocation(new TMapPoint(latitude, longitude));

        if(!firstLocation) {
            Toast.makeText(getApplicationContext(), "Get Location", Toast.LENGTH_SHORT).show();
            firstLocation = true;
        }

        if (startNavigation) {
            TMapPoint destPoint = CommonData.getInstance().getDestination();
            TMapPoint currPoint = new TMapPoint(location.getLatitude(), location.getLongitude());

            TMapPolyLine tMapPolyLine = new TMapPolyLine();

            tMapPolyLine.addLinePoint(currPoint);
            tMapPolyLine.addLinePoint(CommonData.getInstance().getNextPoint());
            if (tMapPolyLine.getDistance() < 10) {
                if (CommonData.getInstance().getRemainDistancePoint() < 1) {
                    Toast.makeText(getApplicationContext(), "Ending Success", Toast.LENGTH_SHORT).show();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                else {
                    CommonData.getInstance().setNextPoint();
                }
            }

            tMapPolyLine = new TMapPolyLine();
            tMapPolyLine.addLinePoint(currPoint);
            tMapPolyLine.addLinePoint(destPoint);
            Double straightD = tMapPolyLine.getDistance();
            CommonData.getInstance().setRemainStraightDistance(straightD);

            tMapPolyLine = new TMapPolyLine();
            ArrayList<TMapPoint> tMapPoints = CommonData.getInstance().getSimplePathPoint();
            tMapPolyLine.addLinePoint(currPoint);
            for (int i = CommonData.getInstance().getNextPointRaw(); i < tMapPoints.size(); i++) {
                tMapPolyLine.addLinePoint(tMapPoints.get(i));
            }
            Double remainD = tMapPolyLine.getDistance();
            CommonData.getInstance().setRemainDistance(remainD);

            //Toast.makeText(getApplicationContext(), Integer.toString(CommonData.getInstance().getNextPointRaw()) + "," + Double.toString(straightD) + "," + Double.toString(remainD), Toast.LENGTH_SHORT).show();
        }

        if ( callback1 != null ) {
            callback1.onLocationChange(location);
        }
        if (callback2 != null) {
            callback2.onLocationChange(location);
        }
    }

    public void setLocationChangeCallback(TMapGpsManager.onLocationChangedCallback ca) {
        startNavigation = true;
        callback1 = ca;
    }

    public void setLocationChangeCallBackCamera (TMapGpsManager.onLocationChangedCallback ca) {
        callback2 = ca;
    }

    public void deleteLocationChangeCallback() {
        callback1 = null;
    }

    public void deleteLocationChangeCallBackCamera() {
        callback2 = null;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        gps.CloseGps();
    }
}
