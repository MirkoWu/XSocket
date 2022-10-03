package com.mirkowu.xsocket.server;

import com.mirkowu.xsocket.core.IConnectable;
import com.mirkowu.xsocket.core.IDisconnectable;
import com.mirkowu.xsocket.core.ISendData;

public interface IClient extends IConnectable, IDisconnectable {

    String getHostIp();

    int getHostPort();

    String getHostName();

    String getUniqueTag();

    void send(ISendData sendData);

    void addClientIOListener(IClientIOListener listener);

    void removeClientIOListener(IClientIOListener listener);

    void removeAllClientIOListener();
}
