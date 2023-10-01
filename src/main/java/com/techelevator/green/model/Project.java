package com.techelevator.green.model;

import jakarta.persistence.*;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

    private String url;

    private String name;

    private String description;

    public Student getStudentData() {
        return student;
    }

    public void setStudentData(Student student) {
        this.student = student;
    }
}
