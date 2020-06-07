package com.utils;

import java.io.Serializable;

public class QrcodeScan implements Serializable {
    // uuid对应的qrcode的扫描状态
    private boolean scanFlag = false;

    public boolean IsScan() {
        return scanFlag;
    }

    public void setScan(boolean scanFlag) {
        this.scanFlag = scanFlag;
    }
    //多线程下持续获取扫描状态
    public synchronized boolean getScanStatus() {
        try {
            if (!IsScan())
                this.wait();
            //若解锁之后是Scan状态就会返回true，否则此次请求返回false
            if(IsScan()){
                System.out.println("扫描成功!");
                return true;
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return false;
    }
    //扫描之后调用
    public synchronized void scanSuccess(){
        try{
            System.out.println("已被扫描");
            setScan(true);
            this.notifyAll();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public synchronized void notifyPool(){  
        try  
        {  
            this.notifyAll();  
        } catch (Exception e)  
        {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
}