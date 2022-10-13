package com.mirkowu.xsocket.server.action;

import com.mirkowu.xsocket.server.listener.IClientStatusListener;

public interface IClientStatusRegister {
    void registerClientStatusListener(IClientStatusListener listener);

    void unRegisterClientStatusListener(IClientStatusListener listener);
}
