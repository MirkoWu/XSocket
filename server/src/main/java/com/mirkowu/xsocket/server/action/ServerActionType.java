package com.mirkowu.xsocket.server.action;

public interface ServerActionType {
    interface Server {
        int ACTION_SERVER_LISTENING = 201;
        int ACTION_CLIENT_CONNECTED = 202;
        int ACTION_CLIENT_DISCONNECTED = 203;
        int ACTION_SERVER_WILL_SHUTDOWN = 204;
        int ACTION_SERVER_ALREADY_SHUTDOWN = 205;

    }

}
