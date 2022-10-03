package com.mirkowu.xsocket.server;

import com.mirkowu.xsocket.core.ISendData;

public interface IClientIOListener {

    void onReceiveFromClient(byte[] bytes, IClient client, IClientPool<String, IClient> clientPool);

    void onSendToClient(ISendData sendData, IClient client, IClientPool<String, IClient> clientPool);

}
