package com.nxp.cccbu;

import android.app.Application;
import android.content.Context;

public class CCCBU extends Application {
    private static CCCBU thiz;
    private int mBleConnectedTimeStamp;
    private int mBlePhyUpdatedTimeStamp;

    public int getBleConnectedTimeStamp() {
        return mBleConnectedTimeStamp;
    }

    public void setBleConnectedTimeStamp(int mBleConnectedTimeStamp) {
        this.mBleConnectedTimeStamp = mBleConnectedTimeStamp;
    }

    public int getBlePhyUpdatedTimeStamp() {
        return mBlePhyUpdatedTimeStamp;
    }

    public void setBlePhyUpdatedTimeStamp(int mBlePhyUpdatedTimeStamp) {
        this.mBlePhyUpdatedTimeStamp = mBlePhyUpdatedTimeStamp;
    }

    public static CCCBU getInstance(){
        return thiz;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        thiz = this;
    }
}
