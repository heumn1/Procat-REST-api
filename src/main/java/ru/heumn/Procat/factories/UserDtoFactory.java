package ru.heumn.Procat.factories;


import org.springframework.stereotype.Component;
import ru.heumn.Procat.storage.dto.UserDto;
import ru.heumn.Procat.storage.entities.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDtoFactory {

    public UserDto makeUserDto(UserEntity userEntity){
        return UserDto.builder()
                .id(userEntity.getId())
                .dateCreate(userEntity.getDateCreate())
                .lastLogin(userEntity.getLastLogin())
                .name(userEntity.getName())
                .roles(userEntity.getRoles())
                .active(userEntity.getActive())
                .lastName(userEntity.getLastName())
                .patronymic(userEntity.getPatronymic())
                .build();
    }

    public List<UserDto> makeUserDtoList(List<UserEntity> userEntityList){
        return userEntityList.stream().map(this::makeUserDto).collect(Collectors.toList());
    }

    public UserEntity makeUserEntity(UserDto userDto){
        return UserEntity.builder()
                .id(userDto.getId())
                .dateCreate(userDto.getDateCreate())
                .lastLogin(userDto.getLastLogin())
                .name(userDto.getName())
                .active(userDto.getActive())
                .lastName(userDto.getLastName())
                .patronymic(userDto.getPatronymic())
                .password(userDto.getPassword())
                .login(userDto.getLogin())
                .roles(userDto.getRoles())
                //.id(null)
                .build();
    }
}
