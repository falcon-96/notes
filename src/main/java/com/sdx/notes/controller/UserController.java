package com.sdx.notes.controller;

import com.sdx.notes.model.User;
import com.sdx.notes.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createNewUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable(name = "id") long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUser(id));
    }

    @PutMapping
    public ResponseEntity<User> updateUserDetails(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
