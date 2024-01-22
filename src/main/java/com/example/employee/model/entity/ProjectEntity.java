package com.example.employee.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.time.LocalDate;
@Entity
@Table(name="projects")
@Transactional
public class ProjectEntity extends BaseEntity {
    private int employeeId;
    private int projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String fileId;
    private long days;

    public ProjectEntity(){}

    @Column(name="employee_id",nullable = false)
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Column(name="project_id",nullable = false)
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    @Column(name="file_id",nullable = false)
    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Column(name="date_from",nullable = false)
    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }
    @Column(name="date_to",nullable = false)
    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }
    @Column(name="days",nullable = false)
    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }
}
