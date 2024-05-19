package com.yt.smartmarket.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    // 数据库名称
    public final static String MYDBNAME = "smartshopdb.db";
    // 版本号
    public final static int VERSION = 1;
    public DBHelper(@Nullable Context context) {
        super(context,MYDBNAME,null,VERSION);
    }
    // 注册、更新、删除操作 以写的方式打开数据库；登录、查询操作以读的方式打开数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建用户信息表--T-SQL语句
        String userSql = "CREATE TABLE Users (\n" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    Name TEXT NOT NULL,\n" +
                "    Email TEXT NOT NULL UNIQUE,\n" +
                "    Password TEXT NOT NULL,\n" +
                "    Phone TEXT ,\n" +
                "    Addr TEXT ,\n" +
                "    IsFirstLogin INTEGER NOT NULL,\n" +
                "    Avatar TEXT\n" +
                ");";
        db.execSQL(userSql);

        // 创建商品信息表

        // 创建订单表

        //

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    // 新用户的注册
    public String userRegister(SQLiteDatabase db,String userName,String Password,String Email,String Avatar){
        ContentValues values = new ContentValues();
        values.put("Name", userName);
        values.put("Password", Password);
        values.put("Email", Email);
        values.put("Avatar", Avatar);
        values.put("IsFirstLogin",1);//设置是第一次登录
        long result = db.insert("Users", null, values);
        if (result == -1) {
            return "注册失败";
        } else {
            return "注册成功";
        }
    }
}
