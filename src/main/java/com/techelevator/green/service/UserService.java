package com.techelevator.green.service;

import com.techelevator.green.model.Student;
import com.techelevator.green.model.auth.ERole;
import com.techelevator.green.model.auth.Role;
import com.techelevator.green.model.auth.User;
import com.techelevator.green.payload.request.SignupRequest;
import com.techelevator.green.payload.request.UserPatchRequest;
import com.techelevator.green.payload.response.MessageResponse;
import com.techelevator.green.repository.RoleRepository;
import com.techelevator.green.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<String> getRoles() {
        return List.of("student", "admin");
    }


    public User updateUser(Long userId, UserPatchRequest user) {
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }

        User existingUser = optUser.get();
        existingUser.setId(userId);
        existingUser.setUsername(user.getUsername());
        existingUser.setRoles(mapRoles(user.getRoles()));

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User createUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Username is already taken.");
        }

        User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = mapRoles(strRoles);
        user.setRoles(roles);

        if (strRoles.contains("student")) {
            Student student = new Student();
            user.setStudent(student);
            student.setUser(user);
        }

        return userRepository.save(user);
    }

    private Set<Role> mapRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch (role) {
                case "student" -> {
                    Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(studentRole);
                }
                case "admin" -> {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                }
                default -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role.");
                }
            }
        });

        return roles;
    }
}
