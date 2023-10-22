package com.techelevator.green.service;

import com.techelevator.green.model.Project;
import com.techelevator.green.model.auth.ERole;
import com.techelevator.green.model.auth.User;
import com.techelevator.green.repository.ProjectRepository;
import com.techelevator.green.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    public List<Project> getAllProjects() { return projectRepository.findAll();}

    public Project getById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if(project.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found.");
        }
        return project.get();
    }


    public Project createProject(Project project, Principal principal) {
        Long userId = getUserId(principal);

        if(!userId.equals(project.getStudent().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to create project for another student.");
        }

        List<Project> prevProjects = projectRepository.findProjectsByStudent(project.getStudent());
        for (Project p: prevProjects) {
            if (project.getName().equals(p.getName())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This project name already exists for this student.");
            }
        }
        return projectRepository.save(project);
    }

    public Project updateProject(Long projectId, Project project, Principal principal) {
        Long userId = getUserId(principal);
        if (!isAdmin(principal) && !userId.equals(project.getStudent().getId())) {
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
        existingProject.setDescription(project.getDescription());

        return projectRepository.save(existingProject);
    }

    public void deleteProject(Long projectId, Principal principal) {
        Long userId = getUserId(principal);

        Optional<Project> optProject = projectRepository.findById(projectId);
        if (optProject.isEmpty()) return;
        if (!isAdmin(principal) && !userId.equals(optProject.get().getStudent().getId())) {
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

    private boolean isAdmin(Principal principal) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Logged in user does not exist.");
        }

        return user.get().getRoles().stream().anyMatch(r -> r.getName().equals(ERole.ROLE_ADMIN));
    }
}
