package com.yuansong.xf.XF;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;

public class IflytekHelper {

    private static final String APPID = "5b14de4c";
    private static final String VOICE_NAME = "xiaoyan";

    private AppCompatActivity mActivity;
    private SpeechUtility mSpeechUtility;
    private SpeechSynthesizer mSpeechSynthesizer = null;
    private SpeechRecognizer mSpeechRecognizer = null;
    private String recResult = null;
    private AIUIAgent mAIUIAgent = null;

    public interface InitListener{
        void onSuccess();
        void onFailed(int errCode);
    }

    public interface SpeakListener{
        void preSpeak();
        void postSpeak();
        void onCompleted();
        void onFailed(int errCode, String errDesc);
    }

    public interface RecognizerListener{
        void preRecognize();
        void postRecognize();
        void onCompleted(String result);
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


    public void InitAIUIAgent(){
        mAIUIAgent = AIUIAgent.createAgent(mActivity.getApplicationContext(), "",
                new AIUIListener() {
                    @Override
                    public void onEvent(AIUIEvent aiuiEvent) {

                    }
                });
    }

    public void destroy(){
        if(mSpeechSynthesizer != null){
            mSpeechSynthesizer.destroy();
        }
        if(mSpeechRecognizer != null){
            mSpeechRecognizer.destroy();
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

        mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
        mSpeechRecognizer.setParameter(SpeechConstant.ACCENT,"mandarin");
        mSpeechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE,SpeechConstant.TYPE_CLOUD);
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_BOS,"4000");
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_EOS,"1000");
        mSpeechRecognizer.setParameter(SpeechConstant.ASR_PTT,"0");
        mSpeechRecognizer.setParameter(SpeechConstant.AUDIO_SOURCE, String.valueOf(MediaRecorder.AudioSource.MIC));
        mSpeechRecognizer.setParameter(SpeechConstant.RESULT_TYPE,"plain");
        mSpeechRecognizer.setParameter(SpeechConstant.DOMAIN, "iat");
    }

    public void startListening(final IflytekHelper.RecognizerListener listener){
        recResult = "";
        if(listener != null){
            listener.preRecognize();
        }
        mSpeechRecognizer.startListening(new com.iflytek.cloud.RecognizerListener() {
            @Override
            public void onVolumeChanged(int i, byte[] bytes) {

            }

            @Override
            public void onBeginOfSpeech() {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                Log.i("msg",recognizerResult.getResultString());
                if(recognizerResult != null){
                    recResult = recResult + recognizerResult.getResultString();
                }
                if(listener != null && b == true){
                    listener.onCompleted(recResult);
                    listener.postRecognize();
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                if(listener != null){
                    listener.onFailed(speechError.getErrorCode(),speechError.getErrorDescription());
                    listener.postRecognize();
                }
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }

    public void stopListening(){
        mSpeechRecognizer.stopListening();
    }
}
