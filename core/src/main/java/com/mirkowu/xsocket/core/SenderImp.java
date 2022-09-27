package com.mirkowu.xsocket.core;

import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;

public class SenderImp implements ISender {
    OutputStream outputStream;
    LinkedBlockingQueue<I>

    @Override
    public void init(OutputStream outputStream) {
        this.outputStream=outputStream;
    }

    @Override
    public boolean send() {
        outputStream.write();
        outputStream.flush();
        return false;
    }

    @Override
    public void offer() {

    }

    @Override
    public byte[] getData() {
        return new byte[0];
    }
}
