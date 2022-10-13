package com.mirkowu.xsocket.core.data;

import com.mirkowu.xsocket.core.data.ISendData;

public interface IUdpSendData extends ISendData {
    String getIp();

    int getPort();
}
