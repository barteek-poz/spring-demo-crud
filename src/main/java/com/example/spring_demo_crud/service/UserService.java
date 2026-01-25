package com.example.spring_demo_crud.service;

import com.example.spring_demo_crud.dto.TravelDto;
import com.example.spring_demo_crud.dto.UserResponseDto;
import com.example.spring_demo_crud.dto.UserUpdateDto;
import com.example.spring_demo_crud.entity.Travel;
import com.example.spring_demo_crud.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    public List<User> getAllUsersEntity();
    public List<UserResponseDto> getAllUsers();
    public User getUserById(int userId);
    public UserResponseDto getUserByIdDto(int userId);
    public ResponseEntity<String> patchUser(int userId, UserUpdateDto dto);
    public ResponseEntity<String> putUser(int userId, User user);
    public ResponseEntity<String> createUser(User user);
    public User saveUser(User user);
    public void deleteUser(int userId);
    public void addTravel(int userId, TravelDto travel);
}
