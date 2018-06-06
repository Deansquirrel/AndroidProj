package com.yuansong.xf.XF;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;


public class SynthesizerHelper {

    private String APPID = "5b14de4c";
    private String VOICE_NAME = "xiaoyan";

    private AppCompatActivity mActivity = null;
    private SpeechUtility mSpeechUtility = null;
    private SpeechSynthesizer mSpeechSynthesizer = null;
    
    public interface InitListener{
        void onSuccess();
        void onFailed(int errCode);
    }

    public SynthesizerHelper(AppCompatActivity activity, final InitListener listener){
        mActivity = activity;

        mSpeechUtility = SpeechUtility.createUtility(activity.getApplicationContext(),"appid=" + APPID);

        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mActivity.getApplicationContext(),
                new com.iflytek.cloud.InitListener() {
                    @Override
                    public void onInit(int errCode) {
                        if(errCode == ErrorCode.SUCCESS){
                            listener.onSuccess();
                            setParams();
                        }
                        else{
                            SynthesizerHelper.this.destroy();
                            listener.onFailed(errCode);
                            mSpeechSynthesizer = null;
                        }
                    }
                });
    }

    public void destroy(){
        if(mSpeechSynthesizer != null){
            mSpeechSynthesizer.destroy();
        }
        if(mSpeechUtility != null){
            mSpeechUtility.destroy();
        }
    }

    public void setParams(){
        if(mSpeechSynthesizer == null){
            return;
        }
        mSpeechSynthesizer.setParameter(SpeechConstant.PARAMS,null);

        mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME,this.VOICE_NAME);
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED,"50");
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME,"50");
        mSpeechSynthesizer.setParameter(SpeechConstant.PITCH,  "50");

        mSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE,SpeechConstant.TYPE_CLOUD);

        mSpeechSynthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
    }

    public void speak(String msg){
        Log.i("SynthesizerHelper.speak",msg);
        mSpeechSynthesizer.startSpeaking(msg,null);
    }
}
