package com.mirkowu.xsocket.core.io;

import com.mirkowu.xsocket.core.ISendData;

public interface IUdpSendable {
    void send(String ip, int port, ISendData sendData);
}
