package com.alkemy.ong.models;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
COMO desarrollador 
QUIERO agregar la entidad Role
PARA representar en la implementación la estructura de datos

Criterios de aceptación: 
Nombre de tabla: roles. Los campos son:
name: VARCHAR NOT NULL
description: VARCHAR NULLABLE
timestamps
*/

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
public class RoleEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 36)
	private Long id;
	
	@Column(nullable = false, length = 50)
	private String name;
	
	@Column(length = 50)
	private String description;
	
	@CreatedDate
	@Column(length = 50)
	private Timestamp timestamp;
	
}
