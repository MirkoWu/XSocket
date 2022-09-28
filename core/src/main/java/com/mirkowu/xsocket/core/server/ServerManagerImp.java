package com.mirkowu.xsocket.core.server;

import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.io.LoopThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerManagerImp implements IServerManager {
    ServerSocket mServerSocket;
    AcceptThread mAcceptThread;


    public void start() {
        try {
            mServerSocket = new ServerSocket(8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
        configuration(mServerSocket);
        mAcceptThread = new AcceptThread("server accepting in ");
        mAcceptThread.start();
    }

    private void configuration(ServerSocket mServerSocket) {

    }


    private class AcceptThread extends LoopThread {

        public AcceptThread(String name) {
            super(name);
        }

        @Override
        protected void onLoopStart() {
            XLog.e("Server onLoopStart");

        }

        @Override
        protected void onLoopExec() {
            try {
                Socket socket = mServerSocket.accept();
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
//              inputStream.read()



                outputStream.write("accept hello".getBytes());
                outputStream.flush();
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

//        if (mClientPoolImpl != null) {
//            mClientPoolImpl.serverDown();
//        }

        try {
            mServerSocket.close();
        } catch (IOException e) {
        }

        mServerSocket = null;
//        mClientPoolImpl = null;
//        mAcceptThread.shutdown(new InitiativeDisconnectException());
        mAcceptThread = null;

//        sendBroadcast(IAction.Server.ACTION_SERVER_ALLREADY_SHUTDOWN);
    }
}
