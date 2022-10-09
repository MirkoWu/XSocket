package com.mirkowu.xsocket.client.connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;

public interface IClientSocket {
    void createSocket() throws Exception;

    InputStream getInputStream();

    OutputStream getOutputStream();

    DatagramSocket getDatagramSocket();

    void close() throws IOException;

    boolean isClosed();

    boolean isConnected();

}
