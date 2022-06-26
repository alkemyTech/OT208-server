package com.alkemy.ong.dto.response.user;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasicUserDto {

	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String photo;
	@JsonProperty(value = "roles")
	private List<RoleEntityName> roleIdsRoleEntity;

	@Getter
	@Setter
	public static class RoleEntityName {

		private String name;
	}
}
