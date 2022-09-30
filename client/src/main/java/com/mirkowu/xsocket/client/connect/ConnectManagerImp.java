package com.mirkowu.xsocket.client.connect;

import com.mirkowu.xsocket.client.IPConfig;
import com.mirkowu.xsocket.client.Options;
import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.client.dispatcher.ActionDispatcher;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.client.io.IOThreadManager;
import com.mirkowu.xsocket.client.listener.ISocketListener;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.exception.ManualCloseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ConnectManagerImp implements IConnectManager {

    // ISocket socket;
  private  IPConfig ipConfig;
  private  Options options;
  private  Socket socket;
  private  volatile boolean isDisconnecting = false;
  private  volatile boolean isAllowConnect = true;
  private  IOThreadManager ioThreadManager;
  private  ConnectThread connectThread;
  private  ActionDispatcher actionDispatcher;
  private  AbsReconnectionManager reconnectManager;

    public ConnectManagerImp(IPConfig config) {
        this(config, Options.defaultOptions());
    }

    public ConnectManagerImp(IPConfig config, Options options) {
        this.ipConfig = config;
        this.options = options;
        actionDispatcher = new ActionDispatcher(this, ipConfig);
      //  reconnectManager = new DefaultReconnectManager(actionDispatcher);
    }


    @Override
    public synchronized void connect() {
        if (!isAllowConnect) {
            return;
        }
        isAllowConnect = false;
        if (isConnected()) {
            return;
        }
        isDisconnecting = false;

        if (ipConfig == null) {
            throw new RuntimeException(new RuntimeException("IpConfig参数不能为空！"));
        }

        if (reconnectManager != null) {
            reconnectManager.detach();
            XLog.i("ReconnectionManager is detached.");
        }
        reconnectManager = options.getReconnectionManager();
        if (reconnectManager != null) {
            reconnectManager.attach(this);
            XLog.i("ReconnectionManager is attached.");
        }


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
            } finally {
                isAllowConnect = true;
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

        if (e instanceof ManualCloseException) {
            if (reconnectManager != null) {
                reconnectManager.detach();
                XLog.i("ReconnectionManager is detached.");
            }
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
                isAllowConnect = true;

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
    public IActionDispatcher getActionDispatcher(){
        return actionDispatcher;
    }

    @Override
    public void registerSocketListener(ISocketListener listener) {
        actionDispatcher.registerSocketListener(listener);
    }

    @Override
    public void unRegisterSocketListener(ISocketListener listener) {
        actionDispatcher.unRegisterSocketListener(listener);
    }

    void dispatchAction(int action) {
        actionDispatcher.dispatchAction(action);
    }

    void dispatchAction(int action, ActionBean actionBean) {
        actionDispatcher.dispatchAction(action, actionBean);
    }



}
