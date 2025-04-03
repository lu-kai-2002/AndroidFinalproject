package com.example.finalproject.ui.dashboard;

import android.os.Parcel;
import android.os.Parcelable;

public class BarItem implements Parcelable{
    private final String name;
    private final int imageResId;
    private final String address;
    private double rating;

    public BarItem(String name, int imageResId, String address, double rating) {
        this.name = name;
        this.imageResId = imageResId;
        this.address = address;
        this.rating = rating;
    }

    // Getter 方法
    public String getName() { return name; }
    public int getImageResId() { return imageResId; }
    public String getAddress() { return address; }
    public float getRating() { return (float)rating; }
    protected BarItem(Parcel in) {
        name = in.readString();
        imageResId = in.readInt();
        address = in.readString();
        rating = in.readDouble();
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
    // 新增评分更新方法
    public void updateRating(double newRating) {
        this.rating = newRating;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(imageResId);
        dest.writeString(address);
        dest.writeDouble(rating);
    }
}
