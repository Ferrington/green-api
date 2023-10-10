package com.techelevator.green.repository;

import com.techelevator.green.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByNameIgnoreCase(String name);

    List<Project> findByStudent(String student);

    List<Project> findByNameAndStudent(String name, String student);
}
