package com.mirkowu.xsocket.core.listener;

public interface IActionRegister<T> {

    void registerActionListener(T listener);

    void unRegisterActionListener(T listener);
}
