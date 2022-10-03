package com.mirkowu.xsocket.server;



public interface IServerSocketListener {
    void onServerListening(int serverPort);

    void onClientConnected(IClient client, int serverPort, IClientPool clientPool);

    void onClientDisconnected(IClient client, int serverPort, IClientPool clientPool);

    void onServerWillBeShutdown(int serverPort, IShutdown shutdown, IClientPool clientPool, Throwable e);

    void onServerAlreadyShutdown(int serverPort, Throwable e);

}
