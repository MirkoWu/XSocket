package com.mirkowu.xsocket.core.server;

public interface ServerActionType {
    interface Server{
        int ACTION_LISTENING=1;
        int ACTION_CLIENT_CONNECTED=2;
        int ACTION_CLIENT_DISCONNECTED=3;
        int ACTION_WILL_SHUTDOWN=4;
        int ACTION_ALREADY_SHUTDOWN=5;

    }
    interface Client{

    }

}
