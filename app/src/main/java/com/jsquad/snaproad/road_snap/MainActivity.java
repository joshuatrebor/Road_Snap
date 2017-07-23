package com.jsquad.snaproad.road_snap;

import android.content.Intent;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private GPSTracker tracker;
    private RoadMap roadMap;

    //User data
    private String uID;
    private String userName;
    private String lastName;
    private String firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FLOATING ACTION
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //DRAWER LAYOUT
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //NAVIGATION VIEW
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //INITIALIZE
        init();
    }

    private void init(){
        //Initialize GPSTracker
        tracker = new GPSTracker(this, this);

        updateUserInfo();

        //Initialize Google Map
        roadMap = new RoadMap();
        //Get current location
        Location location = tracker.getLastKnownLocation();
        if(location == null){   //if there's no current location
            Log.d("JOCAS", "DEFAULT LOCATION USED");
            location = new Location("gps");
            location.setLatitude(14.599512);
            location.setLongitude(120.984222);
        }
        //Get map fragment
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //display map
        roadMap.displayMap(location, mapFragment);

        tracker.start(2000, 0, null);
    }

    public void updateUserInfo(){
        this.uID = getIntent().getStringExtra("uID");
        FirebaseDatabase fireBase = FirebaseDatabase.getInstance();
        DatabaseReference ref = fireBase.getReference();
        ref.child("users").child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lastName = dataSnapshot.child("lastName").getValue().toString();
                firstName = dataSnapshot.child("firstName").getValue().toString();
                userName = dataSnapshot.child("userName").getValue().toString();
                //TODO display username
                Log.d("JOCAS",userName + " logged in");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * NAVIGATION LISTENERS
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.mnLoc: {
                Location location = tracker.getCurrentLocation();
                if(location == null)
                    Log.d("JOCAS", "GOOGLE MAP LOCATION NOT CHANGED");
                else{
                    Log.d("JOCAS", "GOOGLE MAP LOCATION CHANGED");
                    roadMap.moveCamera(location);
                }
                break;
            }
            case R.id.mnDashCam:{
                tracker.stop();
                Intent intent = new Intent(MainActivity.this, DashCamActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
                break;
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
