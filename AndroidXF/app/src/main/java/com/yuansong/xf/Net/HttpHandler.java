package com.yuansong.xf.Net;


import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpHandler {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public interface ICallBack {
        void onGetData(String data);
        void onGetError(Exception ex);
        void onPreExecute();
        void onPostExecute();
    }

    private ICallBack mCallBack = null;
    private Integer mTimeout = 10 * 1000;
    //当前对象是否忙碌，禁止重复调用（避免回调混乱）
    private boolean mIsWorking = false;

    private String mData;
    private Exception mEx = null;
    private boolean mHasError = false;

    public void setTimeout(int timeout){
        mTimeout = timeout;
    }


    /**
     * 获取http信息（GET）
     * @param url 网址
     * @param callBack 回调接口
     */
    public void getHttpData_Get(String url,ICallBack callBack){
        if(callBack != null){
            if(mIsWorking){
                callBack.onGetError(new Exception("HTTP调用中，禁止重复调用"));
            }
            else{
                mCallBack = callBack;
                HttpHandleGetWorker httpHandleGetWorker = new HttpHandleGetWorker();
                httpHandleGetWorker.execute(url);
            }
        }
        else{
            Log.w("warn","回调对象为空，不执行任何操作");
        }
    }

    /**
     * 获取http信息（POST）
     * @param url 网址
     * @param data JSON数据
     * @param callBack 回调接口
     */
    public void getHttpData_Post(String url, String data, ICallBack callBack){
        if(callBack != null){
            if(mIsWorking){
                callBack.onGetError(new Exception("HTTP调用中，禁止重复调用"));
            }
            else{
                mCallBack = callBack;
                HttpHandlePostWorker httpHandlePostWorker = new HttpHandlePostWorker();
                httpHandlePostWorker.execute(url,data);
            }
        }
        else{
            Log.w("warn","回调对象为空，不执行任何操作");
        }
    }

    private class HttpHandleGetWorker extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(mTimeout, TimeUnit.MILLISECONDS)
                    .readTimeout(mTimeout,TimeUnit.MILLISECONDS)
                    .build();
            String url = params[0];
            final Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response;
            try{
                response = client.newCall(request).execute();
                mData = response.body().string();
                mHasError = false;
            }
            catch (Exception ex){
                mEx = ex;
                mHasError = true;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mIsWorking = true;
            if(mCallBack != null){
                mCallBack.onPreExecute();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mCallBack != null){
                if(mHasError){
                    mCallBack.onGetError(mEx);
                }
                else{
                    mCallBack.onGetData(mData);
                }
            }
            if(mCallBack != null){
                mCallBack.onPostExecute();
            }
            mIsWorking = false;
        }
    }
    
    private class HttpHandlePostWorker extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(mTimeout, TimeUnit.MILLISECONDS)
                    .readTimeout(mTimeout,TimeUnit.MILLISECONDS)
                    .build();
            String url = params[0];
            String data = params[1];
            RequestBody body = RequestBody.create(JSON, data);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response;
            try{
                response = client.newCall(request).execute();
                mData = response.body().string();
                mHasError = false;
            }
            catch (Exception ex){
                mEx = ex;
                mHasError = true;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mIsWorking = true;
            if(mCallBack != null){
                mCallBack.onPreExecute();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(mCallBack != null){
                if(mHasError){
                    mCallBack.onGetError(mEx);
                }
                else{
                    mCallBack.onGetData(mData);
                }
            }
            if(mCallBack != null){
                mCallBack.onPostExecute();
            }
            mIsWorking = false;
        }
    }
}
