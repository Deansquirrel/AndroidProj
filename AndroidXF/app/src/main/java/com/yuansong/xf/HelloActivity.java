package com.yuansong.xf;

import android.os.Bundle;
import android.widget.TextView;

import com.yuansong.xf.Common.CommonFun;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        task = new TimerTask() {
            @Override
            public void run() {
                CommonFun.showActivity(HelloActivity.this,MainActivity.class,true);
            }
        };

        timer.schedule(task,2000);
    }

}
