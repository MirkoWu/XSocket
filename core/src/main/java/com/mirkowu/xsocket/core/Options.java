package com.mirkowu.xsocket.core;

import com.mirkowu.xsocket.core.io.IOThreadMode;

public class Options {

    private IReceiver receiver;
    private ISender sender;
    private IOThreadMode ioThreadMode;

    public Options setReceiver(IReceiver receiver) {
        this.receiver = receiver;
        return this;
    }

    public Options setSender(ISender sender) {
        this.sender = sender;
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


    public static Options defaultOptions() {
        Options options = new Options();
        options.ioThreadMode = IOThreadMode.DUPLEX;
        return options;
    }
}
