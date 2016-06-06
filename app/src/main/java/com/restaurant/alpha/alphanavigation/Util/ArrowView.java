/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.restaurant.alpha.alphanavigation.Util;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.restaurant.alpha.alphanavigation.Util.ArrowRenderer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class ArrowView extends GLSurfaceView {

    private final ArrowRenderer mRenderer;
    private Timer refreshTimer = new Timer();

    public ArrowView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new ArrowRenderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        refreshTimer.scheduleAtFixedRate(new refreshTask(), 0, 30);
    }

    /* dEW = E (+) W (-)
     * dNS = N (+) S (-) */
    public void setDestination(float dEW, float dNS) {
        mRenderer.setDestination(dEW, dNS);
        requestRender();
    }
    class refreshTask extends TimerTask {
        public void run() {
            float[] fusedOrientation = SensorFusionListener.getInstance(null).getFusedOrientation();
            mRenderer.setFusedOrientation(fusedOrientation[0], fusedOrientation[1], fusedOrientation[2]);
            requestRender();
        }
    }
}
