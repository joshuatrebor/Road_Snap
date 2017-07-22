package com.jsquad.snaproad.road_snap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by efc1980 on 7/21/2017.
 */

public class GPSTracker implements LocationListener {

    private Context context;
    private RoadMap roadMap;

    private LocationManager locMan;
    private String provider;

    private Location location;

    GPSTracker(Context context, RoadMap roadMap) {
        this.context = context;
        this.roadMap = roadMap;

        locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        provider = locMan.getBestProvider(new Criteria(), true);
    }

    public void start(){
        if(checkPermission()){
            locMan.requestLocationUpdates(provider, 5000, 0, this);
        }
    }

    public boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("jocas", "Permission Denied");
            return false;
        }
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Location getLocation(){
        return location;
    }
}
