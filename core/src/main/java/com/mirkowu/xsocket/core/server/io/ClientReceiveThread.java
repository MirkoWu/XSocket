package com.mirkowu.xsocket.core.server.io;

import com.mirkowu.xsocket.core.IReceiver;
import com.mirkowu.xsocket.core.ISender;
import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.io.LoopThread;
import com.mirkowu.xsocket.core.server.IServerActionDispatcher;

public class ClientReceiveThread extends LoopThread {
    private IReceiver receiver;
    private IServerActionDispatcher dispatcher;

    public ClientReceiveThread(IReceiver receiver, IServerActionDispatcher dispatcher) {
        super("server_client_receive_thread");
        this.receiver = receiver;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void onLoopStart() {

    }

    @Override
    protected void onLoopExec() throws Exception {
        byte[] bytes = receiver.receive();
        dispatcher.dispatch(ActionType.ACTION_RECEIVE, new ActionBean(bytes));
    }

    @Override
    protected void onLoopEnd(Exception e) {

    }

    @Override
    public synchronized void shutdown() {
        receiver.close();
        super.shutdown();
    }

}
