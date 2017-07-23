package com.jsquad.snaproad.road_snap;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DashCamActivity extends AppCompatActivity implements View.OnClickListener,
Camera.PictureCallback{

    private CamSurfaceView camView;
    private Camera camera;

    private FrameLayout frameLayout;
    private FloatingActionButton btnStartCapture;
    private GPSTracker tracker;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dash_cam);
        btnStartCapture = (FloatingActionButton)findViewById(R.id.captureButton);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        frameLayout = (FrameLayout) findViewById(R.id.camera_holder);
        btnStartCapture = (FloatingActionButton)findViewById(R.id.captureButton);

        camera = pullDeviceCamera();
        camView = new CamSurfaceView(this, camera);
        frameLayout.addView(camView);
        btnStartCapture.setOnClickListener(this);
        tracker = new GPSTracker(this,this);
        /*btnStartCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        Log.d("JOCAS","0001");
                        savePicture(data);
                    }
                });
            }
        });*/
    }


    private Camera pullDeviceCamera(){
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCamera;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
        tracker.start(5000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                DashCamActivity.this.location = location;
                camera.takePicture(null, null, DashCamActivity.this);
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
        });
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.d("jocas","HEEEERE");
        camera.startPreview();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dref = database.getReference();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReference();

        final GeoFire geo = new GeoFire(dref.child("geobase"));

        Log.d("jocas","HEEEERE2");
        final String geoBucket = (location.getLatitude()+"").replace("."," ") + "-" + (location.getLongitude()+"").replace("."," ");
        final String key = dref.child("geobuckets").child(geoBucket).push().getKey();
        dref.child("geobuckets").child(geoBucket).child(key).setValue(new Frame(getIntent().getStringExtra("userName"), new Date()));

        Log.d("jocas","HEEEERE3");
        UploadTask uploadTask = ref.child("captures").child(key).putBytes(data);
        Log.d("jocas","here");
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("jocas","failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                dref.child("geobuckets").child(geoBucket).child(key).child("imgUrl").setValue(downloadUrl.toString());
                Log.d("jocas", geoBucket);
                String key = dref.child("geobase").push().getKey();
                dref.child("geobase").child(key).setValue(new Frame(getIntent().getStringExtra("userName"), new Date()));
                dref.child("geobase").child(key).child("imgUrl").setValue(downloadUrl.toString());
                geo.setLocation(key, new GeoLocation(location.getLatitude(),location.getLongitude()));
            }
        });
    }
}
