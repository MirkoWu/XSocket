package com.mirkowu.xsocket.core;

public interface IConnectManager {
    void connect();

    void disconnect();
    void disconnect(Exception e);


    boolean isClosed();

    boolean isConnected();

    boolean isDisconnecting();
}
