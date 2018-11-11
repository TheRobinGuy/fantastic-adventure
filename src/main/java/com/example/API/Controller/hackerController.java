package com.example.API.Controller;

import com.example.API.Dto.hackerDto;
import com.example.API.Service.hackerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RequestMapping(value = "/api/hacker")
@RestController
public class hackerController {

    final private hackerService hackerService;

    @Inject
    public hackerController(hackerService hackerService) {
        this.hackerService = hackerService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<hackerDto> getUserById(@PathVariable("id") final Integer id){
        return new ResponseEntity<hackerDto>(hackerService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<hackerDto>createUser(@RequestBody final hackerDto hackerDto){
        hackerDto createdUser = hackerService.createUser(hackerDto);
        return new ResponseEntity<hackerDto>(new hackerDto.Builder()
                .withFirstName(createdUser.getFirstName())
                .withSurName(createdUser.getSurName())
                .withGender(createdUser.getGender())
                .withUserName(createdUser.getHackerName())
                .withId(createdUser.getId())
                .build(), HttpStatus.CREATED);
    }
}
