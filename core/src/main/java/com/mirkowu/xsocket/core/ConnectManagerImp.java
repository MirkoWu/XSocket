package com.mirkowu.xsocket.core;

public class ConnectManagerImp implements IConnectManager {

    ISocket socket;
    IPConfig config;

    public ConnectManagerImp(IPConfig config) {
        this.config = config;
    }


    @Override
    public void connect() {

    }
}
