package com.restaurant.alpha.alphanavigation;

import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;

import java.util.ArrayList;

/**
 * @author AlphaRestaurant
 *         singleton class to share data for all activities
 *         This should be initiated at Application object to prevent being freed by android.
 */
public class CommonData {
    private static CommonData mInstance = null;
    private TMapPoint destination = null;
    private ArrayList<TMapPoint> stops = null;
    private TMapPoint currentLocation = null;
    private double remainStraightDistance;
    private double remainDistance;
    private int nextPoint = 1;

    private TMapPolyLine pathFound = null;
    private ArrayList<TMapPoint> simplePathPoint = null;

    public static CommonData getInstance() {
        /* DO NOT modify this */
        if ( mInstance == null )
            mInstance = new CommonData();
        return mInstance;
    }

    private CommonData() {
        /* initialization */
    }

    public void setDestination(TMapPoint item) {
        destination = item;
    }

    public void setStops(ArrayList<TMapPoint> items) {
        stops = items;
    }

    public void setCurrentLocation(TMapPoint item) {
        currentLocation = item;
    }

    public void setPathFound(TMapPolyLine item) {
        pathFound = item;
    }

    public void setSimplePathPoint(ArrayList<TMapPoint> items) {
        simplePathPoint = items;
    }

    public void setRemainStraightDistance(Double distance) {
        remainStraightDistance = distance;
    }

    public void setRemainDistance (Double distance) {
        remainDistance = distance;
    }

    public void setNextPoint () {
        nextPoint++;
    }

    public TMapPoint getDestination() {
        return destination;
    }

    public ArrayList<TMapPoint> getStops() {
        return stops;
    }

    public TMapPoint getCurrentLocation() {
        return currentLocation;
    }

    public TMapPolyLine getPathFound() {
        return pathFound;
    }

    public ArrayList<TMapPoint> getSimplePathPoint() {
        return simplePathPoint;
    }

    public double getRemainStraightDistance() {
        return remainStraightDistance;
    }

    public double getRemainDistance() {
        return remainDistance;
    }

    public int getNextPointRaw() {
        return nextPoint;
    }

    public TMapPoint getNextPoint() {
        return simplePathPoint.get(nextPoint);
    }

    public int getRemainDistancePoint() {
        return simplePathPoint.size() - nextPoint;
    }
}
