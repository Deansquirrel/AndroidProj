package com.yuansong.xf.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.yuansong.xf.BaseActivity;
import com.yuansong.xf.R;

public class SynthesizerActivity extends BaseActivity {

    private Toolbar mToolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synthesizer);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("在线语音合成");
        setSupportActionBar(mToolbar);

        setLightWindow();
        showBackOption();
    }
}
