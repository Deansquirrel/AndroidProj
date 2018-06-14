package com.yuansong.xf.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.yuansong.xf.BaseActivity;
import com.yuansong.xf.Common.CommonFun;
import com.yuansong.xf.R;

public class UnderstanderActivity extends BaseActivity {

    private Toolbar mToolbar = null;

    private Button mBtnText = null;
    private Button mBtnVoice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_understander);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("语义理解");
        setSupportActionBar(mToolbar);

        mBtnText = findViewById(R.id.btnText);
        mBtnText.setText("文本");
        mBtnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFun.showActivity(UnderstanderActivity.this,UnderstanderTextActivity.class,false);
            }
        });

        mBtnVoice = findViewById(R.id.btnVoice);
        mBtnVoice.setText("语音");
        mBtnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFun.showActivity(UnderstanderActivity.this,UnderstanderVoiceActivity.class,false);
            }
        });

        setLightWindow();
        showBackOption();
    }
}
