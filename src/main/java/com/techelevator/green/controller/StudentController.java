package com.techelevator.green.controller;

import com.techelevator.green.model.Student;
import com.techelevator.green.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/api/student")
@CrossOrigin
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{studentId}")
    public Student getStudent(@PathVariable long studentId) {
        return studentService.getStudent(studentId);
    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public Student updateStudent(@PathVariable Long studentId, @RequestBody Student student, Principal principal) {
        return studentService.updateStudent(studentId, student, principal);
    }

}
