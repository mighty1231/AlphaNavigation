package com.restaurant.alpha.alphanavigation;

/* singleton class to share data for all activities
 * This should be initiated at Application object to prevent being freed by android.*/
public class CommonData {
    private static CommonData mInstance = null;

    public static CommonData getInstance() {
        /* DO NOT modify this */
        if (mInstance == null)
            mInstance = new CommonData();
        return mInstance;
    }

    private CommonData() {
        /* initialization */
    }
}
