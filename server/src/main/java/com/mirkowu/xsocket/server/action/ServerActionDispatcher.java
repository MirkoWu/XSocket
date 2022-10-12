package com.mirkowu.xsocket.server.action;


import com.mirkowu.xsocket.core.ISendData;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.action.ActionBean;
import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.io.LoopThread;
import com.mirkowu.xsocket.core.listener.IRegister;
import com.mirkowu.xsocket.server.IClientStatusListener;
import com.mirkowu.xsocket.server.IServerSocketListener;
import com.mirkowu.xsocket.server.IClient;
import com.mirkowu.xsocket.server.IClientPool;
import com.mirkowu.xsocket.server.IServerManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerActionDispatcher implements IActionDispatcher, IRegister<IServerSocketListener>, IClientStatusRegister {
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
    private volatile List<IClientStatusListener> mClientStatusListenerList = new ArrayList<>();
    /**
     * 服务器端口
     */
    private volatile int mServerPort;
    /**
     * 客户端池子
     */
    private volatile IClientPool<String, IClient> mClientPool;

    private IServerManager mServerManager;

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
    public void registerClientStatusListener(IClientStatusListener listener) {
        if (listener != null) {
            synchronized (mClientStatusListenerList) {
                if (!mClientStatusListenerList.contains(listener)) {
                    mClientStatusListenerList.add(listener);
                }
            }
        }
    }

    @Override
    public void unRegisterClientStatusListener(IClientStatusListener listener) {
        if (listener != null) {
            synchronized (mClientStatusListenerList) {
                mClientStatusListenerList.remove(listener);
            }
        }
    }


    @Override
    public void dispatchAction(int action, ActionBean bean) {
        if (bean == null) bean = new ActionBean();
        bean.action = action;
        bean.dispatcher = this;
        ACTION_QUEUE.offer(bean);
    }

    @Override
    public void dispatchAction(int action) {
        dispatchAction(action, null);
    }


    private void  dispatchServerAction(int action, ActionBean bean, IServerSocketListener listener) {
        try {
            switch (action) {
                case ServerActionType.ACTION_SERVER_LISTENING:
                    listener.onServerListening(mServerPort);
                    break;
                case ActionType.ACTION_RECEIVE:
                    listener.onReceiveFromClient((byte[]) bean.data, (IClient) bean.args2, mClientPool);
                    break;
                case ActionType.ACTION_SEND:
                    listener.onSendToClient((ISendData) bean.data, (IClient) bean.args2, mClientPool);
                    break;
                case ServerActionType.ACTION_SERVER_WILL_SHUTDOWN:
                    listener.onServerWillBeShutdown(mServerPort, mServerManager, mClientPool, (Exception) bean.data);
                    break;
                case ServerActionType.ACTION_SERVER_ALREADY_SHUTDOWN:
                    listener.onServerAlreadyShutdown(mServerPort, (Exception) bean.data);
                    break;

            }
        } catch (Exception e) {
            XLog.e("ServerActionDispatcher handleAction  error:" + e.toString());
        }
    }

    private void  dispatchClientStatusAction(int action, ActionBean bean, IClientStatusListener listener) {
        try {
            switch (action) {
                case ServerActionType.ACTION_CLIENT_CONNECTED:
                    listener.onClientConnected((IClient) bean.data, mServerPort, mClientPool);
                    break;
                case ServerActionType.ACTION_CLIENT_DISCONNECTED:
                    listener.onClientDisconnected((IClient) bean.data, mServerPort, mClientPool, (Exception) bean.args2);
                    break;
            }
        } catch (Exception e) {
            XLog.e("ServerActionDispatcher handleAction  error:" + e.toString());
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
            if (bean != null && bean.dispatcher != null) {
                ServerActionDispatcher actionDispatcher = (ServerActionDispatcher) bean.dispatcher;

                if(bean.action>ServerActionType.ACTION_CLIENT_STATUS_START
                        && bean.action<ServerActionType.ACTION_CLIENT_STATUS_END){
                    synchronized (actionDispatcher.mClientStatusListenerList) {
                        List<IClientStatusListener> list = new ArrayList<>(actionDispatcher.mClientStatusListenerList);
                        Iterator<IClientStatusListener> it = list.iterator();
                        while (it.hasNext()) {
                            IClientStatusListener listener = it.next();
                            actionDispatcher.dispatchClientStatusAction(bean.action, bean, listener);
                        }
                    }
                }else{
                    synchronized (actionDispatcher.mSocketListenerList) {
                        List<IServerSocketListener> list = new ArrayList<>(actionDispatcher.mSocketListenerList);
                        Iterator<IServerSocketListener> it = list.iterator();
                        while (it.hasNext()) {
                            IServerSocketListener listener = it.next();
                            actionDispatcher.dispatchServerAction(bean.action, bean, listener);
                        }
                    }
                }

            }

        }

        @Override
        protected void onLoopEnd(Exception e) {

        }
    }
}
