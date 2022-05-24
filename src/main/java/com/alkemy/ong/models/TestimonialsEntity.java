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

/**
 * @author nagredo
 * @project OT208-server
 * @class Testimonials
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "testimonials")
@Where(clause = "softDelete = false")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE testimonials SET softDelete = false WHERE id=?")
public class TestimonialsEntity implements Serializable {
    private static final long serialVersionUID = 641554778L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(length = 36)
    private String id;

    @Column(length = 100)
    private String name;

    @Column(nullable = false, length = 150)
    private String image;

    @Column(nullable = false, length = 150)
    private String content;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime timestamps;

    private boolean softDelete = Boolean.FALSE;
}
