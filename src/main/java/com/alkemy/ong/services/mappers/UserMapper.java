package com.alkemy.ong.services.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.alkemy.ong.dto.response.user.BasicUserDto;
import com.alkemy.ong.models.UserEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {

	private final ModelMapper mapper;

	public BasicUserDto mapperUserEntityToBasicUserDto(UserEntity user) {
		return mapper.map(user, BasicUserDto.class);
	}
		
}
