package com.mirkowu.xsocket.core.server.io;

import com.mirkowu.xsocket.core.IReceiver;
import com.mirkowu.xsocket.core.ISender;
import com.mirkowu.xsocket.core.ReceiverImp;
import com.mirkowu.xsocket.core.SenderImp;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.exception.ManualCloseException;
import com.mirkowu.xsocket.core.io.IIOManager;
import com.mirkowu.xsocket.core.server.ServerOptions;

import java.io.InputStream;
import java.io.OutputStream;

public class ClientIOManager implements IIOManager {

    private InputStream inputStream;
    private OutputStream outputStream;
    private IReceiver receiver;
    private ISender sender;
    private ServerOptions serverOptions;

    private ClientReceiveThread receiveThread;
    private ClientSendThread sendThread;
    private IActionDispatcher clientActionDispatcher;

    public ClientIOManager(InputStream inputStream, OutputStream outputStream, ServerOptions serverOptions, IActionDispatcher clientActionDispatcher) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.serverOptions = serverOptions;
        this.clientActionDispatcher = clientActionDispatcher;

        initIO();
    }

    private void initIO() {
        receiver = new ReceiverImp();
        sender = new SenderImp();

        if (serverOptions != null) {
            if (serverOptions.getReceiver() != null) {
                receiver = serverOptions.getReceiver();

            }
            if (serverOptions.getSender() != null) {
                sender = serverOptions.getSender();
            }
        }
        receiver.init(inputStream);
        sender.init(outputStream);
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
    public void send(byte[] bytes) {
        sender.offer(bytes);
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
