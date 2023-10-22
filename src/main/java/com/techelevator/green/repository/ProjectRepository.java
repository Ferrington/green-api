package com.techelevator.green.repository;

import com.techelevator.green.model.Project;
import com.techelevator.green.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findProjectsByStudent(Student student);

}
