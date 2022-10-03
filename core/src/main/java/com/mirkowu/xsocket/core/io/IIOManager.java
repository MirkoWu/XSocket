package com.mirkowu.xsocket.core.io;

import com.mirkowu.xsocket.core.ISendData;

public interface IIOManager {
    void start();

    void send(ISendData sendData);

    void close();

    void close(Exception e);

}
