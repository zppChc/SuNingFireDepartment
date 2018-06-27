package com.yatai.suningfiredepartment.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class WorkItemEntity implements Parcelable{
    private int id;

    private String name;

    private String repeat;

    private int grid_level;

    private List<WorkTemplateEntity> template ;

    private String task_category_id;

    private String description;

    private int record_id;

    private int status;

    private String image;

    protected WorkItemEntity(Parcel in) {
        id = in.readInt();
        name = in.readString();
        repeat = in.readString();
        grid_level = in.readInt();
        template = in.createTypedArrayList(WorkTemplateEntity.CREATOR);
        task_category_id = in.readString();
        description = in.readString();
        record_id = in.readInt();
        status = in.readInt();
        image = in.readString();
    }

    public static final Creator<WorkItemEntity> CREATOR = new Creator<WorkItemEntity>() {
        @Override
        public WorkItemEntity createFromParcel(Parcel in) {
            return new WorkItemEntity(in);
        }

        @Override
        public WorkItemEntity[] newArray(int size) {
            return new WorkItemEntity[size];
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

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public int getGrid_level() {
        return grid_level;
    }

    public void setGrid_level(int grid_level) {
        this.grid_level = grid_level;
    }

    public List<WorkTemplateEntity> getTemplate() {
        return template;
    }

    public void setTemplate(List<WorkTemplateEntity> template) {
        this.template = template;
    }

    public String getTask_category_id() {
        return task_category_id;
    }

    public void setTask_category_id(String task_category_id) {
        this.task_category_id = task_category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRecord_id() {
        return record_id;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        parcel.writeString(repeat);
        parcel.writeInt(grid_level);
        parcel.writeTypedList(template);
        parcel.writeString(task_category_id);
        parcel.writeString(description);
        parcel.writeInt(record_id);
        parcel.writeInt(status);
        parcel.writeString(image);
    }
}
