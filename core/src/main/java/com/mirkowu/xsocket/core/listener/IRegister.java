package com.mirkowu.xsocket.core.listener;

public interface IRegister<T> {

    void registerSocketListener(T listener);

    void unRegisterSocketListener(T listener);
}
