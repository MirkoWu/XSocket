package com.mirkowu.xsocket.core;

import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.listener.ISocketListener;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 抽象联调管理器
 */
public abstract class AbsReconnectionManager implements ISocketListener {
    /**
     * 连接管理器
     */
    protected volatile IConnectManager mConnectManager;
    /**
     * 心跳管理器
     */
    protected PulseManager mPulseManager;
    /**
     * 是否销毁
     */
    protected volatile boolean mDetach;
    /**
     * 需要忽略的断开连接集合,当Exception在此集合中,忽略该类型的断开异常,不会自动重连
     */
    protected volatile Set<Class<? extends Exception>> mIgnoreDisconnectExceptionList = new LinkedHashSet<>();


   protected IActionDispatcher dispatcher;

    public AbsReconnectionManager() {
    }
    /**
     * 关联到某一个连接管理器
     *
     * @param manager 当前连接管理器
     */
    public synchronized void attach(IConnectManager manager) {
        if (mDetach) {
            detach();
        }
        mDetach = false;
        mConnectManager = manager;
     dispatcher=   manager.getActionDispatcher();
     //   mPulseManager = manager.getPulseManager();
        mConnectManager.registerSocketListener(this);
    }

    /**
     * 解除连接当前的连接管理器
     */
    public synchronized void detach() {
        mDetach = true;
        if (mConnectManager != null) {
            mConnectManager.unRegisterSocketListener(this);
        }
    }

    /**
     * 添加需要忽略的异常,当断开异常为该异常时,将不会进行重连.
     *
     * @param e 需要忽略的异常
     */
    public final void addIgnoreException(Class<? extends Exception> e) {
        synchronized (mIgnoreDisconnectExceptionList) {
            mIgnoreDisconnectExceptionList.add(e);
        }
    }

    /**
     * 添加需要忽略的异常,当断开异常为该异常时,将不会进行重连.
     *
     * @param e 需要删除的异常
     */
    public final void removeIgnoreException(Exception e) {
        synchronized (mIgnoreDisconnectExceptionList) {
            mIgnoreDisconnectExceptionList.remove(e.getClass());
        }
    }

    /**
     * 删除需要忽略的异常
     *
     * @param e 需要忽略的异常
     */
    public final void removeIgnoreException(Class<? extends Exception> e) {
        synchronized (mIgnoreDisconnectExceptionList) {
            mIgnoreDisconnectExceptionList.remove(e);
        }
    }

    /**
     * 删除所有的忽略异常
     */
    public final void removeAll() {
        synchronized (mIgnoreDisconnectExceptionList) {
            mIgnoreDisconnectExceptionList.clear();
        }
    }



    @Override
    public void onReceive(IPConfig config, byte[] bytes) {

    }

    @Override
    public void onSend(IPConfig config, byte[] bytes) {

    }

    @Override
    public void onReconnect(IPConfig config) {

    }
}
