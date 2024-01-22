package com.example.employee.repository;

import com.example.employee.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity,String> {
    List<ProjectEntity>findByProjectIdAndFileId(int projectId, String fileId);
    List<ProjectEntity>findByFileId(String fileId);
}
