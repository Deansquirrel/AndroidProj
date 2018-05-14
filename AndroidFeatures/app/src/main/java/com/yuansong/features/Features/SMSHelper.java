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

        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("smsto:" + phoneNumber));
        sendIntent.putExtra("sms_body", message);
        activity.startActivity(sendIntent);
    }

}
