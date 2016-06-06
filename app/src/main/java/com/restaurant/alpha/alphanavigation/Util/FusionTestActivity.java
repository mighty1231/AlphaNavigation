package com.restaurant.alpha.alphanavigation.Util;

import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.restaurant.alpha.alphanavigation.R;

import java.util.Timer;
import java.util.TimerTask;

public class FusionTestActivity extends AppCompatActivity {
    TextView textView;
    float[] fusedOrientation;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_render_test);

        textView = (TextView)findViewById(R.id.renderTestTV);

        SensorFusionListener.getInstance(null).activate();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fusedOrientation = SensorFusionListener.getInstance(null).getFusedOrientation();
                FusionTestActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(String.format("[%f, %f, %f]", fusedOrientation[0]*57.29578, fusedOrientation[1]*57.29578, fusedOrientation[2]*57.29578));
                    }
                });
            }
        }, 0, 100);
    }


    @Override
    public void onStop() {
        super.onStop();
        SensorFusionListener.getInstance(null).deactivate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SensorFusionListener.getInstance(null).deactivate();
    }

    @Override
    public void onResume() {
        super.onResume();
        SensorFusionListener.getInstance(null).activate();
    }

}
