package com.mirkowu.xsocket.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReceiverImp implements IReceiver {
    public static final int BUFF_SIZE = 1024;
    InputStream inputStream;
    byte[] recBuffer = new byte[BUFF_SIZE];
    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    @Override
    public void init(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public byte[] receive() throws IOException {
//        try {
//            if (inputStream.available() <= 0) return null;
//            byte[] data = new byte[inputStream.available()];
//            inputStream.read(data);
//            return data;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            bos.reset();
            int len = inputStream.read(recBuffer);
            if (len != -1) {
                bos.write(recBuffer, 0, len);
                byte[] data = bos.toByteArray();
                return data;
            }else {
                throw new IOException("");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void close() {
        try {
            if (bos != null) {
                bos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
