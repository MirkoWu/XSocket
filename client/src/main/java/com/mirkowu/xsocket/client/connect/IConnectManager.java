package com.mirkowu.xsocket.client.connect;

import com.mirkowu.xsocket.core.IConnectable;
import com.mirkowu.xsocket.core.IDisconnectable;
import com.mirkowu.xsocket.core.ISendData;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.client.listener.ISocketListener;
import com.mirkowu.xsocket.core.listener.IRegister;

public interface IConnectManager extends IConnectable, IDisconnectable, IRegister<ISocketListener> {

    void send(ISendData sendData);

    boolean isClosed();

    boolean isConnected();

    boolean isDisconnecting();

    IActionDispatcher getActionDispatcher();

    PulseManager getPulseManager();

    void removeAllSocketListener();
}
