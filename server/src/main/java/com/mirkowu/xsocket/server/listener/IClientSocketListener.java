package com.mirkowu.xsocket.server.listener;

import com.mirkowu.xsocket.core.IPConfig;
import com.mirkowu.xsocket.core.data.ISendData;

public interface IClientSocketListener {

    void onClientReceiveReady();

    void onClientSendReady();

    void onClientReceiveDead(Exception e);

    void onClientSendDead(Exception e);

    void onClientReceive(byte[] bytes, IPConfig config);

    void onClientSend(ISendData sendData, IPConfig config);


}
