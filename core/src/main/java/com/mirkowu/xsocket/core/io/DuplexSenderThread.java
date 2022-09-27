package com.mirkowu.xsocket.core.io;

import com.mirkowu.xsocket.core.ISender;

public class DuplexSenderThread extends LoopThread {

    ISender sender;

   public DuplexSenderThread(ISender sender) {
       super();
       this.sender = sender;
    }

    @Override
    protected void onLoopStart() {

    }

    @Override
    protected void onLoopExec() {
        if (sender != null) {
            boolean result = sender.send();
        }
    }

    @Override
    protected void onLoopEnd(Exception e) {

    }

    @Override
    public void shutdown( ) {

        if (sender != null) {
            sender.close();
        }
        super.shutdown( );
    }
}
