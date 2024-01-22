package com.example.employee.model.view;

import java.time.LocalDate;

public class ProjectViewModel {
    private int firstEmployeeId;
    private int secondEmployeeId;
    private int projectId;
    private long days;

    public ProjectViewModel() {
    }


    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }


    public int getFirstEmployeeId() {
        return firstEmployeeId;
    }

    public void setFirstEmployeeId(int firstEmployeeId) {
        this.firstEmployeeId = firstEmployeeId;
    }

    public int getSecondEmployeeId() {
        return secondEmployeeId;
    }

    public void setSecondEmployeeId(int secondEmployeeId) {
        this.secondEmployeeId = secondEmployeeId;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }
}
