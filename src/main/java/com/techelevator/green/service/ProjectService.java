package com.techelevator.green.service;

import com.techelevator.green.model.Project;
import com.techelevator.green.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    public List<Project> getAllProjects() { return projectRepository.findAll();}

    public List<Project> getProjectsByName(String name) {
        return projectRepository.findProjectByProjectName(name);
    }

    public List<Project> getProjectsByStudent(String student) {
        return projectRepository.findProjectByStudent(student);
    }

    public List<Project> getProjectsByNameAndStudent(String name, String student) {
        return projectRepository.findProjectByNameAndStudent(name, student);
    }
}
