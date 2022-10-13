package com.mirkowu.xsocket.client.io;

import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.IReceiver;
import com.mirkowu.xsocket.core.io.LoopThread;

public class DuplexReceiverThread extends LoopThread {

    private IReceiver receiver;
    private IActionDispatcher dispatcher;

    public DuplexReceiverThread(IReceiver receiver, IActionDispatcher dispatcher) {
        super();
        this.receiver = receiver;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void onLoopStart() {
        dispatcher.dispatchAction(ActionType.ACTION_RECEIVE_START);
    }

    @Override
    protected void onLoopExec() throws Exception {
        byte[] data = receiver.receive();
    }

    @Override
    protected void onLoopEnd(Exception e) {
        dispatcher.dispatchAction(ActionType.ACTION_RECEIVE_SHUTDOWN, new ActionBean(e));
    }

    @Override
    public void shutdown() {
        if (receiver != null) {
            receiver.close();
        }
        super.shutdown();
    }
}
