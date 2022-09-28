package com.mirkowu.xsocket.core;

import java.io.OutputStream;

public interface ISender {
    void init(OutputStream outputStream);

    boolean send();

    void offer(byte[] bytes);
    void close();

}
