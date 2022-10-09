package com.mirkowu.xsocket.core;

public class UdpSendData {
    IPConfig ipConfig;
    ISendData sendData;

    public UdpSendData(IPConfig ipConfig, ISendData sendData) {
        this.ipConfig = ipConfig;
        this.sendData = sendData;
    }
}
