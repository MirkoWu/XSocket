package com.mirkowu.xsocket.core;

import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.exception.ManualCloseException;
import com.mirkowu.xsocket.core.client.IOThreadManager;
import com.mirkowu.xsocket.core.action.ActionDispatcher;
import com.mirkowu.xsocket.core.listener.IClientActionListener;

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

    ActionDispatcher actionDispatcher;

    public ConnectManagerImp(IPConfig config) {
        this(config, Options.defaultOptions());
    }

    public ConnectManagerImp(IPConfig config, Options options) {
        this.ipConfig = config;
        this.options = options;
        actionDispatcher = new ActionDispatcher(this, ipConfig);
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

                int TIME_OUT = 60 * 1000;
                try {
                    socket = new Socket();

//                    socket.setReuseAddress(true);//复用端口
//                    socket.setKeepAlive(true);
//                    socket.setSoTimeout(TIME_OUT);

                    SocketAddress socketAddress = new InetSocketAddress(ipConfig.ip, ipConfig.port);

//                     socket.bind(socketAddress);
//
                    socket.connect(socketAddress, TIME_OUT);

                    XLog.e("Start connect: " + ipConfig.ip + ":" + ipConfig.port + " socket server...");
                    //关闭Nagle算法,无论TCP数据报大小,立即发送
                    socket.setTcpNoDelay(true);

                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();

                    ioThreadManager = new IOThreadManager(inputStream, outputStream, options, actionDispatcher);
                    ioThreadManager.start();

                    dispatchAction(ActionType.ACTION_CONNECT_SUCCESS);

                } catch (Exception e) {
                    dispatchAction(ActionType.ACTION_CONNECT_FAIL, new ActionBean(e));
                    e.printStackTrace();
                }
            } else {
                dispatchAction(ActionType.ACTION_CONNECT_FAIL, new ActionBean(new RuntimeException("ip参数不能为空！")));
            }
        }
    }

    @Override
    public void send(byte[] bytes) {
        if (ioThreadManager != null && bytes != null && isConnected()) {
            ioThreadManager.send(bytes);
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
                        e.printStackTrace();
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
//                    mException = mException instanceof ManualCloseException ? null : mException;
                dispatchAction(ActionType.ACTION_DISCONNECT, new ActionBean(mException));
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


    @Override
    public void registerActionListener(IClientActionListener listener) {
        actionDispatcher.registerClientActionListener(listener);
    }

    @Override
    public void unRegisterActionListener(IClientActionListener listener) {
        actionDispatcher.unRegisterClientActionListener(listener);
    }

    void dispatchAction(int action) {
        actionDispatcher.dispatch(action);
    }

    void dispatchAction(int action, ActionBean actionBean) {
        actionDispatcher.dispatch(action, actionBean);
    }


}
