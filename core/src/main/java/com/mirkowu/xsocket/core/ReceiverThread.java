package com.mirkowu.xsocket.core;

public class ReceiverThread extends LoopThread {


    ISocket socket;



    @Override
    protected void onLoopStart() {

    }

    @Override
    protected void onLoopExec() {
IReceiver receiver =socket.receive();

    }

    @Override
    protected void onLoopEnd(Exception e) {

    }

}
