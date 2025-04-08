package com.example.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobhunter.domain.User;
import com.example.jobhunter.service.UserService;
import com.example.jobhunter.util.error.IdInvalidException;

import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/users")

public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // create one user
    @PostMapping("/create")
    public ResponseEntity<User> createNewUser(@RequestBody User postManUser) {
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassWord());
        postManUser.setPassWord((hashPassword));
        User newUser = userService.handleSaveUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }


    // delete by id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> DeleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if (id > 1500) {
            throw new IdInvalidException("ID khong lon hon 1501");
        }

        userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> fetchUserById(@PathVariable("id") long id) {
        User newUser = userService.fetchOneUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> fetchAllUser() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.fetchAllUser());
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUserById(@RequestBody User user) {
        User ericUser = userService.handleUpdateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(ericUser);
    }
}
