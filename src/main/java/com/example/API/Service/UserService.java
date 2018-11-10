package com.example.API.Service;

import com.example.API.Dto.UserDto;
import com.example.API.Entity.UserEntity;
import com.example.API.Repository.UserRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

@Named
public class UserService {

    final private UserRepository userRepository;

    @Inject
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUserById(final Integer id){
        Optional<UserEntity> returnedUser = userRepository.findById(id);
        if (returnedUser.isPresent()){
            UserEntity userEntity = returnedUser.get();
            return new UserDto.Builder()
                    .withId(userEntity.getId())
                    .withFirstName(userEntity.getFirstName())
                    .withUserName(userEntity.getUserName())
                    .withSurName(userEntity.getSurName())
                    .withGender(userEntity.getGender())
                    .build();
        }
        else{
            return new UserDto.Builder().build();
        }
    }

    public UserDto createUser(final UserDto userDto){
        UserEntity userEntity = new UserEntity.Builder()
                .withUserName(userDto.getUserName())
                .withFirstName(userDto.getFirstName())
                .withSurName(userDto.getSurName())
                .withGender(userDto.getGender())
                .build();

        UserEntity returned = userRepository.save(userEntity);

        return new UserDto.Builder()
                .withId(returned.getId())
                .withUserName(returned.getUserName())
                .withFirstName(returned.getFirstName())
                .withSurName(returned.getSurName())
                .withGender(returned.getGender())
                .build();
    }
}
