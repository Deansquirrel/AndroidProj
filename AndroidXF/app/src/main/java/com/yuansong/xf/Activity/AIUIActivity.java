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
        StringBuffer strBuffer = new StringBuffer()
                .append("可用技能：")
                .append("\n")
                .append("  显示{Number}号桌菜品");
        mTextViewDesc.setText(strBuffer.toString());

        mTextViewResult = findViewById(R.id.textViewResult);
        mTextViewResult.setVisibility(View.GONE);
        mTextViewResult.setText("");

        mBtnSet = findViewById(R.id.btnSet);
        mBtnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewDesc.setVisibility(View.GONE);
                mTextViewResult.setText("TEST");
                mTextViewResult.setVisibility(View.VISIBLE);
                mBtnSet.setVisibility(View.GONE);
                mBtnClear.setVisibility(View.VISIBLE);
            }
        });


        mBtnClear = findViewById(R.id.btnClear);
        mBtnClear.setVisibility(View.GONE);
        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewDesc.setVisibility(View.VISIBLE);
                mTextViewResult.setText("TEST");
                mTextViewResult.setVisibility(View.GONE);
                mBtnSet.setVisibility(View.VISIBLE);
                mBtnClear.setVisibility(View.GONE);
            }
        });

        mBtnSet.setEnabled(false);
        mIflytekHelper = new IflytekHelper(AIUIActivity.this);
        mIflytekHelper.InitAIUIAgent(new IflytekHelper.InitListener() {
            @Override
            public void onSuccess() {
                Log.i("msg","语义理解初始化成功");
                mBtnSet.setEnabled(true);
            }

            @Override
            public void onFailed(int errCode) {
                String msg = "语义理解初始化失败（" + String.valueOf(errCode) + "）";
                CommonFun.showMsg(AIUIActivity.this,msg);
                Log.i("err",msg);
                AIUIActivity.this.finish();
            }
        });

        setLightWindow();
        showBackOption();
    }
}
