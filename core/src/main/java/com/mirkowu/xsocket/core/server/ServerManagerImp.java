package com.mirkowu.xsocket.core.server;

import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.exception.ManualCloseException;
import com.mirkowu.xsocket.core.io.LoopThread;
import com.mirkowu.xsocket.core.listener.IServerSocketListener;
import com.mirkowu.xsocket.core.server.action.ServerActionDispatcher;
import com.mirkowu.xsocket.core.server.action.ServerActionType;
import com.mirkowu.xsocket.core.server.client.ClientImp;
import com.mirkowu.xsocket.core.server.client.ClientPoolImp;
import com.mirkowu.xsocket.core.server.client.IClient;
import com.mirkowu.xsocket.core.server.client.IClientPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerManagerImp implements IServerManager, IActionDispatcher {
    private ServerSocket mServerSocket;
    private AcceptThread mAcceptThread;
    private ServerOptions mServerOptions;
    private ClientPoolImp clientPoolImp;
    private volatile boolean isInit = false;
    private ServerActionDispatcher actionDispatcher;
    private int mServerPort;

    public ServerManagerImp(int serverPort, ServerOptions serverOptions) {
        this.mServerPort = serverPort;
        this.mServerOptions = serverOptions;
        actionDispatcher = new ServerActionDispatcher(this);
        actionDispatcher.setServerPort(serverPort);
    }

    @Override
    public void listen() {
        // if(mServerPort)

        if (mServerOptions == null) {
            throw new IllegalArgumentException("option can not be null");
        }

        try {
            mServerSocket = new ServerSocket(mServerPort);
            configuration(mServerSocket);
            mAcceptThread = new AcceptThread("server accepting in ");
            mAcceptThread.start();
        } catch (Exception e) {
            shutdown(e);
        }
    }

    @Override
    public boolean isLive() {
        return mServerSocket != null && !mServerSocket.isClosed()
                && mAcceptThread != null && mAcceptThread.isRunning();
    }

    @Override
    public IClientPool<String, IClient> getClientPool() {
        return clientPoolImp;
    }


    private void configuration(ServerSocket mServerSocket) {

    }

    @Override
    public void dispatchAction(int action) {
        actionDispatcher.dispatchAction(action);
    }

    @Override
    public void dispatchAction(int action, ActionBean bean) {
        actionDispatcher.dispatchAction(action, bean);
    }


    @Override
    public void registerSocketListener(IServerSocketListener listener) {
        actionDispatcher.registerSocketListener(listener);
    }

    @Override
    public void unRegisterSocketListener(IServerSocketListener listener) {
        actionDispatcher.unRegisterSocketListener(listener);
    }


    private class AcceptThread extends LoopThread {

        public AcceptThread(String name) {
            super(name);
        }

        @Override
        protected void onLoopStart() {
            clientPoolImp = new ClientPoolImp(mServerOptions.getMaxConnectCapacity());
            actionDispatcher.setClientPool(clientPoolImp);
            dispatchAction(ServerActionType.Server.ACTION_SERVER_LISTENING);
        }

        @Override
        protected void onLoopExec() throws Exception {
            Socket socket = mServerSocket.accept();
            ClientImp client = new ClientImp(socket, mServerOptions, clientPoolImp, ServerManagerImp.this);
            //clientPoolImp.cache(client);

            client.startSendThread();
        }

        @Override
        protected void onLoopEnd(Exception e) {
          //  shutdown(e);
            //   dispatchAction(ServerActionType.Server.ACTION_SERVER_WILL_SHUTDOWN, new ActionBean(e));
        }
    }

    @Override
    public void shutdown() {
        shutdown(new ManualCloseException());
    }


    private void shutdown(Exception e) {
        if (mServerSocket == null && e instanceof ManualCloseException) {
            e = new IllegalStateException("ServerSocket already shutdown!");
        }

        if (clientPoolImp != null) {
            clientPoolImp.serverShutdown();
        }
        try {
            if (mServerSocket != null) {
                mServerSocket.close();
            }
        } catch (IOException e1) {
        }
        mServerSocket = null;
        clientPoolImp = null;
        if (mAcceptThread != null) {
            mAcceptThread.shutdown(e);
            mAcceptThread = null;
        }

        dispatchAction(ServerActionType.Server.ACTION_SERVER_ALREADY_SHUTDOWN, new ActionBean(e));
    }
}
