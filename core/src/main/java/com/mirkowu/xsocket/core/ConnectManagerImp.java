package com.mirkowu.xsocket.core;

import com.mirkowu.xsocket.core.exception.ManualCloseException;
import com.mirkowu.xsocket.core.io.DuplexReceiverThread;
import com.mirkowu.xsocket.core.io.DuplexSenderThread;
import com.mirkowu.xsocket.core.io.IOThreadManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

public class ConnectManagerImp implements IConnectManager {

    // ISocket socket;
    IPConfig ipConfig;


    Options options;
    Socket socket;
    volatile boolean isDisconnecting;
    IOThreadManager ioThreadManager;
    ConnectThread connectThread;

    public ConnectManagerImp(IPConfig config) {
        this(config, Options.defaultOptions());
    }

    public ConnectManagerImp(IPConfig config, Options options) {
        this.ipConfig = config;
        this.options = options;
    }


    @Override
    public void connect() {
        connectThread = new ConnectThread(" Connect thread for ");
        connectThread.setDaemon(true);
        connectThread.start();
    }


    private class ConnectThread extends Thread {
        public ConnectThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            if (ipConfig != null) {

                OutputStream outputStream = null;
                InputStream inputStream = null;
                int TIME_OUT = 60 * 1000;
                try {
                    socket = new Socket();

                    socket.setReuseAddress(true);//复用端口
                    socket.setKeepAlive(true);

                    socket.setSoTimeout(TIME_OUT);
                    SocketAddress socketAddress = new InetSocketAddress(ipConfig.ip, ipConfig.port);
                    socket.connect(socketAddress, TIME_OUT);

                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ioThreadManager = new IOThreadManager(inputStream, outputStream, options);
                ioThreadManager.start();
            }
        }
    }

    public void send(ISendable sendable) {
        if (ioThreadManager != null && sendable != null && isConnected()) {
            ioThreadManager.send(sendable);
        }
    }

    @Override
    public void disconnect() {
        disconnect(new ManualCloseException());
    }

    @Override
    public void disconnect(Exception e) {
        synchronized (this) {
            if (isDisconnecting) {
                return;
            }
            isDisconnecting = true;
        }

        DisconnectThread disconnectThread = new DisconnectThread(e, " Connect thread for ");
        disconnectThread.setDaemon(true);
        disconnectThread.start();
    }


    private class DisconnectThread extends Thread {
        private Exception mException;

        public DisconnectThread(Exception exception, String name) {
            super(name);
            mException = exception;
        }

        @Override
        public void run() {
            try {
                if (ioThreadManager != null) {
                    ioThreadManager.close(mException);
                }

                if (connectThread != null && connectThread.isAlive()) {
                    connectThread.interrupt();
                    try {
                        connectThread.join();
                    } catch (InterruptedException e) {
                    }
                    connectThread = null;
                }

                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                    }
                }
            } finally {
                isDisconnecting = false;
//                isConnectionPermitted = true;
//                if (!(mException instanceof UnConnectException) && mSocket != null) {
//                    mException = mException instanceof ManuallyDisconnectException ? null : mException;
//                    sendBroadcast(IAction.ACTION_DISCONNECTION, mException);
//                }
                socket = null;

//                if (mException != null) {
//                    SLog.e("socket is disconnecting because: " + mException.getMessage());
//                    if (mOptions.isDebug()) {
//                        mException.printStackTrace();
//                    }
//                }
            }
        }
    }

    @Override
    public boolean isClosed() {
        return socket == null || socket.isClosed();
    }

    @Override
    public boolean isConnected() {
        return socket != null && !socket.isClosed() && socket.isConnected();
    }

    @Override
    public boolean isDisconnecting() {
        return isDisconnecting;
    }
}
