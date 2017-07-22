package com.jsquad.snaproad.road_snap;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by efc1980 on 7/21/2017.
 */

public class FirebaseController {

    private DatabaseReference ref;

    public FirebaseController(){
        FirebaseDatabase firebase = FirebaseDatabase.getInstance();
        ref = firebase.getReference();
    }

    public void addFrame(LatLng location, Frame frame){
        String geoBucket = (location.latitude+"").replace('.',' ') + "-" + (location.longitude+"").replace('.',' ');
        String key = ref.child("geoframes").child(geoBucket).push().getKey();
        ref.child("geoframes").child(geoBucket).child(key).setValue(frame);
    }

    public void addUser(String uID, User user){
        ref.child("users").child(uID).setValue(user);
    }

}
