package com.techelevator.green.controller;

import com.techelevator.green.model.Student;
import com.techelevator.green.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/api/student")
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

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Student createStudent(@RequestBody Student student, Principal principal) {
        return studentService.createStudent(student, principal);
    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public Student updateStudent(@PathVariable Long studentId, @RequestBody Student student, Principal principal) {
        return studentService.updateStudent(studentId, student, principal);
    }

    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long studentId, Principal principal) {
        studentService.deleteStudent(studentId, principal);
    }
}
