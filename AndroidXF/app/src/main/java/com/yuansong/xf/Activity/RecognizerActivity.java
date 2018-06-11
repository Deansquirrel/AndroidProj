package com.yuansong.xf.Activity;

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


public class RecognizerActivity extends BaseActivity {

    private Toolbar mToolbar = null;

    private TextView mTextView = null;
    private Button mButton = null;

    private RecStatus status = null;

    private IflytekHelper mIflytekHelper = null;

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
        mButton.setBackgroundColor(getResources().getColor(R.color.themeBlueColor));

        mButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.i("ACTION","DOWN");
                        startListener();
                        return  true;
                    case MotionEvent.ACTION_UP:
                        Log.i("ACTION","UP");
                        stopListener();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mIflytekHelper != null){
            mIflytekHelper.destroy();
        }
    }

    private void startListener(){
        mIflytekHelper.startListening(new IflytekHelper.RecognizerListener() {
            @Override
            public void preRecognize() {
                mButton.setBackgroundColor(getResources().getColor(R.color.themeRedColor));
                mButton.setText("录音中...");
            }

            @Override
            public void postRecognize() {
                mButton.setBackgroundColor(getResources().getColor(R.color.themeBlueColor));
                mButton.setText("开  始");
            }

            @Override
            public void onCompleted(String result) {
                mTextView.setText(result);
            }

            @Override
            public void onFailed(int errCode, String errDesc) {
                CommonFun.showError(RecognizerActivity.this,
                        errDesc + "(" + String.valueOf(errCode) + ")",
                        false);
            }
        });
    }

    private void stopListener(){
        mIflytekHelper.stopListening();
    }


}
