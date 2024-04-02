package com.nxp.cccbu;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceViewBuilder {
    private static final String LOG = "DeviceViewBuilder";
    private Activity mActivity;
    private AlertDialog mDialog;
    private RecyclerView mlistView;
    private List<BluetoothDevice> mDeviceList = new ArrayList<>();
    private BLEDeviceAdapter mAdapter;
    private BleManager mBle ;

//    private Timer mTimer;
//    private TimerTask mTimerTask;


    public DeviceViewBuilder(Activity act, BleManager ble){
        mActivity=act;
        mBle = ble;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
        dialogBuilder.setTitle("CARS");
        View view = mActivity.getLayoutInflater().inflate(R.layout.device_list_layout, null);
        view.setBackgroundColor(Color.BLACK);
        dialogBuilder.setView(view);
        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBle.stopScan();
            }
        });
        mDialog = dialogBuilder.create();
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.7f;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        mDialog.setContentView(view);

        mlistView = view.findViewById(R.id.listView);
        mlistView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new BLEDeviceAdapter();
        mlistView.setAdapter(mAdapter);
        EventBus.getDefault().register(this);

    }

    public void showDeviceView(){
        mDialog.show();
        scanDevice();
    }

    public void scanDevice(){
        mBle.startScan();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Events.DeviceScannedEvent e){
//        if(e.getDevice().getName()==null){return;}
//        if(!e.getDevice().getName().startsWith("NXP_CAR")){
//            return;
//        }
        if(!mDeviceList.contains(e.getDevice())){
            mDeviceList.add(e.getDevice());
            mAdapter.notifyDataSetChanged();
        }


    }
    static class BLEDeviceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.device_name)
        TextView name;

        @BindView(R.id.device_mac_address)
        TextView mac;

        @BindView(R.id.device_bond_state)
        TextView bond;

        @BindView(R.id.device_rssi)
        TextView rssi;

        @BindView(R.id.set_phy)
        Button setPhy;

        public BLEDeviceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    private final View.OnClickListener mDeviceClickHandler = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int itemPosition = mlistView.getChildAdapterPosition(v);
            BluetoothDevice device = mDeviceList.get(itemPosition);
            mDialog.dismiss();
            Events.ConnectDeviceEvent event = new Events.ConnectDeviceEvent();
            event.setDevice(device);
            EventBus.getDefault().post(event);
        }
    };
    class BLEDeviceAdapter extends RecyclerView.Adapter<BLEDeviceHolder> {

        @Override
        public int getItemCount() {
            return mDeviceList == null ? 0 : mDeviceList.size();
        }

        @Override
        public BLEDeviceHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mActivity.getLayoutInflater().inflate(R.layout.ble_device_item, viewGroup, false);
            view.setOnClickListener(mDeviceClickHandler);
            return new BLEDeviceHolder(view);
        }

        @Override
        public void onBindViewHolder(final BLEDeviceHolder holder, final int position) {
            BluetoothDevice device = mDeviceList.get(position);
            if(position%2==0){
                holder.itemView.setBackgroundColor(Color.GRAY);
            }else{
                holder.itemView.setBackgroundColor(Color.WHITE);
            }

            holder.name.setText(TextUtils.isEmpty(device.getName()) ? "Unknown" : device.getName());
            holder.mac.setText(device.getAddress());
            holder.bond.setText(device.getBondState() == BluetoothDevice.BOND_BONDED ? "Bonded" : "Unbonded");
//            holder.rssi.setText(bleDevice.getRssi() + " dBm");
        }
    }
}
