package com.restaurant.alpha.alphanavigation;

import android.app.Application;

public class AlphaNavigation extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize singleton(s)
        CommonData.getInstance();

        /* initialize db, and... such things here
         * see https://github.com/wordpress-mobile/WordPress-Android/blob/develop/WordPress/src/main/java/org/wordpress/android/WordPress.java */
    }
}
