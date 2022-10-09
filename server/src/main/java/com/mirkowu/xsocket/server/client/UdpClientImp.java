//package com.mirkowu.xsocket.server.client;
//
//import com.mirkowu.xsocket.core.IConnectable;
//import com.mirkowu.xsocket.core.IDisconnectable;
//import com.mirkowu.xsocket.core.ISendData;
//import com.mirkowu.xsocket.core.action.ActionBean;
//import com.mirkowu.xsocket.core.action.IActionDispatcher;
//import com.mirkowu.xsocket.core.exception.CacheException;
//import com.mirkowu.xsocket.server.IClient;
//import com.mirkowu.xsocket.server.IClientIOListener;
//import com.mirkowu.xsocket.server.IClientSocketListener;
//import com.mirkowu.xsocket.server.ServerOptions;
//import com.mirkowu.xsocket.server.action.ClientActionDispatcher;
//import com.mirkowu.xsocket.server.action.ServerActionType;
//import com.mirkowu.xsocket.server.io.ClientIOManager;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.DatagramSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.List;
//
//public class UdpClientImp implements IClient,IClientSocketListener {
//    private volatile boolean isDead;
//    private volatile boolean isReadThreadStarted;
//
//    private ServerOptions mServerOptions;
//    private ClientIOManager ioManager;
//    private IActionDispatcher clientActionDispatcher;
//    private IActionDispatcher serverActionDispatcher;
//
//    DatagramSocket datagramSocket;
//    private volatile List<IClientIOListener> clientIOListenerList = new ArrayList<>();
//
//    public UdpClientImp(DatagramSocket datagramSocket, ServerOptions serverOptions, IActionDispatcher serverActionDispatcher) {
//        this.datagramSocket = datagramSocket;
//        this.mServerOptions = serverOptions;
//        this.serverActionDispatcher = serverActionDispatcher;
//        clientActionDispatcher = new ClientActionDispatcher(this);
//
//        try {
//            initIOManager();
//        } catch (IOException e) {
//            disconnect(e);
//        }
//    }
//
//
//    private void initIOManager() throws IOException {
//        ioManager = new ClientIOManager(datagramSocket, mServerOptions, clientActionDispatcher);
//    }
//
//    public void startSendThread() {
//        if (ioManager != null) {
//            synchronized (ioManager) {
//                ioManager.startSendThread();
//            }
//        }
//    }
//
//    @Override
//    public void connect() {
//
//    }
//
//    @Override
//    public void disconnect() {
//        disconnect(null);
//    }
//
//    @Override
//    public void disconnect(Exception e) {
//        if (ioManager != null) {
//            synchronized (ioManager) {
//                ioManager.close(e);
//            }
//        } else {
//            onClientDead(e);
//        }
//
//            synchronized (datagramSocket) {
//                datagramSocket.close();
//            }
//
//
//        removeAllClientIOListener();
//        isReadThreadStarted = false;
//    }
//
//
//    @Override
//    protected void onClientReady() {
//        if (isDead) {
//            return;
//        }
////        if (clientPool != null) {
////            clientPool.cache(this);
////        }
//        serverActionDispatcher.dispatchAction(ServerActionType.Server.ACTION_CLIENT_CONNECTED, new ActionBean(this));
//    }
//
//    @Override
//    protected synchronized void onClientDead(Exception e) {
//        if (isDead) {
//            return;
//        }
////        if (!(e instanceof CacheException)) {
////            clientPool.unCache(this);
////        }
//        if (e != null) {
//            if (mServerOptions.isDebug()) {
//                e.printStackTrace();
//            }
//        }
//        disconnect(e);
//
//        isDead = true;
//
//        serverActionDispatcher.dispatchAction(ServerActionType.Server.ACTION_CLIENT_DISCONNECTED, new ActionBean(this));
//
//    }
//
//    @Override
//    public void send(ISendData sendData) {
//        if (ioManager != null) {
//            ioManager.send(sendData);
//        }
//    }
//
//
//    @Override
//    public void onClientReceive(byte[] bytes) {
//        for (IClientIOListener listener : clientIOListenerList) {
//            try {
//                listener.onReceiveFromClient(bytes, this, null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void onClientSend(ISendData sendData) {
//        for (IClientIOListener listener : clientIOListenerList) {
//            try {
//                listener.onSendToClient(sendData, this, null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void addClientIOListener(IClientIOListener listener) {
//        if (isDead) {
//            return;
//        }
//        synchronized (clientIOListenerList) {
//            if (!clientIOListenerList.contains(listener)) {
//                clientIOListenerList.add(listener);
//            }
//        }
//        synchronized (ioManager) {
//            if (!isReadThreadStarted) {
//                isReadThreadStarted = true;
//                ioManager.startReceiveThread();
//            }
//        }
//    }
//
//    @Override
//    public void removeClientIOListener(IClientIOListener listener) {
//        synchronized (clientIOListenerList) {
//            clientIOListenerList.remove(listener);
//        }
//    }
//
//    @Override
//    public void removeAllClientIOListener() {
//        synchronized (clientIOListenerList) {
//            clientIOListenerList.clear();
//        }
//    }
//}
