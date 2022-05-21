/*
Ticket OT208-22
COMO desarrollador QUIERO agregar la entidad Member
PARA representar en la implementación la estructura de datos

Criterios de aceptación: 
Nombre de tabla: members. Campos:
name: VARCHAR NOT NULL
facebookUrl: VARCHAR NULLABLE
instagramUrl: VARCHAR NULLABLE
linkedinUrl: VARCHAR NULLABLE
image: VARCHAR NOT NULL
description: VARCHAR NULLABLE
timestamps y softDelete
 */
package com.alkemy.ong.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author Adrian E. Camus <https://acamus79.github.io/>
 */
@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE members SET active = false WHERE id=?")
@Where(clause = "active = true")
@EntityListeners(AuditingEntityListener.class)
public class MemberEntity implements Serializable {
    
    private static final long serialVersionUID = 641554778L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String name;
    
    @Column(nullable = true, length = 150)
    private String facebookUrl;
    
    @Column(nullable = true, length = 150)
    private String instagramUrl;
        
    @Column(nullable = true, length = 150)
    private String linkedinUrl;
    
    @Column(nullable = false, length = 80)
    private String image;
    
    @Column(nullable = true, length = 255)
    private String description;
   
    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime created;
   
    private boolean active = Boolean.TRUE;
    
}
