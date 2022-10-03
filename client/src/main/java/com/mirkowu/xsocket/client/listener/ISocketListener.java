package com.mirkowu.xsocket.client.listener;


import com.mirkowu.xsocket.client.IPConfig;
import com.mirkowu.xsocket.core.IPulseSendData;
import com.mirkowu.xsocket.core.ISendData;

public interface ISocketListener extends IConnectStatusListener {

    void onSend(IPConfig config, ISendData sendData);

    void onPulseSend(IPConfig config, IPulseSendData sendData);

    void onReceive(IPConfig config, ISendData sendData);
}
