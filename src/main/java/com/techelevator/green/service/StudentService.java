package com.techelevator.green.service;

import com.techelevator.green.model.Student;
import com.techelevator.green.model.auth.ERole;
import com.techelevator.green.model.auth.User;
import com.techelevator.green.repository.StudentRepository;
import com.techelevator.green.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserRepository userRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudent(long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found.");
        }

        return student.get();
    }

    public Student createStudent(Student student, Principal principal) {
        Long userId = getUserId(principal);
        Optional<Student> studentData = studentRepository.findById(userId);
        if (studentData.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student already exists.");
        }

        student.setId(userId);
        return studentRepository.save(student);
    }

    public Student updateStudent(Long studentId, Student student, Principal principal) {
        Long userId = getUserId(principal);

        if (!isAdmin(principal) && !userId.equals(studentId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to modify this student.");
        }

        Optional<Student> optStudent = studentRepository.findById(studentId);
        if (optStudent.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found.");
        }

        Student existingStudent = optStudent.get();
        existingStudent.setId(studentId);
        existingStudent.setFanPageUrl(student.getFanPageUrl());
        existingStudent.setFanPageTitle(student.getFanPageTitle());
        existingStudent.setFanPageDescription(student.getFanPageDescription());
        existingStudent.setGithubUrl(student.getGithubUrl());
        existingStudent.setProjects(student.getProjects());

        return studentRepository.save(existingStudent);
    }

    private Long getUserId(Principal principal) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Logged in user does not exist.");
        }
        return user.get().getId();
    }

    private boolean isAdmin(Principal principal) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Logged in user does not exist.");
        }

        return user.get().getRoles().stream().anyMatch(r -> r.getName().equals(ERole.ROLE_ADMIN));
    }
}
