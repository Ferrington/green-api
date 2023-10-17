package com.techelevator.green.controller;

import com.techelevator.green.model.auth.Role;
import com.techelevator.green.model.auth.User;
import com.techelevator.green.payload.request.SignupRequest;
import com.techelevator.green.payload.request.UserPatchRequest;
import com.techelevator.green.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(path = "/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/roles")
    public List<String> getRoles() {
        return userService.getRoles();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userService.createUser(signUpRequest);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @Valid @RequestBody UserPatchRequest user) {
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
