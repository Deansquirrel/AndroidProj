package com.yuansong.features;

import android.os.Bundle;
import android.widget.TextView;

import com.yuansong.features.Common.CommonFun;

import java.util.Timer;
import java.util.TimerTask;

public class HelloActivity extends BaseActivity {

    private TextView mTextViewTitle = null;

    private final Timer timer = new Timer();
    private TimerTask task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        mTextViewTitle = findViewById(R.id.textView);

        mTextViewTitle.setText(getResources().getText(R.string.activity_hello_content));

        task = new TimerTask() {
            @Override
            public void run() {
                gotoMainActivity();
            }
        };

        timer.schedule(task,2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void gotoMainActivity(){
        CommonFun.showActivity(this,MainActivity.class,true);
    }


}
