package com.mirkowu.xsocket.core.io;

import com.mirkowu.xsocket.core.IReceiver;
import com.mirkowu.xsocket.core.ISendable;
import com.mirkowu.xsocket.core.ISender;
import com.mirkowu.xsocket.core.Options;
import com.mirkowu.xsocket.core.ReceiverImp;
import com.mirkowu.xsocket.core.SenderImp;
import com.mirkowu.xsocket.core.exception.ManualCloseException;

import java.io.InputStream;
import java.io.OutputStream;

public class IOThreadManager implements IIOManager {

    ISender sender;
    IReceiver receiver;

    DuplexSenderThread duplexSenderThread;
    DuplexReceiverThread duplexReceiverThread;
    SimplexThread simplexThread;
    Options options;


    public IOThreadManager(InputStream inputStream, OutputStream outputStream,Options options) {
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
        sender.init(outputStream);
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
        simplexThread = new SimplexThread(receiver, sender);
        simplexThread.start();
    }

    private void startDuplexMode() {
        shutDownAllThread(null);
        duplexReceiverThread = new DuplexReceiverThread(receiver);
        duplexSenderThread = new DuplexSenderThread(sender);
        duplexReceiverThread.start();
        duplexSenderThread.start();
    }

    @Override
    public void send(ISendable sendable) {
        sender.offer(sendable);
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
