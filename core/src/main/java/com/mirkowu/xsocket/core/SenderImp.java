package com.mirkowu.xsocket.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class SenderImp implements ISender {
    OutputStream outputStream;
    LinkedBlockingQueue<ISendable> queue = new LinkedBlockingQueue<>();

    @Override
    public void init(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public boolean send() {
        ISendable sendable = null;
        try {
            sendable = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (sendable != null) {
            byte[] data = sendable.getData();
            if (data != null) {
                try {
                    outputStream.write(data);
                    outputStream.flush();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    @Override
    public void offer(ISendable sendable) {
        queue.offer(sendable);
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
