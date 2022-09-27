package com.mirkowu.xsocket.core;

public abstract class LoopThread implements Runnable {
    protected volatile Thread coreThread;
    protected volatile boolean isRunning = false;
    protected String name = this.getClass().getSimpleName();
    protected volatile Exception exception;

    LoopThread() {
    }

    public LoopThread start() {
        if (!isRunning) {
            isRunning = true;
            coreThread = new Thread(this, name);
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
        } catch (InterruptedException e) {

        } catch (Exception e) {
            exception = e;
        } finally {
            isRunning = false;
            onLoopEnd(exception);
        }
    }

    public void shutDown() {
        isRunning = false;
        if (coreThread != null) {
            coreThread.interrupt();
        }
        coreThread = null;
    }



    protected abstract void onLoopStart() ;
    protected abstract void onLoopEnd(Exception e) ;
    protected abstract void onLoopExec() ;
}
