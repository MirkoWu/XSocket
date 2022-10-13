package com.mirkowu.xsocket.server.listener;


import com.mirkowu.xsocket.server.client.IClient;
import com.mirkowu.xsocket.server.client.IClientPool;

public interface IClientStatusListener {

    void onClientConnected(IClient client, int serverPort, IClientPool clientPool);

    void onClientDisconnected(IClient client, int serverPort, IClientPool clientPool,Exception e);

}
