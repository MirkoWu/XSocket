package com.mirkowu.xsocket.core.server;

import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.exception.ManualCloseException;
import com.mirkowu.xsocket.core.io.LoopThread;
import com.mirkowu.xsocket.core.listener.IRegister;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerManagerImp implements IServerManager, IRegister<IServerActionDispatcher> {
    private ServerSocket mServerSocket;
    private AcceptThread mAcceptThread;
    private ServerOptions mServerOptions;
    private ClientPoolImp clientPoolImp;
    private volatile boolean isInit = false;
    private IServerActionDispatcher actionDispatcher;
    private int  mServerPort;

    public ServerManagerImp(int serverPort,ServerOptions serverOptions) {
        this.mServerPort=serverPort;
        this.mServerOptions=serverOptions;
        actionDispatcher = new ServerActionDispatcher(this);
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
        } catch (IOException e) {
            shutdown();
        }
    }

    @Override
    public boolean isLive() {
        return mServerSocket != null && mServerSocket.isClosed()
                && mAcceptThread != null && mAcceptThread.isRunning();
    }

    @Override
    public IClientPool<String, IClient> getClientPool() {
        return clientPoolImp;
    }


    private void configuration(ServerSocket mServerSocket) {

    }

    @Override
    public void registerSocketListener(IServerActionDispatcher listener) {
        actionDispatcher.registerSocketListener(listener);
    }

    @Override
    public void unRegisterSocketListener(IServerActionDispatcher listener) {
        actionDispatcher.unRegisterSocketListener(listener);
    }

    public void dispatchAction(int action) {
        actionDispatcher.dispatch(action);
    }

    public void dispatchAction(int action, ActionBean actionBean) {
        actionDispatcher.dispatch(action, actionBean);
    }


    private class AcceptThread extends LoopThread {

        public AcceptThread(String name) {
            super(name);
        }

        @Override
        protected void onLoopStart() {
            XLog.e("Server onLoopStart");
            clientPoolImp = new ClientPoolImp(mServerOptions.getMaxConnectCapacity());

        }

        @Override
        protected void onLoopExec() {
            try {
                Socket socket = mServerSocket.accept();

                ClientImp client = new ClientImp(socket, mServerOptions);
                client.startSendThread();

                XLog.e("Server client accept :" + socket.getLocalAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onLoopEnd(Exception e) {
            String msg = e != null ? e.toString() : "null";
            XLog.e("Server onLoopEnd :" + msg);

        }
    }

    @Override
    public void shutdown() {
        if (mServerSocket == null) {
            return;
        }

        if (clientPoolImp != null) {
            clientPoolImp.serverShutdown();
        }

        try {
            mServerSocket.close();
        } catch (IOException e) {
        }

        mServerSocket = null;
        clientPoolImp = null;
        mAcceptThread.shutdown(new ManualCloseException());
        mAcceptThread = null;

//        sendBroadcast(IAction.Server.ACTION_SERVER_ALLREADY_SHUTDOWN);
    }
}
