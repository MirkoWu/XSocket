package com.mirkowu.xsocket.client;

import com.mirkowu.xsocket.client.connect.CacheManager;
import com.mirkowu.xsocket.client.connect.IConnectManager;
import com.mirkowu.xsocket.core.IReceiver;
import com.mirkowu.xsocket.core.ISender;
import com.mirkowu.xsocket.core.server.IServerManager;
import com.mirkowu.xsocket.core.server.ServerManagerImp;
import com.mirkowu.xsocket.core.server.ServerOptions;

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


    public IServerManager getServer(int serverPort){
        IServerManager serverManager=  new ServerManagerImp(serverPort, ServerOptions.getDefault());
        return serverManager;
    }

}
