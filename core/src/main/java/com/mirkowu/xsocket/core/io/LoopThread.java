package com.mirkowu.xsocket.core.io;

public abstract class LoopThread implements Runnable {
    protected volatile Thread coreThread;
    protected volatile boolean isRunning = false;
    protected volatile boolean isShutdown = false;
    protected String threadName = this.getClass().getSimpleName();
    protected volatile Exception exception;

    public LoopThread(){
    }
    public LoopThread(String threadName){
        this.threadName = threadName;
    }

    public LoopThread start() {
        if (!isRunning) {
            isRunning = true;
            coreThread = new Thread(this, threadName);
            coreThread.start();
        }
        return this;
    }


    @Override
    public void run() {
        try {
            onLoopStart();
            while (isRunning && !Thread.currentThread().isInterrupted()) {
                onLoopExec();
            }
        } catch (Exception e) {
            exception = e;
        } finally {
            isRunning = false;
            onLoopEnd(exception);
        }
    }

    public void shutdown(Exception e) {
        this.exception = e;
        shutdown();
    }

    public synchronized void shutdown() {
        isRunning = false;
        if (coreThread != null) {
            try {
                coreThread.interrupt();
            } catch (SecurityException e) {
            }
        }
        coreThread = null;
    }


    public boolean isRunning(){
        return isRunning;
    }

    protected abstract void onLoopStart();

    protected abstract void onLoopExec() throws Exception;

    protected abstract void onLoopEnd(Exception e);
}
