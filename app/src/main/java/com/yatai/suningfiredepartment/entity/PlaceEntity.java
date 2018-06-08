package com.yatai.suningfiredepartment.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaceEntity implements Parcelable {
    private int id;
    private String name;
    private String address;
    private String mobile;
    private int grid_id;
    private String image;

    protected PlaceEntity(Parcel in) {
        id = in.readInt();
        name = in.readString();
        address = in.readString();
        mobile = in.readString();
        grid_id = in.readInt();
        image = in.readString();
    }

    public static final Creator<PlaceEntity> CREATOR = new Creator<PlaceEntity>() {
        @Override
        public PlaceEntity createFromParcel(Parcel in) {
            return new PlaceEntity(in);
        }

        @Override
        public PlaceEntity[] newArray(int size) {
            return new PlaceEntity[size];
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getGrid_id() {
        return grid_id;
    }

    public void setGrid_id(int grid_id) {
        this.grid_id = grid_id;
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
        parcel.writeString(address);
        parcel.writeString(mobile);
        parcel.writeInt(grid_id);
        parcel.writeString(image);
    }
}
