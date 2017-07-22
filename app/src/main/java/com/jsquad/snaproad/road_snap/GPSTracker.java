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
    private LocationManager locMan;
    private String provider;
    private Location location;

    GPSTracker(Context context) {
        this.context = context;
        locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        provider = locMan.getBestProvider(new Criteria(), true);
    }

    public void start(long time, float distance, LocationListener locationListener){
        if(checkPermission()){
            if(locationListener != null){
                locMan.requestLocationUpdates(provider, time, distance, locationListener);
                Log.d("JOCAS", "GEOLOCATION STARTED USING GIVEN LISTENER");
            }
            else if(locationListener == null){
                locMan.requestLocationUpdates(provider, time, distance, this);
                Log.d("JOCAS", "GEOLOCATION STARTED USING GPSTRACKER LISTENER");
            }
        }
    }

    public Location getLastKnownLocation(){

        if(checkPermission()){
            location = locMan.getLastKnownLocation(provider);
            if(location != null) Log.d("JOCAS", "LAST KNOWN LOCATION RETURNED");
            else Log.d("JOCAS", "LAST KNOWN LOCATION NOT AVAILABLE");

            return location;
        }
        Log.d("JOCAS", "LAST KNOWN LOCATION NOT AVAILABLE");
        return null;
    }

    public void askForPermission(){
        
    }

    public Location getCurrentLocation(){
        Log.d("JOCAS", location == null ? "LOCATION NOT YET AVAILABLE":"LOCATION RETURNED");
        return location;
    }

    public boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("JOCAS", "LOCATION PERMISSION DENIED");
            return false;
        }
        return true;
    }

    /**
     * LOCATION lISTENER METHODS
     *
     */

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        Log.d("JOCAS", "LOCATION: " + location.getLatitude() + " " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}


}
