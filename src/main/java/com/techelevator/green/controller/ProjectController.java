package com.techelevator.green.controller;

import com.techelevator.green.model.Project;
import com.techelevator.green.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/api/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping
    @PreAuthorize("permitAll")
    public List<Project> listProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping(path="/{id}")
    @PreAuthorize("permitAll")
    public Project getProject(@PathVariable long id) {
        return projectService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @PutMapping(path="/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public Project updateProject(@PathVariable Long id, @RequestBody Project project, Principal principal) {
        return projectService.updateProject(id, project, principal);
    }

    @DeleteMapping(path="/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable Long id, Principal principal) {
        projectService.deleteProject(id, principal);
    }


}