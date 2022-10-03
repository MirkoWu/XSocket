package com.mirkowu.xsocket.client.dispatcher;

import android.os.Handler;
import android.os.Looper;

import com.mirkowu.xsocket.client.connect.IConnectManager;
import com.mirkowu.xsocket.client.IPConfig;
import com.mirkowu.xsocket.client.listener.ISocketListener;
import com.mirkowu.xsocket.core.IPulseSendData;
import com.mirkowu.xsocket.core.ISendData;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.listener.IRegister;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ActionDispatcher implements IActionDispatcher, IRegister<ISocketListener> {

    public static final String TAG = ActionDispatcher.class.getSimpleName();
    //    private static class Holder {
//        public static HandleDispatcher INSTANCE = new HandleDispatcher();
//    }
//
//    private HandleDispatcher() {
//    }
//
//    public static HandleDispatcher getInstance() {
//        return Holder.INSTANCE;
//    }
    private volatile IConnectManager connectManager;
    private volatile IPConfig ipConfig;

    private volatile List<ISocketListener> listenerList = new ArrayList<>();

    public ActionDispatcher(IConnectManager connectManager, IPConfig ipConfig) {
        this.connectManager = connectManager;
        this.ipConfig = ipConfig;
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            int action = msg.arg1;
//            ActionBean bean = (ActionBean) msg.obj;
//            handleAction(action, bean, bean.listener);
//        }
    };

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
        // bean.listener = listener;

//        Message message = Message.obtain();
//        message.arg1 = action;
//        message.obj = bean;
//        mHandler.sendMessage(message);
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
                    listener.onReceive(ipConfig, (ISendData) bean.data);
                    break;
                case ActionType.ACTION_CONNECT_SUCCESS:
                    listener.onConnectSuccess(ipConfig);
                    break;
                case ActionType.ACTION_CONNECT_FAIL:
                    listener.onConnectFail(ipConfig, (Exception) bean.data);
                    break;
                case ActionType.ACTION_DISCONNECT:
                    listener.onDisConnect(ipConfig, (Exception) bean.data);
                    break;
                case ActionType.ACTION_RECONNECT:
                    listener.onReconnect(ipConfig);
                    break;
            }
        } catch (Exception e) {
            XLog.e("ActionDispatcher handleAction error: " + e.toString());
        }
    }


}
