package com.techelevator.green.controller;

import com.techelevator.green.model.Student;
import com.techelevator.green.model.auth.User;
import com.techelevator.green.repository.StudentRepository;
import com.techelevator.green.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/student")
public class StudentController {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student, Principal principal) {
        Optional<User> user = userRepository.findByUsername(principal.getName());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
