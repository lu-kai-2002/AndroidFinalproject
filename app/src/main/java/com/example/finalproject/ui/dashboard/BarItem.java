package com.example.finalproject.ui.dashboard;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BarItem implements Parcelable{
    private final String name;
    private final int imageResId;
    private final String address;
    private double rating;

    // 新增字段
    private double totalRating; // 总评分
    private int ratingCount;    // 评分次数

    // 计算平均分的方法
    public double getAverageRating() {
        if (ratingCount <= 0) return 0;
        // 添加精确计算保护
        BigDecimal total = new BigDecimal(String.valueOf(totalRating));
        BigDecimal count = new BigDecimal(ratingCount);
        return total.divide(count, 1, RoundingMode.HALF_UP).doubleValue();
    }
    public BarItem(String name, int imageResId, String address,
                   double initialRating, int ratingCount) {
        this.name = name;
        this.imageResId = imageResId;
        this.address = address;
        this.totalRating = initialRating * ratingCount;
        this.ratingCount = ratingCount;
    }



    // 添加评分
    public void addRating(double newRating) {
        totalRating += newRating;
        ratingCount++;
    }

    // Parcelable 实现需要新增字段
    protected BarItem(Parcel in) {
        name = in.readString();
        imageResId = in.readInt();
        address = in.readString();
        totalRating = in.readDouble();
        ratingCount = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(imageResId);
        dest.writeString(address);
        dest.writeDouble(totalRating);
        dest.writeInt(ratingCount);
    }

    // 生成ContentValues的方法
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(BarDbHelper.COLUMN_NAME, name);
        values.put(BarDbHelper.COLUMN_IMAGE_RES_ID, imageResId);
        values.put(BarDbHelper.COLUMN_ADDRESS, address);
        values.put(BarDbHelper.COLUMN_TOTAL_RATING, totalRating);
        values.put(BarDbHelper.COLUMN_RATING_COUNT, ratingCount);
        return values;
    }


    public double getTotalRating(){return totalRating;}
    // Getter 方法
    public String getName() { return name; }
    public int getImageResId() { return imageResId; }
    public int getRatingCount() { return ratingCount; }
    public String getAddress() { return address; }
    public float getRating() { return (float)rating; }


    public static final Parcelable.Creator<BarItem> CREATOR = new Parcelable.Creator<BarItem>() {
        @Override
        public BarItem createFromParcel(Parcel in) {
            return new BarItem(in);
        }

        @Override
        public BarItem[] newArray(int size) {
            return new BarItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    // 新增评分更新方法
    public void updateRating(double newRating) {
        this.rating = newRating;
    }
    // 新增便捷方法
    public String getFormattedRating() {
        return String.format("%.1f（%d人评分）", getAverageRating(), ratingCount);
    }


}
