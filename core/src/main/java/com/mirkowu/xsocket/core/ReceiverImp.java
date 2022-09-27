package com.mirkowu.xsocket.core;

import java.io.IOException;
import java.io.InputStream;

public class ReceiverImp implements IReceiver {
    InputStream inputStream;

    @Override
    public void init(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public byte[] receive() {
        try {
            if (inputStream.available() <= 0) return null;
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
//        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
//        int length;
//        while ((length = inputStream.read(recBuffer)) != -1) {
//            outSteam.write(recBuffer, 0, length);
//        }
//        outSteam.close();
//        byte[] rec = outSteam.toByteArray();
    }

    @Override
    public void close() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
