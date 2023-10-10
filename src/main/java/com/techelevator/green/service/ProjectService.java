package com.techelevator.green.service;

import com.techelevator.green.model.Project;
import com.techelevator.green.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    public List<Project> getAllProjects() { return projectRepository.findAll();}

    public List<Project> getProjectsByName(String name) {
        return projectRepository.findByNameIgnoreCase(name);
    }

    public List<Project> getProjectsByStudent(String student) {
        return projectRepository.findByStudent(student);
    }

    public List<Project> getProjectsByNameAndStudent(String name, String student) {
        return projectRepository.findByNameAndStudent(name, student);
    }

    public Project getById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if(project.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found.");
        }
        return project.get();
    }

    public Project createProject() {
        return null;
    }

}
