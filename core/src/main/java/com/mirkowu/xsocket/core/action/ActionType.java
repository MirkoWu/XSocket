package com.mirkowu.xsocket.core.action;

public interface ActionType {

    int ACTION_CONNECT_SUCCESS = 101;
    int ACTION_CONNECT_FAIL = 102;
    int ACTION_DISCONNECT = 103;
    int ACTION_RECONNECT = 104;

    int ACTION_RECEIVE_START = 105;
    int ACTION_RECEIVE_SHUTDOWN = 106;
    int ACTION_SEND_START = 107;
    int ACTION_SEND_SHUTDOWN = 108;

    int ACTION_SEND = 109;
    int ACTION_RECEIVE = 110;

    int ACTION_PULSE_SEND = 111;
}
