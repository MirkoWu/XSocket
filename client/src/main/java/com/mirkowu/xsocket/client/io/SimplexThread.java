package com.mirkowu.xsocket.client.io;

import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.client.dispatcher.IActionDispatcher;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.util.ByteUtils;
import com.mirkowu.xsocket.core.IReceiver;
import com.mirkowu.xsocket.core.ISender;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.io.LoopThread;

public class SimplexThread extends LoopThread {
    IReceiver receiver;
    ISender sender;
    IActionDispatcher dispatcher;

    public SimplexThread(IReceiver receiver, ISender sender, IActionDispatcher dispatcher) {
        super();
        this.receiver = receiver;
        this.sender = sender;
        this.dispatcher = dispatcher;
    }


    @Override
    protected void onLoopStart() {
        XLog.e(getClass().getSimpleName() + " onLoopStart");
    }

    @Override
    protected void onLoopExec() throws Exception{
        if (sender != null) {
            boolean result = sender.send();
            dispatcher.dispatch(ActionType.ACTION_SEND);
            if (result) {
                if (receiver != null) {
                    byte[] data = receiver.receive();
                    dispatcher.dispatch(ActionType.ACTION_RECEIVE, new ActionBean(data));
                    XLog.e(getClass().getSimpleName() + " receiver :" + ByteUtils.bytes2String(data));

                }
            }
        }

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
        if (sender != null) {
            sender.close();
        }
        super.shutdown();
    }
}
