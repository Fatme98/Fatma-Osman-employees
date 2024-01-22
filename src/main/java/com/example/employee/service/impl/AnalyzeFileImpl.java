package com.example.employee.service.impl;

import com.example.employee.error.NegativeDaysBetweenDates;
import com.example.employee.error.PairNotFoundException;
import com.example.employee.model.entity.ProjectEntity;
import com.example.employee.model.service.ProjectServiceModel;
import com.example.employee.model.view.ProjectViewModel;
import com.example.employee.repository.ProjectRepository;
import com.example.employee.service.AnalyzeFile;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.validator.DateValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyzeFileImpl implements AnalyzeFile {
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public AnalyzeFileImpl(ProjectRepository projectRepository, ModelMapper modelMapper){
        this.projectRepository = projectRepository;
        this.modelMapper=modelMapper;
    }
    @Override
    public void analyzeAndSaveFile(ProjectServiceModel projectServiceModel) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setEmployeeId(projectServiceModel.getEmployeeId());
        projectEntity.setProjectId(projectServiceModel.getProjectId());
        projectEntity.setFileId(projectServiceModel.getFileId());
        DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ofPattern("[yyyy/MM/dd]"+"[dd/MM/yyyy]"+"[MM/dd/yyyy]"+"[yyyy-MM-dd]"+"[dd-MM-yyyy]"+"[MM-dd-yyyy]"));
        DateTimeFormatter dateTimeFormatter = dateTimeFormatterBuilder.toFormatter();
        LocalDate dateFrom = LocalDate.parse(projectServiceModel.getDateFrom(),dateTimeFormatter);

        LocalDate dateTo = LocalDate.now();
        if(projectServiceModel.getDateTo()!=null){
            dateTo = LocalDate.parse(projectServiceModel.getDateTo(),dateTimeFormatter);
        }else{
            dateTo = LocalDate.now();
        }
        long days= ChronoUnit.DAYS.between(dateFrom, dateTo);
        if(days<0){
            throw new NegativeDaysBetweenDates();
        }
        projectEntity.setDateFrom(dateFrom);
        projectEntity.setDateTo(dateTo);
        projectEntity.setDays(days);
        this.projectRepository.saveAndFlush(projectEntity);
    }

    @Override
    public List<ProjectViewModel> findTheDurationAndPairsOfProjects(String fileId){
        List<ProjectViewModel>projectViewModels = new ArrayList<>();
        List<Integer>projects  = this.projectRepository.findByFileId(fileId).stream()
                .map(ProjectEntity::getProjectId).distinct().collect(Collectors.toList());
        for (Integer project : projects) {
            List<ProjectEntity> empByProjectId = this.projectRepository.findByProjectIdAndFileId(project,fileId);
            if(empByProjectId.size()==2){
                ProjectViewModel projectViewModel = new ProjectViewModel();
                projectViewModel.setFirstEmployeeId(empByProjectId.get(0).getEmployeeId());
                projectViewModel.setSecondEmployeeId(empByProjectId.get(1).getEmployeeId());
                projectViewModel.setProjectId(project);
                projectViewModel.setDays(empByProjectId.get(0).getDays());
                projectViewModels.add(projectViewModel);
            }else{
                throw new PairNotFoundException();
            }
        }
        return projectViewModels;
    }

}
