package com.restaurant.alpha.alphanavigation.CameraNavigation;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.restaurant.alpha.alphanavigation.AlphaNavigation;
import com.restaurant.alpha.alphanavigation.CommonData;
import com.restaurant.alpha.alphanavigation.Floating.FloatingService;
import com.restaurant.alpha.alphanavigation.R;
import com.restaurant.alpha.alphanavigation.TwoDMap.SimpleTwoDMapActivity;
import com.restaurant.alpha.alphanavigation.Util.ArrowView;
import com.restaurant.alpha.alphanavigation.Util.SensorFusionListener;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapPoint;

public class CameraNavigationActivity extends Activity implements TMapGpsManager.onLocationChangedCallback {
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout mCameraNavigationLayout;
    private ArrowView arrowView;
    private TextView remainDistance;

    Button twoDMapButton, floatingViewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_navigation);



        mCameraNavigationLayout = (FrameLayout) findViewById(R.id.camera_preview);
        twoDMapButton = (Button)findViewById(R.id.button_2d);
        floatingViewButton = (Button)findViewById(R.id.button_floating);

        remainDistance = (TextView)findViewById(R.id.remain_distance);
        remainDistance.setText(String.format(Double.toString(Math.round(CommonData.getInstance().getRemainDistance())) + "m"));

        // Create an instance of Camera
        mCamera = getCameraInstance();

        arrowView = new ArrowView(this);
        mCameraNavigationLayout.addView(arrowView, -1);
        FrameLayout.LayoutParams arrowParams = (FrameLayout.LayoutParams) arrowView.getLayoutParams();
        arrowParams.width = (int) (80 * getApplicationContext().getResources().getDisplayMetrics().density);
        arrowParams.height = (int) (80 * getApplicationContext().getResources().getDisplayMetrics().density);
        arrowParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        arrowParams.topMargin = (int) (60 * getApplicationContext().getResources().getDisplayMetrics().density);
        arrowView.setLayoutParams(arrowParams);
        SensorFusionListener.getInstance(null).activate("CameraNavigationActivity");

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
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        floatingViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(CameraNavigationActivity.this, FloatingService.class));
                moveTaskToBack(true);
            }
        });
        ((AlphaNavigation) getApplication()).setLocationChangeCallBackCamera(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        SensorFusionListener.getInstance(null).deactivate("CameraNavigationActivity");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCamera == null) {
            mCamera = Camera.open();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        arrowView.onPause();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        SensorFusionListener.getInstance(null).deactivate("CameraNavigationActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        if (mCamera == null) {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
        }
        if (mPreview != null) {
            mPreview.setCamera(mCamera);
        }
        stopService(new Intent(this, FloatingService.class));
        arrowView.onResume();
        SensorFusionListener.getInstance(null).activate("CameraNavigationActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((AlphaNavigation) getApplication()).deleteLocationChangeCallBackCamera();
        if (mCamera != null) mCamera.release();
        SensorFusionListener.getInstance(null).deactivate("CameraNavigationActivity");
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
                TMapPoint point = CommonData.getInstance().getNextPoint();
                TMapPoint current = CommonData.getInstance().getCurrentLocation();
                remainDistance.setText(String.format(Double.toString(Math.round(CommonData.getInstance().getRemainDistance())) + "m"));
                if (point != null && current != null)
                    arrowView.setDestination((float)(point.getLongitude() - current.getLongitude()), (float)(point.getLatitude() - current.getLatitude()));
            }
        });
    }
}
