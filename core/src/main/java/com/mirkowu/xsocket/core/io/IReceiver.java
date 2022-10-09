package com.mirkowu.xsocket.core.io;

import com.mirkowu.xsocket.core.action.IActionDispatcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;

public interface IReceiver {
    void initTcp(InputStream inputStream, IActionDispatcher dispatcher);

    void initUdp(DatagramSocket socket,IActionDispatcher dispatcher);

    byte[] receive() throws IOException;

    void close();
}
