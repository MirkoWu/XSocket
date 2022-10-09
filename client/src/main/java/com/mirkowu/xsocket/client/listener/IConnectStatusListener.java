package com.mirkowu.xsocket.client.listener;


import com.mirkowu.xsocket.core.IPConfig;

public interface IConnectStatusListener {
    void onConnectSuccess(IPConfig config);

    void onConnectFail(IPConfig config, Exception e);

    void onDisConnect(IPConfig config, Exception e);

    void onReconnect(IPConfig config);
}
