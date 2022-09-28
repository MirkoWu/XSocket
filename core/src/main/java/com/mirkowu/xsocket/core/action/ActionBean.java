package com.mirkowu.xsocket.core.action;

import com.mirkowu.xsocket.core.listener.ISocketListener;

public class ActionBean {
    Object data;
    volatile ISocketListener listener;

    public ActionBean() {
    }

    public ActionBean(Object data) {
        this.data = data;
    }

}
