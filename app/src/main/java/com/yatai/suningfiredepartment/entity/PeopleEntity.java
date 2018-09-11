package com.yatai.suningfiredepartment.entity;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class PeopleEntity implements Parcelable{
    private int id;
    private String name;
    private String address;
    private String mobile;
    private String id_card;
    private int grid_id;
    private String image;

    protected PeopleEntity(Parcel in) {
        id = in.readInt();
        name = in.readString();
        address = in.readString();
        mobile = in.readString();
        id_card = in.readString();
        grid_id = in.readInt();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(mobile);
        dest.writeString(id_card);
        dest.writeInt(grid_id);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PeopleEntity> CREATOR = new Creator<PeopleEntity>() {
        @Override
        public PeopleEntity createFromParcel(Parcel in) {
            return new PeopleEntity(in);
        }

        @Override
        public PeopleEntity[] newArray(int size) {
            return new PeopleEntity[size];
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

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
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
}
