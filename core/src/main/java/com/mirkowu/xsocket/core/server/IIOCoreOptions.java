package com.mirkowu.xsocket.core.server;


import com.mirkowu.xsocket.core.IReceiver;
import com.mirkowu.xsocket.core.ISender;

import java.nio.ByteOrder;

public interface IIOCoreOptions {

    ISender getSender();

    IReceiver getReceiver();

    ByteOrder getReadByteOrder();

    int getMaxReadDataMB();

///    IReaderProtocol getReaderProtocol();

    ByteOrder getWriteByteOrder();

    int getReadPackageBytes();

    int getWritePackageBytes();

    boolean isDebug();
    int getMaxConnectCapacity();

}
