package com.mirkowu.xsocket.core.server.client;

import com.mirkowu.xsocket.core.server.client.IClient;
import com.mirkowu.xsocket.core.server.client.IClientPool;

public interface IClientIOListener {

    void onReceiveFromClient(byte[] bytes, IClient client, IClientPool<String, IClient> clientPool);

    void onSendToClient(byte[] bytes, IClient client, IClientPool<String, IClient> clientPool);

}
