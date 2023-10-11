package com.techelevator.green.service;

import com.techelevator.green.model.Project;
import com.techelevator.green.model.auth.User;
import com.techelevator.green.repository.ProjectRepository;
import com.techelevator.green.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

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


    public Project createProject(Project project) {
        //so, I'm not doing any checks here because a student can have many projects
        //I could maybe check to see if a project with the same name already exists and is
        //associated with that student, to throw an error.
        //It doesn't feel entirely necessary to me? Happy to add it if someone wants it implemented!
        return projectRepository.save(project);
    }

    public Project updateProject(Long projectId, Project project, Principal principal) {
        Long userId = getUserId(principal);
        if (!userId.equals(project.getStudent().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to modify project.");
        }

        Optional<Project> optProject = projectRepository.findById(projectId);
        if (optProject.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found.");
        }

        Project existingProject = optProject.get();
        existingProject.setUrl(project.getUrl());
        existingProject.setStudent(project.getStudent());
        existingProject.setName(project.getName());
        existingProject.setId(project.getId());
        existingProject.setDescription(project.getDescription());

        return projectRepository.save(existingProject);
    }

    public void deleteProject(Long projectId, Principal principal) {
        Long userId = getUserId(principal);

        Optional<Project> optProject = projectRepository.findById(projectId);
        if(!userId.equals(optProject.get().getStudent().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to delete this project.");
        }

        projectRepository.deleteById(projectId);
    }



    private Long getUserId(Principal principal) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Logged in user does not exist.");
        }
        return user.get().getId();
    }


}
