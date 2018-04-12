package com.yuansong.features.Common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by yuansong on 2018/3/9.
 */

public class CommonFun {

    private CommonFun(){}

    public static String createNewGuid(){
        return java.util.UUID.randomUUID().toString().toUpperCase();
    }

    public static void showActivity(AppCompatActivity context, Class<?> cls, boolean isQuit){
        Log.i("CommonFun","showActivity|" + context.getClass().toString() + "|" + cls.toString() + "|" + String.valueOf(isQuit));
        final Intent it = new Intent(context,cls);
        if(isQuit){
            context.finish();
        }
        context.startActivity(it);
    }

    public static void setFocus(AppCompatActivity activity, EditText et){
        //设置焦点
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        //弹出键盘
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public static void showMsg(AppCompatActivity activity, String msg){
//        Toast.makeText(activity.getApplicationContext(), msg,Toast.LENGTH_SHORT).show();
        if("main".equals(Thread.currentThread().getName())){
            Toast.makeText(activity.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }else{
            final AppCompatActivity mAct = activity;
            final String mMsg = msg;
            // 子线程
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mAct.getApplicationContext(), mMsg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void showError(AppCompatActivity activity,String msg,boolean isQuit){
        CommonFun.showMsg(activity,msg);
        if(isQuit){
            activity.finish();
        }
    }
}
