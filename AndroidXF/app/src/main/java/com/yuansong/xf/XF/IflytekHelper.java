package com.yuansong.xf.XF;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

public class IflytekHelper {

    private static final String APPID = "5b14de4c";
    private static final String VOICE_NAME = "xiaoyan";

    private AppCompatActivity mActivity;
    private SpeechUtility mSpeechUtility;
    private SpeechSynthesizer mSpeechSynthesizer;
    private SpeechRecognizer mSpeechRecognizer;

    public interface InitListener{
        void onSuccess();
        void onFailed(int errCode);
    }

    public interface SpeakListener{
        void preSpeak();
        void onCompleted();
        void postSpeak();
        void onFailed(int errCode, String errDesc);
    }

    public IflytekHelper(AppCompatActivity activity){
        mActivity = activity;
        mSpeechUtility = SpeechUtility.createUtility(activity.getApplicationContext(),"appid=" + APPID);
    }

    public void InitSynthesizer(final IflytekHelper.InitListener listener){
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mActivity.getApplicationContext(),
                new com.iflytek.cloud.InitListener() {
                    @Override
                    public void onInit(int errCode) {
                        if(errCode == ErrorCode.SUCCESS){
                            listener.onSuccess();
                            setSynthesizerParams();
                        }
                        else{
                            IflytekHelper.this.destroy();
                            listener.onFailed(errCode);
                            mSpeechSynthesizer = null;
                        }
                    }
                });
    }

    public void InitRecognizer(final IflytekHelper.InitListener listener){
        mSpeechRecognizer = SpeechRecognizer.createRecognizer(mActivity.getApplicationContext(),
                new com.iflytek.cloud.InitListener() {
                    @Override
                    public void onInit(int errCode) {
                        if(errCode == ErrorCode.SUCCESS){
                            listener.onSuccess();
                            setRecognizerParams();
                        }
                        else{
                            IflytekHelper.this.destroy();
                            listener.onFailed(errCode);
                            mSpeechRecognizer = null;
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

    public void speak(String msg, final SpeakListener listener){
        Log.i("SynthesizerHelper.speak",msg);
        if(listener != null){
            listener.preSpeak();
        }
        mSpeechSynthesizer.startSpeaking(msg, new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {
                Log.i("msg","");
            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {
                Log.i("msg","");
            }

            @Override
            public void onSpeakPaused() {
                Log.i("msg","");
            }

            @Override
            public void onSpeakResumed() {
                Log.i("msg","");
            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {
                Log.i("msg","");
            }

            @Override
            public void onCompleted(SpeechError speechError) {
                Log.i("msg","");
                if(listener != null){
                    if(speechError == null){
                        listener.onCompleted();
                    }
                    else{
                        listener.onFailed(speechError.getErrorCode(),speechError.getErrorDescription());
                    }
                    listener.postSpeak();
                }
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {
                Log.i("msg","");
            }
        });
    }

    private void setSynthesizerParams(){
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

    private void setRecognizerParams(){
        if(mSpeechRecognizer == null){
            return;
        }
        mSpeechRecognizer.setParameter(SpeechConstant.PARAMS,null);
    }
}
