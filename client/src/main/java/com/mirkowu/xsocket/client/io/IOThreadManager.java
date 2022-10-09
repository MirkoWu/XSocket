package com.mirkowu.xsocket.client.io;

import com.mirkowu.xsocket.core.IPConfig;
import com.mirkowu.xsocket.client.Options;
import com.mirkowu.xsocket.core.IUdpSendData;
import com.mirkowu.xsocket.core.SocketType;
import com.mirkowu.xsocket.core.ISendData;
import com.mirkowu.xsocket.core.action.IActionDispatcher;
import com.mirkowu.xsocket.core.io.IReceiver;
import com.mirkowu.xsocket.core.io.ISender;
import com.mirkowu.xsocket.core.io.TcpReceiverImp;
import com.mirkowu.xsocket.core.io.TcpSenderImp;
import com.mirkowu.xsocket.core.exception.ManualCloseException;
import com.mirkowu.xsocket.core.io.IIOManager;
import com.mirkowu.xsocket.core.io.IOThreadMode;
import com.mirkowu.xsocket.core.io.UdpReceiverImp;
import com.mirkowu.xsocket.core.io.UdpSenderImp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;

public class IOThreadManager implements IIOManager {

    private ISender sender;
    private IReceiver receiver;
    private DuplexSenderThread duplexSenderThread;
    private DuplexReceiverThread duplexReceiverThread;
    private SimplexThread simplexThread;
    private Options options;
    private IActionDispatcher dispatcher;
    private boolean isTcp = true;
    private IPConfig ipConfig;

    private IOThreadManager(Options options, IActionDispatcher dispatcher) {
        this.options = options;
        this.dispatcher = dispatcher;


        if (options != null) {
            if (options.getReceiver() != null) {
                receiver = options.getReceiver();
            }
            if (options.getSender() != null) {
                sender = options.getSender();
            }

            isTcp = options.getSocketType() == SocketType.TCP;
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

    public IOThreadManager(InputStream inputStream, OutputStream outputStream, Options options, IActionDispatcher dispatcher) {
        this(options, dispatcher);
        receiver.initTcp(inputStream,dispatcher);
        sender.initTcp(outputStream, dispatcher);
    }

    public IOThreadManager(DatagramSocket datagramSocket, IPConfig ipConfig, Options options, IActionDispatcher dispatcher) {
        this(options, dispatcher);
        this.ipConfig=ipConfig;
        receiver.initUdp(datagramSocket,dispatcher);
        sender.initUdp(datagramSocket, dispatcher);
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
        if (isTcp) {
            sender.offer(sendData);
        } else {
            if (sendData instanceof IUdpSendData) {
                sender.offer(sendData);
            } else {
                sender.offer(new IUdpSendData() {
                    @Override
                    public String getIp() {
                        return ipConfig.ip;
                    }

                    @Override
                    public int getPort() {
                        return ipConfig.port;
                    }

                    @Override
                    public byte[] getData() {
                        return sendData.getData();
                    }
                });
            }
        }

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
