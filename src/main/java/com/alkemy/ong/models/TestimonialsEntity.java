package com.alkemy.ong.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Where(clause = "active = true")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE testimonials SET active = false WHERE id=?")
public class TestimonialsEntity implements Serializable {
    private static final long serialVersionUID = 641554778L;

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(nullable = false, length = 150)
    private String image;

    @Column(nullable = false, length = 150)
    private String content;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime created;

    private boolean active = Boolean.TRUE;
}
