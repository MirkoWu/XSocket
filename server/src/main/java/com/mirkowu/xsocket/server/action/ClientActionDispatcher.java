package com.mirkowu.xsocket.server.action;

import com.mirkowu.xsocket.core.IPConfig;
import com.mirkowu.xsocket.core.ISendData;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.listener.IRegister;
import com.mirkowu.xsocket.server.IClientSocketListener;

public class ClientActionDispatcher implements IActionDispatcher, IRegister<IClientSocketListener> {

    private IClientSocketListener socketListener;

    public ClientActionDispatcher(IClientSocketListener socketListener) {
        this.socketListener = socketListener;
    }


    @Override
    public void dispatchAction(int action) {
        if (socketListener == null) return;
        dispatchAction(action, null);
    }

    @Override
    public void dispatchAction(int action, ActionBean bean) {
        if (socketListener == null) return;
        if (bean == null) bean = new ActionBean();
        dispatchToListener(action, bean);
    }

    private void dispatchToListener(int action, ActionBean bean) {
        try {
            switch (action) {
                case ActionType.ACTION_RECEIVE:
                    socketListener.onClientReceive((byte[]) bean.data, (IPConfig) bean.args2);
                    break;
                case ActionType.ACTION_SEND:
                    socketListener.onClientSend((ISendData) bean.data, (IPConfig) bean.args2);
                    break;
                case ActionType.ACTION_RECEIVE_START:
                    socketListener.onClientReceiveReady();
                    break;
                case ActionType.ACTION_RECEIVE_SHUTDOWN:
                    socketListener.onClientReceiveDead((Exception) bean.data);
                    break;
                case ActionType.ACTION_SEND_START:
                    socketListener.onClientSendReady();
                    break;
                case ActionType.ACTION_SEND_SHUTDOWN:
                    socketListener.onClientSendDead((Exception) bean.data);
                    break;
            }
        } catch (Exception e) {
            XLog.e("ClientActionDispatcher handleAction  error:" + e.toString());
        }
    }


    @Override
    public void registerSocketListener(IClientSocketListener listener) {

    }

    @Override
    public void unRegisterSocketListener(IClientSocketListener listener) {

    }
}
