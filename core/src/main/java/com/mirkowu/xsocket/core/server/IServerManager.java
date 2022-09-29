package com.mirkowu.xsocket.core.server;

import com.mirkowu.xsocket.core.listener.IRegister;
import com.mirkowu.xsocket.core.listener.IServerSocketListener;

public interface IServerManager extends IRegister<IServerSocketListener> {
    void listen();

    boolean isLive();
    IClientPool<String,IClient> getClientPool();
    void shutdown();
}
