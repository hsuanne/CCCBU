package com.nxp.cccbu;

import android.bluetooth.BluetoothDevice;

public class Events {
    public static class ConnectedEvent{
        public BleManager mBle;
    }

    public static class StartRangingEvent{
    }




    public static class DeviceScannedEvent{
        public BluetoothDevice getDevice() {
            return device;
        }

        public void setDevice(BluetoothDevice device) {
            this.device = device;
        }

        private BluetoothDevice device;

    }
    public static class ConnectDeviceEvent{
        public BluetoothDevice getDevice() {
            return device;
        }

        public void setDevice(BluetoothDevice device) {
            this.device = device;
        }

        private BluetoothDevice device;

    }
    public static class UpdateDistanceEvent{


        private int distanceA;
        private int distanceB;
        private int distanceC;
        private int distanceD;

        public UpdateDistanceEvent(byte[] raw) {
            distanceA = ByteUtils.twoBytesToInt(new byte[]{raw[0],raw[1]});
//            distanceB = ByteUtils.twoBytesToInt(new byte[]{raw[2],raw[3]});
//            distanceC = ByteUtils.twoBytesToInt(new byte[]{raw[4],raw[5]});
//            distanceD = ByteUtils.twoBytesToInt(new byte[]{raw[6],raw[7]});
        }

        public int getDistanceA() {
            return distanceA;
        }

        public int getDistanceB() {
            return distanceB;
        }

        public int getDistanceC() {
            return distanceC;
        }

        public int getDistanceD() {
            return distanceD;
        }
    }

    public static class PhyUpdateEvent{

    }

    public static class DisconnectEvent{

    }


    public static class InfoEvent {

        private String info;

        public InfoEvent(String info){
            this.info = info;
        }

        public String getInfo(){
            return info;
        }
    }

}
