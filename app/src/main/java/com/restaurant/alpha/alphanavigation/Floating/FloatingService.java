package com.restaurant.alpha.alphanavigation.Floating;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
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

import com.restaurant.alpha.alphanavigation.R;

public class FloatingService extends Service {
    private WindowManager windowManager;
    private RelativeLayout btnView, removeView;
    private ImageView btnImg, removeImg;
    private int btnView_w = 0, btnView_h = 0;
    private int removeView_w = 0, removeView_h = 0;
    private int removeView_cx;
    private int removeView_cy;

    private int iv_x, iv_y;
    private int lastclick_x, lastclick_y;

    private Point szWindow = new Point();

    public FloatingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void start(){

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(szWindow);
        removeView_cx = szWindow.x/2;
        removeView_cy = (int) (szWindow.y - (25 * getApplicationContext().getResources().getDisplayMetrics().density));

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        btnView = (RelativeLayout)inflater.inflate(R.layout.floating_btn, null);
        btnImg = (ImageView)btnView.findViewById(R.id.floating_btn);
        WindowManager.LayoutParams btnParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        btnParams.gravity = Gravity.TOP | Gravity.LEFT;
        windowManager.addView(btnView, btnParams);

        removeView = (RelativeLayout)inflater.inflate(R.layout.floating_remove, null);
        removeImg = (ImageView)removeView.findViewById(R.id.floating_remove);
        removeView.setVisibility(View.GONE);
        WindowManager.LayoutParams rmvParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        rmvParams.gravity = Gravity.TOP | Gravity.LEFT;
        rmvParams.x = 0;
        rmvParams.y = 100;
        windowManager.addView(removeView, rmvParams);

        // set click listener
        btnView.setOnTouchListener(new View.OnTouchListener() {
            private double removeView_scaleconst = 1.5; // size multiple if head goes to specific region
            private boolean removeView_visible = false;
            private boolean remove_bound = false;
            private long lClickTime;

            Handler handle_longclick = new Handler();
            Runnable run_longclick = new Runnable() {
                @Override
                public void run() {
                    Log.d("floatingService", "run!!");
                    removeView.setVisibility(View.VISIBLE);
                    removeView_visible = true;

                    WindowManager.LayoutParams rmvParams = (WindowManager.LayoutParams) removeView.getLayoutParams();
                    removeImg.getLayoutParams().width = removeView_w;
                    removeImg.getLayoutParams().height = removeView_h;
                    rmvParams.x = removeView_cx - removeView_w/2;
                    rmvParams.y = removeView_cy - removeView_h/2;
                    windowManager.updateViewLayout(removeView, rmvParams);
                }
            };

            @Override public boolean onTouch(View v, MotionEvent event) {
                WindowManager.LayoutParams params = (WindowManager.LayoutParams) btnView.getLayoutParams();
                if (removeView_w == 0) {
                    removeView_w = removeView.getWidth();
                    removeView_h = removeView.getHeight();
                    btnView_w = btnView.getWidth();
                    btnView_h = btnView.getHeight();
                }
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

                        Log.d("floatingService", "up with remove_bound = "+remove_bound);
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

                                    WindowManager.LayoutParams rmvParams = (WindowManager.LayoutParams) removeView.getLayoutParams();
                                    removeImg.getLayoutParams().width = (int)(removeView_w * removeView_scaleconst);
                                    removeImg.getLayoutParams().height = (int)(removeView_h * removeView_scaleconst);
                                    rmvParams.x = removeView_cx - (int)(removeView_w * removeView_scaleconst)/2;
                                    rmvParams.y = removeView_cy - (int)(removeView_h * removeView_scaleconst)/2;
                                    windowManager.updateViewLayout(removeView, rmvParams);

                                    WindowManager.LayoutParams btnParams = (WindowManager.LayoutParams) btnView.getLayoutParams();
                                    btnParams.x = removeView_cx - btnView_w/2;
                                    btnParams.y = removeView_cy - btnView_h/2;
                                    windowManager.updateViewLayout(btnView, btnParams);
                                } else {
                                    if (removeView.getLayoutParams().width == removeView_w) {
                                        Log.e("floatingService", "EEEEEEERORR -- this should not be happened");
                                    }
                                }
                                break;
                            } else {
                                remove_bound = false;

                                WindowManager.LayoutParams rmvParams = (WindowManager.LayoutParams) removeView.getLayoutParams();
                                removeImg.getLayoutParams().width = removeView_w;
                                removeImg.getLayoutParams().height = removeView_h;
                                rmvParams.x = removeView_cx - removeView_w/2;
                                rmvParams.y = removeView_cy - removeView_h/2;
                                windowManager.updateViewLayout(removeView, rmvParams);
                            }
                        }
                        windowManager.updateViewLayout(btnView, params);
                        return true;
                }
                return false;
            }
        });
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
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        windowManager.getDefaultDisplay().getSize(szWindow);
        removeView_cx = szWindow.x/2;
        removeView_cy = (int) (szWindow.y - (25 * getApplicationContext().getResources().getDisplayMetrics().density));
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
