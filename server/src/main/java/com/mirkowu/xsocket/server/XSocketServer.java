package com.mirkowu.xsocket.server;

import com.mirkowu.xsocket.core.IReceiver;
import com.mirkowu.xsocket.core.ISender;
import com.mirkowu.xsocket.core.XLog;

public class XSocketServer {

    public static void setDebug(boolean debug){
        XLog.setDebug(debug);
       // return this;
    }

    public XSocketServer setSender(ISender sender) {

        return this;
    }

    public XSocketServer setReceiver(IReceiver receiver) {
        return this;
    }



    public IServerManager getServer(int serverPort){
        IServerManager serverManager=  new ServerManagerImp(serverPort, ServerOptions.getDefault());
        return serverManager;
    }
}
