package com.yatai.suningfiredepartment.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecordEntity implements Parcelable {
    @SerializedName("record_id")
    private int recordId;
    @SerializedName("image")
    private String image;

    protected RecordEntity(Parcel in) {
        recordId = in.readInt();
        image = in.readString();
    }

    public RecordEntity(){

    }
    public static final Creator<RecordEntity> CREATOR = new Creator<RecordEntity>() {
        @Override
        public RecordEntity createFromParcel(Parcel in) {
            return new RecordEntity(in);
        }

        @Override
        public RecordEntity[] newArray(int size) {
            return new RecordEntity[size];
        }
    };

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
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
        parcel.writeInt(recordId);
        parcel.writeString(image);
    }
}
