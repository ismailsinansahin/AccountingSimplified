package com.cydeo.accountingsimplified.mapper;

import com.cydeo.accountingsimplified.entity.User;
import com.cydeo.accountingsimplified.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User convertToEntity(UserDto dto){
        return modelMapper.map(dto,User.class);

    }

    public UserDto convertToDto(User entity){
        return modelMapper.map(entity,UserDto.class);
    }

}
