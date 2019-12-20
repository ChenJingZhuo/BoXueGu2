package com.boxuegu.sqlite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static java.sql.Types.VARCHAR;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    public static String DB_NAME = "bxg.db";
    public static final String U_USERINFO = "userinfo";
    public static final String U_VIDEO_PLAY_LIST = "videoplaylist";     //视频播放列表

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + U_USERINFO + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "userName VARCHAR,"
                + "nickName VARCHAR,"
                + "sex VARCHAR,"
                + "signature VARCHAR"
                + ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + U_VIDEO_PLAY_LIST + "("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "userName VARCHAR, "
                + "chapterId INT,"
                + "videoId VARCHAR, "
                + "videoPath VARCHAR, "
                + "title VARCHAR, "
                + "secondTitle VARCHAR"
                + ")");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + U_USERINFO);
        db.execSQL("DROP TABLE IF EXISTS " + U_VIDEO_PLAY_LIST);
        onCreate(db);
    }
}
