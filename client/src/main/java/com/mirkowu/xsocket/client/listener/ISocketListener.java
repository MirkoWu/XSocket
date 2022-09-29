package com.mirkowu.xsocket.client.listener;


import com.mirkowu.xsocket.client.IPConfig;

public interface ISocketListener extends IConnectStatusListener {

    void onSend(IPConfig config, byte[] bytes);

    void onReceive(IPConfig config,byte[] bytes);
}
