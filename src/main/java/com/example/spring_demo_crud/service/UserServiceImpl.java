package com.example.spring_demo_crud.service;

import com.example.spring_demo_crud.dao.UserRepository;
import com.example.spring_demo_crud.dto.TravelDto;
import com.example.spring_demo_crud.dto.UserResponseDto;
import com.example.spring_demo_crud.dto.UserUpdateDto;
import com.example.spring_demo_crud.entity.Travel;
import com.example.spring_demo_crud.entity.User;
import com.example.spring_demo_crud.exception.UserAlreadyExistsException;
import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static UserResponseDto UserDtoMapper(User user) {
        List<Travel> userTravels = user.getTravels();
        List<TravelDto> travels = new ArrayList<>();

        for(Travel travel : userTravels){
            travels.add(new TravelDto(travel.getDestination(), travel.getStartDate(), travel.getEndDate()));
        }

        return new UserResponseDto(user.getFirstName(), user.getEmail(), travels);
    }

    @Override
    public List<User> getAllUsersEntity(){
       return userRepository.findAll();
    };

    public List<UserResponseDto> getAllUsers(){
        List<UserResponseDto> users = new ArrayList<>();
        List<User> usersEntity = getAllUsersEntity();
        for(User user : usersEntity){
            users.add(UserDtoMapper(user));
        }
        return users;
    }

    @Override
    public User getUserById(int userId){
        Optional<User> result = userRepository.findWithTravelsById(userId);
        User user = null;
        if(result.isPresent()) {
            user = result.get();
        } else {
            throw new RuntimeException("User with given ID was not found");
        }
        System.out.println(user);
        return user;
    }

    @Override
    public UserResponseDto getUserByIdDto(int userId){
        User user = getUserById(userId);
        return UserDtoMapper(user);
    }

    @Override
    @Transactional
    public ResponseEntity<String> patchUser(int userId, UserUpdateDto dto){
        User dbUser = getUserById(userId);
        if(dto.getEmail() != null) {
            dbUser.setEmail(dto.getEmail());
        }
        if(dto.getName() != null) {
            dbUser.setFirstName(dto.getName());
        }
        return ResponseEntity.status(HttpStatus.OK).body("User was updated");
    }

    @Override
    @Transactional
    public ResponseEntity<String> putUser(int userId, User user){
        User dbUser = getUserById(userId);
        user.setId(userId);
        saveUser(user);
        return ResponseEntity.status(HttpStatus.OK).body("User was updated");
    }

        @Override
        public ResponseEntity<String> createUser(User user){
           try {
               userRepository.save(user);
               return ResponseEntity.status(HttpStatus.CREATED).body("User was created");
           } catch(DataIntegrityViolationException ex){
               throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
           }
        }

    @Override
    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(int userId){
        userRepository.deleteById(userId);
    }

    @Override
    public void addTravel(int userId, TravelDto travelDto) {
        if(travelDto == null) {
            throw new RuntimeException("Invalid travel data");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Travel travel = new Travel(
                travelDto.destination(),
                travelDto.startDate(),
                travelDto.endDate()
        );
        user.addTravel(travel);
    }
}
