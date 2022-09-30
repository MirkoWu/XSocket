package com.mirkowu.xsocket.core.action;


public interface IActionDispatcher {
    void dispatchAction(int action );
    void dispatchAction(int action, ActionBean bean);
}
