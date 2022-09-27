package com.mirkowu.xsocket.core;

import java.io.InputStream;
import java.io.OutputStream;

public interface IReceiver {
   void init(InputStream inputStream);


   byte[] receive();

   void close();
}
