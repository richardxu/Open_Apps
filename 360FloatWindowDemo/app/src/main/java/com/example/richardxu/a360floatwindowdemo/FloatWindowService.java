package com.example.richardxu.a360floatwindowdemo;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FloatWindowService extends Service {
    public FloatWindowService() {
    }

    /**
     * 用于在线程中创建或移除悬浮窗
     */
    private Handler handler = new Handler();

    /**
     * 定时器，定时进行检测当前应该创建or移除悬浮窗
     *
     */
    private Timer timer;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //开启启动器，每0.5秒刷新一次
        if (timer == null)
        {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // Service 被终止的同时也停止定时器继续运行
        timer.cancel();
        timer = null;
    }

    class RefreshTask extends TimerTask {

        @Override
        public void run() {
            //当前界面时桌面，且没有悬浮窗显示，则创建悬浮窗.
            if (isHome() && !MyWindowManager.isWindowShowing()){
                Log.d("ricahrd", "refresh task ============= 111111111111");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager.createSmallWindow(getApplicationContext());
                    }
                });
            }
            //当前界面不是桌面，且有悬浮窗显示，则移除悬浮窗
            else if(!isHome() && MyWindowManager.isWindowShowing()){
                Log.d("ricahrd", "refresh task ============= 222222222222 ");
                /**
                 *  记录手指按下时在小悬浮窗的View上的纵坐标的值
                 */
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager.removeSmallWindow(getApplicationContext());
                        MyWindowManager.removeBigWindow(getApplicationContext());
                    }
                });
            }
            //当前界面时桌面，且有悬浮窗显示，则更新内存数据.
            else if(isHome() && MyWindowManager.isWindowShowing()){
                Log.d("ricahrd", "refresh task ============= 3333333333333 ");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager.updateUsedPercent(getApplicationContext());
                    }
                });
            }
        }
    }

    /**
     * 判断当前界面是否是桌面
     */
    private boolean isHome(){
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
//        Log.d("Richard", "isHome: =========== getPackageName()" + rti.get(0).topActivity.getPackageName());
//        return getHomes().contains(rti.get(0).topActivity.getPackageName());

        List<ActivityManager.RunningAppProcessInfo> pis = mActivityManager.getRunningAppProcesses();
        ActivityManager.RunningAppProcessInfo topAppProcess = pis.get(0);
        if(topAppProcess != null && topAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
            return getHomes().contains(topAppProcess.processName);
        }

        return  false;
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含有包名的字符串列表
     */
    private List<String> getHomes(){
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfos){
            names.add(ri.activityInfo.packageName);
        }
        Log.d("Richard", "============= getHomes: names: " + names);
        return names;
    }

}
