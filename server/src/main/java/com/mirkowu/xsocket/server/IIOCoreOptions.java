package com.mirkowu.xsocket.server;


import com.mirkowu.xsocket.core.AbsReceiver;
import com.mirkowu.xsocket.core.AbsSender;

public interface IIOCoreOptions {

    AbsSender getSender();

    AbsReceiver getReceiver();

//    ByteOrder getReadByteOrder();

//    int getMaxReadDataMB();

///    IReaderProtocol getReaderProtocol();

//    ByteOrder getWriteByteOrder();
//
//    int getReadPackageBytes();
//
//    int getWritePackageBytes();

    boolean isDebug();

    int getMaxConnectCapacity();

}
