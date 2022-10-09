package com.mirkowu.xsocket.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class UdpServer implements IServerSocket{
    DatagramSocket mServerSocket;
    int mServerPort;
    @Override
    public void createServerSocket(int serverPort) throws IOException {
        this.mServerPort=serverPort;
        this.mServerSocket=new DatagramSocket(serverPort);
    }

    @Override
    public Socket accept() throws IOException{
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
