package com.task.tracker.controller;

import java.util.*;

import com.task.tracker.entity.user.User;
import com.task.tracker.payload.request.SignupRequest;
import com.task.tracker.payload.request.UpdateUserRequest;
import com.task.tracker.payload.response.MessageResponse;
import com.task.tracker.servise.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        try {
            Pageable paging = PageRequest.of(page, size);
            Page<User> pageUsers = userService.findAll(paging);
            List<User> users = pageUsers.getContent();
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("users", users);
            response.put("currentPage", pageUsers.getNumber());
            response.put("totalItems", pageUsers.getTotalElements());
            response.put("totalPages", pageUsers.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{user_id}")
    public ResponseEntity<User> getUserById(@PathVariable("user_id") Long user_id) {
        Optional<User> userFromDB = userService.findById(user_id);

        return userFromDB.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).
                orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        user.setRoles(userService.getRoles(strRoles));
        userService.save(user);
        return ResponseEntity.ok(new MessageResponse("User added successfully!"));
    }

    @PutMapping("/users/{user_id}")
    public ResponseEntity<User> updateUser(@PathVariable("user_id") Long user_id,
                                           @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        Optional<User> userFromDB = userService.findById(user_id);

        if (userFromDB.isPresent()) {
            User user = userFromDB.get();
            user.setFirstName(updateUserRequest.getFirstName());
            user.setLastName(updateUserRequest.getLastName());
            Set<String> strRoles = updateUserRequest.getRole();
            user.setRoles(userService.getRoles(strRoles));
            return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("username") String username) {
        try {
            userService.deleteByUsername(username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/users")
    public ResponseEntity<HttpStatus> deleteAllUsers() {
        try {
            userService.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
}