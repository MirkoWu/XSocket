package com.mirkowu.xsocket.core;

import android.util.Log;

public class XLog {
    public static final String TAG = "XSocekt";
    public static boolean isDebug = true;

    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

}
