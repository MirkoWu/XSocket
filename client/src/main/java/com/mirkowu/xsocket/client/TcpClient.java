package com.mirkowu.xsocket.client;

import com.mirkowu.xsocket.client.connect.IClientSocket;
import com.mirkowu.xsocket.core.IPConfig;
import com.mirkowu.xsocket.client.Options;
import com.mirkowu.xsocket.core.XLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class TcpClient implements IClientSocket {

    private IPConfig ipConfig;
    private Options options;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;
    private int TIME_OUT = 60 * 1000;

    public TcpClient(IPConfig ipConfig, Options options) {
        this.ipConfig = ipConfig;
        this.options = options;
    }


    @Override
    public void createSocket() throws Exception {
        socket = new Socket();
        socket.setReuseAddress(true);//复用端口
        socket.setKeepAlive(true);
        socket.setSoTimeout(TIME_OUT);
        //关闭Nagle算法,无论TCP数据报大小,立即发送
        socket.setTcpNoDelay(true);
        SocketAddress socketAddress = new InetSocketAddress(ipConfig.ip, ipConfig.port);

//       socket.bind(socketAddress);
//
        socket.connect(socketAddress, TIME_OUT);

        XLog.e("Start connect: " + ipConfig.ip + ":" + ipConfig.port + " socket server...");


        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public DatagramSocket getDatagramSocket() {
        return null;
    }

    @Override
    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }

    @Override
    public boolean isClosed() {
        return socket != null && socket.isClosed();
    }

    @Override
    public boolean isConnected() {
        return socket != null && !socket.isClosed() && socket.isConnected();
    }
}
