package com.yuansong.xf.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yuansong.xf.BaseActivity;
import com.yuansong.xf.Common.CommonFun;
import com.yuansong.xf.R;
import com.yuansong.xf.XF.IflytekHelper;

public class AIUIActivity extends BaseActivity {

    private Toolbar mToolbar = null;

    private TextView mTextViewDesc = null;
    private TextView mTextViewResult = null;
    private Button mBtnSet = null;
    private Button mBtnClear = null;

    private IflytekHelper mIflytekHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aiui);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("语义理解");
        setSupportActionBar(mToolbar);

        mTextViewDesc = findViewById(R.id.textViewDesc);
        mTextViewDesc.setVisibility(View.VISIBLE);
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("可用技能：")
                .append("\n")
                .append("  显示{Number}号桌菜品");
        mTextViewDesc.setText(strBuilder.toString());

        mTextViewResult = findViewById(R.id.textViewResult);
        mTextViewResult.setVisibility(View.GONE);
        mTextViewResult.setText("");

        mBtnSet = findViewById(R.id.btnSet);
//        mBtnSet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mTextViewDesc.setVisibility(View.GONE);
//                mTextViewResult.setText("TEST");
//                mTextViewResult.setVisibility(View.VISIBLE);
//                mBtnSet.setVisibility(View.GONE);
//                mBtnClear.setVisibility(View.VISIBLE);
////                mIflytekHelper.understand();
//                mIflytekHelper.understandText("显示6桌菜品", new IflytekHelper.TextUnderstanderListener() {
//                    @Override
//                    public void preUnderstand() {
//
//                    }
//
//                    @Override
//                    public void postUnderstand() {
//
//                    }
//
//                    @Override
//                    public void onCompleted(String result) {
//                        Log.i("result",result);
//                    }
//
//                    @Override
//                    public void onFailed(int errCode, String errDesc) {
//                        Log.i("err",String.valueOf(errCode) + " | " + errDesc);
//                    }
//                });
//            }
//        });


        mBtnClear = findViewById(R.id.btnClear);
        mBtnClear.setVisibility(View.GONE);
        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewDesc.setVisibility(View.VISIBLE);
                mTextViewResult.setText("");
                mTextViewResult.setVisibility(View.GONE);
                mBtnSet.setVisibility(View.VISIBLE);
                mBtnClear.setVisibility(View.GONE);
            }
        });

        mBtnSet.setEnabled(false);
        mIflytekHelper = new IflytekHelper(AIUIActivity.this);
        mIflytekHelper.InitTextUnderstander(new IflytekHelper.InitListener() {
            @Override
            public void onSuccess() {
                Log.i("msg","语义理解（文本）初始化成功");
                mBtnSet.setEnabled(true);
            }

            @Override
            public void onFailed(int errCode) {
                String msg = "语义理解（文本）初始化失败（" + String.valueOf(errCode) + "）";
                Log.i("err",msg);
                CommonFun.showError(AIUIActivity.this,msg,true);
            }
        });
        mIflytekHelper.InitSpeechUnderstander(new IflytekHelper.InitListener() {
            @Override
            public void onSuccess() {
                Log.i("msg","语义理解（语音）初始化成功");
                mBtnSet.setEnabled(true);
            }

            @Override
            public void onFailed(int errCode) {
                String msg = "语义理解（语音）初始化失败（" + String.valueOf(errCode) + "）";
                Log.i("err",msg);
                CommonFun.showError(AIUIActivity.this,msg,true);
            }
        });
//        mIflytekHelper.InitAIUIAgent(new IflytekHelper.AIUIListener() {
//            @Override
//            public void onInitSuccess() {
//                Log.i("msg","语义理解初始化成功");
//                mBtnSet.setEnabled(true);
//            }
//
//            @Override
//            public void onInitFailed(int errCode, String errDesc) {
//                String msg = "语义理解初始化失败（" + String.valueOf(errCode) + "|" + errDesc + "）";
//                Log.i("err",msg);
//                CommonFun.showError(AIUIActivity.this,msg,true);
//            }
//
//            @Override
//            public void onStateChanged(IflytekHelper.AIUIServiceState state) {
//
//            }
//
//            @Override
//            public void onError(int errCode, String errDesc) {
//                String msg = "语义理解遇到错误（" + String.valueOf(errCode) + "|" + errDesc + "）";
//                Log.i("err",msg);
//                CommonFun.showError(AIUIActivity.this,msg,true);
//            }
//
//        });

        setLightWindow();
        showBackOption();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIflytekHelper.destroy();
    }


}
