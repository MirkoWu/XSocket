package com.mirkowu.xsocket.core.server;

import com.mirkowu.xsocket.core.listener.IRegister;
import com.mirkowu.xsocket.core.listener.IServerSocketListener;
import com.mirkowu.xsocket.core.server.client.IClient;
import com.mirkowu.xsocket.core.server.client.IClientPool;

public interface IServerManager extends IShutdown, IRegister<IServerSocketListener> {
    void listen();

    boolean isLive();
    IClientPool<String, IClient> getClientPool();
   // void shutdown(Exception e);
   // void shutdown();
}
