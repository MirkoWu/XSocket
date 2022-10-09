package com.mirkowu.xsocket.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public interface IServerSocket {

    void createServerSocket(int serverPort) throws IOException;

    Socket accept() throws IOException;

    DatagramSocket getDatagramSocket();

    InetAddress getInetAddress();

    int getPort();

    void close() throws IOException;

    boolean isClosed();


    //  getServerSocket();


//    @Override
//    public boolean isConnected() {
//        return socket != null && !socket.isClosed() && socket.isConnected();
//    }

}
