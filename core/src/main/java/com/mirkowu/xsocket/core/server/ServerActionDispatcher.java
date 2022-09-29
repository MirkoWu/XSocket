package com.mirkowu.xsocket.core.server;

import static com.mirkowu.xsocket.core.server.ServerActionType.Server.ACTION_ALREADY_SHUTDOWN;
import static com.mirkowu.xsocket.core.server.ServerActionType.Server.ACTION_CLIENT_CONNECTED;
import static com.mirkowu.xsocket.core.server.ServerActionType.Server.ACTION_CLIENT_DISCONNECTED;
import static com.mirkowu.xsocket.core.server.ServerActionType.Server.ACTION_LISTENING;
import static com.mirkowu.xsocket.core.server.ServerActionType.Server.ACTION_WILL_SHUTDOWN;

import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.listener.IRegister;
import com.mirkowu.xsocket.core.listener.IServerSocketListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerActionDispatcher implements IServerActionDispatcher, IRegister<IServerSocketListener> {
    /**
     * 事件消费队列
     */
    private static final LinkedBlockingQueue<ActionBean> ACTION_QUEUE = new LinkedBlockingQueue();
    /**
     * 回调列表
     */
    private volatile List<IServerSocketListener> mSocketListenerList = new ArrayList<>();
    /**
     * 服务器端口
     */
    private volatile int mServerPort;
    /**
     * 客户端池子
     */
    private volatile IClientPool<IClient, String> mClientPool;

    private IServerManager mServerManager;

    public ServerActionDispatcher(IServerManager serverManager) {
        this.mServerManager = serverManager;
    }

    @Override
    public void dispatch(int action, ActionBean bean) {
        if (bean == null) bean = new ActionBean();
    }

    @Override
    public void dispatch(int action) {

    }


    @Override
    public void registerSocketListener(IServerSocketListener listener) {
        if (listener != null) {
            synchronized (mSocketListenerList) {
                if (!mSocketListenerList.contains(listener)) {
                    mSocketListenerList.add(listener);
                }
            }
        }
    }

    @Override
    public void unRegisterSocketListener(IServerSocketListener listener) {
        if (listener != null) {
            synchronized (mSocketListenerList) {
                mSocketListenerList.remove(listener);
            }
        }
    }

    private void dispatchActionToListener(int action, ActionBean bean, IServerSocketListener listener) {

    }

    private void handleAction(int action, ActionBean bean, IServerSocketListener listener) {
        switch (action) {
            case ACTION_LISTENING:
                listener.onServerListening();
                break;
            case ACTION_CLIENT_CONNECTED:
                break;
            case ACTION_CLIENT_DISCONNECTED:
                break;
            case ACTION_WILL_SHUTDOWN:
                break;
            case ACTION_ALREADY_SHUTDOWN:
                break;

        }
    }
}
