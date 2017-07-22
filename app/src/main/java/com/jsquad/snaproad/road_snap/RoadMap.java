package com.jsquad.snaproad.road_snap;

import android.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by efc1980 on 7/21/2017.
 */

public class RoadMap extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.InfoWindowAdapter{

    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private Location currentLocation;
    private Marker marker;

    public void displayMap(Location location, SupportMapFragment mapFragment){
        this.currentLocation = location;
        this.mapFragment = mapFragment;
        mapFragment.getMapAsync(this);
    }

    public void moveCamera(Location location){
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("JOCAS", "MARKER CLICKED");
                if(marker.isInfoWindowShown())
                    marker.hideInfoWindow();
                else{
                    Log.d("JOCAS", "INFO WINDOWS SHOWN");
                    marker.showInfoWindow();
                }
                return false;
            }
        });
    }

    public GoogleMap getGoogleMap(){
        return googleMap;
    }

    /**
     *
     * MAP CLICK LISTENERS
     */
    @Override
    public void onMapClick(LatLng latLng) {

        if(marker == null){
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
            );
        }
        else{
            marker.setPosition(latLng);
        }


    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }


    /**
     *
     * INFO WINDOW ADAPTER IMPLEMENTATION METHODS
     */
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
