package com.restaurant.alpha.alphanavigation.Util;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.restaurant.alpha.alphanavigation.R;

import java.util.Locale;

public class RenderTestActivity extends AppCompatActivity {
    ArrowView arrowView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_render_test);

        arrowView = new ArrowView(this);

        // NNE direction
        arrowView.setDestination(1.0f, 2.0f);
        setContentView(arrowView);
        SensorFusionListener.getInstance(null).activate("RenderTestActivity");
    }

    @Override
    public void onStop() {
        super.onStop();
        SensorFusionListener.getInstance(null).deactivate("RenderTestActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        arrowView.onPause();
        SensorFusionListener.getInstance(null).deactivate("RenderTestActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        arrowView.onResume();
        SensorFusionListener.getInstance(null).activate("RenderTestActivity");
    }

}
