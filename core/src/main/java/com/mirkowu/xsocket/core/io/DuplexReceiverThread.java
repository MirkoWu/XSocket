package com.mirkowu.xsocket.core.io;

import com.mirkowu.xsocket.core.IReceiver;

public class DuplexReceiverThread extends LoopThread {


    IReceiver receiver;

    public DuplexReceiverThread(IReceiver receiver) {
        super();
        this.receiver = receiver;
    }

    @Override
    protected void onLoopStart() {

    }

    @Override
    protected void onLoopExec() {
        byte[] data = receiver.receive();
    }

    @Override
    protected void onLoopEnd(Exception e) {

    }

    @Override
    public void shutdown() {
        if (receiver != null) {
            receiver.close();
        }
        super.shutdown();
    }
}
