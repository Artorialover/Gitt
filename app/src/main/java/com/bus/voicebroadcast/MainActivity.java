package com.bus.voicebroadcast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContentResolverCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.functions.Consumer;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.bus.voicebroadcast.ble.BleUtil;
import com.bus.voicebroadcast.ble.ScanService;
import com.bus.voicebroadcast.viewmodel.MyLifecycleObserver;
import com.bus.voicebroadcast.viewmodel.SeatViewModel;
import com.bus.voicebroadcast.viewmodel.ViewModelFactory;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhang.audiolibrary.AudioUtil;
import com.zhang.netlibrary.download.AndroidDownloadManager;
import com.zhang.netlibrary.download.AndroidDownloadManagerListener;

public class MainActivity extends AppCompatActivity {
    private String TAG="voicebroadcast";
    private RxPermissions rxPermissions;
    private final int REQUEST_BLUETOOTH_CODE = 1;
    private SeatViewModel seatViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BLUETOOTH_CODE && resultCode == RESULT_OK) {
            //startTask();
        } else if (requestCode == REQUEST_BLUETOOTH_CODE) {
            showToast("请允许打开蓝牙");
        }
    }

    void init() {
        seatViewModel = new ViewModelProvider(this,new ViewModelFactory(2)).get(SeatViewModel.class);
        seatViewModel.getCounter().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(TAG, "onChanged: "+integer);
            }
        });
        getLifecycle().addObserver(new MyLifecycleObserver(getLifecycle()));
        rxPermissions = new RxPermissions(this);
        requestPermissions();
    }


    void requestPermissions() {
        rxPermissions.requestEachCombined(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_CONTACTS
        ).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (permission.granted) {//全部同意后调用
                    openBlueAdapter();
                } else if (permission.shouldShowRequestPermissionRationale) {//只要有一个选择：禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                    showToast("请允许权限");
                } else {//只要有一个选择：禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示
                    showToast("请到设置中打开权限");
                }
            }
        });
    }

    void openBlueAdapter() {
        if (!BleUtil.isSupportBle(this)) {
            showToast("本设备不支持BLE");
            return;
        }
        if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            //执行操作
            //startTask();
            //download();
            //AudioUtil.getInstance(this).speak("hello,google");
            readContacts();
        } else {
            //未打开蓝牙
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_BLUETOOTH_CODE);
        }
    }

    void startTask() {
        Intent intent = new Intent(this, ScanService.class);
        startService(intent);
    }

    void readContacts(){
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null
                , null, null, null);
        if(cursor==null){
            return;
        }
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String tel=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.d("读取联系人-->", name+"   "+tel);
        }
    }

    private final String ApkUrl = "https://appdlcs.hicloud.com/dl/appdl/application/apk/1b/1bd2ad1f9cc54ae0aff86d60e7b07097/cn.wch.bledemo.1912161426.apk?sign=portal@portal1579164863000&amp;source=portalsite&amp;maple=0&amp;distOpEntity=HWSW";

    void download() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},111);
            return;
        }else {
            new AndroidDownloadManager(this, ApkUrl, "测试.apk", new AndroidDownloadManagerListener() {
                @Override
                public void onPrepare() {
                    Log.d(TAG, "onPrepare");
                }

                @Override
                public void onSuccess(String path) {
                    Log.d(TAG, "onSuccess: "+path);
                }

                @Override
                public void onFailed(Throwable throwable) {
                    Log.d(TAG, "onFailed");
                }
            }).download();
            /*try {
                NetUtil.startDownloadNewApk(this, ApkUrl, "/test/target.apk");
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }

    void showToast(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
