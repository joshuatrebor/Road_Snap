package com.jsquad.snaproad.road_snap;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DashCamActivity extends AppCompatActivity {

    private CamSurfaceView camView;
    private Camera camera;

    private FrameLayout frameLayout;
    private FloatingActionButton btnStartCapture;

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
}
