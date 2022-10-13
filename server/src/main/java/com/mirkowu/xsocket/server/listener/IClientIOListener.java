package com.mirkowu.xsocket.server.listener;

import com.mirkowu.xsocket.core.data.ISendData;
import com.mirkowu.xsocket.server.client.IClient;
import com.mirkowu.xsocket.server.client.IClientPool;

public interface IClientIOListener {

    void onReceiveFromClient(byte[] bytes, IClient client, IClientPool<String, IClient> clientPool);

    void onSendToClient(ISendData sendData, IClient client, IClientPool<String, IClient> clientPool);

}
