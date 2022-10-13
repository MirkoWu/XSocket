package com.mirkowu.xsocket.client.dispatcher;

import android.os.Handler;
import android.os.Looper;

import com.mirkowu.xsocket.client.connect.IConnectManager;
import com.mirkowu.xsocket.core.IPConfig;
import com.mirkowu.xsocket.client.listener.ISocketListener;
import com.mirkowu.xsocket.core.data.IPulseSendData;
import com.mirkowu.xsocket.core.data.ISendData;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.exception.ManualCloseException;
import com.mirkowu.xsocket.core.listener.IRegister;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ActionDispatcher implements IActionDispatcher, IRegister<ISocketListener> {

    public static final String TAG = ActionDispatcher.class.getSimpleName();
    private volatile IConnectManager connectManager;
    private volatile IPConfig ipConfig;
    private volatile boolean isDisconnectByIOThread = false;

    private volatile List<ISocketListener> listenerList = new ArrayList<>();

    public ActionDispatcher(IConnectManager connectManager, IPConfig ipConfig) {
        this.connectManager = connectManager;
        this.ipConfig = ipConfig;
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void registerSocketListener(ISocketListener listener) {
        if (listener != null && !listenerList.contains(listener)) {
            listenerList.add(listener);
        }
    }

    @Override
    public void unRegisterSocketListener(ISocketListener listener) {
        if (listenerList.contains(listener)) {
            listenerList.remove(listener);
        }
    }

    public void removeAllSocketListener(){
        listenerList.clear();
    }

    @Override
    public void dispatchAction(int action) {
        dispatchAction(action, new ActionBean());
    }

    @Override
    public synchronized void dispatchAction(int action, ActionBean bean) {
//        List<ISocketListener> copyData = new ArrayList<>(listenerList);
//        Iterator<ISocketListener> it = copyData.iterator();
        Iterator<ISocketListener> it = listenerList.iterator();
        while (it.hasNext()) {
            ISocketListener listener = it.next();
            dispatchActionToListener(action, bean, listener);
        }
    }

    public synchronized void dispatchActionToListener(int action, ActionBean bean, ISocketListener listener) {
        if (bean == null) bean = new ActionBean();
        ActionBean finalBean = bean;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                handleAction(action, finalBean, listener);
            }
        });
    }


    private void handleAction(int action, ActionBean bean, ISocketListener listener) {
        try {
            switch (action) {
                case ActionType.ACTION_SEND:
                    listener.onSend(ipConfig, (ISendData) bean.data);
                    break;
                case ActionType.ACTION_PULSE_SEND:
                    listener.onPulseSend(ipConfig, (IPulseSendData) bean.data);
                    break;
                case ActionType.ACTION_RECEIVE:
                    listener.onReceive(ipConfig, (byte[]) bean.data);
                    break;
                case ActionType.ACTION_CONNECT_SUCCESS:
                    listener.onConnectSuccess(ipConfig);
                    break;
                case ActionType.ACTION_CONNECT_FAIL:
                    listener.onConnectFail(ipConfig, (Exception) bean.data);

                    onConnectFail(bean);
                    break;
                case ActionType.ACTION_DISCONNECT:
                    listener.onDisConnect(ipConfig, (Exception) bean.data);
                    break;
                case ActionType.ACTION_RECONNECT:
                    listener.onReconnect(ipConfig);
                    break;
                case ActionType.ACTION_SEND_START:
                case ActionType.ACTION_RECEIVE_START:
                    onIOThreadStart(bean);
                    break;
                case ActionType.ACTION_SEND_SHUTDOWN:
                case ActionType.ACTION_RECEIVE_SHUTDOWN:
                    onIOThreadShutdown(bean);
                    break;

            }
        } catch (Exception e) {
            XLog.e("ActionDispatcher handleAction error: " + e.toString());
        }
    }


    private void onConnectFail(ActionBean bean) {
        if (connectManager != null) {
            connectManager.disconnect((Exception) bean.data);
        }
    }

    private void onIOThreadStart(ActionBean bean) {
        isDisconnectByIOThread = false;
    }

    private void onIOThreadShutdown(ActionBean bean) {
        if (!isDisconnectByIOThread) {
            isDisconnectByIOThread = true;
            Object exception = bean.data;
            if (!(exception instanceof ManualCloseException)) {
                connectManager.disconnect((Exception) exception);
            }
        }
    }
}
