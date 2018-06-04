package com.yuansong.xf.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.yuansong.xf.BaseActivity;
import com.yuansong.xf.Common.CommonFun;
import com.yuansong.xf.R;

public class OkHttpActivity extends BaseActivity {

    private Toolbar mToolbar = null;
    private Button mBtnGet = null;
    private Button mBtnPost = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("OkHttp");
        setSupportActionBar(mToolbar);

        mBtnGet = findViewById(R.id.btnGet);
        mBtnPost = findViewById(R.id.btnPost);

        mBtnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subGet("http://123.57.70.114");
            }
        });
        mBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subPost("https://oapi.dingtalk.com/robot/send?access_token=7a84d09b83f9633ad37866505d2c0c26e39f4fa916b3af8f6a702180d3b9906b",
                        "{\n" +
                                "     \"msgtype\": \"text\",\n" +
                                "     \"text\": {\n" +
                                "         \"content\": \"我就是我\"\n" +
                                "     }\n" +
                                " }");
            }
        });

        showBackOption();
        setLightWindow();
    }

    private void subGet(String url) {
        com.yuansong.features.Net.HttpHandler httpHandler = new com.yuansong.features.Net.HttpHandler();
        httpHandler.getHttpData_Get(url, new com.yuansong.features.Net.HttpHandler.ICallBack() {
            @Override
            public void onGetData(String data) {
                CommonFun.showMsg(OkHttpActivity.this,data);
            }

            @Override
            public void onGetError(Exception ex) {
                CommonFun.showError(OkHttpActivity.this,ex.getMessage(),false);
                ex.printStackTrace();
            }

            @Override
            public void onPreExecute() {
                mBtnGet.setEnabled(false);
            }

            @Override
            public void onPostExecute() {
                mBtnGet.setEnabled(true);
            }
        });
    }

    private void subPost(String url,String data){
        com.yuansong.features.Net.HttpHandler httpHandler = new com.yuansong.features.Net.HttpHandler();
        httpHandler.getHttpData_Post(url, data, new com.yuansong.features.Net.HttpHandler.ICallBack() {
            @Override
            public void onGetData(String data) {
                CommonFun.showMsg(OkHttpActivity.this,data);
            }

            @Override
            public void onGetError(Exception ex) {
                CommonFun.showError(OkHttpActivity.this,ex.getMessage(),false);
                ex.printStackTrace();
            }

            @Override
            public void onPreExecute() {
                mBtnPost.setEnabled(false);
            }

            @Override
            public void onPostExecute() {
                mBtnPost.setEnabled(true);
            }
        });
    }
}