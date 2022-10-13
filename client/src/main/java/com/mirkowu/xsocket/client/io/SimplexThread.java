package com.mirkowu.xsocket.client.io;

import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
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
        dispatcher.dispatchAction(ActionType.ACTION_SEND_START);
        dispatcher.dispatchAction(ActionType.ACTION_RECEIVE_START);
    }

    @Override
    protected void onLoopExec() throws Exception {
        if (sender != null) {
            boolean result = sender.send();
            if (result) {
                if (receiver != null) {
                    byte[] data = receiver.receive();
                    dispatcher.dispatchAction(ActionType.ACTION_RECEIVE, new ActionBean(data));
                    XLog.e(getClass().getSimpleName() + " receiver :" + ByteUtils.bytes2String(data));
                }
            }
        }

    }


    @Override
    protected void onLoopEnd(Exception e) {
        ActionBean bean = new ActionBean(e);
        dispatcher.dispatchAction(ActionType.ACTION_SEND_SHUTDOWN, bean);
        dispatcher.dispatchAction(ActionType.ACTION_RECEIVE_SHUTDOWN, bean);
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
