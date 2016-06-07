package com.restaurant.alpha.alphanavigation.CameraNavigation;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.restaurant.alpha.alphanavigation.AlphaNavigation;
import com.restaurant.alpha.alphanavigation.CommonData;
import com.restaurant.alpha.alphanavigation.R;
import com.restaurant.alpha.alphanavigation.TwoDMap.SimpleTwoDMapActivity;
import com.skp.Tmap.TMapGpsManager;

public class CameraNavigationActivity extends Activity implements TMapGpsManager.onLocationChangedCallback {
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout mCameraNavigationLayout;
    private TextView remainDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_navigation);

        ((AlphaNavigation) getApplication()).setLocationChangeCallBackCamera(this);

        mCameraNavigationLayout = (FrameLayout) findViewById(R.id.camera_preview);
        Button twoDMapButton = (Button)findViewById(R.id.button_2d);

        remainDistance = (TextView)findViewById(R.id.remain_distance);
        remainDistance.setText(String.format(Double.toString(Math.round(Math.round(CommonData.getInstance().getRemainDistance() / 10) * 10)) + "m"));
        int viewCount = mCameraNavigationLayout.getChildCount();

        // Create an instance of Camera
        mCamera = getCameraInstance();

        assert(mCameraNavigationLayout != null);
        assert(remainDistance != null);

        // Create our Preview view and set it as the content of our activity.
        if (mCamera != null) {
            mPreview = new CameraPreview(this, mCamera);
            mCameraNavigationLayout.addView(mPreview, 0);
        } else {
            Toast.makeText(this, "Camera is not available", Toast.LENGTH_LONG).show();
        }

        twoDMapButton.setOnClickListener(new View.OnClickListener() {
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
        ((AlphaNavigation) getApplication()).deleteLocationChangeCallBackCamera();
        if (mCamera != null) mCamera.release();
    }
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
            c.setDisplayOrientation(90);
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onLocationChange(Location location) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                remainDistance.setText(String.format(Integer.toString(CommonData.getInstance().getNextPointRaw()) + ", " + Double.toString(Math.round(CommonData.getInstance().getRemainDistance())) + "m"));
            }
        });
    }
}
