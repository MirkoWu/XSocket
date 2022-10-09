package com.mirkowu.xsocket.client.io;

import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.io.ISender;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.io.LoopThread;

public class DuplexSenderThread extends LoopThread {

    private ISender sender;
    private IActionDispatcher dispatcher;

    public DuplexSenderThread(ISender sender, IActionDispatcher dispatcher) {
        super();
        this.sender = sender;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void onLoopStart() {
        dispatcher.dispatchAction(ActionType.ACTION_SEND_START);
    }

    @Override
    protected void onLoopExec() throws Exception {
        if (sender != null) {
            boolean result = sender.send();
        }
    }

    @Override
    protected void onLoopEnd(Exception e) {
        dispatcher.dispatchAction(ActionType.ACTION_SEND_SHUTDOWN, new ActionBean(e));
    }

    @Override
    public void shutdown() {
        if (sender != null) {
            sender.close();
        }
        super.shutdown();
    }
}
