package com.example.employee.web;

import com.example.employee.error.FileNotFoundException;
import com.example.employee.error.FileNotUploadedException;
import com.example.employee.error.InputDataNotPresent;
import com.example.employee.model.service.ProjectServiceModel;
import com.example.employee.model.view.ProjectViewModel;
import com.example.employee.service.AnalyzeFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/file")
public class AnalyzeCsvFileController {
    private final AnalyzeFile analyzeFile;

    @Autowired
    public AnalyzeCsvFileController(AnalyzeFile analyzeFile) {
        this.analyzeFile = analyzeFile;
    }

    @GetMapping("/add-file")
    public ModelAndView addFile(ModelAndView modelAndView, @AuthenticationPrincipal Principal principal){
        modelAndView.setViewName("add_file");
        return modelAndView;
    }

    @PostMapping("/upload")
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal Principal principal, ModelAndView modelAndView){
        String fileId = UUID.randomUUID().toString();
        if(file.isEmpty()){
            throw new FileNotFoundException();
        }else{
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line="";
                while((line=reader.readLine())!=null){
                    String[] data = line.substring(1,line.length()-1).split(",");
                    validateInputData(data[0],data[1], data[2]);
                    if(isANumber(data[0])&&isANumber(data[1])){
                        int employeeId = Integer.parseInt(data[0]);
                        int projectId = Integer.parseInt(data[1]);
                        String dateFrom = data[2];
                        String dateTo = data[3];
                        ProjectServiceModel projectServiceModel = new ProjectServiceModel();
                        projectServiceModel.setEmployeeId(employeeId);
                        projectServiceModel.setProjectId(projectId);
                        projectServiceModel.setDateFrom(dateFrom);
                        projectServiceModel.setDateTo(dateTo);
                        projectServiceModel.setFileId(fileId);
                        this.analyzeFile.analyzeAndSaveFile(projectServiceModel);
                    }
                }
                List<ProjectViewModel> projectViewModels = this.analyzeFile.findTheDurationAndPairsOfProjects(fileId);
                modelAndView.addObject("projects",projectViewModels);
                modelAndView.setViewName("analyze_results");

            } catch (Exception ex) {
                throw new FileNotUploadedException();
            }
        }
        modelAndView.addObject("name",principal.getName());
        return modelAndView;
    }
    private boolean isANumber(String input){
        try{
            int value = Integer.parseInt(input);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    private void validateInputData(String empId, String projectId, String dateFrom){
        if(empId==null){
            throw new InputDataNotPresent("Employee id should be provided");
        }
        if(projectId==null){
            throw new InputDataNotPresent("Project id should be provided");
        }
        if(dateFrom==null){
            throw new InputDataNotPresent("DateFrom should be provided");
        }
    }
}
