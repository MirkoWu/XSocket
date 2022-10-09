package com.mirkowu.xsocket.server;


import com.mirkowu.xsocket.core.io.AbsReceiver;
import com.mirkowu.xsocket.core.io.AbsSender;
import com.mirkowu.xsocket.core.io.IReceiver;
import com.mirkowu.xsocket.core.io.ISender;

import java.nio.ByteOrder;

public interface IIOCoreOptions {

    AbsSender getSender();

    AbsReceiver getReceiver();

    ByteOrder getReadByteOrder();

    int getMaxReadDataMB();

///    IReaderProtocol getReaderProtocol();

    ByteOrder getWriteByteOrder();

    int getReadPackageBytes();

    int getWritePackageBytes();

    boolean isDebug();

    int getMaxConnectCapacity();

}
