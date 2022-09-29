package com.mirkowu.xsocket.core.server;

public interface IClientSocketListener {

    void onClientReceiveReady();

    void onClientSendReady();

    void onClientReceiveDead(Exception e);

    void onClientSendDead(Exception e);

    void onClientReceive(byte[] bytes);

    void onClientSend(byte[] bytes);

}
