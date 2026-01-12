package com.example.spring_demo_crud.controller;

import com.example.spring_demo_crud.dto.UserResponseDto;
import com.example.spring_demo_crud.dto.UserUpdateDto;
import com.example.spring_demo_crud.entity.User;
import com.example.spring_demo_crud.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsersController {

    private UserService userService;

    public UsersController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserResponseDto> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/users/{userId}")
    public UserResponseDto getUserById(@PathVariable int userId){
        return userService.getUserByIdDto(userId);
    }

    @PostMapping("/users/register")
    public ResponseEntity<String> createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<String> patchUser(@PathVariable int userId, @RequestBody UserUpdateDto dto){
        return userService.patchUser(userId, dto);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<String> putUser(@PathVariable int userId, @RequestBody User user){
        return userService.putUser(userId, user);
    }

    @DeleteMapping("users/{userId}")
    public void deleteUser(@PathVariable int userId){
        userService.deleteUser(userId);
    }

}
