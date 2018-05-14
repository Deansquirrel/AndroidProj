package com.yuansong.features;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.yuansong.features.Activity.SMSSenderActivity;
import com.yuansong.features.Common.CommonFun;

public class MainActivity extends BaseActivity {

    private Toolbar mToolbar = null;
    private Button mBtnSMSSender = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mBtnSMSSender = findViewById(R.id.btnSMSSender);

        mBtnSMSSender.setOnClickListener(new BtnClickSMSSender());
    }

    private class BtnClickSMSSender implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            CommonFun.showActivity(MainActivity.this, SMSSenderActivity.class,false);
        }
    }

}
