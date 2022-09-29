package com.mirkowu.xsocket.core.server;

import com.mirkowu.xsocket.core.action.ActionBean;

public interface IServerActionDispatcher {
    void dispatch(int action, ActionBean bean);
    void dispatch(int action );
}
