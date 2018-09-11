package com.yatai.suningfiredepartment.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WorkTemplateEntity implements Parcelable{
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;

    private String content;

    protected WorkTemplateEntity(Parcel in) {
        name = in.readString();
        type = in.readString();
        content = in.readString();
    }

    public static final Creator<WorkTemplateEntity> CREATOR = new Creator<WorkTemplateEntity>() {
        @Override
        public WorkTemplateEntity createFromParcel(Parcel in) {
            return new WorkTemplateEntity(in);
        }

        @Override
        public WorkTemplateEntity[] newArray(int size) {
            return new WorkTemplateEntity[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(content);
    }
}
