package com.mirkowu.xsocket.core;

import com.mirkowu.xsocket.core.listener.IClientActionListener;
import com.mirkowu.xsocket.core.listener.IRegister;

public interface IConnectManager extends IRegister<IClientActionListener> {
    void connect();

    void send(byte[] bytes);

    void disconnect();

    void disconnect(Exception e);


    boolean isClosed();

    boolean isConnected();

    boolean isDisconnecting();

}
