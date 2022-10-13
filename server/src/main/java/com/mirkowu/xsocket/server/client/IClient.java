package com.mirkowu.xsocket.server.client;

import com.mirkowu.xsocket.core.IConnectable;
import com.mirkowu.xsocket.core.IDisconnectable;
import com.mirkowu.xsocket.core.data.ISendData;
import com.mirkowu.xsocket.core.SocketType;

public interface IClient extends IConnectable, IDisconnectable {

    SocketType getSocketType();

    String getHostName();

    String getHostIp();

    int getHostPort();


    String getUniqueTag();

    void send(ISendData sendData);

//    void addClientIOListener(IClientIOListener listener);
//
//    void removeClientIOListener(IClientIOListener listener);
//
//    void removeAllClientIOListener();
}
