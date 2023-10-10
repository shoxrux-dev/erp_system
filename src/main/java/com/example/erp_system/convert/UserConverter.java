package com.example.erp_system.convert;

import com.example.erp_system.dto.user.UserCreateRequestDto;
import com.example.erp_system.dto.user.UserResponseDto;
import com.example.erp_system.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserConverter {
    public User convertToDomain(UserCreateRequestDto userCreateRequestDto){
        return User.builder()
                .username(userCreateRequestDto.getUsername())
                .password(userCreateRequestDto.getPassword())
                .build();
    }

    public UserResponseDto from(User user){
        return UserResponseDto.builder()
                .username(user.getUsername())
                .roles(user.getRoles())
                .image(user.getImage())
                .build();
    }

}
