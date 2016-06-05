package com.restaurant.alpha.alphanavigation;

import com.skp.Tmap.TMapPoint;

import java.util.ArrayList;

/* singleton class to share data for all activities
 * This should be initiated at Application object to prevent being freed by android.*/
public class CommonData {
    private static CommonData mInstance = null;
    private TMapPoint destination = null;
    private ArrayList<TMapPoint> stops = null;
    private TMapPoint currentLocation = null;

    public static CommonData getInstance() {
        /* DO NOT modify this */
        if (mInstance == null)
            mInstance = new CommonData();
        return mInstance;
    }

    private CommonData() {
        /* initialization */
    }

    public void setDestination (TMapPoint item) {
        destination = item;
    }

    public void setStops (ArrayList<TMapPoint> items) {
        stops = items;
    }

    public void setCurrentLocation (TMapPoint item) {
        currentLocation = item;
    }

    public TMapPoint getDestination () {
        return destination;
    }

    public ArrayList<TMapPoint> getStops () {
        return stops;
    }

    public TMapPoint getCurrentLocation () {
        return currentLocation;
    }
}
