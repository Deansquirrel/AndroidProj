package com.yuansong.xf;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;

/**
 * Created by yuansong on 2018/3/5.
 */

public abstract class BaseActivity extends AppCompatActivity {

//    private CacheDBHelper mCacheDBHelper = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BaseActivity","onCreate");
//        mCacheDBHelper = CacheDBHelper.getInstance(this);

        //竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //标题栏置白
        setLightWindow();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("BaseActivity","onPause");
//        mCacheDBHelper.saveCacheUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("BaseActivity","onResume");
//        mCacheDBHelper.refreshCacheUser();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            clickActionBarBackBtn();
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            clickActionBarBackBtn();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    protected void showBackOption(){
        ActionBar ab = this.getSupportActionBar();
        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void clickActionBarBackBtn(){
        if(BaseActivity.this.isTaskRoot()){
            moveTaskToBack(false);
        }
        else{
            finish();
        }
    }

    protected void setDarkWindow(){
        //标题栏颜色
        Window window = this.getWindow();
        if(window != null){
            window.setStatusBarColor(this.getResources().getColor(R.color.themeBlueColor));
        }
    }

    protected void setLightWindow(){
        //标题栏置白
        Window window = this.getWindow();
        if(window != null){
            window.setStatusBarColor(this.getResources().getColor(R.color.whiteColor));
        }
    }
}
