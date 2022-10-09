package com.mirkowu.xsocket.server;



public interface IClientStatusListener {

    void onClientConnected(IClient client, int serverPort, IClientPool clientPool);

    void onClientDisconnected(IClient client, int serverPort, IClientPool clientPool,Exception e);

}
