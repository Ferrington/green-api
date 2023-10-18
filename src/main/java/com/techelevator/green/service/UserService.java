package com.techelevator.green.service;

import com.techelevator.green.model.Student;
import com.techelevator.green.model.auth.ERole;
import com.techelevator.green.model.auth.Role;
import com.techelevator.green.model.auth.User;
import com.techelevator.green.payload.request.UserPatchRequest;
import com.techelevator.green.repository.RoleRepository;
import com.techelevator.green.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.*;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    public List<User> getAllUsers(boolean isStudent) {
        if (isStudent) {
            return userRepository.findByStudentIsNotNull();
        }
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

    public User createUser(UserPatchRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Username is already taken.");
        }

        User user = new User(
                signUpRequest.getUsername(),
                encoder.encode(generatePassword()),
                generateUUID()
        );

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

    private String generatePassword() {
        return new SecureRandom().ints(10, 33, 122).collect(StringBuilder::new,
                        StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
