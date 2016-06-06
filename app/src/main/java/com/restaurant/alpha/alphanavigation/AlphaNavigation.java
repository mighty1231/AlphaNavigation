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

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AlphaNavigation extends Application implements TMapGpsManager.onLocationChangedCallback {

    public static TMapGpsManager gps = null;

    public TMapGpsManager.onLocationChangedCallback callback1 = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize singleton(s)
        CommonData.getInstance();
        SensorFusionListener.getInstance(this);


        gps = new TMapGpsManager(this);
        gps.setMinTime(1000);
        gps.setMinDistance(5);
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

        if ( callback1 != null )
            callback1.onLocationChange(location);
    }

    public void setLocationChangeCallback(TMapGpsManager.onLocationChangedCallback ca) {
        callback1 = ca;
    }

    public void deleteLocationChangeCallback() {
        callback1 = null;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        gps.CloseGps();
    }
}
