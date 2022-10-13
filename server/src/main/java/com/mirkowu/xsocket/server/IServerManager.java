package com.mirkowu.xsocket.server;

import com.mirkowu.xsocket.core.listener.IRegister;
import com.mirkowu.xsocket.server.client.IClient;
import com.mirkowu.xsocket.server.client.IClientPool;
import com.mirkowu.xsocket.server.listener.IServerSocketListener;
import com.mirkowu.xsocket.server.action.IClientStatusRegister;

public interface IServerManager extends IShutdown, IRegister<IServerSocketListener>, IClientStatusRegister {
    void listen();

    boolean isLive();

    IClientPool<String, IClient> getClientPool();
    // void shutdown(Exception e);
    // void shutdown();
}
