package com.mirkowu.xsocket.core.server;

import com.mirkowu.xsocket.core.IConnectable;
import com.mirkowu.xsocket.core.IDisconnectable;
import com.mirkowu.xsocket.core.ISendable;

public interface IClient extends IConnectable, IDisconnectable {

    String getHostIp();

    int getHostPort();

    String getHostName();

    String getUniqueTag();

    void send(byte[] bytes);

    void addClientIOListener(IClientIOListener listener);
    void removeClientIOListener(IClientIOListener listener);
    void removeAllClientIOListener();
}
