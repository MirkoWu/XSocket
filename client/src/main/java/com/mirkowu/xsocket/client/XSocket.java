package com.mirkowu.xsocket.client;

import com.mirkowu.xsocket.client.connect.CacheManager;
import com.mirkowu.xsocket.client.connect.ConnectManagerImp;
import com.mirkowu.xsocket.client.connect.IConnectManager;
import com.mirkowu.xsocket.core.IPConfig;
import com.mirkowu.xsocket.core.XLog;

import java.util.HashMap;
import java.util.Map;

public class XSocket {
    private static volatile Map<IPConfig, IConnectManager> mManagerMap = new HashMap<>();

//    private static class Holder {
//        private static final XSocket INSTANCE = new XSocket();
//    }
//
//    private static XSocket getInstance() {
//        return Holder.INSTANCE;
//    }

    private XSocket() {
    }
    public static void setDebug(boolean debug) {
        XLog.setDebug(debug);
        // return this;
    }

    public static IConnectManager config(String ip, int port) {
        return config(ip, port, Options.defaultOptions());
    }

    public static IConnectManager config(String ip, int port, Options options) {
        IPConfig ipConfig = new IPConfig(ip, port);
        IConnectManager connectManager = mManagerMap.get(ipConfig);
        if (connectManager == null) {
            connectManager = new ConnectManagerImp(ipConfig, options);
            synchronized (mManagerMap) {
                mManagerMap.put(ipConfig, connectManager);
            }
        }
        return connectManager;
    }
}
