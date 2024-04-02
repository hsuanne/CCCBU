package com.nxp.cccbu;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import static com.nxp.cccbu.CommonConfigs.uwbInitiationTime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;

import com.dxjia.library.ImageTextButton;

public class MainActivity extends AppCompatActivity {
    private static final String LOG = "MainActivity";

    private static final int REQUEST_PERMISSION_CODE = 2710;

    private BleManager mBle;
    private UwbManager mUwb;
//    private Communicator mCom;

    //    @BindView(R.id.scan_connect)
//    Button scan_connect;
//
//    @BindView(R.id.start_ranging)
//    Button start_ranging;
    @BindView(R.id.tv_status)
    TextView mTvStatus;

    @BindView(R.id.chart)
    LineChart mChart;

    @BindView(R.id.btn_start)
    ImageTextButton mBtnStart;

    @BindView(R.id.btn_lock)
    ImageTextButton mBtnLock;

    @BindView(R.id.btn_alert)
    ImageTextButton mBtnAlert;

    @BindView(R.id.tv_distance)
    Button mDistance;

    @BindView(R.id.circle_out)
    ImageView mCircleOut;

    @BindView(R.id.circle_middle)
    ImageView mCircleMiddle;

    @BindView(R.id.circle_inner)
    ImageView mCircleInner;

    @BindView(R.id.sector_out)
    RingPercentView mSector_out;

    @BindView(R.id.sector_middle)
    RingPercentView mSector_middle;

    @BindView(R.id.sector_inner)
    RingPercentView mSector_inner;


    @BindView(R.id.info)
    TextView mInfo;

    private int index = 0;
    private int circleMode = -1;
    AlphaAnimation alphaAnimationInner;
    AlphaAnimation alphaAnimationMiddle;
    AlphaAnimation alphaAnimationOut;

    private Events.UpdateDistanceEvent uptdstsevt = null;
    private Handler mainLooper;
    private final Runnable mChartRefresher = new Runnable() {
        @Override
        public void run() {
            refreshChart();
        }
    };

    private boolean isStartTimeSync = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_board);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(0));
        ButterKnife.bind(this);
        requestPermission();
        EventBus.getDefault().register(this);
        mBle = new BleManager(this);
        mUwb = UwbManager.getInstance();

        mainLooper = new Handler(Looper.getMainLooper());
        alphaAnimationInner = (AlphaAnimation) AnimationUtils.loadAnimation(this, R.anim.animation_outest);
        alphaAnimationMiddle = (AlphaAnimation) AnimationUtils.loadAnimation(this, R.anim.animation_outest);
        alphaAnimationOut = (AlphaAnimation) AnimationUtils.loadAnimation(this, R.anim.animation_outest);

        circleMode = 2;
        mSector_inner.setVisibility(View.VISIBLE);
        mSector_middle.setVisibility(View.VISIBLE);
        mSector_out.setVisibility(View.VISIBLE);


//        drawSector(90);
    }

    private void drawSector(int start) {
//        mSector_out.setBg(0, 360,R.color.this_red);
        mSector_out.setFrontColor(Color.RED);
//        mSector_out.setPrimaryTextParam(100,Color.rgb(255, 255, 255), "%");
//        mSector_out.setSecondryTextParam(60, Color.rgb(255, 255, 255), "CPU");
//        int start = 0;
//        if(!isTop){
//            start =180;
//        }

        mSector_out.setRingWidth(40);
        mSector_out.drawCircleRing(start, 25, 500);


//        mSector_middle.setBg(0, 360,R.color.this_yellow);
        mSector_middle.setFrontColor(Color.YELLOW);
        mSector_middle.setRingWidth(40);
//        mSector_middle.setPrimaryTextParam(100,Color.rgb(255, 255, 255), "%");
//        mSector_middle.setSecondryTextParam(60, Color.rgb(255, 255, 255), "CPU");
        mSector_middle.drawCircleRing(start, 25, 50);

//        mSector_inner.setBg(0, 360,R.color.this_green);
        mSector_inner.setFrontColor(Color.GREEN);
        mSector_inner.setRingWidth(50);
//        mSector_inner.setPrimaryTextParam(100,Color.rgb(255, 255, 255), "%");
//        mSector_inner.setSecondryTextParam(60, Color.rgb(255, 255, 255), "CPU");
        mSector_inner.drawCircleRing(start, 25, 500);
//        arcView=(RingPercentView) findViewById(R.id.arc_percent);
//        arcView.setBg(160, 220,Color.rgb(30, 96, 200));
//        arcView.setFrontColor(Color.rgb(255, 255, 255));
//        arcView.setPrimaryTextParam(100,Color.rgb(255, 255, 255), "%");
//        arcView.setSecondryTextParam(60, Color.rgb(255, 255, 255), "硬盘");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBle.releaseBle();
    }

    /*-----------------------------------------setting--menu---------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_scan) {
            DeviceViewBuilder builder = new DeviceViewBuilder(this, mBle);
            builder.showDeviceView();
            clearChart();
            return true;
        }
        if (id == R.id.menu_s_phy) {
//            clearChart();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*----------------------------------------------------------------------------------------*/


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Events.DisconnectEvent evt){
        clearChart();
    }


    //connect ble device(used for manual selection in scan list )
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent( Events.ConnectDeviceEvent event){
        isStartTimeSync = false;
        clearChart();
        mBle.releaseBle();
        mBle.connect(event.getDevice());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Events.StartRangingEvent event){
        mUwb.stopRanging();
        mUwb.sessionDeInitialize();
        CommonConfigs.noOfControlee = (byte) mBle.getmAnchor_num();
        if(CommonConfigs.noOfControlee >1){
            CommonConfigs.multiNodeMode= 1;
        }else {
            CommonConfigs.multiNodeMode= 0;
        }


        mUwb.setAppConfig();

        mUwb.startRanging();
    }

    //update UI after receive distance data from ble
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Events.UpdateDistanceEvent event){

        if(uptdstsevt == null) {
            uptdstsevt = event;
            refreshChart();
        }else {
            uptdstsevt = event;
        }
    }



    private void switchSectorAnimation(){

        mainLooper.post(()->{
            if(mSector_inner.getAnimation()!=null){
                mSector_inner.getAnimation().cancel();
                mSector_inner.clearAnimation();
            }

            if(mSector_middle.getAnimation()!=null){
                mSector_middle.getAnimation().cancel();
                mSector_middle.clearAnimation();
            }

            if(mSector_out.getAnimation()!=null){
                mSector_out.getAnimation().cancel();
                mSector_out.clearAnimation();
            }

            switch (circleMode){
                case 0:
                    mSector_middle.setVisibility(View.GONE);
                    mSector_out.setVisibility(View.GONE);
                    mSector_inner.setVisibility(View.VISIBLE);
                    mSector_inner.startAnimation(alphaAnimationInner);
                    break;
                case 1:
                    mSector_middle.setVisibility(View.VISIBLE);
                    mSector_out.setVisibility(View.GONE);
                    mSector_inner.setVisibility(View.VISIBLE);
                    mSector_middle.startAnimation(alphaAnimationMiddle);
                    break;
                case 2:
                    mSector_middle.setVisibility(View.VISIBLE);
                    mSector_out.setVisibility(View.VISIBLE);
                    mSector_inner.setVisibility(View.VISIBLE);
                    mSector_out.startAnimation(alphaAnimationOut);
                    break;
                case 4:
                    mSector_middle.setVisibility(View.GONE);
                    mSector_out.setVisibility(View.GONE);
                    mSector_inner.setVisibility(View.GONE);
                    break;
            }
        });

    }

    private void updateCircle(int value){
        Log.v("updateCircle",value+"");
        int cm = -1;
        if(value<200){
            cm =0;
        }else if(value<500){
            cm=1;
        }else {
            cm=2;
        }
        if(value == -1){
            cm = 4;
        }
        if(cm != circleMode){
            circleMode =cm;
//            switchAnimation();
            switchSectorAnimation();
        }
    }

    private void switchAnimation(){

        mainLooper.post(()->{
            if(mCircleInner.getAnimation()!=null){
                mCircleInner.getAnimation().cancel();
                mCircleInner.clearAnimation();
            }

            if(mCircleMiddle.getAnimation()!=null){
                mCircleMiddle.getAnimation().cancel();
                mCircleMiddle.clearAnimation();
            }

            if(mCircleOut.getAnimation()!=null){
                mCircleOut.getAnimation().cancel();
                mCircleOut.clearAnimation();
            }

            switch (circleMode){
                case 0:
                    mCircleMiddle.setVisibility(View.GONE);
                    mCircleOut.setVisibility(View.GONE);
                    mCircleInner.setVisibility(View.VISIBLE);
                    mCircleInner.startAnimation(alphaAnimationInner);
                    break;
                case 1:
                    mCircleMiddle.setVisibility(View.VISIBLE);
                    mCircleOut.setVisibility(View.GONE);
                    mCircleInner.setVisibility(View.VISIBLE);
                    mCircleMiddle.startAnimation(alphaAnimationMiddle);
                    break;
                case 2:
                    mCircleMiddle.setVisibility(View.VISIBLE);
                    mCircleOut.setVisibility(View.VISIBLE);
                    mCircleInner.setVisibility(View.VISIBLE);
                    mCircleOut.startAnimation(alphaAnimationOut);
                    break;
                case 4:
                    mCircleMiddle.setVisibility(View.GONE);
                    mCircleOut.setVisibility(View.GONE);
                    mCircleInner.setVisibility(View.GONE);
                    break;
            }
        });

    }

    private void addValueToChart(Events.UpdateDistanceEvent evt) {
        int value1 = evt.getDistanceA();
        int value2 = evt.getDistanceB();
        int value3 = evt.getDistanceC();
        int value4 = evt.getDistanceD();
        Log.v(LOG,"v1: "+value1 +" v2: "+value2+" v3: "+value3+" v4: "+value4);
        if(value1>value2){
            if(value3>value4){
//                drawSector(0);
            }else{
//                drawSector(90);
            }
        }else {
            if(value3>value4){
//                drawSector(270);
            }else{
//                drawSector(180);
            }
        }
        updateCircle(Math.min(Math.min(value1,value2),Math.min(value3,value4)));
        LineData lineData = mChart.getData();
        mDistance.setText(value1+"");

        if (value1 > mChart.getAxisLeft().getAxisMaximum()) {
            mChart.getAxisLeft().setAxisMaxValue(value1);
        }
        if (value1 < mChart.getAxisLeft().getAxisMinimum()) {
            mChart.getAxisLeft().setAxisMinValue(value1);
        }
        if(index>mChart.getXAxis().getAxisMaximum()){
            mChart.getXAxis().setAxisMaximum(index);
        }
        if(value1>0){
            lineData.addEntry(new Entry(index,value1), 0);
        }
        if(value2>0){
            lineData.addEntry(new Entry(index,value2), 1);
        }
        if(value3>0){
            lineData.addEntry(new Entry(index,value3), 2);
        }
        if(value4>0){
            lineData.addEntry(new Entry(index,value4), 3);
        }
        index++;

        if(index>100){
            if(value1>0){
                Entry ent1= lineData.getDataSetByIndex(0).getEntryForIndex(0);
                lineData.removeEntry(ent1,0);
            }
            if(value2>0){
                Entry ent2= lineData.getDataSetByIndex(1).getEntryForIndex(0);
                lineData.removeEntry(ent2,1);
            }
            if(value3>0){
                Entry ent3= lineData.getDataSetByIndex(2).getEntryForIndex(0);
                lineData.removeEntry(ent3,2);
            }
            if(value4>0){
                Entry ent4= lineData.getDataSetByIndex(3).getEntryForIndex(0);
                lineData.removeEntry(ent4,3);
            }
        }

    }

    private void clearChart(){
        configChart();
        index =0;
        uptdstsevt = null;
    }
    private void refreshChart() {
        addValueToChart(uptdstsevt);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
        mChart.setVisibility(View.VISIBLE);
        mainLooper.postDelayed(mChartRefresher, 500);

    }
    private void configChart() {
        LineDataSet dataSet1 = new LineDataSet(new ArrayList<Entry>(), null);
        dataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet1.setDrawCircles(false);
        dataSet1.setDrawValues(false);
        dataSet1.setLineWidth(2);
        dataSet1.setColor(Color.WHITE);

        LineDataSet dataSet2 = new LineDataSet(new ArrayList<Entry>(), null);
        dataSet2.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet2.setDrawCircles(false);
        dataSet2.setDrawValues(false);
        dataSet2.setLineWidth(2);
        dataSet2.setColor(Color.RED);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet1);
        dataSets.add(dataSet2);

//        LineData data = new LineData(dataSet1);//new LineData(new ArrayList<String>(), dataSets);
        LineData data = new LineData( dataSets);
        // x values start from 0 to 20 (21 values total)
        for (int i = 0; i <= 20; ++i) {
            if (i % 5 == 0) {
//                data.getXVals().add(i + "");
            } else {
//                data.getXVals().add("");
            }
        }
        mChart.setData(data);

        mChart.setBackgroundColor(Color.TRANSPARENT);
        mChart.setDrawGridBackground(false);
        mChart.getLegend().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setStartAtZero(false);
        mChart.getAxisLeft().setAxisMinValue(0);
        mChart.getAxisLeft().setAxisMaxValue(200);
        mChart.getXAxis().setAxisMaximum(10);
        //Get original Dimension
        float scaleRatio = getResources().getDisplayMetrics().density;
//        float dimenPix = getResources().getDimension(com.dxjia.library.R.dimen.default_text_size);
        float dimenPix = 12.f;//getResources().getDimension();
        float dimenOrginal = dimenPix/scaleRatio/2;
        mChart.getAxisLeft().setTextSize(dimenOrginal);
        mChart.getAxisLeft().setTextColor(Color.WHITE);
        mChart.getXAxis().setTextColor(Color.WHITE);
        mChart.getXAxis().setLabelCount(10,false);
        mChart.getXAxis().setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                if (value > 0) {
                    int intValue = (int) value;
                    if (intValue % 2 == 0) {
                        return intValue + "";
                    }
                }
                return "";
            }
        });
        mChart.getAxisLeft().setLabelCount(10, false);
        mChart.getAxisLeft().setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                if (value > 0) {
                    int intValue = (int) value;
                    if (intValue % 10 == 0) {
                        return intValue + "";
                    }
                }
                return "";
            }
        });
        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.getXAxis().setTextSize(dimenOrginal);
//        mChart.getXAxis().
        mChart.getXAxis().setAxisLineWidth(2.f);
        mChart.getAxisLeft().setAxisLineWidth(2.f);
        mChart.getXAxis().setAxisLineColor(Color.YELLOW);
        mChart.getAxisLeft().setAxisLineColor(Color.YELLOW);
        Description d = new Description();
        d.setPosition(150,200);
        d.setTextColor(Color.WHITE);
        d.setText("cm");
        mChart.setDescription(d);
        mChart.setTouchEnabled(false);
        mChart.invalidate();
//        mChartContainer.setVisibility(View.INVISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Events.InfoEvent evt){
        mInfo.append(evt.getInfo()+"\r\n");
    }
/*----------------------------------grant permission----------------------------------------------------------*/
private static String[] permissions = {
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_PRIVILEGED
};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_PERMISSION_CODE == requestCode) {
            boolean grantedAccessCoarseLocation = true;
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permission)) {
                    grantedAccessCoarseLocation = grantResults[i] == PERMISSION_GRANTED;
                }
            }
            //
            if (grantedAccessCoarseLocation) {
            } else {
                Toast.makeText(this, "Please grant permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void requestPermission() {

        if (SdkUtils.hasMarshmallow()) {
            for(String permission :permissions){
                if (PERMISSION_GRANTED != checkSelfPermission(permission)){
                    if (!shouldShowRequestPermissionRationale(permission)){

//                        showMessageOKCancel(getString(R.string.grant_permission),
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        requestPermissions(permissions, REQUEST_PERMISSION_CODE);
//                                    }
//                                });
//                        return;
                    }
                    requestPermissions(permissions, REQUEST_PERMISSION_CODE);
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    /*-----------------------------------------------------------------------------------------------------------*/
}