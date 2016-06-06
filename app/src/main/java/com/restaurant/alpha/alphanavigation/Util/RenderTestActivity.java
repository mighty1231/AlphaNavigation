package com.restaurant.alpha.alphanavigation.Util;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.restaurant.alpha.alphanavigation.R;

import java.util.Locale;

public class RenderTestActivity extends AppCompatActivity {
    SensorFusionListener sensorFusionListener = null;
    TextView textView = null;
    Handler handler = null;
    private float[] fusedOrientation;
    ArrowView arrowView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_render_test);

        arrowView = new ArrowView(this);
        setContentView(arrowView);
//        sensorFusionListener = new SensorFusionListener(this);
//        sensorFusionListener.create();
//
//        textView = (TextView) findViewById(R.id.renderTestTV);
//
//        handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                fusedOrientation = sensorFusionListener.getFusedOrientation();
//
//                textView.setText(String.format(Locale.KOREA, "[%f, %f, %f]", fusedOrientation[0], fusedOrientation[1], fusedOrientation[2]));
//                handler.postDelayed(this, 100);
//            }
//        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        arrowView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        arrowView.onResume();
    }

}
