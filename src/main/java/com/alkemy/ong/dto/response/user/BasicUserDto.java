package com.alkemy.ong.dto.response.user;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BasicUserDto {

	private String id;

	private String firstName;

	private String lastName;

	private String email;

	private String photo;

	private List<RoleNameDto> roleIdsRoleEntityName;
	
	@Setter
	@Getter
	public static class RoleNameDto {
		
		private String name;
	}
}
