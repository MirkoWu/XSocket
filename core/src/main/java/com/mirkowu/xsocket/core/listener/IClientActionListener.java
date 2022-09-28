package com.mirkowu.xsocket.core.listener;

import com.mirkowu.xsocket.core.IPConfig;

public interface IClientActionListener extends IConnectStatusListener {

    void onSend(IPConfig config,byte[] bytes);

    void onReceive(IPConfig config,byte[] bytes);
}
