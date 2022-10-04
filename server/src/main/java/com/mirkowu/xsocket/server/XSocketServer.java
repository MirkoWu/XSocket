package com.mirkowu.xsocket.server;

import com.mirkowu.xsocket.core.XLog;

public class XSocketServer {

    public static void setDebug(boolean debug) {
        XLog.setDebug(debug);
        // return this;
    }

    public static IServerManager getServer(int serverPort) {
        IServerManager serverManager = new ServerManagerImp(serverPort, ServerOptions.getDefault());
        return serverManager;
    }

    public static IServerManager getServer(int serverPort, ServerOptions options) {
        IServerManager serverManager = new ServerManagerImp(serverPort, options);
        return serverManager;
    }
}
