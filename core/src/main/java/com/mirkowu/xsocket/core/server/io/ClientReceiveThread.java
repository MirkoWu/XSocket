package com.mirkowu.xsocket.core.server.io;

import com.mirkowu.xsocket.core.IReceiver;
import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.io.LoopThread;

public class ClientReceiveThread extends LoopThread {
    private IReceiver receiver;
    private IActionDispatcher dispatcher;

    public ClientReceiveThread(IReceiver receiver, IActionDispatcher dispatcher) {
        super("server_client_receive_thread");
        this.receiver = receiver;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void onLoopStart() {
        dispatcher.dispatchAction(ActionType.ACTION_RECEIVE_START);

    }

    @Override
    protected void onLoopExec() throws Exception {
        byte[] bytes = receiver.receive();
        dispatcher.dispatchAction(ActionType.ACTION_RECEIVE, new ActionBean(bytes));
    }

    @Override
    protected void onLoopEnd(Exception e) {
        dispatcher.dispatchAction(ActionType.ACTION_RECEIVE_SHUTDOWN);

    }

    @Override
    public synchronized void shutdown() {
        receiver.close();
        super.shutdown();
    }

}
