package com.mirkowu.xsocket.server;

import com.mirkowu.xsocket.core.SocketType;
import com.mirkowu.xsocket.core.io.AbsReceiver;
import com.mirkowu.xsocket.core.io.AbsSender;
import com.mirkowu.xsocket.core.io.IReceiver;
import com.mirkowu.xsocket.core.io.ISender;

import java.nio.ByteOrder;

public class ServerOptions implements IIOCoreOptions {
    private SocketType socketType = SocketType.TCP;
    private AbsReceiver receiver;
    private AbsSender sender;

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

    @Override
    public int getMaxConnectCapacity() {
        return 2;
    }

    @Override
    public AbsSender getSender() {
        return null;
    }

    @Override
    public AbsReceiver getReceiver() {
        return null;
    }

    @Override
    public ByteOrder getReadByteOrder() {
        return null;
    }

    @Override
    public int getMaxReadDataMB() {
        return 0;
    }

    @Override
    public ByteOrder getWriteByteOrder() {
        return null;
    }

    @Override
    public int getReadPackageBytes() {
        return 0;
    }

    @Override
    public int getWritePackageBytes() {
        return 0;
    }

    @Override
    public boolean isDebug() {
        return false;
    }

    public static ServerOptions getDefault() {
        ServerOptions serverOptions = new ServerOptions();
        return serverOptions;
    }
}
