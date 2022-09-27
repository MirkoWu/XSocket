package com.mirkowu.xsocket.core.exception;

public class ManualCloseException extends RuntimeException{
    public ManualCloseException( ) {
        super("手动关闭");
    }
}
