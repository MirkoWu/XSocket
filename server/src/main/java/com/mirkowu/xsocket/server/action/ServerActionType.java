package com.mirkowu.xsocket.server.action;

public interface ServerActionType {

    int ACTION_SERVER_LISTENING = 201;
    int ACTION_SERVER_WILL_SHUTDOWN = 204;
    int ACTION_SERVER_ALREADY_SHUTDOWN = 205;

    int ACTION_CLIENT_STATUS_START=500;
    int ACTION_CLIENT_CONNECTED = 501;
    int ACTION_CLIENT_DISCONNECTED = 502;
    int ACTION_CLIENT_STATUS_END=599;


}
