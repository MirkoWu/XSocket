package com.mirkowu.xsocket.server;

import com.mirkowu.xsocket.core.listener.IRegister;
import com.mirkowu.xsocket.server.IServerSocketListener;

public interface IServerManager extends IShutdown, IRegister<IServerSocketListener> {
    void listen();

    boolean isLive();
    IClientPool<String, IClient> getClientPool();
   // void shutdown(Exception e);
   // void shutdown();
}
