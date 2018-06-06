package com.yuansong.xf.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.yuansong.xf.BaseActivity;
import com.yuansong.xf.Common.CommonFun;
import com.yuansong.xf.R;
import com.yuansong.xf.XF.SynthesizerHelper;

public class SynthesizerActivity extends BaseActivity {

    private Toolbar mToolbar = null;
    private EditText mEditText = null;
    private Button mButton = null;

    private SynthesizerHelper mSynthesizerHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synthesizer);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("在线语音合成");
        setSupportActionBar(mToolbar);

        mEditText = findViewById(R.id.editText);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().equals("")){
                    mButton.setEnabled(false);
                }
                else{
                    mButton.setEnabled(true);
                }
            }
        });

        mButton = findViewById(R.id.btnButton);
        mButton.setText("合  成");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mEditText.getText().toString().trim();
                mSynthesizerHelper.speak(msg);
            }
        });

        subInit();

        setLightWindow();
        showBackOption();

        mEditText.setEnabled(false);
        mSynthesizerHelper = new SynthesizerHelper(SynthesizerActivity.this, new SynthesizerHelper.InitListener() {
            @Override
            public void onSuccess() {
                Log.i("msg","在线语音合成初始化成功");
                mEditText.setEnabled(true);
            }

            @Override
            public void onFailed(int errCode) {
                String msg = "在线语音合成初始化失败（" + String.valueOf(errCode) + "）";
                CommonFun.showMsg(SynthesizerActivity.this,msg);
                Log.i("err",msg);
                SynthesizerActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSynthesizerHelper != null){
            mSynthesizerHelper.destroy();
        }
    }

    private void subInit(){
        mEditText.setText("");
        mButton.setEnabled(false);
    }
}
