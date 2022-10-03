package com.mirkowu.xsocket.core;

import com.mirkowu.xsocket.core.action.IActionDispatcher;

import java.io.OutputStream;

public interface ISender {
    void init(OutputStream outputStream, IActionDispatcher dispatcher);

    boolean send() throws  Exception;

    void offer(ISendData sendData);
    void close();

}
