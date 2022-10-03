package com.mirkowu.xsocket.client.connect;

import com.mirkowu.xsocket.client.Options;
import com.mirkowu.xsocket.core.IPulseSendData;
import com.mirkowu.xsocket.core.ISendData;
import com.mirkowu.xsocket.core.exception.PulseDeadException;
import com.mirkowu.xsocket.core.io.IOThreadMode;
import com.mirkowu.xsocket.core.io.LoopThread;

import java.util.concurrent.atomic.AtomicInteger;

public class PulseManager implements IPulse {

    private volatile IConnectManager connectManager;
    private volatile Options options;
    private volatile boolean isDead = false;
    private volatile IOThreadMode threadMode;
    /**
     * 当前频率
     */
    private volatile long mCurrentFrequency;
    /**
     * 允许遗漏的次数
     */
    private volatile AtomicInteger mLoseTimes = new AtomicInteger(-1);
    private PulseThread mPulseThread = new PulseThread();
    private IPulseSendData pulseSendData;

    public PulseManager(IConnectManager connectManager, Options options) {
        this.connectManager = connectManager;
        this.options = options;
        threadMode = options.getIoThreadMode();
    }

    public synchronized IPulse setPulseSendData(IPulseSendData sendData) {
        if (sendData != null) {
            pulseSendData = sendData;
        }
        return this;
    }

    @Override
    public void pulse() {
        privateDead();
        updateFrequency();
        if (threadMode != IOThreadMode.SIMPLEX) {
            if (!mPulseThread.isRunning()) {
                mPulseThread.start();
            }
        }

    }

    private void updateFrequency() {
        if (threadMode != IOThreadMode.SIMPLEX) {
            mCurrentFrequency = options.getPulseFrequency();
            mCurrentFrequency = mCurrentFrequency < 1000 ? 1000 : mCurrentFrequency;
        } else {
            privateDead();
        }
    }

    private void privateDead() {
        if (mPulseThread != null) {
            mPulseThread.shutdown();
        }
    }

    @Override
    public void trigger() {
        if (isDead) {
            return;
        }
        if (threadMode != IOThreadMode.SIMPLEX && connectManager != null && pulseSendData != null) {
            connectManager.send(pulseSendData);
        }

    }

    @Override
    public void dead() {
        mLoseTimes.set(0);
        isDead = true;
        privateDead();
    }

    @Override
    public void feed() {
        mLoseTimes.set(-1);
    }

    public int getLoseTimes() {
        return mLoseTimes.get();
    }

    private class PulseThread extends LoopThread {

        @Override
        protected void onLoopStart() {

        }

        @Override
        protected void onLoopExec() throws Exception {
            if (isDead) {
                shutdown();
                return;
            }

            if (connectManager != null && pulseSendData != null) {
                if (options.getPulseFeedLoseTimes() != -1 && mLoseTimes.incrementAndGet() >= options.getPulseFeedLoseTimes()) {
                    connectManager.disconnect(new PulseDeadException("You need to feed the dog, or the connection will be disconnected"));
                } else {
                    connectManager.send(pulseSendData);
                }
            }
            Thread.sleep(mCurrentFrequency);
        }

        @Override
        protected void onLoopEnd(Exception e) {

        }
    }

}
