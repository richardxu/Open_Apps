package com.imooc.activitystack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class ActivitySingleTask extends Activity {

    private TextView mShowClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singletask);

        Log.d("Richard", "============= call onCreate ===========");

        mShowClass = (TextView) findViewById(R.id.show_class);
        mShowClass.setText(this.toString());
    }

    public void btnStandard(View view) {
        startActivity(new Intent(this, MainActivity.class));
        mShowClass.setText(this.toString());
    }

    public void btnSingleTop(View view) {
        startActivity(new Intent(this, ActivitySingleTop.class));
        mShowClass.setText(this.toString());
    }

    public void btnSingleTask(View view) {
        startActivity(new Intent(this, ActivitySingleTask.class));
        mShowClass.setText(this.toString());
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        Log.d("Richard", "============= call onNewIntent ===========");

        finish();   //增加finish；如果此ActivitySingleTask为主Activity，这样在退回到此Activity时，
        // 会把覆盖在它上面的Activity都清除掉，然后调用finis退出。
    }
}
