package com.mirkowu.xsocket.core;

import com.mirkowu.xsocket.core.action.IActionDispatcher;

import java.io.OutputStream;
import java.net.DatagramSocket;

public abstract class AbsSender implements ISender {
    protected DatagramSocket datagramSocket;
    protected OutputStream outputStream;
    protected IActionDispatcher dispatcher;

    @Override
    public void initTcp(OutputStream outputStream, IActionDispatcher dispatcher) {
        this.outputStream = outputStream;
        this.dispatcher = dispatcher;
    }

    @Override
    public void initUdp(DatagramSocket datagramSocket,IActionDispatcher dispatcher) {
        this.datagramSocket = datagramSocket;
        this.dispatcher = dispatcher;
    }


}
