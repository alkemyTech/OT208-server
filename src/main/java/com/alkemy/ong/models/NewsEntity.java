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

@Entity
@Table(name="news")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE news SET active = false WHERE id=?")
@Where(clause = "active=true")
@EntityListeners(AuditingEntityListener.class)
public class NewsEntity implements Serializable {


    private static final long serialVersionUID = 2888946607675320668L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private  String content;

    @Column(nullable = false, length = 150)
    private String image;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name="category_id")
    private CategoryEntity categoryId;

    @Column(nullable = false)
    private boolean active = Boolean.TRUE;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime created;
}
