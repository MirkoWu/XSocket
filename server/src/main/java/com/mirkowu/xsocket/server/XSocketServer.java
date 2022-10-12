package com.mirkowu.xsocket.server;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

import com.mirkowu.xsocket.core.XLog;

import java.util.HashMap;
import java.util.Map;

public class XSocketServer {

    private static volatile Map<Integer, IServerManager> mServerManagerMap = new HashMap<>();

//    private static class Holder {
//        private static final XSocketServer INSTANCE = new XSocketServer();
//    }
//
//    private static XSocketServer getInstance() {
//        return Holder.INSTANCE;
//    }

    private XSocketServer() {
    }

    public static void setDebug(boolean debug) {
        XLog.setDebug(debug);
        // return this;
    }

    public static IServerManager getServer(int serverPort) {
        return getServer(serverPort, ServerOptions.getDefault());
    }

    public static IServerManager getServer(int serverPort, ServerOptions options) {
        IServerManager serverManager = mServerManagerMap.get(serverPort);
        if (serverManager == null) {
            serverManager = new ServerManagerImp(serverPort, options);
            synchronized (mServerManagerMap) {
                mServerManagerMap.put(serverPort, serverManager);
            }
        }
        return serverManager;
    }
}
