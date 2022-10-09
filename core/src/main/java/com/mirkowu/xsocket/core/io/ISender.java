package com.mirkowu.xsocket.core.io;

import com.mirkowu.xsocket.core.ISendData;
import com.mirkowu.xsocket.core.action.IActionDispatcher;

import java.io.OutputStream;
import java.net.DatagramSocket;

public interface ISender {
    void initTcp(OutputStream outputStream, IActionDispatcher dispatcher);

    void initUdp(DatagramSocket datagramSocket,   IActionDispatcher dispatcher);

    boolean send() throws Exception;

    void offer(ISendData sendData);

    void close();

}
