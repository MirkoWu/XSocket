package com.mirkowu.xsocket.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer implements IServerSocket {
    protected ServerSocket mServerSocket;
    private int mServerPort;

    @Override
    public void createServerSocket(int serverPort) throws IOException {
        this.mServerPort = serverPort;
        mServerSocket = new ServerSocket();
        mServerSocket.setReuseAddress(true);
        mServerSocket.bind(new InetSocketAddress(mServerPort));

    }

    @Override
    public Socket accept() throws IOException {
        return mServerSocket.accept();
    }

    @Override
    public DatagramSocket getDatagramSocket() {
        return null;
    }

    @Override
    public InetAddress getInetAddress() {
        return mServerSocket.getInetAddress();
    }

    @Override
    public int getPort() {
        return mServerSocket.getLocalPort();
    }

    @Override
    public void close() throws IOException {
        if (mServerSocket != null) {
            mServerSocket.close();
        }
    }

    @Override
    public boolean isClosed() {
        return mServerSocket != null && mServerSocket.isClosed();
    }


}
