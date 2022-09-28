package com.mirkowu.xsocket.core.listener;

public interface IRegister<T> {

    void registerActionListener(T listener);
    void unRegisterActionListener(T listener);
}
