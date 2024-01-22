package com.example.employee.service;

import com.example.employee.model.service.ProjectServiceModel;
import com.example.employee.model.view.ProjectViewModel;

import java.util.List;

public interface AnalyzeFile {
    void analyzeAndSaveFile(ProjectServiceModel projectServiceModel);
    List<ProjectViewModel> findTheDurationAndPairsOfProjects(String fileId);
}
