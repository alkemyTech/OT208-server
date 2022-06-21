/*
Ticket OT208-18

COMO desarrollador QUIERO agregar la entidad Category
PARA representar en la implementación la estructura de datos

Criterios de aceptación: 
Nombre de tabla: categories. Los campos son:
name: VARCHAR NOT NULL
description: VARCHAR NULLABLE
image: VARCHAR NULLABLE
timestamps y softDelete
 */
package com.alkemy.ong.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE categories SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete = false")
@EntityListeners(AuditingEntityListener.class)
public class CategoryEntity implements Serializable {

    private static final long serialVersionUID = 741558L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 50)
    private String name;

    private String description;

    @Column(length = 255)
    private String image;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime timestamps;

    private Boolean softDelete = Boolean.FALSE;

}
