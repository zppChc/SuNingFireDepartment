package com.yatai.suningfiredepartment.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class HomeWorkCategoryEntity implements Parcelable{
    private int id;
    private String name;
    private String image;

    protected HomeWorkCategoryEntity(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.readString();
    }

    public static final Creator<HomeWorkCategoryEntity> CREATOR = new Creator<HomeWorkCategoryEntity>() {
        @Override
        public HomeWorkCategoryEntity createFromParcel(Parcel in) {
            return new HomeWorkCategoryEntity(in);
        }

        @Override
        public HomeWorkCategoryEntity[] newArray(int size) {
            return new HomeWorkCategoryEntity[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(image);
    }
}
