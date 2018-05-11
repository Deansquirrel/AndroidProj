package com.yuansong.features.Features;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class SMSHelper{

    /**
     * 发送短信，未跟踪发送至短信中心及对方接收结果
     * @param phoneNumber
     * @param message
     */
    public void sendMessage(String phoneNumber,String message){
        Log.i("phoneNumber",phoneNumber);
        Log.i("message",message);

        SmsManager smsManager = SmsManager.getDefault();

        List<String> sms = smsManager.divideMessage(message);
        for (String msg :sms){
            smsManager.sendTextMessage(phoneNumber,null,msg,null,null);
        }
    }

    /**
     * 转至短信发送界面
     * @param activity
     * @param phoneNumber
     * @param message
     */
    public void sendMessagePre(Activity activity, String phoneNumber, String message){
        Log.i("phoneNumber",phoneNumber);
        Log.i("message",message);
//        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//        smsIntent.setData(Uri.parse("smsto:"));
//        smsIntent.setType("vnd.android-dir/mms-sms");
//
//        smsIntent.putExtra("address"  , phoneNumber);
//        smsIntent.putExtra("sms_body"  , message);
//        try {
//            activity.startActivity(smsIntent);
//            Log.i("Finished sending SMS...", "");
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(activity,"SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
//        }
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("smsto:" + phoneNumber));
        sendIntent.putExtra("sms_body", message);
        activity.startActivity(sendIntent);
    }

}
