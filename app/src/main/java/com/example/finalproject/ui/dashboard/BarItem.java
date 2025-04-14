package com.example.finalproject.ui.dashboard;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BarItem implements Parcelable {
    private final String name;
    private final int imageResId;
    private final String address;
    private double totalRating; // 累计的总评分
    private int ratingCount;    // 评分次数

    // 计算平均分：总评分 / 次数
    public double getAverageRating() {
        if (ratingCount <= 0) return 0;
        BigDecimal total = new BigDecimal(String.valueOf(totalRating));
        BigDecimal count = new BigDecimal(ratingCount);
        return total.divide(count, 1, RoundingMode.HALF_UP).doubleValue();
    }

    // 构造函数：用于创建新对象时，传入的 initialRating 为平均分
    public BarItem(String name, int imageResId, String address,
                   double initialRating, int ratingCount) {
        this.name = name;
        this.imageResId = imageResId;
        this.address = address;
        // 假设 initialRating 为平均分，这里转换成总评分
        this.totalRating = initialRating * ratingCount;
        this.ratingCount = ratingCount;
    }

    // 新增构造函数：用于从数据库中读取数据时，不再进行乘法运算
    public BarItem(String name, int imageResId, String address,
                   double totalRating, int ratingCount, boolean fromDb) {
        this.name = name;
        this.imageResId = imageResId;
        this.address = address;
        // 数据库中存储的就是累计的总评分
        this.totalRating = totalRating;
        this.ratingCount = ratingCount;
    }

    // 评分累加方法
    public void addRating(double newRating) {
        totalRating += newRating;
        ratingCount++;
    }

    // Parcelable 相关代码等保持不变……
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

    // Getter 方法……
    public String getName() { return name; }
    public int getImageResId() { return imageResId; }
    public int getRatingCount() { return ratingCount; }
    public String getAddress() { return address; }
    public double getTotalRating(){ return totalRating; }

    // 便捷的格式化评分
    public String getFormattedRating() {
        return String.format("%.1f（%d人评分）", getAverageRating(), ratingCount);
    }
}
