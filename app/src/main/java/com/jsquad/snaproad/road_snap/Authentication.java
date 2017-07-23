package com.jsquad.snaproad.road_snap;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by efc1980 on 7/22/2017.
 */

public class Authentication {

    private FirebaseAuth auth;

    public Authentication(){
        auth = FirebaseAuth.getInstance();
    }

    public void registerUser(String email, String password, OnCompleteListener<AuthResult> onCompleteListener){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }


}
