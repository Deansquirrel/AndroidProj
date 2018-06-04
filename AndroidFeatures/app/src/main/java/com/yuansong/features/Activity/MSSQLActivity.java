package com.yuansong.features.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yuansong.features.BaseActivity;
import com.yuansong.features.Common.CommonFun;
import com.yuansong.features.DB.MsSqlHelper;
import com.yuansong.features.R;

public class MSSQLActivity extends BaseActivity {

    private Toolbar mToolbar = null;
    private Button mBtnSearch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mssql);

        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("MS SQL");
        setSupportActionBar(mToolbar);

        mBtnSearch = findViewById(R.id.btnSearch);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subSearch();
            }
        });

        showBackOption();
        setLightWindow();
    }


//    --长沙多喜来
//    select * from [dbo].[dtv5dtchdanz9csdxl20171127_ywdata-801huiz]
//    select * from [dbo].[dtv5dtchdanz9csdxl20171127_ywdata-801kuc]
//    select * from[dbo].[dtv5dtchdanz9csdxl20171127_ywdata-801caiw]
//    select * from[dbo].[dtv5dtchdanz9csdxl20171127_ywdata-801dd]
//    select * from[dbo].[dtv5dtchdanz9csdxl20171127_ywdata-801dhk]
//    select * from[dbo].[dtv5dtchdanz9csdxl20171127_ywdata-801hsq]
//    select * from[dbo].[dtv5dtchdanz9csdxl20171127_ywdata-801mdkd]
//    select * from[dbo].[dtv5dtchdanz9csdxl20171127_ywdata-801mdxs]
//    select * from[dbo].[dtv5dtchdanz9csdxl20171127_ywdata-801trfv3]
//    select * from[dbo].[dtv5dtchdanz9csdxl20171127_ywdata-801wul]
//    select * from[dbo].[dtv5dtchdanz9csdxl20171127_ywdata-801zaixzhh]
//    通道库地址：
//    server=szbj1dat.sqlserver.rds.aliyuncs.com,3433
//    dbname=szbj02data
//    user:szbj02data
//    pass:szbj02_8289986

    private void subSearch(){
        Log.i("msg","SQL Search");
        MsSqlHelper msSqlHelper = new MsSqlHelper(
                "szbj1dat.sqlserver.rds.aliyuncs.com",
                "3433",
                "szbj02data",
                "szbj02data",
                "szbj02_8289986");
        int count = msSqlHelper.ExecuteNonQuery("select count(*) from [dbo].[dtv5dtchdanz9csdxl20171127_ywdata-801huiz]",null);
        Log.i("count",String.valueOf(count));
        CommonFun.showMsg(MSSQLActivity.this,String.valueOf(count));
    }
}
