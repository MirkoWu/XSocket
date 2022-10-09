package com.mirkowu.xsocket.client.connect;

import com.mirkowu.xsocket.core.IPConfig;
import com.mirkowu.xsocket.client.Options;
import com.mirkowu.xsocket.core.SocketType;
import com.mirkowu.xsocket.core.ISendData;
import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.client.dispatcher.ActionDispatcher;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.client.io.IOThreadManager;
import com.mirkowu.xsocket.client.listener.ISocketListener;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.exception.ManualCloseException;
import com.mirkowu.xsocket.core.io.IIOManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;

public class ConnectManagerImp implements IConnectManager {

    private IPConfig ipConfig;
    private Options options;
    private volatile IClientSocket socketClient;
    private volatile boolean isDisconnecting = false;
    private volatile boolean isAllowConnect = true;
    private IIOManager ioThreadManager;
    private ConnectThread connectThread;
    private ActionDispatcher actionDispatcher;
    private AbsReconnectionManager reconnectManager;
    private PulseManager pulseManager;

    public ConnectManagerImp(IPConfig config, Options options) {
        this.ipConfig = config;
        this.options = options;
        actionDispatcher = new ActionDispatcher(this, ipConfig);
        pulseManager = new PulseManager(this, options);
        if (options.getSocketType() == SocketType.TCP) {
            socketClient = new TcpClient(config, options);
        } else {
            socketClient = new UdpClient(config, options);
        }
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
            try {
                socketClient.createSocket();

                if (options.getSocketType() == SocketType.TCP) {
                    InputStream inputStream = socketClient.getInputStream();
                    OutputStream outputStream = socketClient.getOutputStream();
                    ioThreadManager = new IOThreadManager(inputStream, outputStream, options, actionDispatcher);
                } else {
                    DatagramSocket datagramSocket = socketClient.getDatagramSocket();
                    ioThreadManager = new IOThreadManager(datagramSocket,ipConfig, options, actionDispatcher);
                }
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
    public void send(ISendData sendData) {
        XLog.e("ConnectManagerImp send");

        if (ioThreadManager != null && sendData != null && isConnected()) {
            ioThreadManager.send(sendData);
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

                if (socketClient != null) {
                    try {
                        socketClient.close();
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
                socketClient = null;

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
        return socketClient == null || socketClient.isClosed();
    }

    @Override
    public boolean isConnected() {
        return socketClient != null &&  socketClient.isConnected();
    }

    @Override
    public boolean isDisconnecting() {
        return isDisconnecting;
    }

    @Override
    public IActionDispatcher getActionDispatcher() {
        return actionDispatcher;
    }

    @Override
    public PulseManager getPulseManager() {
        return pulseManager;
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
