package com.yatai.suningfiredepartment.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class DepartmentEntity implements Parcelable{
    private int id;
    private String name;
    private int grid_id;
    private int user_id;
    private String image;

    protected DepartmentEntity(Parcel in) {
        id = in.readInt();
        name = in.readString();
        grid_id = in.readInt();
        user_id = in.readInt();
        image = in.readString();
    }

    public static final Creator<DepartmentEntity> CREATOR = new Creator<DepartmentEntity>() {
        @Override
        public DepartmentEntity createFromParcel(Parcel in) {
            return new DepartmentEntity(in);
        }

        @Override
        public DepartmentEntity[] newArray(int size) {
            return new DepartmentEntity[size];
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

    public int getGrid_id() {
        return grid_id;
    }

    public void setGrid_id(int grid_id) {
        this.grid_id = grid_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
        parcel.writeInt(grid_id);
        parcel.writeInt(user_id);
        parcel.writeString(image);
    }
}
