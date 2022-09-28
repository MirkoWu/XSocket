package com.mirkowu.xsocket.core.action;

public interface ActionType {
    int ACTION_SEND = 1;
    int ACTION_RECEIVE = 2;
    int ACTION_CONNECT_SUCCESS = 3;
    int ACTION_CONNECT_FAIL = 4;
    int ACTION_DISCONNECT = 5;
    int ACTION_RECONNECT = 6;
}
