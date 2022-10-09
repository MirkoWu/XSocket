package com.mirkowu.xsocket.core;

public class IPConfig {
    public String ip;
    public int port;

    public IPConfig(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IPConfig) {
            IPConfig config = (IPConfig) obj;
            return this.ip.equals(config.ip) && this.port == config.port;
        }
        return super.equals(obj);
    }


    @Override
    public int hashCode() {
        int result = ip.hashCode();
        result = 31 * result + port;
        return result;
    }
}
