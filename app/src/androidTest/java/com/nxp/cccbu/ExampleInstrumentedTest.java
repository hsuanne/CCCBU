package com.nxp.cccbu;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.nxp.cccbu", appContext.getPackageName());
    }
    @Test
    public  void SpakeTest(){
        int Nscrypt = 32768;
        int r = 8;
        int p = 1;
        int dkLen = 80;
//        Spake2.generage_scrypt(Nscrypt,r,p,dkLen);
        Spake2.cal_Xpoint();
//        Spake2.cal_M2(null);
    }

    @Test

    public void AESCCMTest(){
        byte[] key = new byte[]
                {0x0,0x01,0x02,0x03,
                        0x04,0x05,0x06,0x07,
                        0x08,0x09,0x0A,0x0B,
                        0x0C,0x0D,0x0E,0x0F};
        byte[] iv = new byte[]{0x31 ,0x32 ,0x33 ,0x34 ,0x35 ,0x36 ,0x37 ,0x38};
        byte[] plain = new byte[]{
                0x00, 0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,
                0x09, 0x08,0x07,0x06,0x05,0x04,0x03,0x02,0x01,0x00,
                0x01,0x02,0x03,0x04,0x05,0x06, 0x07,0x08,0x09,0x00,
                0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,0x00,0x00};
        byte[] encrypt = AESCCM.encrypt(plain,key,iv);
        Log.v("AESCCMTest",ByteUtils.bytes2String(encrypt ));
        Log.v("AESCCMTest",ByteUtils.bytes2String( AESCCM.getMac()));
//        1C3F4973BCB1BB427927B0E8AAFB4BB6
//        66EF9277
        Log.v("AESCCMTest",
                ByteUtils.bytes2String(
                        AESCCM.decrypt(encrypt,key,iv,AESCCM.getMac())));

    }
}