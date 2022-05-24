/*COMO usuario administrador
  QUIERO gestionar las imágenes
  PARA modificar eficientemente el contenido visual

   Criterios de aceptación:

   Los Slides de la pantalla de inicio serán gestionados de forma dinámica por el administrador del sitio.
   Los mismos tendrán como campos imageUrl, text, order y organizationId (ya que pertenecerán una ONG).
*/
package com.alkemy.ong.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/*
 * @author Eduardo Sanchez <https://github.com/EdwardDavys/>
 */

@Entity
@Table(name = "slides")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SlideEntity implements Serializable {

    private static final long serialVersionUID =1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(nullable = false,length = 80)
    private String imageUrl;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String text;

    @Column(name = "orders",nullable = false)
    private Integer order;

    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    private OrganizationEntity organizationEntityId;


}
