package com.mirkowu.xsocket.core.client;

import com.mirkowu.xsocket.core.ISender;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.io.LoopThread;

public class DuplexSenderThread extends LoopThread {

    ISender sender;
    IActionDispatcher dispatcher;

    public DuplexSenderThread(ISender sender, IActionDispatcher dispatcher) {
        super();
        this.sender = sender;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void onLoopStart() {
        XLog.e(getClass().getSimpleName() + " onLoopStart");
    }

    @Override
    protected void onLoopExec() {
        if (sender != null) {
            boolean result = sender.send();
            dispatcher.dispatch(ActionType.ACTION_SEND);
            XLog.e(getClass().getSimpleName() + " send :" + result);
        }
    }

    @Override
    protected void onLoopEnd(Exception e) {
        String msg = e != null ? e.toString() : "null";
        XLog.e(getClass().getSimpleName() + " onLoopEnd :" + msg);
    }

    @Override
    public void shutdown() {
        if (sender != null) {
            sender.close();
        }
        super.shutdown();
    }
}
