package com.mirkowu.xsocket.core.listener;


import com.mirkowu.xsocket.core.server.IShutdown;
import com.mirkowu.xsocket.core.server.client.IClient;
import com.mirkowu.xsocket.core.server.client.IClientPool;
import com.mirkowu.xsocket.core.server.IServerManager;

public interface IServerSocketListener {
    void onServerListening(int serverPort);

    void onClientConnected(IClient client, int serverPort, IClientPool clientPool);

    void onClientDisconnected(IClient client, int serverPort, IClientPool clientPool);

    void onServerWillBeShutdown(int serverPort, IShutdown shutdown, IClientPool clientPool, Throwable e);

    void onServerAlreadyShutdown(int serverPort, Throwable e);

}
