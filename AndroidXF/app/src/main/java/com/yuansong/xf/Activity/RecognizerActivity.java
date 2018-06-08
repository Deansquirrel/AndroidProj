package com.yuansong.xf.Activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yuansong.xf.BaseActivity;
import com.yuansong.xf.Common.CommonFun;
import com.yuansong.xf.R;
import com.yuansong.xf.XF.IflytekHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.sql.Time;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class RecognizerActivity extends BaseActivity {

    private Toolbar mToolbar = null;

    private TextView mTextView = null;
    private Button mButton = null;

    private RecStatus status = null;

    private Calendar mCalendar = null;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;

    private String mText = "";
    private Handler handler = null;

    private IflytekHelper mIflytekHelper = null;

    private static final int HANDLE_TYPE_UPDATE = 1;

    private enum RecStatus{
        Waiting,
        Recording,
        Translating,
        Speaking
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognizer);

        status = RecStatus.Waiting;

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("语音听写");
        setSupportActionBar(mToolbar);

        mTextView = findViewById(R.id.textView);
        mTextView.setText("");

        mButton = findViewById(R.id.btnButton);
        mButton.setText("开  始");

        mTextView.setText("OFF");

        mButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.i("ACTION","DOWN");
                        mTextView.setText("ON");
                        return  true;
                    case MotionEvent.ACTION_UP:
                        Log.i("ACTION","UP");
                        mTextView.setText("OFF");
                        return true;
                }
                return false;
            }
        });

        mButton.setEnabled(false);
        mIflytekHelper = new IflytekHelper(RecognizerActivity.this);
        mIflytekHelper.InitRecognizer(new IflytekHelper.InitListener() {
            @Override
            public void onSuccess() {
                Log.i("msg","语音听写初始化成功");
                mButton.setEnabled(true);
            }

            @Override
            public void onFailed(int errCode) {
                String msg = "语音听写初始化失败（" + String.valueOf(errCode) + "）";
                CommonFun.showMsg(RecognizerActivity.this,msg);
                Log.i("err",msg);
                RecognizerActivity.this.finish();
            }
        });

        setLightWindow();
        showBackOption();
    }

}
