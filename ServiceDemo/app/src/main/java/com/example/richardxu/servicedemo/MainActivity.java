package com.example.richardxu.servicedemo;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.icu.text.LocaleDisplayNames;
import android.nfc.Tag;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startService;
    private Button stopService;
    private Button bindService;
    private Button unbindService;

    private MyService.MyBinder myBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            myBinder = (MyService.MyBinder) service;
            myBinder.startDownload();
            Log.d("ServiceConnection", " ======== onServiceConnected ============ ");
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("ServiceConnection", " ======== onServiceDisConnected ============ ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "========= MainActivity thread id is " + Thread.currentThread().getId());

        startService = (Button) findViewById(R.id.start_service);
        stopService = (Button) findViewById(R.id.stop_service);
        bindService = (Button) findViewById(R.id.bind_service);
        unbindService = (Button) findViewById(R.id.unbind_service);

        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        bindService.setOnClickListener(this);
        unbindService.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.start_service:
                    Intent startIntent = new Intent(this, MyService.class);
                    startService(startIntent);
                    break;
                case R.id.stop_service:
                    Intent stopIntent = new Intent(this, MyService.class);
//                    startService(stopIntent);
                    stopService(stopIntent);
                    break;
                case R.id.bind_service:
                    Intent bindIntent = new Intent(this, MyService.class);
                    bindService(bindIntent, connection, BIND_AUTO_CREATE);
                    break;
                case R.id.unbind_service:
                    Log.d("MyService", "click Unbind Service button");
                    if(myBinder != null)
                        unbindService(connection);
                    break;
                default:
                    break;
            }
    }
}
