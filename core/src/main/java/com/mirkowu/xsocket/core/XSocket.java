package com.mirkowu.xsocket.core;

public class XSocket {



    public IConnectManager connect(String ip, int port) {
        return CacheManager.getInstance().get(new IPConfig(ip, port));
    }

    public XSocket setSender(ISender sender) {

        return this;
    }

    public XSocket setReceiver(IReceiver receiver) {

        return this;
    }

    public XSocket setTimer() {
        return this;
    }

    public XSocket setPulseManager() {
        return this;
    }

    public XSocket setReconnectManager() {
        return this;
    }


}
