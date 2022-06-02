package com.alkemy.ong.dto.response.user;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicUserDto {

	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String photo;
	private List<RoleEntityName> roleIdsRoleEntityName;

	@Getter
	@Setter
	public static class RoleEntityName {

		private String name;
	}
}
