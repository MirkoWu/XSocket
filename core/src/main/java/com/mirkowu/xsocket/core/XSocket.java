package com.mirkowu.xsocket.core;

import com.mirkowu.xsocket.core.server.IServerManager;
import com.mirkowu.xsocket.core.server.ServerManagerImp;

public class XSocket {



    public  IConnectManager connect(String ip, int port) {
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


    public IServerManager startServer(){
        IServerManager serverManager=  new ServerManagerImp();
        return serverManager;
    }

}
