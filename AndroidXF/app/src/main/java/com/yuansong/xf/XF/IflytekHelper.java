package com.yuansong.xf.XF;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIMessage;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IflytekHelper {

    private static final String APPID = "5b14de4c";
    private static final String VOICE_NAME = "xiaoyan";

    private Activity mActivity;
    private SpeechUtility mSpeechUtility;
    private SpeechSynthesizer mSpeechSynthesizer = null;
    private SpeechRecognizer mSpeechRecognizer = null;
    private String recResult = null;

    private TextUnderstander mTextUnderstander = null;
    private SpeechUnderstander mSpeechUnderstander = null;


//    private AIUIAgent mAIUIAgent = null;


//    private AIUIEventType mAIUIEventType = AIUIEventType.None;
//    private enum AIUIEventType{
//        None,
//        InitCheck,
//        Understand
//    }
//    private AIUIServiceState mAIUIServiceState = AIUIServiceState.STATE_NONE;
//    public enum AIUIServiceState{
//        STATE_NONE,
//        STATE_IDLE,
//        STATE_READY,
//        STATE_WORKING
//    }

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

    public interface TextUnderstanderListener{
        void preUnderstand();
        void postUnderstand();
        void onCompleted(int rc, String service, String intent, Map<String,String>data);
        void onFailed(int errCode, String errDesc);
    }

//    /**
//     * 语义理解回调接口
//     */
//    public interface AIUIListener{
//        void onInitSuccess();
//        void onInitFailed(int errCode, String errDesc);
//        void onStateChanged(AIUIServiceState state);
//        void onError(int errCode, String errDesc);
//    }

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
                            setSynthesizerParams();
                            if(listener != null){
                                listener.onSuccess();
                            }
                        }
                        else{
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
                            setRecognizerParams();
                            if(listener != null){
                                listener.onSuccess();
                            }
                        }
                        else{
                            if(listener != null){
                                listener.onFailed(errCode);
                            }
                            mSpeechRecognizer = null;
                        }
                    }
                });
    }

    /**
     * 语义理解（文本）
     * @param listener
     */
    public void InitTextUnderstander(final IflytekHelper.InitListener listener){
        mTextUnderstander = TextUnderstander.createTextUnderstander(mActivity.getApplicationContext(),
                new com.iflytek.cloud.InitListener() {
                    @Override
                    public void onInit(int errCode) {
                        if(errCode == ErrorCode.SUCCESS){
                            setTextUnderstanderParams();
                            if(listener != null){
                                listener.onSuccess();
                            }
                        }
                        else{
                            if(listener != null){
                                listener.onFailed(errCode);
                            }
                            mTextUnderstander = null;
                        }
                    }
                });
    }

    /**
     * 语义理解（语音）
     * @param listener
     */
    public void InitSpeechUnderstander(final IflytekHelper.InitListener listener){
        mSpeechUnderstander = SpeechUnderstander.createUnderstander(mActivity.getApplicationContext(),
                new com.iflytek.cloud.InitListener() {
                    @Override
                    public void onInit(int errCode) {
                        if(errCode == ErrorCode.SUCCESS){
                            setSpeechUnderstanderParams();
                            if(listener != null){
                                listener.onSuccess();
                            }
                        }
                        else{
                            if(listener != null){
                                listener.onFailed(errCode);
                            }
                            mSpeechUnderstander = null;
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
//        if(mAIUIAgent != null){
//            mAIUIAgent.destroy();
//        }
        if(mTextUnderstander != null){
            mTextUnderstander.destroy();
        }
        if(mSpeechUnderstander != null){
            mSpeechUnderstander.destroy();
        }
        if(mSpeechUtility != null){
            mSpeechUtility.destroy();
        }
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
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_EOS,"10000");
        mSpeechRecognizer.setParameter(SpeechConstant.ASR_PTT,"0");
        mSpeechRecognizer.setParameter(SpeechConstant.AUDIO_SOURCE, String.valueOf(MediaRecorder.AudioSource.MIC));
        mSpeechRecognizer.setParameter(SpeechConstant.RESULT_TYPE,"plain");
        mSpeechRecognizer.setParameter(SpeechConstant.DOMAIN, "iat");
    }

    private void setTextUnderstanderParams(){
        if(mTextUnderstander == null){
            return;
        }
        mTextUnderstander.setParameter(SpeechConstant.PARAMS,null);

        mTextUnderstander.setParameter(SpeechConstant.SCENE,"main");
        mTextUnderstander.setParameter(SpeechConstant.NET_TIMEOUT,"20000");
        mTextUnderstander.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
        mTextUnderstander.setParameter(SpeechConstant.ACCENT,"mandarin");
        mTextUnderstander.setParameter(SpeechConstant.DOMAIN, "iat");
        mTextUnderstander.setParameter(SpeechConstant.RESULT_TYPE,"json");
        mTextUnderstander.setParameter(SpeechConstant.ENGINE_TYPE,"cloud");
    }

    private void setSpeechUnderstanderParams(){
        if(mSpeechUnderstander == null){
            return;
        }
        mSpeechUnderstander.setParameter(SpeechConstant.PARAMS,null);

        mSpeechUnderstander.setParameter(SpeechConstant.SCENE,"main");
        mSpeechUnderstander.setParameter(SpeechConstant.NLP_VERSION,"3.0");
        mSpeechUnderstander.setParameter(SpeechConstant.NET_TIMEOUT,"20000");
        mSpeechUnderstander.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT,"60000");
        mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE,"zh_cn");
        mSpeechUnderstander.setParameter(SpeechConstant.ACCENT,"mandarin");
        mSpeechUnderstander.setParameter(SpeechConstant.DOMAIN, "iat");
        mSpeechUnderstander.setParameter(SpeechConstant.AUDIO_SOURCE, String.valueOf(MediaRecorder.AudioSource.MIC));
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_BOS,"4000");
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_EOS,"10000");
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_ENABLE,"1");
        mSpeechUnderstander.setParameter(SpeechConstant.SAMPLE_RATE,"16000");
        mSpeechUnderstander.setParameter(SpeechConstant.RESULT_TYPE,"json");
        mSpeechUnderstander.setParameter(SpeechConstant.ENGINE_TYPE,"cloud");
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

    public void understandText(String msg, final TextUnderstanderListener listener){
        if(listener != null){
            listener.preUnderstand();
        }
        mTextUnderstander.understandText(msg, new com.iflytek.cloud.TextUnderstanderListener() {
            @Override
            public void onResult(UnderstanderResult understanderResult) {
                Log.i("result",understanderResult.getResultString());
                if(listener != null){
//                    listener.onCompleted(understanderResult.getResultString());
                    try {
                        JSONObject json = new JSONObject(understanderResult.getResultString());
                        int rc = json.getInt("rc");
                        switch (rc){
                            case 0:
                                String service = json.getString("service");
                                JSONArray semantic = json.getJSONArray("semantic");
//                                Log.i("json data",ja.get(0).toString());
                                JSONObject semanticFirst = new JSONObject(semantic.get(0).toString());
//                                Log.i("slots",j.getString("slots"));
                                String intent = semanticFirst.getString("intent");
                                JSONArray slots = semanticFirst.getJSONArray("slots");
//                                Log.i("json data",slots.get(0).toString());
//                                JSONObject slot = new JSONObject(slots.get(0).toString());
//                                Log.i("name",slot.getString("name"));
//                                Log.i("value",slot.getString("value"));
                                Map<String,String> data = new HashMap<>();
                                for(int i=0;i<slots.length();i++){
                                    JSONObject slot = new JSONObject(slots.get(i).toString());
                                    data.put(slot.getString("name"),slot.getString("value"));
                                }
                                listener.onCompleted(rc,service,intent, data);
                                break;
                            case 1:
                                listener.onFailed(rc,"输入异常");
                                break;
                            case 2:
                                listener.onFailed(rc,"系统内部异常");
                                break;
                            case 3:
                                listener.onFailed(rc,json.get("error").toString());
                                break;
                            case 4:
                                listener.onFailed(rc,"文本没有匹配的技能场景，技能不理解或不能处理该文本");
                                break;
                            default:
                                listener.onFailed(rc,"rc类型未定义");
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFailed(-1,e.getMessage());
                    }

                    listener.postUnderstand();
                }
            }
            @Override
            public void onError(SpeechError speechError) {
                if(listener != null){
                    listener.onFailed(speechError.getErrorCode(),speechError.getErrorDescription());
                    listener.postUnderstand();
                }
            }
        });
    }

    public void understandVoice(final TextUnderstanderListener listener){
        if(listener != null){
            listener.preUnderstand();
        }
        mSpeechUnderstander.startUnderstanding(new SpeechUnderstanderListener() {
            @Override
            public void onVolumeChanged(int i, byte[] bytes) {

            }

            @Override
            public void onBeginOfSpeech() {
                Log.i("begin","onBeginOfSpeech");
            }

            @Override
            public void onEndOfSpeech() {
                Log.i("end","onEndOfSpeech");
            }

            @Override
            public void onResult(UnderstanderResult understanderResult) {
                Log.i("result",understanderResult.getResultString());
                if(listener != null){
//                    listener.onCompleted(understanderResult.getResultString());
                    try {
                        JSONObject json = new JSONObject(understanderResult.getResultString());
                        int rc = json.getInt("rc");
                        switch (rc){
                            case 0:
                                String service = json.getString("service");
                                JSONArray semantic = json.getJSONArray("semantic");
//                                Log.i("json data",ja.get(0).toString());
                                JSONObject semanticFirst = new JSONObject(semantic.get(0).toString());
//                                Log.i("slots",j.getString("slots"));
                                String intent = semanticFirst.getString("intent");
                                JSONArray slots = semanticFirst.getJSONArray("slots");
//                                Log.i("json data",slots.get(0).toString());
//                                JSONObject slot = new JSONObject(slots.get(0).toString());
//                                Log.i("name",slot.getString("name"));
//                                Log.i("value",slot.getString("value"));
                                Map<String,String> data = new HashMap<>();
                                for(int i=0;i<slots.length();i++){
                                    JSONObject slot = new JSONObject(slots.get(i).toString());
                                    data.put(slot.getString("name"),slot.getString("value"));
                                }
                                listener.onCompleted(rc,service,intent, data);
                                break;
                            case 1:
                                listener.onFailed(rc,"输入异常");
                                break;
                            case 2:
                                listener.onFailed(rc,"系统内部异常");
                                break;
                            case 3:
                                listener.onFailed(rc,json.get("error").toString());
                                break;
                            case 4:
                                listener.onFailed(rc,"文本没有匹配的技能场景，技能不理解或不能处理该文本");
                                break;
                            default:
                                listener.onFailed(rc,"rc类型未定义");
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFailed(-1,e.getMessage());
                    }

                    listener.postUnderstand();
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                if(listener != null){
                    listener.onFailed(speechError.getErrorCode(),speechError.getErrorDescription());
                    listener.postUnderstand();
                }
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }
//    public void understand(String msg){
////        if(mAIUIServiceState != AIUIServiceState.STATE_WORKING){
////            wakeupAIUIService();
////        }
////        String params = "data_type=text";
////        byte[] textData = msg.getBytes();
////        AIUIMessage textMsg = new AIUIMessage(AIUIConstant.CMD_WRITE
////                , 0
////                , 0
////                , params
////                , textData);
////        mAIUIAgent.sendMessage(textMsg);
//    }
//
//    public void understand(){
//
//    }

//    private String getAIUIparams(){
//        HashMap<String,Object> strParams = new HashMap<>();
//        HashMap<String,String> param;
//
//        param = new HashMap<>();
//        param.put("appid",APPID);
//        strParams.put("login",param);
//
//        param = new HashMap<>();
//        param.put("interact_timeout","60000");
//        param.put("result_timeout","5000");
//        strParams.put("interact",param);
//
//        param = new HashMap<>();
//        param.put("scene","main");
//        strParams.put("global",param);
//
//        param = new HashMap<>();
//        param.put("res_type","assets");
//        param.put("res_path","vad/meta_vad_16k.jet");
//        strParams.put("vad",param);
//
//        param = new HashMap<>();
//        param.put("sample_rate","16000");
//        strParams.put("iat",param);
//
//        param = new HashMap<>();
//        param.put("data_source","sdk");
//        param.put("interact_mode","continuous");
//        strParams.put("speech",param);
//
//        Gson gson = new Gson();
//        Log.i("params",gson.toJson(strParams));
//        return gson.toJson(strParams);
//
////        Map<String,String> params = new HashMap();
//////        params.put("scene","main");
//////        params.put("interact_timeout","60000");
//////        params.put("result_timeout","5000");
//////        params.put("engine_type","meta");
//////        params.put("res_type","assets");
//////        params.put("res_path","");
//////        params.put("sample_rate","16000");
//////        params.put("data_source","sdk");
//////        params.put("interact_mode",mActivity.getApplicationContext().getPackageResourcePath() +
//////                "meta_vad_16k.jet");
//////        params.put("","");
//////        params.put("","");
////
////        StringBuilder strResult = new StringBuilder();
////        strResult.append("");
////        for (String key:params.keySet()) {
////            strResult.append(key + "=" + params.get(key) + ";");
////            Log.i("params",strResult.toString());
////        }
////        return strResult.toString();
////        return "";
//    }

//    private void startAIUISerivce(){
//        AIUIMessage startMsg = new AIUIMessage(AIUIConstant.CMD_START,
//                0,
//                0,
//                null,
//                null);
//        mAIUIAgent.sendMessage(startMsg);
//    }
//
//    private void wakeupAIUIService(){
//        AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_WAKEUP
//                    , 0
//                    , 0
//                    , ""
//                    , null);
//        mAIUIAgent.sendMessage(wakeupMsg);
//    }
//
//    private void stopAIUISerivce(){
//        AIUIMessage stopMsg = new AIUIMessage(AIUIConstant.CMD_STOP,
//                0,
//                0,
//                null,
//                null);
//        mAIUIAgent.sendMessage(stopMsg);
//    }
//
//    private void getAiuiServiceState(){
//        AIUIMessage getStateMsg = new AIUIMessage(AIUIConstant.CMD_GET_STATE,
//                0,
//                0,
//                null,
//                null);
//        mAIUIAgent.sendMessage(getStateMsg);
//    }


    //    /**
//     * 语义识别 初始化
//     */
//    public void InitAIUIAgent(final IflytekHelper.AIUIListener listener){
//        mAIUIEventType = AIUIEventType.InitCheck;
//        mAIUIAgent = AIUIAgent.createAgent(mActivity.getApplicationContext(),
//                getAIUIparams(),
//                new com.iflytek.aiui.AIUIListener() {
//                    @Override
//                    public void onEvent(AIUIEvent aiuiEvent) {
//                        Log.i("event",String.valueOf(aiuiEvent.eventType));
//                        switch (aiuiEvent.eventType){
//                            case AIUIConstant.EVENT_RESULT:
//                                Log.i("result","--------------------------------------");
//                                Log.i("arg1",String.valueOf(aiuiEvent.arg1));
//                                Log.i("arg2",String.valueOf(aiuiEvent.arg2));
//                                Log.i("info",aiuiEvent.info);
//                                Log.i("data",aiuiEvent.data.toString());
//                                Log.i("result","--------------------------------------");
//                                break;
//                            case AIUIConstant.EVENT_STATE:
//                                switch (aiuiEvent.arg1){
//                                    case AIUIConstant.STATE_IDLE:
//                                        Log.i("state","STATE_IDLE");
//                                        mAIUIServiceState = AIUIServiceState.STATE_IDLE;
//                                        if(listener != null){
//                                            listener.onStateChanged(AIUIServiceState.STATE_IDLE);
//                                        }
//                                        break;
//                                    case AIUIConstant.STATE_READY:
//                                        Log.i("state","STATE_READY");
//                                        mAIUIServiceState = AIUIServiceState.STATE_READY;
//                                        if(listener != null){
//                                            if(mAIUIEventType == AIUIEventType.InitCheck){
//                                                mAIUIEventType = AIUIEventType.None;
//                                                listener.onInitSuccess();
//                                            }
//                                            listener.onStateChanged(AIUIServiceState.STATE_READY);
//                                        }
//                                        break;
//                                    case AIUIConstant.STATE_WORKING:
//                                        Log.i("state","STATE_WORKING");
//                                        mAIUIServiceState = AIUIServiceState.STATE_WORKING;
//                                        if(listener != null){
//                                            listener.onStateChanged(AIUIServiceState.STATE_WORKING);
//                                        }
//                                        break;
//                                }
//                                break;
//                            case AIUIConstant.EVENT_ERROR:
//                                if(listener != null){
//                                    if(mAIUIEventType == AIUIEventType.InitCheck){
//                                        mAIUIEventType = AIUIEventType.None;
//                                        listener.onInitFailed(aiuiEvent.arg1,aiuiEvent.info);
//                                    }
//                                    else{
//                                        listener.onError(aiuiEvent.arg1,aiuiEvent.info);
//                                    }
//                                }
//                                break;
//                        }
//                    }
//                });
//    }
}
