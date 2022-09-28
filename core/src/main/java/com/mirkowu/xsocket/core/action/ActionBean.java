package com.mirkowu.xsocket.core.action;

import com.mirkowu.xsocket.core.listener.IClientActionListener;

public class ActionBean {
    IClientActionListener listener;
    Object data;


    public ActionBean( ){

    }
    public ActionBean(Object data){
        this.data=data;
    }

}
