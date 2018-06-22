package com.yuansong.xf.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yuansong.xf.BaseActivity;
import com.yuansong.xf.Common.CommonFun;
import com.yuansong.xf.R;
import com.yuansong.xf.XF.IflytekHelper;

import java.util.Map;

public class UnderstanderTextActivity extends BaseActivity {

    private Toolbar mToolbar = null;
    private TextView mTextView = null;
    private EditText mEditText = null;
    private Button mButton = null;

    private IflytekHelper mIflytekHelper = null;

    Gson mGson = null;

    //缓存
    private int mTableNo = -1;
    private int mCurrPage = -1;
    private int mPageSize = -1;
    private int mPageCount = -1;
    private SparseArray<TableRow> tableInfo;

    private enum RowState{
        Waiting,
        Done,
        Cancel
    }

    private class TableRow{
        private String mName;
        private RowState mState;
        public TableRow(String name, RowState state){
            mName = name;
            mState = state;
        }

        public String getName() {
            return mName;
        }

        public RowState getState() {
            return mState;
        }
    }

    private enum RowAction{
        Done,
        Cancel
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_understander_text);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("语义理解 - 文本");
        setSupportActionBar(mToolbar);

        mTextView = findViewById(R.id.textView);
        mTextView.setText("");
        mTextView.setVisibility(View.GONE);

        mEditText = findViewById(R.id.editText);
        mEditText.setText("");
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().equals("")){
                    mButton.setEnabled(false);
                }
                else{
                    mButton.setEnabled(true);
                }
            }
        });

        mButton = findViewById(R.id.btnButton);
        mButton.setText("确  定");
        mButton.setEnabled(false);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIflytekHelper.understandText(mEditText.getText().toString().trim(),
                        new IflytekHelper.TextUnderstanderListener() {
                            @Override
                            public void preUnderstand() {
                                mButton.setEnabled(false);
                            }

                            @Override
                            public void postUnderstand() {
                                mButton.setEnabled(true);
                            }

                            @Override
                            public void onCompleted(int rc, String service, String intent, Map<String,String> data) {
                                Log.i("rc",String.valueOf(rc));
                                Log.i("service",service);
                                switch (service){
                                    case "YUANSONG.ShowMenu":
                                        switch (intent){
                                            case "ShowMenu":
                                                showMenu(Integer.valueOf(data.get("tableNo")),10,-1);
                                                break;
                                            case "PageAction":
                                                String action = data.get("PageAction");
                                                switch (action){
                                                    case "上一页":
                                                        pageUp();
                                                        break;
                                                    case "下一页":
                                                        pageDown();
                                                        break;
                                                }
                                                break;
                                            case "MenuAction":
                                                Log.i("data",mGson.toJson(data));
                                                Log.i("rowNo",data.get("rowNo"));
                                                Log.i("menuAction",data.get("menuAction"));
                                                int rowNo = Integer.valueOf(data.get("rowNo"));
                                                switch(data.get("menuAction")){
                                                    case "上菜":
                                                        menuAction(rowNo,RowAction.Done);
                                                        break;
                                                    case "撤菜":
                                                        menuAction(rowNo,RowAction.Cancel);
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                }
                            }

                            @Override
                            public void onFailed(int errCode, String errDesc) {
                                Log.w("warn",
                                        "语义理解遇到错误(" + String.valueOf(errCode) + "|" + errDesc);
                                Log.w("warn",
                                        "语义理解文本 - " + mEditText.getText().toString().trim());
                                CommonFun.showError(UnderstanderTextActivity.this,
                                        "语义理解遇到错误(" + String.valueOf(errCode) + "|" + errDesc,
                                        false);

                            }
                        });
            }
        });

        tableInfo = new SparseArray<>();
        mIflytekHelper = new IflytekHelper(UnderstanderTextActivity.this);
        mButton.setEnabled(false);
        mIflytekHelper.InitTextUnderstander(new IflytekHelper.InitListener() {
            @Override
            public void onSuccess() {
                Log.i("msg","语义理解初始化成功");
            }

            @Override
            public void onFailed(int errCode) {
                Log.w("warn","语义理解初始化失败（" + String.valueOf(errCode) + "）");
                CommonFun.showError(UnderstanderTextActivity.this,
                        "语义理解初始化失败（" + String.valueOf(errCode) + "）",true);
            }
        });

        mGson = new Gson();

        setLightWindow();
        showBackOption();
    }

//        UnderstanderResult r = mGson.fromJson(result,UnderstanderResult.class);
//        switch (r.getRc()){
//            case 0:
//                Log.i("msg","操作成功");
//                Log.i("type",r.getService());
//                switch (r.getService()){
//                    case "YUANSONG.ShowMenu":
//                        Log.i("action type","ShowMenu");
//                        Log.i("semantic",r.getSemantic().toString());
//                        showMenu(10);
//
//                        break;
//                }
//                break;
//            case 1:
//                Log.i("msg","输入异常");
//                break;
//            case 2:
//                Log.i("msg","系统内部异常");
//                break;
//            case 3:
//                Log.i("msg","业务操作失败，错误信息在error字段描述");
//                break;
//            case 4:
//                Log.i("msg","文本没有匹配的技能场景，技能不理解或不能处理该文本");
//                break;
//        }

    private void showMenu(int tableNo, int pageSize, int currPage){
        if(tableNo != mTableNo){
            tableInfo.clear();
            mTableNo = tableNo;
            for(int i=1;i<=50;i++){
                tableInfo.append(i,new TableRow("菜品-" + String.valueOf(i), RowState.Waiting));
            }
        }

        mPageSize = pageSize;
        mCurrPage = currPage;
        int ram = tableInfo.size() % mPageSize;
        int pageCount = tableInfo.size() / mPageSize;
        if(ram > 0){
            pageCount = pageCount + 1;
        }
        mPageCount = pageCount;
        if(mCurrPage < 1 || mCurrPage > mPageCount){
            mCurrPage = 1;
        }

        int itemMin = pageSize * (mCurrPage - 1);
        if(itemMin < 0 || itemMin >= tableInfo.size()){
            itemMin = 0;
        }
        int itemMax = pageSize * mCurrPage;
        if(itemMax > tableInfo.size()){
            itemMax = tableInfo.size();
        }
        SparseArray<TableRow> page = new SparseArray<>();
        for(int i=0;i<itemMax - itemMin;i++){
            page.append(i+1,tableInfo.get(i + 1 + itemMin));
        }

        Log.i("min",String.valueOf(itemMin));
        Log.i("max",String.valueOf(itemMax));
        Log.i("pageCount",String.valueOf(pageCount));
        Log.i("count",String.valueOf(page.size()));
        Log.i("count",String.valueOf(tableInfo.size()));

        StringBuilder sb = new StringBuilder()
                .append("桌号 - ").append(tableNo).append("  ").append(String.valueOf(mCurrPage)).append("/").append(String.valueOf(pageCount)).append("\n");

        for(int i=1;i<=page.size();i++){
            TableRow row = page.get(i);
            sb.append(String.valueOf(i)).append("  ").append(row.getName()).append("  ").append(row.getState().toString());
            if(i != page.size()){
                sb.append("\n");
            }
        }

        mTextView.setText(sb.toString());
        mTextView.setVisibility(View.VISIBLE);

        mEditText.setText("");
    }

    private void pageUp(){
        Log.i("PageAction","Up");
        if(mCurrPage > 1){
            showMenu(mTableNo,mPageSize,mCurrPage - 1);
        }
        else{
            CommonFun.showMsg(UnderstanderTextActivity.this,"已到首页");
        }
    }

    private void pageDown(){
        Log.i("PageAction","Down");
        if(mCurrPage < mPageCount){
            showMenu(mTableNo,mPageSize,mCurrPage + 1);
        }
        else{
            CommonFun.showMsg(UnderstanderTextActivity.this,"已到尾页");
        }
    }

    private void menuAction(int rowNo, RowAction rowAction){
        Log.i("MenuAction",String.valueOf(rowNo) + " | " + rowAction.toString());
        if(rowNo > mPageSize || rowNo < 1){
            String msg = "没有 " + String.valueOf(rowNo) + " 行";
            Log.w("warn",msg);
            CommonFun.showMsg(UnderstanderTextActivity.this,msg);
        }
        else{
            int key = mPageSize * (mCurrPage - 1) + rowNo;
            TableRow tableRow = tableInfo.get(key);
            switch (rowAction){
                case Done:
                    tableRow.mState = RowState.Done;
                    break;
                case Cancel:
                    tableRow.mState = RowState.Cancel;
                    break;
            }
            tableInfo.append(key,tableRow);
            showMenu(mTableNo,mPageSize,mCurrPage);
        }
    }
}
