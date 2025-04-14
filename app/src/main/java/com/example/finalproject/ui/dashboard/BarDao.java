package com.example.finalproject.ui.dashboard;

import static com.example.finalproject.ui.dashboard.BarDbHelper.COLUMN_NAME;
import static com.example.finalproject.ui.dashboard.BarDbHelper.COLUMN_RATING_COUNT;
import static com.example.finalproject.ui.dashboard.BarDbHelper.COLUMN_TOTAL_RATING;
import static com.example.finalproject.ui.dashboard.BarDbHelper.TABLE_BARS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BarDao {
    private final BarDbHelper dbHelper;

    public BarDao(Context context) {
        dbHelper = new BarDbHelper(context);
    }

    public List<BarItem> getAllBars() {
        List<BarItem> bars = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 按平均分降序排序 (total_rating/rating_count)
        String query = "SELECT *, (" + COLUMN_TOTAL_RATING + "/" +
                COLUMN_RATING_COUNT + ") AS avg_rating " +
                "FROM " + TABLE_BARS + " " +
                "ORDER BY avg_rating DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                bars.add(createBarItemFromCursor(cursor));
            }
            cursor.close();
        }
        db.close();
        return bars;
    }

    public void updateRating(String barName, double newRating) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        double newTotal = 0;
        int newCount = 0;
        try {
            // 1. 获取当前数据
            String[] columns = {COLUMN_TOTAL_RATING, COLUMN_RATING_COUNT};
            Cursor cursor = db.query(
                    TABLE_BARS,
                    columns,
                    COLUMN_NAME + " = ?",
                    new String[]{barName},
                    null, null, null
            );

            if (cursor.moveToFirst()) {
                // 2. 读取现有值
                double currentTotal = cursor.getDouble(0);
                int currentCount = cursor.getInt(1);

                // 3. 计算新值
                newTotal = currentTotal + newRating;
                newCount = currentCount + 1;

                // 4. 更新数据库
                ContentValues values = new ContentValues();
                values.put(COLUMN_TOTAL_RATING, newTotal);
                values.put(COLUMN_RATING_COUNT, newCount);

                db.update(
                        TABLE_BARS,
                        values,
                        COLUMN_NAME + " = ?",
                        new String[]{barName}
                );
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        Log.d("RatingDebug", "更新数据 - 名称: " + barName +
                " 总评分: " + newTotal +
                " 次数: " + newCount +
                " 平均分: " + (newTotal / newCount));
    }


    public BarItem getBarByName(String barName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        BarItem bar = getBarByName(db, barName);
        db.close();
        return bar;
    }

    // 内部方法：从Cursor创建BarItem对象
    private BarItem createBarItemFromCursor(Cursor cursor) {
        return new BarItem(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(BarDbHelper.COLUMN_IMAGE_RES_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(BarDbHelper.COLUMN_ADDRESS)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_RATING)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING_COUNT)),
                true
        );
    }

    // 内部方法：使用现有数据库连接查询
    private BarItem getBarByName(SQLiteDatabase db, String barName) {
        Cursor cursor = db.query(
                TABLE_BARS,
                null,
                COLUMN_NAME + " = ?",
                new String[]{barName},
                null, null, null
        );

        BarItem bar = null;
        if (cursor != null && cursor.moveToFirst()) {
            bar = createBarItemFromCursor(cursor);
            cursor.close();
        }
        return bar;
    }

    public void close() {
        dbHelper.close();
    }
    // 新增方法：添加评分（原子操作）
    public void addNewRating(String barName, double newRating) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        Cursor cursor = null;
        try {
            // 1. 获取当前数据
            String[] columns = {COLUMN_TOTAL_RATING, COLUMN_RATING_COUNT};
            cursor = db.query(TABLE_BARS, columns,
                    COLUMN_NAME + " = ?", new String[]{barName}, null, null, null);

            if (cursor.moveToFirst()) {
                // 2. 计算新值
                double currentTotal = cursor.getDouble(0);
                int currentCount = cursor.getInt(1);
                double newTotal = currentTotal + newRating;
                int newCount = currentCount + 1;

                // 3. 更新数据库
                ContentValues values = new ContentValues();
                values.put(COLUMN_TOTAL_RATING, newTotal);
                values.put(COLUMN_RATING_COUNT, newCount);
                db.update(TABLE_BARS, values, COLUMN_NAME + " = ?", new String[]{barName});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            cursor.close();
        }
    }
}