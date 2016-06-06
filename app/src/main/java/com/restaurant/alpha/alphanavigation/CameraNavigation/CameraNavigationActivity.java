package com.restaurant.alpha.alphanavigation.CameraNavigation;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.restaurant.alpha.alphanavigation.R;

public class CameraNavigationActivity extends Activity {
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout mCameraNavigationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_navigation);
        mCameraNavigationLayout = (FrameLayout) findViewById(R.id.camera_preview);
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
