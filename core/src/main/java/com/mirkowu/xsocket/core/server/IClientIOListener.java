package com.mirkowu.xsocket.core.server;

public interface IClientIOListener {

    void onClientReceive(byte[] bytes, IClient client, IClientPool<String, IClient> clientPool);

    void onClientSend(byte[] bytes, IClient client, IClientPool<String, IClient> clientPool);

}
