package com.mirkowu.xsocket.server.io;

import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.io.ISender;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.io.LoopThread;

public class ClientSendThread extends LoopThread {
    private ISender sender;
    private IActionDispatcher dispatcher;

    public ClientSendThread(ISender sender, IActionDispatcher dispatcher) {
        super("server_client_send_thread");
        this.sender = sender;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void onLoopStart() {
        dispatcher.dispatchAction(ActionType.ACTION_SEND_START);
    }

    @Override
    protected void onLoopExec() throws Exception {
        sender.send();
    }

    @Override
    protected void onLoopEnd(Exception e) {
        dispatcher.dispatchAction(ActionType.ACTION_SEND_SHUTDOWN, new ActionBean(e));

    }

    @Override
    public void shutdown() {
        sender.close();
        super.shutdown();
    }
}
