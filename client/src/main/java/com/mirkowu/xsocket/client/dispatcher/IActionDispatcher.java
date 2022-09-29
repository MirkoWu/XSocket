package com.mirkowu.xsocket.client.dispatcher;

import com.mirkowu.xsocket.core.action.ActionBean;

public interface IActionDispatcher {
    void dispatch(int action );
    void dispatch(int action, ActionBean bean);
}
