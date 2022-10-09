package com.mirkowu.xsocket.server;

import com.mirkowu.xsocket.core.SocketType;
import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.exception.ManualCloseException;
import com.mirkowu.xsocket.core.io.LoopThread;
import com.mirkowu.xsocket.server.action.IClientStatusRegister;
import com.mirkowu.xsocket.server.client.ClientImp;
import com.mirkowu.xsocket.server.client.ClientPoolImp;
import com.mirkowu.xsocket.server.action.ServerActionDispatcher;
import com.mirkowu.xsocket.server.action.ServerActionType;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerManagerImp implements IServerManager, IActionDispatcher , IClientStatusRegister {
    private IServerSocket mServerSocket;
    private AcceptThread mAcceptThread;
    private ServerOptions mServerOptions;
    private ClientPoolImp clientPoolImp;
    private volatile boolean isInit = false;
    private ServerActionDispatcher actionDispatcher;
    private int mServerPort;
    private volatile boolean isTcp;

    public ServerManagerImp(int serverPort, ServerOptions serverOptions) {
        this.mServerPort = serverPort;
        this.mServerOptions = serverOptions;
        actionDispatcher = new ServerActionDispatcher(this);
        actionDispatcher.setServerPort(serverPort);

        isTcp = mServerOptions.getSocketType() == SocketType.TCP;
        if (isTcp) {
            mServerSocket = new TcpServer();
        } else {
            mServerSocket = new UdpServer();
        }
    }

    @Override
    public void listen() {
        // if(mServerPort)

        if (mServerOptions == null) {
            throw new IllegalArgumentException("option can not be null");
        }

        try {

            mServerSocket.createServerSocket(mServerPort);
            configuration(mServerSocket);

            if (isTcp) {
                mAcceptThread = new AcceptThread("server accepting in ");
                mAcceptThread.start();
            } else {
                clientPoolImp = new ClientPoolImp(mServerOptions.getMaxConnectCapacity());
                actionDispatcher.setClientPool(clientPoolImp);
                dispatchAction(ServerActionType.ACTION_SERVER_LISTENING);

                ClientImp client = new ClientImp(mServerOptions, ServerManagerImp.this);
                DatagramSocket socket = mServerSocket.getDatagramSocket();
                client.initUdp(socket);
                client.startSendThread();
                client.startReceiveThread();

            }


        } catch (Exception e) {
            shutdown(e);
        }
    }

    @Override
    public boolean isLive() {
        return mServerSocket != null && !mServerSocket.isClosed() &&
                (!isTcp || (isTcp && mAcceptThread != null && mAcceptThread.isRunning()));
    }

    @Override
    public IClientPool<String, IClient> getClientPool() {
        return clientPoolImp;
    }


    private void configuration(IServerSocket mServerSocket) {

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


    @Override
    public void registerClientStatusListener(IClientStatusListener listener) {
        actionDispatcher.registerClientStatusListener(listener);
    }

    @Override
    public void unRegisterClientStatusListener(IClientStatusListener listener) {
        actionDispatcher.unRegisterClientStatusListener(listener);
    }


    private class AcceptThread extends LoopThread {

        public AcceptThread(String name) {
            super(name);
        }

        @Override
        protected void onLoopStart() {
            clientPoolImp = new ClientPoolImp(mServerOptions.getMaxConnectCapacity());
            actionDispatcher.setClientPool(clientPoolImp);
            dispatchAction(ServerActionType.ACTION_SERVER_LISTENING);
        }

        @Override
        protected void onLoopExec() throws Exception {
            ClientImp client = new ClientImp(mServerOptions, ServerManagerImp.this);
            Socket socket = mServerSocket.accept();
            client.initTcp(socket, clientPoolImp);
            client.startSendThread();
            client.startReceiveThread();
        }

        @Override
        protected void onLoopEnd(Exception e) {
            //  shutdown(e);
            //   dispatchAction(ServerActionType.ACTION_SERVER_WILL_SHUTDOWN, new ActionBean(e));
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

        dispatchAction(ServerActionType.ACTION_SERVER_ALREADY_SHUTDOWN, new ActionBean(e));
    }
}
