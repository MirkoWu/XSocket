package com.mirkowu.xsocket.server.client;

import com.mirkowu.xsocket.server.IClient;
import com.mirkowu.xsocket.server.IClientSocketListener;
import com.mirkowu.xsocket.server.ServerOptions;

import java.net.InetAddress;
import java.net.Socket;

public abstract class AbsClient implements IClient, IClientSocketListener {
    protected Socket mSocket;
    protected ServerOptions mServerOptions;
    protected InetAddress mInetAddress;
    protected String mUniqueTag;

    protected volatile boolean isCallReady;
    protected volatile boolean isCallDead;

    public AbsClient(Socket socket, ServerOptions serverOptions) {
        this.mSocket = socket;
        this.mServerOptions = serverOptions;

        this.mInetAddress = socket.getInetAddress();
//        this.mReaderProtocol = mOkServerOptions.getReaderProtocol();
        mUniqueTag = getHostIp() + ":" + getHostPort() + "-" + System.currentTimeMillis() + "-" + System.nanoTime();

    }

    @Override
    public String getHostIp() {
        return mInetAddress.getHostAddress();
    }

    @Override
    public int getHostPort() {
        return mSocket.getPort();
    }

    @Override
    public String getHostName() {
        return mInetAddress.getCanonicalHostName();
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
