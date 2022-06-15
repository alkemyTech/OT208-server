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
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "testimonials")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE testimonials SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete = false")
public class TestimonialsEntity implements Serializable {

    private static final long serialVersionUID = 641554778L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(length = 36)
    private String id;

    @NotNull
    @Column(length = 100)
    private String name;

    @Column(length = 255)
    private String image;

    @Column(length = 150)
    private String content;

    @CreatedDate
    private LocalDateTime timestamps;

    private Boolean softDelete = Boolean.FALSE;
}
