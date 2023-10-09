package com.techelevator.green.repository;

import com.techelevator.green.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    //Create


    //Read
    @Override
    List<Project> findAll();

    List<Project> findProjectByProjectName(String projectName);

    List<Project> findProjectByStudent(String student);

    List<Project> findProjectByNameAndStudent(String name, String student);

    //Update


    //Delete


}
