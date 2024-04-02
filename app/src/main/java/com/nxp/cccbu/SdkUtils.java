/**
 * Copyright 2016 Freescale Semiconductors, Inc.
 */
package com.nxp.cccbu;


import android.os.Build;

public class SdkUtils {

    private SdkUtils() {
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
}

