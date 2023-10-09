package com.techelevator.green.controller;

import com.techelevator.green.model.Project;
import com.techelevator.green.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/projects")
public class ProjectController {

    ProjectService projectService;

    //see all projects
    /*
    so my idea here is that this page is going to have a bunch of divs that will represent projects and we can look up projects by student name,
    project name, or see a full list
     */
    @GetMapping
    public List<Project> listProjects(@RequestParam String name, @RequestParam String student) {
        if (!name.isEmpty() && student.isEmpty()) {
            return projectService.getProjectsByName(name);
        } else if (name.isEmpty() && !student.isEmpty()) {
            return projectService.getProjectsByStudent(student);
        } else if (!name.isEmpty() && !student.isEmpty()) {
            return projectService.getProjectsByNameAndStudent(name, student);
        }
        return projectService.getAllProjects();
    }

    //I also want a further endpoint to get more information about a particular project.....maybe?

}
