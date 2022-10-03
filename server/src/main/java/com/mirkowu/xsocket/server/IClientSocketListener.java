package com.mirkowu.xsocket.server;

import com.mirkowu.xsocket.core.ISendData;

public interface IClientSocketListener {

    void onClientReceiveReady();

    void onClientSendReady();

    void onClientReceiveDead(Exception e);

    void onClientSendDead(Exception e);

    void onClientReceive(byte[] bytes);

    void onClientSend(ISendData sendData);


}
