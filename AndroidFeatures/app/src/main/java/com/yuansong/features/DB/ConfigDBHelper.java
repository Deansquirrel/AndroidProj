package com.yuansong.features.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yuansong.features.Common.Global;


/**
 * Created by yuansong on 2018/3/9.
 */

public class ConfigDBHelper extends SQLiteOpenHelper {

    private static final String DB_CONFIG_FILE_NAME = "config";

    /**
     * 内部缓存
     */
    private static volatile Context mContext = null;
    private static volatile ConfigDBHelper mConfigDBHelper = null;

    private static volatile String mWebAddress = "";
    private static volatile boolean isConfigChanged = false;

    /**
     * 私有构造函数
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    private ConfigDBHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }

    /**
     * 获取实例对象（单例模式）
     */
    public static ConfigDBHelper getInstance(Context context){
        if((mConfigDBHelper == null)||(!context.equals(mContext))){
            synchronized (ConfigDBHelper.class){
                if(mConfigDBHelper == null){
                    mConfigDBHelper = new ConfigDBHelper(context,DB_CONFIG_FILE_NAME,null,Global.SQLITE_DB_VERSION);
                    mContext = context;
                }
            }
        }
        return mConfigDBHelper;
    }

    //----------------------------------------------------------------------------------------------
    //Config表

    private static final String TABLE_NAME_CONFIG = "Config";

    private static final String CONFIG_CREATE = "CREATE TABLE " + TABLE_NAME_CONFIG + "(" +
            " WebAddress text " +
            " )";

    private static final String CONFIG_INSERT = "INSERT INTO " + TABLE_NAME_CONFIG + "(" +
            " WebAddress " +
            " ) values(?)";

    private static final String CONFIG_UPDATE = "UPDATE " + TABLE_NAME_CONFIG + " set " +
            " WebAddress = ? ";

    private static final String CONFIG_SELECT = "SELECT " +
            " WebAddress " +
            " FROM " + TABLE_NAME_CONFIG;

    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CONFIG_CREATE);
        Log.i("msg","SQLite DB config created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME_CONFIG);
        onCreate(db);
        Log.i("msg","SQLite DB config updated,oldVersion - " + String.valueOf(oldVersion) + ",newVersion - " + String.valueOf(newVersion));
    }

    /**
     * 判断SQLList中是否有数据
     * @return
     */
    private boolean hasUserData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.rawQuery("select count(*) from " + TABLE_NAME_CONFIG,null);
        c.moveToFirst();
        int rowCount = c.getInt(0);
        c.close();
        return (rowCount > 0);
    }

    /**
     * 更新缓存
     * @param webAddress
     */
    public void updateCacheConfig(String webAddress){
        mWebAddress = webAddress;
        isConfigChanged = true;
    }

    /**
     * 将缓存写入SQLite
     */
    public void saveCacheConfig(){
        if(isConfigChanged){
            isConfigChanged = false;
        }
        else{
            return;
        }

        String sql;
        if(hasUserData()){
            sql = CONFIG_UPDATE;
        }
        else{
            sql = CONFIG_INSERT;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql,new Object[]{
                mWebAddress
        });
    }

    /**
     * 从SQLLite中获取数据并更新缓存
     */
    public void refreshCacheConfig(){
        if(hasUserData()){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(CONFIG_SELECT,null);
            if(c.getCount() > 0){
                c.moveToFirst();
                mWebAddress = c.getString(0);
            }
            c.close();
        }
        else{
            mWebAddress = "";
        }
    }

    /**
     * 返回缓存中的WebAddress
     * @return
     */
    public static String getWebAddress(){
        return mWebAddress;
    }

}
