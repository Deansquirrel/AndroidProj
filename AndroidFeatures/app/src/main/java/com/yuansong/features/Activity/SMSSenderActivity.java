package com.yuansong.features.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.yuansong.features.BaseActivity;
import com.yuansong.features.Common.CommonFun;
import com.yuansong.features.Features.SMSHelper;
import com.yuansong.features.R;

public class SMSSenderActivity extends BaseActivity {

    private Toolbar mToolbar = null;
    private EditText mEditTextPhoneNumber = null;
    private EditText mEditTextMessage = null;
    private CheckBox mCheckBox = null;
    private Button mBtnSend = null;

    private SMSHelper mSMSHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smssender);

        mSMSHelper = new SMSHelper();

        mToolbar = findViewById(R.id.toolbar);
        mEditTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        mEditTextMessage = findViewById(R.id.editTextMessage);
        mCheckBox = findViewById(R.id.checkBox);
        mBtnSend = findViewById(R.id.btnSend);

        mToolbar.setTitle("SMS Sender");
        setSupportActionBar(mToolbar);

        mCheckBox.setText("直接发送");
        mCheckBox.setChecked(true);

        mBtnSend.setText("Send");
        mBtnSend.setOnClickListener(new BtnClickSend());

        ActionBar ab = this.getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        CommonFun.setFocus(SMSSenderActivity.this,mEditTextPhoneNumber);
    }

    private class BtnClickSend implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            mBtnSend.setEnabled(false);

            String phoneNumber = mEditTextPhoneNumber.getText().toString().trim();
            String message = mEditTextMessage.getText().toString().trim();
            if(phoneNumber.equals("") || message.equals("")){
                CommonFun.showMsg(SMSSenderActivity.this,"电话或发送内容不能为空");
                if(phoneNumber.equals("")){
                    CommonFun.setFocus(SMSSenderActivity.this,mEditTextPhoneNumber);
                }
                else{
                    CommonFun.setFocus(SMSSenderActivity.this,mEditTextMessage);
                }
            }
            else{
                if(mCheckBox.isChecked()){
                    mSMSHelper.sendMessage(phoneNumber,message);
                }
                else{
                    mSMSHelper.sendMessagePre(SMSSenderActivity.this,phoneNumber,message);
                }
                mEditTextPhoneNumber.setText("");
                mEditTextMessage.setText("");
            }

            mBtnSend.setEnabled(true);
        }
    }
}
