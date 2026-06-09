package io.github.pluton33.ezgloszenie.data;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toDto(UserEntity userEntity);
    UserEntity toEntity(User userDTO);
    UserEntity toEntity(RegisterUserRequest userRequest);
}
