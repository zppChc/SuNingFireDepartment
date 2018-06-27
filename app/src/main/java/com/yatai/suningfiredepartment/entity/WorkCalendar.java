package com.yatai.suningfiredepartment.entity;


public class WorkCalendar {
    private int day;
    private int week;
    private WorkItemEntity task;
    private boolean hasTask;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public WorkItemEntity getTask() {
        return task;
    }

    public void setTask(WorkItemEntity task) {
        this.task = task;
    }


    public boolean isHasTask() {
        return hasTask;
    }

    public void setHasTask(boolean hasTask) {
        this.hasTask = hasTask;
    }
}
