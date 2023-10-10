package com.techelevator.green.controller;

import com.techelevator.green.model.Project;
import com.techelevator.green.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/projects")
public class ProjectController {

    ProjectService projectService;

    /*
    so my idea here is that this page is going to have a bunch of divs that will represent projects and we can look up projects by student name,
    project name, both?? (can remove) or see a full list

    Note to Sam: maybe also add a category for projects - so we can search by category.
    The OG idea of this site by Daniel was to display fan pages so it'd be fun to be able to just look at those
    Would need to consult with someone??? about category options - obvi
    portfolio and fanpage, not sure what else.
     */
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
    @PreAuthorize("isAuthenticated()")
    public Project getProject(@PathVariable Long id) {
        return projectService.getById(id);
    }


}
