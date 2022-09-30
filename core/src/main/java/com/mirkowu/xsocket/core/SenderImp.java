package com.mirkowu.xsocket.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class SenderImp implements ISender {
    OutputStream outputStream;
    LinkedBlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();

    @Override
    public void init(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public boolean send() throws Exception {
        byte[] bytes = null;
        try {
            bytes = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        }
        if (bytes != null) {
            try {
                outputStream.write(bytes);
                outputStream.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }

        return false;
    }

    @Override
    public void offer(byte[] bytes) {
        queue.offer(bytes);
    }

    @Override
    public void close() {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
