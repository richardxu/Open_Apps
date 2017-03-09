package com.example.richardxu.servicedemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }

    public static final String TAG = "MyService";

    private MyBinder mBinder = new MyBinder();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("MyService", "========= MyService thread id is " + Thread.currentThread().getId());

        Log.d(TAG, "onCreate() executed");

//        Notification notification = new Notification(R.mipmap.ic_launcher,
//                "有通知到来", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
//        notification.setLatestEventInfo(this, "这是通知的标题",
//                "这是通知的内容",pendingIntent);
        Notification.Builder builder = new Notification.Builder(this)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("这是通知的标题");
        Notification notification = builder.build();
        startForeground(1, notification);   //调到前台
        Log.d(TAG, "onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStartCommand() executed");

        /*  -------- Richard： 测试在Service的主线程中跑耗时操作；过后出现anr问题：/data/ant/trace.txt
        try{
            Thread.sleep(6000000);
        }catch (Exception e)
        {
            e.printStackTrace();
        }*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "========== doing something ==========");
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MyService", "====== onBind ============");
        return mBinder;
    }

    class MyBinder extends Binder {
        public void startDownload() {

            //Thread.sleep(6000000);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "====== doing another thing =======");
                }
            });

            Log.d(TAG, "startDownload() executed");
        }
    }
}
