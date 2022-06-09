/*
* Ticket OT208-21
* COMO desarollador
* QUIERO agregar la entidad Activity
* PARA representar en la implementacion la estructura de datos
*
* Criterios de Aceptacion:
* Nombre de la tabla: activities, Campos:
* name: VARCHAR NOT NULL
* content: TEXT NOT NULL
* image: VARCHAR NOT NULL
* timestamps y softDelete
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
@Table(name = "activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE activities SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete = false")
@EntityListeners(AuditingEntityListener.class)
public class ActivityEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false, length = 255)
    private String image;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime timestamps;

    @Column(nullable = false)
    private Boolean softDelete = Boolean.FALSE;

}
