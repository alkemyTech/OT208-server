package com.alkemy.ong.services.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.alkemy.ong.dto.response.user.BasicUserDto;
import com.alkemy.ong.models.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserMapper {

	private final ModelMapper mapper;

	public BasicUserDto mapperUserEntityToBasicUserDto(UserEntity userEntity) {
		return mapper.map(userEntity, BasicUserDto.class);
	}
}
