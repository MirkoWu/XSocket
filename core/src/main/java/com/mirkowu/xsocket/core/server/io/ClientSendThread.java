package com.mirkowu.xsocket.core.server.io;

import com.mirkowu.xsocket.core.ISender;
import com.mirkowu.xsocket.core.io.LoopThread;
import com.mirkowu.xsocket.core.server.IServerActionDispatcher;

public class ClientSendThread extends LoopThread {
  private ISender sender;
  private IServerActionDispatcher dispatcher;

    public ClientSendThread(ISender sender, IServerActionDispatcher dispatcher) {
       super("server_client_send_thread");
       this.sender = sender;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void onLoopStart() {

    }

    @Override
    protected void onLoopExec() throws Exception {
sender.send();
    }

    @Override
    protected void onLoopEnd(Exception e) {

    }

    @Override
    public void shutdown() {
        sender.close();
        super.shutdown();
    }
}
