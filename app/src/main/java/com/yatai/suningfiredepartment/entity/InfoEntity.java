package com.yatai.suningfiredepartment.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class InfoEntity implements Parcelable{
    private String id;
    private String title;
    private String category_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    protected InfoEntity(Parcel in) {
        id = in.readString();
        title = in.readString();
        category_id = in.readString();
    }

    public static final Creator<InfoEntity> CREATOR = new Creator<InfoEntity>() {
        @Override
        public InfoEntity createFromParcel(Parcel in) {
            return new InfoEntity(in);
        }

        @Override
        public InfoEntity[] newArray(int size) {
            return new InfoEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(category_id);
    }
}
