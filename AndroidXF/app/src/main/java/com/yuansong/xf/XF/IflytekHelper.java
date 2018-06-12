package com.yuansong.xf.XF;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IflytekHelper {

    private static final String APPID = "5b14de4c";
    private static final String VOICE_NAME = "xiaoyan";

    private Activity mActivity;
    private SpeechUtility mSpeechUtility;
    private SpeechSynthesizer mSpeechSynthesizer = null;
    private SpeechRecognizer mSpeechRecognizer = null;
    private String recResult = null;
    private AIUIAgent mAIUIAgent = null;

    /**
     * 初始化回调接口
     */
    public interface InitListener{
        void onSuccess();
        void onFailed(int errCode);
    }

    /**
     * 在线语音合成回调接口
     */
    public interface SpeakListener{
        void preSpeak();
        void postSpeak();
        void onCompleted();
        void onFailed(int errCode, String errDesc);
    }

    /**
     * 语音识别回调接口
     */
    public interface RecognizerListener{
        void preRecognize();
        void postRecognize();
        void onCompleted(String result);
        void onFailed(int errCode, String errDesc);
    }

    /**
     * 构造函数
     * @param activity 调用的Activity
     */
    public IflytekHelper(Activity activity){
        mActivity = activity;
        mSpeechUtility = SpeechUtility.createUtility(activity.getApplicationContext(),"appid=" + APPID);
    }

    /**
     * 在线语音合成 初始化
     * @param listener
     */
    public void InitSynthesizer(final IflytekHelper.InitListener listener){
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mActivity.getApplicationContext(),
                new com.iflytek.cloud.InitListener() {
                    @Override
                    public void onInit(int errCode) {
                        if(errCode == ErrorCode.SUCCESS){
                            if(listener != null){
                                listener.onSuccess();
                            }
                            setSynthesizerParams();
                        }
                        else{
                            IflytekHelper.this.destroy();
                            if(listener != null){
                                listener.onFailed(errCode);
                            }
                            mSpeechSynthesizer = null;
                        }
                    }
                });
    }

    /**
     * 语音识别 初始化
     * @param listener
     */
    public void InitRecognizer(final IflytekHelper.InitListener listener){
        mSpeechRecognizer = SpeechRecognizer.createRecognizer(mActivity.getApplicationContext(),
                new com.iflytek.cloud.InitListener() {
                    @Override
                    public void onInit(int errCode) {
                        if(errCode == ErrorCode.SUCCESS){
                            if(listener != null){
                                listener.onSuccess();
                            }
                            setRecognizerParams();
                        }
                        else{
                            IflytekHelper.this.destroy();
                            if(listener != null){
                                listener.onFailed(errCode);
                            }
                            mSpeechRecognizer = null;
                        }
                    }
                });
    }

    /**
     * 语义识别 初始化
     */
    public void InitAIUIAgent(final IflytekHelper.InitListener listener){
        mAIUIAgent = AIUIAgent.createAgent(mActivity.getApplicationContext(),
                getAIUIparams(),
                new AIUIListener() {
                    @Override
                    public void onEvent(AIUIEvent aiuiEvent) {
                        Log.i("msg","----------------------------------------------");
                        Log.i("aiuiEvent",String.valueOf(aiuiEvent.eventType));
                        Log.i("msg","----------------------------------------------");
                        switch (aiuiEvent.eventType){
                            case AIUIConstant.EVENT_RESULT:
                                if(listener != null){
                                    listener.onSuccess();
                                }
                                break;
                            case AIUIConstant.EVENT_ERROR:
                                if(listener != null){
                                    listener.onFailed(aiuiEvent.arg1);
                                }
                                break;
                        }
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
        if(mAIUIAgent != null){
            mAIUIAgent.destroy();
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

    private String getAIUIparams(){
        Map<String,String> params = new HashMap();
//        params.put("scene","main");
//        params.put("interact_timeout","60000");
//        params.put("result_timeout","5000");
//        params.put("engine_type","meta");
//        params.put("res_type","assets");
//        params.put("res_path","");
//        params.put("sample_rate","16000");
//        params.put("data_source","sdk");
//        params.put("interact_mode",mActivity.getApplicationContext().getPackageResourcePath() +
//                "meta_vad_16k.jet");
//        params.put("","");
//        params.put("","");

        StringBuilder strResult = new StringBuilder();
        strResult.append("");
        for (String key:params.keySet()) {
            strResult.append(key + "=" + params.get(key) + ";");
        }
        return strResult.toString();
    }

    private void startAIUISerivce(){
        AIUIMessage startMsg = new AIUIMessage(AIUIConstant.CMD_START,
                0,
                0,
                null,
                null);
        mAIUIAgent.sendMessage(startMsg);
    }

    private void stopAIUISerivce(){
        AIUIMessage stopMsg = new AIUIMessage(AIUIConstant.CMD_STOP,
                0,
                0,
                null,
                null);
        mAIUIAgent.sendMessage(stopMsg);
    }
}
