package com.utils;

import java.util.concurrent.ConcurrentHashMap;

public class QrcodePool {
    public static ConcurrentHashMap<String,QrcodeScan> cacheMap = new ConcurrentHashMap<>();
    static{
        new Thread(new Runnable(){
        
            @Override
            public void run() {
                // TODO Auto-generated method stub
            }
        });
    }
}