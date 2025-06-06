package com.hublocal.board.handler.mappers;

import com.hublocal.board.handler.entities.Users;
import com.hublocal.board.handler.model.UsersDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsersMapper {

    @Mapping(ignore = true, target = "announcements")
    Users toEntity(UsersDto usersDto);

    UsersDto toDto(Users users);
}