package com.mirkowu.xsocket.client;

import com.mirkowu.xsocket.core.XLog;

import java.util.Iterator;
import java.util.ServiceLoader;

public class SPIUtils {

    public static <E> E load(Class<E> clz) {
        if (clz == null) {
            XLog.e("load null clz error!");
            return null;
        }
        ServiceLoader<E> serviceLoader = ServiceLoader.load(clz, clz.getClassLoader());
        Iterator<E> it = serviceLoader.iterator();
        try {
            if (it.hasNext()) {
                E service = it.next();
                return service;
            }
        } catch (Throwable throwable) {
            XLog.e("load " + clz.getSimpleName() + " error! " + throwable.getMessage());
        }
        return null;
    }
}
