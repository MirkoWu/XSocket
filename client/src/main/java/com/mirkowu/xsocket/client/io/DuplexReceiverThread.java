package com.mirkowu.xsocket.client.io;

import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.util.ByteUtils;
import com.mirkowu.xsocket.core.IReceiver;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.io.LoopThread;

public class DuplexReceiverThread extends LoopThread {


    IReceiver receiver;
    IActionDispatcher dispatcher;

    public DuplexReceiverThread(IReceiver receiver, IActionDispatcher dispatcher) {
        super();
        this.receiver = receiver;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void onLoopStart() {
        XLog.e(getClass().getSimpleName() + " onLoopStart");
    }

    @Override
    protected void onLoopExec() throws Exception {
        byte[] data = receiver.receive();
        dispatcher.dispatchAction(ActionType.ACTION_RECEIVE, new ActionBean(data));
    }

    @Override
    protected void onLoopEnd(Exception e) {
        String msg = e != null ? e.toString() : "null";
        XLog.e(getClass().getSimpleName() + " onLoopEnd :" + msg);
    }

    @Override
    public void shutdown() {
        if (receiver != null) {
            receiver.close();
        }
        super.shutdown();
        XLog.e("DuplexReceiverThread shutdown :" + (isRunning == false));
    }
}
