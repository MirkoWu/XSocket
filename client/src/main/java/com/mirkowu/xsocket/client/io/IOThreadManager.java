package com.mirkowu.xsocket.client.io;

import com.mirkowu.xsocket.client.Options;
import com.mirkowu.xsocket.core.ISendData;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.IReceiver;
import com.mirkowu.xsocket.core.ISender;
import com.mirkowu.xsocket.core.ReceiverImp;
import com.mirkowu.xsocket.core.SenderImp;
import com.mirkowu.xsocket.core.exception.ManualCloseException;
import com.mirkowu.xsocket.core.io.IIOManager;
import com.mirkowu.xsocket.core.io.IOThreadMode;

import java.io.InputStream;
import java.io.OutputStream;

public class IOThreadManager implements IIOManager {

    private ISender sender;
    private IReceiver receiver;
    private DuplexSenderThread duplexSenderThread;
    private DuplexReceiverThread duplexReceiverThread;
    private SimplexThread simplexThread;
    private Options options;
    private IActionDispatcher dispatcher;


    public IOThreadManager(InputStream inputStream, OutputStream outputStream, Options options, IActionDispatcher dispatcher) {
        this.options = options;
        this.dispatcher = dispatcher;

        receiver = new ReceiverImp();
        sender = new SenderImp();

        if (options != null) {
            if (options.getReceiver() != null) {
                receiver = options.getReceiver();

            }
            if (options.getSender() != null) {
                sender = options.getSender();
            }
        }

        receiver.init(inputStream);
        sender.init(outputStream,dispatcher);
    }


    @Override
    public void start() {
        if (options != null) {
            IOThreadMode ioThreadMode = options.getIoThreadMode();
            switch (ioThreadMode) {
                case SIMPLEX:
                    startSimplexMode();
                    break;
                case DUPLEX:
                    startDuplexMode();
                    break;
                default:
                    throw new RuntimeException("请指定IO线程模式");
            }
        }
    }

    private void startSimplexMode() {
        shutDownAllThread(null);
        simplexThread = new SimplexThread(receiver, sender, dispatcher);
        simplexThread.start();
    }

    private void startDuplexMode() {
        shutDownAllThread(null);
        duplexReceiverThread = new DuplexReceiverThread(receiver, dispatcher);
        duplexSenderThread = new DuplexSenderThread(sender, dispatcher);
        duplexReceiverThread.start();
        duplexSenderThread.start();
    }

    @Override
    public void send(ISendData sendData) {
        sender.offer(sendData);
    }

    public void shutDownAllThread(Exception e) {
        if (duplexSenderThread != null) {
            duplexSenderThread.shutdown(e);
        }
        if (duplexReceiverThread != null) {
            duplexReceiverThread.shutdown(e);
        }
        if (simplexThread != null) {
            simplexThread.shutdown(e);
        }
    }

    @Override
    public void close() {
        shutDownAllThread(new ManualCloseException());
    }

    @Override
    public void close(Exception e) {
        shutDownAllThread(e);
    }
}
