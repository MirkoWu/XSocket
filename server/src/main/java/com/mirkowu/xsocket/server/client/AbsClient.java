package com.mirkowu.xsocket.server.client;

import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.server.IClient;
import com.mirkowu.xsocket.server.IClientSocketListener;
import com.mirkowu.xsocket.server.ServerOptions;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public abstract class AbsClient implements IClient, IClientSocketListener {
    protected DatagramSocket datagramSocket;
    protected Socket mSocket;
//    protected ServerOptions mServerOptions;
    protected volatile String hostName;
    protected volatile String hostIp;
    protected volatile int  port=-1;
//    protected InetAddress mInetAddress;

    protected String mUniqueTag;

    protected volatile boolean isCallReady;
    protected volatile boolean isCallDead;
    protected volatile ClientPoolImp clientPool;

    public void initTcp(Socket socket,  ClientPoolImp clientPool  ) {
        this.mSocket = socket;
        this.clientPool = clientPool;

//        this.mReaderProtocol = mOkServerOptions.getReaderProtocol();

                 InetAddress  inetAddress= socket.getInetAddress();
        hostName=  inetAddress.getCanonicalHostName()  ;
        hostIp=  inetAddress.getHostAddress()  ;
        port=  socket.getPort()  ;
        mUniqueTag = getHostIp() + ":" + getHostPort() + "-" + System.currentTimeMillis() + "-" + System.nanoTime();

    }

    public void initUdp(DatagramSocket socket ) {
        this.datagramSocket = socket;


//        this.mInetAddress = socket.getInetAddress();
////        this.mReaderProtocol = mOkServerOptions.getReaderProtocol();
//        mUniqueTag = getHostIp() + ":" + getHostPort() + "-" + System.currentTimeMillis() + "-" + System.nanoTime();

    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public String getHostIp() {
        return hostIp;
    }

    @Override
    public int getHostPort() {
        return port;
    }


    @Override
    public String getUniqueTag() {
        if (isEmpty(mUniqueTag)) {
            return getHostIp() + ":" + getHostPort();
        }
        return mUniqueTag;
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    @Override
    public void onClientSendReady() {
        if (!isCallReady) {
            onClientReady();
            isCallReady = true;
            isCallDead = false;
        }
    }

    @Override
    public void onClientReceiveReady() {
        if (!isCallReady) {
            onClientReady();
            isCallReady = true;
            isCallDead = false;
        }
    }

    @Override
    public void onClientSendDead(Exception e) {
        if (!isCallDead) {
            onClientDead(e);
            isCallReady = false;
            isCallDead = true;
        }
    }

    @Override
    public void onClientReceiveDead(Exception e) {
        if (!isCallDead) {
            onClientDead(e);
            isCallReady = false;
            isCallDead = true;
        }
    }

    protected abstract void onClientReady();

    protected abstract void onClientDead(Exception e);
}
