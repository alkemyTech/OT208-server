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
@Table(name = "organizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE organizations SET softDelete = true WHERE id=?")
@Where(clause = "softDelete = false")
@EntityListeners(AuditingEntityListener.class)
public class OrganizationEntity implements Serializable {
    
    private static final long serialVersionUID = 8111554778L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String name;
    
    @Column(nullable = false, length = 80)
    private String image;
    
    @Column(nullable = true, length = 150)
    private String address;
            
    @Column(nullable = true, length = 150)
    private Integer phone;
            
    @Column(nullable = false, length = 80)
    private String email;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String welcomeText;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String aboutUsText;
    
    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime timestamps;
   
    private boolean softDelete = Boolean.FALSE;
    
}
