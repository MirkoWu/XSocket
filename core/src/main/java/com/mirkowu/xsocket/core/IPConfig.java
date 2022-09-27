package com.mirkowu.xsocket.core;

import android.text.TextUtils;

import androidx.annotation.Nullable;

public class IPConfig {
    public String ip;
    public int port;

    public IPConfig(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof IPConfig) {
            IPConfig config = (IPConfig) obj;
            return TextUtils.equals(this.ip, config.ip) && this.port == config.port;
        }
        return super.equals(obj);
    }

}