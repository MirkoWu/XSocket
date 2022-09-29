package com.mirkowu.xsocket.core.server;

import android.os.Handler;
import android.os.Looper;

import static com.mirkowu.xsocket.core.server.ServerActionType.Server.ACTION_ALREADY_SHUTDOWN;
import static com.mirkowu.xsocket.core.server.ServerActionType.Server.ACTION_CLIENT_CONNECTED;
import static com.mirkowu.xsocket.core.server.ServerActionType.Server.ACTION_CLIENT_DISCONNECTED;
import static com.mirkowu.xsocket.core.server.ServerActionType.Server.ACTION_LISTENING;
import static com.mirkowu.xsocket.core.server.ServerActionType.Server.ACTION_WILL_SHUTDOWN;

import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.io.LoopThread;
import com.mirkowu.xsocket.core.listener.IRegister;
import com.mirkowu.xsocket.core.listener.IServerSocketListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerActionDispatcher implements IServerActionDispatcher, IRegister<IServerSocketListener> {
    /**
     * 事件消费队列
     */
    private static final LinkedBlockingQueue<ActionBean> ACTION_QUEUE = new LinkedBlockingQueue();


    private static final DispatchThread dispatchThread = new DispatchThread();


    static {
        dispatchThread.start();
    }

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
    private volatile IClientPool<String, IClient> mClientPool;

    private IServerManager mServerManager;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public ServerActionDispatcher(IServerManager serverManager) {
        this.mServerManager = serverManager;
    }

    public void setServerPort(int localPort) {
        mServerPort = localPort;
    }

    public void setClientPool(IClientPool<String, IClient> clientPool) {
        mClientPool = clientPool;
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

    @Override
    public void dispatch(int action, ActionBean bean) {
        if (bean == null) bean = new ActionBean();
        bean.action = action;
        bean.args2 = this;
        ACTION_QUEUE.offer(bean);
    }

    @Override
    public void dispatch(int action) {
        dispatch(action, null);
    }

    private void dispatchActionToListener(int action, ActionBean bean, IServerSocketListener listener) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                handleAction(action, bean, listener);
            }
        });
    }

    private void handleAction(int action, ActionBean bean, IServerSocketListener listener) {
        switch (action) {
            case ACTION_LISTENING:
                listener.onServerListening(mServerPort);
                break;
            case ACTION_CLIENT_CONNECTED:
                listener.onClientConnected((IClient) bean.data, mServerPort, mClientPool);
                break;
            case ACTION_CLIENT_DISCONNECTED:
                listener.onClientDisconnected((IClient) bean.data, mServerPort, mClientPool);
                break;
            case ACTION_WILL_SHUTDOWN:
//                listener.
                break;
            case ACTION_ALREADY_SHUTDOWN:
                listener.onServerAlreadyShutdown(mServerPort);
                break;

        }
    }

    private static class DispatchThread extends LoopThread {
        public DispatchThread() {
            super("server_action_dispatch_thread");
        }

        @Override
        protected void onLoopStart() {

        }

        @Override
        protected void onLoopExec() throws Exception {
            ActionBean bean = ACTION_QUEUE.take();
            if (bean != null && bean.args2 != null) {
                ServerActionDispatcher actionDispatcher = (ServerActionDispatcher) bean.args2;
                synchronized (actionDispatcher.mSocketListenerList) {
                    List<IServerSocketListener> list = new ArrayList<>(actionDispatcher.mSocketListenerList);
                    Iterator<IServerSocketListener> it = list.iterator();
                    while (it.hasNext()) {
                        IServerSocketListener listener = it.next();
                        actionDispatcher.dispatchActionToListener(bean.action, bean, listener);
                    }
                }
            }

        }

        @Override
        protected void onLoopEnd(Exception e) {

        }
    }
}
