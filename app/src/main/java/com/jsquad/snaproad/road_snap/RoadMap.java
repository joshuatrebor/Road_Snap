package com.jsquad.snaproad.road_snap;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by efc1980 on 7/21/2017.
 */

public class RoadMap implements OnMapReadyCallback {

    private GoogleMap googleMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(14.6091,121.0223)));
    }

}
