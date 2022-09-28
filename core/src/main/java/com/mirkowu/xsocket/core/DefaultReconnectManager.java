package com.mirkowu.xsocket.core;

import com.mirkowu.xsocket.core.action.ActionType;
import com.mirkowu.xsocket.core.exception.ManualCloseException;
import com.mirkowu.xsocket.core.io.LoopThread;
import com.mirkowu.xsocket.core.util.ThreadUtils;

public class DefaultReconnectManager extends AbsReconnectionManager {

    public static final int MAX_TRY_TIMES = 10;
    private volatile int tryTimes;
    ReconnectThread reconnectThread;


    public DefaultReconnectManager() {
        reconnectThread = new ReconnectThread();
    }

    @Override
    public void onConnectSuccess(IPConfig config) {
        resetThread(true);
    }

    private void resetThread(boolean isResetTimes) {
        if (isResetTimes) {
            tryTimes = 0;
        }
        if (reconnectThread != null) {
            reconnectThread.shutdown();
        }
    }

    @Override
    public void onConnectFail(IPConfig config, Exception e) {
        if (e != null) {
            tryTimes++;
            if (tryTimes < MAX_TRY_TIMES) {
                tryReconnect();
            } else {
                resetThread(false);
            }
        }
    }

    @Override
    public void onDisConnect(IPConfig config, Exception e) {
        if (isNeedReconnect(e)) {
            tryReconnect();
        } else {
            resetThread(true);
        }
    }

    private void tryReconnect() {
        if (reconnectThread != null && !reconnectThread.isRunning()) {
            reconnectThread.start();
        }
    }

    private boolean isNeedReconnect(Exception e) {
        if (e instanceof ManualCloseException) {
            return false;
        } else {
            return true;
        }
    }


    class ReconnectThread extends LoopThread {
        long tryReconnectPeriod = 5 * 1000;

        @Override
        protected void onLoopStart() {

        }

        @Override
        protected void onLoopExec() throws Exception {
            if (mDetach) {
                shutdown();
                return;
            }

            ThreadUtils.sleep(tryReconnectPeriod);

            if (mDetach) {
                shutdown();
                return;
            }

            if (!mConnectManager.isConnected()) {
                if (dispatcher != null) {
                    dispatcher.dispatch(ActionType.ACTION_RECONNECT);
                }

                mConnectManager.connect();
            } else {
                shutdown();
            }
        }

        @Override
        protected void onLoopEnd(Exception e) {

        }
    }


}
