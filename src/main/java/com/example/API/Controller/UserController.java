package com.example.API.Controller;

import com.example.API.Dto.UserDto;
import com.example.API.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RequestMapping(value = "/api")
@RestController
public class UserController {

    final private UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") final Integer id){
        return new ResponseEntity<UserDto>(userService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto>createUser(@RequestBody final UserDto userDto){
        UserDto createdUser = userService.createUser(userDto);
        return new ResponseEntity<UserDto>(new UserDto.Builder()
                .withFirstName(createdUser.getFirstName())
                .withSurName(createdUser.getSurName())
                .withGender(createdUser.getGender())
                .withUserName(createdUser.getUserName())
                .withId(createdUser.getId())
                .build(), HttpStatus.CREATED);
    }
}
