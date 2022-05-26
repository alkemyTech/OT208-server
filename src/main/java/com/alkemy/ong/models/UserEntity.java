package com.alkemy.ong.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE users SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete = false")
public class UserEntity implements Serializable {

	private static final long serialVersionUID = -693893366266595654L;

	@Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(length = 36)
	private String id;

	@Column(nullable = false, length = 50)
	private String firstName;

	@Column(nullable = false, length = 50)
	private String lastName;

	@Column(unique = true, nullable = false, length = 255)
	private String email;

	@Column(nullable = false, length = 255)
	private String password;

	@Column(length = 255)
	private String photo;

	@Column(nullable = false)
	@ManyToMany
	@JoinTable(	name = "roles_users",
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<RoleEntity> roleIds;

	@CreatedDate
	private LocalDateTime timestamps;

	@Column(nullable = false)
	private Boolean softDelete = Boolean.TRUE;
}
