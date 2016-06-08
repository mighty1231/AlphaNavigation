package com.restaurant.alpha.alphanavigation.Floating;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Sensor;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.restaurant.alpha.alphanavigation.CommonData;
import com.restaurant.alpha.alphanavigation.R;
import com.restaurant.alpha.alphanavigation.Util.ArrowView;
import com.restaurant.alpha.alphanavigation.Util.RenderTestActivity;
import com.restaurant.alpha.alphanavigation.Util.SensorFusionListener;
import com.skp.Tmap.TMapPoint;

import java.util.Timer;
import java.util.TimerTask;

public class FloatingService extends Service{
    public static final String ACTION_NAME = "com.restaurant.alpha.alphanavigation.Floating.FloatingService";
    private WindowManager windowManager;
    private RelativeLayout btnView, removeView;
    private ArrowView btnImg;
    private ImageView removeImg;

    private Timer refreshTimer;

    private Point szWindow = new Point();
    private float density;
    private int btnView_w;
    private int btnView_h;
    private int removeView_w;
    private int removeView_h;
    private int removeView_d; // difference on size of x and y
    private int removeView_cx;
    private int removeView_cy;

    private int iv_x, iv_y;
    private int lastclick_x, lastclick_y;


    public FloatingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void start(){
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(szWindow);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        SensorFusionListener.getInstance(null).activate("FloatingService");

        // initialize views. 1. removeView. 2. btnView
        density = getApplicationContext().getResources().getDisplayMetrics().density;
        btnView_w = (int)(60 * density);
        btnView_h = (int)(60 * density);
        removeView_w = (int)(80 * density);
        removeView_h = (int)(80 * density);
        removeView_d = (int)(30 * density);
        removeView_cx = szWindow.x/2;
        removeView_cy = (int) (szWindow.y - 25*density - removeView_h/2);
        Log.d("FloatingService", String.format("%f %d %d %d %d %d %d %d", density, btnView_w, btnView_h, removeView_w, removeView_h, removeView_d, removeView_cx, removeView_cy));

        removeView = (RelativeLayout)inflater.inflate(R.layout.floating_remove, null);
        removeImg = (ImageView)removeView.findViewById(R.id.floating_remove_img);
        WindowManager.LayoutParams removeParams = new WindowManager.LayoutParams(
                removeView_w + removeView_d, removeView_h + removeView_d,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
        );
        removeParams.gravity = Gravity.TOP | Gravity.LEFT;
        removeParams.x = removeView_cx - (removeView_w + removeView_d) / 2;
        removeParams.y = removeView_cy - (removeView_h + removeView_d) / 2;
        removeView.setVisibility(View.GONE);
        windowManager.addView(removeView, removeParams);

        btnView = (RelativeLayout)inflater.inflate(R.layout.floating_btn, null);
        btnImg = new ArrowView(this);
        btnView.addView(btnImg);

        btnImg.getLayoutParams().width = btnView_w;
        btnImg.getLayoutParams().height = btnView_h;
        WindowManager.LayoutParams btnParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        btnParams.gravity = Gravity.TOP | Gravity.LEFT;
        windowManager.addView(btnView, btnParams);

        // set click listener
        btnView.setOnTouchListener(new View.OnTouchListener() {
            private boolean removeView_visible = false;
            private boolean remove_bound = false;
            private long lClickTime;

            Handler handle_longclick = new Handler();
            Runnable run_longclick = new Runnable() {
                @Override
                public void run() {
                    Log.d("floatingService", "run!!");
                    if (removeView_visible == false) {
                        removeView_visible = true;

                        RelativeLayout.LayoutParams removeParams = (RelativeLayout.LayoutParams) removeImg.getLayoutParams();
                        removeParams.width = removeView_w;
                        removeParams.height = removeView_h;
                        removeImg.setLayoutParams(removeParams);
                        removeView.setVisibility(View.VISIBLE);
                    }
                }
            };

            @Override public boolean onTouch(View v, MotionEvent event) {
                WindowManager.LayoutParams params = (WindowManager.LayoutParams) btnView.getLayoutParams();
                int event_x = (int) event.getRawX();
                int event_y = (int) event.getRawY();
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lClickTime = System.currentTimeMillis();
                        iv_x = params.x;
                        iv_y = params.y;

                        lastclick_x = event_x;
                        lastclick_y = event_y;

                        handle_longclick.postDelayed(run_longclick, 800);
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - lClickTime < 300) {
                            onClick();
                        }
                        handle_longclick.removeCallbacks(run_longclick);
                        removeView_visible = false;
                        removeView.setVisibility(View.GONE);

                        if (remove_bound) {
                            stopService(new Intent(FloatingService.this, FloatingService.class));
                            remove_bound = false;
                            break;
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = iv_x + (event_x - lastclick_x);
                        params.y = iv_y + (event_y - lastclick_y);

                        // this param could be not actually parameter for layout for update.
                        // if removeView is visible and near, it would have center at remove view.

                        if (removeView_visible) {
                            if (Math.abs(params.x + btnView_w/2 - removeView_cx) <= removeView_w/2 &&
                                    Math.abs(params.y + btnView_h/2 - removeView_cy) <= removeView_h/2) {
                                /* bound */
                                if (!remove_bound) {
                                    remove_bound = true;

                                    RelativeLayout.LayoutParams removeParams = (RelativeLayout.LayoutParams) removeImg.getLayoutParams();
                                    removeParams.width = removeView_w + removeView_d;
                                    removeParams.height = removeView_h + removeView_d;
                                    removeImg.setLayoutParams(removeParams);

                                    WindowManager.LayoutParams btnParams = (WindowManager.LayoutParams) btnView.getLayoutParams();
                                    btnParams.x = removeView_cx - btnView_w/2;
                                    btnParams.y = removeView_cy - btnView_h/2;
                                    windowManager.updateViewLayout(btnView, btnParams);
                                }
                                break;
                            } else {
                                if (remove_bound) {
                                    remove_bound = false;

                                    RelativeLayout.LayoutParams removeParams = (RelativeLayout.LayoutParams) removeImg.getLayoutParams();
                                    removeParams.width = removeView_w;
                                    removeParams.height = removeView_h;
                                    removeImg.setLayoutParams(removeParams);
                                }
                            }
                        }
                        windowManager.updateViewLayout(btnView, params);
                        return true;
                }
                return false;
            }
        });

        refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run () {
                TMapPoint point = CommonData.getInstance().getNextPoint();
                TMapPoint current = CommonData.getInstance().getCurrentLocation();
                if (point != null && current != null)
                    btnImg.setDestination((float)(point.getLongitude() - current.getLongitude()), (float)(point.getLatitude() - current.getLatitude()));
            }
        }, 0, 50);
    }

    // click callback
    // longclick call back could be called by above runnable object.
    public void onClick() {
        // startActivity()
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (btnView != null) windowManager.removeView(btnView);
        if (removeView != null) windowManager.removeView(removeView);
        SensorFusionListener.getInstance(null).deactivate("FloatingService");
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        windowManager.getDefaultDisplay().getSize(szWindow);
        removeView_cx = szWindow.x/2;
        removeView_cy = (int) (szWindow.y - 25*density - removeView_h/2);

        // @TODO touch listener should do something...
        RelativeLayout.LayoutParams removeParams = (RelativeLayout.LayoutParams) removeImg.getLayoutParams();
        removeParams.width = removeView_w;
        removeParams.height = removeView_h;
        removeImg.setLayoutParams(removeParams);

        WindowManager.LayoutParams btnParams = (WindowManager.LayoutParams) btnView.getLayoutParams();
        btnParams.x = removeView_cx - btnView_w/2;
        btnParams.y = removeView_cy - btnView_h/2;
        windowManager.updateViewLayout(btnView, btnParams);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        if (intent != null) {
            // analyze intent
        }
        if (startid == Service.START_STICKY) {
            start();
            return super.onStartCommand(intent, flags, startid);
        } else {
            return Service.START_NOT_STICKY;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // not used
        return null;
    }
}
