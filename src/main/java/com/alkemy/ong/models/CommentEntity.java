package com.alkemy.ong.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Comments")
public class CommentEntity {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    @Column(nullable = false, length = 255)
    private String body;

    @ManyToOne
    @JoinColumn(name = "news_id")
    private NewsEntity newsId;

    @CreatedDate
    private LocalDateTime timestamps;

}
