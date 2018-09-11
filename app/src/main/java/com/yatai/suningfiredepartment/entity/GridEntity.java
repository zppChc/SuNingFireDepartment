package com.yatai.suningfiredepartment.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GridEntity implements Parcelable {
    private int id;
    private int parent_id;
    private int grid_level;
    private String name;
    private List<List<Double>> polygon;
    private int user_id;
    private String image;

    protected GridEntity(Parcel in) {
        id = in.readInt();
        parent_id = in.readInt();
        grid_level = in.readInt();
        name = in.readString();
        user_id = in.readInt();
        image = in.readString();
    }

    public static final Creator<GridEntity> CREATOR = new Creator<GridEntity>() {
        @Override
        public GridEntity createFromParcel(Parcel in) {
            return new GridEntity(in);
        }

        @Override
        public GridEntity[] newArray(int size) {
            return new GridEntity[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getGrid_level() {
        return grid_level;
    }

    public void setGrid_level(int grid_level) {
        this.grid_level = grid_level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<Double>> getPolygon() {
        return polygon;
    }

    public void setPolygon(List<List<Double>> polygon) {
        this.polygon = polygon;
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
        parcel.writeInt(parent_id);
        parcel.writeInt(grid_level);
        parcel.writeString(name);
        parcel.writeInt(user_id);
        parcel.writeString(image);
    }
}
