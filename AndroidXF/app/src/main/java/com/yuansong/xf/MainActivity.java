package com.yuansong.xf;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.yuansong.xf.Activity.AIUIActivity;
import com.yuansong.xf.Activity.RecognizerActivity;
import com.yuansong.xf.Activity.SynthesizerActivity;
import com.yuansong.xf.Activity.UnderstanderActivity;
import com.yuansong.xf.Common.CommonFun;

public class MainActivity extends BaseActivity {

    private Toolbar mToolbar = null;
    private Button mBtnSynthesizer = null;
    private Button mBtnRecognizer = null;
    private Button mBtnAIUI = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("讯飞Test");
        setSupportActionBar(mToolbar);

        mBtnSynthesizer = findViewById(R.id.btnSynthesizer);
        mBtnSynthesizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CommonFun.showMsg(MainActivity.this,"在线语音合成");
                CommonFun.showActivity(MainActivity.this, SynthesizerActivity.class,false);
            }
        }) ;
        mBtnRecognizer = findViewById(R.id.btnRecognizer);
        mBtnRecognizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CommonFun.showMsg(MainActivity.this,"语音听写");
                CommonFun.showActivity(MainActivity.this, RecognizerActivity.class,false);
            }
        });
        mBtnAIUI = findViewById(R.id.btnAIUI);
        mBtnAIUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CommonFun.showMsg(MainActivity.this,"语义理解");
                CommonFun.showActivity(MainActivity.this, UnderstanderActivity.class,false);
            }
        });

    }


}
