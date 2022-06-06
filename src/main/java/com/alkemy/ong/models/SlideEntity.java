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
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "slides")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SlideEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 150)
    private String imageUrl;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(name = "orders", nullable = false)
    private Integer order;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private OrganizationEntity organizationEntityId;

}
