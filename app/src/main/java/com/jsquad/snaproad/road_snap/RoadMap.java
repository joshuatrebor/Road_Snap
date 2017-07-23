package com.jsquad.snaproad.road_snap;

import android.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by efc1980 on 7/21/2017.
 */

public class RoadMap extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.InfoWindowAdapter{

    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private Location currentLocation;
    private Marker marker;
    private Context context;
    private Circle circle;
    private Marker cMarker;
    private GeoFire geo;

    RoadMap(Context context){
        this.context = context;
    }


    public void displayMap(Location location, SupportMapFragment mapFragment){
        this.currentLocation = location;
        this.mapFragment = mapFragment;
        mapFragment.getMapAsync(this);
    }

    public void moveCamera(Location location){
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    public ArrayList<LatLng> getNearestLocations(LatLng latLng){
        ArrayList<LatLng> latLngs = new ArrayList<>();
        String preLat = (latLng.latitude+"").substring(0,6);
        String preLon = (latLng.longitude+"").substring(0,6);
        for(int i = 0; i < 99; i++){
            for(int j = 0; j < 99; j++){
                Double lat = Double.parseDouble(preLat+i);
                Double lon = Double.parseDouble(preLon+j);
                latLngs.add(new LatLng(lat,lon));
            }
        }
        return latLngs;
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
        CircleOptions ops = new CircleOptions()
                .center(latLng)
                .radius(50)
                .strokeWidth(10)
                .fillColor(Color.YELLOW);

        circle = googleMap.addCircle(ops);
        geo = new GeoFire(FirebaseDatabase.getInstance().getReference("geobase"));
    }

    public GoogleMap getGoogleMap(){
        return googleMap;
    }

    public void setFirebaseListener(){

    }

    /**
     *
     * MAP CLICK LISTENERS
     */
    @Override
    public void onMapClick(LatLng latLng) {

        circle.setCenter(latLng);
        GeoQuery query = geo.queryAtLocation(new GeoLocation(latLng.latitude,latLng.longitude),.05);
        query.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.latitude, location.longitude)));
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        return true;
                    }
                });
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
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
