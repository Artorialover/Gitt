package com.bus.voicebroadcast.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.bus.voicebroadcast.debug.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class BleUtil {
    private static BleUtil bleUtil;
    private BluetoothLeScanner scanner;
    private List<ScanFilter> scanFilters;
    private ScanSettings scanSettings;

    public static BleUtil getInstance(Context context){
        if(bleUtil==null){
            synchronized (BleUtil.class){
                bleUtil=new BleUtil(context);
            }
        }
        return bleUtil;
    }
    public BleUtil(Context context) {
        if(!isSupportBle(context)){
            return;
        }
        scanner=BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        if(scanner!=null){
            scanFilters=new ArrayList<>();
            initScanPolicy();
        }
    }

    public void initScanPolicy(){
        ScanFilter filter=new ScanFilter.Builder().build();
        scanSettings=new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_BALANCED).build();
        scanFilters.add(filter);
    }

    public void startLeScan(){
        if(scanner!=null && scanSettings!=null && scanFilters!=null){
            LogUtil.d("-->startLeScan");
            scanner.startScan(scanFilters,scanSettings,scanCallback);
        }
    }

    public void stopLeScan(){
        if(scanner!=null && scanCallback!=null){
            scanner.stopScan(scanCallback);
        }
    }

    private ScanCallback scanCallback=new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            LogUtil.d("-->"+result.getDevice().getAddress());
        }
    };

    public static boolean isSupportBle(Context context){
        PackageManager packageManager=context.getPackageManager();
        return BluetoothAdapter.getDefaultAdapter()!=null && packageManager!=null && packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public static boolean isAddressValid(String address){
        return BluetoothAdapter.checkBluetoothAddress(address);
    }
}
