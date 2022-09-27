package com.mirkowu.xsocket.core.io;

import com.mirkowu.xsocket.core.IReceiver;
import com.mirkowu.xsocket.core.ISender;

public class SimplexThread extends LoopThread {
    IReceiver receiver;
    ISender sender;

    public SimplexThread(IReceiver receiver, ISender sender) {
        super();
        this.receiver = receiver;
        this.sender = sender;
    }

    @Override
    protected void onLoopStart() {

    }

    @Override
    protected void onLoopExec() {
        if (sender != null) {
            boolean result = sender.send();
            if (result) {
                if (receiver != null) {
                    byte[] data = receiver.receive();
                }
            }
        }

    }

    @Override
    protected void onLoopEnd(Exception e) {

    }

    @Override
    public void shutdown( ) {
        if (receiver != null) {
            receiver.close();
        }
        if (sender != null) {
            sender.close();
        }
        super.shutdown( );
    }
}
