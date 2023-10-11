package com.techelevator.green.controller;

import com.techelevator.green.model.Project;
import com.techelevator.green.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/api/projects")
public class ProjectController {

    ProjectService projectService;

    @GetMapping
    @PreAuthorize("permitAll")
    public List<Project> listProjects(@RequestParam String name, @RequestParam String student) {
        if (!name.isEmpty() && !student.isEmpty()) {
            return projectService.getProjectsByNameAndStudent(name, student);
        } else if (name.isEmpty() && !student.isEmpty()) {
            return projectService.getProjectsByStudent(student);
        } else if (!name.isEmpty() && student.isEmpty()) {
            return projectService.getProjectsByName(name);
        }
        return projectService.getAllProjects();
    }

    //I also want an authorized endpoint where students can update, delete, create projects
    //will need to look into how this works, so they can only mess with their own, because it could be disastrous if,
    //say, I could update Christopher's projects or something. For now...will try these.
    //maybe I need to do an if statement that checks if the user's id doesn't match the project's student id
    //then throw an unauthorized exception if that's true???????????


    @GetMapping(path="/project/{id}")
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

    @PutMapping(path="/project/{id]")
    @PreAuthorize("hasRole('STUDENT')")
    public Project updateProject(@PathVariable Long id, @RequestBody Project project, Principal principal) {
        return projectService.updateProject(id, project, principal);
    }

    @DeleteMapping(path="/project/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public void deleteProject(@PathVariable Long id, Principal principal) {
        projectService.deleteProject(id, principal);
    }


}
