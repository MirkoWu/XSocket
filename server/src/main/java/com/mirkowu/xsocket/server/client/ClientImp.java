package com.mirkowu.xsocket.server.client;

import com.mirkowu.xsocket.core.IPConfig;
import com.mirkowu.xsocket.core.data.ISendData;
import com.mirkowu.xsocket.core.SocketType;
import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.exception.CacheException;
import com.mirkowu.xsocket.server.listener.IClientIOListener;
import com.mirkowu.xsocket.server.ServerOptions;
import com.mirkowu.xsocket.server.io.ClientIOManager;
import com.mirkowu.xsocket.server.action.ClientActionDispatcher;
import com.mirkowu.xsocket.server.action.ServerActionType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientImp extends AbsClient {
    private volatile boolean isDead;
    private volatile boolean isReadThreadStarted;

    private ClientIOManager ioManager;
    private IActionDispatcher clientActionDispatcher;
    private IActionDispatcher serverActionDispatcher;

    private volatile ServerOptions mServerOptions;

    public ClientImp(ServerOptions serverOptions, IActionDispatcher serverActionDispatcher) {
        this.mServerOptions = serverOptions;
        this.serverActionDispatcher = serverActionDispatcher;
        this.clientActionDispatcher = new ClientActionDispatcher(this);
    }

    @Override
    public void initTcp(Socket socket, ClientPoolImp clientPool) {
        super.initTcp(socket, clientPool);
        try {
            InputStream inputStream = mSocket.getInputStream();
            OutputStream outputStream = mSocket.getOutputStream();
            ioManager = new ClientIOManager(inputStream, outputStream, mServerOptions, clientActionDispatcher);

        } catch (IOException e) {
            disconnect(e);
        }
    }

    @Override
    public void initUdp(DatagramSocket socket) {
        super.initUdp(socket);
        ioManager = new ClientIOManager(datagramSocket, mServerOptions, clientActionDispatcher);
    }

    public void startSendThread() {
        if (ioManager != null) {
            synchronized (ioManager) {
                ioManager.startSendThread();
            }
        }
    }

    public void startReceiveThread() {
        synchronized (ioManager) {
            if (!isReadThreadStarted) {
                isReadThreadStarted = true;
                ioManager.startReceiveThread();
            }
        }
    }

    @Override
    public void connect() {

    }

    @Override
    public SocketType getSocketType() {
        return mServerOptions.getSocketType();
    }

    @Override
    public void disconnect() {
        disconnect(null);
    }

    @Override
    public void disconnect(Exception e) {
        if (ioManager != null) {
            synchronized (ioManager) {
                ioManager.close(e);
            }
        } else {
            onClientDead(e);
        }
        try {
            if (mSocket != null) {
                synchronized (mSocket) {
                    mSocket.close();
                }
            }
            if (datagramSocket != null) {
                synchronized (datagramSocket) {
                    datagramSocket.close();
                }
            }

        } catch (IOException e1) {
        }

        isReadThreadStarted = false;
    }


    @Override
    protected void onClientReady() {
        if (isDead) {
            return;
        }
        if (clientPool != null) {
            clientPool.cache(this);
        }
        serverActionDispatcher.dispatchAction(ServerActionType.ACTION_CLIENT_CONNECTED, new ActionBean(this));
    }

    @Override
    protected synchronized void onClientDead(Exception e) {
        if (isDead) {
            return;
        }
        if (clientPool != null && !(e instanceof CacheException)) {
            clientPool.unCache(this);
        }
        if (e != null) {
            if (mServerOptions.isDebug()) {
                e.printStackTrace();
            }
        }
        disconnect(e);

        isDead = true;

        serverActionDispatcher.dispatchAction(ServerActionType.ACTION_CLIENT_DISCONNECTED, new ActionBean(this, e));

    }

    @Override
    public void send(ISendData sendData) {
        if (ioManager != null) {
            ioManager.send(sendData);
        }
    }


    @Override
    public void onClientReceive(byte[] bytes, IPConfig config) {
        if (getSocketType() == SocketType.UDP) {
            this.hostIp = config.ip;
            this.port = config.port;
        }
        serverActionDispatcher.dispatchAction(ActionType.ACTION_RECEIVE, new ActionBean(bytes, this));
    }

    @Override
    public void onClientSend(ISendData sendData, IPConfig config) {
        if (getSocketType() == SocketType.UDP) {
            this.hostIp = config.ip;
            this.port = config.port;
        }
        serverActionDispatcher.dispatchAction(ActionType.ACTION_SEND, new ActionBean(sendData, this));
    }

}
