package com.mirkowu.xsocket.server.listener;


import com.mirkowu.xsocket.server.client.IClientPool;
import com.mirkowu.xsocket.server.IShutdown;

public interface IServerSocketListener extends IClientIOListener{
    void onServerListening(int serverPort);

//    void onClientConnected(IClient client, int serverPort, IClientPool clientPool);
//
//    void onClientDisconnected(IClient client, int serverPort, IClientPool clientPool,Exception e);



    void onServerWillBeShutdown(int serverPort, IShutdown shutdown, IClientPool clientPool, Exception e);

    void onServerAlreadyShutdown(int serverPort, Exception e);

}
