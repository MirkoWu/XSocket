package com.mirkowu.xsocket.client;

import com.mirkowu.xsocket.client.connect.CacheManager;
import com.mirkowu.xsocket.client.connect.IConnectManager;
import com.mirkowu.xsocket.core.XLog;

public class XSocket {

    public static void setDebug(boolean debug) {
        XLog.setDebug(debug);
        // return this;
    }

    public static IConnectManager config(String ip, int port) {
        return CacheManager.getInstance().get(new IPConfig(ip, port));
    }

    public static IConnectManager config(String ip, int port, Options options) {
        return CacheManager.getInstance().get(new IPConfig(ip, port), options);
    }
}
