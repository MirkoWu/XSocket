package com.mirkowu.xsocket.core.server;

public interface IServerManager {
    void listen();

    boolean isLive();
    IClientPool<String,IClient> getClientPool();
    void shutdown();
}
