package com.mirkowu.xsocket.core.io;

import com.mirkowu.xsocket.core.action.IActionDispatcher;

import java.io.InputStream;
import java.net.DatagramSocket;

public abstract class AbsReceiver implements IReceiver {
    protected InputStream inputStream;
    protected DatagramSocket datagramSocket;
    protected IActionDispatcher dispatcher;
    @Override
    public void initTcp(InputStream inputStream,IActionDispatcher dispatcher) {
        this.inputStream = inputStream;
        this.dispatcher = dispatcher;
    }

    @Override
    public void initUdp(DatagramSocket datagramSocket,IActionDispatcher dispatcher) {
        this.datagramSocket = datagramSocket;
        this.dispatcher = dispatcher;
    }

}
