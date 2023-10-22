package com.techelevator.green.controller;

import com.techelevator.green.model.auth.User;
import com.techelevator.green.payload.request.SetPasswordRequest;
import com.techelevator.green.payload.request.UserPatchRequest;
import com.techelevator.green.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public List<User> getAllUsers(@RequestParam(name="student", defaultValue = "false") String isStudent) {
        return userService.getAllUsers(isStudent.equals("true"));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("permitAll()")
    public User getUserByUUID(@PathVariable String uuid) {
        return userService.getUserByUUID(uuid);
    }

    @GetMapping("/roles")
    public List<String> getRoles() {
        return userService.getRoles();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody UserPatchRequest signUpRequest) {
        return userService.createUser(signUpRequest);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @Valid @RequestBody UserPatchRequest user) {
        return userService.updateUser(userId, user);
    }

    @PreAuthorize("permitAll()")
    @PatchMapping("/setPassword/{uuid}")
    @ResponseStatus(value = HttpStatus.OK)
    public void setPassword(@PathVariable String uuid, @RequestBody SetPasswordRequest setPasswordRequest) {
        userService.setPassword(uuid, setPasswordRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
