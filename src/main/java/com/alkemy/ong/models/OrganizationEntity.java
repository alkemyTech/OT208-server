/*
Ticket OT208-15
COMO desarrollador QUIERO agregar la entidad OrganizationEntity
PARA representar en la implementación la estructura de datos

Criterios de aceptación: 
Nombre de tabla: organizations. Los campos son:
name: VARCHAR NOT NULL
image: VARCHAR NOT NULL
address: VARCHAR NULLABLE
phone: INTEGER NULLABLE
email: VARCHAR NOT NULL
welcomeText: TEXT NOT NULL
aboutUsText: TEXT NULLABLE
timestamps y softDelete

 */
package com.alkemy.ong.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "organizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE organizations SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete = false")
@EntityListeners(AuditingEntityListener.class)
public class OrganizationEntity implements Serializable {

    private static final long serialVersionUID = 8111554778L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 150)
    private String image;

    @Column(length = 150)
    private String address;

    @Column(length = 30)
    private Integer phone;

    @Column(nullable = false, length = 80)
    private String email;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String welcomeText;

    @Column(columnDefinition = "TEXT")
    private String aboutUsText;

    @Column(length = 100)
    private String facebookUrl;

    @Column(length = 100)
    private String linkedinUrl;

    @Column(length = 100)
    private String instagramUrl;

    @CreatedDate
    private LocalDateTime timestamps;

    private Boolean softDelete = Boolean.FALSE;
}
