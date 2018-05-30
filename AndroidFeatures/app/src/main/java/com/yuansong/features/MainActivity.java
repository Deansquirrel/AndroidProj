package com.yuansong.features;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.yuansong.features.Activity.GDMapActivity;
import com.yuansong.features.Activity.OkHttpActivity;
import com.yuansong.features.Activity.SMSSenderActivity;
import com.yuansong.features.Common.CommonFun;

public class MainActivity extends BaseActivity {

    private Toolbar mToolbar = null;
    private Button mBtnSMSSender = null;
    private Button mBtnGDMap = null;
    private Button mBtnOkHttp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mBtnSMSSender = findViewById(R.id.btnSMSSender);
        mBtnGDMap = findViewById(R.id.btnGDMap);
        mBtnOkHttp = findViewById(R.id.btnOkHttp);

        mToolbar.setTitle("Feature List");
        setSupportActionBar(mToolbar);

        mBtnSMSSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFun.showActivity(MainActivity.this, SMSSenderActivity.class,false);
            }
        });
        mBtnGDMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFun.showActivity(MainActivity.this, GDMapActivity.class,false);
            }
        });
        mBtnOkHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFun.showActivity(MainActivity.this, OkHttpActivity.class,false);
            }
        });

    }


}
