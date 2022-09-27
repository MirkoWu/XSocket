package com.mirkowu.xsocket.core.io;

import com.mirkowu.xsocket.core.ISendable;

public interface IIOManager {
    void start();

    void send(ISendable sendable);

    void close();
    void close(Exception e);

}
