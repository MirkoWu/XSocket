package com.mirkowu.xsocket.client.listener;


import com.mirkowu.xsocket.core.IPConfig;
import com.mirkowu.xsocket.core.data.IPulseSendData;
import com.mirkowu.xsocket.core.data.ISendData;

public interface ISocketListener extends IConnectStatusListener {

    void onSend(IPConfig config, ISendData sendData);

    void onPulseSend(IPConfig config, IPulseSendData sendData);

    void onReceive(IPConfig config, byte[] bytes);
}
