package com.mirkowu.xsocket.server;

import com.mirkowu.xsocket.core.SocketType;
import com.mirkowu.xsocket.core.AbsReceiver;
import com.mirkowu.xsocket.core.AbsSender;

public class ServerOptions implements IIOCoreOptions {
    private SocketType socketType = SocketType.TCP;
    private AbsReceiver receiver;
    private AbsSender sender;
    private int maxConnectCapacity = 50;
    private boolean isDebug = false;

    public SocketType getSocketType() {
        return socketType;
    }

    public ServerOptions setSocketType(SocketType socketType) {
        this.socketType = socketType;
        return this;
    }

    public ServerOptions setReceiver(AbsReceiver receiver) {
        this.receiver = receiver;
        return this;
    }

    public ServerOptions setSender(AbsSender sender) {
        this.sender = sender;
        return this;
    }
    public ServerOptions setDebug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    @Override
    public int getMaxConnectCapacity() {
        return maxConnectCapacity;
    }

    @Override
    public AbsSender getSender() {
        return sender;
    }

    @Override
    public AbsReceiver getReceiver() {
        return receiver;
    }

    @Override
    public boolean isDebug() {
        return isDebug;
    }

    public static ServerOptions getDefault() {
        ServerOptions serverOptions = new ServerOptions();
        return serverOptions;
    }
}
