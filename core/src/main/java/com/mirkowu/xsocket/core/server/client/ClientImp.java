package com.mirkowu.xsocket.core.server.client;

import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.exception.CacheException;
import com.mirkowu.xsocket.core.server.ServerOptions;
import com.mirkowu.xsocket.core.server.action.ClientActionDispatcher;
import com.mirkowu.xsocket.core.server.action.ServerActionType;
import com.mirkowu.xsocket.core.server.io.ClientIOManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientImp extends AbsClient {
    private volatile boolean isDead;
    private volatile boolean isReadThreadStarted;

    private ClientIOManager ioManager;
    private IActionDispatcher clientActionDispatcher;
    private IActionDispatcher serverActionDispatcher;

    private volatile ClientPoolImp clientPool;

    private volatile List<IClientIOListener> clientIOListenerList = new ArrayList<>();

    public ClientImp(Socket socket, ServerOptions serverOptions, ClientPoolImp clientPool, IActionDispatcher serverActionDispatcher) {
        super(socket, serverOptions);
        this.clientPool = clientPool;
        this.serverActionDispatcher = serverActionDispatcher;
        clientActionDispatcher = new ClientActionDispatcher(this);

        try {
            initIOManager();
        } catch (IOException e) {
            disconnect(e);
        }

    }

    private void initIOManager() throws IOException {
        InputStream inputStream = mSocket.getInputStream();
        OutputStream outputStream = mSocket.getOutputStream();
        ioManager = new ClientIOManager(inputStream, outputStream, mServerOptions, clientActionDispatcher);
    }

    public void startSendThread() {
        if (ioManager != null) {
            synchronized (ioManager) {
                ioManager.startSendThread();
            }
        }
    }

    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {
        disconnect(null);
    }

    @Override
    public void disconnect(Exception e) {
        if (ioManager != null) {
            ioManager.close(e);
        } else {
            onClientDead(e);
        }
        try {
            synchronized (mSocket) {
                mSocket.close();
            }
        } catch (IOException e1) {
        }

        removeAllClientIOListener();
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
        serverActionDispatcher.dispatchAction(ServerActionType.Server.ACTION_CLIENT_CONNECTED, new ActionBean(this));
    }

    @Override
    protected synchronized void onClientDead(Exception e) {
        if (isDead) {
            return;
        }
        if (!(e instanceof CacheException)) {
            clientPool.unCache(this);
        }
        if (e != null) {
            if (mServerOptions.isDebug()) {
                e.printStackTrace();
            }
        }
        disconnect(e);

        isDead = true;

        serverActionDispatcher.dispatchAction(ServerActionType.Server.ACTION_CLIENT_DISCONNECTED, new ActionBean(this));

    }

    @Override
    public void send(byte[] bytes) {
        if (ioManager != null) {
            ioManager.send(bytes);
        }
    }



    @Override
    public void onClientReceive(byte[] bytes) {
        for (IClientIOListener listener : clientIOListenerList) {
            try {
                listener.onReceiveFromClient(bytes, this, clientPool);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClientSend(byte[] bytes) {
        for (IClientIOListener listener : clientIOListenerList) {
            try {
                listener.onSendToClient(bytes, this, clientPool);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addClientIOListener(IClientIOListener listener) {
        if (isDead) {
            return;
        }
        synchronized (clientIOListenerList) {
            if (!clientIOListenerList.contains(listener)) {
                clientIOListenerList.add(listener);
            }
        }
        synchronized (ioManager) {
            if (!isReadThreadStarted) {
                isReadThreadStarted = true;
                ioManager.startReceiveThread();
            }
        }
    }

    @Override
    public void removeClientIOListener(IClientIOListener listener) {
        synchronized (clientIOListenerList) {
            clientIOListenerList.remove(listener);
        }
    }

    @Override
    public void removeAllClientIOListener() {
        synchronized (clientIOListenerList) {
            clientIOListenerList.clear();
        }
    }
}
