package com.restaurant.alpha.alphanavigation;

import android.app.Application;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.restaurant.alpha.alphanavigation.Util.SensorFusionListener;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapPoint;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AlphaNavigation extends Application implements TMapGpsManager.onLocationChangedCallback {

    @Override
    public void onLocationChange (Location location) {
        CommonData.getInstance().setCurrentLocation(new TMapPoint(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize singleton(s)
        CommonData.getInstance();
        SensorFusionListener.getInstance(this);


        TMapGpsManager gps = new TMapGpsManager(this);
        gps.setMinTime(1000);
        gps.setMinDistance(5);
        gps.setProvider(TMapGpsManager.GPS_PROVIDER);
        gps.OpenGps();
        /* initialize db, and... such things here
         * see https://github.com/wordpress-mobile/WordPress-Android/blob/develop/WordPress/src/main/java/org/wordpress/android/WordPress.java */

        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("alphaNavigation.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
