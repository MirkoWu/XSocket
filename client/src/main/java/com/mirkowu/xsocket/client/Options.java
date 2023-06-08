package com.mirkowu.xsocket.client;

import com.mirkowu.xsocket.client.connect.AbsReconnectionManager;
import com.mirkowu.xsocket.core.SocketType;
import com.mirkowu.xsocket.core.AbsReceiver;
import com.mirkowu.xsocket.core.AbsSender;
import com.mirkowu.xsocket.core.io.IOThreadMode;

public class Options {
    private SocketType socketType = SocketType.TCP;
    private AbsReceiver receiver;
    private AbsSender sender;
    private IOThreadMode ioThreadMode = IOThreadMode.DUPLEX;
    private AbsReconnectionManager reconnectionManager;
    private long pulseFrequency;
    private long pulseFeedLoseTimes;

    public Options setSocketType(SocketType socketType) {
        this.socketType = socketType;
        return this;
    }

    public Options setPulseFeedLoseTimes(long pulseFeedLoseTimes) {
        this.pulseFeedLoseTimes = pulseFeedLoseTimes;
        return this;
    }

    public Options setReceiver(AbsReceiver receiver) {
        this.receiver = receiver;
        return this;
    }

    public Options setSender(AbsSender sender) {
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

    public Options setPulseFrequency(long pulseFrequency) {
        this.pulseFrequency = pulseFrequency;
        return this;
    }

    public SocketType getSocketType() {
        return socketType;
    }

    public long getPulseFeedLoseTimes() {
        return pulseFeedLoseTimes;
    }

    public AbsReceiver getReceiver() {
        return receiver;
    }

    public AbsSender getSender() {
        return sender;
    }

    public IOThreadMode getIoThreadMode() {
        return ioThreadMode;
    }

    public AbsReconnectionManager getReconnectionManager() {
        return reconnectionManager;
    }

    public long getPulseFrequency() {
        return pulseFrequency;
    }

    public static Options defaultOptions() {
        Options options = new Options();
//        options.reconnectionManager = new DefaultReconnectManager();
        return options;
    }
}
