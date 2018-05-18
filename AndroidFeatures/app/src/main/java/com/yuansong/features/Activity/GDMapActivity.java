package com.yuansong.features.Activity;

import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import com.yuansong.features.BaseActivity;
import com.yuansong.features.Common.CommonFun;
import com.yuansong.features.R;

public class GDMapActivity extends BaseActivity {

    private Toolbar mToolbar = null;
    private Button mBtnGPSLocation = null;
    private Button mBtnShowMap = null;
    private Button mBtnMapNavigation = null;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdmap);

        mToolbar = findViewById(R.id.toolbar);
        mBtnGPSLocation = findViewById(R.id.btnGPSLocation);
        mBtnShowMap = findViewById(R.id.btnShowMap);
        mBtnMapNavigation = findViewById(R.id.btnMapNavigation);

        mToolbar.setTitle("GD Map");
        setSupportActionBar(mToolbar);

        mBtnGPSLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFun.showMsg(GDMapActivity.this,"开始获取GPS定位");
                subGetLocation();
            }
        });
        mBtnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFun.showActivity(GDMapActivity.this,GDMapShowMapActivity.class,false);
            }
        });
        mBtnMapNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFun.showMsg(GDMapActivity.this,"开发中");
            }
        });

        //------------------------------------------------------------------------------------------
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setNeedAddress(false);
        mLocationOption.setHttpTimeOut(10000);
        mLocationOption.setLocationCacheEnable(false);
        //------------------------------------------------------------------------------------------

        showBackOption();
        setLightWindow();
    }

    private void subGetLocation(){
        mBtnGPSLocation.setEnabled(false);
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        Log.i("Location changed",String.valueOf(aMapLocation.getLongitude()) + " - " + String.valueOf(aMapLocation.getLatitude()));
                        CommonFun.showMsg(GDMapActivity.this,String.valueOf(aMapLocation.getLongitude()) + " - " + String.valueOf(aMapLocation.getLatitude()));
                    }else {
                        CommonFun.showError(GDMapActivity.this,"定位失败（" + String.valueOf(aMapLocation.getErrorCode()) + "|" + aMapLocation.getErrorInfo() + "）",false);
                    }
                }
                else{
                    CommonFun.showError(GDMapActivity.this,"定位失败（未知错误）",false);
                }
                mLocationClient.stopLocation();
                mLocationClient.onDestroy();
                mBtnGPSLocation.setEnabled(true);
            }
        });
        mLocationClient.startLocation();
    }

}
