package com.nxp.cccbu;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.ParcelUuid;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BleManager {
    private static final String LOG = "BleManager";
//    public static final String CCC_SERVICE_UUID = "0000fff5-0000-1000-8000-00805f9b34fb";
//    public static final String CCC_CHAR_UUID ="d3b5a130-9e23-4b3a-8be4-6b1ee5f980a3";

    public static final String CCC_UWB_SERVICE_UUID = "e0b5a130-9e23-4b3a-8be4-6b1ee5f980a3";
    public static final String CCC_UWB_CHAR_UUID = "e1b5a130-9e23-4b3a-8be4-6b1ee5f980a3";
    public static final String CCC_UWB_CHAR_ANCHOR_UUID = "e2b5a130-9e23-4b3a-8be4-6b1ee5f980a3";


    public static final String CCCD_UUID = "00002902-0000-1000-8000-00805f9b34fb";

    private BluetoothAdapter mAdapter;
    private BluetoothManager mBluetoothManager;
    private BluetoothSocket mSocket;
    private BluetoothDevice mDevice;
    private BluetoothGatt mGatt;

//    private InputStream mIs;
//    private OutputStream mOs;

    private int mAnchor_num;

    public int getmAnchor_num() {
        return mAnchor_num;
    }

    private Context mContext;
    private static String address;

    public static String getAddress() {
        return address;
    }


    public BluetoothDevice getDevice() {
        return mDevice;
    }

    public BleManager(Context context) {
        mContext = context;
        mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mAdapter = mBluetoothManager.getAdapter();
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return ;
        }
        address = mAdapter.getAddress();
//        createBondOutOfBand(null);
//        BleReflection br = new BleReflection(mContext,mAdapter);
//        try {
//            br.getLocalOobData();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mContext.registerReceiver(new BroadcastReceiver() {
            @SuppressLint("MissingPermission")
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                    if(mDevice!=null){
                        if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                            mGatt.discoverServices();
                        }
                    }
                }
            }
        },filter);
    }

    @SuppressLint("MissingPermission")
    public void startScan(){
        mAdapter.getBluetoothLeScanner().startScan(new ScanCallback());
    }

    private boolean createBondOutOfBand(final byte[] oobKey) {
//        mAdapter.readOutOfBandData();
        try {

            Method gen1 = mAdapter.getClass().getMethod("generateLocalOobData",
                    new Class[]{int.class,mContext.getMainExecutor().getClass(),
                            Class.forName("android.bluetooth.BluetoothAdapter$OobDataCallback")});
            Class<?>[] cls=mAdapter.getClass().getDeclaredClasses();
            for(Class<?> c:cls){
                if(c.getSimpleName().equals("OobDataCallback")){
                    Method gen = mAdapter.getClass().getMethod("generateLocalOobData",
                            new Class[]{int.class,mContext.getMainExecutor().getClass(),
                                    c});
                    gen.invoke(0,mContext.getMainExecutor(),null);

                }
            }
            Method[] ms1 = mAdapter.getClass().getMethods();
            for(Method m:ms1){
                Log.v("method",m.getName());
            }



            Method bond  = mDevice.getClass().getMethod("createBondOutOfBand",new Class[]{int.class,Class.forName("android.bluetooth.OobData")});
            bond.invoke(mDevice,0,null);
            Method ma = mAdapter.getClass().getMethod("readOutOfBandData",new Class[]{});
            Pair<byte[],byte[]> rst= (Pair<byte[], byte[]>) ma.invoke(mAdapter);
            Log.v("readOutOfBandData",ByteUtils.bytes2String(rst.first));
            Class c = Class.forName("android.bluetooth.OobData");
            Constructor<?>[] constructors = c.getConstructors();
            constructors[0].newInstance();
//            Constructor constr = c.getConstructor();
            Object oobData = c.newInstance();
            Method method = c.getMethod("setSecurityManagerTk", byte[].class);
            method.invoke(oobData, oobKey);

            Method m = mDevice.getClass().getMethod("createBondOutOfBand", int.class, c);
            boolean res = (boolean)m.invoke(mDevice, BluetoothDevice.TRANSPORT_LE, oobData);
       return res;

        }
        catch (Exception e) {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint({"MissingPermission", "NewApi"})
    public void connectL2CAP(){
//        if(mSocket != null){return;}
//        boolean isBonded =(mDevice.getBondState()==BluetoothDevice.BOND_BONDED);
//        try {
//            if(isBonded){
//                mSocket =mDevice.createL2capChannel(mPsm);
//            }else {
//                mSocket =mDevice.createInsecureL2capChannel(mPsm);
//            }
//            mSocket.connect();
//            mOs = mSocket.getOutputStream();
//            mIs = mSocket.getInputStream();

            Events.ConnectedEvent event = new Events.ConnectedEvent();
            event.mBle = this;
            EventBus.getDefault().post(event);

//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

//    public InputStream getmIs() {
//        return mIs;
//    }
//
//    public OutputStream getmOs() {
//        return mOs;
//    }

    @SuppressLint("MissingPermission")
    public void connect(BluetoothDevice device){
        device.connectGatt(mContext, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if(newState == BluetoothProfile.STATE_CONNECTED &&status ==BluetoothGatt.GATT_SUCCESS){
                    Log.v(LOG,"STATE_CONNECTED");
                    mGatt = gatt;
//                    CCCBU.getInstance().setBleConnectedTimeStamp(UwbManager.getInstance().getTime());
                    mDevice = device;
                    if(device.getBondState() == BluetoothDevice.BOND_BONDED){
                        gatt.discoverServices();
                    }
                }
                if(newState == BluetoothProfile.STATE_DISCONNECTED){
                    Log.v(LOG,"STATE_DISCONNECTED");
                    Events.DisconnectEvent event =new Events.DisconnectEvent();
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                Log.v(LOG,"onServicesDiscovered");
                if(status==BluetoothGatt.GATT_SUCCESS){
                    BluetoothGattService notify_service = gatt.getService(UUID.fromString(CCC_UWB_SERVICE_UUID));
                    BluetoothGattCharacteristic anchor_num_char = notify_service.getCharacteristic(UUID.fromString(CCC_UWB_CHAR_ANCHOR_UUID));
                    gatt.readCharacteristic(anchor_num_char);
                }

            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                Log.v(LOG,"onCharacteristicRead");
                if(status==BluetoothGatt.GATT_SUCCESS){
                    if(characteristic.getUuid().equals(UUID.fromString(CCC_UWB_CHAR_ANCHOR_UUID))){
                        byte[] anchor_num=characteristic.getValue();
                        mAnchor_num = ByteUtils.twoBytesToInt(anchor_num);
//                        connectL2CAP();
                        BluetoothGattService notify_service = gatt.getService(UUID.fromString(CCC_UWB_SERVICE_UUID));
                        BluetoothGattCharacteristic notify_characteristic= notify_service.getCharacteristic(UUID.fromString(CCC_UWB_CHAR_UUID));
                        gatt.setCharacteristicNotification(notify_characteristic,true);
                        BluetoothGattDescriptor descriptor = notify_characteristic.getDescriptor(UUID.fromString(CCCD_UUID));
                        if (descriptor != null) {
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            gatt.writeDescriptor(descriptor);
                        }
                    }
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                Log.v(LOG,"onCharacteristicChanged");
                Events.UpdateDistanceEvent event =new Events.UpdateDistanceEvent(characteristic.getValue());
                EventBus.getDefault().post(event);
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
                Log.v(LOG,"onDescriptorWrite");
                if(descriptor.getUuid().equals(UUID.fromString(CCCD_UUID))){
//                    BluetoothGattService service = gatt.getService(UUID.fromString(CCC_UWB_SERVICE_UUID));
//                    BluetoothGattCharacteristic characteristic= service.getCharacteristic(UUID.fromString(CCC_UWB_CHAR_ANCHOR_UUID));
//                    gatt.readCharacteristic(characteristic);
                    Events.StartRangingEvent evt = new Events.StartRangingEvent();
                    EventBus.getDefault().post(evt);

                }
            }

            @Override
            public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
                super.onPhyUpdate(gatt, txPhy, rxPhy, status);
                Log.v(LOG,"onPhyUpdate");
//                CCCBU.getInstance().setBlePhyUpdatedTimeStamp(UwbManager.getInstance().getTime());
//                Events.PhyUpdateEvent event = new Events.PhyUpdateEvent();
//                EventBus.getDefault().post(event);
            }

            @Override
            public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
                super.onPhyRead(gatt, txPhy, rxPhy, status);
            }
        });
    }


    class ScanCallback extends android.bluetooth.le.ScanCallback{
        public ScanCallback() {
            super();
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if(result.getDevice().getBondState()==BluetoothDevice.BOND_BONDED){
                if(result.getDevice().getName().startsWith("NXP_BLE_CAR")){
                    Events.DeviceScannedEvent event = new Events.DeviceScannedEvent();
                    event.setDevice(result.getDevice());
                    EventBus.getDefault().post(event);
                }
            }
            if(result.getScanRecord().getServiceUuids()==null){return;}
            if(result.getScanRecord().getServiceUuids().contains(ParcelUuid.fromString(CCC_UWB_SERVICE_UUID))){
                Events.DeviceScannedEvent event = new Events.DeviceScannedEvent();
                event.setDevice(result.getDevice());
                EventBus.getDefault().post(event);
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    }

    @SuppressLint("MissingPermission")
    public void stopScan(){
        mAdapter.getBluetoothLeScanner().stopScan(new ScanCallback());
    }

    @SuppressLint("MissingPermission")
    public void releaseBle(){
        if(mGatt !=null&&mDevice!=null){
            if(mBluetoothManager.getConnectionState(mDevice,BluetoothProfile.GATT) == BluetoothProfile.STATE_CONNECTED){
                mGatt.disconnect();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mDevice = null;
            mGatt = null;
        }
    }
}
