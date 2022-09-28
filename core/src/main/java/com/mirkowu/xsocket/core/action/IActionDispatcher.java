package com.mirkowu.xsocket.core.action;

public interface IActionDispatcher {
    void dispatch(int action );
    void dispatch(int action,ActionBean bean);
}
