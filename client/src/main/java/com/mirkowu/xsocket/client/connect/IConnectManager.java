package com.mirkowu.xsocket.client.connect;

import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.client.listener.ISocketListener;
import com.mirkowu.xsocket.core.listener.IRegister;

public interface IConnectManager extends IRegister<ISocketListener> {
    void connect();

    void send(byte[] bytes);

    void disconnect();

    void disconnect(Exception e);


    boolean isClosed();

    boolean isConnected();

    boolean isDisconnecting();

    IActionDispatcher getActionDispatcher();

}
