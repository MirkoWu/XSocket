package com.mirkowu.xsocket.server;

import com.mirkowu.xsocket.core.IReceiver;
import com.mirkowu.xsocket.core.ISender;

import java.nio.ByteOrder;

public class ServerOptions implements IIOCoreOptions{

    @Override
    public int getMaxConnectCapacity() {
        return 2;
    }

    @Override
    public ISender getSender() {
        return null;
    }

    @Override
    public IReceiver getReceiver() {
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

    public static ServerOptions getDefault(){
        ServerOptions serverOptions=new ServerOptions();
        return serverOptions;
    }
}
