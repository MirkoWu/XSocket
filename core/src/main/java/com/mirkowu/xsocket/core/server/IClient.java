package com.mirkowu.xsocket.core.server;

import com.mirkowu.xsocket.core.IConnectable;
import com.mirkowu.xsocket.core.IDisconnectable;
import com.mirkowu.xsocket.core.ISendable;

public interface IClient extends IConnectable, IDisconnectable {

   String  getIp();

   String getHostName();

   String getUniqueTag();

    void send(byte[] bytes);
}
