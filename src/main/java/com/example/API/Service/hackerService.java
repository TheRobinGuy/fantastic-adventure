package com.example.API.Service;

import com.example.API.Dto.hackerDto;
import com.example.API.Entity.hackerEntity;
import com.example.API.Repository.hackerRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

@Named
public class hackerService {

    final private hackerRepository hackerRepository;

    @Inject
    public hackerService(final hackerRepository hackerRepository) {
        this.hackerRepository = hackerRepository;
    }

    public hackerDto getUserById(final Integer id){
        Optional<hackerEntity> returnedUser = hackerRepository.findById(id);
        if (returnedUser.isPresent()){
            hackerEntity hackerEntity = returnedUser.get();
            return new hackerDto.Builder()
                    .withId(hackerEntity.getId())
                    .withFirstName(hackerEntity.getFirstName())
                    .withUserName(hackerEntity.getHackerName())
                    .withSurName(hackerEntity.getSurName())
                    .withGender(hackerEntity.getGender())
                    .build();
        }
        else{
            return new hackerDto.Builder().build();
        }
    }

    public hackerDto createUser(final hackerDto hackerDto){
        hackerEntity hackerEntity = new hackerEntity.Builder()
                .withUserName(hackerDto.getHackerName())
                .withFirstName(hackerDto.getFirstName())
                .withSurName(hackerDto.getSurName())
                .withGender(hackerDto.getGender())
                .build();

        hackerEntity returned = hackerRepository.save(hackerEntity);

        return new hackerDto.Builder()
                .withId(returned.getId())
                .withUserName(returned.getHackerName())
                .withFirstName(returned.getFirstName())
                .withSurName(returned.getSurName())
                .withGender(returned.getGender())
                .build();
    }
}
