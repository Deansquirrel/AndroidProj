package com.yuansong.features.Activity;

import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yuansong.features.BaseActivity;
import com.yuansong.features.Common.CommonFun;
import com.yuansong.features.R;

public class GDMapActivity extends BaseActivity {

    private Toolbar mToolbar = null;
    private Button mBtnGPSLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdmap);

        mToolbar = findViewById(R.id.toolbar);
        mBtnGPSLocation = findViewById(R.id.btnGPSLocation);

        mToolbar.setTitle("GD Map");
        setSupportActionBar(mToolbar);

        mBtnGPSLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFun.showMsg(GDMapActivity.this,"获取GPS定位");
            }
        });
    }

}
