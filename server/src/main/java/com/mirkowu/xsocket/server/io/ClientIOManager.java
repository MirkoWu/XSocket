package com.mirkowu.xsocket.server.io;

import com.mirkowu.xsocket.core.IUdpSendData;
import com.mirkowu.xsocket.core.SocketType;
import com.mirkowu.xsocket.core.XLog;
import com.mirkowu.xsocket.core.io.IReceiver;
import com.mirkowu.xsocket.core.ISendData;
import com.mirkowu.xsocket.core.io.ISender;
import com.mirkowu.xsocket.core.io.TcpReceiverImp;
import com.mirkowu.xsocket.core.io.TcpSenderImp;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.exception.ManualCloseException;
import com.mirkowu.xsocket.core.io.IIOManager;
import com.mirkowu.xsocket.core.io.UdpReceiverImp;
import com.mirkowu.xsocket.core.io.UdpSenderImp;
import com.mirkowu.xsocket.server.ServerOptions;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;

public class ClientIOManager implements IIOManager {


    private IReceiver receiver;
    private ISender sender;
    private ServerOptions serverOptions;

    private ClientReceiveThread receiveThread;
    private ClientSendThread sendThread;
    private IActionDispatcher clientActionDispatcher;
    private boolean isTcp = true;

    public ClientIOManager(InputStream inputStream, OutputStream outputStream, ServerOptions serverOptions, IActionDispatcher clientActionDispatcher) {
        this(serverOptions, clientActionDispatcher);
        receiver.initTcp(inputStream, clientActionDispatcher);
        sender.initTcp(outputStream, clientActionDispatcher);
    }

    public ClientIOManager(DatagramSocket datagramSocket, ServerOptions serverOptions, IActionDispatcher clientActionDispatcher) {
        this(serverOptions, clientActionDispatcher);
        receiver.initUdp(datagramSocket, clientActionDispatcher);
        sender.initUdp(datagramSocket, clientActionDispatcher);
    }


    private ClientIOManager(ServerOptions serverOptions, IActionDispatcher clientActionDispatcher) {
        this.serverOptions = serverOptions;
        this.clientActionDispatcher = clientActionDispatcher;


        if (serverOptions != null) {
            if (serverOptions.getReceiver() != null) {
                receiver = serverOptions.getReceiver();

            }
            if (serverOptions.getSender() != null) {
                sender = serverOptions.getSender();
            }
            isTcp = serverOptions.getSocketType() == SocketType.TCP;
        }

        //默认兜底
        if (receiver == null) {
            if (isTcp) {
                receiver = new TcpReceiverImp();
            } else {
                receiver = new UdpReceiverImp();
            }
        }
        if (sender == null) {
            if (isTcp) {
                sender = new TcpSenderImp();
            } else {
                sender = new UdpSenderImp();
            }
        }

    }

    @Override
    public void start() {

    }

    public void startReceiveThread() {
        if (receiveThread != null) {
            receiveThread.shutdown();
            receiveThread = null;
        }
        receiveThread = new ClientReceiveThread(receiver, clientActionDispatcher);
        receiveThread.start();
    }

    public void startSendThread() {
        if (sendThread != null) {
            sendThread.shutdown();
            sendThread = null;
        }
        sendThread = new ClientSendThread(sender, clientActionDispatcher);
        sendThread.start();
    }

    @Override
    public void send(ISendData sendData) {
        sender.offer(sendData);
    }

    @Override
    public void close() {
        close(new ManualCloseException());
    }

    @Override
    public void close(Exception e) {
        shutdownAllThread(e);
    }

    public void shutdownAllThread(Exception e) {
        if (sendThread != null) {
            sendThread.shutdown(e);
            sendThread = null;
        }
        if (receiveThread != null) {
            receiveThread.shutdown(e);
            receiveThread = null;
        }
    }
}
