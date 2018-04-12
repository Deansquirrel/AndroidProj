package com.yuansong.features.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yuansong.features.Common.Global;

import java.util.Calendar;

/**
 * Created by yuansong on 2018/3/9.
 */

public class CacheDBHelper extends SQLiteOpenHelper {

    private static final String DB_CACHE_FILE_NAME = "cache";

    /**
     * 内部缓存
     */
    private static volatile Context mContext = null;
    private static volatile CacheDBHelper mCacheDBHelper = null;

    private static volatile int mUserId = -1;
    private static volatile String mUserName = "";
    private static volatile Calendar mUpdateTime = null;
    private static volatile boolean isUserChanged = false;

    /**
     * 私有构造函数
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    private CacheDBHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }

    /**
     * 获取实例对象（单例模式）
     */
    public static CacheDBHelper getInstance(Context context){

        if((mCacheDBHelper == null) || (!context.equals(mContext))){
            synchronized (CacheDBHelper.class){
                if(mCacheDBHelper == null){
                    mCacheDBHelper = new CacheDBHelper(context,DB_CACHE_FILE_NAME,null,Global.SQLITE_DB_VERSION);
                    mContext = context;
                }
            }
        }
        return mCacheDBHelper;
    }

    //----------------------------------------------------------------------------------------------
    //User表

    private static final String TABLE_NAME_USER = "User";

    private static final String USER_CREATE = "CREATE TABLE " + TABLE_NAME_USER + "(" +
            " UserId integer," +
            " UserName text," +
            " UpdateTimeYear integer," +
            " UpdateTimeMonth integer," +
            " UpdateTimeDay integer," +
            " UpdateTimeHour integer," +
            " UpdateTimeMin integer," +
            " UpdateTimeSec integer)";

    private static final String USER_INSERT = "INSERT INTO " + TABLE_NAME_USER + "(" +
            " UserId ," +
            " UserName ," +
            " UpdateTimeYear ," +
            " UpdateTimeMonth ," +
            " UpdateTimeDay ," +
            " UpdateTimeHour ," +
            " UpdateTimeMin ," +
            " UpdateTimeSec ) values(?,?,?,?,?,?,?,?)";

    private static final String USER_UPDATE = "UPDATE " + TABLE_NAME_USER + " set " +
            " UserId = ?," +
            " UserName = ?," +
            " UpdateTimeYear = ?," +
            " UpdateTimeMonth = ?," +
            " UpdateTimeDay = ?," +
            " UpdateTimeHour = ?," +
            " UpdateTimeMin = ?," +
            " UpdateTimeSec = ?";

    private static final String USER_SELECT = "SELECT " +
            " UserId," +
            " UserName ," +
            " UpdateTimeYear ," +
            " UpdateTimeMonth ," +
            " UpdateTimeDay ," +
            " UpdateTimeHour ," +
            " UpdateTimeMin ," +
            " UpdateTimeSec " +
            " FROM " + TABLE_NAME_USER;

    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_CREATE);
        Log.i("msg","SQLite DB cache created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME_USER);
        onCreate(db);
        Log.i("msg","SQLite DB cache updated,oldVersion - " + String.valueOf(oldVersion) + ",newVersion - " + String.valueOf(newVersion));
    }

    /**
     * 判断SQLList中是否有数据
     * @return
     */
    private boolean hasUserData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.rawQuery("select count(*) from " + TABLE_NAME_USER,null);
        c.moveToFirst();
        int rowCount = c.getInt(0);
        c.close();
        return (rowCount > 0);
    }

    /**
     * 更新缓存
     * @param userId
     * @param userName
     */
    public void updateCacheUser(int userId,String userName){
        mUserId = userId;
        mUserName = userName;
        mUpdateTime = Calendar.getInstance();
        isUserChanged = true;
    }

    /**
     * 将缓存写入SQLLite
     */
    public void saveCacheUser(){
        if(isUserChanged){
            isUserChanged = false;
        }
        else{
            return;
        }

        String sql;
        if(hasUserData()){
            sql = USER_UPDATE;
        }
        else{
            sql = USER_INSERT;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql,new Object[]{
                mUserId,
                mUserName,
                mUpdateTime.get(Calendar.YEAR),
                mUpdateTime.get(Calendar.MONTH),
                mUpdateTime.get(Calendar.DAY_OF_MONTH),
                mUpdateTime.get(Calendar.HOUR),
                mUpdateTime.get(Calendar.MINUTE),
                mUpdateTime.get(Calendar.SECOND)
        });
    }

    /**
     * 从SQLLite中获取数据并更新缓存
     */
    public void refreshCacheUser(){
        if(hasUserData()){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(USER_SELECT,null);
            if(c.getCount() > 0){
                c.moveToFirst();
                mUserId = c.getInt(0);
                mUserName = c.getString(1);

                Calendar calendar = Calendar.getInstance();
                calendar.set(c.getInt(2),c.getInt(3),c.getInt(4),
                        c.getInt(5),c.getInt(6),c.getInt(7));

                mUpdateTime = calendar;
            }
            c.close();
        }
        else{
            mUserId = -1;
            mUserName = "";
            mUpdateTime = null;
        }
    }

    /**
     * 返回缓存中的UserID
     * @return
     */
    public static int getUserId(){
        return mUserId;
    }

    /**
     * 返回缓存中的UserName
     * @return
     */
    public static String getUserName(){
        return mUserName;
    }

    /**
     * 返回缓存中的UpdateTime
     * @return
     */
    public static Calendar getUpdateTime(){
        return mUpdateTime;
    }

}
