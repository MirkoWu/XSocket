package com.mirkowu.xsocket.core.action;



public class ActionBean {
    public int action;
    public Object dispatcher;
    public Object data;
    public Object args2;

    public ActionBean() {
    }

    public ActionBean(Object data) {
        this.data = data;
    }

    public ActionBean(Object data, Object args2) {
        this.data = data;
        this.args2 = args2;
    }
}
