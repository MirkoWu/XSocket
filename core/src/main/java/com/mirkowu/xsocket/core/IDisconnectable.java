package com.mirkowu.xsocket.core;

public interface IDisconnectable {
    void disconnect();

    void disconnect(Exception e);
}
