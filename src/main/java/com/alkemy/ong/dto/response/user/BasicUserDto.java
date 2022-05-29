package com.alkemy.ong.dto.response.user;

import java.time.LocalDateTime;
import java.util.List;

import com.alkemy.ong.models.RoleEntity;

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

    private List<RoleEntity> roleIds;

    private LocalDateTime timestamps;

    private Boolean softDelete;
}
