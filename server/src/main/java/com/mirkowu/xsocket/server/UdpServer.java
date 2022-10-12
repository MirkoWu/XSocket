package com.mirkowu.xsocket.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class UdpServer implements IServerSocket {
    private DatagramSocket mServerSocket;
    private int mServerPort;

    @Override
    public void createServerSocket(int serverPort) throws IOException {
        this.mServerPort = serverPort;
        this.mServerSocket = new DatagramSocket(null);
        mServerSocket.setReuseAddress(true);//复用端口
        mServerSocket.bind(new InetSocketAddress(serverPort));
    }

    @Override
    public Socket accept() throws IOException {
        return null;
    }

    @Override
    public DatagramSocket getDatagramSocket() {
        return mServerSocket;
    }

    @Override
    public InetAddress getInetAddress() {
        return mServerSocket.getInetAddress();
    }

    @Override
    public int getPort() {
        return mServerSocket.getPort();
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
