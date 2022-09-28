package com.mirkowu.xsocket.core.io;

import com.mirkowu.xsocket.core.ISendable;

public interface IIOManager {
    void start();

    void send(byte[] bytes);

    void close();
    void close(Exception e);

}
