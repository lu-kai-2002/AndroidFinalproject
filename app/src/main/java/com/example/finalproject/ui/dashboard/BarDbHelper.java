package com.example.finalproject.ui.dashboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.finalproject.R;

public class BarDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bars.db";
    private static final int DATABASE_VERSION = 4; // 版本号升级

    // 表结构
    public static final String TABLE_BARS = "bars";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE_RES_ID = "image_res_id";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_TOTAL_RATING = "total_rating";
    public static final String COLUMN_RATING_COUNT = "rating_count";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_BARS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_IMAGE_RES_ID + " INTEGER NOT NULL, " +
                    COLUMN_ADDRESS + " TEXT NOT NULL, " +
                    COLUMN_TOTAL_RATING + " REAL DEFAULT 0, " +
                    COLUMN_RATING_COUNT + " INTEGER DEFAULT 0)"; // 新增评分次数列

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 完全删除旧表重建
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BARS);
        onCreate(db); // 重新创建新结构
    }

    // 修改初始化数据插入方法
    private void insertInitialData(SQLiteDatabase db) {
        // 清空旧数据
        db.delete(TABLE_BARS, null, null);

        BarItem[] defaultBars = {
                new BarItem("月光酒吧", R.drawable.bar1, "朝阳区酒仙桥路1号", 4.5, 1),
                new BarItem("星空酒廊", R.drawable.bar2, "海淀区中关村大街5号", 4.4, 1),
                new BarItem("蓝调之夜", R.drawable.bar3, "东城区王府井大街8号", 4.3, 1),
                new BarItem("爵士俱乐部", R.drawable.bar4, "西城区金融街3号", 4.2, 1),
                new BarItem("老地方酒吧", R.drawable.bar5, "丰台区方庄路12号", 4.1, 1)
        };

        for (BarItem bar : defaultBars) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, bar.getName());
            values.put(COLUMN_IMAGE_RES_ID, bar.getImageResId());
            values.put(COLUMN_ADDRESS, bar.getAddress());
            values.put(COLUMN_TOTAL_RATING, bar.getTotalRating());
            values.put(COLUMN_RATING_COUNT, bar.getRatingCount());

            db.insertOrThrow(TABLE_BARS, null, values);
        }
    }
    public BarDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        insertInitialData(db);
    }


}