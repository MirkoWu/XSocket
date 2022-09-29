package com.mirkowu.xsocket.client;

import com.mirkowu.xsocket.client.connect.AbsReconnectionManager;
import com.mirkowu.xsocket.client.connect.DefaultReconnectManager;
import com.mirkowu.xsocket.core.IReceiver;
import com.mirkowu.xsocket.core.ISender;
import com.mirkowu.xsocket.core.io.IOThreadMode;

public class Options {

    private IReceiver receiver;
    private ISender sender;
    private IOThreadMode ioThreadMode;
    private AbsReconnectionManager reconnectionManager;

    public Options setReceiver(IReceiver receiver) {
        this.receiver = receiver;
        return this;
    }

    public Options setSender(ISender sender) {
        this.sender = sender;
        return this;
    }

    public Options setIOThreadMode(IOThreadMode ioThreadMode) {
        this.ioThreadMode = ioThreadMode;
        return this;
    }

    public Options setReconnectionManager(AbsReconnectionManager reconnectionManager) {
        this.reconnectionManager = reconnectionManager;
        return this;
    }

    public IReceiver getReceiver() {
        return receiver;
    }

    public ISender getSender() {
        return sender;
    }

    public IOThreadMode getIoThreadMode() {
        return ioThreadMode;
    }

    public AbsReconnectionManager getReconnectionManager() {
        return reconnectionManager;
    }

    public static Options defaultOptions() {
        Options options = new Options();
        options.ioThreadMode = IOThreadMode.DUPLEX;
        options.reconnectionManager = new DefaultReconnectManager();
        return options;
    }
}
