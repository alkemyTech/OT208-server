package com.alkemy.ong.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {

	@Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(length = 36)
	private String id;
	
	@Column(nullable = false)
	private UserEntity userId;
	
	@Column(nullable = false, length = 255)
	private String body;
	
	@Column(nullable = false)
	private NewsEntity newsId;
}
