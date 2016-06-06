package com.restaurant.alpha.alphanavigation.CameraNavigation;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.restaurant.alpha.alphanavigation.R;
import com.restaurant.alpha.alphanavigation.TwoDMap.SimpleTwoDMapActivity;

public class CameraNavigationActivity extends Activity {
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout mCameraNavigationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_navigation);
        mCameraNavigationLayout = (FrameLayout) findViewById(R.id.camera_preview);
        Button twoDMapbutton = (Button)findViewById(R.id.button_2d);
        int viewCount = mCameraNavigationLayout.getChildCount();

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        if (mCamera != null) {
            mPreview = new CameraPreview(this, mCamera);
            mCameraNavigationLayout.addView(mPreview, 0);
        } else {
            Toast.makeText(this, "Camera is not available", Toast.LENGTH_LONG);
        }

        twoDMapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SimpleTwoDMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
//        // Make all other views being front of the camera view.
//        Log.d("CameraNavigation", "viewcound = " + viewCount);
//        for (int i = 0; i < viewCount; i++) {
//            mCameraNavigationLayout.getChildAt(i).bringToFront();
//            mCameraNavigationLayout.getChildAt(i).invalidate();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCamera != null) mCamera.release();
    }
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}
